package dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import controller.Controller;
import util.JDBCUtil;

public class BoardDao {

	
	//싱글톤 패턴 
	private static BoardDao instance;
	private BoardDao(){}
	
	public static BoardDao getInstance() {
		if(instance == null){
			instance = new BoardDao();
		}
		return instance;
		
	}
	
	//-------------
	
	
	private JDBCUtil jdbc = JDBCUtil.getInstance();


	public List<Map<String, Object>> selectBoardList(){
		String sql = "SELECT board_num, board_name, board_onoff"
				+ " FROM TAB_BOARDLIST ORDER BY board_num DESC";
		
		return jdbc.selectList(sql);
		
	}

	public List<Map<String, Object>> selectBoard(int board_no) {
		
		String sql = "SELECT POST_NUM, POST_TITLE, POST_DATE, POST_WRITER, MEM_NAME, POST_PAR_NUM" +
				" FROM TAB_POST JOIN TAB_MEMBER ON (TAB_POST.POST_WRITER = TAB_MEMBER.MEM_NUM)" +
				" WHERE BOARD_NUM = ?" +
				" AND POST_DEL = 1" +
				" START WITH POST_PAR_NUM IS NULL" +
				" CONNECT BY PRIOR POST_NUM = POST_PAR_NUM" +
				" ORDER SIBLINGS BY POST_NUM DESC";


		ArrayList<Object> param = new ArrayList<>();
		param.add(board_no);
		
		
		
		return jdbc.selectList(sql, param);
	}


	public Map<String, Object> postSelect(int selectedPostNo){


		String sql = "SELECT POST_NUM, POST_TITLE, POST_CONTENT, POST_DATE, POST_VIEW, POST_WRITER, POST_DEL, POST_PAR_NUM, MEM_NAME" +
				" FROM TAB_POST JOIN TAB_MEMBER ON (TAB_POST.POST_WRITER = TAB_MEMBER.MEM_NUM)" +
				" WHERE POST_NUM = ?";
		ArrayList<Object> param = new ArrayList<>();
		param.add(selectedPostNo);


		return jdbc.selectOne(sql, param);
	}

	public int postUpdate(ArrayList<Object> param) {

		String sql = "UPDATE TAB_POST SET POST_TITLE = ? , POST_CONTENT = ? WHERE POST_NUM = ?";
		
		return jdbc.update(sql, param);
		
		
		
	}

	public int postDelete(ArrayList<Object> param) {

		String sql = "UPDATE TAB_POST SET POST_DEL = 2 WHERE POST_NUM = ?";
		
		
		return jdbc.update(sql,param);
	}
	
	
	public int postInsert(ArrayList<Object> param){
		String sql = "INSERT INTO TAB_POST VALUES( (SELECT NVL(MAX(POST_NUM),0) +1 FROM TAB_POST) , ?, ?, ? ,sysdate , 0 , 1,?,? )";
				
		return jdbc.update(sql, param);
	}



	public int createBoardList(String name){
		String sql = "INSERT INTO TAB_BOARDLIST (BOARD_NUM, BOARD_NAME, BOARD_DATE, BOARD_ONOFF, BOARD_CREATE_NUM)" +
				" VALUES ((SELECT NVL(MAX(BOARD_NUM),0)+1 FROM TAB_BOARDLIST), ? , SYSDATE, 1, ?)";
		ArrayList<Object> param = new ArrayList<>();
		param.add(name);
		param.add(Controller.loginUser.get("MEM_NUM"));



		return jdbc.update(sql, param);
	}


	public int commentAdd(ArrayList<Object> param) {

		String sql = "INSERT INTO TAB_COMMENT VALUES ((SELECT NVL(MAX(COM_NUM),0)+1 FROM TAB_COMMENT), ?, ?, ?, sysdate, 1, ?)";


		return jdbc.update(sql, param);
	}
	public int commentEdit(ArrayList<Object> param) {

		String sql = "UPDATE TAB_COMMENT SET COM_CONTENT = ?, COM_DATE = sysdate WHERE COM_NUM = ? ";


		return jdbc.update(sql, param);
	}

	public List<Map<String, Object>> commentList(int selectedPostNo) {

		String sql = "SELECT COM_NUM, COM_WRITER, COM_CONTENT , COM_DATE , MEM_NAME, COM_PAR_NUM" +
				" FROM TAB_COMMENT JOIN TAB_MEMBER ON (TAB_COMMENT.COM_WRITER = TAB_MEMBER.MEM_NUM)" +
				" WHERE COM_DEL = 1" +
				" AND COM_POST_NUM = ?" +
				" START WITH COM_PAR_NUM IS NULL" +
				" CONNECT BY PRIOR COM_NUM = COM_PAR_NUM";

		ArrayList<Object> param = new ArrayList<>();
		param.add(selectedPostNo);

		return jdbc.selectList(sql, param);
	}

	public int commentDelete(ArrayList<Object> param) {
		String sql = "UPDATE TAB_COMMENT SET COM_DEL = 2 WHERE COM_NUM = ?";


		return jdbc.update(sql,param);
	}

	public Map<String, Object> commentWriter(int writer_num){
		String sql = "SELECT COM_WRITER FROM TAB_COMMENT WHERE COM_NUM = ?";
		ArrayList<Object> param = new ArrayList<>();
		param.add(writer_num);

		return jdbc.selectOne(sql, param);
	}

}
