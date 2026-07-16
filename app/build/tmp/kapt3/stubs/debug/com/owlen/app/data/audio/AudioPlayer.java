package com.owlen.app.data.audio;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000>\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0017\n\u0000\n\u0002\u0010\u0007\n\u0002\b\u0002\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0006\u0018\u0000 \u001c2\u00020\u0001:\u0001\u001cB\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\r\u001a\u00020\u000eH\u0016J\u0006\u0010\u000f\u001a\u00020\u0010J\u001e\u0010\u0011\u001a\u00020\u000e2\u0006\u0010\u0012\u001a\u00020\b2\u0006\u0010\u0013\u001a\u00020\bH\u0086@\u00a2\u0006\u0002\u0010\u0014J&\u0010\u0015\u001a\u00020\u000e2\u0006\u0010\u0016\u001a\u00020\u00172\u0006\u0010\u0012\u001a\u00020\b2\u0006\u0010\u0013\u001a\u00020\bH\u0086@\u00a2\u0006\u0002\u0010\u0018J\u0018\u0010\u0019\u001a\u00020\u000e2\b\b\u0002\u0010\u001a\u001a\u00020\u0010H\u0086@\u00a2\u0006\u0002\u0010\u001bR\u0010\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u001d"}, d2 = {"Lcom/owlen/app/data/audio/AudioPlayer;", "Ljava/io/Closeable;", "()V", "audioTrack", "Landroid/media/AudioTrack;", "brownNoiseBuffer", "", "currentVolume", "", "pinkNoiseBuffer", "startMaskingTimeMs", "", "whiteNoiseBuffer", "close", "", "isPlaying", "", "setVolume", "volume", "maxVolume", "(FFLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "startMasking", "sound", "Lcom/owlen/app/domain/model/MaskingSound;", "(Lcom/owlen/app/domain/model/MaskingSound;FFLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "stopMasking", "immediate", "(ZLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "Companion", "app_debug"})
public final class AudioPlayer implements java.io.Closeable {
    public static final int PLAYBACK_SAMPLE_RATE = 44100;
    public static final int CHANNEL_CONFIG = android.media.AudioFormat.CHANNEL_OUT_MONO;
    public static final int AUDIO_FORMAT = android.media.AudioFormat.ENCODING_PCM_16BIT;
    public static final int AUDIO_USAGE = android.media.AudioAttributes.USAGE_MEDIA;
    public static final int CONTENT_TYPE = android.media.AudioAttributes.CONTENT_TYPE_MUSIC;
    public static final int FADE_STEPS = 100;
    public static final long FADE_DURATION_MS = 2000L;
    public static final long FADE_STEP_DELAY_MS = 20L;
    public static final long MINIMUM_MASKING_DURATION_MS = 30000L;
    @org.jetbrains.annotations.NotNull()
    private final short[] brownNoiseBuffer = null;
    @org.jetbrains.annotations.NotNull()
    private final short[] pinkNoiseBuffer = null;
    @org.jetbrains.annotations.NotNull()
    private final short[] whiteNoiseBuffer = null;
    @org.jetbrains.annotations.Nullable()
    private android.media.AudioTrack audioTrack;
    private long startMaskingTimeMs = 0L;
    private float currentVolume = 0.0F;
    @org.jetbrains.annotations.NotNull()
    public static final com.owlen.app.data.audio.AudioPlayer.Companion Companion = null;
    
    public AudioPlayer() {
        super();
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object startMasking(@org.jetbrains.annotations.NotNull()
    com.owlen.app.domain.model.MaskingSound sound, float volume, float maxVolume, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object stopMasking(boolean immediate, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object setVolume(float volume, float maxVolume, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    public final boolean isPlaying() {
        return false;
    }
    
    @java.lang.Override()
    public void close() {
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0010\t\n\u0002\b\u0005\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\tX\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\tX\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u000e"}, d2 = {"Lcom/owlen/app/data/audio/AudioPlayer$Companion;", "", "()V", "AUDIO_FORMAT", "", "AUDIO_USAGE", "CHANNEL_CONFIG", "CONTENT_TYPE", "FADE_DURATION_MS", "", "FADE_STEPS", "FADE_STEP_DELAY_MS", "MINIMUM_MASKING_DURATION_MS", "PLAYBACK_SAMPLE_RATE", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
}