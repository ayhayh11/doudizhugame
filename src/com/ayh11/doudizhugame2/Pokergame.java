package com.ayh11.doudizhugame2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

public class Pokergame {
    static ArrayList<String> list = new ArrayList<>();
    static HashMap<String, Integer> pokermap = new HashMap<>();

    static {
        String[] colors = {"♦", "♣", "♥", "♠"};
        String[] numbers = {"3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K", "A", "2"};
        for (String number : numbers) {
            for (String color : colors) {
                list.add(color + number);
            }
        }
        list.add("小王");
        list.add("大王");
        pokermap.put("J", 11);
        pokermap.put("Q", 12);
        pokermap.put("K", 13);
        pokermap.put("A", 14);
        pokermap.put("2", 15);
        pokermap.put("小王", 50);
        pokermap.put("大王", 100);
       /* System.out.println(list);
        System.out.println(pokermap);*/
    }

    public Pokergame() {
        //1.洗牌
        java.util.Collections.shuffle(list);
        // System.out.println(list);
        //2.发牌
        ArrayList<String> player1 = new ArrayList<>();
        ArrayList<String> player2 = new ArrayList<>();
        ArrayList<String> player3 = new ArrayList<>();
        ArrayList<String> dipai = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            if (i >= 51) {
                dipai.add(list.get(i));
            } else {
                if (i % 3 == 0) {
                    player1.add(list.get(i));
                } else if (i % 3 == 1) {
                    player2.add(list.get(i));
                } else {
                    player3.add(list.get(i));
                }
            }
        }
        //3.排序
        sort(player1);
        sort(player2);
        sort(player3);
        sort(dipai);

        //4.看牌(控制台输出)
        lookpoker("玩家1", player1);
        lookpoker("玩家2", player2);
        lookpoker("玩家3", player3);
        lookpoker("底牌", dipai);
        
        //5.打开游戏窗口(显示所有玩家手牌)
        javax.swing.SwingUtilities.invokeLater(() -> {
            new GameFrame(player1, player2, player3, dipai).setVisible(true);
        });

    }

    //排序
    public void sort(ArrayList<String> list) {
        Collections.sort(list, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                String color1 = o1.substring(0, 1);
                int value1 = getvalue(o1);
                String color2 = o2.substring(0, 1);
                int value2 = getvalue(o2);
                int i = value2 - value1;
                return i == 0 ? color2.compareTo(color1):i;
            }
        });
    }

    //获取牌值
    public int getvalue(String poker) {
        if (poker.contains("小王")) {
            return 50;
        }
        if (poker.contains("大王")) {
            return 100;
        }
        String val = poker.substring(1);
        if (pokermap.containsKey(val)) {
            return pokermap.get(val);
        } else {
            return Integer.parseInt(val);
        }
    }

    //看牌
    public void lookpoker(String name, ArrayList<String> list) {
        System.out.print(name + "的牌是：");
        for (String s : list) {
            System.out.print(s + " ");
        }
        System.out.println();
    }
}
