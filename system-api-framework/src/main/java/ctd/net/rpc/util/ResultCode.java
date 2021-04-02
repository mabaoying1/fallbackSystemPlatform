package ctd.net.rpc.util;
/**
 * 结果返回编码
 * @author caidao
 *
 */
public class ResultCode
{
	/**
	 *请求服务成功 
	 */
	public final static int REQUEST_SUCCESS=100;
	/**
	 * 请求服务传入参数非法
	 */
	public final static int NO_PARAM =101;
	/**
	 * 请求服务传入头部非法
	 */
	public final static int NO_HEAD =102;
	/**
	 * 请求服务授权失败
	 */
	public final static int NO_AUTH =103;
	
	/**
	 * 数据库错误
	 */
	public final static int DATABASE_ERROR=104;
	
	/**
	 * 系统错误
	 */
	public final static int SYSTEM_ERROR = 500;
	
}