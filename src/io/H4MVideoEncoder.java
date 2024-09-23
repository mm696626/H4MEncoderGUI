package io;

import java.io.IOException;

public class H4MVideoEncoder {

    public void encodeVideo(String commandString, String videoFilePath) throws IOException {

        String dumpBMPCommand = "ffmpeg.exe -i" + " \"" + videoFilePath + "\"" + " frame%05d.bmp";
        String[] command = {"cmd.exe", "/c", "start", "cmd.exe", "/c", "cd tools && " + dumpBMPCommand + " && " + commandString + " && *.bmp"};
        ProcessBuilder processBuilder = new ProcessBuilder(command);
        processBuilder.start();
    }
}
