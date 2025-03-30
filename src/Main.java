import javax.sound.sampled.*;
import java.io.File;
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
                Test.add(buffer[buffer.length - 1]);
                bufferCount++;
                if (bufferCount == 10) {
                    bufferCount = 0;
                    for (int i : Test ) {
                     fullSum = fullSum + Math.abs(i);
                    }
                    fullSum = fullSum / Test.size();
                   if (fullSum > 100) {
                       File annoyingAudio = new File("C:\\Users\\isaac\\Downloads\\Symbal.wav");
                       playSound(annoyingAudio);
                       System.out.println(fullSum);

                   }
                    Test.clear();
                }
            }
        } catch (LineUnavailableException e) {
            throw new RuntimeException(e);
        }
    }
    public static void main(String[] args) {
        prepareMicrophone();

    }
}
