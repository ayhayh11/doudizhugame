package com.ayh11.doudizhugame2;

import javax.swing.*;

public class app {
    public static void main(String[] args) {
        // 直接启动游戏,跳过登录界面
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Pokergame();
            }
        });
    }
}
