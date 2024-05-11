package com.nmmedit.protect.ui;

import com.nmmedit.protect.ApkMain;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.*;
import java.io.File;

import java.io.IOException;
import java.io.PrintStream;
import java.net.URISyntaxException;
import java.util.List;

public class GUIExample extends JFrame {
    private JProgressBar progressBar;
    public static AppData currentAppData = null;
    /**
     * 选择框
     */
    private final JComboBox<String> comboBox;
    /**
     * 混淆规则
     */
    private final JTextArea proguardRulesArea;
    /**
     * apk路径
     */
    private final JTextField apkPathField;
    /**
     * 输出路径
     */
    private final JTextField outputPathField;
    private final JTextField signPathField;
    private final JTextField aliasField;
    private final JTextField signPasswordField;
    private final JTextField storePasswordField;
    /**
     * 控制台文本
     */
    private JTextArea textArea;
    public GUIExample() {
        setTitle("App Nmmp加密");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(600, 800)); // 设置首选大小

        DataBaseHelp.init();

        textArea = new JTextArea(10, 20);
        textArea.setEditable(false);
        // 设置 JTextArea 的字体为支持中文的字体
        textArea.setFont(new Font("宋体", Font.PLAIN, 12));
        JScrollPane controlConsolePane = new JScrollPane(textArea);


        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // 添加内边距

        add(mainPanel, BorderLayout.CENTER);

