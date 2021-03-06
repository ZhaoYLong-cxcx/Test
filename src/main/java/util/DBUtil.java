package util;

import org.sqlite.SQLiteConfig;
import org.sqlite.SQLiteDataSource;

import javax.sql.DataSource;
import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

//主要用于连接和关闭数据库资源    利用DataSource
public class DBUtil {
    private static volatile DataSource DATA_SOURCE;

    private DBUtil(){

    }

    private static String getUrl() throws URISyntaxException {
        String dbName = "xiaotianquan.db";
        URL url = DBUtil.class.getClassLoader().getResource(".");
        return "jdbc:sqlite://" + (new File(url.toURI()).getParent() + File.separator + dbName);
    }
    public static DataSource getDataSource() throws URISyntaxException {
        if(DATA_SOURCE==null){
            synchronized (DBUtil.class){
                if(DATA_SOURCE==null){
                    SQLiteConfig config = new SQLiteConfig();
                    config.setDateStringFormat(Util.DATE_PATTERN);
                    DATA_SOURCE = new SQLiteDataSource(config);
                    ((SQLiteDataSource) DATA_SOURCE).setUrl(getUrl());
                }
            }
        }
        return DATA_SOURCE;
    }
    public static Connection getConnection(){
        try {
            return getDataSource().getConnection();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("数据库连接失败");
        }
    }
    public static void close(Connection connection, Statement statement, ResultSet resultSet){
        try {
            if(resultSet!=null){
                resultSet.close();
            }
            if(statement!=null){
                statement.close();
            }
            if(connection!=null){
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("释放数据库资源错误");
        }
    }

    public static void close(Connection connection, Statement statement){
        close(connection,statement,null);
    }
  /*  public static void main(String[] args) {
        Connection connection = getConnection();
    }*/
  public static void main(String[] args) throws URISyntaxException {
        /*URL url = DBUtil.class.getClassLoader().getResource("./init.sql");
        System.out.println(new File(url.toURI()).getParent());*/
      Connection connection = getConnection();
  }
}
