package service;

import java.util.List;
import java.util.Map;

import controller.Controller;
import dao.MyPageAdminDao;
import util.ScanUtil;
import util.View;

public class MyPageAdminService {
	
	//싱글톤 패턴 
	private static MyPageAdminService instance;
	private MyPageAdminService(){}
	public static MyPageAdminService getInstance() {
			if(instance == null){
				instance = new MyPageAdminService();
			}
			return instance;
			
		}
		
	//-------------
	
	private MyPageAdminDao myPageAdminDao = MyPageAdminDao.getInstance();
	public static String memId = "";
	public static int insertPermission = 0;
	
	public static int selectBranchNum = 0;
	public static int insertBranchNum = 0;
	public static int deleteBranchNum = 0;
	
	public static int BranchNum = 0;
	public static String updateBranchName = "";
	public static String updateBranchAddress = "";
	public static String updateBranchEmail = "";
	public static String updateBrachPhoneNum = "";
	
	public static String insertBranchName = "";
	public static String insertBranchAddress = "";
	public static String insertBranchEmail = "";
	public static String insertBranchPhoneNum = "";
	
	public static int insertWhNum = 0;
	public static String insertWhAddress = "";
	public static int insertWhUse = 0 ;
	
	public static int deleteWhNum = 0 ;
	
	public static int updateBeforeWhnum = 0;
	public static int updateWhNum = 0;
	public static String updateWhAddress = "";
	public static int updateWhUse = 0;
	
	public static int insertProdNum = 0;
	public static String insertProdCategory = "";
	public static String insertProdName = "";
	public static String insertProdText = "";
	public static int insertPridPrice = 0;
	
	public static int deleteProdNum = 0; 
	
	public static int selectWhNum = 0;
	
//	public static int insertWhNum = 0;
//	public static int insertProdNum = 0;
	public static int insertStockAddCount = 0;
	
	public static int deleteStockWhNum = 0;
	public static int deletProdNum = 0;
		
	//관리자화면	
	public static int myPageAdminHome(){

		System.out.println("======================================");
		System.out.println("관리자 페이지");
		System.out.println("1. 회원관리");
		System.out.println("2. 지점관리");
		System.out.println("3. 창고관리");
		System.out.println("4. 품목관리");
		System.out.println("5. 재고관리");
		System.out.println("6. 주문배송관리------" );
		System.out.println("0. 이전화면");
		System.out.println("======================================");
		System.out.print("입력  > ");

		int input = ScanUtil.nextInt();
		
		switch (input) {
		case 1:
			return View.MEM_CONTROL;
		case 2: 
			return View.BRANCH_CONTROL;
		case 3: 
			return View.WH_CONTROL;
		case 4: 
			return View.PROD_CONTROL;
		case 5: 
			return View.WH_STOCK;
		case 0:
			return View.BOARD_LIST;
		}
		
		return View.BOARD_LIST;
		
	}
	
	//회원관리
	public int myPageAdminMemControl() {
		List<Map<String , Object>> memberList = myPageAdminDao.selectMemberList();
		System.out.println("================회 원 목 록===================");
		System.out.println("회원이름\t회원아이디\t\t가입일자\t\t권한");
		for(Map<String, Object> member : memberList){
			System.out.println(member.get("MEM_NAME") + "\t"
					+ member.get("MEM_ID") + "\t"
					+ member.get("MEM_REGDATE") + "\t"
					+ (member.get("MEM_PERMISSION").equals("1") ? "관리자" : "지점"));
		}
		System.out.println("===========================================");
		System.out.println("1.회원 권한 수정\t0.이전 페이지");
		System.out.print("입력 > ");
		
		int input = ScanUtil.nextInt();
		
		switch (input) {
		case 1:
			System.out.println("권한을 수정할 회원아이디>");
			memId = ScanUtil.nextLine();
			System.out.println("수정할 권한 (1:관리자 2:회원)>");
			insertPermission = ScanUtil.nextInt();

			myPageAdminDao.updateMemberPermission(insertPermission, memId);
			
			
				
		case 0:
			return View.MY_PAGE_ADMIN;
		}
		return View.BOARD_LIST;
	}
	
