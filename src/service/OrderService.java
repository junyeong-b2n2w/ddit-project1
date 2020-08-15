package service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import controller.Controller;
import dao.BoardDao;
import dao.OrderDao;
import util.ScanUtil;
import util.View;

public class OrderService {

	
	
	//싱글톤 패턴 
	private static OrderService instance;
	private OrderService(){}
	
	public static OrderService  getInstance() {
		if(instance == null){
			instance = new OrderService();
		}
		return instance;
		
	}

	private OrderDao orderDao = OrderDao.getInstance();
	private CartService cartService = CartService.getInstance();
	private OrderViewService orderViewService = OrderViewService.getInstance();

	
	
public int orderMainView(){
	System.out.println("┏━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┓");
	System.out.println("                           주문 페이지                           ");
	System.out.println("┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛");
	System.out.println("1.검색  2.장바구니 추가  3.장바구니 확인  4.주문내역 확인  0.돌아가기");
	System.out.print("입력 > ");
	int input = ScanUtil.nextInt();
	switch (input) {
	case 1:
		System.out.print("카테고리 입력 (식료품 = 1, 부가기재 = 2, 전체 = 0) > ");
		int cate = ScanUtil.nextInt();
		System.out.print("검색어 입력(전체검색 = 0) > ");
		String search = ScanUtil.nextLine();
		return orderSearch(cate, search);

	case 2:
		return View.CART_ADD;
		
	case 3:
		return View.CART_MAIN_VIEW;
	case 4:
		orderViewService.orderViewList();
		return View.ORDER_MAIN_VIEW;
		
	case 0: return View.BOARD_LIST;
		
		
	}
	
	
	
	
	
	return View.BOARD_LIST;
	
}





public int orderSearch(int cate, String search) {


	Object wh_num = Controller.loginUser.get("MEM_PERMISSION").equals("1")? "0":
			orderDao.checkCredit(Controller.loginUser.get("BRC_NUM")).get("BRC_WH_NUM");
	
	List<Map<String, Object>> items = orderDao.orderSearchItem(cate, search, wh_num);

	System.out.println("┏━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┓");
	System.out.printf("┃       검색 결과 페이지   카테고리 :%-20.20s  검색어 : %-20.20s  ┃\n", cate == 0 ? "전체": cate == 1 ? "식료품" : "부가기재" , search.equals("0") ? "전체" : search);
	System.out.println("┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛");
	System.out.println("┏━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┓");
	System.out.printf("┃ %-5.5s %-20.20s    %-15.15s   %10.10s %5.5s %8.8s ┃\n","품목번호","품목명","설명","가격","재고","카테고리");


	for(Map<String, Object> prod : items){
		System.out.printf("┃ %-5.5s %-20.20s    %-15.15s    %10.10s %5.5s %8.8s ┃\n",
				prod.get("PROD_NUM"), prod.get("PROD_NAME"), prod.get("PROD_TEXT"),
				prod.get("PROD_PRICE") , prod.get("STOCK_COUNT"), prod.get("PROD_CTEGORY"));

	}
	System.out.println("┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛");
	
	
	return View.ORDER_MAIN_VIEW;
}



}