package com.nmmedit.protect;

import com.nmmedit.apkprotect.ApkFolders;
import com.nmmedit.apkprotect.ApkProtect;
import com.nmmedit.apkprotect.deobfus.MappingReader;
import com.nmmedit.apkprotect.dex2c.converter.ClassAnalyzer;
import com.nmmedit.apkprotect.dex2c.converter.instructionrewriter.RandomInstructionRewriter;
import com.nmmedit.apkprotect.dex2c.filters.*;

import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * 如果出现APk无法安装问题请参考
 * https://github.com/maoabc/nmmp/issues/72
 * 一般是在安卓项目中的app模块中的清单文件的   <application 加入: android:extractNativeLibs="true"
 */
public class ApkMain {

    public static void main(String[] args) throws IOException {
        System.out.println("开始执行");
        if (args.length < 1) {
            System.err.println("No Input apk.");
            System.err.println("<inApk> [<convertRuleFile> mapping.txt]");
            return;
        }
        final File apk = new File(args[0]);
        final File outDir = new File(apk.getParentFile(), "build");

        ClassAndMethodFilter filterConfig = new BasicKeepConfig();
        final SimpleRules simpleRules = new SimpleRules();
        if (args.length > 1) {
            simpleRules.parse(new StringReader(args[1]));
        } else {
            //all classes
            simpleRules.parse(new StringReader("class *"));
        }


        filterConfig = new SimpleConvertConfig(new BasicKeepConfig(), simpleRules);


        final ClassAnalyzer classAnalyzer = new ClassAnalyzer();
        //todo 可能需要加载某些厂商私有的sdk


        final ApkFolders apkFolders = new ApkFolders(apk, outDir);

        final ApkProtect apkProtect = new ApkProtect.Builder(apkFolders)
                .setInstructionRewriter(new RandomInstructionRewriter())
                .setFilter(filterConfig)
                .setClassAnalyzer(classAnalyzer)
                .build();
        apkProtect.run();

        if (args.length > 2){
            String apkPath = args[2];
            String keyPath = args[3];
            String alias = args[4];
            String storePassword = args[5];
            String keyPassword = args[6];
            apkPath = apkPath.replace("\\","/");
            keyPath = keyPath.replace("\\","/");

            ApkSigner.signApk(apkPath, keyPath, alias, storePassword, keyPassword);
        }

    }
}
