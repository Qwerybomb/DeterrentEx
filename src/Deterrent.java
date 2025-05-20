import javax.sound.sampled.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import javax.swing.*;

public final class Deterrent {

    // initialize normal variables n such
    static int DBlimit = 20;
    static File audio;

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

    public static void prepareMicrophone() throws LineUnavailableException {
        audio_Player audioPlayer = new audio_Player(audio);
        GUI window = new GUI();
        AudioFormat format = new AudioFormat(44100, 16, 1, true, true);
        DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
        TargetDataLine mic = (TargetDataLine) AudioSystem.getLine(info);
        mic.open(format);
        audioPlayer.start();
        mic.start();
        byte[] buffer = new byte[2048];
        int bytesRead;
        // forever loop
        while (true) {
            bytesRead = mic.read(buffer, 0, buffer.length);
            double rms = calculateRMSLevel(buffer, bytesRead);
            double db = 20 * Math.log10(rms);
            if (db > DBlimit && window.isToggle) {
                audioPlayer.booleanUpdate(true);
            } else {
                audioPlayer.booleanUpdate(false);
            }
            window.labelUpdate(String.valueOf(Math.round(db)));
        }
    }

    public static void settingsReader() {
        String data = null;
        try {
            File myObj = new File("src/settings.txt");
            Scanner myReader = new Scanner(myObj);
            audio = new File(myReader.nextLine());
            DBlimit = Integer.parseInt(myReader.nextLine());
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws LineUnavailableException {
        settingsReader();
        prepareMicrophone();
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
            Thread.sleep(clip.getMicrosecondLength() / 1000);
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
           if (peakReached) {playSound(audio);}
       }
    }
}
class GUI implements ActionListener {
    boolean isToggle = false;
    JPanel panel = new JPanel();
    JFrame window = new JFrame("Deterrent");
    JButton toggleButton = new JButton(" ");
    JLabel DBreader = new JLabel("");
    public GUI() {
        // button stuff
        toggleButton.setPreferredSize(new Dimension(500,50));
        toggleButton.setText("Toggled : Off");
        toggleButton.addActionListener(this);
        DBreader.setPreferredSize(new Dimension(20,20));

        // do things with the panel
        panel.add(toggleButton);
        panel.add(DBreader);

        // window refinement
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.add(panel);
        window.setSize(300,300);
        window.setVisible(true);
    }
public void buttonToggle() {
       isToggle = !isToggle;
       if (isToggle) toggleButton.setText("Toggled : On"); else toggleButton.setText("Toggled : Off");
    }
public void labelUpdate(String text) {DBreader.setText(text);}
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == toggleButton) {buttonToggle();}
    }
}