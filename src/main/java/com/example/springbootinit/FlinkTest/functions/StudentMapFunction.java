package com.example.springbootinit.FlinkTest.functions;


import com.example.springbootinit.FlinkTest.Entity.Student;
import org.apache.flink.api.common.functions.MapFunction;

/**
 * 自定义map算子
 * 以，为分隔解析数据
 */
public class StudentMapFunction implements MapFunction<String, Student> {
    @Override
    public Student map(String s) throws Exception {
        String[] data = s.split(",");
        return new Student(data[0],Integer.valueOf(data[1]));
    }
}
