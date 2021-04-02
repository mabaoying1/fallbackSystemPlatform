package ctd.net.rpc.util;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
/**
 * @ClassName SystemDateUtil
 * @Description: TODO
 * @Author caidao
 * @Date 2021/3/23
 * @Version V1.0
 **/
@Component
public class SystemDateSQLUtil {

    @Value("${spring.his.datasource.driverClassName}")
    private String driverClassName;

    /**
     * 获取日期yyyy-mm-dd</br>
     * @return
     */
    public Object getSystemDate(){
        if(driverClassName.indexOf("sqlserver") != -1){
            //说明是sqlserver 数据库
            return "CONVERT(varchar(100), GETDATE(), 23)";
        }else if(driverClassName.indexOf("OracleDriver") != -1){
            //说明是Oracle 数据库
            return "to_char(SYSDATE,'yyyy-mm-dd')";
        }
        return -1;
    }

    /**
     * 获取日期yyyy-mm-dd</br>
     * @return
     */
    public Object getSystemDateTime(){
        if(driverClassName.indexOf("sqlserver") != -1){
            //说明是sqlserver 数据库
            return "CONVERT(varchar(100), GETDATE(), 120)";
        }else if(driverClassName.indexOf("OracleDriver") != -1){
            //说明是Oracle 数据库
            return "SYSDATE";
        }
        return -1;
    }
}
