// H4M Encoder GUI by Matt McCullough
// This is to encode H4M files

import ui.H4MEncoderUI;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {

        String OS = System.getProperty("os.name").toLowerCase();
        if (OS.contains("windows")) {
            H4MEncoderUI h4MEncoderUI = new H4MEncoderUI();
            h4MEncoderUI.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            h4MEncoderUI.pack();
            h4MEncoderUI.setVisible(true);
        }
        else {
            System.exit(0);
        }
    }
}