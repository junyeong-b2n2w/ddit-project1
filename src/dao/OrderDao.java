package dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import util.JDBCUtil;

public class OrderDao {
 

		//싱글톤 패턴 
		private static OrderDao instance;
		private OrderDao(){}
		
		public static OrderDao getInstance() {
			if(instance == null){
				instance = new OrderDao();
			}
			return instance;
			
		
	

		}

		private JDBCUtil jdbc = JDBCUtil.getInstance();
		
		public List<Map<String, Object>> orderSearchItem(int cate, String search , Object brc_num) {
			// TODO Auto-generated method stub
			
			String sql = "SELECT tab_wh_stock.WH_NUM, tab_wh_stock.PROD_NUM, tab_product.prod_ctegory,"
					+ " tab_product.prod_name, tab_product.prod_text, tab_product.prod_price, tab_wh_stock.stock_count "
					+ " FROM (tab_wh_stock JOIN tab_warehouse ON (tab_wh_stock.WH_NUM = tab_warehouse.WH_NUM) )"
					+ " JOIN tab_product ON (tab_wh_stock.PROD_NUM = tab_product.PROD_NUM)" ;
					
//			String sql = "SELECT a.PROD_NUM, b.prod_ctegory,"
//					+ " b.prod_name, b.prod_text, b.prod_price , count "
//					+ " FROM"
//					+ " (SELECT tab_product.PROD_NUM, SUM(stock_count) count"
//					+ " FROM tab_product JOIN tab_wh_stock ON (tab_wh_stock.PROD_NUM = tab_product.prod_num)"
//					+ " GROUP BY tab_product.prod_num) a JOIN tab_product b ON (a.PROD_NUM = b.prod_num)";
			
			ArrayList<Object> param = new ArrayList<>();
			
			if(cate == 1){	param.add("식료품");}
			else if (cate == 2) {param.add("부가기재");}
			
			
			
			sql = cate != 0 ? sql + " WHERE tab_product.prod_ctegory = ?" : sql + " WHERE tab_product.prod_ctegory IS NOT NULL";
//			sql = cate != 0 ? sql + " WHERE b.prod_ctegory = ?" : sql + " WHERE b.prod_ctegory IS NOT NULL";
			
			if (!search.equals("0")){
				sql = sql + " AND tab_product.prod_NAME LIKE '%'||?||'%'";
				param.add(search);
			}
//			if (!search.equals("0")){
//				sql = sql + " AND b.prod_NAME LIKE '%'||?||'%'";
//				param.add(search);
//			}
			
			sql = sql + " AND tab_wh_stock.WH_NUM = ? ORDER BY tab_wh_stock.prod_num";
			Object wh_num = checkCredit(brc_num).get("BRC_WH_NUM");
		
			
			param.add(wh_num);
			
			return jdbc.selectList(sql, param);
		}

		
		
		public int cartToOrder(Object order_num ,Map<String, Object> cartItem){
			
			String sql = "INSERT INTO TAB_ORDER_DETAIL VALUES(? , ? ,?)";
			
			ArrayList<Object> param = new ArrayList<>();
			param.add(order_num);
			param.add(cartItem.get("PROD_NUM"));
			param.add(cartItem.get("CART_QUNTITY"));
			
			return jdbc.update(sql, param);
		}

		public Map<String, Object> checkCredit(Object brc_num) {
			
			String sql = "SELECT BRC_CREDIT, BRC_WH_NUM FROM TAB_BRANCH WHERE BRC_NUM = ?";
			
			ArrayList<Object> param = new ArrayList<>();
			param.add(brc_num);
			
			return jdbc.selectOne(sql,param);
		}

		public int useCredit(int remain, Object brc_num) {
			String sql = "UPDATE TAB_BRANCH SET BRC_CREDIT =  ? WHERE BRC_NUM = ?";
			ArrayList<Object> param = new ArrayList<>();
			param.add(remain);
			param.add(brc_num);
			
			return jdbc.update(sql, param);
		}

		public int outStock(Map<String, Object> cartItem, Object brc_num) {
			
			String sql = "UPDATE TAB_WH_STOCK SET STOCK_COUNT = "
					+ " (SELECT STOCK_COUNT FROM TAB_WH_STOCK WHERE WH_NUM = ? AND PROD_NUM = ?) - ?"
					+ " WHERE WH_NUM =? AND PROD_NUM = ? ";
			
			ArrayList<Object> param = new ArrayList<>();
			param.add(checkCredit(brc_num).get("BRC_WH_NUM"));
			param.add(cartItem.get("PROD_NUM"));
			param.add(cartItem.get("CART_QUNTITY"));
			param.add(checkCredit(brc_num).get("BRC_WH_NUM"));
			param.add(cartItem.get("PROD_NUM"));
			
			
			return jdbc.update(sql,param);
		}
		
		
}

