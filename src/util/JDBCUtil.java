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

public class JDBCUtil {

	private JDBCUtil() {

	}

	// 인스턴스를 보관할 변수
	private static JDBCUtil instance;

	// 인스턴스를 빌려주는 메소드
	public static JDBCUtil getInstance() {
		if (instance == null) {
			instance = new JDBCUtil();
		}
		return instance;
	}

	// db접속정보
	String url = "jdbc:oracle:thin:@127.0.0.1:1521:XE";
	String user = "jun";
	String password = "java";

	Connection con = null;
	PreparedStatement ps = null;
	ResultSet rs = null;

	/*
	 * Map<String, Object> selectOne(String sql)
	 * Map<String, Object> selectOne(String sql) Map<String, Object>
	 * selectOne(String sql, List<Object> param) List<Map<String, Object>>
	 * selectList(String sql) List<Map<String, Object>> selectList(String sql,
	 * List<Object> param) int update (String sql) int update (String sql,
	 * List<Object> param)
	 */

	public Map<String, Object> selectOne(String sql) {
		
		Map<String, Object> row = null;
		
		try {
		con = DriverManager.getConnection(url, user, password);
		
		ps = con.prepareStatement(sql);
		rs = ps.executeQuery();
		
		ResultSetMetaData md = rs.getMetaData();
		
		int columnCount = md.getColumnCount();
		
		while(rs.next()){
			row = new HashMap<String, Object>();
			for (int i = 1 ; i<= columnCount; i++){
				String key = md.getColumnLabel(i);
				Object value = rs.getObject(i);
				row.put(key, value);
			}
		}
		
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (rs != null)
				try {
					rs.close();
				} catch (Exception e) {
				}
			if (ps != null)
				try {
					ps.close();
				} catch (Exception e) {
				}
			if (con != null)
				try {
					con.close();
				} catch (Exception e) {
				}

		}

		return row;

	}

	public Map<String, Object> selectOne(String sql, List<Object> param) {

		Map<String, Object> row = null;
		
		try {
		con = DriverManager.getConnection(url, user, password);
		
		ps = con.prepareStatement(sql);
		
		for (int i =0 ; i < param.size(); i++){
			ps.setObject(i+1, param.get(i));
		}
		
		rs = ps.executeQuery();
		
		ResultSetMetaData md = rs.getMetaData();
		
		int columnCount = md.getColumnCount();
		
		while(rs.next()){
			row = new HashMap<String, Object>();
			for (int i = 1 ; i<= columnCount; i++){
				String key = md.getColumnLabel(i);
				Object value = rs.getObject(i);
				row.put(key, value);
			}
		}
		
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (rs != null)
				try {
					rs.close();
				} catch (Exception e) {
				}
			if (ps != null)
				try {
					ps.close();
				} catch (Exception e) {
				}
			if (con != null)
				try {
					con.close();
				} catch (Exception e) {
				}

		}

		return row;

	}

	public List<Map<String, Object>> selectList(String sql) {
		
		List<Map<String, Object>> list = new ArrayList<>();
		
		try {
		con = DriverManager.getConnection(url, user, password);
		
		ps = con.prepareStatement(sql);
		
		
		rs = ps.executeQuery();
		
		ResultSetMetaData md = rs.getMetaData();
		
		int columnCount = md.getColumnCount();
		
		while(rs.next()){
			HashMap<String, Object> row = new HashMap<String, Object>();
			for (int i = 1 ; i<= columnCount; i++){
				String key = md.getColumnLabel(i);
				Object value = rs.getObject(i);
				row.put(key, value);
				
			}
		
			list.add(row);
		}
		
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (rs != null)
				try {
					rs.close();
				} catch (Exception e) {
				}
			if (ps != null)
				try {
					ps.close();
				} catch (Exception e) {
				}
			if (con != null)
				try {
					con.close();
				} catch (Exception e) {
				}

		}

		return list;
	}

	public List<Map<String, Object>> selectList(String sql, List<Object> param) {
		List<Map<String, Object>> list = new ArrayList<>();
		
		try {
		con = DriverManager.getConnection(url, user, password);
		
		ps = con.prepareStatement(sql);
		
		for (int i =0 ; i < param.size(); i++){
			ps.setObject(i+1, param.get(i));
		}
		
		rs = ps.executeQuery();
		
		ResultSetMetaData md = rs.getMetaData();
		
		int columnCount = md.getColumnCount();
		
		while(rs.next()){
			HashMap<String, Object> row = new HashMap<String, Object>();
			for (int i = 1 ; i<= columnCount; i++){
				String key = md.getColumnLabel(i);
				Object value = rs.getObject(i);
				row.put(key, value);
			}
			list.add(row);
		}
		
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (rs != null)
				try {
					rs.close();
				} catch (Exception e) {
				}
			if (ps != null)
				try {
					ps.close();
				} catch (Exception e) {
				}
			if (con != null)
				try {
					con.close();
				} catch (Exception e) {
				}

		}

		return list;
	}

	public int update(String sql) {
		
		int result = 0;
		
		try {
		con = DriverManager.getConnection(url, user, password);
		
		ps = con.prepareStatement(sql);
		result = ps.executeUpdate();
		
		
		
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (rs != null)
				try {
					rs.close();
				} catch (Exception e) {
				}
			if (ps != null)
				try {
					ps.close();
				} catch (Exception e) {
				}
			if (con != null)
				try {
					con.close();
				} catch (Exception e) {
				}

		}

		return result;

	}

	public int update(String sql, List<Object> param) {
	int result = 0;
		
		try {
		con = DriverManager.getConnection(url, user, password);
		
		ps = con.prepareStatement(sql);
		
		for (int i =0 ; i < param.size(); i++){
			ps.setObject(i+1, param.get(i));
		}
		
		result = ps.executeUpdate();
		
		
		
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (rs != null)
				try {
					rs.close();
				} catch (Exception e) {
				}
			if (ps != null)
				try {
					ps.close();
				} catch (Exception e) {
				}
			if (con != null)
				try {
					con.close();
				} catch (Exception e) {
				}

		}

		return result;

	}

}
