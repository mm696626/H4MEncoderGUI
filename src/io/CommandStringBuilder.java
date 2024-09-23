package io;

import java.io.File;

public class CommandStringBuilder {

    public String buildCommandString (String videoFilePath, String waveFilePath, int qualitySliderValue, int frameRateSliderValue) {
        String commandString = "";

        double frameRateInMicroseconds = (1.0/frameRateSliderValue) * 1000000;
        String waveFileString = " -w " + "\"" + waveFilePath +  "\"";
        if (waveFilePath == null || waveFilePath.isEmpty()) {
            waveFileString = "";
        }

        File videoFile = new File(videoFilePath);
        String videoFileName = videoFile.getName();
        videoFileName = videoFileName.substring(0, videoFileName.indexOf("."));

        commandString = "hvqm4enc.exe " + "-q " + qualitySliderValue + " -f " + (int)frameRateInMicroseconds + waveFileString + " frame00001.bmp " + videoFileName + ".h4m";

        return commandString;
    }
}
