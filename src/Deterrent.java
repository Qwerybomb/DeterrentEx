import javax.sound.sampled.*;
import java.io.File;

public class Deterrent {
    private static double calculateRMSLevel(byte[] audioData, int bytesRead) {
        long sum = 0;
        for (int i = 0; i < bytesRead - 1; i += 2) {
            // Convert two bytes into one sample (16-bit audio)
            int sample = (audioData[i] << 8) | (audioData[i + 1] & 0xFF);
            sum += sample * sample;
        }

        double rms = Math.sqrt(sum / (bytesRead / 2.0));
        return rms == 0 ? 1 : rms; // Avoid log(0)
    }
public static void prepareMicrophone(File audio) throws LineUnavailableException{
    audio_Player audioPlayer = new audio_Player(audio);
    AudioFormat format = new AudioFormat(44100, 16, 1, true, true);
    DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
    TargetDataLine mic = (TargetDataLine) AudioSystem.getLine(info);
    mic.open(format);
    audioPlayer.start();
    mic.start();
    byte[] buffer = new byte[2048];
    int bytesRead;

    while (true) {
        // constantly running
        bytesRead = mic.read(buffer, 0, buffer.length);
        double rms = calculateRMSLevel(buffer, bytesRead);
        double db = 20 * Math.log10(rms);
        System.out.println(db);
        if (db > 20) {audioPlayer.booleanUpdate(true);} else {audioPlayer.booleanUpdate(false);}
    }

}
    public static void main(String[] args) throws LineUnavailableException {
        File sound = new File("C:\\Users\\isaac\\Music\\sharuk.wav");
        prepareMicrophone(sound);
    }
}
class audio_Player extends Thread {
    File audio;
    boolean peakReached = false;
   public audio_Player(File f) {
       this.audio = f;
   }
   public void booleanUpdate(boolean updater) {
       peakReached = updater;
   }
    public static void playSound(File Audio) {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(Audio.getAbsoluteFile());
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
            Thread.sleep(clip.getMicrosecondLength() * 1000);
        } catch (Exception ex) {
            System.out.println("Failed to run audio");
            ex.printStackTrace();
        }
    }
    @Override
    public void run() {
       while (true) {
           // no idea why I need a System call for this to work ¯\_(ツ)_/¯
           System.out.print("");
           if (peakReached) playSound(audio);
       }
    }
}
