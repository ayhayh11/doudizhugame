package com.ayh11.doudizhugame;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeSet;

public class Pokergame {
    static ArrayList<Integer> list = new ArrayList<>();
    static HashMap<Integer, String> pokermap = new HashMap<>();

    static {
        int weight = 1;
        String[] colors = {"♦", "♣", "♥", "♠"};
        String[] numbers = {"3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K", "A", "2"};
        for (String number : numbers) {
            for (String color : colors) {
                pokermap.put(weight, color + number);
                list.add(weight);
                weight++;
            }
        }
        list.add(weight);
        pokermap.put(weight, "小王");
        weight++;
        list.add(weight);
        pokermap.put(weight, "大王");
 /*       System.out.println(list);
        System.out.println(pokermap);*/
    }

    public Pokergame() {
        //1.洗牌
        java.util.Collections.shuffle(list);
       /* System.out.println(list);*/
        //2.发牌
        TreeSet<Integer> player1 = new TreeSet<>();
        TreeSet<Integer> player2 = new TreeSet<>();
        TreeSet<Integer> player3 = new TreeSet<>();
        TreeSet<Integer> dipai = new TreeSet<>();
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

        //3.看牌
        lookpoker("玩家1", player1);
        lookpoker("玩家2", player2);
        lookpoker("玩家3", player3);
        lookpoker("底牌", dipai);

    }


    public void lookpoker(String name, TreeSet<Integer> ts) {
        System.out.print(name + "的牌是：");
        for (int s : ts) {
            String s1 = pokermap.get(s);
            System.out.print(s1 + " ");
        }
        System.out.println();
    }
}
