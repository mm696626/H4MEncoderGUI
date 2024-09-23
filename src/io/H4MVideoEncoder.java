package io;

import java.io.IOException;

public class H4MVideoEncoder {

    public void encodeVideo(String commandString, String videoFilePath) throws IOException, InterruptedException {

        String dumpBMPCommand = "ffmpeg.exe -i" + " \"" + videoFilePath + "\"" + " frame%05d.bmp";
        String h4mFile = commandString.substring(commandString.lastIndexOf(" ") + 1);
        String[] commands = {"cmd.exe", "/c", "start", "cmd.exe", "/c", "cd tools && " + dumpBMPCommand + " && " + commandString + " && del *.bmp && move " + h4mFile + " ../" + h4mFile};
        ProcessBuilder processBuilder = new ProcessBuilder(commands);
        processBuilder.start();
    }
}
