package dao;

import java.util.ArrayList;
import java.util.List;
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
	

	public int cartOrderProc(Object brc_num) {
		
		String sql = "INSERT INTO TAB_ORDER VALUES ((SELECT NVL(MAX(OD_NUM),0) +1 FROM TAB_ORDER), ? ,sysdate)";
		
		ArrayList<Object> param = new ArrayList<>();
		param.add(brc_num);
		
		return jdbc.update(sql, param);
	}
	
	public Map<String, Object> currentOrderNum (){
	
		String sql = "SELECT NVL(MAX(OD_NUM),0) OD_NUM FROM TAB_ORDER";
	
	return jdbc.selectOne(sql);
	}

	public List<Map<String, Object>>  searchStock(Object brc_num) {
		String sql = "SELECT PROD_NUM FROM TAB_WH_STOCK "
				+ " WHERE WH_NUM = (SELECT BRC_WH_NUM FROM TAB_BRANCH WHERE BRC_NUM = ?)";
		
		ArrayList<Object> param = new ArrayList<>();
		param.add(brc_num);
		
		return jdbc.selectList(sql,param);
	}
	
	
	
}