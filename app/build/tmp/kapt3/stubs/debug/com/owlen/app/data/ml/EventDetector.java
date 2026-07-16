package com.owlen.app.data.ml;

/**
 * Maps YAMNet's 521 AudioSet class scores to Owlen's 10 event classes.
 *
 * Index constants below are 0-based positions in the yamnet_label_list.txt
 * embedded in the bundled yamnet.tflite. Each Owlen class takes the maximum
 * score across its mapped AudioSet classes.
 *
 * YAMNet scores are independent sigmoids that rarely approach 1.0 even for a
 * clearly present sound, so raw scores are rescaled by CONFIDENCE_GAIN before
 * being reported as confidence (a raw 0.5 maps to full confidence). The
 * DisturbanceScorer's 0.70 safety-bypass threshold therefore corresponds to a
 * raw YAMNet score of 0.35 for Baby Cry / Smoke Alarm.
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0014\n\u0002\b\u0003\u0018\u0000 \n2\u00020\u0001:\u0001\nB\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u0003\u001a\u00020\u0004H\u0016J\u0016\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bH\u0086@\u00a2\u0006\u0002\u0010\t\u00a8\u0006\u000b"}, d2 = {"Lcom/owlen/app/data/ml/EventDetector;", "Ljava/lang/AutoCloseable;", "()V", "close", "", "detect", "Lcom/owlen/app/domain/model/DetectedEvent;", "scores", "", "([FLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "Companion", "app_debug"})
public final class EventDetector implements java.lang.AutoCloseable {
    public static final int NUM_CLASSES = 521;
    public static final float DETECTION_FLOOR = 0.1F;
    public static final float CONFIDENCE_GAIN = 2.0F;
    @org.jetbrains.annotations.NotNull()
    private static final java.util.Map<com.owlen.app.domain.model.EventClass, int[]> CLASS_MAP = null;
    @org.jetbrains.annotations.NotNull()
    public static final com.owlen.app.data.ml.EventDetector.Companion Companion = null;
    
    public EventDetector() {
        super();
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object detect(@org.jetbrains.annotations.NotNull()
    float[] scores, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.owlen.app.domain.model.DetectedEvent> $completion) {
        return null;
    }
    
    @java.lang.Override()
    public void close() {
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010$\n\u0002\u0018\u0002\n\u0002\u0010\u0015\n\u0000\n\u0002\u0010\u0007\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u001a\u0010\u0003\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00060\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\bX\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u0086T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\f"}, d2 = {"Lcom/owlen/app/data/ml/EventDetector$Companion;", "", "()V", "CLASS_MAP", "", "Lcom/owlen/app/domain/model/EventClass;", "", "CONFIDENCE_GAIN", "", "DETECTION_FLOOR", "NUM_CLASSES", "", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
}