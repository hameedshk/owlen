package com.owlen.app.data.audio;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u0017\n\u0002\b\u0003\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0006\u0010\u0007\u001a\u00020\bJ\u0006\u0010\t\u001a\u00020\bJ\u0006\u0010\n\u001a\u00020\bR\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u000b"}, d2 = {"Lcom/owlen/app/data/audio/NoiseGenerator;", "", "()V", "BUFFER_SIZE", "", "DURATION_SECONDS", "SAMPLE_RATE", "generateBrownNoise", "", "generatePinkNoise", "generateWhiteNoise", "app_debug"})
public final class NoiseGenerator {
    private static final int SAMPLE_RATE = 44100;
    private static final int DURATION_SECONDS = 10;
    private static final int BUFFER_SIZE = 441000;
    @org.jetbrains.annotations.NotNull()
    public static final com.owlen.app.data.audio.NoiseGenerator INSTANCE = null;
    
    private NoiseGenerator() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final short[] generateWhiteNoise() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final short[] generatePinkNoise() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final short[] generateBrownNoise() {
        return null;
    }
}