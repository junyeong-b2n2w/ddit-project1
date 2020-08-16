package service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import controller.Controller;
import dao.DeliveryDao;
import util.ScanUtil;
import util.View;
import dao.CartDao;
import dao.OrderDao;

public class CartService {

	
	
	//싱글톤 패턴 
	private static CartService instance;
	private CartService(){}
	
	public static CartService  getInstance() {
		if(instance == null){
			instance = new CartService();
		}
		return instance;
		
	}
	private CartDao cartDao = CartDao.getInstance();
	private OrderDao orderDao = OrderDao.getInstance();
	private DeliveryDao deliveryDao = DeliveryDao.getInstance();

	public static List<Map<String, Object>> cart = new ArrayList<>();
	
	
	public int cartMain() {
	
		cartView();
		System.out.println("1.수량 변경\t2.품목 삭제\t3.주문하기\t0.돌아가기");
		System.out.print("입력 >");
		int input = ScanUtil.nextInt();
		
		switch (input) {
		case 1:
			if(cart.size() == 0){
				System.out.println("장바구니가 비었습니다.");
				return View.CART_MAIN_VIEW;
			}
			cartEdit();
			return View.CART_MAIN_VIEW;

		case 2:
			if(cart.size() == 0){
				System.out.println("장바구니가 비었습니다.");
				return View.CART_MAIN_VIEW;
			}
			cartDelete();
			return View.CART_MAIN_VIEW;
		case 3: 
			if(cart.size() == 0){
				System.out.println("장바구니가 비었습니다.");
				return View.CART_MAIN_VIEW;
			}
			cartOrder();
			//배송을 만들어 줘야해
			deliveryDao.insertOrder(Controller.loginUser.get("BRC_NUM"));

			return View.ORDER_MAIN_VIEW;
		case 0:
			return View.ORDER_MAIN_VIEW;
			
		}
		
		
		return View.ORDER_MAIN_VIEW;
	}
	
	
	
	private int cartOrder() {
		int total = 0;

		// 장바구니에 담긴 물품의 총 가격을 가져옴
		for(Map<String, Object> cartItem : cart){
		total = total + Integer.valueOf(String.valueOf(cartItem.get("CART_QUNTITY")))
				 * Integer.valueOf(String.valueOf(cartItem.get("PROD_PRICE")));
		}

		//지점의 예치금을 가져와
		int credit = Integer.valueOf(String.valueOf(orderDao.checkCredit(Controller.loginUser.get("BRC_NUM")).get("BRC_CREDIT")));
		
		
		if(total > credit){
			System.out.println("예치금이 부족합니다");
			return View.CART_MAIN_VIEW;
		}

		int remain = credit - total;
		orderDao.useCredit(remain, Controller.loginUser.get("BRC_NUM"));
		System.out.println("예치금 사용합니다. 잔여 :" + remain);


		//주문 하기
		//1. 주문 번호 생성 및 주문

		int proc = cartDao.cartOrderProc(Controller.loginUser.get("BRC_NUM"));

		//2. 주문상세
		int count = 0;

		Map<String, Object> odnum = cartDao.currentOrderNum();
		Object wh_num = orderDao.checkCredit(Controller.loginUser.get("BRC_NUM")).get("BRC_WH_NUM");

		for(Map<String, Object> cartItem : cart){
			//카트에서 아이템ㅇ 빼와서
			//주문 상세에 인서트 하고
			orderDao.cartToOrder(odnum.get("OD_NUM") ,cartItem);
			//재고 테이블에서 주문된만큼 재고를 빼준다.
			orderDao.outStock(cartItem, wh_num);
			count++;
		}

		System.out.println("주문번호" + odnum.get("OD_NUM")+ "," +count + "건이 주문 완료되었습니다.");

		// 장바구니 클리
		cart.removeAll(cart);



		return View.ORDER_MAIN_VIEW;
	}

	private void cartDelete() {
		System.out.print("삭제할 품목 번호 > ");
		int selectedItem = ScanUtil.nextInt();

		int index=0;
		for(Map<String, Object> item : cart){
			if(String.valueOf(item.get("PROD_NUM")).equals(String.valueOf(selectedItem))){
				break;
			}
			index++;
		}

		cart.remove(index);
	}

	public void cartEdit() {
		System.out.print("수정할 품목 번호 > ");
		int selectedItem = ScanUtil.nextInt();
		System.out.print("수정할 수량  > ");
		int selectedItemCount = ScanUtil.nextInt();

		for(Map<String, Object> item : cart){

			if(String.valueOf(item.get("PROD_NUM")).equals(String.valueOf(selectedItem))){
				item.put("CART_QUNTITY", selectedItemCount);
			}
		}

	}

	public void cartView() {
		int total = 0;
		System.out.println("┏━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┓");
		System.out.println("                          장바구니                                  ");
		System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
		System.out.printf("    %-4.4s  %-20.20s %3.3s %10.10s %10.10s   \n","품목번호","품목명","수량","개당 가격","총 가격");
		System.out.println("─────────────────────────────────────────────────────────────────── ");
		for(Map<String, Object> cartItem : cart){
			System.out.printf("   %-4.4s %-20.20s      %3.3s %10.10s %10.10s \n",
					cartItem.get("PROD_NUM"), cartItem.get("PROD_NAME"),
					cartItem.get("CART_QUNTITY"), cartItem.get("PROD_PRICE"),
					(Integer.valueOf(String.valueOf(cartItem.get("CART_QUNTITY")))
							* Integer.valueOf(String.valueOf(cartItem.get("PROD_PRICE"))))
			);



		total = total + Integer.valueOf(String.valueOf(cartItem.get("CART_QUNTITY")))
				 * Integer.valueOf(String.valueOf(cartItem.get("PROD_PRICE")));
		}
		System.out.printf("  장바구니 총액 : %-45.453s \n", total);

		System.out.println("┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛");
	}


	public int cartAdd() {

		System.out.print("추가할 품목 번호 > ");
		int selectedItem = ScanUtil.nextInt();

		List<Map<String, Object>> searchItemList = cartDao.searchStock(Controller.loginUser.get("BRC_NUM"));

		if (searchItemList.size() == 0 ){
			System.out.println("본인이 사용하는 창고에 해당 품목이 존재하지 않습니다.");
			return View.ORDER_MAIN_VIEW;
		}

		int count = 0;
		for(Map<String, Object> item : searchItemList){
			if(Integer.valueOf(String.valueOf(item.get("PROD_NUM"))) != selectedItem){
				count ++;
			}
		}

		if(searchItemList.size() == count){
			System.out.println("본인이 사용하는 창고에 해당 품목이 존재하지 않습니다.");
			return View.ORDER_MAIN_VIEW;
		}

		System.out.print("추가할 수량  > ");
		int selectedItemCount = ScanUtil.nextInt();
		
		
		Map<String, Object> item = cartDao.selectProd(selectedItem);
		item.put("CART_QUNTITY", selectedItemCount);
		
		//
		if(cart.size() == 0){
			cart.add(item);
		}else{
			for(Map<String, Object> cartItem : cart){
				if(cartItem.get("PROD_NUM").equals(item.get("PROD_NUM"))){
					cartItem.put("CART_QUNTITY", Integer.valueOf(String.valueOf(cartItem.get("CART_QUNTITY"))) 
							+ Integer.valueOf(String.valueOf(item.get("CART_QUNTITY"))));
				}
			}
			
		cart.add(item);
			}
		
		return View.ORDER_MAIN_VIEW;
		}
		
		
	}
