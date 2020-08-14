package service;

import java.util.List;
import java.util.Map;

import dao.GraphDao;
import util.ScanUtil;
import util.View;

public class GraphService {
	
	//싱글톤 패턴 
		private static GraphService instance;
		private GraphService(){}
		
		public static GraphService  getInstance() {
			if(instance == null){
				instance = new GraphService();
			}
			return instance;
		}
		
		//--------
		
		private GraphDao graphDao = GraphDao.getInstance();
		
		
		
		
		//그래프 메인화면 
		public int graphHome() {
			List<Map<String , Object>> braList = graphDao.selectBrcList();
			
			System.out.println("================= 보유 지점 =================");						
			for(Map<String, Object> brc : braList){
				System.out.println("[ 지점번호: " + brc.get("BRC_NUM") + " / "
						+ brc.get("BRC_NAME") + " 지점 ]");
			}
			System.out.println("=======================================================");
			System.out.println("1.지점별 통계조회\t0.이전 페이지");
			System.out.print("> ");
			
			int input = 0;
			input = ScanUtil.nextInt();
			
			switch (input) {
			
			case 1:
				
				List<Map<String , Object>> nowMonth = graphDao.selectYearMonth();
				System.out.println("===================================================");
				System.out.println("지점이름 / 제품번호 / 카테고리 / 제품이름 / 년월 / 구입수량");
				for(Map<String, Object> now : nowMonth){
					System.out.println(now.get("OBRC") + "\t"
							+ now.get("PNUM") + "\t"
							+ now.get("CGR") + "\t"
							+ now.get("PNAME") + "\t"
							+ now.get("ODATE") + "\t"
							+ now.get("SUM")
							);
				}
				System.out.println("===================================================");
				System.out.println("1.더 자세한 통계(카테고리, 이름, 기간)\t0.이전 페이지\"");
				System.out.print("> ");
				
				input = 0;
				input = ScanUtil.nextInt();
				
				String category = "";
				String pname = "";
				String startYeatMonth = "";
				String endYeatMonth = "";
				
				switch (input) {
				
				case 1:
					
					System.out.println("1.제품 카테고리 입력 [YES(1) or NO(2)]");
					System.out.println("> ");
					input = 0;
					input = ScanUtil.nextInt();
					switch (input) {
					case 1:
						System.out.println("조회할 카테고리 입력>");
					    category = ScanUtil.nextLine();
					case 2: //
					}
					
					System.out.println("2.제품이름 입력 [YES(1) or NO(2)]");
					System.out.println("> ");
					input = 0;
					input = ScanUtil.nextInt();
					switch (input) {
					case 1:
						System.out.println("조회할 제품이름 입력>");
					    pname = ScanUtil.nextLine();
					case 2: //
					}
					
					System.out.println("3.조회기간 입력 [YES(1) or NO(2)]");
					System.out.println("> ");
					input = 0;
					input = ScanUtil.nextInt();
					switch (input) {
					case 1:
						System.out.println("조회할 조회할 첫달(yy-mm)");
						System.out.println("> ");
					    startYeatMonth = ScanUtil.nextLine();
						
						System.out.println("조회할 조회할 마지막달(yy-mm)");
						System.out.println("> ");
					    endYeatMonth = ScanUtil.nextLine();
					
					case 2: //
						
					}
					
					List<Map<String , Object>> deepSelect = graphDao.selectDeep(category, pname, startYeatMonth, endYeatMonth);
					System.out.println("===================================================");
					System.out.println("지점이름 / 제품번호 / 카테고리 / 제품이름 / 년월 / 구입수량");
					for(Map<String, Object> deep : deepSelect){
						System.out.println(deep.get("OBRC") + "\t"
								+ deep.get("PNUM") + "\t"
								+ deep.get("CGR") + "\t"
								+ deep.get("PNAME") + "\t"
								+ deep.get("ODATE") + "\t"
								+ deep.get("SUM")
								);
					}
					System.out.println("===================================================");
					
					return View.GRAPH;
					
				case 0:
				
				return View.GRAPH;
					
				}
				
				
				
				
			} //스위치
			
			
			return View.GRAPH;
		}
		
		
		
		
}