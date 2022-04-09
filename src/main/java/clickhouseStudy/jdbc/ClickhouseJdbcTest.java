package clickhouseStudy.jdbc;

import java.sql.*;

/**
 * clickhouse的jdbc测试类
 */
public class ClickhouseJdbcTest {

    public static void main(String[] args) {
        try{
            Class.forName("com.clickhouse.jdbc.ClickHouseDriver");
        }catch(ClassNotFoundException e){
            throw new RuntimeException(e);
        }
        String url = "jdbc:clickhouse://master:8123/default";
        String user = "default";
        String password = "123456";
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try{
             connection = DriverManager.getConnection(url,user,password);
             statement = connection.createStatement();
            resultSet = statement.executeQuery("select * from ontime_all");
            while(resultSet.next()){
                String flightDate = resultSet.getString("FlightDate");
                System.out.println(flightDate);
            }
        }catch(SQLException e){
            e.printStackTrace();
        }finally {
            try{
                resultSet.close();
                statement.close();
                connection.close();
            }catch (SQLException e){
                e.printStackTrace();
            }

        }

    }
}
