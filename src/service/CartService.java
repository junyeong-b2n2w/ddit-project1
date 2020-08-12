package service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import controller.Controller;
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
	public static List<Map<String, Object>> cart = new ArrayList<>();
	
	
	public int cartMain() {
	
		cartView();
		System.out.println("1.수량 변경\t2.품목 삭제\t3.주문하기\t0.돌아가기");
		System.out.print("입력 >");
		int input = ScanUtil.nextInt();
		
		switch (input) {
		case 1:
			cartEdit();
			return View.CART_MAIN_VIEW;

		case 2: 
			cartDelete();
			return View.CART_MAIN_VIEW;
		case 3: return View.HOME;
		case 0:
			return View.HOME;
			
		}
		
		
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

	private void cartEdit() {
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

	private void cartView() {
		int total = 0;
		System.out.println("=====장바구니======");
		System.out.println("품목번호\t품목명\t수량\t개당가격\t총가격" );
		for(Map<String, Object> cartItem : cart){
			System.out.println( cartItem.get("PROD_NUM") + "\t" 
		+ cartItem.get("PROD_NAME") + "\t"
		+ cartItem.get("CART_QUNTITY") + "\t"
		+ cartItem.get("PROD_PRICE") + "\t"
		 + (Integer.valueOf(String.valueOf(cartItem.get("CART_QUNTITY")))
				 * Integer.valueOf(String.valueOf(cartItem.get("PROD_PRICE"))))
					);
		total = total + Integer.valueOf(String.valueOf(cartItem.get("CART_QUNTITY")))
				 * Integer.valueOf(String.valueOf(cartItem.get("PROD_PRICE")));
		}
		System.out.println("total = " + total);

	}
	
	
	void cartAdd() {
		
		System.out.print("추가할 품목 번호 > ");
		int selectedItem = ScanUtil.nextInt();
		System.out.print("추가할 수량  > ");
		int selectedItemCount = ScanUtil.nextInt();
		
		
		Map<String, Object> item = cartDao.selectProd(selectedItem);
		item.put("CART_QUNTITY", selectedItemCount);
		
		cart.add(item);
		
	}
}