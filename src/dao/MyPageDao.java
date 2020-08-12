package dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import util.JDBCUtil;
import controller.Controller;

public class MyPageDao {
	
	private JDBCUtil jdbc = JDBCUtil.getInstance();
	/*- 내정보확인
    	- 내지점정보 수정
    	지점이름
    	주소
    	이메일
    	전화번호
	- 예치금확인
    	- 예치금 추가*/
	private static MyPageDao instance;
	private MyPageDao(){}
	
	public static MyPageDao getInstance() {
		if(instance == null){
			instance = new MyPageDao();
		}
		return instance;
	}
	
	public Map<String, Object> selectMyPage(Object mem_num){
		String sql = "SELECT brc_wh_num, brc_num, brc_name, brc_address, brc_email, brc_phone, brc_credit"
				+ " FROM tab_branch"
				+ " WHERE brc_mem_num = ?";
		
		List<Object> param = new ArrayList<>();
		param.add(mem_num);
		
		return jdbc.selectOne(sql, param);
	}
	
	public int udtBrcInfo(List<Object> param, String col){
		String sql = null;
		
		if(col.equals("BRC_NAME")){
			sql = "UPDATE TAB_BRANCH SET BRC_NAME = ?"
					+ " WHERE BRC_NUM = ?";
		} 
		else if(col.equals("BRC_ADDRESS")){
			sql = "UPDATE TAB_BRANCH SET BRC_ADDRESS = ?"
					+ " WHERE BRC_NUM = ?";
		}
		else if(col.equals("BRC_EMAIL")){
			sql = "UPDATE TAB_BRANCH SET BRC_EMAIL = ?"
					+ " WHERE BRC_NUM = ?";
		}
		else if(col.equals("BRC_PHONE")){
			sql = "UPDATE TAB_BRANCH SET BRC_PHONE = ?"
					+ " WHERE BRC_NUM = ?";
		} else if(col.equals("BRC_CREDIT")){
			sql = "UPDATE TAB_BRANCH SET BRC_CREDIT = ?"
					+ " WHERE BRC_NUM = ?";
		}
		return jdbc.update(sql, param);
	}
	
	
	
}
