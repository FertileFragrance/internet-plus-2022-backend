package com.example.springbootinit.Service.Impl;

import com.example.springbootinit.Entity.InnerPolicy;
import com.example.springbootinit.Entity.Penalty;
import com.example.springbootinit.Entity.TextMatch;
import com.example.springbootinit.Repository.PenaltyRepository;
import com.example.springbootinit.Repository.TextMatchRepository;
import com.example.springbootinit.Service.SimilarityService;
import com.example.springbootinit.VO.PenaltyVO;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.functions.ReduceFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.CloseableIterator;
import org.apache.flink.util.Collector;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.Serializable;
import java.sql.*;
import java.time.LocalDate;
import java.util.*;

@Service
public class SimilarityServiceImpl implements SimilarityService {

    private static final String URL = "jdbc:mysql://localhost:3306/internet2022?zeroDateTimeBehavior=CONVERT_TO_NULL";
    private static final String USER = "root";
    private static final String PASSWORD = "123456";
    private static final ArrayList<Penalty> PENALTIES = new ArrayList<>();

    public SimilarityServiceImpl() {
        buildPenalties();
    }

    @Resource
    private PenaltyRepository penaltyRepository;

    @Resource
    private TextMatchRepository textMatchRepository;

    static class MapTextMatchToInnerPolicy implements Serializable, MapFunction<TextMatch, InnerPolicy> {
        @Override
        public InnerPolicy map(TextMatch textMatch) throws Exception {
            InnerPolicy innerPolicy = null;
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM inner_policy where id = " + textMatch.getInnerPolicyId());
                while (rs.next()) {
                    innerPolicy = new InnerPolicy(rs.getInt("id"), rs.getString("file"),
                            rs.getString("department"), rs.getString("chapter"),
                            rs.getString("article"), rs.getString("content"));
                }
                conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return innerPolicy;
        }
    }

    static class FlatMapInnerPolicyToTuple2 implements Serializable, FlatMapFunction<InnerPolicy, Tuple2<InnerPolicy, Penalty>> {
        private final Integer penaltyId;

        public FlatMapInnerPolicyToTuple2(Integer penaltyId) {
            this.penaltyId = penaltyId;
        }

