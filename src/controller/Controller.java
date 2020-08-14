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
	private OrderViewService orderViewService = OrderViewService.getInstance();
	private void start() {
		//test
		int view = View.HOME;
		
		while(true){
			switch (view) {
				case View.HOME: view = home(); break;

				//유저, 로그인
				case View.LOGIN: view = userService.login(); break;
				case View.JOIN: view = userService.join(); break;

				// 게시판
				case View.BOARD_LIST:  view = boardService.boardList(); break;
				case View.BOARD_VIEW:  view = boardService.boardView(boardService.selectedBoardNo); break;
				case View.BOARD_INSERT_FORM:  view = boardService.postInsert(); break;

				//게시글
				case View.POST_VIEW:  view = boardService.postView(boardService.selectedPostNo); break;

				//마이페이지 유저
				case View.MY_PAGE_USER: view = myPageService.myPageUser(); break;

				//마이페이지 관리자
				case View.MY_PAGE_ADMIN: view = myPageAdminService.myPageAdminHome(); break;
				case View.MEM_CONTROL: view = myPageAdminService.myPageAdminMemControl(); break;
				case View.BRANCH_CONTROL: view = myPageAdminService.myPageAdminBranchControl(); break;
				case View.WH_CONTROL: view = myPageAdminService.myPageAdminWhControl(); break;
				case View.PROD_CONTROL: view = myPageAdminService.myPageAdminProdControl(); break;
				case View.WH_STOCK: view = myPageAdminService.myPageAdminWhStockControl(); break;

				//??
				case View.ORDER_CONTROL: view = myPageAdminService.myPageAdminOrderControl(); break;

				//주문내역 관리자
				case View.ORDER_VIEW_ADMIN: view = orderViewService.orderViewAdmin(); break;

				//주문하기
				case View.ORDER_MAIN_VIEW:  view = orderService.orderMainView(); break;

				//장바구니
				case View.CART_MAIN_VIEW:  view = cartService.cartMain(); break;
				case View.CART_ADD:  view = cartService.cartAdd(); break;


			default:
				break;
			}
			
		}
	}

	private int home() {
		
		System.out.println("┏━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┓");
		System.out.println("┃                                                             ┃ ");
		System.out.println("┃                                                             ┃ ");
		System.out.println("┃                 \"민규네 치킨\"  물류 주문 시스템                    ┃ ");
		System.out.println("┃                                                             ┃ ");
		System.out.println("┃               예상치 못한 코로나에 힘드신 점주님들 힘내세요!            ┃ ");
		System.out.println("┃                                                             ┃ ");
		System.out.println("┃                                                             ┃ ");
		System.out.println("┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛");
		System.out.println("1.로그인\t\t2.회원가입\t\t0.프로그램 종료");
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
