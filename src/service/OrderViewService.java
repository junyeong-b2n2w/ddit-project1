package service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import controller.Controller;
import dao.MyPageDao;
import util.JDBCUtil;
import util.ScanUtil;
import util.View;
import dao.DeliveryDao;
import dao.OrderViewDao;

public class OrderViewService {
    private static OrderViewService instance;

    private OrderViewService() {
    }

    public static OrderViewService getInstance() {
        if (instance == null) {
            instance = new OrderViewService();
        }
        return instance;
    }

    private OrderViewDao orderViewDao = OrderViewDao.getInstance();
    private DeliveryDao deliveryDao = DeliveryDao.getInstance();
    private MyPageDao myPageDao = MyPageDao.getInstance();
    public static int selectOdNum = 0;
    public static int selectBrcNum = 0;

    //유저
    public int orderViewList() {
        System.out.println("조회할 기간을 설정해주세요.");
        System.out.println("1.최근 30일\t2.최근 60일\t3.최근 90일\t4.최근 180일 \t5.1년");
        int month = ScanUtil.nextInt();
        int serchMonth = 0;
        switch (month) {
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

        for (Map<String, Object> orders : orderList) {
            if (!line.equals(orders.get("OD_NUM"))) {
                System.out.println("----------------------------------------------------------------------");
                line = orders.get("OD_NUM");
                Map<String, Object> delivery = deliveryDao.deliveryView(orders.get("OD_NUM"));
                System.out.println("배송번호 : " + delivery.get("DV_NUM") + "\t" + "배송상태 : " + delivery.get("DV_STATUS"));
                count++;
            }
            System.out.println("  " + orders.get("OD_NUM") + "\t" + orders.get("PROD_NAME") + "\t\t\t" + orders.get("OD_COUNT") + "\t" + orders.get("OD_DATE"));

            if (count == printCount) {
                System.out.println("----------------------------------------------------------------------");
                return View.ORDER_MAIN_VIEW;
            }
        }
        return View.ORDER_MAIN_VIEW;
    }
//------------------------------------------------------------------------------------------------------

    //관리자
    public int orderViewAdmin() {
        System.out.println("======================== 배송 완료 되지 않은 주문내역================================");
        System.out.println("배송 번호\t지점번호\t지점이름\t주문 날짜\t\t\t주문번호\t배송 상태");
        System.out.println("======================================================================");
        List<Map<String, Object>> orderListAdmin = deliveryDao.deliveryViewAdmin();
        Object line = "0";

        for (Map<String, Object> orders : orderListAdmin) {
            System.out.println("  " + orders.get("DV_NUM") + "\t" + orders.get("BRC_NUM") + "\t" + orders.get("BRC_NAME")
                    + "\t" + orders.get("OD_DATE") + "\t" + orders.get("OD_NUM") + "\t" + orders.get("DV_STATUS"));
            System.out.println("----------------------------------------------------------------------");
        }
        System.out.println("======================================================================");
        //선택지
        //배송 완료 목록, 지점별 > 기간별
        System.out.println("1.상세 내역 조회\t2.전체 배송 내역\t3.지점별 조회\t4.배송 상태 관리\t0.돌아가기");
        int input = ScanUtil.nextInt();

        switch (input) {
            case 1:
                System.out.println("조회할 배송 번호 > ");
                selectOdNum = ScanUtil.nextInt();
                detailOrderList(selectOdNum);
                return View.ORDER_VIEW_ADMIN_MAIN;
            case 2:
                return orderViewAll();
            case 3:
                //지점별 조회
                System.out.print("조회할 지점의 번호를 입력하세요. > ");
                selectBrcNum = ScanUtil.nextInt();
                viewBrc(selectBrcNum);
                return View.ORDER_VIEW_ADMIN_MAIN;
            case 4:
                deliveryProd(0);
                break;
            case 0:
                return View.MY_PAGE_ADMIN;
        }
        return View.MY_PAGE_ADMIN;
    }

    //------------------------------------------------------------------------------------------------------
    //case1 상세내역 조회
    public int detailOrderList(int dv_num) {
        System.out.println("================================주문내역================================");
        System.out.println("배송 번호\t주문번호\t지점번호\t주문 날짜\t\t\t창고 번호\t배송 상태");//제품번호, 제품이름, 카테고리
        //각 상품 단가, 각 상품 총 가격, 주문 총 가격, 창고번호, 주문 시간
        //지점 번호 누르면 타고 들어가서 지점 정보 조회
        List<Map<String, Object>> orderList = orderViewDao.dvViewAdmin(dv_num);

        int count = 0;
        for (Map<String, Object> orderListAdmin : orderList) {
            System.out.println(orderListAdmin.get("DV_NUM") + "\t" + orderListAdmin.get("OD_NUM") + "\t  "
                    + orderListAdmin.get("BRC_NUM") + "\t" + orderListAdmin.get("OD_DATE") + "\t"
                    + orderListAdmin.get("BRC_WH_NUM") + "\t" + orderListAdmin.get("DV_STATUS"));
            count++;
            if (count == 1) {
                break;
            }
        }
        System.out.println("======================================================================");
        System.out.println("제품번호\t제품이름\t\t\t\t제품 단가\t제품 개수");
        System.out.println("======================================================================");
        int sum = 0;
        for (Map<String, Object> orderListAdmin : orderList) {
            System.out.println(orderListAdmin.get("PROD_NUM") + "\t" + orderListAdmin.get("PROD_NAME") + "\t\t\t" + orderListAdmin.get("PROD_PRICE") + "\t" + orderListAdmin.get("OD_COUNT"));
            sum += Integer.valueOf(String.valueOf(orderListAdmin.get("PROD_PRICE"))) * Integer.valueOf(String.valueOf(orderListAdmin.get("OD_COUNT")));
            System.out.println("----------------------------------------------------------------------");
        }
        System.out.println("\t\t\t\t금액 합계\t" + sum);
        System.out.println("======================================================================");
        System.out.println("1.배송 상태 관리\t2.제품 정보 조회\t0.돌아가기");
        sw(dv_num);
        return View.ORDER_VIEW_ADMIN_MAIN;
    }

    //------------------------------------------------------------------------------------------------------
    //case2.전체 배송 내역
    public int orderViewAll() {
        System.out.println("======================================================================");
        System.out.println("배송 번호\t주문번호\t지점번호\t지점이름\t주문 날짜\t\t\t배송 상태");
        System.out.println("======================================================================");

        int sum = 0;
        List<Map<String, Object>> viewAll = deliveryDao.deliveryViewAllAdmin();

        for (Map<String, Object> orderListAdmin : viewAll) {
            System.out.println(orderListAdmin.get("DV_NUM") + "\t" + orderListAdmin.get("OD_NUM") + "\t  "
                    + orderListAdmin.get("BRC_NUM") + "\t" + orderListAdmin.get("BRC_NAME") + "\t" +
                    orderListAdmin.get("OD_DATE") + "\t" + orderListAdmin.get("DV_STATUS"));
            System.out.println("----------------------------------------------------------------------");
        }
        System.out.println("======================================================================");
        System.out.println("1.상세 내역 조회\t0. 돌아가기");
        int input = ScanUtil.nextInt();
        switch (input) {
            case 1:
                System.out.println("조회할 배송 번호 > ");
                selectOdNum = ScanUtil.nextInt();
                detailOrderList(selectOdNum);
                return View.ORDER_VIEW_ADMIN_MAIN;
            case 0:
                return View.ORDER_VIEW_ADMIN_MAIN;
        }
        return View.ORDER_VIEW_ADMIN_MAIN;
    }
    //------------------------------------------------------------------------------------------------------
    //case 3

    public int viewBrc(int selectBrcNum) {

        int count = 0;

        Map<String, Object> brcInfo = myPageDao.selectMyPage(selectBrcNum);
        System.out.println("================================지점 정보================================");
        System.out.println("지점번호\t지점 이름");

        System.out.println(brcInfo.get("BRC_NUM") + "\t" + brcInfo.get("BRC_NAME"));


        System.out.println("----------------------------------------------------------------------");

//		count = 0;
//		for(Map<String, Object> selectBranch : brcInfo){
//			count++;
//
//			System.out.println("회원 번호\t" + selectBranch.get("BRC_MEM_NUM") + "\t\t회원 이름\t" + Controller.loginUser.get("MEM_NUM"));
//			System.out.println("지점이름\t" + selectBranch.get("BRC_NAME") + "\t\t주소\t" + selectBranch.get("BRC_ADDRESS"));
//			System.out.println("예치금\t" + selectBranch.get("BRC_CREDIT") + "\t\t창고 번호\t" + selectBranch.get("BRC_WH_NUM"));
//			System.out.println("전화번호\t" + selectBranch.get("BRC_PHONE") + "\t이메일\t\t" + selectBranch.get("BRC_EMAIL"));
//
//			if(count == 1){
//				break;
//			}
//			return 0;
//		}

//        System.out.println("================================주문내역================================");
//        System.out.println("주문번호\t제품번호\t제품이름\t\t\t주문개수\t제품 가격(단가)\t주문 날짜\t배송 번호\t배송 상태");
//        System.out.println("======================================================================");

        System.out.println("================================주문내역================================");
        System.out.println("배송 번호\t주문 번호 \t주문 날짜\t\t\t배송 상태");
        System.out.println("======================================================================");

        Object line = "0";

        List<Map<String, Object>> branchOrderList = deliveryDao.deliveryViewBranch(selectBrcNum);
        for (Map<String, Object> orders : branchOrderList) {
            if (!line.equals(orders.get("OD_NUM"))) {
                System.out.println("----------------------------------------------------------------------");
                line = orders.get("OD_NUM");
                Map<String, Object> delivery = deliveryDao.deliveryView(orders.get("OD_NUM"));

                System.out.println(delivery.get("DV_NUM") + "\t" + delivery.get("OD_NUM") + "\t  "
                         + "\t" + delivery.get("OD_DATE") + "\t"
                        + "\t" + delivery.get("DV_STATUS"));

//                System.out.println("  " + orders.get("OD_NUM") + "\t" + orders.get("PROD_NUM") + "\t" + orders.get("PROD_NAME") + "\t\t\t" + orders.get("OD_COUNT")
//                        + "\t" + orders.get("OD_DATE") + "\t" + delivery.get("DV_NUM") + "\t" + delivery.get("DV_STATUS"));
            } else {
//                System.out.println("  " + orders.get("OD_NUM") + "\t" + orders.get("PROD_NUM") + "\t" + orders.get("PROD_NAME") + "\t\t\t" + orders.get("OD_COUNT") + "\t" + orders.get("OD_DATE"));
            }
        }
        System.out.println("----------------------------------------------------------------------");
        System.out.println("1.배송 상세 조회\t2.제품 조회\t0.돌아가기");
        int input = ScanUtil.nextInt();
        switch (input) {
            case 1:
                System.out.println("조회할 배송 번호 > ");
                selectOdNum = ScanUtil.nextInt();
                detailOrderList(selectOdNum);



            case 2:
                System.out.print("정보를 조회할 제품 번호 >");
                int prodSelect = ScanUtil.nextInt();
                prodView(prodSelect);
                return View.DELIVERY_VIEW_BRANCH;
            case 0:
                return View.ORDER_VIEW_ADMIN_MAIN;
        }
        return View.ORDER_VIEW_ADMIN_MAIN;
    }

    //------------------------------------------------------------------------------------------------------
    //case 4
    public int deliveryProd(int input) {
        List<Integer> dvNum = new ArrayList<>();

        int num = input;
        if(input == 0 ) {
            while (true) {
                System.out.println("추가 관리 할 배송 번호를 입력해주세요.(0: 종료)");
                num = ScanUtil.nextInt();
                if (num == 0) {
                    System.out.println("배송 관리를 종료합니다.");
                    break;
                }//if2
                dvNum.add(num);

            }//while
        }else{
            dvNum.add(input);
        }
        System.out.println("======================================================================");
        System.out.println("배송 번호\t지점번호\t지점이름\t주문 날짜\t\t\t주문번호\t배송 상태");
        System.out.println("======================================================================");
        List<Map<String, Object>> viewAllList = new ArrayList<>();

        for (int i = 0; i < dvNum.size(); i++) {

            viewAllList.add(orderViewDao.dvProdView(dvNum.get(i)));

        }

        for (Map<String, Object> orders : viewAllList) {
            System.out.println("  " + orders.get("DV_NUM") + "\t" + orders.get("BRC_NUM") + "\t" + orders.get("BRC_NAME")
                    + "\t" + orders.get("OD_DATE") + "\t" + orders.get("OD_NUM") + "\t" + orders.get("DV_STATUS"));
            System.out.println("----------------------------------------------------------------------");
        }
        System.out.println("======================================================================");
        System.out.println("1.배송 상태 업데이트\t2.배송 관리 목록 수정, 삭제\t0.돌아가기");
        input = ScanUtil.nextInt();
        switch (input) {
            case 1:
                System.out.println("1.배송 중으로\t2.배송 완료로\t0.돌아가기");
                input = ScanUtil.nextInt();
//			배송상태 업데이트
                //
                switch (input) {
                    case 1:
//				1. 배송 중
                        for (int i = 0; i < dvNum.size(); i++) {
                            deliveryDao.dvStatusUpdate("배송 중", dvNum.get(i));
                        }
                        break;
                    case 2:
//				2. 배송 완료
                        for (int i = 0; i < dvNum.size(); i++) {
                            deliveryDao.dvStatusUpdate("배송 완료", dvNum.get(i));
                        }
                        break;
                    case 0:
//				0. 돌아가기
                        break;
                }
                break;
            //
            case 2:
                num = 0;
                    while (true) {
                        if(dvNum.size() == 0){
                            System.out.println("삭제할 리스트가 없어서 종료합니다");
                            break;
                        }
                        System.out.println("삭제 할 배송 번호를 입력해주세요.(0: 종료)");
                        num = ScanUtil.nextInt();
                        if (num == 0) {
                            System.out.println("배송번호 삭제를  종료합니다");
                            break;
                        }//if2
                        for(int i =0 ; i < dvNum.size() ; i ++) {
                            if(dvNum.get(i).equals(num)){
                                dvNum.remove(i);
                            }
                        }
                    }//while

                break;
            case 0:
//			0. 돌아가기
                break;
        }

        return 0;
    }


    //제품정보 조회 case 1-2
    public int prodView(int prodSelect) {
        System.out.println("======================================================================");
        System.out.println("제품번호\t카테고리\t제품명\t\t\t제품설명\t가격");
        System.out.println("======================================================================");
        Map<String, Object> prod = orderViewDao.prodView(prodSelect);
        System.out.println(prod.get("PROD_NUM") + "\t" + prod.get("PROD_CTEGORY") + "\t" + prod.get("PROD_NAME") + "\t" + prod.get("PROD_TEXT") + "\t" + prod.get("PROD_PRICE"));
        System.out.println("0. 돌아가기");
        int input = ScanUtil.nextInt();
        return detailOrderList(selectOdNum);
    }

    public int sw(int dv_num) {
        int input = ScanUtil.nextInt();
        switch (input) {
            case 1:
                deliveryProd(dv_num);
                //배송상태 업데이트
                break;
            case 2:
                System.out.print("정보를 조회할 제품 번호 >");
                int prodSelect = ScanUtil.nextInt();
                prodView(prodSelect);
                return View.ORDER_VIEW_ADMIN_MAIN;
            case 0:
                return View.ORDER_VIEW_ADMIN_MAIN;
        }
        return View.ORDER_VIEW_ADMIN_MAIN;
    }


}




