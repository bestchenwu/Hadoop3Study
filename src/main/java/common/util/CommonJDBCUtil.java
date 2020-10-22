package common.util;


import java.sql.*;

/**
 * 通用JDBC的工具类
 *
 * @author chenwu on 2020.10.22
 */
public class CommonJDBCUtil {

    private static volatile CommonJDBCUtil commonJDBCUtil;
    private static final String INSERT_SQL = "insert into topic_offset_record (topic_name,topic_partition,topic_offset) values(?,?,?) ";
    private static final String QUERY_SQL = "select topic_offset from topic_offset_record where topic_name = ? and topic_partition = ? order by update_time desc";
    private Connection connection;

    public static CommonJDBCUtil getInstance(){
        synchronized (CommonJDBCUtil.class){
            if(commonJDBCUtil == null){
                try{
                    commonJDBCUtil = new CommonJDBCUtil();
                }catch(SQLException e){
                    throw new RuntimeException(e);
                }
            }
        }
        return commonJDBCUtil;
    }

    private CommonJDBCUtil() throws SQLException {
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
        }catch(Exception e){
            throw new RuntimeException(e);
        }
        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/babytree?serverTimezone=UTC&useSSL=false", "root", "123456");
        connection.setAutoCommit(false);
    }

    /**
     * 将topic 、分区、分区里面的位移存入到mysql
     *
     * @param topic
     * @param partition
     * @param offset
     * @throws SQLException
     * @author chenwu on 2020.10.22
     */
    public void insertTopicOffset(String topic,int partition,long offset) throws SQLException{
        PreparedStatement preparedStatement = connection.prepareStatement(INSERT_SQL);
        preparedStatement.setString(1,topic);
        preparedStatement.setInt(2,partition);
        preparedStatement.setLong(3,offset);
        preparedStatement.execute();
        connection.commit();
    }

    /**
     * 根据topic和分区number获取位移
     *
     * @param topic
     * @param parition
     * @return offset 分区最新位移
     * @throws SQLException
     * @author chenwu on 2020.10.22
     */
    public long getOffset(String topic,int parition) throws SQLException{
        PreparedStatement preparedStatement = connection.prepareStatement(QUERY_SQL);
        preparedStatement.setString(1,topic);
        preparedStatement.setInt(2,parition);
        ResultSet resultSet = preparedStatement.executeQuery();
        long topic_offset = 0l;
        if(resultSet.next()){
            topic_offset = resultSet.getLong("topic_offset");
        }
        return topic_offset;
    }

    private void close(){
        if(connection!=null){
            try{
                connection.close();
            }catch(SQLException e){
                throw new RuntimeException(e);
            }
        }
    }
}
