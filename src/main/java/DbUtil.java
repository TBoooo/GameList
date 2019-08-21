import com.alibaba.druid.pool.DruidPooledConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * @Author:Tang
 * @Description:
 * @Date:Created in 2018/3/14-10:27
 * Modified By:
 */
public class DbUtil {
    private static final Logger log= LoggerFactory.getLogger( DbUtil.class );

    public static int MysqlUpdate(String  sql , String ... param) {
        try {
            try (DruidPooledConnection connection = ServerDBConnection.INSTANCE.getConnection();
                 PreparedStatement pst = connection.prepareStatement(sql)) {

               for (int i=1;i<=param.length;i++){
                   pst.setString(i,param[i-1]);
               }
               return pst.executeUpdate();
            }
        } catch (SQLException e) {
            log.error( "数据库操作异常,详细信息:{}",e.getMessage() );
//            System.out.println("数据操作异常--》"+e.getMessage());
        }
        return 0;
    }
    public static boolean MysqlExceute(String  sql , String ... param) {
        try {
            try (DruidPooledConnection connection = ServerDBConnection.INSTANCE.getConnection();
                 PreparedStatement pst = connection.prepareStatement(sql)) {
                for (int i=1;i<=param.length;i++){
                    pst.setString(i,param[i-1]);
                }
                return pst.execute();

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}


