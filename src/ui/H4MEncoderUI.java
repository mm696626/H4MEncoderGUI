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

    private JButton videoFilePicker, encodeH4M;
    private JLabel qualityLabel, videoFileNameLabel, qualityValueLabel;
    private JSlider qualitySlider;
    private JTextField videoFileTextField;
    private String videoFilePath;

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

        deleteTempDirectory();
    }

    private void generateUI() {
        videoFileNameLabel = new JLabel("Input Video File");
        videoFileTextField = new JTextField();
        videoFileTextField.setColumns(10);
        videoFileTextField.setEditable(false);
        videoFilePicker = new JButton("Browse for Video File");
        videoFilePicker.addActionListener(this);

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

        addComponent(qualityLabel, 0, 1);
        addComponent(qualitySlider, 1, 1);
        addComponent(qualityValueLabel, 2, 1);

        addComponent(encodeH4M, 2, 2);
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

        if (e.getSource() == encodeH4M) {

            if (videoFileTextField.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "No video was chosen!");
                return;
            }

            deleteTempDirectory();

            File videoFile = new File(videoFilePath);

            if (videoFile.exists()) {
                CommandStringBuilder commandStringBuilder = new CommandStringBuilder();
                H4MVideoEncoder h4MVideoEncoder = new H4MVideoEncoder();
                String commandString = null;
                try {
                    commandString = commandStringBuilder.buildCommandString(videoFilePath, qualitySlider.getValue());
                    h4MVideoEncoder.encodeVideo(commandString, videoFilePath);
                } catch (IOException | InterruptedException ex) {
                    throw new RuntimeException(ex);
                }
            }
            else {
                JOptionPane.showMessageDialog(this, "The video file you tried to encode doesn't exist!");
            }

            videoFileTextField.setText("");
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

    private void deleteTempDirectory() {
        File tempFolder = new File("temp");
        if (tempFolder.exists()) {
            deleteDirectory(tempFolder.getAbsolutePath());
        }
    }
}