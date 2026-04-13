package com.ayh11.doudizhugame2;

import java.util.*;

/**
 * 牌型判断工具类
 */
public class CardTypeChecker {
    
    /**
     * 牌型枚举
     */
    public enum CardType {
        SINGLE,      // 单张
        PAIR,        // 对子
        TRIPLE,      // 三个
        TRIPLE_WITH_SINGLE,  // 三带一
        TRIPLE_WITH_PAIR,    // 三带二
        PLANE,       // 飞机(连续三个)
        PLANE_WITH_WINGS,    // 飞机带翅膀
        BOMB,        // 炸弹(四个)
        ROCKET,      // 王炸
        STRAIGHT,    // 顺子(5张以上连续)
        STRAIGHT_PAIR,  // 连对(3个以上连续对子)
        INVALID      // 无效牌型
    }
    
    /**
     * 获取牌的权重值
     */
    public static int getCardValue(String card) {
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
    
    /**
     * 判断牌型
     */
    public static CardType checkCardType(List<String> cards) {
        if (cards == null || cards.isEmpty()) {
            System.out.println("  [DEBUG] 牌为空");
            return CardType.INVALID;
        }
        
        int size = cards.size();
        System.out.println("  [DEBUG] 检查牌型, 数量: " + size);
        for (String c : cards) {
            System.out.println("    - " + c);
        }
        
        // 王炸
        if (size == 2) {
            boolean hasSmallJoker = cards.stream().anyMatch(c -> c.trim().equals("小王"));
            boolean hasBigJoker = cards.stream().anyMatch(c -> c.trim().equals("大王"));
            if (hasSmallJoker && hasBigJoker) {
                return CardType.ROCKET;
            }
        }
        
        // 统计每个点数的牌数
        Map<Integer, Integer> valueCount = new HashMap<>();
        for (String card : cards) {
            int value = getCardValue(card);
            System.out.println("  牌: " + card + " -> 值: " + value);
            valueCount.put(value, valueCount.getOrDefault(value, 0) + 1);
        }
        
        List<Integer> values = new ArrayList<>(valueCount.keySet());
        Collections.sort(values);
        System.out.println("  values.size(): " + values.size() + ", size: " + size);
        
        // 单张
        if (size == 1) {
            return CardType.SINGLE;
        }
        
        // 对子
        if (size == 2 && values.size() == 1) {
            System.out.println("  [DEBUG] 识别为: 对子");
            return CardType.PAIR;
        }
        
        // 三个
        if (size == 3 && values.size() == 1) {
            return CardType.TRIPLE;
        }
        
        // 炸弹
        if (size == 4 && values.size() == 1) {
            return CardType.BOMB;
        }
        
        // 三带一
        if (size == 4 && values.size() == 2) {
            for (int count : valueCount.values()) {
                if (count == 3) {
                    return CardType.TRIPLE_WITH_SINGLE;
                }
            }
        }
        
        // 三带二
        if (size == 5 && values.size() == 2) {
            boolean hasTriple = false;
            boolean hasPair = false;
            for (int count : valueCount.values()) {
                if (count == 3) hasTriple = true;
                if (count == 2) hasPair = true;
            }
            if (hasTriple && hasPair) {
                return CardType.TRIPLE_WITH_PAIR;
            }
        }
        
        // 顺子(至少5张连续,不能有2和大小王)
        if (size >= 5 && values.size() == size) {
            boolean isStraight = true;
            for (int i = 0; i < values.size() - 1; i++) {
                if (values.get(i + 1) - values.get(i) != 1 || values.get(i) >= 15) {
                    isStraight = false;
                    break;
                }
            }
            if (isStraight) {
                return CardType.STRAIGHT;
            }
        }
        
        // 连对(至少3个连续的对子,不能有2和大小王)
        if (size >= 6 && size % 2 == 0 && values.size() == size / 2) {
            // 检查是否每个点数都有2张
            boolean allPairs = true;
            for (int count : valueCount.values()) {
                if (count != 2) {
                    allPairs = false;
                    break;
                }
            }
            
            if (allPairs) {
                // 检查是否连续
                boolean isContinuous = true;
                for (int i = 0; i < values.size() - 1; i++) {
                    if (values.get(i + 1) - values.get(i) != 1 || values.get(i) >= 15) {
                        isContinuous = false;
                        break;
                    }
                }
                if (isContinuous) {
                    System.out.println("  [DEBUG] 识别为: 连对");
                    return CardType.STRAIGHT_PAIR;
                }
            }
        }
        
        // 飞机(连续的两个或更多三个)
        if (size >= 6) {
            List<Integer> triples = new ArrayList<>();
            for (Map.Entry<Integer, Integer> entry : valueCount.entrySet()) {
                if (entry.getValue() == 3) {
                    triples.add(entry.getKey());
                }
            }
            Collections.sort(triples);
            
            if (triples.size() >= 2) {
                boolean isContinuous = true;
                for (int i = 0; i < triples.size() - 1; i++) {
                    if (triples.get(i + 1) - triples.get(i) != 1 || triples.get(i) >= 15) {
                        isContinuous = false;
                        break;
                    }
                }
                if (isContinuous) {
                    // 检查是否有带牌
                    int remainingCards = size - triples.size() * 3;
                    if (remainingCards == 0) {
                        return CardType.PLANE;  // 纯飞机
                    } else if (remainingCards == triples.size() || remainingCards == triples.size() * 2) {
                        return CardType.PLANE_WITH_WINGS;  // 飞机带翅膀
                    }
                }
            }
        }
        
        return CardType.INVALID;
    }
    
    /**
     * 比较两组牌的大小
     * @return 正数表示cards1大,负数表示cards2大,0表示相等,-999表示无法比较
     */
    public static int compareCards(List<String> cards1, List<String> cards2) {
        if (cards1 == null || cards1.isEmpty()) return -1;
        if (cards2 == null || cards2.isEmpty()) return 1;
        
        CardType type1 = checkCardType(cards1);
        CardType type2 = checkCardType(cards2);
        
        // 王炸最大
        if (type1 == CardType.ROCKET) return 1;
        if (type2 == CardType.ROCKET) return -1;
        
        // 炸弹比普通牌大
        if (type1 == CardType.BOMB && type2 != CardType.BOMB) return 1;
        if (type2 == CardType.BOMB && type1 != CardType.BOMB) return -1;
        
        // 同类型才能比较
        if (type1 != type2) return -999;
        
        // 获取主要点数
        int maxValue1 = getMaxValue(cards1);
        int maxValue2 = getMaxValue(cards2);
        
        return maxValue1 - maxValue2;
    }
    
    /**
     * 获取牌组中的最大点数
     */
    private static int getMaxValue(List<String> cards) {
        int maxVal = 0;
        Map<Integer, Integer> valueCount = new HashMap<>();
        
        for (String card : cards) {
            int value = getCardValue(card);
            valueCount.put(value, valueCount.getOrDefault(value, 0) + 1);
        }
        
        // 找到出现次数最多的点数
        int maxCount = 0;
        for (Map.Entry<Integer, Integer> entry : valueCount.entrySet()) {
            if (entry.getValue() > maxCount || 
                (entry.getValue() == maxCount && entry.getKey() > maxVal)) {
                maxCount = entry.getValue();
                maxVal = entry.getKey();
            }
        }
        
        return maxVal;
    }
}
