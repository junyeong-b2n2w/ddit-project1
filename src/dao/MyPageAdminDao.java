package dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import controller.Controller;
import util.JDBCUtil;

public class MyPageAdminDao {

    //싱글톤 패턴
    private static MyPageAdminDao instance;

    private MyPageAdminDao() {
    }

    public static MyPageAdminDao getInstance() {
        if (instance == null) {
            instance = new MyPageAdminDao();
        }
        return instance;

    }

    //-------------

    private JDBCUtil jdbc = JDBCUtil.getInstance();

    //멤버리스트 조회

    public List<Map<String, Object>> selectMemberList() {
        String sql = "SELECT mem_name, mem_id, mem_regdate, mem_permission"
                + " FROM tab_member";

        return jdbc.selectList(sql);
    }

    public int updateMemberPermission(Object insertPermission, String memId) {
        String sql = "UPDATE tab_member"
                + " SET mem_permission = ?"
                + " WHERE mem_id = ?";

        ArrayList<Object> param = new ArrayList<>();
        param.add(insertPermission);
        param.add(memId);

        return jdbc.update(sql, param);
    }

    public List<Map<String, Object>> selectBrcList() {
        String sql = "SELECT brc_num, brc_name"
                + " FROM tab_branch"
                + " WHERE brc_mem_num > 0";

        return jdbc.selectList(sql);
    }

    public List<Map<String, Object>> selectBrcInfo(int selectBranchNum) {
        String sql = "SELECT b.brc_num, b.brc_name, m.mem_name, b.brc_address, b.brc_email, b.brc_phone, b.brc_credit"
                + " FROM tab_branch b, tab_member m"
                + " WHERE b.brc_mem_num(+) = m.mem_num AND b.brc_num = ?";

        ArrayList<Object> param = new ArrayList<>();
        param.add(selectBranchNum);

        return jdbc.selectList(sql, param);
    }

    public int insertBrcInfo(String insertBranchName, String insertBranchAddress, String insertBranchEmail, String insertBranchPhoneNum, Object a) {
        String sql = "INSERT INTO tab_branch VALUES ((SELECT NVL(MAX(BRC_NUM),0) +1 FROM TAB_BRANCH), ?, ?, ?, ?, 0, ?,1)";

        ArrayList<Object> param = new ArrayList<>();
        param.add(insertBranchName);
        param.add(insertBranchAddress);
        param.add(insertBranchEmail);
        param.add(insertBranchPhoneNum);
        param.add(a);

        return jdbc.update(sql, param);

    }

    public int deleteBrcInfo(int deleteBranchNum) {
        String sql = "DELETE"
                + " FROM tab_branch"
                + " WHERE brc_num = ?";

        ArrayList<Object> param = new ArrayList<>();
        param.add(deleteBranchNum);

        return jdbc.update(sql, param);

    }

    public int updateBrcInfo(String updateBranchName, String updateBranchAddress, String updateBranchEmail, String updateBrachPhoneNum, int BranchNum) {
        String sql = "UPDATE tab_branch"
                + " SET brc_name=?, brc_address=?, brc_email=?, brc_phone=?"
                + " WHERE brc_num=?";

        ArrayList<Object> param = new ArrayList<>();
        param.add(updateBranchName);
        param.add(updateBranchAddress);
        param.add(updateBranchEmail);
        param.add(updateBrachPhoneNum);
        param.add(BranchNum);

        return jdbc.update(sql, param);

    }

    public List<Map<String, Object>> selectWhList() {
        String sql = "SELECT WH_NUM, WH_ADRESS, WH_USE"
                + " FROM tab_warehouse";

        return jdbc.selectList(sql);
    }

    public int insertWhInfo(int insertWhNum, String insertWhAddress, int insertWhUse) {

        String sql = "INSERT INTO tab_warehouse values ((SELECT NVL(MAX(wh_num), 0)+1 FROM tab_warehouse), ?, ?)";
        ArrayList<Object> param = new ArrayList<>();
        param.add(insertWhAddress);
        param.add(insertWhUse);

        return jdbc.update(sql, param);

    }

    public int deleteWhInfo(int deleteWhNum) {

        String sql = "DELETE FROM tab_warehouse"
                + " WHERE wh_num = ?";

        ArrayList<Object> param = new ArrayList<>();
        param.add(deleteWhNum);

        return jdbc.update(sql, param);

    }

