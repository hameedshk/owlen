param(
    [switch]$FullBuild = $false,
    [switch]$VerboseBuild = $false,
    [switch]$Reinstall = $false,
    [switch]$ClearData = $false,
	[switch]$RunTests = $false,
    [string[]]$LogTag,
    [switch]$Logcat = $false
)

# ================= CONFIG =================
$APP_ID        = "com.owlen"
$MAIN_ACTIVITY = ".MainActivity"
$STATE_HASH_FILE  = ".last_successful_state"
$STATE_FILES_FILE = ".last_successful_files"
$env:GRADLE_OPTS = "-Dorg.gradle.logging.level=info"
$BUILD_SUCCESS_FILE = ".last_build_success"
$BUILD_LOGS_DIR = "build-logs"
$SOURCE_EXTENSIONS  = @("*.kt","*.java","*.xml","*.gradle","*.kts","*.properties","*.json","*.pro")
$EXCLUDE_DIR_NAMES  = @("build",".gradle",".idea","build-logs")

$ProgressPreference = 'SilentlyContinue'
$ErrorActionPreference = 'Continue'

$forceFullBuild = $FullBuild.IsPresent
$verboseGradle  = $VerboseBuild.IsPresent
$forceReinstall = $Reinstall.IsPresent
$forceClearData = $ClearData.IsPresent
$attachLogcat = $Logcat.IsPresent
$LAST_DEVICE_FILE = ".last_device"

# ============== DEVICE HELPERS ==============
$scriptStart = Get-Date

# Validate flag combinations
if ($attachLogcat -and $RunTests) {
    Write-Error "❌ -Logcat and -RunTests are mutually exclusive (logcat blocks test execution)" -ErrorAction Stop
    exit 1
}

Import-Module BurntToast

function Format-Duration($ts) {
    return "{0:mm\:ss\.fff}" -f $ts
}

function Load-LastDevice {
    if (Test-Path $LAST_DEVICE_FILE) {
        $line = Get-Content $LAST_DEVICE_FILE -ErrorAction SilentlyContinue
        if ($line -match "^(.*):(\d+)$") {
            return @{ ip = $matches[1]; port = $matches[2] }
        }
    }
    return $null
}

function Save-LastDevice($ip, $port) {
    "$ip`:$port" | Out-File $LAST_DEVICE_FILE -Encoding ascii -Force
}

function Initialize-BuildLog {
    # Create build-logs directory if it doesn't exist
    if (-not (Test-Path $BUILD_LOGS_DIR)) {
        New-Item -ItemType Directory -Path $BUILD_LOGS_DIR -Force | Out-Null
    }

    # Generate timestamped log filename
    $timestamp = Get-Date -Format "yyyy-MM-dd_HH-mm-ss"
    $logFile = Join-Path $BUILD_LOGS_DIR "build_${timestamp}.log"

    # Initialize log with header
    $header = @(
        "==============================================="
        "Build Log — $(Get-Date -Format 'yyyy-MM-dd HH:mm:ss')"
        "Device: $env:ADB_DEVICE"
        "==============================================="
        ""
    ) -join "`n"

    $header | Out-File $logFile -Encoding utf8 -Force
    return $logFile
}

function Add-ToBuildLog {
    param([string]$LogFile, [string]$Message)

    if ($LogFile -and (Test-Path (Split-Path $LogFile))) {
        Add-Content $LogFile -Value $Message -Encoding utf8
    }
}

function Clean-OldBuildLogs {
    # Keep last 20 build logs, delete older ones
    $maxLogs = 20
    $logs = @(Get-ChildItem $BUILD_LOGS_DIR -Filter "build_*.log" -ErrorAction SilentlyContinue |
              Sort-Object CreationTime -Descending)

    if ($logs.Count -gt $maxLogs) {
        $logsToDelete = $logs | Select-Object -Skip $maxLogs
        foreach ($log in $logsToDelete) {
            Remove-Item $log.FullName -Force -ErrorAction SilentlyContinue
        }
    }
}

function Detect-DeviceType {
    param([string]$Serial)
    # USB serials are plain alphanumeric (no dots, colons, or network indicators)
    if ($Serial -match "^[A-Za-z0-9]+$") { return "USB" }
    return "WiFi"
}

function Get-ConnectedDevices {
    $devices = @()

    adb devices | ForEach-Object {
        $line = $_.Trim()

        if (-not $line) { return }
        if ($line -like "List of devices*") { return }

        if ($line -match "^(?<serial>\S+)\s+device$") {
            $serial = $matches['serial']
            $type = Detect-DeviceType $serial
            $devices += @{ serial = $serial; type = $type }
        }
    }

    return ,$devices
}

