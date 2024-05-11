package com.nmmedit.protect;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;

/**
 * 目标版本33以上使用
 */
public class ApkSigner {
    public static void main(String[] args) {




//        String workingDir = System.getProperty("user.dir");
//        System.out.println(workingDir);
    }

    public static void signApk(String apkPath, String keyPath, String alias, String storePassword, String keyPassword) {
        try {
            System.out.println("APK签名开始");
            String workingDir = System.getProperty("user.dir");
            System.out.println("batPath:" + workingDir);
            String batPath  = workingDir.replace("\\","/")+"/34.0.0/apksigner_user.bat";
            System.out.println(batPath);
            System.out.println("batPath: "+ new File(batPath).exists());
            System.out.println("apkPath: "+ new File(apkPath).exists());
            System.out.println("keyPath: "+ new File(keyPath).exists());
            String outName = "已签名-"+ new File(apkPath).getName();
            String outPath = new File(apkPath).getParent();
            System.out.println("outPath: "+ new File(outPath,outName).getAbsolutePath());


            ProcessBuilder processBuilder = new ProcessBuilder().command(
                    "cmd", "/c", batPath,
                    "sign",
                    "--ks", keyPath,
                    "--ks-key-alias", alias,
                    "--ks-pass", "pass:" + storePassword,
                    "--key-pass", "pass:" + storePassword,
                    "--out", new File(outPath, outName).getAbsolutePath(),
                    "--v1-signing-enabled", "true",
                    "--v2-signing-enabled", "true",
                    "--v3-signing-enabled", "true",
                    "--v4-signing-enabled", "true",
                    apkPath
            );
            Process process = processBuilder.start();
            process.waitFor();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.err.println(line);
            }
            if (process.exitValue() == 0) {
                System.out.println("APK签名成功!");
                System.out.println("点击打开目录: "+new File(outPath,outName).getParent());
            } else {
                System.err.println("APK签名失败! "+ process);
            }


        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
