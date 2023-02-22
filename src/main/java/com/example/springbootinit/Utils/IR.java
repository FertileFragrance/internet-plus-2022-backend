package com.example.springbootinit.Utils;

import com.example.springbootinit.Entity.InnerPolicy;
import org.apache.commons.lang3.tuple.Pair;

import java.sql.*;
import java.util.*;

public class IR {

    private static final String URL = "jdbc:mysql://localhost:3306/internet2022";
    private static final String USER = "root";
    private static final String PASSWORD = "123456";

    private static final ArrayList<InnerPolicy> INNER_POLICIES = new ArrayList<>();

    private static final int SELECTED_TOP_K = 8;

    public static void main(String[] args) {
        buildInnerPolicies();
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM penalty");
            // 如果有数据，rs.next()返回true
            while (rs.next()) {
                System.out.println(rs.getInt("id") + " " + rs.getString("basis"));
                matchInnerPolicy(rs.getInt("id"), rs.getString("basis"));
            }
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
    }

    private static void buildInnerPolicies() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM inner_policy");
            while (rs.next()) {
                INNER_POLICIES.add(new InnerPolicy(rs.getInt("id"), rs.getString("file"),
                        rs.getString("department"), rs.getString("chapter"),
                        rs.getInt("article"), rs.getString("content")));
            }
            conn.close();
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
        System.out.println(INNER_POLICIES);
    }

    private static void matchInnerPolicy(Integer penaltyId, String basis) {
        Map<String, Float> keywords = TextRankKeyword.getKeyword("", basis);
        if (keywords.size() < 10) {
            return;
        }
        System.out.println(keywords);
        ArrayList<Pair<Integer, Double>> cosineValues = calculateSimilarity(keywords, basis);
        // 插入数据库
        Connection conn = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
        String sql = "INSERT INTO text_match(penalty_id, inner_policy_id, ranking, cosine) VALUES(?, ?, ?, ?)";
        for (int i = 0; i < SELECTED_TOP_K; i++) {
            if (i >= cosineValues.size()) {
                break;
            }
            Pair<Integer, Double> cosineValue = cosineValues.get(i);
            int innerPolicyId = INNER_POLICIES.get(cosineValue.getLeft()).getId();
            try {
                assert conn != null;
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setInt(1, penaltyId);
                ps.setInt(2, innerPolicyId);
                ps.setInt(3, i + 1);
                ps.setDouble(4, cosineValue.getRight());
                ps.executeUpdate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        try {
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static ArrayList<Pair<Integer, Double>> calculateSimilarity(Map<String, Float> keywords, String basis) {
        // wordCounter记录每个关键词出现在多少个innerPolicy中，用于计算IDF
        Map<String, Integer> wordCounter = new HashMap<>(keywords.size());
        for (String keyword : keywords.keySet()) {
            wordCounter.put(keyword, 0);
        }
        for (InnerPolicy innerPolicy : INNER_POLICIES) {
            for (String keyword : keywords.keySet()) {
                int cnt = countStr(innerPolicy.getContent(), keyword);
                if (cnt > 0) {
                    wordCounter.put(keyword, wordCounter.get(keyword) + 1);
                }
            }
        }
        // 对于每个关键词，记录它在各个innerPolicy中的TF，以下ArrayList的索引即是innerPolicy的索引
        ArrayList<Map<String, Double>> tfList = new ArrayList<>();
        for (InnerPolicy innerPolicy : INNER_POLICIES) {
            Map<String, Double> tf = new HashMap<>(keywords.size());
            for (String keyword : keywords.keySet()) {
                int cnt = countStr(innerPolicy.getContent(), keyword);
                // tf等于该关键词在innerPolicy中出现的次数除以innerPolicy的总词数
                tf.put(keyword, (double) cnt / TextRankKeyword.countWord(innerPolicy.getContent()));
            }
            tfList.add(tf);
        }
        // 对于每个关键词，记录它的IDF，计算个数时要算上basis本身
        Map<String, Double> idfMap = new HashMap<>(keywords.size());
        for (String keyword : keywords.keySet()) {
            // idf等于总文档数，即INNER_POLICIES的大小加1，除以包含该关键词的文档数加1，即wordCounter的大小加2
            double idf = Math.log10((double) (INNER_POLICIES.size() + 1) / wordCounter.get(keyword) + 2);
            idf = Math.max(idf, 0);
            idfMap.put(keyword, idf);
        }
        // 对于每个关键词，记录它在basis文档中的TF
        Map<String, Double> basisTf = new HashMap<>(keywords.size());
        for (String keyword : keywords.keySet()) {
            int cnt = countStr(basis, keyword);
            basisTf.put(keyword, (double) cnt / TextRankKeyword.countWord(basis));
        }
        // 构建除basis以外的term-document矩阵，文档特征选择为TF*IDF
        ArrayList<Map<String, Double>> matrix = new ArrayList<>();
        for (Map<String, Double> tf : tfList) {
            Map<String, Double> tfidf = new HashMap<>(keywords.size());
            for (String keyword : keywords.keySet()) {
                tfidf.put(keyword, tf.get(keyword) * idfMap.get(keyword));
            }
            matrix.add(tfidf);
        }
        // 构建basis向量
        Map<String, Double> basisVector = new HashMap<>(keywords.size());
        for (String keyword : keywords.keySet()) {
            basisVector.put(keyword, basisTf.get(keyword) * idfMap.get(keyword));
        }
        // 计算basis向量与matrix各向量的余弦值
        double basisModulus = calculateModulus(basisVector);
        // cosinesValues的各个元素，左项表示INNER_POLICIES的索引，右项表示basis与该文档向量的余弦
        ArrayList<Pair<Integer, Double>> cosineValues = new ArrayList<>();
        int index = 0;
        for (Map<String, Double> innerPolicyVector : matrix) {
            double innerPolicyModulus = calculateModulus(innerPolicyVector);
            double dotProduct = calculateDotProduct(basisVector, innerPolicyVector);
            cosineValues.add(Pair.of(index, dotProduct / (basisModulus * innerPolicyModulus)));
            index++;
        }
        cosineValues.sort((o1, o2) -> o2.getRight().compareTo(o1.getRight()));
        return cosineValues;
    }

    /**
     * count how many str2 is contained in str1
     */
    private static int countStr(String str1, String str2) {
        int counter = 0;
        while (str1.contains(str2)) {
            counter++;
            str1 = str1.substring(str1.indexOf(str2) + str2.length());
        }
        return counter;
    }

    private static double calculateModulus(Map<String, Double> vector) {
        double modulus = 0;
        for (String key : vector.keySet()) {
            modulus += vector.get(key) * vector.get(key);
        }
        modulus = Math.sqrt(modulus);
        return modulus;
    }

    private static double calculateDotProduct(Map<String, Double> vec1, Map<String, Double> vec2) {
        double dotProduct = 0;
        for (String key : vec1.keySet()) {
            dotProduct += vec1.get(key) * vec2.get(key);
        }
        return dotProduct;
    }

}
