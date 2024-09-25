package ui;

import io.CommandStringBuilder;
import io.H4MVideoEncoder;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

public class H4MEncoderUI extends JFrame implements ActionListener {

    private JButton videoFilePicker, waveFilePicker, encodeH4M;
    private JLabel qualityLabel, videoFileNameLabel, waveFileNameLabel, qualityValueLabel;
    private JSlider qualitySlider;
    private JTextField waveFileTextField, videoFileTextField;
    private String videoFilePath, waveFilePath;

    GridBagConstraints gridBagConstraints;


    public H4MEncoderUI()
    {
        setTitle("H4M Encoder");
        generateUI();
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch (Exception e) {
            System.exit(0);
        }

        File tempFolder = new File("temp");
        if (tempFolder.exists()) {
            deleteDirectory(tempFolder.getAbsolutePath());
        }
    }

    private void generateUI() {
        videoFileNameLabel = new JLabel("Input Video File");
        videoFileTextField = new JTextField();
        videoFileTextField.setColumns(10);
        videoFileTextField.setEditable(false);
        videoFilePicker = new JButton("Browse for Video File");
        videoFilePicker.addActionListener(this);

        waveFileNameLabel = new JLabel("Input WAV File");
        waveFileTextField = new JTextField();
        waveFileTextField.setColumns(10);
        waveFileTextField.setEditable(false);
        waveFilePicker = new JButton("Browse for WAV File");
        waveFilePicker.addActionListener(this);

        qualityLabel = new JLabel("Video Quality (higher values mean lower quality)");
        qualitySlider = new JSlider(5, 1000, 400);
        qualitySlider.setPaintTicks(true);
        qualitySlider.setMajorTickSpacing(50);
        qualitySlider.setMinorTickSpacing(10);

        qualityValueLabel = new JLabel("400");

        encodeH4M = new JButton("Encode H4M");
        encodeH4M.addActionListener(this);

        qualitySlider.addChangeListener(e -> qualityValueLabel.setText(String.valueOf(qualitySlider.getValue())));

        setLayout(new GridBagLayout());
        gridBagConstraints = new GridBagConstraints();

        addComponent(videoFileNameLabel, 0, 0);
        addComponent(videoFileTextField, 1, 0);
        addComponent(videoFilePicker, 2, 0);

        addComponent(waveFileNameLabel, 0, 1);
        addComponent(waveFileTextField, 1, 1);
        addComponent(waveFilePicker, 2, 1);

        addComponent(qualityLabel, 0, 2);
        addComponent(qualitySlider, 1, 2);
        addComponent(qualityValueLabel, 2, 2);

        addComponent(encodeH4M, 2, 3);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == videoFilePicker) {
            JFileChooser fileChooser = new JFileChooser();
            FileNameExtensionFilter videoFileFilter = new FileNameExtensionFilter("Video File","avi", "mkv", "mp4", "mov", "AVI", "MKV", "MP4", "MOV");
            fileChooser.setFileFilter(videoFileFilter);
            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            int response = fileChooser.showOpenDialog(null);
            if (response == JFileChooser.APPROVE_OPTION) {
                videoFilePath = fileChooser.getSelectedFile().getAbsolutePath();
                videoFileTextField.setText(videoFilePath);
            } else {
                return;
            }
        }

        if (e.getSource() == waveFilePicker) {
            JFileChooser fileChooser = new JFileChooser();
            FileNameExtensionFilter waveFileFilter = new FileNameExtensionFilter("WAV Files","wav", "WAV");
            fileChooser.setFileFilter(waveFileFilter);
            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            int response = fileChooser.showOpenDialog(null);
            if (response == JFileChooser.APPROVE_OPTION) {
                waveFilePath = fileChooser.getSelectedFile().getAbsolutePath();
                waveFileTextField.setText(waveFilePath);
            } else {
                return;
            }
        }

        if (e.getSource() == encodeH4M) {
            CommandStringBuilder commandStringBuilder = new CommandStringBuilder();
            String commandString = null;
            try {
                commandString = commandStringBuilder.buildCommandString(videoFilePath, waveFilePath, qualitySlider.getValue());
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            H4MVideoEncoder h4MVideoEncoder = new H4MVideoEncoder();
            try {
                h4MVideoEncoder.encodeVideo(commandString, videoFilePath);
            } catch (IOException | InterruptedException ex) {
                throw new RuntimeException(ex);
            }
        }

    }

    private void addComponent(JComponent component, int gridX, int gridY) {
        gridBagConstraints.gridx = gridX;
        gridBagConstraints.gridy = gridY;
        add(component, gridBagConstraints);
    }

    private boolean deleteDirectory(String folderPath) {

        File[] folderFileList = new File(folderPath).listFiles();

        //Grab all files and check subfolders if the file is a directory
        for (File file: folderFileList) {
            if (file.isDirectory()) {
                deleteDirectory(file.getAbsolutePath());
            }

            file.delete();
        }

        File folder = new File(folderPath);
        return folder.delete();
    }
}