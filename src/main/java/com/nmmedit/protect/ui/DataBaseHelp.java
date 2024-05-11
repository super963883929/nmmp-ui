package com.nmmedit.protect.ui;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DataBaseHelp {
    static {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            System.err.println("Failed to load JDBC driver");
            e.printStackTrace();
        }
    }
    static String url = "";
    public static void init() {
        String userHome = System.getProperty("user.home");


        File dbDirectory = new File(userHome + File.separator + "Nmmp");
        if (!dbDirectory.exists()) {
            dbDirectory.mkdirs(); // 创建目录
        }

        File dbFile = new File(dbDirectory, "appdata.db");
        if (!dbFile.exists()) {
            try {
                dbFile.createNewFile(); // 创建新文件
            } catch (IOException e) {
                e.printStackTrace();
                // 处理异常，比如提醒用户检查磁盘空间或权限问题
            }
        }

         url = "jdbc:sqlite:" + dbFile.getAbsolutePath();
        createTable();
    }

    private static void createTable() {
        String sqlCreate = "CREATE TABLE IF NOT EXISTS configurations (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "apkTitle TEXT," +
                "apkPath TEXT," +
                "proguardRules TEXT," +
                "outputPath TEXT," +
                "signPath TEXT," +
                "alias TEXT," +
                "signPassword TEXT," +
                "storePassword TEXT)";
        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement()) {
             stmt.execute(sqlCreate);
        } catch (SQLException e) {
            System.err.println("Error creating table in SQLite database");
            e.printStackTrace();
        }
    }

    public static boolean updateConfiguration(AppData appData) {
        String sqlUpdate = "UPDATE configurations SET apkTitle = ?, apkPath = ?, proguardRules = ?, " +
                "outputPath = ?, signPath = ?, alias = ?, signPassword = ?, storePassword = ? " +
                "WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sqlUpdate)) {
            // Set parameters
            pstmt.setString(1, appData.apkTitle);
            pstmt.setString(2, appData.apkPath);
            pstmt.setString(3, appData.proguardRules);
            pstmt.setString(4, appData.outputPath);
            pstmt.setString(5, appData.signPath);
            pstmt.setString(6, appData.alias);
            pstmt.setString(7, appData.signPassword);
            pstmt.setString(8, appData.storePassword);
            pstmt.setInt(9, appData.id);

            // Execute update
            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Record updated successfully!");
                return true;
            } else {
                System.out.println("No records updated!");
                return false;
            }
        } catch (SQLException e) {
            System.err.println("Error updating record in SQLite database");
            e.printStackTrace();
            return false;
        }
    }

    public static void updateConfiguration(int id, String apkTitle, String apkPath, String proguardRules,
                                    String outputPath, String signPath, String alias,
                                    String signPassword, String storePassword) {
        String sqlUpdate = "UPDATE configurations SET apkTitle = ?, apkPath = ?, proguardRules = ?, " +
                "outputPath = ?, signPath = ?, alias = ?, signPassword = ?, storePassword = ? " +
                "WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sqlUpdate)) {
            // Set parameters
            pstmt.setString(1, apkTitle);
            pstmt.setString(2, apkPath);
            pstmt.setString(3, proguardRules);
            pstmt.setString(4, outputPath);
            pstmt.setString(5, signPath);
            pstmt.setString(6, alias);
            pstmt.setString(7, signPassword);
            pstmt.setString(8, storePassword);
            pstmt.setInt(9, id);

            // Execute update
            pstmt.executeUpdate();

            System.out.println("Record updated successfully!");
        } catch (SQLException e) {
            System.err.println("Error updating record in SQLite database");
            e.printStackTrace();
        }
    }

    public static void add(
            String apkTitle,
            String apkPathField,
                           String proguardRulesArea,
                           String outputPathField,
                           String signPathField,
                           String aliasField,
                           String signPasswordField,
                           String storePasswordField){
        try (Connection conn = DriverManager.getConnection(url)) {
            String sql = "INSERT INTO configurations (apkTitle, apkPath, proguardRules, outputPath, signPath, alias, signPassword, storePassword) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, apkTitle);
                pstmt.setString(2, apkPathField);
                pstmt.setString(3, proguardRulesArea);
                pstmt.setString(4, outputPathField);
                pstmt.setString(5, signPathField);
                pstmt.setString(6, aliasField);
                pstmt.setString(7, signPasswordField);
                pstmt.setString(8, storePasswordField);
                pstmt.executeUpdate();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

    }


    public static List<AppData> queryAll(){
        List<AppData> list = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(url)) {
            String query = "SELECT * FROM configurations";
            try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
                while (rs.next()) {
                    // 读取每一列的数据，假设你知道数据类型和列名
                    int id = rs.getInt("id");
                    String apkTitle = rs.getString("apkTitle");
                    String apkPath = rs.getString("apkPath");
                    String proguardRules = rs.getString("proguardRules");
                    String outputPath = rs.getString("outputPath");
                    String signPath = rs.getString("signPath");
                    String alias = rs.getString("alias");
                    String signPassword = rs.getString("signPassword");
                    String storePassword = rs.getString("storePassword");

                    AppData appData = new AppData(
                            apkTitle,
                            apkPath,
                            proguardRules,
                            outputPath,
                            signPath,
                            alias,
                            signPassword,
                            storePassword

                    );
                    appData.setId(id);
                    list.add(appData);

//                    // 打印读取的数据
//                    System.out.println("ID: " + id);
//                    System.out.println("APK Title: " + apkTitle);
//                    System.out.println("APK Path: " + apkPath);
//                    System.out.println("Proguard Rules: " + proguardRules);
//                    System.out.println("Output Path: " + outputPath);
//                    System.out.println("Sign Path: " + signPath);
//                    System.out.println("Alias: " + alias);
//                    System.out.println("Sign Password: " + signPassword);
//                    System.out.println("Store Password: " + storePassword);
//                    System.out.println("-------------------------------");
                }
            }
        } catch (SQLException e) {
            System.out.println("Error connecting to SQLite database");
            e.printStackTrace();
        }
        return list;

    }

}