        comboBox = new JComboBox<>();
        List<AppData> list = DataBaseHelp.queryAll();
        if (!list.isEmpty()){
            currentAppData = list.get(0);
        }
        for (AppData appData : list){
            comboBox.addItem(appData.apkTitle);
        }
        JPanel comboBoxFieldPanel = new JPanel();
        comboBoxFieldPanel.add(comboBox);
        comboBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    JComboBox comboBox = (JComboBox)e.getSource();
                    String selectedOption = (String)comboBox.getSelectedItem();
                    System.out.println("Selected option: " + selectedOption);
                    for (AppData appData : list){
                        if (appData.apkTitle.equals(selectedOption) ){
                            currentAppData = appData;
                            loadData();
                            break;
                        }
                    }
                }
            }
        });
        mainPanel.add(comboBoxFieldPanel);
        // 添加按钮
        JButton addButton = new JButton("添加");
        comboBoxFieldPanel.add(addButton);
        addButton.setAlignmentX(Component.LEFT_ALIGNMENT); // 左对齐
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 在这里添加弹出添加对话框的逻辑
                AddDialog dialog = new AddDialog(GUIExample.this);
                dialog.showDialog();
                dialog.addWindowListener(new WindowListener() {
                    @Override
                    public void windowOpened(WindowEvent e) {

                    }

                    @Override
                    public void windowClosing(WindowEvent e) {

                    }

                    @Override
                    public void windowClosed(WindowEvent e) {
                        currentAppData = DataBaseHelp.queryAll().get(DataBaseHelp.queryAll().size()-1);

                        comboBox.addItem(currentAppData.apkTitle);
                        comboBox.setSelectedItem(currentAppData.apkTitle);

                        loadData();
                    }

                    @Override
                    public void windowIconified(WindowEvent e) {

                    }

                    @Override
                    public void windowDeiconified(WindowEvent e) {

                    }

                    @Override
                    public void windowActivated(WindowEvent e) {

                    }

                    @Override
                    public void windowDeactivated(WindowEvent e) {

                    }
                });
            }
        });
        outputPathField = new JTextField(20);
        // APK路径
        JPanel apkPanel = new JPanel();
        apkPanel.add(new JLabel("APK 路径:"));
        apkPathField = new JTextField(20);
        if (currentAppData!=null){
            apkPathField.setText(currentAppData.apkPath);
        }
        apkPanel.add(apkPathField);
        JButton browseApkButton = new JButton("浏览...");
        apkPanel.add(apkPathField);
        browseApkButton.addActionListener(e -> {

            String path =  selectDirectoryChooser();
            apkPathField.setText(path);
//            JFileChooser chooser = new JFileChooser();
//            if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
//                apkPathField.setText(chooser.getSelectedFile().getAbsolutePath());
//            }
        });
        apkPathField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updateLabel();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateLabel();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                // Plain text components don't fire these events
            }

            private void updateLabel() {
                String parent = new File(apkPathField.getText()).getParent();
                outputPathField.setText(parent+"\\build\\app-release-protect.apk");
            }
        });

        apkPanel.add(browseApkButton);
        mainPanel.add(apkPanel);


        // 混淆规则
        JPanel proguardPanel = new JPanel();
        proguardPanel.setLayout(new BoxLayout(proguardPanel, BoxLayout.Y_AXIS));
        proguardPanel.add(new JLabel("混淆规则:"));
        proguardRulesArea = new JTextArea(8, 20);
        if (currentAppData!=null){
            proguardRulesArea.setText(currentAppData.proguardRules);
        }

        proguardRulesArea.setLineWrap(true);
        JScrollPane scrollPane = new JScrollPane(proguardRulesArea);

        proguardPanel.add(scrollPane,BorderLayout.CENTER);
        mainPanel.add(proguardPanel,BorderLayout.CENTER);

        // 输出路径
        JPanel outputPathPanel = new JPanel();
        if (currentAppData!=null){
            outputPathField.setText(currentAppData.outputPath);
        }
        outputPathPanel.setBorder(new EmptyBorder(5, 5, 5, 5)); // 设置内边距
        outputPathPanel.add(new JLabel("输出路径:"));
        outputPathPanel.add(outputPathField);
        mainPanel.add(outputPathPanel);

        // 签名路径
        JPanel signPathPanel = new JPanel();
        signPathPanel.setBorder(new EmptyBorder(5, 5, 5, 5)); // 设置内边距
        signPathPanel.add(new JLabel("签名路径:"));
        signPathField = new JTextField(20);
        if (currentAppData!=null){
            signPathField.setText(currentAppData.signPath);
        }
        signPathPanel.add(signPathField);
        JButton btnSelectSigFile = new JButton("浏览...");
        btnSelectSigFile.addActionListener(e -> {

            String path =  selectDirectoryChooser();
            signPathField.setText(path);

        });
        signPathPanel.add(btnSelectSigFile);
        mainPanel.add(signPathPanel);

        // Alias
        JPanel aliasPanel = new JPanel();
        aliasPanel.add(new JLabel("Alias:"));
        aliasField = new JTextField(20);
        if (currentAppData!=null){
            aliasField.setText(currentAppData.alias);
        }
        aliasPanel.add(aliasField);
        mainPanel.add(aliasPanel);

        // 签名密码
        JPanel signPasswordFieldPanel = new JPanel();
        signPasswordFieldPanel.add(new JLabel("签名密码:"));
        signPasswordField = new JTextField(20);
        if (currentAppData!=null){
            signPasswordField.setText(currentAppData.signPassword);
        }
        signPasswordFieldPanel.add(signPasswordField);
        mainPanel.add(signPasswordFieldPanel);

        // Store密码
        JPanel storePasswordFieldPanel = new JPanel();
        storePasswordFieldPanel.add(new JLabel("Store密码:"));
        storePasswordField = new JTextField(20);
        if (currentAppData!=null){
            storePasswordField.setText(currentAppData.storePassword);
        }
        storePasswordFieldPanel.add(storePasswordField);
        mainPanel.add(storePasswordFieldPanel);



        JPanel bntPanel = new JPanel();
        // 开始执行按钮
        JButton saveButton = new JButton("保存修改");
        saveButton.setAlignmentX(Component.LEFT_ALIGNMENT); // 左对齐
        saveButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                currentAppData.apkPath = apkPathField.getText().trim();
                currentAppData.proguardRules = proguardRulesArea.getText();
                currentAppData.outputPath = outputPathField.getText().trim();
                currentAppData.signPath = signPathField.getText().trim();
                currentAppData.alias = aliasField.getText().trim();
                currentAppData.storePassword = storePasswordField.getText().trim();
                currentAppData.signPassword = signPasswordField.getText().trim();

                if (DataBaseHelp.updateConfiguration(currentAppData)){

                    JOptionPane.showMessageDialog(null, "修改成功");

                }else{
                    JOptionPane.showMessageDialog(null, "修改失败");

                }

            }
        });
        bntPanel.add(saveButton);

        // 开始执行按钮
        JButton startButton = new JButton("开始执行");
        startButton.setAlignmentX(Component.LEFT_ALIGNMENT); // 左对齐
        startButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                start();
            }
        });
        bntPanel.add(startButton);

        // 打开目录按钮
        JButton openDirectoryButton = new JButton("打开目录");
        openDirectoryButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openTheDir( new File(currentAppData.outputPath).getParent());
            }
        });
        openDirectoryButton.setAlignmentX(Component.LEFT_ALIGNMENT); // 左对齐
        bntPanel.add(openDirectoryButton);
        mainPanel.add(bntPanel);

        // 创建一个进度条，并设置初始值和最大值
        progressBar = new JProgressBar();
        progressBar.setIndeterminate(true);
        progressBar.setString("正在执行...");
        progressBar.setBorder(new EmptyBorder(20, 5, 20, 5)); // 设置内边距
        mainPanel.add(progressBar, BorderLayout.CENTER);
        progressBar.setVisible(false);



        mainPanel.add(controlConsolePane, BorderLayout.CENTER);

        // 重定向标准输出流到文本区域
        PrintStream printStream = new PrintStream(new CustomOutputStream(textArea));
        System.setOut(printStream);



        pack();
        setLocationRelativeTo(null); // 将窗口放置在屏幕中央


        try {
            String path = new File(GUIExample.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getPath();
            path = path.replace("\\","/")+"/34.0.0/apksigner_user.bat";
            System.out.println("batPath:" + path);

            String workingDir = System.getProperty("user.dir");
            System.out.println("batPath2:" + workingDir);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    private boolean isRunIng  = false;
    private void start(){
        if (isRunIng){
            JOptionPane.showMessageDialog(null, "请等待执行完毕");
            return;
        }
        new Thread(() -> {
            isRunIng = true;
            textArea.setText("");
            progressBar.setVisible(true);
            progressBar.setString("正在运行...");
            String[] args = new String[]{
                    apkPathField.getText(),
                    proguardRulesArea.getText(),
                    outputPathField.getText(),
                    signPathField.getText(),
                    aliasField.getText(),
                    storePasswordField.getText(),
                    signPasswordField.getText(),

            };
            try {
                ApkMain.main(args);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            isRunIng = false;
            progressBar.setVisible(false);
            JOptionPane.showMessageDialog(null, "加密完成");
        }).start();

    }

    private void loadData(){
        apkPathField.setText(currentAppData.apkPath);
        proguardRulesArea.setText(currentAppData.proguardRules);
                outputPathField.setText(currentAppData.outputPath);
                signPathField.setText(currentAppData.signPath);
                aliasField.setText(currentAppData.alias);
                storePasswordField.setText(currentAppData.storePassword);
                signPasswordField.setText(currentAppData.signPassword);
    }


    private String selectDirectoryChooser() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Select a directory");
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            return selectedFile.getAbsolutePath();
        }else{
            return "";
        }
    }


    private void openTheDir( String directoryPath ){

            // 检查当前平台是否支持Desktop类
            if (!Desktop.isDesktopSupported()) {
                System.out.println("Desktop is not supported");
                return;
            }

            // 获取Desktop实例
            Desktop desktop = Desktop.getDesktop();

            // 检查指定目录是否存在
            File directory = new File(directoryPath);
            if (!directory.exists()) {
                System.out.println("Directory does not exist");
                return;
            }
            // 使用系统默认的文件浏览器打开指定目录
            try {
                desktop.open(directory);
            } catch (IOException e) {
                System.out.println("Error opening directory: " + e.getMessage());
            }

    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new GUIExample().setVisible(true);
            }
        });
    }
}
