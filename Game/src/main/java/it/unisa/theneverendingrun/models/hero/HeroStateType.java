package it.unisa.theneverendingrun.models.hero;

import com.badlogic.gdx.audio.Sound;
import it.unisa.theneverendingrun.utilities.SoundUtils;

public enum HeroStateType {
    IDLE(""),
    DEAD("sounds/music/playerDiedMusic.mp3"),
    FALL(""),
    SLIDE("sounds/effects/heroSlide.mp3"),
    JUMP("sounds/effects/heroJump.mp3"),
    RUN(""),
    ATTACK("");

    private String soundPath;

    private Sound sound;

    HeroStateType(String soundPath) {
        this.soundPath = soundPath;

        try {
            sound = SoundUtils.getSound(soundPath);
        } catch (IllegalArgumentException exc) {
            sound = null;
        }
    }

    public String getSoundPath() {
        return soundPath;
    }

    public Sound getSound() {
        return sound;
    }
}