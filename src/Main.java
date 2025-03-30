import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class Main {
    public static void playSound(File Audio) {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(Audio.getAbsoluteFile());
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
            Thread.sleep(1000);
        } catch (Exception ex) {
            System.out.println("Failed to run audio");
            ex.printStackTrace();
        }
    }











    public static void main(String[] args) {
        File Test = new File("C:\\Users\\isaac\\Downloads\\Symbal.wav");
playSound(Test);
    }
}
