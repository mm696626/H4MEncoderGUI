package ui;

import io.CommandStringBuilder;
import io.H4MVideoEncoder;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class H4MEncoderUI extends JFrame implements ActionListener {

    private JButton videoFilePicker, waveFilePicker, encodeH4M;
    private JLabel qualityLabel, frameRateLabel, videoFileNameLabel, waveFileNameLabel, qualityValueLabel, frameRateValueLabel;
    private JSlider qualitySlider, frameRateSlider;
    private JTextField waveFileTextField, videoFileTextField;
    private String videoFilePath, waveFilePath;

    GridBagConstraints gridBagConstraints;


    public H4MEncoderUI()
    {
        setTitle("H4M Encoder");
        generateUI();
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

        qualityLabel = new JLabel("Video Quality");
        qualitySlider = new JSlider(5, 1000, 400);
        qualitySlider.setPaintTicks(true);
        qualitySlider.setMajorTickSpacing(50);
        qualitySlider.setMinorTickSpacing(10);

        frameRateLabel = new JLabel("Video Frame Rate");
        frameRateSlider = new JSlider(1, 120, 30);
        frameRateSlider.setPaintTicks(true);
        frameRateSlider.setMajorTickSpacing(10);
        frameRateSlider.setMinorTickSpacing(1);

        qualityValueLabel = new JLabel("400");
        frameRateValueLabel = new JLabel("30");

        encodeH4M = new JButton("Encode H4M");
        encodeH4M.addActionListener(this);

        qualitySlider.addChangeListener(e -> qualityValueLabel.setText(String.valueOf(qualitySlider.getValue())));
        frameRateSlider.addChangeListener(e -> frameRateValueLabel.setText(String.valueOf(frameRateSlider.getValue())));

        setLayout(new GridBagLayout());
        gridBagConstraints = new GridBagConstraints();

        gridBagConstraints.gridx=0;
        gridBagConstraints.gridy=0;
        add(videoFileNameLabel, gridBagConstraints);

        gridBagConstraints.gridx=1;
        gridBagConstraints.gridy=0;
        add(videoFileTextField, gridBagConstraints);

        gridBagConstraints.gridx=2;
        gridBagConstraints.gridy=0;
        add(videoFilePicker, gridBagConstraints);

        gridBagConstraints.gridx=0;
        gridBagConstraints.gridy=1;
        add(waveFileNameLabel, gridBagConstraints);

        gridBagConstraints.gridx=1;
        gridBagConstraints.gridy=1;
        add(waveFileTextField, gridBagConstraints);

        gridBagConstraints.gridx=2;
        gridBagConstraints.gridy=1;
        add(waveFilePicker, gridBagConstraints);

        gridBagConstraints.gridx=0;
        gridBagConstraints.gridy=2;
        add(qualityLabel, gridBagConstraints);

        gridBagConstraints.gridx=1;
        gridBagConstraints.gridy=2;
        add(qualitySlider, gridBagConstraints);

        gridBagConstraints.gridx=2;
        gridBagConstraints.gridy=2;
        add(qualityValueLabel, gridBagConstraints);

        gridBagConstraints.gridx=0;
        gridBagConstraints.gridy=3;
        add(frameRateLabel, gridBagConstraints);

        gridBagConstraints.gridx=1;
        gridBagConstraints.gridy=3;
        add(frameRateSlider, gridBagConstraints);

        gridBagConstraints.gridx=2;
        gridBagConstraints.gridy=3;
        add(frameRateValueLabel, gridBagConstraints);

        gridBagConstraints.gridx=2;
        gridBagConstraints.gridy=4;
        add(encodeH4M, gridBagConstraints);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == videoFilePicker) {
            JFileChooser fileChooser = new JFileChooser();
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
            String commandString = commandStringBuilder.buildCommandString(videoFilePath, waveFilePath, qualitySlider.getValue(), frameRateSlider.getValue());
            H4MVideoEncoder h4MVideoEncoder = new H4MVideoEncoder();
            try {
                h4MVideoEncoder.encodeVideo(commandString, videoFilePath);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }

    }
}