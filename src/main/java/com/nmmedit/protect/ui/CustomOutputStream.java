package com.nmmedit.protect.ui;

import javax.swing.*;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CodingErrorAction;
import java.nio.charset.StandardCharsets;

public class CustomOutputStream extends OutputStream {
    private final JTextArea textArea;
    private final CharsetDecoder decoder;
    private final ByteBuffer buffer;

    public CustomOutputStream(JTextArea textArea) {
        this.textArea = textArea;
        this.decoder = StandardCharsets.UTF_8.newDecoder();
        this.decoder.onMalformedInput(CodingErrorAction.REPLACE);
        this.decoder.onUnmappableCharacter(CodingErrorAction.REPLACE);
        this.buffer = ByteBuffer.allocate(1024);
    }

    @Override
    public void write(int b) throws IOException {
        buffer.put((byte) b);
        if (buffer.position() >= 1) {  // 简单判断，尝试解码，适用于多数UTF-8字符
            buffer.flip();
            try {
                // 尝试解码
                StringBuilder output = new StringBuilder();
                decoder.decode(buffer, CharBuffer.wrap(output), false);  // 解码但不完成，以处理可能的分割字节
                if (output.length() > 0) {
                    String text = output.toString();
                    SwingUtilities.invokeLater(() -> textArea.append(text));
                }
            } finally {
                buffer.compact();  // 重置buffer，准备接收新的输入
            }
        }
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        SwingUtilities.invokeLater(() -> textArea.append(new String(b, off, len, StandardCharsets.UTF_8)));
    }

    @Override
    public void flush() throws IOException {
        if (buffer.position() > 0) {
            buffer.flip();
            StringBuilder output = new StringBuilder();
            decoder.decode(buffer, CharBuffer.wrap(output), true);  // 最终解码
            decoder.flush(CharBuffer.wrap(output));
            SwingUtilities.invokeLater(() -> textArea.append(output.toString()));
            buffer.clear();
        }
    }
}
