package com.nmmedit.protect;

import java.io.IOException;
import java.util.Arrays;

public class Main {


    public static void main(String[] args) throws IOException {




        String fileName = args[0];
        int dotIndex = fileName.lastIndexOf(".");
        String suffix = fileName.substring(dotIndex + 1);


        switch (suffix) {
            case "apk" -> {
                ApkMain.main(args);
            }
            case "aab" -> {
                AabMain.main(args);
            }
            case "aar" -> {
                AarMain.main(args);
            }
            default -> {
                System.err.println("Unknown subcommand");
                System.exit(-1);
            }
        }
    }
}
