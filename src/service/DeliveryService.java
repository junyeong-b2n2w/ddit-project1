package service;

import java.util.List;
import java.util.Map;

import dao.DeliveryDao;
import dao.MyPageDao;

public class DeliveryService {
	private static DeliveryService instance;
	
	public static DeliveryService getInstance() {
		if(instance == null){
			instance = new DeliveryService();
		}
		return instance;
	}
	private DeliveryDao deliveryDao = DeliveryDao.getInstance();

//	public List<Map<String, Object>> orderDeliveryAdmin(){
//		System.out.println("=======================================주문내역=======================================");
//		System.out.println("주문번호\t지점번호\t지점이름\t주문 날짜\t배송 번호\t배송 상태");
//		System.out.println("====================================================================================");
//		List<Map<String, Object>> orderList = 
//		//일단 다 띄우기
//		for(Map<String, Object> orders: orderList){
//			if (!line.equals(orders.get("OD_NUM"))){
//				System.out.println("----------------------------------------------------------------------");
//				line = orders.get("OD_NUM");
//				Map<String, Object> delivery = deliveryDao.deliveryView(orders.get("OD_NUM"));
//				System.out.println("  " + orders.get("OD_NUM") +"\t" + orders.get("PROD_NAME") +"\t\t\t" + orders.get("OD_COUNT") 
//						+ "\t" +orders.get("OD_DATE") + "\t" + delivery.get("DV_NUM") + "\t" + delivery.get("DV_STATUS"));
//				count++;
//			}else{
//				System.out.println("  " + orders.get("OD_NUM") +"\t" + orders.get("PROD_NAME") +"\t\t\t" + orders.get("OD_COUNT") + "\t" +orders.get("OD_DATE"));
//			}
//		}
//		//배송번호 입력
//		//위에 포함 상품 목록, 각 상품 단가, 각 상품 총 가격, 주문 총 가격, 창고번호, 주문 시간
//		
//		return ;
//	}
}
