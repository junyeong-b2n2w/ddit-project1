package service;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import controller.Controller;
import dao.MyPageDao;
import dao.UserDao;
import util.ScanUtil;
import util.View;

public class UserService {

	
	//싱글톤 패턴 
	private static UserService instance;
	private UserService(){}
	
	public static UserService  getInstance() {
		if(instance == null){
			instance = new UserService();
		}
		return instance;
		
	}
	
	//-------------
	
	private UserDao userDao = UserDao.getInstance();
	private MyPageDao myPageDao = MyPageDao.getInstance();
	
	
	public int join() {
		//아이디 중복 검증
		System.out.println("===========회원가입================");
		// ID 5-20 영소문자 숫자 - _ 만가능
		// PW 8-20 영소문자 숫자 !@# 만 가능
		System.out.print("아이디>");
		String userId = ScanUtil.nextLine();

		System.out.print("비밀번호>");
		String password = ScanUtil.nextLine();
		System.out.print("비밀번호 확인>");
		String password2 = ScanUtil.nextLine();

		System.out.print("이름>");
		String userName = ScanUtil.nextLine();


		//userId 중복 검사

		Map<String, Object> idExist = userDao.isUserExist(userId);

		if(null != idExist.get("MEM_ID")){
			System.out.println("해당 아이디가 이미 존재합니다.");
			return View.HOME;
		}

		//정규 표현식 검사
		String regexId = "[a-z0-9-_]{5,20}";
		Pattern p_id = Pattern.compile(regexId);

		String regexPw = "[a-z0-9!@#]{8,20}";
		Pattern p_pw= Pattern.compile(regexPw);

		Matcher m_id = p_id.matcher(userId);
		Matcher m_pw = p_pw.matcher(password);

		if(!m_id.matches()){
			System.out.println("ID는 5-20글자 영소문자, 숫자 - _ 만 가능합니다.");
			return View.HOME;
		}
		if(!m_pw.matches()){
			System.out.println("비밀번호는 8-20글자 영소문자, 숫자 !@# 만 가능합니다.");
			return View.HOME;
		}
		if(!password.equals(password2)){
			System.out.println("비밀번호와 비밀번호 확인이 일치하지 않습니다.");
			return View.HOME;
		}

		//비밀번호 암호화
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("SHA-256");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		md.update(password.getBytes());
		String encPassword = String.format("%064x", new BigInteger(1, md.digest()));



		// 담기
		Map<String, Object> param = new HashMap<>();
		
		param.put("MEM_ID", userId);
		param.put("MEM_PASSWORD", encPassword);
		param.put("MEM_NAME", userName);
		
		
		int result = userDao.insertUser(param);
		
		if(0<result){
			System.out.println("회원가입 성공");
		}else{
			System.out.println("회원가입 실패");
		}
		
		
		return View.HOME;
	}
	
	public int login(){
		
		System.out.println("================로그인============");
		System.out.print("아이디>");
		String userId = ScanUtil.nextLine();
		System.out.print("비밀번호>");
		String password = ScanUtil.nextLine();

		//비밀번호 복호화?
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("SHA-256");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		md.update(password.getBytes());
		String encPassword = String.format("%064x", new BigInteger(1, md.digest()));


		Map<String, Object> user = userDao.selectUser(userId, encPassword);
		
		
		
		if(user == null){
			System.out.println("아이디 혹은 비밀번호를 잘못 입력하였습니다.");
		}else{
			System.out.println("로그인 성공");
			
			Controller.loginUser = user;
			user.put("BRC_NUM", (myPageDao.selectMyPage(user.get("MEM_NUM"))).get("BRC_NUM"));
		
			return View.BOARD_LIST;
		}
		return View.LOGIN;
		
	}
}
