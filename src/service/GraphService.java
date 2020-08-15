package service;

import java.util.List;
import java.util.Map;

import dao.GraphDao;
import util.ScanUtil;
import util.View;

public class GraphService {

	//싱글톤 패턴 
	private static GraphService instance;

	private GraphService() {
	}

	public static GraphService getInstance() {
		if (instance == null) {
			instance = new GraphService();
		}
		return instance;
	}

	//--------

	private GraphDao graphDao = GraphDao.getInstance();


	//그래프 메인화면
	public int graphHome() {

		int input = 0;

		System.out.println("┏━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┓");
		System.out.println("            통계 보기               ");
		System.out.println("                                  ");
		System.out.println("          1. 통계 검색하기           ");
		System.out.println("          0. 돌아가기               ");
		System.out.println("                                  ");
		System.out.println("┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛");
		System.out.print("입력 >");
		input = ScanUtil.nextInt();

		switch (input){
			case 1:
				System.out.print("검색할 품목 명을 입력해 주세요 (전체 : 0 ) > ");
				String prod = ScanUtil.nextLine();
				System.out.print("검색할 지점 번호를 입력해 주세요 (전체 : 0 ) > ");
				String branch = ScanUtil.nextLine();
				System.out.print("검색할 카테고리 번호를 입력해 주세요 (식료품 :1 , 부가기재 :2 전체 : 0 ) > ");
				String cate = ScanUtil.nextLine();

				System.out.print("검색할 기간의 시작을 입력해 주세요 (전체 : 0  | ex) 200814 ) > ");
				String startDate = ScanUtil.nextLine();
				System.out.print("검색할 기간의 마지막을 입력해 주세요 (전체 : 0  | ex) 200814 ) > ");
				String endDate = ScanUtil.nextLine();
				System.out.print("정렬 기준을 선택해 주세요 (판매량 내림차순 :1, 판매량 오름차순: 2) > ");
				String OrderBy = ScanUtil.nextLine();

				// 품목 이름 검
				//날짜 검색
				List<Map<String, Object>> searchByProdList = graphDao.searchByProd(prod, branch,cate,startDate, endDate, OrderBy);



				System.out.println("┏━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┓");
				System.out.printf("       품목별 통계 결과 페이지   검색 품목명 : %-10.10s   %-6.6s ~ %-6.6s   %-8.8s %-4.4s   ┃\n",
						prod.equals("0")? "전체" : prod,startDate.equals("0")? "" : startDate , endDate.equals("0")? "" : endDate
				, branch.equals("0")? "전제 지점" : branch + "지점", cate.equals("0") ? "": cate.equals("1") ? "식료품" : "부가기재" );
				System.out.println("┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛");
				System.out.println("┏━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┓");
				System.out.printf(" %-5.5s %-20.20s    %-15.15s   %10.10s  ┃\n","품목번호","품목명","판매량","가격");


				for(Map<String, Object> searchedProd : searchByProdList){
					System.out.printf("┃ %-5.5s %-20.20s    %-15.15s    %10.10s  ┃\n",
							searchedProd.get("PROD_NUM"), searchedProd.get("PROD_NAME"), searchedProd.get("COUNT"),
							searchedProd.get("PROD_PRICE") );

				}
				System.out.println("┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛");


				return View.GRAPH;

			case 0:

				return View.MY_PAGE_ADMIN;

		}


		return View.MY_PAGE_ADMIN;
	}


}
