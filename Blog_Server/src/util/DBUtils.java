package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DBUtils {

	{

	}

	static {
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

	public static int update(String sql, Object... paramArray) throws SQLException {
		Connection conn = getConnection();

		PreparedStatement stat = conn.prepareStatement(sql);
		for (int i = 0; i < paramArray.length; i++) {
			stat.setObject(i + 1, paramArray[i]);
			System.out.println("参数" + (i + 1) + ":" + paramArray[i]);
		}
		int count = stat.executeUpdate();
		System.out.println("成功更新了" + count + "条几录");
		conn.close();
		return count;
	}

	public static List<Map<String, Object>> query(String sql, Object... paramArray) throws SQLException {
		Connection conn = getConnection();
		PreparedStatement stat = conn.prepareStatement(sql);
		System.out.println("SQL:" + sql);
		for (int i = 0; i < paramArray.length; i++) {
			stat.setObject(i + 1, paramArray[i]);
			System.out.println("参数" + (i + 1) + ":" + paramArray[i]);
		}
		System.out.println(sql);

		ResultSet rs = stat.executeQuery();
		ResultSetMetaData md = rs.getMetaData();

		int columnCount = md.getColumnCount();

		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		while (rs.next()) {
			Map<String, Object> map = new HashMap<String, Object>();

			for (int i = 1; i <= columnCount; i++) {
				String columnName = md.getColumnName(i);
				Object columnValue = rs.getObject(columnName);
				map.put(columnName, columnValue);
			}
			list.add(map);
		}
		conn.close();
		return list;
	}

	private static Connection getConnection() throws SQLException {
		String url = "jdbc:oracle:thin:@//127.0.0.1:1521/orcl";
		String user = "system";
		String password = "a";
		Connection conn = DriverManager.getConnection(url, user, password);
		return conn;   
		
	}
	public static long count(String sql, Object... paramArray) throws SQLException{
	sql="select count(*) cnt from("+sql+")";
	List<Map<String,Object>> list =query(sql,paramArray);
	String snumber ="" + list.get(0).get("cnt");
	return Long.valueOf(snumber);
	}
	
	public static List<Map<String,Object>> queryByPage(String sql,int page,int rows,Object...paramArray)throws SQLException{
		int begin =(page -1)*rows +1;
		int end =begin+rows-1;
		String pageSql="select * from(select rownum rw,a.*from("+sql+") a"
				+"where rownum <="+end+") where rw>="+begin;
		return query(pageSql,paramArray);
	}
	public static Map<String,Object> selectOne(String sql,Object...paramArray)throws SQLException{
		sql="select rownum,a.*from("+sql+") a where rowmun<=2";
		List<Map<String,Object>> list =query(sql,paramArray);
		if(list.size()==0){Z
			return null;
		}else if(list.size()>1){
			throw new RuntimeException("查询的结果数量大于1！");
		}else{
			return list.get(0);
		}
	}
	public static void main(String[] args) throws SQLException{
		List<Map<String,Object>> list=query(
				"select*from emp where ename like?","%s%");
		for(Map<String,Object>map:list){
			System.out.println(map);
		}
	}
	
}
