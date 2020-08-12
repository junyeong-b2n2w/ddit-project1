package controller;

import java.util.Map;

import service.*;
import util.ScanUtil;
import util.View;

public class Controller {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		
		new Controller().start();
	}
	
	public static Map<String, Object> loginUser;
	
	private UserService userService = UserService.getInstance();
	private BoardService boardService = BoardService.getInstance();
	private MyPageService myPageService = MyPageService.getInstance();
	private OrderService orderService = OrderService.getInstance();
	private CartService cartService = CartService.getInstance();
	private MyPageAdminService myPageAdminService = MyPageAdminService.getInstance();


	private void start() {
		
		int view = View.HOME;
		
		while(true){
			switch (view) {
				case View.HOME: view = home(); break;
				case View.LOGIN: view = userService.login(); break;
				case View.JOIN: view = userService.join(); break;
				case View.BOARD_LIST:  view = boardService.boardList(); break;
				case View.BOARD_VIEW:  view = boardService.boardView(boardService.selectedBoardNo); break;
				case View.BOARD_INSERT_FORM:  view = boardService.postInsert(); break;
				case View.POST_VIEW:  view = boardService.postView(boardService.selectedPostNo); break;
				case View.MY_PAGE_USER: view = myPageService.myPageUser(); break;
				case View.MY_PAGE_ADMIN: view = myPageAdminService.myPageAdminHome(); break;
				case View.MEM_CONTROL: view = myPageAdminService.myPageAdminMemControl(); break;
				case View.BRANCH_CONTROL: view = myPageAdminService.myPageAdminBranchControl(); break;
				case View.WH_CONTROL: view = myPageAdminService.myPageAdminWhControl(); break;
				case View.PROD_CONTROL: view = myPageAdminService.myPageAdminProdControl(); break;
				case View.WH_STOCK: view = myPageAdminService.myPageAdminWhStockControl(); break;
				case View.MY_PAGE_COMMON: view = myPageAdminService.myPageCommonHome(); break;


				case View.ORDER_MAIN_VIEW:  view = orderService.orderMainView(); break;
				case View.CART_MAIN_VIEW:  view = cartService.cartMain(); break;

			default:
				break;
			}
			
		}
	}

	private int home() {
		
		System.out.println("-------------------------------------------------");
		System.out.println("대덕 1차 프로젝트 테스트");
		System.out.println("-------------------------------------------------");
		System.out.println("1.로그인\t\t2.회원가입\t\t0.프로그램 종료");
		System.out.println("-------------------------------------------------");
		System.out.print("번호입력 > ");
		
		
		int input = ScanUtil.nextInt();
		
		switch (input) {
		case 1: return View.LOGIN;			
		case 2: return View.JOIN;
		case 0:
			System.out.println("프로그램이 종료되었습니다");
			System.exit(0);
			break;
		
		}
		return View.HOME;
	}

}
