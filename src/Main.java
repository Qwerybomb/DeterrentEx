import javax.sound.sampled.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

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
        ArrayList<Byte> Test = new ArrayList<Byte>();
        try {
            AudioFormat format = new AudioFormat(48100, 16, 1, true, true);
            DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
            TargetDataLine microphone = (TargetDataLine) AudioSystem.getLine(info);
            microphone.open(format);
            microphone.start();
            byte[] buffer = new byte[1024];
            int bufferCount = 0;
            int bytesRead;
            int fullSum = 0;
            while ((bytesRead = microphone.read(buffer, 0, buffer.length)) > 0) {
                System.out.println(Arrays.toString(buffer));
            }
        } catch (LineUnavailableException e) {
            throw new RuntimeException(e);
        }
    }
    public static String convertTo(Byte Input) {
       String output = "";
        for (int i = 0; i < Input ;i++) {
            output = output + ".";
        }
        return output;
    }
    public static void main(String[] args) {
        prepareMicrophone();
       System.out.println("program Terminated");
    }
}
