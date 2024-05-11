package com.nmmedit.protect.ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;

public class AddDialog extends JDialog {


    public AddDialog(JFrame parent) {
        super(parent, "添加对话框", true); // 设置为模态对话框

        Container contentPane = getContentPane();
        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));


        JPanel apkNamePanel = new JPanel();
        apkNamePanel.setBorder(new EmptyBorder(5, 5, 5, 5)); // 设置内边距
        apkNamePanel.add(new JLabel("APK 名称:"));
        JTextField apkNameField = new JTextField(20);
        apkNamePanel.add(apkNameField);
        contentPane.add(apkNamePanel);

        JTextField outputPathField = new JTextField(20);
        // APK路径
        JPanel apkPanel = new JPanel();
        apkPanel.setBorder(new EmptyBorder(5, 5, 5, 5)); // 设置内边距
        apkPanel.add(new JLabel("APK 路径:"));
        JTextField apkPathField = new JTextField(20);
        apkPanel.add(apkPathField);
        JButton browseApkButton = new JButton("浏览...");
        browseApkButton.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                apkPathField.setText(chooser.getSelectedFile().getAbsolutePath());
            }
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
                outputPathField.setText(parent + "\\build\\app-release-protect.apk");
            }
        });
        apkPanel.add(browseApkButton);
        contentPane.add(apkPanel);

        // 混淆规则
        JPanel proguardPanel = new JPanel();
        proguardPanel.setBorder(new EmptyBorder(5, 5, 5, 5)); // 设置内边距
        proguardPanel.add(new JLabel("混淆规则:"));
        JTextArea proguardRulesArea = new JTextArea(5, 20);
        proguardRulesArea.setLineWrap(true);
        JScrollPane scrollPane = new JScrollPane(proguardRulesArea);
        proguardPanel.add(scrollPane);
        contentPane.add(proguardPanel);

        // 输出路径
        JPanel outputPathPanel = new JPanel();
        outputPathPanel.setBorder(new EmptyBorder(5, 5, 5, 5)); // 设置内边距
        outputPathPanel.add(new JLabel("输出路径:"));
        outputPathPanel.add(outputPathField);
        contentPane.add(outputPathPanel);

        // 签名路径
        JPanel signPathPanel = new JPanel();
        signPathPanel.setBorder(new EmptyBorder(5, 5, 5, 5)); // 设置内边距
        signPathPanel.add(new JLabel("签名路径:"));
        JTextField signPathField = new JTextField(20);
        signPathPanel.add(signPathField);
        JButton btnSelectSigFile = new JButton("浏览...");
        btnSelectSigFile.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                signPathField.setText(chooser.getSelectedFile().getAbsolutePath());
            }
        });
        signPathPanel.add(btnSelectSigFile);
        contentPane.add(signPathPanel);

        // Alias
        JPanel aliasPanel = new JPanel();
        aliasPanel.setBorder(new EmptyBorder(5, 5, 5, 5)); // 设置内边距
        aliasPanel.add(new JLabel("Alias:"));
        JTextField aliasField = new JTextField(20);
        aliasPanel.add(aliasField);
        contentPane.add(aliasPanel);

        // 签名密码
        JPanel signPasswordFieldPanel = new JPanel();
        signPasswordFieldPanel.setBorder(new EmptyBorder(5, 5, 5, 5)); // 设置内边距
        signPasswordFieldPanel.add(new JLabel("签名密码:"));
        JTextField signPasswordField = new JTextField(20);
        signPasswordFieldPanel.add(signPasswordField);
        contentPane.add(signPasswordFieldPanel);

        // Store密码
        JPanel storePasswordFieldPanel = new JPanel();
        storePasswordFieldPanel.setBorder(new EmptyBorder(5, 5, 5, 5)); // 设置内边距
        storePasswordFieldPanel.add(new JLabel("Store密码:"));
        JTextField storePasswordField = new JTextField(20);
        storePasswordFieldPanel.add(storePasswordField);
        contentPane.add(storePasswordFieldPanel);

        // 确定按钮
        JButton okButton = new JButton("确定");
        JPanel btnP = new JPanel();
        btnP.setLayout(new FlowLayout());
        btnP.setBorder(new EmptyBorder(10, 10, 20, 10));
        btnP.add(okButton);
        okButton.addActionListener(e -> {
            dispose();
            DataBaseHelp.add(
                    apkNameField.getText(),
                    apkPathField.getText(),
                    proguardRulesArea.getText(),
                    outputPathField.getText(),
                    signPathField.getText(),
                    aliasField.getText(),
                    signPasswordField.getText(),
                    storePasswordField.getText()
            );
            DataBaseHelp.queryAll();
        });
        contentPane.add(btnP);

        pack();
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(parent);
    }

    public void showDialog() {
        setVisible(true);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        JButton openDialog = new JButton("Open Dialog");
        openDialog.addActionListener(e -> {

                }
                );
        frame.setLayout(new FlowLayout());
        frame.add(openDialog);
        frame.setSize(300, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}