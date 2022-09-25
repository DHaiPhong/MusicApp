import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

public class Sound {
    Clip clip;
    float previousVolume = 0;
    float currentVolume = 0;
    FloatControl fc;
    boolean mute = false;

    public void volumeUp() {
        currentVolume += 1.0f;
        if(currentVolume > 6.0f) {
            currentVolume = 6.0f;
        }
        fc.setValue(currentVolume);
    }
    public void volumeDown() {
        currentVolume -= 1.0f;
        if(currentVolume < -80.0f) {
            currentVolume = -80.0f;
        }
        fc.setValue(currentVolume);
    }
    public void volumeMute() {
        if(mute == false) {
            previousVolume = currentVolume;
            currentVolume = 80.0f;
            fc.setValue(currentVolume);
            mute = true;
        } else if(mute == true) {
            currentVolume = previousVolume;
            fc.setValue(currentVolume);
            mute = false;
        }

    }
}
