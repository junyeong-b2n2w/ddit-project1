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

    public List<Map<String, Object>> selectBrcList() {
		String sql = "SELECT brc_num, brc_name FROM tab_branch";

        return jdbc.selectList(sql);
    }

	public List<Map<String, Object>> selectYearMonth() {
		String sql = "SELECT obrc, pnum, cgr, pname, odate, SUM(cnt) sum\n" + 
				"FROM\n" + 
				"    (SELECT brc. brc_name obrc, prod.prod_num pnum, prod.prod_ctegory cgr, prod.prod_name pname, TO_CHAR((od.od_date), 'yy-mm') odate, od_d.od_count cnt\n" + 
				"     FROM tab_order_detail od_d, tab_product prod, tab_order od, tab_branch brc\n" + 
				"     WHERE od_d.prod_num = prod.prod_num AND od_d.od_num = od.od_num AND brc.brc_num = od.od_brc_num)\n" + 
				"GROUP BY obrc, pnum, cgr, pname, odate\n" + 
				"ORDER BY obrc, pnum, odate";
		
    

        return jdbc.selectList(sql);
	}

	public List<Map<String, Object>> selectDeep(String category, String pname, String startYeatMonth, String endYeatMonth) {
		String sql = "SELECT obrc, pnum, cgr, pname, odate, SUM(cnt) sum\n" + 
				"FROM\n" + 
				"    (SELECT brc. brc_name obrc, prod.prod_num pnum, prod.prod_ctegory cgr, prod.prod_name pname, TO_CHAR((od.od_date), 'yy-mm') odate, od_d.od_count cnt\n" + 
				"     FROM tab_order_detail od_d, tab_product prod, tab_order od, tab_branch brc\n" + 
				"     WHERE od_d.prod_num = prod.prod_num AND od_d.od_num = od.od_num AND brc.brc_num = od.od_brc_num)\n" + 
				"GROUP BY obrc, pnum, cgr, pname, odate\n" + 
				"ORDER BY obrc, pnum, odate";
		
//		if 
//		sql +
		
		ArrayList<Object> param = new ArrayList<>();
        param.add(category);
        param.add(pname);
        param.add(startYeatMonth);
        param.add(endYeatMonth);

        return jdbc.selectList(sql);
	}
    
    

	

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
