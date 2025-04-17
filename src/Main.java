import javax.sound.sampled.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import static java.lang.Math.log10;
import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JFrame;
import javax.swing.JLabel;

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
    AudioFormat format = new AudioFormat(44100, 16, 1, true, true);
    DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
    TargetDataLine mic = (TargetDataLine) AudioSystem.getLine(info);
    mic.open(format);
    mic.start();
    byte[] buffer = new byte[2048];
    int bytesRead;

    while (true) {
        // constantly running
        bytesRead = mic.read(buffer, 0, buffer.length);
        double rms = calculateRMSLevel(buffer, bytesRead);
        double db = 20 * Math.log10(rms);
        if (db > 80) playSound(audio);
    }

}
    public static void main(String[] args) throws LineUnavailableException {
        File sound = new File("C:\\Users\\isaac\\Downloads\\heavy-oh_nooooo");
        prepareMicrophone(sound);
    }
}

