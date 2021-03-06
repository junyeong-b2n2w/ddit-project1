package dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import controller.Controller;
import service.DeliveryService;
import util.JDBCUtil;

public class DeliveryDao {
	private static DeliveryDao instance;
	private JDBCUtil jdbc = JDBCUtil.getInstance();

	public static DeliveryDao getInstance() {
		if(instance == null){
			instance = new DeliveryDao();
		}
		return instance;
	}
	
	
	public int insertOrder(Object brcNum){
		
		String sql = "INSERT INTO tab_delivery VALUES ((SELECT NVL(MAX(dv_num), 0) +1  FROM tab_delivery), "
				+" (SELECT MAX(od_num) FROM tab_order, tab_branch WHERE tab_branch.brc_num = tab_order.od_brc_num AND od_brc_num = ?), "
				+" SYSDATE +1, '배송 준비 중', "
				+" (SELECT brc_wh_num FROM tab_branch WHERE brc_num = ?))";
				
		
		
		List<Object> param = new ArrayList<>();
		param.add(brcNum);
		param.add(brcNum);
		
		return jdbc.update(sql, param);
	}

	public Map<String, Object> deliveryView(Object od_Num){
		String sql = "SELECT dv_num, dv_status, tab_delivery.OD_NUM, OD_DATE" +
				" FROM tab_delivery , tab_order" +
				" WHERE TAB_DELIVERY.OD_NUM = tab_order.od_num" +
				" AND tab_delivery.od_num = ?" +
				" ORDER BY tab_delivery.dv_num desc";;
		
		List<Object> param = new ArrayList<>();
		param.add(od_Num);
		
		return jdbc.selectOne(sql, param);
	}
	
	public List<Map<String, Object>> deliveryViewAdmin(){
//		주문번호\t지점번호\t지점이름\t주문 날짜\t배송 번호\t배송 상태
		String sql = "SELECT od.od_num, brc.brc_num, brc.brc_name, od.od_date, dv.dv_num, dv.dv_status"
				+ " FROM tab_delivery dv, tab_branch brc, tab_order od"
				+ " WHERE brc.brc_num = od.od_brc_num AND od.od_num = dv.od_num"
				+ " AND dv_status != '배송 완료'"
				+ " ORDER BY dv.dv_num desc";
		
		return jdbc.selectList(sql);
	}

	public List<Map<String, Object>> deliveryViewBranch(int brc_num){
//		주문번호\t지점번호\t지점이름\t주문 날짜\t배송 번호\t배송 상태
		String sql = "SELECT od.od_num, brc.brc_num, brc.brc_name, od.od_date, dv.dv_num, dv.dv_status"
				+ " FROM tab_delivery dv, tab_branch brc, tab_order od"
				+ " WHERE brc.brc_num = od.od_brc_num AND od.od_num = dv.od_num"
				+ " AND BRC_NUM = ?"
				+ " ORDER BY dv.dv_num desc";
		List<Object> param = new ArrayList<>();
		param.add(brc_num);
		return jdbc.selectList(sql, param);
	}

	public List<Map<String, Object>> deliveryViewAllAdmin(){
		String sql = "SELECT od.od_num, brc.brc_num, brc.brc_name, od.od_date, dv.dv_num, dv.dv_status "
				+ " FROM tab_delivery dv, tab_branch brc, tab_order od"
				+ " WHERE brc.brc_num = od.od_brc_num AND od.od_num = dv.od_num"
				+ " ORDER BY dv.dv_num desc";
		
		return jdbc.selectList(sql);
	}
	
	public int dvStatusUpdate(String dv_status,int dv_num){
		String sql = "UPDATE tab_delivery SET dv_status = ? WHERE dv_num = ?";
		
		List<Object> param = new ArrayList<>();
		param.add(dv_status);
		param.add(dv_num);
		
		return jdbc.update(sql, param);
	}
}
