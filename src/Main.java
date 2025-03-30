import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

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
    public static void prepareMicrophone() {
        ArrayList<Byte> Test = new ArrayList<>();
        try {
            AudioFormat format = new AudioFormat(48100, 16, 1, true, true);
            DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
            TargetDataLine microphone = (TargetDataLine) AudioSystem.getLine(info);
            microphone.open(format);
            microphone.start();
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = microphone.read(buffer, 0, buffer.length)) > 0) {
                Test.add(buffer[buffer.length - 1]);
            }
        } catch (LineUnavailableException e) {
            throw new RuntimeException(e);
        }
    }
    public static void main(String[] args) {
        File Test = new File("C:\\Users\\isaac\\Downloads\\Symbal.wav");
        prepareMicrophone();

    }
}
