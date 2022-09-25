import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

import javax.sound.midi.Soundbank;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

public class Application implements ActionListener {
    JFrame frame;
    JLabel songNameLabel = new JLabel();
    //Button
    JButton selectButton = new JButton("Select Song");
    JButton playButton = new JButton("Play");
    JButton pauseButton = new JButton("Pause");
    JButton resumeButton = new JButton("Resume");
    JButton stopButton = new JButton("Stop");
    Sound sound = new Sound();
    JSlider slider;


//    JButton volumeUpButton = new JButton("+");
//    JButton volumeDownButton = new JButton("-");

    //File Chooser
    JFileChooser fileChooser;

    FileInputStream fileInputStream;
    BufferedInputStream bufferedInputStream;

    //File
    File myfile = null;
    String filename;
    String filePath;
    long totalLeght;
    long pause;
    Player player;
    Thread playThread;
    Thread resumeThread;

    Application() {
        prepareGUI();
        addActionEvents();
        playThread = new Thread(runnablePlay);
        resumeThread = new Thread(runnableResume);
    }

    public void prepareGUI() {
        //Setting properties
        slider = new JSlider(-80, 6);
        frame = new JFrame();
        frame.setTitle("Music Player");
        frame.getContentPane().setLayout(null);
        frame.getContentPane().setBackground(Color.BLACK);
        frame.setSize(440, 200);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Setting Button
        selectButton.setBounds(140,10,150,30);
        frame.add(selectButton);

        //Setting song name
        songNameLabel.setBounds(100,50,300,30);
        songNameLabel.setForeground(Color.WHITE);
        frame.add(songNameLabel);

        //Setting play button
        playButton.setBounds(30,110,90,30);
        frame.add(playButton);

        //Setting pause button
        pauseButton.setBounds(120,110,90,30);
        frame.add(pauseButton);

        //Setting resume button
        resumeButton.setBounds(210,110,90,30);
        frame.add(resumeButton);

        //Setting stop button
        stopButton.setBounds(300,110,90,30);
        frame.add(stopButton);

        slider.setBounds(30, 100,360,10);
        slider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                sound.currentVolume = slider.getValue();
                sound.fc.setValue(sound.currentVolume);
            }
        });
        frame.add(slider);

        //Setting volume button
//        volumeUpButton.setBounds(30,75,45,20);
//        frame.add(volumeUpButton);
//        volumeDownButton.setBounds(75,75,45,20);
//        frame.add(volumeDownButton);

    }

    public void addActionEvents() {
        selectButton.addActionListener(this);
        playButton.addActionListener(this);
        pauseButton.addActionListener(this);
        resumeButton.addActionListener(this);
        stopButton.addActionListener(this);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == selectButton) {
            //Select Song
            fileChooser = new JFileChooser();
            fileChooser.setCurrentDirectory(new File("E:\\AssignmentAP\\MusicPlayerApp\\Music"));
            fileChooser.setDialogTitle("Select Song");
            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            fileChooser.setFileFilter(new FileNameExtensionFilter("Mp3 files", "mp3"));
            if(fileChooser.showOpenDialog(selectButton) == JFileChooser.APPROVE_OPTION) {
                myfile = fileChooser.getSelectedFile();
                filename = fileChooser.getSelectedFile().getName();
                filePath = fileChooser.getSelectedFile().getPath();
            }
        }
        if(e.getSource() == playButton) {
            //Start
            playThread.start();
            songNameLabel.setText("Now Playing..." + filename);
        }
        if(e.getSource() == pauseButton) {
            //Pause
            if(player != null) {
                try {
                    pause = fileInputStream.available();
                    player.close();
                    songNameLabel.setText("Paused");
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
        if(e.getSource() == resumeButton) {
            //Resume
            resumeThread.start();
        }
        if(e.getSource() == stopButton) {
            //Stop
            if(player != null) {
                player.close();
                songNameLabel.setText("Stoped");
            }
        }

    }

    Runnable runnablePlay = new Runnable() {
        @Override
        public void run() {
            try {
                //Play btn
                fileInputStream = new FileInputStream(myfile);
                bufferedInputStream = new BufferedInputStream(fileInputStream);
                player = new Player(bufferedInputStream);
                totalLeght = fileInputStream.available();
                player.play();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JavaLayerException e) {
                e.printStackTrace();
            }
        }
    };

    Runnable runnableResume = new Runnable() {
        @Override
        public void run() {
            try {
                //Resume
                fileInputStream = new FileInputStream(myfile);
                bufferedInputStream = new BufferedInputStream(fileInputStream);
                player = new Player(bufferedInputStream);
                fileInputStream.skip(totalLeght-pause);
                player.play();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JavaLayerException e) {
                e.printStackTrace();
            }
        }
    };
}
