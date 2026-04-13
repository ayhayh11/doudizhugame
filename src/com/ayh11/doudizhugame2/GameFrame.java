package com.ayh11.doudizhugame2;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

public class GameFrame extends JFrame {
    private List<String> playerHand, player2Hand, player3Hand, dipaiCards;
    private List<String> playedCards, lastPlayedCards;
    private List<JButton> cardButtons;
    private JPanel cardsPanel, playedCardsPanel, player2CardsPanel, player3CardsPanel;
    private JPanel player2PlayedPanel, player3PlayedPanel;  // 玩家2和3的出牌区
    private JLabel messageLabel, hintLabel;  // 提示标签
    private int currentPlayer = 1, landlord = 0, callScore = 0, consecutivePass = 0;
    private boolean isCallingLandlord = true;
    
    public GameFrame(List<String> hand, List<String> hand2, List<String> hand3, List<String> dipai) {
        this.playerHand = new ArrayList<>(hand);
        this.player2Hand = new ArrayList<>(hand2);
        this.player3Hand = new ArrayList<>(hand3);
        this.dipaiCards = new ArrayList<>(dipai);
        this.playedCards = new ArrayList<>();
        this.cardButtons = new ArrayList<>();
        initUI();
    }
    
    private void initUI() {
        setTitle("斗地主游戏");
        setSize(1400, 900);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        JPanel mainPanel = new GradientPanel();
        mainPanel.setLayout(null);
        
        // 顶部
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        topPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        topPanel.setBounds(0, 0, 1400, 60);
        
        JLabel titleLabel = new JLabel("🎮 斗地主游戏", SwingConstants.LEFT);
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 24));
        titleLabel.setForeground(new Color(255, 215, 0));
        
        messageLabel = new JLabel("请叫地主", SwingConstants.CENTER);
        messageLabel.setFont(new Font("微软雅黑", Font.PLAIN, 18));
        messageLabel.setForeground(Color.WHITE);
        
        topPanel.add(titleLabel, BorderLayout.WEST);
        topPanel.add(messageLabel, BorderLayout.CENTER);
        mainPanel.add(topPanel);
        
        // 玩家2 - 竖着堆叠
        player2CardsPanel = createCardsPanel();
        player2CardsPanel.setBounds(20, 150, 80, 500);
        updateOtherPlayerCardsVertical(player2CardsPanel, player2Hand.size());
        mainPanel.add(player2CardsPanel);
        
        JLabel p2Label = new JLabel("玩家2", SwingConstants.CENTER);
        p2Label.setFont(new Font("微软雅黑", Font.BOLD, 16));
        p2Label.setForeground(Color.WHITE);
        p2Label.setBounds(20, 120, 80, 30);
        mainPanel.add(p2Label);
        
        // 玩家2出牌区
        player2PlayedPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
        player2PlayedPanel.setOpaque(false);
        player2PlayedPanel.setBounds(100, 250, 200, 100);
        mainPanel.add(player2PlayedPanel);
        
        // 玩家3 - 竖着堆叠
        player3CardsPanel = createCardsPanel();
        player3CardsPanel.setBounds(1300, 150, 80, 500);
        updateOtherPlayerCardsVertical(player3CardsPanel, player3Hand.size());
        mainPanel.add(player3CardsPanel);
        
        JLabel p3Label = new JLabel("玩家3", SwingConstants.CENTER);
        p3Label.setFont(new Font("微软雅黑", Font.BOLD, 16));
        p3Label.setForeground(Color.WHITE);
        p3Label.setBounds(1300, 120, 80, 30);
        mainPanel.add(p3Label);
        
        // 玩家3出牌区
        player3PlayedPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
        player3PlayedPanel.setOpaque(false);
        player3PlayedPanel.setBounds(1100, 250, 200, 100);
        mainPanel.add(player3PlayedPanel);
        
        // 底牌
        JPanel dipaiPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
        dipaiPanel.setOpaque(false);
        dipaiPanel.setBounds(550, 80, 300, 80);
        for (String card : dipaiCards) {
            dipaiPanel.add(createCardLabel(card));
        }
        mainPanel.add(dipaiPanel);
        
        JLabel dipaiLabel = new JLabel("底牌", SwingConstants.CENTER);
        dipaiLabel.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        dipaiLabel.setForeground(new Color(200, 200, 200));
        dipaiLabel.setBounds(550, 160, 300, 20);
        mainPanel.add(dipaiLabel);
        
        // 出牌区
        playedCardsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 8));
        playedCardsPanel.setOpaque(false);
        playedCardsPanel.setBounds(350, 200, 700, 150);
        playedCardsPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(255, 255, 255, 100), 2),
            "出牌区", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP,
            new Font("微软雅黑", Font.BOLD, 16), Color.WHITE));
        mainPanel.add(playedCardsPanel);
        
        // 手牌区
        cardsPanel = new JPanel();
        cardsPanel.setOpaque(false);
        cardsPanel.setLayout(null);
        cardsPanel.setBounds(0, 450, 1400, 300);
        mainPanel.add(cardsPanel);
        
        // 提示标签 - 放在手牌上方(必须在createCardButtons之前初始化)
        hintLabel = new JLabel("", SwingConstants.CENTER);
        hintLabel.setFont(new Font("微软雅黑", Font.BOLD, 20));
        hintLabel.setForeground(new Color(255, 215, 0));
        hintLabel.setBounds(0, 410, 1400, 40);
        mainPanel.add(hintLabel);
        
        createCardButtons();
        
        // 按钮区
        JPanel actionPanel = createActionPanel();
        actionPanel.setBounds(350, 780, 700, 70);
        mainPanel.add(actionPanel);
        
        add(mainPanel);
        startCallLandlord();
    }
    
    private JPanel createCardsPanel() {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(null);
        return panel;
    }
    
    private JPanel createActionPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        panel.setOpaque(false);
        
        String[] callTexts = {"叫1分", "叫2分", "叫3分"};
        for (int i = 0; i < 3; i++) {
            JButton btn = createBtn(callTexts[i], new Color(102, 126, 234));
            final int score = i + 1;
            btn.addActionListener(e -> callLandlord(score));
            btn.setName("call" + score);
            panel.add(btn);
        }
        
        JButton noCall = createBtn("不叫", new Color(150, 150, 150));
        noCall.setName("noCall");
        noCall.addActionListener(e -> passCall());
        panel.add(noCall);
        
        JButton play = createBtn("出牌", new Color(72, 187, 120));
        play.setName("play");
        play.addActionListener(e -> playCards());
        play.setVisible(false);
        panel.add(play);
        
        JButton pass = createBtn("不出", new Color(237, 137, 54));
        pass.setName("pass");
        pass.addActionListener(e -> passTurn());
        pass.setVisible(false);
        panel.add(pass);
        
        JButton hint = createBtn("提示", new Color(102, 126, 234));
        hint.setName("hint");
        hint.addActionListener(e -> showHint());
        hint.setVisible(false);
        panel.add(hint);
        
        JButton surrender = createBtn("投降", new Color(231, 76, 60));
        surrender.setName("surrender");
        surrender.addActionListener(e -> surrenderGame());
        surrender.setVisible(false);
        panel.add(surrender);
        
        return panel;
    }
    
    private JButton createBtn(String text, Color color) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("微软雅黑", Font.BOLD, 15));
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(110, 45));
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btn.setBackground(color.brighter()); }
            public void mouseExited(MouseEvent e) { btn.setBackground(color); }
        });
        return btn;
    }
    
    private JPanel createCardLabel(String card) {
        String trimmed = card.trim();
        
        // 自定义绘制牌面
        JPanel cardPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // 白色背景
                g2d.setColor(Color.WHITE);
                g2d.fillRoundRect(0, 0, getWidth()-1, getHeight()-1, 8, 8);
                
                // 边框
                g2d.setColor(Color.GRAY);
                g2d.setStroke(new BasicStroke(2));
                g2d.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 8, 8);
                
                // 设置字体和颜色
                Color cardColor = getCardColor(card);
                g2d.setColor(cardColor);
                Font font = new Font("微软雅黑", Font.BOLD, 14);
                g2d.setFont(font);
                
                // 提取花色和数字
                String suit = "";
                String number = "";
                if (trimmed.equals("小王") || trimmed.equals("大王")) {
                    number = trimmed;
                } else if (trimmed.length() >= 2) {
                    suit = trimmed.substring(0, 1);
                    number = trimmed.substring(1);
                }
                
                // 左上角 - 正常显示
                if (!number.isEmpty()) {
                    g2d.drawString(number, 8, 18);
                }
                if (!suit.isEmpty()) {
                    g2d.drawString(suit, 8, 32);
                }
                
                // 右下角 - 倒置显示
                g2d.rotate(Math.PI, getWidth()/2, getHeight()/2);
                if (!number.isEmpty()) {
                    g2d.drawString(number, 8, 18);
                }
                if (!suit.isEmpty()) {
                    g2d.drawString(suit, 8, 32);
                }
                g2d.rotate(-Math.PI, getWidth()/2, getHeight()/2);
                
                g2d.dispose();
            }
        };
        
        cardPanel.setPreferredSize(new Dimension(60, 90));
        cardPanel.setOpaque(false);
        return cardPanel;
    }
    
    private void createCardButtons() {
        cardsPanel.removeAll();
        cardButtons.clear();
        
        int cardWidth = 90, cardHeight = 130, overlap = 60;
        int totalWidth = cardWidth + (playerHand.size() - 1) * overlap;
        int startX = (1400 - totalWidth) / 2;
        
        // 从左往右添加,保持索引一致
        for (int i = 0; i < playerHand.size(); i++) {
            final String card = playerHand.get(i);
            final int index = i;
            JButton btn = new JButton() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    Graphics2D g2d = (Graphics2D) g.create();
                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    
                    // 背景
                    if (getModel().isPressed() || getY() < 100) {
                        g2d.setColor(new Color(255, 255, 220));
                    } else {
                        g2d.setColor(Color.WHITE);
                    }
                    g2d.fillRoundRect(0, 0, getWidth()-1, getHeight()-1, 10, 10);
                    
                    // 边框
                    Color borderColor = getCardColor(card);
                    g2d.setColor(borderColor);
                    g2d.setStroke(new BasicStroke(2));
                    g2d.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 10, 10);
                    
                    // 设置字体
                    Font font = new Font("微软雅黑", Font.BOLD, 20);
                    g2d.setFont(font);
                    g2d.setColor(borderColor);
                    
                    String trimmed = card.trim();
                    String suit = "";
                    String number = "";
                    if (trimmed.equals("小王") || trimmed.equals("大王")) {
                        number = trimmed;
                    } else if (trimmed.length() >= 2) {
                        suit = trimmed.substring(0, 1);
                        number = trimmed.substring(1);
                    }
                    
                    // 左上角
                    if (!number.isEmpty()) {
                        g2d.drawString(number, 10, 25);
                    }
                    if (!suit.isEmpty()) {
                        g2d.drawString(suit, 10, 45);
                    }
                    
                    // 右下角 - 倒置
                    g2d.rotate(Math.PI, getWidth()/2, getHeight()/2);
                    if (!number.isEmpty()) {
                        g2d.drawString(number, 10, 25);
                    }
                    if (!suit.isEmpty()) {
                        g2d.drawString(suit, 10, 45);
                    }
                    g2d.rotate(-Math.PI, getWidth()/2, getHeight()/2);
                    
                    g2d.dispose();
                }
            };
            
            btn.setBounds(startX + i * overlap, 100, cardWidth, cardHeight);
            btn.setContentAreaFilled(false);
            btn.setFocusPainted(false);
            btn.setBorderPainted(false);
            btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btn.setToolTipText(card.trim());
            
            btn.addMouseListener(new MouseAdapter() {
                boolean selected = false;
                public void mouseClicked(MouseEvent e) {
                    selected = !selected;
                    if (selected) {
                        btn.setLocation(btn.getX(), btn.getY() - 25);
                        showMessage("已选择: " + card.trim(), Color.YELLOW);
                    } else {
                        btn.setLocation(btn.getX(), btn.getY() + 25);
                        showMessage("请选择要出的牌", Color.WHITE);
                    }
                    repaint();
                }
            });
            
            cardButtons.add(btn);
        }
        
        // 倒序添加到panel,实现右边盖住左边
        for (int i = cardButtons.size() - 1; i >= 0; i--) {
            cardsPanel.add(cardButtons.get(i));
        }
        cardsPanel.revalidate();
        cardsPanel.repaint();
    }
    
    private Color getCardColor(String card) {
        if (card.contains("♥") || card.contains("♦")) return new Color(220, 20, 60);
        if (card.contains("小王")) return new Color(100, 100, 100);
        if (card.contains("大王")) return new Color(255, 140, 0);
        return Color.BLACK;
    }
    
    private int getCardValue(String card) {
        String trimmed = card.trim();
        if (trimmed.equals("小王")) return 16;
        if (trimmed.equals("大王")) return 17;
        
        String num = trimmed.substring(1);
        switch (num) {
            case "3": return 3;
            case "4": return 4;
            case "5": return 5;
            case "6": return 6;
            case "7": return 7;
            case "8": return 8;
            case "9": return 9;
            case "10": return 10;
            case "J": return 11;
            case "Q": return 12;
            case "K": return 13;
            case "A": return 14;
            case "2": return 15;
            default: return 0;
        }
    }
    
    private void startCallLandlord() {
        showMessage("轮到你了,请叫地主", Color.YELLOW);
    }
    
    private void callLandlord(int score) {
        if (score > callScore) {
            callScore = score;
            landlord = 1;
            showMessage("你叫了 " + score + " 分", new Color(72, 187, 120));
            javax.swing.Timer t = new javax.swing.Timer(1500, e -> aiCall());
            t.setRepeats(false);
            t.start();
        } else {
            showMessage("必须叫比 " + callScore + " 更高的分!", Color.RED);
        }
    }
    
    private void passCall() {
        showMessage("你不叫", Color.GRAY);
        javax.swing.Timer t = new javax.swing.Timer(1000, e -> aiCall());
        t.setRepeats(false);
        t.start();
    }
    
    private void aiCall() {
        if (landlord == 0 || Math.random() > 0.3) {
            int s = (int)(Math.random() * 3) + 1;
            if (s > callScore) {
                callScore = s;
                landlord = 2;
                showMessage("玩家2 叫了 " + s + " 分", Color.YELLOW);
                // 玩家3也有机会抢
                javax.swing.Timer t = new javax.swing.Timer(1500, e -> aiCallPlayer3());
                t.setRepeats(false);
                t.start();
                return;
            }
        }
        showMessage("玩家2 不叫", Color.GRAY);
        javax.swing.Timer t = new javax.swing.Timer(1000, e -> aiCallPlayer3());
        t.setRepeats(false);
        t.start();
    }
    
    private void aiCallPlayer3() {
        if (landlord != 0 && Math.random() > 0.4) {
            showMessage("玩家3 不叫", Color.GRAY);
            javax.swing.Timer t = new javax.swing.Timer(1000, e -> startGame());
            t.setRepeats(false);
            t.start();
            return;
        }
        
        int s = (int)(Math.random() * 3) + 1;
        if (s > callScore) {
            callScore = s;
            landlord = 3;
            showMessage("玩家3 叫了 " + s + " 分", Color.YELLOW);
            javax.swing.Timer t = new javax.swing.Timer(1500, e -> startGame());
            t.setRepeats(false);
            t.start();
        } else {
            showMessage("玩家3 不叫", Color.GRAY);
            javax.swing.Timer t = new javax.swing.Timer(1000, e -> startGame());
            t.setRepeats(false);
            t.start();
        }
    }
    
    private void startGame() {
        isCallingLandlord = false;
        
        System.out.println("叫地主前 - playerHand.size() = " + playerHand.size());
        System.out.println("底牌数量 - dipaiCards.size() = " + dipaiCards.size());
        
        if (landlord == 1) {
            // 先添加底牌
            playerHand.addAll(dipaiCards);
            System.out.println("添加底牌后 - playerHand.size() = " + playerHand.size());
            
            // 使用Collections.sort排序,不会去重
            Collections.sort(playerHand, (a, b) -> getCardValue(a) - getCardValue(b));
            System.out.println("排序后 - playerHand.size() = " + playerHand.size());
            
            showMessage("你是地主!获得底牌", new Color(255, 215, 0));
        } else if (landlord == 2) {
            player2Hand.addAll(dipaiCards);
            Collections.sort(player2Hand, (a, b) -> getCardValue(a) - getCardValue(b));
            showMessage("玩家2 是地主!", Color.YELLOW);
        } else {
            player3Hand.addAll(dipaiCards);
            Collections.sort(player3Hand, (a, b) -> getCardValue(a) - getCardValue(b));
            showMessage("玩家3 是地主!", Color.YELLOW);
        }
        
        createCardButtons();
        updateOtherPlayerCardsVertical(player2CardsPanel, player2Hand.size());
        updateOtherPlayerCardsVertical(player3CardsPanel, player3Hand.size());
        
        // 切换按钮
        for (Component c : ((JPanel)((JPanel)getContentPane().getComponent(0)).getComponent(
            ((JPanel)getContentPane().getComponent(0)).getComponentCount() - 1)).getComponents()) {
            if (c instanceof JButton) {
                JButton b = (JButton) c;
                String n = b.getName();
                b.setVisible(n != null && (n.equals("play") || n.equals("pass") || n.equals("hint") || n.equals("surrender")));
            }
        }
        
        currentPlayer = landlord;
        if (currentPlayer != 1) {
            javax.swing.Timer t = new javax.swing.Timer(2000, e -> aiPlay());
            t.setRepeats(false);
            t.start();
        } else {
            showMessage("你是地主,请先出牌", new Color(72, 187, 120));
        }
    }
    
    private void playCards() {
        List<String> selected = new ArrayList<>();
        List<Integer> idxList = new ArrayList<>();
        
        System.out.println("=== 开始出牌 ===");
        System.out.println("cardButtons.size(): " + cardButtons.size());
        System.out.println("playerHand.size(): " + playerHand.size());
        
        // 遍历所有按钮,检查哪些被选中
        for (int btnIdx = 0; btnIdx < cardButtons.size(); btnIdx++) {
            JButton btn = cardButtons.get(btnIdx);
            if (btn.getY() < 100) {
                // 被选中的按钮,需要找到它对应playerHand中的哪个索引
                int btnX = btn.getX();
                
                // 计算这个按钮应该对应playerHand中的哪个索引
                int cardWidth = 90, overlap = 60;
                int totalWidth = cardWidth + (playerHand.size() - 1) * overlap;
                int startX = (1400 - totalWidth) / 2;
                int handIndex = (btnX - startX) / overlap;
                
                System.out.println("按钮索引: " + btnIdx + ", X坐标: " + btnX + ", 计算出的hand索引: " + handIndex);
                
                if (handIndex >= 0 && handIndex < playerHand.size()) {
                    String card = playerHand.get(handIndex);
                    selected.add(card);
                    idxList.add(handIndex);
                    System.out.println("选中: " + card);
                }
            }
        }
        
        System.out.println("共选中 " + selected.size() + " 张牌");
        
        if (selected.isEmpty()) {
            showMessage("请先选择要出的牌!", Color.ORANGE);
            return;
        }
        
        CardTypeChecker.CardType type = CardTypeChecker.checkCardType(selected);
        System.out.println("选中的牌: " + selected);
        System.out.println("牌型: " + type);
        
        if (type == CardTypeChecker.CardType.INVALID) {
            showMessage("无效的牌型!", Color.RED);
            return;
        }
        
        // 如果不是首轮,需要比较牌的大小
        if (lastPlayedCards != null && !lastPlayedCards.isEmpty()) {
            int compareResult = CardTypeChecker.compareCards(selected, lastPlayedCards);
            System.out.println("比较结果: " + compareResult);
            
            if (compareResult == -999) {
                showMessage("牌型不匹配!必须出相同类型的牌", Color.RED);
                return;
            } else if (compareResult <= 0) {
                showMessage("你的牌不够大!", Color.RED);
                return;
            }
        }
        
        System.out.println("准备出的牌: " + selected);
        
        // 清除中间出牌区
        playedCardsPanel.removeAll();
        
        // 显示新出的牌
        for (String card : selected) {
            playedCardsPanel.add(createCardLabel(card));
        }
        playedCardsPanel.revalidate();
        playedCardsPanel.repaint();
        
        lastPlayedCards = new ArrayList<>(selected);
        consecutivePass = 0;
        
        System.out.println("删除前 playerHand: " + playerHand);
        System.out.println("要删除的索引: " + idxList);
        
        for (int i = idxList.size() - 1; i >= 0; i--) {
            int idx = idxList.get(i);
            String removed = playerHand.remove(idx);
            System.out.println("删除索引 " + idx + ": " + removed);
            cardButtons.remove(idx);
        }
        
        System.out.println("删除后 playerHand: " + playerHand);
        
        createCardButtons();
        hintLabel.setText("");  // 清空提示
        showMessage("成功出牌!", new Color(72, 187, 120));
        
        if (playerHand.isEmpty()) {
            JOptionPane.showMessageDialog(this, "恭喜你赢了!", "胜利", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        nextPlayer();
    }
    
    private void updatePlayedCards() {
        playedCardsPanel.removeAll();
        for (String card : playedCards) {
            playedCardsPanel.add(createCardLabel(card));
        }
        playedCardsPanel.revalidate();
        playedCardsPanel.repaint();
    }
    
    private void passTurn() {
        if (lastPlayedCards == null || lastPlayedCards.isEmpty()) {
            showMessage("首轮必须出牌!", Color.RED);
            return;
        }
        
        for (JButton btn : cardButtons) {
            if (btn.getY() < 100) btn.setLocation(btn.getX(), btn.getY() + 25);
        }
        
        consecutivePass++;
        showMessage("你选择了不出", Color.GRAY);
        
        if (consecutivePass >= 2) {
            lastPlayedCards = null;
            playedCardsPanel.removeAll();
            player2PlayedPanel.removeAll();
            player3PlayedPanel.removeAll();
            playedCardsPanel.revalidate();
            playedCardsPanel.repaint();
            player2PlayedPanel.revalidate();
            player2PlayedPanel.repaint();
            player3PlayedPanel.revalidate();
            player3PlayedPanel.repaint();
            showMessage("没人要,你可以任意出牌", new Color(72, 187, 120));
        }
        
        nextPlayer();
    }
    
    private void nextPlayer() {
        // 逆时针顺序: 1→3→2→1
        if (currentPlayer == 1) {
            currentPlayer = 3;
        } else if (currentPlayer == 3) {
            currentPlayer = 2;
        } else {
            currentPlayer = 1;
        }
        
        System.out.println("\n>>> 轮到玩家 " + currentPlayer + " (landlord=" + landlord + ")");
        System.out.println("    lastPlayedCards: " + lastPlayedCards);
        System.out.println("    consecutivePass: " + consecutivePass);
        
        if (currentPlayer != 1) {
            javax.swing.Timer t = new javax.swing.Timer(2000, e -> aiPlay());
            t.setRepeats(false);
            t.start();
        } else {
            showMessage("轮到你了!", new Color(72, 187, 120));
        }
    }
    
    private void showHint() {
        if (playerHand.isEmpty()) {
            hintLabel.setText("已经没有手牌了!");
            hintLabel.setForeground(Color.ORANGE);
            return;
        }
        
        // 先重置所有牌
        for (JButton btn : cardButtons) {
            if (btn.getY() < 100) {
                btn.setLocation(btn.getX(), btn.getY() + 25);
            }
        }
        
        if (lastPlayedCards == null || lastPlayedCards.isEmpty()) {
            // 首轮,建议出最小的单张
            if (!cardButtons.isEmpty()) {
                cardButtons.get(0).setLocation(cardButtons.get(0).getX(), cardButtons.get(0).getY() - 25);
                hintLabel.setText("建议出: " + playerHand.get(0));
                hintLabel.setForeground(new Color(102, 126, 234));
            }
        } else {
            // 需要管上家的牌,查找能管上的牌
            boolean found = false;
            
            // 尝试找单张
            if (lastPlayedCards.size() == 1) {
                int lastValue = CardTypeChecker.getCardValue(lastPlayedCards.get(0));
                for (int i = 0; i < playerHand.size(); i++) {
                    int currentValue = CardTypeChecker.getCardValue(playerHand.get(i));
                    if (currentValue > lastValue) {
                        cardButtons.get(i).setLocation(cardButtons.get(i).getX(), cardButtons.get(i).getY() - 25);
                        hintLabel.setText("可以出: " + playerHand.get(i));
                        hintLabel.setForeground(new Color(102, 126, 234));
                        found = true;
                        break;
                    }
                }
            }
            // 尝试找对子
            else if (lastPlayedCards.size() == 2) {
                CardTypeChecker.CardType lastType = CardTypeChecker.checkCardType(lastPlayedCards);
                if (lastType == CardTypeChecker.CardType.PAIR) {
                    int lastValue = CardTypeChecker.getCardValue(lastPlayedCards.get(0));
                    for (int i = 0; i < playerHand.size() - 1; i++) {
                        if (CardTypeChecker.getCardValue(playerHand.get(i)) == CardTypeChecker.getCardValue(playerHand.get(i+1))) {
                            int currentValue = CardTypeChecker.getCardValue(playerHand.get(i));
                            if (currentValue > lastValue) {
                                cardButtons.get(i).setLocation(cardButtons.get(i).getX(), cardButtons.get(i).getY() - 25);
                                cardButtons.get(i+1).setLocation(cardButtons.get(i+1).getX(), cardButtons.get(i+1).getY() - 25);
                                hintLabel.setText("可以出对" + playerHand.get(i).substring(1));
                                hintLabel.setForeground(new Color(102, 126, 234));
                                found = true;
                                break;
                            }
                        }
                    }
                }
            }
            
            if (!found) {
                // 在手牌上方显示提示标签
                hintLabel.setText("没有能管上的牌,建议不出");
                hintLabel.setForeground(Color.RED);
            }
        }
    }
    
    private void showMessage(String msg, Color color) {
        messageLabel.setText(msg);
        messageLabel.setForeground(color);
    }
    
    private void surrenderGame() {
        int result = JOptionPane.showConfirmDialog(this, 
            "确定要投降吗?", 
            "投降确认", 
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE);
        
        if (result == JOptionPane.YES_OPTION) {
            String winner = landlord == 1 ? "农民" : "地主";
            JOptionPane.showMessageDialog(this, 
                "你投降了!\n" + winner + " 获胜!", 
                "游戏结束", 
                JOptionPane.INFORMATION_MESSAGE);
            System.exit(0);
        }
    }
    
    private void updateOtherPlayerCards(JPanel panel, int count) {
        panel.removeAll();
        int w = 60, h = 90, overlap = 25;
        
        for (int i = 0; i < count; i++) {
            JPanel card = new JPanel() {
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    
                    GradientPaint gp = new GradientPaint(0, 0, new Color(41, 98, 255),
                        getWidth(), getHeight(), new Color(25, 66, 170));
                    g2.setPaint(gp);
                    g2.fillRoundRect(0, 0, getWidth()-1, getHeight()-1, 8, 8);
                    
                    g2.setColor(Color.WHITE);
                    g2.setStroke(new BasicStroke(2));
                    g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 8, 8);
                    
                    g2.setColor(new Color(255, 255, 255, 80));
                    int cx = getWidth()/2, cy = getHeight()/2, s = 15;
                    int[] xp = {cx, cx+s, cx, cx-s};
                    int[] yp = {cy-s, cy, cy+s, cy};
                    g2.fillPolygon(xp, yp, 4);
                    g2.dispose();
                }
            };
            
            card.setBounds(10 + i * overlap, 50 + (i % 3) * 5, w, h);
            card.setOpaque(false);
            panel.add(card);
        }
        panel.revalidate();
        panel.repaint();
    }
    
    /**
     * 竖着堆叠显示其他玩家手牌
     */
    private void updateOtherPlayerCardsVertical(JPanel panel, int count) {
        panel.removeAll();
        int w = 60, h = 90, overlap = 20;  // 减小重叠量到20,更密集
        
        for (int i = 0; i < count; i++) {
            JPanel card = new JPanel() {
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    
                    GradientPaint gp = new GradientPaint(0, 0, new Color(41, 98, 255),
                        getWidth(), getHeight(), new Color(25, 66, 170));
                    g2.setPaint(gp);
                    g2.fillRoundRect(0, 0, getWidth()-1, getHeight()-1, 8, 8);
                    
                    g2.setColor(Color.WHITE);
                    g2.setStroke(new BasicStroke(2));
                    g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 8, 8);
                    
                    g2.setColor(new Color(255, 255, 255, 80));
                    int cx = getWidth()/2, cy = getHeight()/2, s = 15;
                    int[] xp = {cx, cx+s, cx, cx-s};
                    int[] yp = {cy-s, cy, cy+s, cy};
                    g2.fillPolygon(xp, yp, 4);
                    g2.dispose();
                }
            };
            
            // 竖着排列,从上到下
            card.setBounds(10, 10 + i * overlap, w, h);
            card.setOpaque(false);
            panel.add(card);
        }
        panel.revalidate();
        panel.repaint();
    }
    
    private void aiPlay() {
        List<String> hand = currentPlayer == 2 ? player2Hand : player3Hand;
        JPanel cardsPanel = currentPlayer == 2 ? player2CardsPanel : player3CardsPanel;
        JPanel playedPanel = currentPlayer == 2 ? player2PlayedPanel : player3PlayedPanel;
        String name = currentPlayer == 2 ? "玩家2" : "玩家3";
        
        System.out.println("\n=== " + name + " 回合 ===");
        System.out.println("hand.size(): " + hand.size());
        System.out.println("lastPlayedCards: " + lastPlayedCards);
        
        if (hand.isEmpty()) return;
        
        List<String> cardsToPlay = null;
        
        // 如果是首轮或者没人要,任意出牌
        if (lastPlayedCards == null || lastPlayedCards.isEmpty()) {
            // 优先出单张
            cardsToPlay = new ArrayList<>();
            cardsToPlay.add(hand.get(0));
            hand.remove(0);
        } else {
            // 需要管上家的牌
            CardTypeChecker.CardType lastType = CardTypeChecker.checkCardType(lastPlayedCards);
            int lastSize = lastPlayedCards.size();
            
            // 尝试找相同类型的牌
            if (lastSize == 1) {
                // 上家出单张,找更大的单张
                int lastValue = CardTypeChecker.getCardValue(lastPlayedCards.get(0));
                for (int i = 0; i < hand.size(); i++) {
                    int currentValue = CardTypeChecker.getCardValue(hand.get(i));
                    if (currentValue > lastValue) {
                        cardsToPlay = new ArrayList<>();
                        cardsToPlay.add(hand.get(i));
                        hand.remove(i);
                        break;
                    }
                }
            } else if (lastSize == 2) {
                // 上家出对子,找更大的对子
                if (lastType == CardTypeChecker.CardType.PAIR) {
                    int lastValue = CardTypeChecker.getCardValue(lastPlayedCards.get(0));
                    for (int i = 0; i < hand.size() - 1; i++) {
                        if (CardTypeChecker.getCardValue(hand.get(i)) == CardTypeChecker.getCardValue(hand.get(i+1))) {
                            int currentValue = CardTypeChecker.getCardValue(hand.get(i));
                            if (currentValue > lastValue) {
                                cardsToPlay = new ArrayList<>();
                                cardsToPlay.add(hand.get(i));
                                cardsToPlay.add(hand.get(i+1));
                                hand.remove(i+1);
                                hand.remove(i);
                                break;
                            }
                        }
                    }
                }
            }
            // 其他牌型暂不处理,AI选择不出
        }
        
        // 如果找不到能管的牌,选择不出
        if (cardsToPlay == null) {
            consecutivePass++;
            System.out.println(name + " 选择不要, consecutivePass=" + consecutivePass);
            showMessage(name + " 不要", Color.GRAY);
            
            if (consecutivePass >= 2) {
                lastPlayedCards = null;
                playedCardsPanel.removeAll();
                player2PlayedPanel.removeAll();
                player3PlayedPanel.removeAll();
                playedCardsPanel.revalidate();
                playedCardsPanel.repaint();
                player2PlayedPanel.revalidate();
                player2PlayedPanel.repaint();
                player3PlayedPanel.revalidate();
                player3PlayedPanel.repaint();
                showMessage("没人要," + name + "可以任意出牌", new Color(72, 187, 120));
            }
            
            nextPlayer();
            return;
        }
        
        // 清除该玩家之前的出牌
        playedPanel.removeAll();
        
        // 显示新出的牌
        for (String card : cardsToPlay) {
            playedPanel.add(createCardLabel(card));
        }
        playedPanel.revalidate();
        playedPanel.repaint();
        
        lastPlayedCards = new ArrayList<>(cardsToPlay);
        consecutivePass = 0;
        updateOtherPlayerCardsVertical(cardsPanel, hand.size());
        showMessage(name + " 出了 " + cardsToPlay.size() + " 张牌", Color.YELLOW);
        
        if (hand.isEmpty()) {
            JOptionPane.showMessageDialog(this, name + " 赢了!", "游戏结束", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        nextPlayer();
    }
    
    class GradientPanel extends JPanel {
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            GradientPaint gp = new GradientPaint(0, 0, new Color(20, 50, 80),
                getWidth(), getHeight(), new Color(40, 80, 120));
            g2.setPaint(gp);
            g2.fillRect(0, 0, getWidth(), getHeight());
            g2.dispose();
        }
    }
}
