import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.alibaba.druid.pool.DruidPooledConnection;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.Properties;

/**
 * 创建系统自己所用的数据库连接池
 * 使用枚举类型实现单例模式
 * Created by Deng Jialong on 2017/2/25.
 */
public enum ServerDBConnection {
    // 单例模式实例
    INSTANCE;

    private DruidDataSource dds = null;

    private String propertyPath = "./dbConn.properties";

    ServerDBConnection() {
        Properties properties = new Properties();
        try {
            properties.load(Files.newInputStream(Paths.get(propertyPath)));
            System.out.println(properties.getProperty("url"));
            dds = (DruidDataSource) DruidDataSourceFactory.createDataSource(properties);
            System.out.println("数据库DruidDataSource创建完毕！");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public DruidPooledConnection getConnection() {
        try {
            return dds.getConnection();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }
}
