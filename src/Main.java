import javax.sound.sampled.*;
import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import static java.lang.Math.log10;

public class Main {
    public static void playSound(File Audio) {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(Audio.getAbsoluteFile());
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
            while (clip.isRunning()) {
                Thread.sleep(1);
            }
        } catch (Exception ex) {
            System.out.println("Failed to run audio");
            ex.printStackTrace();
        }
    }
    public static int calculateRMSLevel(byte[] audioData)
    {
        long lSum = 0;
        for(int i=0; i < audioData.length; i++)
            lSum = lSum + audioData[i];

        double dAvg = (double) lSum / audioData.length;
        double sumMeanSquare = 0d;

        for(int j=0; j < audioData.length; j++)
            sumMeanSquare += Math.pow(audioData[j] - dAvg, 2d);

        double averageMeanSquare = sumMeanSquare / audioData.length;

        return (int)(Math.pow(averageMeanSquare,0.5d) + 0.5);
    }
    public static void prepareMicrophone() {
        try {
            AudioFormat format = new AudioFormat(48000, 16, 1, true, true);
            DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
            TargetDataLine microphone = (TargetDataLine) AudioSystem.getLine(info);
            microphone.open(format);
            microphone.start();
            byte[] buffer = new byte[1000];
            int bufferCount = 0;
            int bytesRead;
            int fullSum = 0;
            while ((bytesRead = microphone.read(buffer, 0, buffer.length)) > 0) {
               int level = calculateRMSLevel(buffer);
              if (level > 60) {
                  System.out.println(level);
              }
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
