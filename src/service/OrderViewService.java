package service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import controller.Controller;
import util.JDBCUtil;
import util.ScanUtil;
import util.View;
import dao.OrderViewDao;

public class OrderViewService {
	private static OrderViewService instance;
	private OrderViewService(){}
	
	public static OrderViewService getInstance() {
		if(instance == null){
			instance = new OrderViewService();
		}
		return instance;
	}
	
	private OrderViewDao orderViewDao = OrderViewDao.getInstance();
	
	
	public int orderViewList(){
		System.out.println("조회할 기간을 설정해주세요.");
		System.out.println("1.최근 30일\t2.최근 60일\t3.최근 90일\t4.최근 180일 \t5.1년");
		int month = ScanUtil.nextInt();
		int serchMonth = 0;
		switch(month){
		case 1:
			serchMonth = 30;
			break;
		case 2:
			serchMonth = 60;
			break;
		case 3:
			serchMonth = 90;
			break;
		case 4:
			serchMonth = 180;
			break;
		case 5:
			serchMonth = 365;
			break;
		}
		System.out.println("몇 건을 조회하시겠습니까? (전체조회 : 0)");
		int printCount = ScanUtil.nextInt();
		
		System.out.println("================================주문내역================================");
		System.out.println("주문번호\t제품이름\t\t\t\t\t주문개수\t제품 가격(단가)\t주문 날짜");
		System.out.println("======================================================================");
		List<Map<String, Object>> orderList = orderViewDao.orderList(Controller.loginUser.get("BRC_NUM"), serchMonth);
		Object line = "0";
		int count = 0;
		for(Map<String, Object> orders: orderList){
			if (!line.equals(orders.get("OD_NUM"))){
				System.out.println("----------------------------------------------------------------------");
				line = orders.get("OD_NUM");
				count++;
			}
			System.out.println("  " + orders.get("OD_NUM") +"\t" + orders.get("PROD_NAME") +"\t\t\t" + orders.get("OD_COUNT") + "\t" +orders.get("OD_DATE"));
			if(count == printCount){
				System.out.println("----------------------------------------------------------------------");
				return View.ORDER_MAIN_VIEW;
			}
		}
		return View.ORDER_MAIN_VIEW;
	}
	
}