        @Override
        public void flatMap(InnerPolicy innerPolicy, Collector<Tuple2<InnerPolicy, Penalty>> collector) throws Exception {
            List<TextMatch> matches = new ArrayList<>();
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM text_match where inner_policy_id = " + innerPolicy.getId());
                while (rs.next()) {
                    TextMatch textMatch = new TextMatch();
                    textMatch.setPenaltyId(rs.getInt("penalty_id"));
                    textMatch.setInnerPolicyId(innerPolicy.getId());
                    textMatch.setRanking(rs.getInt("ranking"));
                    textMatch.setCosine(rs.getDouble("cosine"));
                    matches.add(textMatch);
                }
                conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            for (TextMatch match : matches) {
                if (match.getCosine() >= 0.7 && !Objects.equals(match.getPenaltyId(), penaltyId)) {
                    try {
                        Class.forName("com.mysql.cj.jdbc.Driver");
                        Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
                        Statement stmt = conn.createStatement();
                        ResultSet rs = stmt.executeQuery("SELECT * FROM penalty where id = " + match.getPenaltyId());
                        while (rs.next()) {
                            LocalDate date = null;
                            try {
                                date = rs.getDate("date").toLocalDate();
                            } catch (Exception ignored) {
                            }
                            if (date == null) {
                                continue;
                            }
                            Penalty penalty = new Penalty(rs.getInt("id"), rs.getString("name"),
                                    rs.getString("number"), rs.getInt("type"),
                                    rs.getString("partyName"), rs.getString("responsiblePersonName"),
                                    rs.getString("facts"), rs.getString("basis"),
                                    rs.getString("decision"), rs.getString("punishmentType"),
                                    rs.getDouble("fine"), rs.getString("organName"),
                                    rs.getString("province"), date, rs.getInt("status"));
                            collector.collect(Tuple2.of(innerPolicy, penalty));
                        }
                        conn.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    static class MapTuple2ToPenalty implements Serializable, MapFunction<Tuple2<InnerPolicy, Penalty>, Penalty> {
        @Override
        public Penalty map(Tuple2<InnerPolicy, Penalty> innerPolicyPenaltyTuple2) throws Exception {
            return innerPolicyPenaltyTuple2.f1;
        }
    }

    @Override
    public List<PenaltyVO> calByBasis(Integer penaltyId) {
        Optional<Penalty> optionalPenalty = penaltyRepository.findById(penaltyId);
        if (optionalPenalty.isEmpty()) {
            return new ArrayList<>();
        }
        Penalty penalty = optionalPenalty.get();
        String basis = penalty.getBasis();
        if (basis == null || basis.trim().isEmpty()) {
            return new ArrayList<>();
        }
        List<TextMatch> textMatches = textMatchRepository.findByPenaltyId(penaltyId);
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        DataStream<Penalty> dataStream = env.fromCollection(textMatches)
                .filter(e -> e.getCosine() >= 0.7)
                .map(new MapTextMatchToInnerPolicy())
                .flatMap(new FlatMapInnerPolicyToTuple2(penaltyId))
                .keyBy(e -> e.f1)
                .reduce((ReduceFunction<Tuple2<InnerPolicy, Penalty>>) (e1, e2) -> e1)
                .map(new MapTuple2ToPenalty());
        HashSet<PenaltyVO> resSet = new HashSet<>();
        try {
            CloseableIterator<Penalty> iterator = dataStream.executeAndCollect();
            iterator.forEachRemaining(e -> {
                resSet.add(convertPenalty(e));
            });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return new ArrayList<>(resSet);
    }

    @Override
    public List<PenaltyVO> calByFine(Integer penaltyId) {
        Optional<Penalty> optionalPenalty = penaltyRepository.findById(penaltyId);
        if (optionalPenalty.isEmpty()) {
            return new ArrayList<>();
        }
        Penalty penalty = optionalPenalty.get();
        Double fine = penalty.getFine();
        if (fine == null || fine.isNaN() || fine.isInfinite() || fine == 0) {
            return new ArrayList<>();
        }
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        DataStream<Penalty> dataStream = env.fromCollection(PENALTIES)
                .filter(e -> e.getFine() != null && !e.getFine().isNaN() && !e.getFine().isInfinite()
                        && e.getFine() != 0 && !Objects.equals(e.getId(), penaltyId))
                .filter(e -> Math.abs(e.getFine() - fine) <= 1000
                        || 0.9 <= e.getFine() / fine && e.getFine() / fine <= 1.1);
        ArrayList<PenaltyVO> res = new ArrayList<>();
        try {
            CloseableIterator<Penalty> iterator = dataStream.executeAndCollect();
            iterator.forEachRemaining(e -> {
                res.add(convertPenalty(e));
            });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return res;
    }

    @Override
    public List<PenaltyVO> calByPartyName(Integer penaltyId) {
        Optional<Penalty> optionalPenalty = penaltyRepository.findById(penaltyId);
        if (optionalPenalty.isEmpty()) {
            return new ArrayList<>();
        }
        Penalty penalty = optionalPenalty.get();
        String partyName = penalty.getPartyName();
        if (partyName == null || partyName.trim().isEmpty()) {
            return new ArrayList<>();
        }
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        DataStream<Penalty> dataStream = env.fromCollection(PENALTIES)
                .filter(e -> e.getPartyName() != null && !e.getPartyName().trim().isEmpty()
                        && !Objects.equals(e.getId(), penaltyId))
                .filter(e -> partyName.trim().equals(e.getPartyName().trim()));
        ArrayList<PenaltyVO> res = new ArrayList<>();
        try {
            CloseableIterator<Penalty> iterator = dataStream.executeAndCollect();
            iterator.forEachRemaining(e -> {
                res.add(convertPenalty(e));
            });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return res;
    }

    @Override
    public List<PenaltyVO> calByOrganName(Integer penaltyId) {
        Optional<Penalty> optionalPenalty = penaltyRepository.findById(penaltyId);
        if (optionalPenalty.isEmpty()) {
            return new ArrayList<>();
        }
        Penalty penalty = optionalPenalty.get();
        String organName = penalty.getOrganName();
        if (organName == null || organName.trim().isEmpty()) {
            return new ArrayList<>();
        }
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        DataStream<Penalty> dataStream = env.fromCollection(PENALTIES)
                .filter(e -> e.getOrganName() != null && !e.getPartyName().trim().isEmpty()
                        && !Objects.equals(e.getId(), penaltyId))
                .filter(e -> organName.trim().equals(e.getOrganName().trim()));
        ArrayList<PenaltyVO> res = new ArrayList<>();
        try {
            CloseableIterator<Penalty> iterator = dataStream.executeAndCollect();
            iterator.forEachRemaining(e -> {
                res.add(convertPenalty(e));
            });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return res;
    }

    private static void buildPenalties() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM penalty");
            while (rs.next()) {
                LocalDate date = null;
                try {
                    date = rs.getDate("date").toLocalDate();
                } catch (Exception ignored) {
                }
                if (date == null) {
                    continue;
                }
                PENALTIES.add(new Penalty(rs.getInt("id"), rs.getString("name"),
                        rs.getString("number"), rs.getInt("type"),
                        rs.getString("partyName"), rs.getString("responsiblePersonName"),
                        rs.getString("facts"), rs.getString("basis"),
                        rs.getString("decision"), rs.getString("punishmentType"),
                        rs.getDouble("fine"), rs.getString("organName"),
                        rs.getString("province"), date, rs.getInt("status")));
            }
            conn.close();
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
    }

    private PenaltyVO convertPenalty(Penalty penalty) {
        PenaltyVO penaltyVO = new PenaltyVO();
        BeanUtils.copyProperties(penalty, penaltyVO);
        penaltyVO.setId(penalty.getId().toString());
        penaltyVO.setType(penalty.getType().toString());
        penaltyVO.setFine(penalty.getFine().toString());
        LocalDate date = penalty.getDate();
        if (date == null) {
            penaltyVO.setDate("0000-00-00");
        } else {
            penaltyVO.setDate(date.toString());
        }
        penaltyVO.setStatus(penalty.getStatus().toString());
        return penaltyVO;
    }
}
