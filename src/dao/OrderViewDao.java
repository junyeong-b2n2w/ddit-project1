package dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import service.OrderViewService;
import util.JDBCUtil;

public class OrderViewDao {
	private static OrderViewDao instance;
	private OrderViewDao(){}
	
	public static OrderViewDao getInstance() {
		if(instance == null){
			instance = new OrderViewDao();
		}
		return instance;
	}

	private JDBCUtil jdbc = JDBCUtil.getInstance();
	
	public List<Map<String, Object>> orderList(Object brc_num, Object serchMonth){
		String sql = "SELECT od_dt.OD_NUM, prod.PROD_NAME, od_dt.od_count, prod.prod_price, od.OD_DATE"
				+ " FROM tab_branch brc, tab_order od, tab_order_detail od_dt, tab_product prod"
				+ " WHERE brc.brc_num = od.od_brc_num AND od.od_num = od_dt.od_num AND od_dt.prod_num = prod.prod_num"
				+ " AND brc.brc_num = ? AND od.od_date >= SYSDATE - ?"
				+ " ORDER BY od_dt.OD_NUM DESC ";
		
		List<Object> param = new ArrayList<>();
		param.add(brc_num);
		param.add(serchMonth);
		
		return jdbc.selectList(sql, param);
	}
	
}
