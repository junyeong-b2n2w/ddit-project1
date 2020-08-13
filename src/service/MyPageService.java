package service;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import util.ScanUtil;
import util.View;
import controller.Controller;
import dao.MyPageDao;
import dao.UserDao;

public class MyPageService {
	private static MyPageService instance;
	private MyPageService(){}
	
	public static MyPageService getInstance() {
		if(instance == null){
			instance = new MyPageService();
		}
		return instance;
	}
	
	private MyPageDao myPageDao = MyPageDao.getInstance();
	
//	- 내정보확인
	public int myPageUser(){
		System.out.println(Controller.loginUser.get("MEM_NUM"));
		Map<String, Object> myBranch = myPageDao.selectMyPage(Controller.loginUser.get("MEM_NUM"));
		
		System.out.println("==============마이페이지==============");
		System.out.println(myBranch.get("BRC_NAME") + "\t지점번호    " + myBranch.get("BRC_NUM"));
		System.out.println("주소\t" + myBranch.get("BRC_ADDRESS"));
		System.out.println("전화번호\t" + myBranch.get("BRC_PHONE") + "\t이메일\t" + myBranch.get("BRC_EMAIL"));
		System.out.println("예치금\t" + myBranch.get("BRC_CREDIT") + "\t창고번호\t" + myBranch.get("BRC_WH_NUM"));
		System.out.println("===================================");
		System.out.println("1.지점 정보 수정\t2.예치금 추가\t0.돌아가기..");
		
		int input = ScanUtil.nextInt();
		switch(input){
		case 1:
			System.out.print("비밀번호를 입력해주세요.\t");
			String password = ScanUtil.nextLine();
			password = pass(password);
			
			if(password.equals(Controller.loginUser.get("MEM_PASSWORD"))){
				System.out.println("1.지점 이름\t2.주소\t3.이메일\t4.전화번호\t5.창고번호");
				input = ScanUtil.nextInt();
				String type = null;
				switch (input) {
//				- 내지점정보 수정
				case 1:
//					지점이름
					System.out.print("수정할 이름 : ");
					String name = ScanUtil.nextLine();
					
					List<Object> param = new ArrayList<>();
					param.add(name);
					param.add(myBranch.get("BRC_NUM"));
					type = "BRC_NAME";
					myPageDao.udtBrcInfo(param, type);
					return View.MY_PAGE_USER;

				case 2:
//					주소
					System.out.println(myBranch.get("BRC_ADDRESS"));
					System.out.print("수정할 주소 : ");
					String brcAdd = ScanUtil.nextLine();
					
					param = new ArrayList<>();
					param.add(brcAdd);
					param.add(myBranch.get("BRC_NUM"));
					type = "BRC_ADDRESS";
					myPageDao.udtBrcInfo(param, type);
					return View.MY_PAGE_USER;
				case 3:
//					이메일
					System.out.println(myBranch.get("BRC_EMAIL"));
					System.out.print("수정할 이메일 : ");
					String email = ScanUtil.nextLine();
					
					param = new ArrayList<>();
					param.add(email);
					param.add(myBranch.get("BRC_NUM"));
					type = "BRC_EMAIL";
				    myPageDao.udtBrcInfo(param, type);
				    return View.MY_PAGE_USER;
				case 4:
//				전화번호
					System.out.println(myBranch.get("BRC_PHONE"));
					System.out.print("수정할 전화번호 : ");
					String pn = ScanUtil.nextLine();
					
					param = new ArrayList<>();
					param.add(pn);
					param.add(myBranch.get("BRC_NUM"));
					type = "BRC_PHONE";
					myPageDao.udtBrcInfo(param, type);
					return View.MY_PAGE_USER;
				
				case 5:
					System.out.println(myBranch.get("BRC_WH_NUM"));
					System.out.print("수정할 창고번호 : ");
					String stock = ScanUtil.nextLine();
					
					param = new ArrayList<>();
					param.add(stock);
					param.add(myBranch.get("BRC_NUM"));
					type = "BRC_WH_NUM";
					myPageDao.udtBrcInfo(param, type);
					Controller.loginUser.put("BRC_NUM", (myPageDao.selectMyPage(Controller.loginUser.get("MEM_NUM"))).get("BRC_NUM"));
					return View.MY_PAGE_USER;
				
				}
				
				//switch
			} else{
				System.out.println("비밀번호가 일치하지 않습니다.");
			} //if
			
			return View.BOARD_LIST; //case1
			
		case 2:
//			- 예치금확인
//				- 예치금 추가
			System.out.print("추가할 예치금 : ");
			int credit = ScanUtil.nextInt();
			credit += Integer.valueOf(String.valueOf(myBranch.get("BRC_CREDIT")));
			System.out.println(credit);
			
			List<Object> param = new ArrayList<>();
			param.add(credit);
			param.add(myBranch.get("BRC_NUM"));
			
			myPageDao.udtBrcInfo(param, "BRC_CREDIT");
			return View.BOARD_LIST;
			
		case 0:
			return View.BOARD_LIST;
		}
		 return View.BOARD_LIST;
	 }
	
	String pass(String password){
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("SHA-256");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		md.update(password.getBytes());
		String encPassword = String.format("%064x", new BigInteger(1, md.digest()));
	
		return encPassword;
	}
}
