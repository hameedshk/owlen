package com.owlen.app.data.ml;

/**
 * Runs the bundled YAMNet classification model (CPU only) on a ~0.975s PCM
 * window and returns the raw scores for all 521 AudioSet classes.
 *
 * The bundled yamnet.tflite is the TF Hub classification variant: input is a
 * float32 waveform of 15600 samples at 16kHz, output is [521] scores in 0..1.
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000:\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u0014\n\u0000\n\u0002\u0010\u0017\n\u0002\b\u0003\u0018\u0000 \u00142\u00020\u0001:\u0001\u0014B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\b\u0010\r\u001a\u00020\u000eH\u0016J\u0016\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u0012H\u0086@\u00a2\u0006\u0002\u0010\u0013R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0015"}, d2 = {"Lcom/owlen/app/data/ml/FeatureExtractor;", "Ljava/lang/AutoCloseable;", "context", "Landroid/content/Context;", "(Landroid/content/Context;)V", "inputBuffer", "Ljava/nio/ByteBuffer;", "inputSamples", "", "interpreter", "Lorg/tensorflow/lite/Interpreter;", "numClasses", "outputBuffer", "close", "", "extract", "", "pcmWindow", "", "([SLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "Companion", "app_debug"})
public final class FeatureExtractor implements java.lang.AutoCloseable {
    public static final int INPUT_SAMPLES = 15600;
    public static final int NUM_CLASSES = 521;
    @org.jetbrains.annotations.NotNull()
    private final org.tensorflow.lite.Interpreter interpreter = null;
    private final int inputSamples = 0;
    private final int numClasses = 0;
    @org.jetbrains.annotations.NotNull()
    private final java.nio.ByteBuffer inputBuffer = null;
    @org.jetbrains.annotations.NotNull()
    private final java.nio.ByteBuffer outputBuffer = null;
    @org.jetbrains.annotations.NotNull()
    public static final com.owlen.app.data.ml.FeatureExtractor.Companion Companion = null;
    
    public FeatureExtractor(@org.jetbrains.annotations.NotNull()
    android.content.Context context) {
        super();
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object extract(@org.jetbrains.annotations.NotNull()
    short[] pcmWindow, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super float[]> $completion) {
        return null;
    }
    
    @java.lang.Override()
    public void close() {
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0006"}, d2 = {"Lcom/owlen/app/data/ml/FeatureExtractor$Companion;", "", "()V", "INPUT_SAMPLES", "", "NUM_CLASSES", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
}