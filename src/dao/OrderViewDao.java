
package dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import service.OrderViewService;
import util.JDBCUtil;

	public class OrderViewDao {
		private static dao.OrderViewDao instance;
		private OrderViewDao(){}

		public static dao.OrderViewDao getInstance() {
			if(instance == null){
				instance = new dao.OrderViewDao();
			}
			return instance;
		}

		private JDBCUtil jdbc = JDBCUtil.getInstance();

		public List<Map<String, Object>> orderList(Object brc_num, Object searchMonth){
			String sql = "SELECT od_dt.OD_NUM, prod.PROD_NAME, od_dt.od_count, prod.prod_price, od.OD_DATE"
					+ " FROM tab_branch brc, tab_order od, tab_order_detail od_dt, tab_product prod"
					+ " WHERE brc.brc_num = od.od_brc_num AND od.od_num = od_dt.od_num AND od_dt.prod_num = prod.prod_num"
					+ " AND brc.brc_num = ? AND od.od_date >= SYSDATE - ?"
					+ " ORDER BY od_dt.OD_NUM DESC";

			List<Object> param = new ArrayList<>();
			param.add(brc_num);
			param.add(searchMonth);

			return jdbc.selectList(sql, param);
		}

		public List<Map<String, Object>> dvViewAdmin(int dvNum){
			String sql = "SELECT od.od_num, brc.brc_num, dv.dv_num, od.od_date, brc.brc_wh_num, dv.dv_status, prod.prod_num, prod.prod_name, prod.prod_price, od_dt.od_count"
					+ " FROM tab_order od, tab_branch brc, tab_delivery dv, tab_product prod, tab_order_detail od_dt"
					+ " WHERE brc.brc_num = od.od_brc_num AND od.od_num = dv.od_num AND od.od_num = od_dt.od_num AND od_dt.prod_num = prod.prod_num"
					+ " AND dv.dv_num = ?";

			List<Object> param = new ArrayList<>();
			param.add(dvNum);

			return jdbc.selectList(sql, param);
		}

		public Map<String, Object> prodView(int select){
			String sql = "SELECT prod_num, prod_ctegory, prod_name, prod_text, prod_price"
					+ " FROM tab_product"
					+ " WHERE prod_num = ?";

			List<Object> param = new ArrayList<>();
			param.add(select);

			return jdbc.selectOne(sql, param);
		}

		public Map<String, Object> dvProdView(int dvNum){
//		주문번호\t지점번호\t지점이름\t주문 날짜\t\t\t배송 번호\t배송 상태
			String sql = "SELECT od.od_num, brc.brc_num, brc.brc_name, od.od_date, dv.dv_num, dv.dv_status"
					+ " FROM tab_delivery dv, tab_branch brc, tab_order od"
					+ " WHERE brc.brc_num = od.od_brc_num AND od.od_num = dv.od_num"
					+ " AND dv.dv_num = ?";

			List<Object> param = new ArrayList<>();
			param.add(dvNum);

			return jdbc.selectOne(sql, param);
		}

//	public List<Map<String, Object>> selectBrcNo(int brcNum){
////		배송 번호\t주문번호\t지점번호\t주문 날짜\t\t\t창고 번호\t배송 상태
//		String sql = "SELECT od.od_num, brc.brc_num, brc.brc_name, od.od_date, dv.dv_num, dv.dv_status"
//					+ " FROM tab_delivery dv, tab_branch brc, tab_order od"
//					+ " WHERE brc.brc_num = od.od_brc_num AND od.od_num = dv.od_num"
//					+ " AND brc.brc_num = ?";
//
//		List<Object> param = new ArrayList<>();
//		param.add(brcNum);
//
//		return jdbc.selectList(sql, param);
//	}

		public List<Map<String, Object>> selectBrcNo(int select) {
//		배송 번호\t주문번호\t지점번호\t주문 날짜\t\t\t창고 번호\t배송 상태
			String sql = "SELECT od.od_num, brc.brc_num, brc.brc_name, od.od_date, dv.dv_num, dv.dv_status"
					+ " FROM tab_delivery dv, tab_branch brc, tab_order od"
					+ " WHERE brc.brc_num = od.od_brc_num AND od.od_num = dv.od_num"
					+ " AND brc.brc_num = ?";

			List<Object> param = new ArrayList<>();
			param.add(select);

			return jdbc.selectList(sql, param);
		}



	public List<Map<String, Object>> orderListAdmin(){
		String sql = "SELECT od_dt.OD_NUM, prod.PROD_NAME, od_dt.od_count, prod.prod_price, od.OD_DATE"
				+ " FROM tab_branch brc, tab_order od, tab_order_detail od_dt, tab_product prod"
				+ " WHERE brc.brc_num = od.od_brc_num AND od.od_num = od_dt.od_num AND od_dt.prod_num = prod.prod_num"
				+ " ORDER BY od_dt.OD_NUM DESC";

		return jdbc.selectList(sql);
	}
}