function Prompt-For-AdbTarget {
    $ip = Read-Host "Enter device IP address"
    $port = Read-Host "Enter adb port"

    if (-not $ip -or -not $port) {
        Write-Error "IP and port are required"
        exit 1
    }
    return @{ ip = $ip; port = [int]$port }
}

function Test-DeviceReachability {
    param([string]$Ip, [int]$TimeoutMs = 2000)

    Write-Host "🌐 Checking network connectivity to $Ip..." -ForegroundColor Cyan

    try {
        $result = Test-NetConnection -ComputerName $Ip -WarningAction SilentlyContinue `
                                     -ErrorAction SilentlyContinue -InformationLevel Quiet
        if ($result) {
            Write-Host "   ✅ Device IP is reachable" -ForegroundColor Green
            return $true
        }
    }
    catch {
        Write-Host "   ⚠️ Network test inconclusive" -ForegroundColor Yellow
    }

    Write-Host "   ❌ Device IP $Ip is NOT reachable" -ForegroundColor Red
    Write-Host "   Troubleshoot:" -ForegroundColor Yellow
    Write-Host "      • Check device is on the same network" -ForegroundColor Yellow
    Write-Host "      • Verify WiFi is connected on device" -ForegroundColor Yellow
    Write-Host "      • Confirm IP address is correct" -ForegroundColor Yellow
    Write-Host "      • Check firewall/router settings" -ForegroundColor Yellow
    return $false
}

function Try-Adb-Connect {
    param([string]$Ip, [int]$Port, [int]$MaxAttempts = 3)

    $target = "${Ip}:${Port}"

    # Pre-flight network check
    if (-not (Test-DeviceReachability $Ip)) {
        Write-Host "❌ Cannot proceed — device not reachable" -ForegroundColor Red
        return $false
    }

    Write-Host "🔄 Attempting WiFi connect to ${target}..." -ForegroundColor Cyan

    for ($attempt = 1; $attempt -le $MaxAttempts; $attempt++) {
        $output = adb connect $target 2>&1
        Write-Host "   $output" -ForegroundColor Gray

        # Wait for device to appear (longer on first attempt)
        $waitTime = if ($attempt -eq 1) { 3 } else { 2 }
        Start-Sleep -Seconds $waitTime

        # Check if connected
        $match = @(adb devices) | Select-String "$([Regex]::Escape($target))\s+device"
        if ($match) {
            Write-Host "✅ Connected to $target" -ForegroundColor Green
            return $true
        }

        if ($attempt -lt $MaxAttempts) {
            Write-Host "   Retry $attempt/$($MaxAttempts - 1)..." -ForegroundColor Yellow
        }
    }

    Write-Host "❌ Failed to connect to $target after $MaxAttempts attempts" -ForegroundColor Red
    Write-Host "   This usually means:" -ForegroundColor Yellow
    Write-Host "      • Device pairing is stale (unpair & re-pair)" -ForegroundColor Yellow
    Write-Host "      • Port number is incorrect (try other common ports like 5555)" -ForegroundColor Yellow
    Write-Host "      • ADB daemon on device crashed (toggle WiFi off/on)" -ForegroundColor Yellow
    return $false
}

function Try-Adb-Recover-Offline {
    param([string]$Device)
    Write-Host "⚠️ Device offline — attempting recovery..." -ForegroundColor Yellow

    adb disconnect $Device | Out-Null
    Start-Sleep -Seconds 1

    # If it's a WiFi device, try reconnect
    if ($Device -match "^(\d+\.\d+\.\d+\.\d+):(\d+)$") {
        $ip = $matches[1]
        $port = $matches[2]
        Start-Sleep -Seconds 2
        return (Try-Adb-Connect $ip $port -MaxAttempts 2)
    }

    # USB device — just wait for it
    Write-Host "⏳ Waiting for USB device to be recognized..." -ForegroundColor Yellow
    Start-Sleep -Seconds 3

    $match = @(adb devices) | Select-String "$([Regex]::Escape($Device))\s+device"
    return ($null -ne $match)
}

function Get-DeviceStorage {
    param([string]$Device)

    try {
        $output = adb -s $Device shell df -h /data 2>&1
        # Parse: Filesystem        Size  Used Avail Use% Mounted on
        # e.g:  /dev/block/mmcblk0p6 113G  89G   24G  79% /data
        $lines = @($output | Where-Object { $_ -match "^\/" })

        if ($lines.Count -gt 0) {
            $parts = $lines[0] -split '\s+' | Where-Object { $_ }
            if ($parts.Count -ge 4) {
                $avail = $parts[3]  # Avail column
                return $avail
            }
        }
    }
    catch {
        Write-Host "   ⚠️ Could not determine storage" -ForegroundColor Yellow
    }

    return $null
}

function Get-DeviceBattery {
    param([string]$Device)

    try {
        $output = adb -s $Device shell dumpsys battery 2>&1
        $level = ($output | Select-String "level:\s+(\d+)" | ForEach-Object { $_.Matches.Groups[1].Value }) | Select-Object -First 1
        $temp = ($output | Select-String "temperature:\s+(\d+)" | ForEach-Object { $_.Matches.Groups[1].Value }) | Select-Object -First 1
        $status = ($output | Select-String "status:\s+(\w+)" | ForEach-Object { $_.Matches.Groups[1].Value }) | Select-Object -First 1

        if ($level) {
            return @{ level = [int]$level; temp = if ($temp) { [int]$temp / 10 } else { $null }; status = $status }
        }
    }
    catch {
        Write-Host "   ⚠️ Could not determine battery status" -ForegroundColor Yellow
    }

    return $null
}

function Run-DevicePreflightChecks {
    param([string]$Device)

    Write-Host "🔍 Running device pre-flight checks..." -ForegroundColor Cyan
    $passed = $true

    # Check storage
    $storage = Get-DeviceStorage $Device
    if ($storage) {
        Write-Host "   💾 Storage available: $storage" -ForegroundColor Green

        # Parse storage value (could be "24G", "500M", etc.)
        if ($storage -match "^(\d+)([KMG])") {
            $size = [int]$matches[1]
            $unit = $matches[2]
            $sizeInMB = switch ($unit) {
                "K" { $size / 1024 }
                "M" { $size }
                "G" { $size * 1024 }
            }

            if ($sizeInMB -lt 500) {
                Write-Host "   ❌ CRITICAL: Less than 500MB free! Available: $storage" -ForegroundColor Red
                $passed = $false
            }
        }
    }

    # Check battery
    $battery = Get-DeviceBattery $Device
    if ($battery) {
        $level = $battery.level
        $status = $battery.status
        $temp = $battery.temp

        if ($level -lt 20) {
            Write-Host "   ⚠️ Low battery: $level% ($status)" -ForegroundColor Yellow
        }
        else {
            Write-Host "   🔋 Battery: $level% ($status)" -ForegroundColor Green
        }

        if ($temp -and $temp -gt 45) {
            Write-Host "   ⚠️ Device hot: ${temp}°C (may throttle)" -ForegroundColor Yellow
        }
    }

    # Check ADB responsiveness
    $adbCheck = adb -s $Device shell echo "ok" 2>&1
    if ($adbCheck -match "ok") {
        Write-Host "   ✅ ADB responsive" -ForegroundColor Green
    }
    else {
        Write-Host "   ⚠️ ADB slow or unresponsive" -ForegroundColor Yellow
    }

    if (-not $passed) {
        Write-Host "❌ Pre-flight check FAILED — address critical issues" -ForegroundColor Red
        return $false
    }

    Write-Host "✅ All pre-flight checks passed" -ForegroundColor Green
    return $true
}

function Get-Device-Status {
    param([string]$Serial)

    $raw = @(adb devices)

    # Check for specific device if provided
    if ($Serial) {
        $match = $raw | Where-Object { $_ -match "^$([Regex]::Escape($Serial))\s+device$" }
        if ($match) { return "ONLINE" }

        $match = $raw | Where-Object { $_ -match "^$([Regex]::Escape($Serial))\s+offline$" }
        if ($match) { return "OFFLINE" }

        $match = $raw | Where-Object { $_ -match "^$([Regex]::Escape($Serial))\s+unauthorized$" }
        if ($match) { return "UNAUTHORIZED" }

        return "NONE"
    }

    # Generic status check
    $devices = $raw | Where-Object { $_ -match "device$" -and $_ -notmatch "List of devices" }
    if ($devices.Count -gt 0) {
        return "ONLINE"
    }

    $offline = $raw | Where-Object { $_ -match "offline" }
    if ($offline) { return "OFFLINE" }

    $unauth = $raw | Where-Object { $_ -match "unauthorized" }
    if ($unauth) { return "UNAUTHORIZED" }

    return "NONE"
}

function Get-WorkingTreeSnapshot {
    $result      = @()
    $snapshotAge = if (Test-Path $STATE_FILES_FILE) { (Get-Item $STATE_FILES_FILE).LastWriteTimeUtc } else { [DateTime]::MinValue }

    # Scan source dirs and root config files
    $scanRoots = @(".")
    Get-ChildItem -Path $scanRoots -Recurse -File -Include $SOURCE_EXTENSIONS -ErrorAction SilentlyContinue |
        Where-Object {
            $p = $_.FullName
            -not ($EXCLUDE_DIR_NAMES | Where-Object { $p -match [Regex]::Escape("\$_\") })
        } |
        ForEach-Object {
            # Fast path: file unchanged since last snapshot — reuse size+mtime as proxy
            if ($_.LastWriteTimeUtc -le $snapshotAge) {
                $proxy = "$($_.Length)_$($_.LastWriteTimeUtc.Ticks)"
                $result += "$proxy $($_.FullName.Replace((Get-Location).Path + '\', ''))"
            }
            else {
                try {
                    $hash = (Get-FileHash $_.FullName -Algorithm SHA1 -ErrorAction SilentlyContinue).Hash
                    if ($hash) {
                        $result += "$hash $($_.FullName.Replace((Get-Location).Path + '\', ''))"
                    }
                }
                catch { }
            }
        }

    return ($result | Sort-Object)
}


# ============ WORKING TREE STATE ============
function Get-WorkingTreeHash {
    $snapshot = Get-WorkingTreeSnapshot
    if (-not $snapshot) { return "empty" }
    return ($snapshot | Out-String) | git hash-object --stdin
}

function Get-ChangedFilesSinceLastRun {
    if (-not (Test-Path $STATE_FILES_FILE)) {
        return @()
    }

    $old = @(Get-Content $STATE_FILES_FILE)
    $new = @(Get-WorkingTreeSnapshot)

    # Build map of old files and their hashes
    $oldMap = @{}
    foreach ($line in $old) {
        if ($line) {
            $hash, $file = $line -split ' ', 2
            $oldMap[$file] = $hash
        }
    }

    $changed = @()

    # Check for new or modified files
    foreach ($line in $new) {
        if ($line) {
            $hash, $file = $line -split ' ', 2

            if (-not $oldMap.ContainsKey($file)) {
                $changed += $file
            }
            elseif ($oldMap[$file] -ne $hash) {
                $changed += $file
            }
        }
    }

    # Check for deleted files
    $newFiles = $new | ForEach-Object { ($_ -split ' ', 2)[1] }
    foreach ($file in $oldMap.Keys) {
        if ($file -notin $newFiles) {
            $changed += $file
        }
    }

    return $changed
}

function Save-Run-State {
      $snapshot = Get-WorkingTreeSnapshot
    $hash     = $snapshot | Out-String | git hash-object --stdin
    $hash     | Out-File $STATE_HASH_FILE  -Encoding ascii
    $snapshot | Out-File $STATE_FILES_FILE -Encoding ascii
}

function Launch-App {
    Write-Host "🚀 Launching app..." -ForegroundColor Cyan
    adb shell am force-stop $APP_ID
    Start-Sleep -Milliseconds 300
    adb shell am start -n "$APP_ID/$MAIN_ACTIVITY"
}

function Wait-For-AppPid {
    param (
        [int]$TimeoutSeconds = 10
    )

    $elapsed = 0
    while ($elapsed -lt $TimeoutSeconds) {
        $apppid = adb -s $ADB_DEVICE shell pidof -s $APP_ID
        if ($apppid) {
            return $apppid
        }
        Start-Sleep -Milliseconds 300
        $elapsed += 0.3
    }

    Write-Host "⚠️ App PID not found — logcat may be empty" -ForegroundColor Yellow
    return $null
}


function Get-LogTagRegex {
    if (-not $LogTag -or $LogTag.Count -eq 0) {
        return $null
    }

    # Escape tags and join with OR
    $escaped = $LogTag | ForEach-Object { [Regex]::Escape($_) }
    return ($escaped -join "|")
}

function Attach-Logcat {
    Write-Host "📜 Attaching logcat (app-only)..." -ForegroundColor Cyan
    adb logcat -c
    $application_pid = Wait-For-AppPid
    $tagRegex = Get-LogTagRegex

    if ($application_pid) {
        if ($tagRegex) {
            Write-Host "🔎 Filtering logs by tag(s): $($LogTag -join ', ')" -ForegroundColor Yellow
            adb logcat --pid=$application_pid |
                ForEach-Object {
                    if ($_ -match $tagRegex) {
                        Write-Host $_
                    }
                }
        }
        else {
            adb logcat --pid=$application_pid
        }
    }
    else {
        adb logcat
    }
}

function Run-InstrumentationTests {

    Write-Host "🧪 Running Android instrumentation tests..." -ForegroundColor Cyan

    $testStart = Get-Date

    ./gradlew connectedDebugAndroidTest --console=plain |
        ForEach-Object {
            if ($_ -match "FAILED" -or $_ -match "PASSED") {
                Write-Host $_
            }
        }

    if ($LASTEXITCODE -ne 0) {
        Write-Host "❌ Test cases failed" -ForegroundColor Red
        New-BurntToastNotification -Text "Tests Failed ❌"
        exit 1
    }

    $duration = (Get-Date) - $testStart
    Write-Host ("🧪 Tests finished in {0}" -f (Format-Duration $duration))
}

# ================= MAIN =================

Write-Host "📱 Initializing ADB..." -ForegroundColor Cyan

$state = (adb get-state 2>&1) | Where-Object { $_ -notmatch "^error" } | Select-Object -First 1

if ($LASTEXITCODE -ne 0) {
    Write-Host "⚠️ ADB server not running — starting..." -ForegroundColor Yellow
    adb start-server | Out-Null
    Start-Sleep -Seconds 2
}
elseif ($state -eq "unknown") {
    Write-Host "⚠️ ADB in bad state — restarting..." -ForegroundColor Yellow
    adb kill-server | Out-Null
    Start-Sleep -Milliseconds 500
    adb start-server | Out-Null
    Start-Sleep -Seconds 2
}
else {
    Write-Host "✅ ADB already running"
}

# Try to connect saved device if none online
$devices = Get-ConnectedDevices
if ($devices.Count -eq 0) {
    Write-Host "⚠️ No devices found — attempting to reconnect WiFi device..." -ForegroundColor Yellow

    $cfg = Load-LastDevice
    if ($cfg) {
        Write-Host "🔄 Trying saved device $($cfg.ip):$($cfg.port)" -ForegroundColor Cyan
        if (Try-Adb-Connect $cfg.ip $cfg.port) {
            $devices = Get-ConnectedDevices
        }
        else {
            Write-Host "⚠️ Saved device failed to connect (port may have changed)" -ForegroundColor Yellow
        }
    }

    if ($devices.Count -eq 0) {
        Write-Host "📍 No USB devices and saved WiFi unavailable — enter WiFi details" -ForegroundColor Yellow
        $cfg = Prompt-For-AdbTarget
        if (Try-Adb-Connect $cfg.ip $cfg.port) {
            Save-LastDevice $cfg.ip $cfg.port
            $devices = Get-ConnectedDevices
        }
        else {
            Write-Host "❌ Connection failed. Check:" -ForegroundColor Red
            Write-Host "   • Device USB connection (if using USB)" -ForegroundColor Red
            Write-Host "   • Wireless Debugging enabled on device" -ForegroundColor Red
            Write-Host "   • Correct IP address and port" -ForegroundColor Red
            exit 1
        }
    }
}

if ($devices.Count -eq 0) {
    Write-Host "❌ No devices connected (USB or WiFi)" -ForegroundColor Red
    exit 1
}

# Prefer USB if available, fall back to WiFi
$usbDevices = @($devices | Where-Object { $_.type -eq "USB" })
$wifiDevices = @($devices | Where-Object { $_.type -eq "WiFi" })

Write-Host "📱 Found $($devices.Count) device(s): $($usbDevices.Count) USB, $($wifiDevices.Count) WiFi"

if ($devices.Count -eq 1) {
    $ADB_DEVICE = $devices[0].serial
    $env:ADB_DEVICE = $ADB_DEVICE
    $deviceType = $devices[0].type
    Write-Host "📱 Using device: $ADB_DEVICE ($deviceType)" -ForegroundColor Green
}
elseif ($devices.Count -gt 1) {
    # Try to reuse env device if still connected
    if (-not [string]::IsNullOrWhiteSpace($env:ADB_DEVICE)) {
        $found = $devices | Where-Object { $_.serial -eq $env:ADB_DEVICE }
        if ($found) {
            $ADB_DEVICE = $env:ADB_DEVICE
            Write-Host "📱 Reusing device from session: $ADB_DEVICE ($($found.type))" -ForegroundColor Yellow
        }
        else {
            # Prefer USB over WiFi
            if ($usbDevices.Count -gt 0) {
                $ADB_DEVICE = $usbDevices[0].serial
            }
            else {
                $ADB_DEVICE = $wifiDevices[0].serial
            }
            $env:ADB_DEVICE = $ADB_DEVICE
            Write-Host "📱 Session device unavailable — using: $ADB_DEVICE ($((Detect-DeviceType $ADB_DEVICE)))" -ForegroundColor Yellow
        }
    }
    else {
        # No session device — show list
        Write-Host "📱 Multiple devices available — select one:" -ForegroundColor Yellow

        # Show USB first, then WiFi
        $displayList = @()
        $index = 0

        foreach ($dev in $usbDevices) {
            Write-Host " [$index] $($dev.serial) [USB]" -ForegroundColor Cyan
            $displayList += $dev.serial
            $index++
        }

        foreach ($dev in $wifiDevices) {
            Write-Host " [$index] $($dev.serial) [WiFi]" -ForegroundColor Yellow
            $displayList += $dev.serial
            $index++
        }

        $choice = Read-Host "Select device index"
        $ADB_DEVICE = $displayList[[int]$choice]
        $env:ADB_DEVICE = $ADB_DEVICE
        Write-Host "📱 Selected: $ADB_DEVICE ($((Detect-DeviceType $ADB_DEVICE)))" -ForegroundColor Green
    }
}

Write-Host "📱 Using device: $env:ADB_DEVICE" -ForegroundColor Green

# Check device health before proceeding
$deviceStatus = Get-Device-Status $env:ADB_DEVICE
if ($deviceStatus -eq "OFFLINE") {
    if (Try-Adb-Recover-Offline $env:ADB_DEVICE) {
        Write-Host "✅ Device recovered from offline state" -ForegroundColor Green
    }
    else {
        Write-Host "❌ Device still offline after recovery attempt" -ForegroundColor Red
        exit 1
    }
}
elseif ($deviceStatus -ne "ONLINE") {
    Write-Host "❌ Device status: $deviceStatus" -ForegroundColor Red
    exit 1
}

# Pre-flight device checks
if (-not (Run-DevicePreflightChecks $env:ADB_DEVICE)) {
    Write-Host "⚠️ Continuing despite pre-flight warnings..." -ForegroundColor Yellow
    Start-Sleep -Seconds 2
}

# ---------- FORCE OVERRIDE (EARLY EXIT GUARD) ----------
$currentHash = Get-WorkingTreeHash

if (-not ($forceFullBuild -or $forceReinstall -or $forceClearData)) {
    $lastBuildSucceeded = Test-Path $BUILD_SUCCESS_FILE

    if (Test-Path $STATE_HASH_FILE) {
        $lastHash = Get-Content $STATE_HASH_FILE

        if ($currentHash -eq $lastHash -and $lastBuildSucceeded) {
            Write-Host "📝 No changes + last build succeeded — skipping build" -ForegroundColor Green
            Launch-App
            if ($attachLogcat) { Attach-Logcat }
            New-BurntToastNotification -Text "No build needed ✅"
            exit 0
        }
        elseif ($currentHash -eq $lastHash -and -not $lastBuildSucceeded) {
            Write-Host "⚠️ No changes but last build FAILED — rebuilding" -ForegroundColor Yellow
        }
    }
}
else {
    Write-Host "🔥 Force flag detected — skipping hash check" -ForegroundColor Cyan
}

# ---------- DISPLAY CHANGES ----------
$changedFiles = Get-ChangedFilesSinceLastRun

if ($changedFiles.Count -gt 0) {
    Write-Host "📝 Files changed since last successful run:" -ForegroundColor Yellow
    foreach ($f in $changedFiles) {
        Write-Host "   • $f" -ForegroundColor Gray
    }
}
else {
    Write-Host "📝 No file changes since last successful run" -ForegroundColor DarkGray
}

# ---------- CHANGE ANALYSIS ----------
$needsBuild = $false
$needsClean = $false

foreach ($file in $changedFiles) {
    # Build config files — always clean & build
    if ($file -match "build\.gradle(\.kts)?$" -or
        $file -match "settings\.gradle" -or
        $file -match "gradle\.properties") {

        $needsClean = $true
        $needsBuild = $true
        break
    }

    # Source files — trigger build
    if ($file -match "\.(kt|java)$" -or $file -match "AndroidManifest\.xml" -or
        $file -match "\.xml$" -or $file -match "google-services\.json") {

        $needsBuild = $true
    }
}

# ---------- FALLBACK: Hash changed but files unidentified ----------
if (-not $needsBuild -and -not $needsClean) {
    $savedHash = Get-Content $STATE_HASH_FILE -ErrorAction SilentlyContinue
    if ($currentHash -ne $savedHash) {
        Write-Host "⚠️ State changed but files unidentified — forcing incremental build" -ForegroundColor Yellow
        $needsBuild = $true
    }
}

# ---------- FORCE FULL BUILD ----------
# Force Gradle to use selected device
$env:ANDROID_SERIAL = $env:ADB_DEVICE
if ($forceFullBuild) {
    Write-Host "🔄 FullBuild requested — ignoring cached state" -ForegroundColor Cyan
    $needsClean = $true
    $needsBuild = $true
}

# ---------- DEVICE STATE ACTIONS ----------
if ($forceReinstall) {
	 Write-Host "🗑️ Reinstall requested — attempting uninstall (non-fatal)" -ForegroundColor Yellow
	 adb uninstall $APP_ID | Out-Null
}
elseif ($forceClearData) {
    Write-Host "🧹 ClearData requested — clearing app data" -ForegroundColor Yellow
    adb shell pm clear $APP_ID | Out-Null
}

# ---------- BUILD ----------
$gradleExit = 0
$buildLogFile = $null
$buildSucceeded = $true

if ($needsClean -or $needsBuild) {
    if (Test-Path $BUILD_SUCCESS_FILE) {
        Remove-Item $BUILD_SUCCESS_FILE -Force
    }

    # Initialize build log
    $buildLogFile = Initialize-BuildLog
    Add-ToBuildLog $buildLogFile "Selected device: $env:ADB_DEVICE"
    Add-ToBuildLog $buildLogFile "Force clean: $needsClean"
    Add-ToBuildLog $buildLogFile "Force build: $needsBuild"
    Add-ToBuildLog $buildLogFile ""
}

Write-Host "▶ Gradle started..." -ForegroundColor Cyan
if ($buildLogFile) {
    Add-ToBuildLog $buildLogFile "Gradle build started"
}

if ($needsClean) {
    Write-Host "🧹 CLEAN build" -ForegroundColor Yellow
	$buildStart = Get-Date

    if ($verboseGradle) {
        ./gradlew clean :app:installDebug --console=plain --profile 2>&1 | Tee-Object -FilePath $buildLogFile -Append | Out-Null
        $gradleExit = $LASTEXITCODE
    }
    else {
        $buildOutput = @(./gradlew clean :app:installDebug --console=plain --profile 2>&1)
        $gradleExit = $LASTEXITCODE

        # Log full output and display key lines
        $buildOutput | ForEach-Object {
            Add-ToBuildLog $buildLogFile $_
            if ($_ -match "took\s+([\d\.]+)\s+secs") {
                Write-Host $_ -ForegroundColor Yellow
            }
            elseif ($_ -match "(?i)(error:|FAILED|exception|could not|unresolved|type mismatch|cannot find symbol)") {
                Write-Host $_ -ForegroundColor Red
            }
            elseif ($_ -match "^\s*e:" -or $_ -match "^\s+\^") {
                Write-Host $_ -ForegroundColor Red
            }
        }
    }

	$buildTime = (Get-Date) - $buildStart
    $buildTimeStr = Format-Duration $buildTime
	Write-Host ("CLEAN & Build took: {0}" -f $buildTimeStr)
    Add-ToBuildLog $buildLogFile "Build time: $buildTimeStr"
}
elseif ($needsBuild) {
    Write-Host "🚀 INCREMENTAL build" -ForegroundColor Cyan
	$buildStart = Get-Date

    if ($verboseGradle) {
        ./gradlew :app:installDebug --console=plain --profile 2>&1 | Tee-Object -FilePath $buildLogFile -Append | Out-Null
        $gradleExit = $LASTEXITCODE
    }
    else {
        $buildOutput = @(./gradlew :app:installDebug --console=plain --profile 2>&1)
        $gradleExit = $LASTEXITCODE

        # Log full output and display key lines
        $buildOutput | ForEach-Object {
            Add-ToBuildLog $buildLogFile $_
            if ($_ -match "took\s+([\d\.]+)\s+secs") {
                Write-Host $_ -ForegroundColor Yellow
            }
            elseif ($_ -match "(?i)(error:|FAILED|exception|could not|unresolved|type mismatch|cannot find symbol)") {
                Write-Host $_ -ForegroundColor Red
            }
            elseif ($_ -match "^\s*e:" -or $_ -match "^\s+\^") {
                Write-Host $_ -ForegroundColor Red
            }
        }
    }

	$buildTime = (Get-Date) - $buildStart
    $buildTimeStr = Format-Duration $buildTime
	Write-Host ("Build took: {0}" -f $buildTimeStr)
    Add-ToBuildLog $buildLogFile "Build time: $buildTimeStr"
}

if ($gradleExit -ne 0) {
    $buildSucceeded = $false
    Add-ToBuildLog $buildLogFile "BUILD FAILED (exit code: $gradleExit)"
	New-BurntToastNotification -Text "Build Failed ❌", "Check logs for errors."
    if ($buildLogFile) {
        Write-Host "❌ Build failed! Check log:" -ForegroundColor Red
        $absPath = (Resolve-Path $buildLogFile).Path
        $fileUri = "file:///$($absPath.Replace('\', '/'))"
        Write-Host "   $fileUri" -ForegroundColor Blue -BackgroundColor Black
    }
    exit 1
}

if ($needsClean -or $needsBuild) {
    "OK" | Out-File $BUILD_SUCCESS_FILE -Encoding ascii
    Add-ToBuildLog $buildLogFile "BUILD SUCCEEDED ✅"
    Add-ToBuildLog $buildLogFile "Finished: $(Get-Date -Format 'yyyy-MM-dd HH:mm:ss')"
}

# ---------- SAVE STATE ----------
Save-Run-State
Write-Host "💾 Saved current working tree state" -ForegroundColor Green
# ---------- AUTO-LAUNCH ----------
Launch-App
Write-Host "✅ App running. Ready for testing." -ForegroundColor Green

if ($attachLogcat) {
    Write-Host "📜 Logcat enabled" -ForegroundColor Cyan
    Attach-Logcat
}
else {
    Write-Host "ℹ️ Logcat disabled (use -Logcat to enable)" -ForegroundColor DarkGray
}
$totalTime = (Get-Date) - $scriptStart

Write-Host ("⏱ Total   : {0}" -f (Format-Duration  $totalTime))


if ($RunTests) {
    Run-InstrumentationTests
}

# Clean up old build logs
if ($buildLogFile -and (Test-Path $buildLogFile)) {
    Clean-OldBuildLogs
}

# Only show notification if a build actually ran
if ($needsClean -or $needsBuild) {
    if ($buildSucceeded) {
        New-BurntToastNotification -Text "Build Success ✅", "Your build completed successfully."
        [console]::beep(800,500)
    } else {
        New-BurntToastNotification -Text "Build Failed ❌", "Check logs for errors."
        [console]::beep(300,900)
    }
}

# ---------- FINAL LOG LINK ----------
if ($buildLogFile -and (Test-Path $buildLogFile)) {
    Write-Host ""
    Write-Host "═══════════════════════════════════════════════════════════" -ForegroundColor DarkGray
    Write-Host "📋 Build Log:" -ForegroundColor Cyan
    $absPath = (Resolve-Path $buildLogFile).Path
    $fileUri = "file:///$($absPath.Replace('\', '/'))"
    Write-Host "   $fileUri" -ForegroundColor Blue -BackgroundColor Black
    Write-Host "═══════════════════════════════════════════════════════════" -ForegroundColor DarkGray
}

# Auto-detect (default)
#powershell -ExecutionPolicy Bypass -File smart_run.ps1

# Force full build
#powershell -ExecutionPolicy Bypass -File smart_run.ps1 -forceFullBuild true

#$pairing device
#adb pair 192.168.0.140:37573
#adb start-server
#adb kill-server
#adb connect 192.168.0.140:45679
#.\smart_run.ps1 -FullBuild $True
#logs check 
#adb logcat --pid=$(adb shell pidof -s com.sukoon.music) | Select-String "ContinueListeningCard"
#.\smart_run.ps1 -Logcat -LogTag "FeedbackRepositoryImpl","Firestore"
#ensure typed ip device is conncected
#work on pairing
#adb forward tcp:5277 tcp:5277
# ./gradlew assembleDebug
#adb shell monkey -p com.sukoon.music --throttle 300 -v 1000
#adb shell monkey -p com.sukoon.music -v 5000 | tee monkey_log.txt
#Select-String -Path monkey_log.txt -Pattern "CRASH","ANR","FATAL EXCEPTION"