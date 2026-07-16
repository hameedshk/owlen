package com.owlen.app.data.audio;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000:\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0017\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\u0018\u0000 \u00162\u00020\u0001:\u0001\u0016B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\f\u001a\u00020\rH\u0016J\u0006\u0010\u000e\u001a\u00020\u000fJ\u000e\u0010\u0010\u001a\u00020\r2\u0006\u0010\u0011\u001a\u00020\u0012J\u0006\u0010\u0013\u001a\u00020\rJ\u0012\u0010\u0014\u001a\u00020\r*\u00020\u0012H\u0082@\u00a2\u0006\u0002\u0010\u0015R\u0014\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\u00050\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b\b\u0010\tR\u0010\u0010\n\u001a\u0004\u0018\u00010\u000bX\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0017"}, d2 = {"Lcom/owlen/app/data/audio/AudioCapture;", "Ljava/io/Closeable;", "()V", "_audioFlow", "Lkotlinx/coroutines/flow/MutableSharedFlow;", "", "audioFlow", "Lkotlinx/coroutines/flow/SharedFlow;", "getAudioFlow", "()Lkotlinx/coroutines/flow/SharedFlow;", "recordingJob", "Lkotlinx/coroutines/Job;", "close", "", "isRecording", "", "start", "scope", "Lkotlinx/coroutines/CoroutineScope;", "stop", "captureLoop", "(Lkotlinx/coroutines/CoroutineScope;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "Companion", "app_debug"})
public final class AudioCapture implements java.io.Closeable {
    public static final int SAMPLE_RATE = 16000;
    public static final int CHANNEL_CONFIG = android.media.AudioFormat.CHANNEL_IN_MONO;
    public static final int AUDIO_FORMAT = android.media.AudioFormat.ENCODING_PCM_16BIT;
    public static final int FRAME_SIZE = 780;
    public static final int WINDOW_FRAMES = 20;
    public static final int WINDOW_SIZE = 15600;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableSharedFlow<short[]> _audioFlow = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.SharedFlow<short[]> audioFlow = null;
    @org.jetbrains.annotations.Nullable()
    private kotlinx.coroutines.Job recordingJob;
    @org.jetbrains.annotations.NotNull()
    public static final com.owlen.app.data.audio.AudioCapture.Companion Companion = null;
    
    public AudioCapture() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.SharedFlow<short[]> getAudioFlow() {
        return null;
    }
    
    public final void start(@org.jetbrains.annotations.NotNull()
    kotlinx.coroutines.CoroutineScope scope) {
    }
    
    private final java.lang.Object captureLoop(kotlinx.coroutines.CoroutineScope $this$captureLoop, kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    public final void stop() {
    }
    
    public final boolean isRecording() {
        return false;
    }
    
    @java.lang.Override()
    public void close() {
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0006\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\n"}, d2 = {"Lcom/owlen/app/data/audio/AudioCapture$Companion;", "", "()V", "AUDIO_FORMAT", "", "CHANNEL_CONFIG", "FRAME_SIZE", "SAMPLE_RATE", "WINDOW_FRAMES", "WINDOW_SIZE", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
}