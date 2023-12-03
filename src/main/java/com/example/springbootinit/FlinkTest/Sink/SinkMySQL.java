package com.example.springbootinit.FlinkTest.Sink;


import com.example.springbootinit.FlinkTest.Entity.Student;
import com.example.springbootinit.FlinkTest.functions.StudentMapFunction;
import org.apache.flink.connector.jdbc.JdbcConnectionOptions;
import org.apache.flink.connector.jdbc.JdbcExecutionOptions;
import org.apache.flink.connector.jdbc.JdbcSink;
import org.apache.flink.connector.jdbc.JdbcStatementBuilder;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.sink.SinkFunction;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * TODO
 *
 * @author cjp
 * @version 1.0
 */
public class SinkMySQL {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);

        //从source读取数据，可以是kafka、socket、文件，此处是从socket获取
        SingleOutputStreamOperator<Student> sensorDS = env
                .socketTextStream("192.168.30.130", 7777)
                .map(new StudentMapFunction());


        /**
         * TODO 写入mysql
         * 1、只能用老的sink写法： addsink
         * 2、JDBCSink的4个参数:
         *    第一个参数： 执行的sql，一般就是 insert into
         *    第二个参数： 预编译sql， 对占位符填充值
         *    第三个参数： 执行选项 ---》 攒批、重试
         *    第四个参数： 连接选项 ---》 url、用户名、密码
         */
        SinkFunction<Student> jdbcSink = JdbcSink.sink(
                "insert into student (name,age) values (?,?)",
                new JdbcStatementBuilder<Student>() {
                    @Override
                    public void accept(PreparedStatement ps, Student student) throws SQLException {
                        ps.setString(1, student.getName());
                        ps.setInt(2, student.getAge());
                    }
                },
                JdbcExecutionOptions.builder()
                        .withMaxRetries(3)
                        .withBatchSize(100)
                        .withBatchIntervalMs(3000)
                        .build(),
                new JdbcConnectionOptions.JdbcConnectionOptionsBuilder()
                        .withUrl("jdbc:mysql://localhost:3306/cloudcalculate?serverTimezone=GMT%2B8&useUnicode=true&characterEncoding=utf-8")
                        .withUsername("root")
                        .withPassword("243121168lyk1018")
                        .withConnectionCheckTimeoutSeconds(60)
                        .build()
        );
        sensorDS.print();
        sensorDS.rebalance().addSink(jdbcSink);
        env.execute();
    }
}