    public int updateWhInfo(int updateBeforeWhnum, int updateWhNum, String updateWhAddress, int updateWhUse) {
        String sql = "UPDATE tab_warehouse"
                + " SET wh_num=?, wh_adress=?, wh_use=?"
                + " WHERE wh_num=?";

        ArrayList<Object> param = new ArrayList<>();
        param.add(updateWhNum);
        param.add(updateWhAddress);
        param.add(updateWhUse);
        param.add(updateBeforeWhnum);

        return jdbc.update(sql, param);

    }

    public List<Map<String, Object>> selectProdList(String name, int cate) {
        String sql = "SELECT * FROM tab_product WHERE 1=1";

        ArrayList<Object> param = new ArrayList<>();

        if(!name.equals("0")){
            sql = sql+ " AND PROD_NAME LIKE '%'||?||'%'";
            param.add(name);
        }

        if(cate == 1){	param.add("식료품");}
        else if (cate == 2) {param.add("부가기재");}



        sql = cate != 0 ? sql + " AND prod_ctegory = ?" : sql + " AND prod_ctegory IS NOT NULL";

        sql = sql + " ORDER BY prod_num";
        return jdbc.selectList(sql, param);
    }

    public int insertProdInfo(int insertProdNum, String insertProdCategory, String insertProdName, String insertProdText, int insertPridPrice) {
        String sql = "INSERT INTO tab_product VALUES ((SELECT NVL(MAX(prod_num), 0)+1 FROM tab_product), ?, ?, ?, ?)";

        ArrayList<Object> param = new ArrayList<>();
        param.add(insertProdCategory);
        param.add(insertProdName);
        param.add(insertProdText);
        param.add(insertPridPrice);

        return jdbc.update(sql, param);

    }

    public int deleteProd(int deleteProdNum) {
        String sql = "DELETE FROM tab_product WHERE prod_num =?";

        ArrayList<Object> param = new ArrayList<>();
        param.add(deleteProdNum);

        return jdbc.update(sql, param);
    }

    public List<Map<String, Object>> selectWhStock(int selectWhNum) {
        String sql = "SELECT w.wh_num, w.prod_num, p.prod_name, w.stock_count"
                + " FROM tab_wh_stock w, tab_product p"
                + " WHERE w.prod_num = p.prod_num AND wh_num = ?";

        ArrayList<Object> param = new ArrayList<>();
        param.add(selectWhNum);

        return jdbc.selectList(sql, param);
    }

    public int insertStock(int insertWhNum, int insertProdNum, int insertStockAddCount) {
        String sql = "INSERT INTO tab_wh_stock VALUES (?, ?, ?)";

        ArrayList<Object> param = new ArrayList<>();
        param.add(insertWhNum);
        param.add(insertProdNum);
        param.add(insertStockAddCount);

        return jdbc.update(sql, param);
    }

    public int deleteStock(int deleteStockWhNum, int deletProdNum) {
        String sql = "DELETE FROM tab_wh_stock WHERE wh_num = ? AND prod_num = ?";

        ArrayList<Object> param = new ArrayList<>();
        param.add(deleteStockWhNum);
        param.add(deletProdNum);

        return jdbc.update(sql, param);
    }

    //재고 업데이트(수량+)
    public int updateStock(int insertWhNum, int insertProdNum, int insertStockAddCount) {
        String sql = "UPDATE tab_wh_stock SET STOCK_COUNT = (SELECT stock_count FROM tab_wh_stock WHERE wh_num = ? AND prod_num = ?) + ? WHERE wh_num = ? AND prod_num = ?";

        ArrayList<Object> param = new ArrayList<>();

        param.add(insertWhNum);
        param.add(insertProdNum);
        param.add(insertStockAddCount);
        param.add(insertWhNum);
        param.add(insertProdNum);


        return jdbc.update(sql, param);
    }

    //재고 검색
    public Map<String, Object> selectStock(int insertWhNum, int insertProdNum) {
        String sql = "SELECT COUNT(STOCK_COUNT) COUNT FROM tab_wh_stock WHERE wh_num = ? AND prod_num = ?";

        ArrayList<Object> param = new ArrayList<>();
        param.add(insertWhNum);
        param.add(insertProdNum);

        return jdbc.selectOne(sql, param);
    }


}
