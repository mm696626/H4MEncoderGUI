package io;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class H4MVideoEncoder {

    public void encodeVideo(String commandString, String videoFilePath) throws IOException, InterruptedException {

        File toolsFolder = new File("tools");
        String toolsFolderPath = toolsFolder.getAbsolutePath();
        String tempFolderPath = toolsFolderPath.substring(0, toolsFolderPath.lastIndexOf("tools")) + "temp";
        String outputFolderPath = toolsFolderPath.substring(0, toolsFolderPath.lastIndexOf("tools")) + "output";

        createFolder(tempFolderPath);
        createFolder(outputFolderPath);

        copyToolsToTempFolder(toolsFolderPath, tempFolderPath);

        String dumpWAVCommand = "ffmpeg.exe -i" + " \"" + videoFilePath + "\"" + " -acodec pcm_s16le -ar 44100 -ac 2 audio.wav";
        String dumpBMPCommand = "ffmpeg.exe -i" + " \"" + videoFilePath + "\"" + " frame%05d.bmp";
        String h4mFile = commandString.substring(commandString.lastIndexOf(" ") + 1);

        String[] commands = {"cmd.exe", "/c", "start", "cmd.exe", "/c", "cd temp && " + dumpWAVCommand + " && " + dumpBMPCommand + " && " + commandString + " && del *.bmp && del audio.wav && move " + h4mFile + " ../output/" + h4mFile};
        ProcessBuilder processBuilder = new ProcessBuilder(commands);
        processBuilder.start();
    }

    private void createFolder(String folderPath) {
        File folder = new File(folderPath);
        if (folder.exists()) {
            folder.delete();
        }
        folder.mkdirs();
    }

    private void copyToolsToTempFolder(String toolsFolderPath, String tempFolderPath) throws IOException {
        File toolsFolder = new File(toolsFolderPath);

        File[] toolsFiles = toolsFolder.listFiles();
        String fileSeparator = tempFolderPath.substring(0, tempFolderPath.lastIndexOf("temp"));
        fileSeparator = fileSeparator.substring(fileSeparator.length() - 1);

        if (toolsFiles != null) {
            for (int i=0; i<toolsFiles.length; i++) {
                String copiedTempFilePath = tempFolderPath + fileSeparator + toolsFiles[i].getName();
                File copiedTempFile = new File(copiedTempFilePath);
                Files.copy(toolsFiles[i].toPath(), copiedTempFile.toPath(), StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.COPY_ATTRIBUTES);
            }
        }
    }
}
