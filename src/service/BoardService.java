package service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import controller.Controller;
import dao.BoardDao;
import util.ScanUtil;
import util.View;

public class BoardService {

	
	//싱글톤 패턴 
	private static BoardService instance;
	private BoardService(){}
	
	public static BoardService  getInstance() {
		if(instance == null){
			instance = new BoardService();
		}
		return instance;
		
	}
	
	//-------------
	
	
	private BoardDao boardDao = BoardDao.getInstance();
	public static int selectedBoardNo = 0;
	public static int selectedPostNo = 0;
	
	public int boardList(){
		
		List<Map<String , Object>> boardList = boardDao.selectBoardList();
		

		System.out.println("======================================");
		System.out.println("번호\t게시판이름");
		System.out.println("--------------------------------------");
		
		for(Map<String, Object> board : boardList){
			System.out.println(board.get("BOARD_NUM") + "\t"
					+ board.get("BOARD_NAME"));
		}
		System.out.println("======================================");
		System.out.println("1.게시판 선택\t2.---\t3.주문테스트\t0.로그아웃");
		System.out.print("입력 > ");
		
		int input = ScanUtil.nextInt();
		
		switch (input) {
		case 1:
			System.out.println("게시판 번호 입력>");
			selectedBoardNo = ScanUtil.nextInt();
			return View.BOARD_VIEW;

		case 2: return View.HOME;
		case 3: return View.ORDER_MAIN_VIEW;
		case 0:
			Controller.loginUser = null;
			return View.HOME;
			
		}
		
		return View.BOARD_LIST;
	}

	
	
	public int boardView(int board_num) {
		
		List<Map<String, Object>> boardArticle = boardDao.selectBoard(board_num);
		
		System.out.println("======================================");
		System.out.println("번호\t 제목\t 작성자\t작성일");
		for(Map<String, Object> post : boardArticle){
			System.out.print(post.get("POST_PAR_NUM") == null ? "" : "▷" );
			System.out.println(post.get("POST_NUM") + "\t"
					+ post.get("POST_TITLE") + "\t"
					+ post.get("MEM_NAME") + "\t"
					+ post.get("POST_DATE"));
		}
		System.out.println("======================================");
		System.out.println("1.조회\t2.글작성\t0.게시판목록 보기");
		System.out.print("입력 > ");
		
		
		
		int input = ScanUtil.nextInt();
		
		ArrayList<Object> param = new ArrayList<>();
		
		int result =0;
		switch (input) {
		case 1:
			System.out.println("조회하고싶은 게시글 번호를 입력해 주세요");
			System.out.print("번호 >");
			selectedPostNo = ScanUtil.nextInt();

			return postView(selectedPostNo);
		case 2: 
			postInsert();
			return View.BOARD_VIEW;
		case 0:
			return View.BOARD_LIST;
			
		}
		
		return View.BOARD_LIST;
	}

	public int postView(int selectedPostNo) {
		
		//조회수 증가 쿼리
		
		postViewCount(selectedPostNo);
		Map<String, Object> postArticle = boardDao.postSelect(selectedPostNo);

		List<Map<String, Object>> comment = boardDao.commentList(selectedPostNo);
		System.out.println("======================================");
		System.out.println("글 번호\t :" + postArticle.get("POST_NUM"));
		System.out.println("글 제목\t :" + postArticle.get("POST_TITLE"));
		System.out.println("작성자\t :" + postArticle.get("MEM_NAME"));
		System.out.println("작성일\t :" + postArticle.get("POST_DATE"));
		System.out.println("조회수\t :" + postArticle.get("POST_VIEW"));
		System.out.println("--------------------------------------");
		System.out.println(postArticle.get("POST_CONTENT"));
		System.out.println("================댓글===================");

		
		
		for(Map<String, Object> com : comment) {
			System.out.print(com.get("COM_PAR_NUM") == null ? "" : "▷" );
			System.out.println(com.get("COM_NUM") + "\t"
			+ com.get("MEM_NAME") + "\t"
			+ com.get("COM_CONTENT") );
		}
		System.out.println("======================================");

		System.out.println("1.수정\t2.삭제\t3.댓글\t0.게시글목록 보기");
		System.out.print("입력 > ");
		int input = ScanUtil.nextInt();

		ArrayList<Object> param = new ArrayList<>();
		int result = 0;

		switch (input) {
			case 1:
				if(!Controller.loginUser.get("MEM_NUM").equals(postArticle.get("POST_WRITER"))){
					System.out.println("작성자만 수정 가능합니다.");
					return View.POST_VIEW;
				}
				System.out.println("수정할 글 제목을 입력 하세요>");
				String title = ScanUtil.nextLine();
				System.out.println("수정할 글 내용을 입력 하세요>");
				String text = ScanUtil.nextLine();

				param.add(title);
				param.add(text);
				param.add(selectedPostNo);
				result = boardDao.postUpdate(param);
				System.out.println(result + "건이 수정 되었습니다.");
				return View.POST_VIEW;
			case 2:
				if(!Controller.loginUser.get("MEM_NUM").equals(postArticle.get("POST_WRITER"))){
					System.out.println("작성자만 삭제 가능합니다.");
					return View.POST_VIEW;
				}
				param.add(selectedPostNo);
				boardDao.postDelete(param);
				return View.BOARD_VIEW;
			case 3:
				comment();
				return View.POST_VIEW;
			case 0:
				return View.BOARD_VIEW;

		}

		return View.BOARD_LIST;
	}

