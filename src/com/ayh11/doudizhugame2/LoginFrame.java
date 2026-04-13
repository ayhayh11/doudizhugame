package com.ayh11.doudizhugame2;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * 斗地主游戏登录界面
 * 提供用户登录功能,登录成功后进入游戏
 */
public class LoginFrame extends JFrame {
    private JTextField usernameField;      // 用户名输入框
    private JPasswordField passwordField;  // 密码输入框
    private JButton loginButton;           // 登录按钮
    private JButton registerButton;        // 注册按钮
    private GradientPanel backgroundPanel; // 渐变背景面板
    
    // 存储注册用户信息(使用文件持久化)
    private static Map<String, String> userDatabase = new HashMap<>();
    private static final String USER_DATA_FILE = "users.dat";
    
    // 静态代码块 - 加载已保存的用户数据
    static {
        loadUserData();
    }
    
    /**
     * 构造方法 - 初始化登录界面
     */
    public LoginFrame() {
        initUI();
        setVisible(true);  // 显示窗口
    }
    
    /**
     * 初始化界面组件和布局
     */
    private void initUI() {
        // 设置窗口基本属性
        setTitle("斗地主游戏");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);  // 窗口居中显示
        setResizable(false);          // 禁止调整窗口大小
        
        // 创建渐变背景面板
        backgroundPanel = new GradientPanel();
        backgroundPanel.setLayout(new BorderLayout());
        
