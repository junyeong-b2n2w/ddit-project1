package dao;

import java.util.ArrayList;
import java.util.Map;

import util.JDBCUtil;

public class CartDao {

	
	//싱글톤 패턴 
	private static CartDao instance;
	private CartDao(){}
	
	public static CartDao getInstance() {
		if(instance == null){
			instance = new CartDao();
		}
		return instance;
		
	}
	
	private JDBCUtil jdbc = JDBCUtil.getInstance();
	
	public Map<String, Object> selectProd(int selectedItem) {
		
		String sql = "SELECT PROD_NUM, PROD_NAME, PROD_PRICE"
				+ " FROM tab_product"
				+ " WHERE prod_num = ?";
		
		ArrayList<Object> param = new ArrayList<>();
		param.add(selectedItem);
		
		
		return jdbc.selectOne(sql,param);
	}
	
}