	//지점관리
	public int myPageAdminBranchControl() {
		
		List<Map<String , Object>> brcArticle = myPageAdminDao.selectBrcList();
		
		System.out.println("====================지 점 목 록==================");
		System.out.println("지점번호\t지점명");
		
		for(Map<String, Object> branch : brcArticle){
			System.out.println(branch.get("BRC_NUM") + "\t"
					+ branch.get("BRC_NAME"));
		}
		System.out.println("===============================================");
		System.out.println("1.지점조회\t2.지점추가\t3.지점삭제\t0.이전 페이지");
		System.out.print("입력 > ");
		
		int input = ScanUtil.nextInt();
		
		switch (input) {
		case 1:
			System.out.println("조회할 지점 번호>");
			selectBranchNum = ScanUtil.nextInt();
			List<Map<String , Object>> brcInfo = myPageAdminDao.selectBrcInfo(selectBranchNum);
			
			for(Map<String, Object> branchNum : brcInfo){
				System.out.println("=================지 점 정 보====================");
				System.out.println("지점번호\t :" + branchNum.get("BRC_NUM"));
				System.out.println("지점명\t :" + branchNum.get("BRC_NAME"));
				System.out.println("지점장\t :" + branchNum.get("MEM_NAME"));
				System.out.println("지점주소\t :" + branchNum.get("BRC_ADDRESS"));
				System.out.println("이메일\t :" + branchNum.get("BRC_EMAIL"));
				System.out.println("전화번호\t :" + branchNum.get("BRC_PHONE"));
				System.out.println("예치금\t :" + branchNum.get("BRC_CREDIT"));
				System.out.println("===============================================");
				 
			}
			System.out.println("1.정보수정 0.이전 페이지");
			System.out.print("입력 > ");
			
			int temp = ScanUtil.nextInt();
			switch (temp) {
			case 1:
				System.out.println("수정할 지점번호>");
				BranchNum = ScanUtil.nextInt();
				System.out.println("변경할 지점명>");
				updateBranchName = ScanUtil.nextLine();
				System.out.println("변경할 주소명>");
				updateBranchAddress  = ScanUtil.nextLine();
				System.out.println("변경할 이메일>");
				updateBranchEmail  = ScanUtil.nextLine();
				System.out.println("변경할 전화번호>");
				updateBrachPhoneNum  = ScanUtil.nextLine();

				myPageAdminDao.updateBrcInfo(updateBranchName, updateBranchAddress, updateBranchEmail, updateBrachPhoneNum, BranchNum);
				return View.BRANCH_CONTROL;
				
			case 0:
				return View.BRANCH_CONTROL;
			}
			
		case 2:
			System.out.println("사용할 지점 번호>");
			insertBranchNum = ScanUtil.nextInt();
			System.out.println("사용할 지점 이름>");
			insertBranchName = ScanUtil.nextLine();
			System.out.println("지점 주소 입력>");
			insertBranchAddress = ScanUtil.nextLine();
			System.out.println("지점 이메일 입력>");
			insertBranchEmail = ScanUtil.nextLine();
			System.out.println("지점 전화번호>");
			insertBranchPhoneNum = ScanUtil.nextLine();

			Object a = Controller.loginUser.get("MEM_NUM");

			myPageAdminDao.insertBrcInfo( insertBranchName, insertBranchAddress, insertBranchEmail, insertBranchPhoneNum, a);
			
			return View.BRANCH_CONTROL;
		case 3:
			System.out.println("삭제할 지점 번호>");
			deleteBranchNum = ScanUtil.nextInt();

			myPageAdminDao.deleteBrcInfo(deleteBranchNum);
			
			return View.BRANCH_CONTROL;
		case 0:
			return View.MY_PAGE_ADMIN;
			
			
			
		}
		return View.BOARD_LIST;
	}

	//창고관리
	public int myPageAdminWhControl() {
		List<Map<String , Object>> whList = myPageAdminDao.selectWhList();
		System.out.println("=================== 창 고 목 록 ===================");
		
		System.out.println("창고번호\t창고주소\t창고사용여부");
		for(Map<String, Object> wh : whList){
			System.out.println(wh.get("WH_NUM") + "\t"
					+ wh.get("WH_ADRESS") + "\t"
					+ (Integer.valueOf(String.valueOf(wh.get("WH_USE"))) == 1 ? "사용" : "X"));
		}
		System.out.println("================================================");
		System.out.println("1.창고 추가\t2.창고 삭제\t3.정보수정\t0.이전 페이지");
		System.out.print("입력 > ");
		
		int input = ScanUtil.nextInt();
		
		switch (input) {
		case 1:
			System.out.println("추가할 창고 번호>");
			insertWhNum = ScanUtil.nextInt();
			System.out.println("창고의 주소>");
			insertWhAddress = ScanUtil.nextLine();
			System.out.println("창고사용여부 (1:사용, 2:비사용)>");
			insertWhUse = ScanUtil.nextInt();

			myPageAdminDao.insertWhInfo(insertWhNum, insertWhAddress, insertWhUse);
			
			return View.WH_CONTROL;
		case 2:
			System.out.println("삭제할 창고 번호>");
			deleteWhNum = ScanUtil.nextInt();

			myPageAdminDao.deleteWhInfo(deleteWhNum);
			
			return View.WH_CONTROL;
		case 3:
			System.out.println("수정할 창고 번호>");
			updateBeforeWhnum = ScanUtil.nextInt();
			System.out.println("변경할 창고번호>");
			updateWhNum = ScanUtil.nextInt();
			System.out.println("변경할 창고주소>");
			updateWhAddress = ScanUtil.nextLine();
			System.out.println("창고사용여부 (1:사용, 2:비사용)>");
			updateWhUse = ScanUtil.nextInt();

			myPageAdminDao.updateWhInfo(updateBeforeWhnum, updateWhNum, updateWhAddress, updateWhUse);
			
			return View.WH_CONTROL;
		case 0:
			return View.MY_PAGE_ADMIN;
			
			
			
		}
		return View.BOARD_LIST;
	}
	
