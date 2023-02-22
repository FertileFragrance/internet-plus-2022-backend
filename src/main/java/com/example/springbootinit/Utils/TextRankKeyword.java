package com.example.springbootinit.Utils;

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.dictionary.stopword.CoreStopWordDictionary;
import com.hankcs.hanlp.seg.common.Term;

import java.util.*;

/**
 * TextRank关键词提取
 *
 * @author hankcs
 */
public class TextRankKeyword {
    public static final int N_KEYWORD = 10;
    /**
     * 阻尼系数（ＤａｍｐｉｎｇＦａｃｔｏｒ），一般取值为0.85
     */
    static final float D = 0.85f;
    /**
     * 最大迭代次数
     */
    static final int MAX_ITER = 200;
    static final float MIN_DIFF = 0.001f;

    public TextRankKeyword() {
        // jdk bug : Exception in thread "main" java.lang.IllegalArgumentException:
        // Comparison method violates its general contract!
        System.setProperty("java.util.Arrays.useLegacyMergeSort", "true");
    }

    public static int countWord(String content){
        List<Term> termList = HanLP.segment(content);
        List<String> wordList = new ArrayList<>();
        for (Term t : termList) {
            if (shouldInclude(t)) {
                wordList.add(t.word);
            }
        }
        return wordList.size();
    }

    public static Map<String,Float> getKeyword(String title, String content) {
        List<Term> termList = HanLP.segment(title + content);
        List<String> wordList = new ArrayList<>();
        for (Term t : termList) {
            if (shouldInclude(t)) {
                wordList.add(t.word);
            }
        }
        Map<String, Set<String>> words = new HashMap<>();
        Queue<String> que = new LinkedList<>();
        for (String w : wordList) {
            if (!words.containsKey(w)) {
                words.put(w, new HashSet<>());
            }
            que.offer(w);
            if (que.size() > 5) {
                que.poll();
            }

            for (String w1 : que) {
                for (String w2 : que) {
                    if (w1.equals(w2)) {
                        continue;
                    }

                    words.get(w1).add(w2);
                    words.get(w2).add(w1);
                }
            }
        }
        Map<String, Float> score = new HashMap<>();
        for (int i = 0; i < MAX_ITER; ++i) {
            Map<String, Float> m = new HashMap<>();
            float maxDiff = 0;
            for (Map.Entry<String, Set<String>> entry : words.entrySet()) {
                String key = entry.getKey();
                Set<String> value = entry.getValue();
                m.put(key, 1 - D);
                for (String other : value) {
                    int size = words.get(other).size();
                    if (key.equals(other) || size == 0) {
                        continue;
                    }
                    m.put(key, m.get(key) + D / size * (score.get(other) == null ? 0 : score.get(other)));
                }
                maxDiff = Math.max(maxDiff, Math.abs(m.get(key) - (score.get(key) == null ? 0 : score.get(key))));
            }
            score = m;
            if (maxDiff <= MIN_DIFF) {
                break;
            }
        }
        List<Map.Entry<String, Float>> entryList = new ArrayList<>(score.entrySet());
        entryList.sort((o1, o2) -> o2.getValue().compareTo(o1.getValue()));
        int num = Math.min(N_KEYWORD, entryList.size());
        Map<String,Float> result = new HashMap<>(num);
        for (int i = 0; i < num; ++i) {
            result.put(entryList.get(i).getKey(),entryList.get(i).getValue());
        }
        return result;
    }

    public static void main(String[] args) {
        String content = "程序员(英文Programmer)是从事程序开发、维护的专业人员。一般将程序员分为程序设计人员和程序编码人员，但两者的界限并不非常清楚，特别是在中国。软件从业人员分为初级程序员、高级程序员、系统分析员和项目经理四大类。";
        System.out.println(TextRankKeyword.getKeyword("", content));

    }

    /**
     * 是否应当将这个term纳入计算，词性属于名词、动词、副词、形容词
     *
     * @param term 某个词
     * @return 是否应当
     */
    public static boolean shouldInclude(Term term) {
        return CoreStopWordDictionary.shouldInclude(term);
    }
}

