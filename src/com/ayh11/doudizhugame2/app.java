package com.ayh11.doudizhugame2;

import javax.swing.*;

public class app {
    public static void main(String[] args) {
        // 启动登录界面
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new LoginFrame();
            }
        });
    }
}
