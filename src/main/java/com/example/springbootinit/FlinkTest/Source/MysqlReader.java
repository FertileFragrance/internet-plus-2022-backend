package com.example.springbootinit.FlinkTest.Source;


import com.example.springbootinit.FlinkTest.Entity.Student;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.source.RichSourceFunction;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class MysqlReader extends RichSourceFunction<Student> {
    private volatile boolean isRunning = true;
    private Connection conn = null;
    private PreparedStatement ps = null;

    //配置数据库连接和准备查询语句
    @Override
    public void open(Configuration parameters) throws Exception {
        super.open(parameters);
        Class.forName("com.mysql.cj.jdbc.Driver");  // 加载数据库驱动
        conn = (Connection) DriverManager.getConnection(  // 获取连接
                "jdbc:mysql://localhost:3306/cloudcalculate?serverTimezone=GMT%2B8&useSSL=false",  // 数据库URL
                "root",  // 用户名
                "243121168lyk1018");  // 登录密码
        ps = (PreparedStatement) conn.prepareStatement(  // 获取执行语句
                "select * from student");  // 需要执行的SQL语句
    }

    //执行查询
    @Override
    public void run(SourceContext<Student> ctx) throws Exception {
        while(isRunning) {  // 使用while循环可以不断读取数据
            ResultSet resultSet = ps.executeQuery();
            while(resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                int age = resultSet.getInt("age");
                ctx.collect(new Student(id,name,age));  // 以流的形式发送结果
            }
            Thread.sleep(5000);  // 每隔5秒查询一次
        }
    }

    @Override
    public void cancel() {
        isRunning = false;
    }
    @Override
    public void close() throws Exception {
        super.close();
        if(conn != null) conn.close();
        if(ps != null) ps.close();
    }

}
