package dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import util.JDBCUtil;

public class GraphDao {
	
	//싱글톤 패턴
    private static GraphDao instance;

    private GraphDao() {
    }

    public static GraphDao getInstance() {
        if (instance == null) {
            instance = new GraphDao();
        }
        return instance;

    }

    //-------------

    private JDBCUtil jdbc = JDBCUtil.getInstance();



	public List<Map<String, Object>> searchByProd(String prod, String branch, String cate, String startDate, String endDate, String OrderBy) {

    	String sql = "SELECT TAB_ORDER_DETAIL.PROD_NUM, SUM(OD_COUNT) COUNT," +
				"  (SELECT prod_NAME FROM TAB_PRODUCT WHERE PROD_NUM = tab_ORDER_DETAIL.PROD_NUM) PROD_NAME," +
				"  (SELECT prod_price FROM TAB_PRODUCT WHERE PROD_NUM = tab_ORDER_DETAIL.PROD_NUM) PROD_PRICE" +
				" FROM TAB_ORDER_DETAIL JOIN TAB_ORDER ON (TAB_ORDER_DETAIL.OD_NUM = TAB_ORDER.OD_NUM) JOIN TAB_PRODUCT ON (TAB_ORDER_DETAIL.PROD_NUM = TAB_PRODUCT.PROD_NUM)" +
				" WHERE 1=1";

		ArrayList<Object> param = new ArrayList<>();

		if(!prod.equals("0")){
			sql = sql + " AND prod_NAME LIKE '%'||?||'%'";
			param.add(prod);
		}

		if(!branch.equals("0")){
			sql = sql + " AND tab_order.OD_BRC_NUM = ?";
			param.add(branch);
		}

		if(!cate.equals("0")){
			sql = sql + " AND prod_ctegory LIKE '%'||?||'%'";
			param.add(cate.equals("1") ? "식료품" : "부가기재");
		}


		if(!startDate.equals("0")){
			sql = sql + " AND OD_DATE >= TO_DATE(? , 'yymmdd')";
			param.add(startDate);
		}
		if(!endDate.equals("0")){
			sql = sql + " AND OD_DATE <= TO_DATE(? , 'yymmdd')";
			param.add(endDate);
		}



    	sql = sql + " GROUP BY TAB_ORDER_DETAIL.PROD_NUM";
		sql = OrderBy.equals("1") ? sql + " ORDER BY count DESC" : sql + " ORDER BY count";

    	return jdbc.selectList(sql, param);
	}



}
