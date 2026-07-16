package com.owlen.app.presentation.session;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000<\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0010\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u000f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0006\u0010\u0018\u001a\u00020\u0019R\u0014\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010\b\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\n0\t0\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0016\u0010\u000b\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\f0\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010\r\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\f0\t0\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\u00070\u000f\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u0011R\u001d\u0010\u0012\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\n0\t0\u000f\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u0011R\u0019\u0010\u0014\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\f0\u000f\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0015\u0010\u0011R\u001d\u0010\u0016\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\f0\t0\u000f\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0017\u0010\u0011R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u001a"}, d2 = {"Lcom/owlen/app/presentation/session/SessionViewModel;", "Landroidx/lifecycle/ViewModel;", "sessionLogger", "Lcom/owlen/app/data/log/SessionLogger;", "(Lcom/owlen/app/data/log/SessionLogger;)V", "_adjustSensitivitySuggested", "Lkotlinx/coroutines/flow/MutableStateFlow;", "", "_latestSessionEvents", "", "Lcom/owlen/app/presentation/ui/SessionEventItem;", "_latestSummary", "Lcom/owlen/app/data/log/SessionSummary;", "_recentSessions", "adjustSensitivitySuggested", "Lkotlinx/coroutines/flow/StateFlow;", "getAdjustSensitivitySuggested", "()Lkotlinx/coroutines/flow/StateFlow;", "latestSessionEvents", "getLatestSessionEvents", "latestSummary", "getLatestSummary", "recentSessions", "getRecentSessions", "refresh", "", "app_debug"})
@dagger.hilt.android.lifecycle.HiltViewModel()
public final class SessionViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull()
    private final com.owlen.app.data.log.SessionLogger sessionLogger = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<java.util.List<com.owlen.app.data.log.SessionSummary>> _recentSessions = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.util.List<com.owlen.app.data.log.SessionSummary>> recentSessions = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<java.util.List<com.owlen.app.presentation.ui.SessionEventItem>> _latestSessionEvents = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.util.List<com.owlen.app.presentation.ui.SessionEventItem>> latestSessionEvents = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<com.owlen.app.data.log.SessionSummary> _latestSummary = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.owlen.app.data.log.SessionSummary> latestSummary = null;
    
    /**
     * Spec: suggest lowering sensitivity when >3 maskings scored under 40 (likely false positives).
     */
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<java.lang.Boolean> _adjustSensitivitySuggested = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.lang.Boolean> adjustSensitivitySuggested = null;
    
    @javax.inject.Inject()
    public SessionViewModel(@org.jetbrains.annotations.NotNull()
    com.owlen.app.data.log.SessionLogger sessionLogger) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.util.List<com.owlen.app.data.log.SessionSummary>> getRecentSessions() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.util.List<com.owlen.app.presentation.ui.SessionEventItem>> getLatestSessionEvents() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.owlen.app.data.log.SessionSummary> getLatestSummary() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.lang.Boolean> getAdjustSensitivitySuggested() {
        return null;
    }
    
    public final void refresh() {
    }
}