	//상품 관리 
	//품목관리
	public int myPageAdminProdControl() {
		List<Map<String , Object>> prodList = myPageAdminDao.selectProdList();
		System.out.println("========================보 유 중 품 목 - 목 록 =======================");
		System.out.println("제품번호\t카테고리\t제품명\t제픔설명\t가격       ");		
		for(Map<String, Object> prod : prodList){
			System.out.println(prod.get("PROD_NUM") + "\t"
					+ prod.get("PROD_CTEGORY") + "\t"
					+ prod.get("PROD_NAME") + "\t"
					+ prod.get("PROD_TEXT") + "\t"
					+ prod.get("PROD_PRICE"));
		}
		System.out.println("==================================================================");
		System.out.println("1.제품검색\t2.제품추가\t3.제품삭제\t0.이전 페이지");
		System.out.print("입력 > ");
		
		int input = ScanUtil.nextInt();
		
		switch (input) {
		case 1:
			System.out.println("형이한거추가 ");
			return View.PROD_CONTROL;
		case 2:
			System.out.println("추가할 제품번호>");
			insertProdNum = ScanUtil.nextInt();
			System.out.println("카테고리>");
			insertProdCategory = ScanUtil.nextLine();
			System.out.println("제품명>");
			insertProdName = ScanUtil.nextLine();
			System.out.println("제품설명>");
			insertProdText = ScanUtil.nextLine();
			System.out.println("가격>");
			insertPridPrice = ScanUtil.nextInt();

			myPageAdminDao.insertProdInfo(insertProdNum, insertProdCategory, insertProdName, insertProdText, insertPridPrice);
			
			return View.PROD_CONTROL;
			
			
		case 3:
			System.out.println("삭재할 제품 번호>");
			deleteProdNum = ScanUtil.nextInt();

			myPageAdminDao.deleteProd(deleteProdNum);
			
			return View.PROD_CONTROL;
			
		case 0:
			return View.MY_PAGE_ADMIN;
			
			
			
		}
		return View.BOARD_LIST;
	}
	
	//재고 관리 
	//재고관리
	public int myPageAdminWhStockControl() {
		List<Map<String , Object>> whList = myPageAdminDao.selectWhList();
		System.out.println("=========== 창고별 제품 재고량 조회 ============");
		System.out.println("창고번호\t창고주소\t창고사용여부");
		for(Map<String, Object> wh : whList){
			System.out.println(wh.get("WH_NUM") + "\t"
					+ wh.get("WH_ADRESS") + "\t"
					+ (Integer.valueOf(String.valueOf(wh.get("WH_USE"))) == 1 ? "사용" : "X"));
		}
		
		System.out.println("===========================================");
		System.out.println("1.재고 조회\t2.재고 추가\t3.재고 방출\t0.이전 페이지");
		System.out.print("입력 > ");
		int input = ScanUtil.nextInt();
		
		switch (input) {
		case 1:
			System.out.println("조회할 창고 번호>");
			selectWhNum = ScanUtil.nextInt();
			
			List<Map<String , Object>> whList2 = myPageAdminDao.selectWhStock(selectWhNum);
			
			System.out.println("===================================");
			System.out.println("-------------재 고 목 록-------------");
			System.out.println("창고코드\t제품코드\t제품명\t수량");
			for(Map<String, Object> wh : whList2){
				System.out.println(wh.get("WH_NUM") + "\t"
						+ wh.get("PROD_NUM") + "\t"
						+ wh.get("PROD_NAME") + "\t"
						+ wh.get("STOCK_COUNT"));
			}
			System.out.println("-----------------------------------");
			System.out.println("===================================");
			
			return View.WH_STOCK;
		case 2:
			System.out.println("재고를 추가할 창고번호>");
			insertWhNum = ScanUtil.nextInt();
			System.out.println("재고를 추가할 제품번호>");
			insertProdNum = ScanUtil.nextInt();
			System.out.println("추가할 수량>");
			insertStockAddCount = ScanUtil.nextInt();

			//if select -> 창고번호, 제품번호로 재고테이블 조회
			// 결과가 있어 -> updateStock
			// 겨로가가 없어 - > INSERTSTOCK


			Map<String , Object> selectStocList = myPageAdminDao.selectStock(insertWhNum, insertProdNum);


			if(Integer.valueOf(String.valueOf((selectStocList.get("COUNT")))) == 0){
				myPageAdminDao.insertStock(insertWhNum, insertProdNum, insertStockAddCount);
				return View.WH_STOCK;
			}else{
				myPageAdminDao.updateStock(insertWhNum, insertProdNum, insertStockAddCount);
				return View.WH_STOCK;
			}
		case 3:
			System.out.println("재고를 삭제할 창고번호>");
			deleteStockWhNum = ScanUtil.nextInt();
			System.out.println("삭제할 제품번호>");
			deletProdNum = ScanUtil.nextInt();

			myPageAdminDao.deleteStock(deleteStockWhNum, deletProdNum);
			
			return View.WH_STOCK;
		case 0:
			return View.MY_PAGE_ADMIN;



		}
		return View.BOARD_LIST;
	}




}
