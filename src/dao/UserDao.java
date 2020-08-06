package dao;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import util.JDBCUtil;


public class UserDao {

	//싱글톤 패턴 
	private static UserDao instance;
	private UserDao(){}
	
	public static UserDao getInstance() {
		if(instance == null){
			instance = new UserDao();
		}
		return instance;
		
	}
	
	//-------------
	
	private JDBCUtil jdbc = JDBCUtil.getInstance();
	
	public int insertUser(Map<String , Object> p){
		
		String sql = "INSERT INTO TAB_MEMBER (MEM_NUM, MEM_PERMISSION, MEM_NAME, MEM_ID, MEM_PASSWORD, MEM_REGDATE)" +
				" VALUES ((SELECT NVL(MAX(MEM_NUM),0)+1 FROM TAB_MEMBER), 2,?,?,?, SYSDATE)";
		
		List<Object> param = new ArrayList<>();
		param.add(p.get("MEM_NAME"));
		param.add(p.get("MEM_ID"));
		param.add(p.get("MEM_PASSWORD"));

		
		return jdbc.update(sql, param);	
	}

	public Map<String, Object> selectUser(String userId, String password) {

		String sql = "SELECT MEM_NUM, MEM_NAME, MEM_ID, MEM_PASSWORD, MEM_PERMISSION FROM TAB_MEMBER"
				+ " WHERE MEM_ID = ?"
				+ " AND MEM_PASSWORD = ?";
		
		List<Object> param = new ArrayList<>();
		param.add(userId);
		param.add(password);
		
		
		
		return jdbc.selectOne(sql, param);
	}

	public Map<String, Object> isUserExist(String userId) {

		String sql = "SELECT MIN(MEM_ID) MEM_ID FROM TAB_MEMBER"
				+ " WHERE MEM_ID = ?";

		List<Object> param = new ArrayList<>();
		param.add(userId);



		return jdbc.selectOne(sql, param);
	}
	
	
	
	
}