	private void postViewCount(int selectedPostNo) {
		
		ArrayList<Object> param = new ArrayList<>();
		param.add(selectedPostNo);
		param.add(selectedPostNo);
		
		boardDao.postViewCount(param);
		
	}


	public int postInsert() {

		System.out.println("답글을 달 번호를 입력해주세요(그냥 글은 0)");
		int input = ScanUtil.nextInt();
		System.out.println("새로 등록할 글 제목을 입력 하세요>");
		String title = ScanUtil.nextLine();
		System.out.println("새로 등록할 글 내용을 입력 하세요>");
		String text = ScanUtil.nextLine();
		
		
		ArrayList<Object> param = new ArrayList<>();
		
//		boardnum =>  타이틀, 텍스트, 작성자

		param.add(input==0 ? null : input);
		param.add(title);
		param.add(text);
		param.add(Controller.loginUser.get("MEM_NUM"));

		param.add(selectedBoardNo);
		
		
		int result = boardDao.postInsert(param);
		
		System.out.println(result + "건이 등록 되었습니다.");
		
		
		return View.BOARD_LIST;
	}

	public int comment() {
		System.out.println("1.댓글 쓰기\t2.댓글수정\t3.댓글삭제\t0.돌아가기");
		int input = ScanUtil.nextInt();
		ArrayList<Object> param = new ArrayList<>();

		switch (input) {
			case 1:
				System.out.println("댓글을 달 번호를 입력해주세요(그냥댓글은 0)");
				input = ScanUtil.nextInt();
				System.out.println("댓글 내용을 입력 하세요>");
				String com = ScanUtil.nextLine();

				param.add(input==0 ? null : input);
				param.add(Controller.loginUser.get("MEM_NUM"));
				param.add(com);
				param.add(selectedPostNo);
				System.out.println(boardDao.commentAdd(param)  + "건의 댓글이 추가 되었습니다.");

				return View.POST_VIEW;
			case 2:
				System.out.println("수정할 댓글 번호를 입력해주세요");
				input = ScanUtil.nextInt();

				if(!boardDao.commentWriter(input).get("COM_WRITER").equals(Controller.loginUser.get("MEM_NUM"))){
					System.out.println("작성자만 수정 가능합니다.");
					return View.POST_VIEW;
				}

				System.out.println("수정할 댓글 내용을 입력 하세요>");
				com = ScanUtil.nextLine();

				param.add(com);
				param.add(input);
				System.out.println(boardDao.commentEdit(param) + "건의 댓글이 수정 되었습니다." );
				return View.POST_VIEW;
			case 3:
				System.out.println("삭제할 댓글 번호를 입력해주세요");
				input = ScanUtil.nextInt();
				if(!boardDao.commentWriter(input).get("COM_WRITER").equals(Controller.loginUser.get("MEM_NUM"))){
					System.out.println("작성자만 삭제 가능합니다.");
					return View.POST_VIEW;
				}
				param.add(input);
				System.out.println(boardDao.commentDelete(param)+ "건의 댓글이 삭제 되었습니다.");
				return View.POST_VIEW;
			case 0:
				return View.POST_VIEW;




		}
		return View.POST_VIEW;
	}
}
