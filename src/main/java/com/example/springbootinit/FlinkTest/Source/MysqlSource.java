package com.example.springbootinit.FlinkTest.Source;


import com.example.springbootinit.FlinkTest.Entity.Student;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public class MysqlSource {
    public static void main(String[] args) throws Exception {
        // ToDo 0.env
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        // ToDo 1.source
        DataStream<Student> dataInput = env.addSource(new MysqlReader());
        // ToDo 3.sink
        dataInput.print();
        // ToDo 4.execute
        env.execute();
    }

}
