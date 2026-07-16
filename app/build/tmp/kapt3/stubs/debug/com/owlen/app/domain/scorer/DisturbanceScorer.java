package com.owlen.app.domain.scorer;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00008\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0007\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0018\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\u0006H\u0002J\u0010\u0010\b\u001a\u00020\u00042\u0006\u0010\t\u001a\u00020\nH\u0002J\u0018\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u00042\u0006\u0010\u000e\u001a\u00020\u000fH\u0002J&\u0010\u0010\u001a\u00020\u00112\u0006\u0010\t\u001a\u00020\u00122\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\r\u001a\u00020\u00042\u0006\u0010\u000e\u001a\u00020\u000f\u00a8\u0006\u0013"}, d2 = {"Lcom/owlen/app/domain/scorer/DisturbanceScorer;", "", "()V", "calculateNormSoundLevel", "", "dBSPL", "", "floor", "getEventWeight", "event", "Lcom/owlen/app/domain/model/EventClass;", "isWithinSleepWindow", "", "currentHour", "settings", "Lcom/owlen/app/domain/model/SleepSettings;", "score", "Lcom/owlen/app/domain/model/DisturbanceResult;", "Lcom/owlen/app/domain/model/DetectedEvent;", "app_debug"})
public final class DisturbanceScorer {
    
    public DisturbanceScorer() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.owlen.app.domain.model.DisturbanceResult score(@org.jetbrains.annotations.NotNull()
    com.owlen.app.domain.model.DetectedEvent event, float dBSPL, int currentHour, @org.jetbrains.annotations.NotNull()
    com.owlen.app.domain.model.SleepSettings settings) {
        return null;
    }
    
    private final int getEventWeight(com.owlen.app.domain.model.EventClass event) {
        return 0;
    }
    
    private final int calculateNormSoundLevel(float dBSPL, float floor) {
        return 0;
    }
    
    private final boolean isWithinSleepWindow(int currentHour, com.owlen.app.domain.model.SleepSettings settings) {
        return false;
    }
}