        // 创建内容面板(透明)
        JPanel contentPanel = new JPanel();
        contentPanel.setOpaque(false);
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(40, 60, 40, 60));
        
        // 创建标题标签
        JLabel titleLabel = createStyledLabel("🎮 斗地主游戏", new Font("微软雅黑", Font.BOLD, 32), 
            new Color(255, 215, 0));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // 创建副标题标签
        JLabel subtitleLabel = createStyledLabel("欢乐扑克 精彩无限", new Font("微软雅黑", Font.PLAIN, 16), 
            new Color(200, 200, 200));
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // 创建输入框容器
        JPanel inputContainer = new JPanel();
        inputContainer.setOpaque(false);
        inputContainer.setLayout(new BoxLayout(inputContainer, BoxLayout.Y_AXIS));
        inputContainer.setBorder(BorderFactory.createEmptyBorder(30, 0, 20, 0));
        
        // 创建用户名输入框
        usernameField = createStyledTextField("👤 用户名");
        usernameField.setAlignmentX(Component.CENTER_ALIGNMENT);
        usernameField.setMaximumSize(new Dimension(380, 45));
        
        // 创建密码输入框
        passwordField = createStyledPasswordField("🔒 密码");
        passwordField.setAlignmentX(Component.CENTER_ALIGNMENT);
        passwordField.setMaximumSize(new Dimension(380, 45));
        
        // 添加输入框到容器
        inputContainer.add(usernameField);
        inputContainer.add(Box.createRigidArea(new Dimension(0, 15)));
        inputContainer.add(passwordField);
        
        // 创建按钮容器
        JPanel buttonContainer = new JPanel();
        buttonContainer.setOpaque(false);
        buttonContainer.setLayout(new BoxLayout(buttonContainer, BoxLayout.Y_AXIS));
        buttonContainer.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // 创建登录按钮
        loginButton = createGradientButton("开始游戏", new Color(102, 126, 234), new Color(118, 75, 162));
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginButton.setMaximumSize(new Dimension(380, 50));
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleLogin();
            }
        });
        
        // 创建注册按钮
        registerButton = createGradientButton("注册账号", new Color(72, 187, 120), new Color(56, 161, 105));
        registerButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        registerButton.setMaximumSize(new Dimension(380, 50));
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showRegisterDialog();
            }
        });
        
        buttonContainer.add(loginButton);
        buttonContainer.add(Box.createRigidArea(new Dimension(0, 10)));
        buttonContainer.add(registerButton);
        
        // 组装界面
        contentPanel.add(titleLabel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        contentPanel.add(subtitleLabel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        contentPanel.add(inputContainer);
        contentPanel.add(buttonContainer);
        
        backgroundPanel.add(contentPanel, BorderLayout.CENTER);
        add(backgroundPanel);
    }
    
    /**
     * 创建样式化标签
     * @param text 标签文本
     * @param font 字体
     * @param color 颜色
     * @return 样式化标签
     */
    private JLabel createStyledLabel(String text, Font font, Color color) {
        JLabel label = new JLabel(text);
        label.setFont(font);
        label.setForeground(color);
        return label;
    }
    
    /**
     * 创建样式化文本输入框(带圆角和半透明背景)
     * @param placeholder 占位符文本
     * @return 样式化文本框
     */
    private JTextField createStyledTextField(String placeholder) {
        JTextField field = new JTextField() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // 绘制半透明背景
                g2d.setColor(new Color(255, 255, 255, 30));
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);
                
                // 绘制边框
                g2d.setColor(new Color(255, 255, 255, 100));
                g2d.setStroke(new BasicStroke(2));
                g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 25, 25);
                
                super.paintComponent(g);
                g2d.dispose();
            }
        };
        
        field.setFont(new Font("微软雅黑", Font.PLAIN, 16));
        field.setForeground(Color.WHITE);
        field.setBackground(new Color(0, 0, 0, 0));
        field.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        field.setOpaque(false);
        
        return field;
    }
    
    /**
     * 创建样式化密码输入框
     * @param placeholder 占位符文本
     * @return 样式化密码框
     */
    private JPasswordField createStyledPasswordField(String placeholder) {
        JPasswordField field = new JPasswordField() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // 绘制半透明背景
                g2d.setColor(new Color(255, 255, 255, 30));
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);
                
                // 绘制边框
                g2d.setColor(new Color(255, 255, 255, 100));
                g2d.setStroke(new BasicStroke(2));
                g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 25, 25);
                
                super.paintComponent(g);
                g2d.dispose();
            }
        };
        
        field.setFont(new Font("微软雅黑", Font.PLAIN, 16));
        field.setForeground(Color.WHITE);
        field.setBackground(new Color(0, 0, 0, 0));
        field.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        field.setOpaque(false);
        
        return field;
    }
    
    /**
     * 创建渐变色按钮(带悬停效果)
     * @param text 按钮文本
     * @param color1 起始颜色
     * @param color2 结束颜色
     * @return 渐变按钮
     */
    private JButton createGradientButton(String text, Color color1, Color color2) {
        JButton button = new JButton(text) {
            private boolean hovering = false;
            
            {
                // 添加鼠标悬停监听
                addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseEntered(MouseEvent e) {
                        hovering = true;
                        repaint();
                    }
                    
                    @Override
                    public void mouseExited(MouseEvent e) {
                        hovering = false;
                        repaint();
                    }
                });
            }
            
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // 根据悬停状态选择颜色
                GradientPaint gradient;
                if (hovering) {
                    gradient = new GradientPaint(0, 0, color1.brighter(), 
                        getWidth(), getHeight(), color2.brighter());
                } else {
                    gradient = new GradientPaint(0, 0, color1, 
                        getWidth(), getHeight(), color2);
                }
                
                g2d.setPaint(gradient);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);
                
                // 绘制边框
                g2d.setColor(new Color(255, 255, 255, 100));
                g2d.setStroke(new BasicStroke(2));
                g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 25, 25);
                
                super.paintComponent(g);
                g2d.dispose();
            }
        };
        
        button.setFont(new Font("微软雅黑", Font.BOLD, 18));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        return button;
    }
    
    /**
     * 处理登录逻辑
     */
    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();
        
        // 验证输入
        if (username.isEmpty() || password.isEmpty()) {
            showCustomDialog("请输入用户名和密码!", "警告");
            return;
        }
        
        // 验证用户是否存在且密码正确
        if (!userDatabase.containsKey(username)) {
            showCustomDialog("用户不存在,请先注册!", "错误");
            return;
        }
        
        if (!userDatabase.get(username).equals(password)) {
            showCustomDialog("密码错误!", "错误");
            return;
        }
        
        // 显示欢迎消息
        showCustomDialog("欢迎, " + username + "!", "登录成功");
        
        // 延迟1秒后关闭登录窗口并启动游戏
        Timer timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new Pokergame();
            }
        });
        timer.setRepeats(false);
        timer.start();
    }
    
    /**
     * 显示注册对话框
     */
    private void showRegisterDialog() {
        JDialog registerDialog = new JDialog(this, "注册账号", true);
        registerDialog.setSize(450, 500);
        registerDialog.setLocationRelativeTo(this);
        registerDialog.setResizable(false);
        registerDialog.setUndecorated(true);  // 去除默认装饰
        
        // 创建圆角背景面板
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // 绘制渐变背景
                GradientPaint gradient = new GradientPaint(
                    0, 0, new Color(20, 30, 48),
                    getWidth(), getHeight(), new Color(36, 59, 85)
                );
                g2d.setPaint(gradient);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                
                g2d.dispose();
            }
        };
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        
        // 顶部关闭按钮
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        topPanel.setOpaque(false);
        JButton closeButton = new JButton("✕");
        closeButton.setFont(new Font("微软雅黑", Font.BOLD, 16));
        closeButton.setForeground(Color.WHITE);
        closeButton.setBackground(new Color(255, 100, 100));
        closeButton.setFocusPainted(false);
        closeButton.setBorderPainted(false);
        closeButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        closeButton.setPreferredSize(new Dimension(30, 30));
        closeButton.addActionListener(e -> registerDialog.dispose());
        topPanel.add(closeButton);
        
        // 内容面板
        JPanel contentPanel = new JPanel();
        contentPanel.setOpaque(false);
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        
        // 标题
        JLabel titleLabel = new JLabel("📝 用户注册", SwingConstants.CENTER);
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 28));
        titleLabel.setForeground(new Color(72, 187, 120));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // 用户名输入框
        JPanel userPanel = createInputPanel("👤 用户名");
        JTextField regUserField = new JTextField();
        regUserField.setFont(new Font("微软雅黑", Font.PLAIN, 15));
        regUserField.setForeground(Color.WHITE);
        regUserField.setBackground(new Color(0, 0, 0, 0));
        regUserField.setOpaque(false);
        regUserField.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        regUserField.setMaximumSize(new Dimension(370, 45));
        userPanel.add(regUserField);
        
        // 密码输入框
        JPanel passPanel = createInputPanel("🔒 密码");
        JPasswordField regPassField = new JPasswordField();
        regPassField.setFont(new Font("微软雅黑", Font.PLAIN, 15));
        regPassField.setForeground(Color.WHITE);
        regPassField.setBackground(new Color(0, 0, 0, 0));
        regPassField.setOpaque(false);
        regPassField.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        regPassField.setMaximumSize(new Dimension(370, 45));
        passPanel.add(regPassField);
        
        // 确认密码输入框
        JPanel confirmPanel = createInputPanel("🔒 确认密码");
        JPasswordField confirmPassField = new JPasswordField();
        confirmPassField.setFont(new Font("微软雅黑", Font.PLAIN, 15));
        confirmPassField.setForeground(Color.WHITE);
        confirmPassField.setBackground(new Color(0, 0, 0, 0));
        confirmPassField.setOpaque(false);
        confirmPassField.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        confirmPassField.setMaximumSize(new Dimension(370, 45));
        confirmPanel.add(confirmPassField);
        
        // 按钮面板
        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        
        JButton cancelBtn = createStyledButton("取消", new Color(150, 150, 150), new Color(120, 120, 120));
        cancelBtn.setMaximumSize(new Dimension(170, 50));
        cancelBtn.addActionListener(e -> registerDialog.dispose());
        
        JButton registerBtn = createStyledButton("立即注册", new Color(72, 187, 120), new Color(56, 161, 105));
        registerBtn.setMaximumSize(new Dimension(170, 50));
        registerBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = regUserField.getText().trim();
                String password = new String(regPassField.getPassword()).trim();
                String confirmPassword = new String(confirmPassField.getPassword()).trim();
                
                // 验证输入
                if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                    showCustomDialog("所有字段都不能为空!", "警告");
                    return;
                }
                
                if (username.length() < 3) {
                    showCustomDialog("用户名至少3个字符!", "警告");
                    return;
                }
                
                if (password.length() < 6) {
                    showCustomDialog("密码至少6个字符!", "警告");
                    return;
                }
                
                if (!password.equals(confirmPassword)) {
                    showCustomDialog("两次输入的密码不一致!", "错误");
                    return;
                }
                
                if (userDatabase.containsKey(username)) {
                    showCustomDialog("用户名已存在!", "错误");
                    return;
                }
                
                // 注册成功
                userDatabase.put(username, password);
                saveUserData();  // 保存到文件
                showCustomDialog("注册成功!现在可以登录了。", "成功");
                
                Timer timer = new Timer(1500, new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        registerDialog.dispose();
                    }
                });
                timer.setRepeats(false);
                timer.start();
            }
        });
        
        buttonPanel.add(cancelBtn);
        buttonPanel.add(Box.createRigidArea(new Dimension(20, 0)));
        buttonPanel.add(registerBtn);
        
        // 组装内容
        contentPanel.add(titleLabel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        contentPanel.add(userPanel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        contentPanel.add(passPanel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        contentPanel.add(confirmPanel);
        contentPanel.add(Box.createVerticalGlue());
        contentPanel.add(buttonPanel);
        
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        
        // 添加鼠标拖动功能
        final Point[] dragOffset = new Point[1];
        mainPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                dragOffset[0] = new Point(e.getX(), e.getY());
            }
        });
        mainPanel.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                registerDialog.setLocation(
                    registerDialog.getX() + e.getX() - dragOffset[0].x,
                    registerDialog.getY() + e.getY() - dragOffset[0].y
                );
            }
        });
        
        registerDialog.add(mainPanel);
        registerDialog.setVisible(true);
    }
    
    /**
     * 创建输入框面板
     */
    private JPanel createInputPanel(String labelText) {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                g2d.setColor(new Color(255, 255, 255, 30));
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);
                
                g2d.setColor(new Color(255, 255, 255, 100));
                g2d.setStroke(new BasicStroke(2));
                g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 25, 25);
                
                g2d.dispose();
            }
        };
        panel.setOpaque(false);
        panel.setLayout(new BorderLayout());
        panel.setMaximumSize(new Dimension(370, 45));
        panel.setPreferredSize(new Dimension(370, 45));
        
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        label.setForeground(new Color(200, 200, 200));
        label.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 0));
        panel.add(label, BorderLayout.WEST);
        
        return panel;
    }
    
    /**
     * 创建样式化按钮
     */
    private JButton createStyledButton(String text, Color color1, Color color2) {
        JButton button = new JButton(text) {
            private boolean hovering = false;
            
            {
                addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseEntered(MouseEvent e) {
                        hovering = true;
                        repaint();
                    }
                    
                    @Override
                    public void mouseExited(MouseEvent e) {
                        hovering = false;
                        repaint();
                    }
                });
            }
            
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                GradientPaint gradient;
                if (hovering) {
                    gradient = new GradientPaint(0, 0, color1.brighter(), 
                        getWidth(), getHeight(), color2.brighter());
                } else {
                    gradient = new GradientPaint(0, 0, color1, 
                        getWidth(), getHeight(), color2);
                }
                
                g2d.setPaint(gradient);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);
                
                g2d.setColor(new Color(255, 255, 255, 100));
                g2d.setStroke(new BasicStroke(2));
                g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 25, 25);
                
                super.paintComponent(g);
                g2d.dispose();
            }
        };
        
        button.setFont(new Font("微软雅黑", Font.BOLD, 16));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        return button;
    }
    
    /**
     * 保存用户数据到文件
     */
    private static void saveUserData() {
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(USER_DATA_FILE))) {
            oos.writeObject(userDatabase);
        } catch (IOException e) {
            System.err.println("保存用户数据失败: " + e.getMessage());
        }
    }
    
    /**
     * 从文件加载用户数据
     */
    @SuppressWarnings("unchecked")
    private static void loadUserData() {
        File file = new File(USER_DATA_FILE);
        if (!file.exists()) {
            return;  // 文件不存在,使用空数据库
        }
        
        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(USER_DATA_FILE))) {
            userDatabase = (Map<String, String>) ois.readObject();
            System.out.println("已加载 " + userDatabase.size() + " 个用户");
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("加载用户数据失败: " + e.getMessage());
            userDatabase = new HashMap<>();  // 加载失败,使用空数据库
        }
    }
    
    /**
     * 显示自定义对话框(自动关闭)
     * @param message 消息内容
     * @param title 对话框标题
     */
    private void showCustomDialog(String message, String title) {
        JOptionPane optionPane = new JOptionPane(message, 
            title.equals("警告") ? JOptionPane.WARNING_MESSAGE : JOptionPane.INFORMATION_MESSAGE);
        JDialog dialog = optionPane.createDialog(this, title);
        dialog.setModal(false);
        dialog.setVisible(true);
        
        // 2秒后自动关闭
        Timer timer = new Timer(2000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
            }
        });
        timer.setRepeats(false);
        timer.start();
    }
    
    /**
     * 渐变背景面板(带装饰圆形)
     */
    class GradientPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            // 绘制渐变背景
            GradientPaint gradient = new GradientPaint(
                0, 0, new Color(20, 30, 48),
                getWidth(), getHeight(), new Color(36, 59, 85)
            );
            
            g2d.setPaint(gradient);
            g2d.fillRect(0, 0, getWidth(), getHeight());
            
            // 绘制装饰性半透明圆形
            for (int i = 0; i < 20; i++) {
                int x = (int) (Math.random() * getWidth());
                int y = (int) (Math.random() * getHeight());
                int size = (int) (Math.random() * 100 + 50);
                
                g2d.setColor(new Color(255, 255, 255, 5));
                g2d.fillOval(x, y, size, size);
            }
            
            g2d.dispose();
        }
    }
}
