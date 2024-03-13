package egovframework.com.fof.utl;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

public class CommonUtil {
	
	final static Logger log = Logger.getLogger(CommonUtil.class);
	
	final static String returnPath = "la/web/";

	/**
	 * 메인레이아웃
	 * @param sysID
	 * @return
	 */
	public static String getMainLayoutPathBySysID(String SystemID){
		return returnPath + SystemID + "/mainLayout";
	}
	
	/**
	 * 서브레이아웃
	 * @param sysID
	 * @return
	 */
	public static String getSubLayoutPathBySysID(String SystemID){
		return returnPath + SystemID + "/subLayout";
	}
	
	/**
	 * 솔트값 생성
	 * @return
	 */
	public static String getSalt() {
		Random random = new Random();     
		byte[] salt = new byte[10];

		random.nextBytes(salt);     

		StringBuffer sb = new StringBuffer();

		for(int i=0; i<salt.length; i++) {
			sb.append(String.format("%02x", salt[i]));
		}     

		return sb.toString();
	}
	
	/**
	 * 사용자 Public Ip 조회
	 * @param request
	 * @return
	 */
	public static String getPublicIpByUser(HttpServletRequest request){
		String cmnuseIp = request.getHeader("X-Forwarded-For");
	 	if (cmnuseIp == null || cmnuseIp.length() == 0 || "unknown".equalsIgnoreCase(cmnuseIp)) { 
	 		cmnuseIp = request.getHeader("Proxy-Client-IP"); 
	 	} 
	 	if (cmnuseIp == null || cmnuseIp.length() == 0 || "unknown".equalsIgnoreCase(cmnuseIp)) { 
	 		cmnuseIp = request.getHeader("WL-Proxy-Client-IP"); 
	 	} 
	 	if (cmnuseIp == null || cmnuseIp.length() == 0 || "unknown".equalsIgnoreCase(cmnuseIp)) { 
	 		cmnuseIp = request.getHeader("HTTP_CLIENT_IP"); 
	 	} 
	 	if (cmnuseIp == null || cmnuseIp.length() == 0 || "unknown".equalsIgnoreCase(cmnuseIp)) { 
	 		cmnuseIp = request.getHeader("HTTP_X_FORWARDED_FOR"); 
	 	} 
	 	if (cmnuseIp == null || cmnuseIp.length() == 0 || "unknown".equalsIgnoreCase(cmnuseIp)) { 
	 		cmnuseIp = request.getRemoteAddr(); 
	 	}
	 	return cmnuseIp;
	}
	/**
	 * 예외처리
	 * @param response
	 * @param message 예외처리시 나타낼 문장
	 * @return
	 */
	public static String alertException(HttpServletResponse response, String message){
		response.setContentType("text/html; charset=UTF-8");
		response.setCharacterEncoding("utf-8");
		PrintWriter pwOut = null;
		
		try {
			pwOut = response.getWriter();
			if (message.length() > 100) { // 100자 이상의 메시지일 경우 한줄로 잘라낸다.
				StringTokenizer tokenMessage = new StringTokenizer(message, "\n");
				message = tokenMessage.nextToken();
			}
			message = strToAlert(message);
			pwOut.println("<!DOCTYPE html><html lang=\"ko\"><head><title>시스템안내</title><meta charset=\"utf-8\">");
			pwOut.println("<script language='javascript'>");
			pwOut.println("alert('" + message + "');");
			pwOut.println("history.back();");
			pwOut.println("</script>");
			pwOut.println("</head></html>");
		} catch (IOException e) {
			log.debug("CommonUtil [alertException] : "+e.getMessage());
			return null;
		} finally {
			pwOut.flush();
		}

		return null;
		
	}
	
	/**
	 * 예외처리
	 * @param response
	 * @param message 예외처리시 나타낼 문장
	 * @param url 이동할 url
	 * @return
	 */
	public static String alertException(HttpServletResponse response, String message, String url){
		response.setContentType("text/html; charset=UTF-8");
		response.setCharacterEncoding("utf-8");
		PrintWriter pwOut = null;
		
		try {
			pwOut = response.getWriter();
			if (message.length() > 100) { // 100자 이상의 메시지일 경우 한줄로 잘라낸다.
				StringTokenizer tokenMessage = new StringTokenizer(message, "\n");
				message = tokenMessage.nextToken();
			}
			message = strToAlert(message);
			pwOut.println("<!DOCTYPE html><html lang=\"ko\"><head><title>시스템안내</title><meta charset=\"utf-8\">");
			pwOut.println("<script language='javascript'>");
			pwOut.println("alert('" + message + "');");
			pwOut.println("location.href='" + url + "';");
			pwOut.println("</script>");
			pwOut.println("</head></html>");
		} catch (IOException e) {
			log.debug("CommonUtil [alertException] : "+e.getMessage());
			return null;
		} finally {
			pwOut.flush();
		}

		return null;
		
	}
	
	/**
	 * 문자열 띄어쓰기 탭 나타나게
	 * @param s 
	 * @return
	 */
	private static String strToAlert(String s) {
		if (s == null) {
			return null;
		}
		StringBuffer buf = new StringBuffer();
		char[] c = s.toCharArray();
		int len = c.length;
		for (int i = 0; i < len; i++) {
			if (c[i] == '\n') {
				buf.append("\\n");
			} else if (c[i] == '\t') {
				buf.append("\\t");
			} else if (c[i] == '"') {
				buf.append(" ");
			} else if (c[i] == '\'') {
				buf.append("\\'");
			} else if (c[i] == '\r') {
				buf.append("\\r");
			} else {
				buf.append(c[i]);
			}
		}
		return buf.toString();
	}
	
	
	/**
	 * 문자열 분리
	 * @param _strData
	 * @param _strSplit
	 * @return
	 */
	public static Vector GetEleFromString(String _strData, String _strSplit) {
		Vector	veResult = new Vector();
		String	strBuf = "";

		if(_strData == null || _strData.equals("")){
			return veResult;
		}
		for(int i=0;i<_strData.length();i++){
			if(_strSplit.equals("" + _strData.charAt(i))){
				veResult.add(strBuf);
				strBuf = "";
			}else{
				strBuf += _strData.charAt(i);
			}
		}

		veResult.add(strBuf);

		return veResult;
	}
	
	/**
	 * 문자의 세자리마다 ,를 삽입한다.<br>
	 * 즉, '1000000'를 '1,000,000'으로 만들어 준다.
	 * 
	 * @param str
	 *            원래 문자열
	 * @return ,를 삽입한 문자열
	 */
	public static String getCommaString(String str) {
	    if(str == null) return "";
	    // 소수점을 잘라둔다.
	    Vector veNum = GetEleFromString(str, ".");
	    String strExt = "";
	    if(veNum.size() > 1) {
			str 	= "" + veNum.get(0);
			strExt 	= "" + veNum.get(veNum.size()-1);
	    }

		StringBuffer sb = new StringBuffer();
		char aChar;
		int len = str.length();

		int commaIndex = (len - 1) % 3;

		for (int i = 0; i < len; i++) {
			aChar = str.charAt(i);

			sb.append(aChar);
			if (i == commaIndex && i < len - 1) {
				sb.append(',');
				commaIndex += 3;
			}
		}
		
		String strResult = sb.toString();
		if(!strExt.equals(""))
		    strResult += "." + strExt;

		return strResult;
	}
	
	/**
	 * 태그제거
	 * @param reqValue
	 * @return
	 */
	public static String getDelTag(String reqValue) {
		if(reqValue == null) {
			return "";
		}
	    String wResult = reqValue;

	    wResult = wResult.replaceAll("<(/)?([a-zA-Z]*)(\\s[a-zA-Z]*=[^>]*)?(\\s)*(/)?>", "");
	    wResult = wResult.replaceAll("<(\"[^\"]*\")*>", "");
	    wResult = wResult.replaceAll("<(\'[^\']*\')*>", "");
	    wResult = wResult.replaceAll("<([^\'\">])*>","");
	    wResult = wResult.replaceAll("<\\w+\\s+[^<]*\\s*>", "");
      	wResult = wResult.replaceAll("&[^;]+;", "");
	    
	    return  wResult;
	}
	
	/**
	 *  2013 ~ 현재년도+5 년도 리턴하기
	 */
	public static List getPdYear() {
	    Calendar wNow = Calendar.getInstance();
	    
	    //현재 +5 년도
	    int fYear = wNow.get(Calendar.YEAR)+5;
	    
	    List yearList = new ArrayList();
	    Map yearMap = new HashMap();
	    
	    for ( int y = 2013 ; y <= fYear ; y++ ) {
	    	   yearMap.put("year", y);
	    	   yearList.add(yearMap);
	    	   yearMap = new HashMap();
	    }
	    
	    return yearList;
	}
	
	  
	/**
	 *  작년 년도 리턴하기
	 */
	public static String getPreYear() {
	    StringBuffer wDate = new StringBuffer();

	    Calendar wNow = Calendar.getInstance();
	    int wYear    = wNow.get(Calendar.YEAR) - 1;
	    wDate.append(wYear);
	    
	    return wDate.toString();

	}
	
	/**
	 *  현재년도 리턴하기
	 */
	public static String getYear() {
		StringBuffer wDate = new StringBuffer();
		
		Calendar wNow = Calendar.getInstance();
		int wYear    = wNow.get(Calendar.YEAR);
		wDate.append(wYear);
		
		return wDate.toString();
		
	}
	  
	/**
	 *  현재월 리턴하기
	 */
	public static String getMonth() {
	    StringBuffer wDate = new StringBuffer();

	    Calendar wNow = Calendar.getInstance();
	    int wYear    = wNow.get(Calendar.YEAR);
	    int wMonth   = wNow.get(Calendar.MONTH) + 1;

	    wDate.append(wYear + "/");

	    if(wMonth < 10){
	    	wDate.append("0");
	    }
	    wDate.append(wMonth);

	    return wDate.toString();

	}
	
	/**
	 *  월 더하기
	 */
	public static String getAddMonth(String paraDate, int addCnt){
		
		String resultDt = "";
		
		try {
			
			SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
			Calendar cal = Calendar.getInstance();
			
			//날짜설정
			Date date = format.parse(paraDate);
	        cal.setTime(date);
	        
	        //월 더하기
	        cal.add(Calendar.MONTH, addCnt); 
	        resultDt = format.format(cal.getTime());
	        
		} catch (ParseException e) {
			log.debug("CommonUtil [alertException] : "+e.getMessage());
		}
		
		
	return resultDt;	
	}
	  
	/**
	 *  현재일 리턴하기
	 */
	public static String getDate() {
	    StringBuffer wDate = new StringBuffer();

	    Calendar wNow = Calendar.getInstance();
	    int wYear    = wNow.get(Calendar.YEAR);
	    int wMonth   = wNow.get(Calendar.MONTH) + 1;
	    int wDay     = wNow.get(Calendar.DATE);

	    wDate.append(wYear + "/");

	    if(wMonth < 10){
	    	wDate.append("0");
	    }
	    wDate.append(wMonth + "/");

	    if(wDay < 10){
	    	wDate.append("0");
	    }
	    wDate.append("" + wDay);

	    return wDate.toString();

	}
	
	/**
	 *  현재일+시간 리턴하기(구분값 없음)
	 */
	public static String getDateTime() {
	    StringBuffer wDate = new StringBuffer();

	    Calendar wNow = Calendar.getInstance();
	    int wYear    = wNow.get(Calendar.YEAR);
	    int wMonth   = wNow.get(Calendar.MONTH) + 1;
	    int wDay     = wNow.get(Calendar.DATE);
	    int wHour     = wNow.get(Calendar.HOUR);
	    int wMin     = wNow.get(Calendar.MINUTE);
	    int wSec     = wNow.get(Calendar.SECOND);

	    wDate.append("" + wYear);

	    if(wMonth < 10){
	    	wDate.append("0");
	    }
	    wDate.append("" + wMonth);

	    if(wDay < 10){
	    	wDate.append("0");
	    }
	    wDate.append("" + wDay);
	    
	    if(wHour < 10){
	    	wDate.append("0");
	    }
	    wDate.append("" + wHour);
	    
	    if(wMin < 10){
	    	wDate.append("0");
	    }
	    wDate.append("" + wMin);
	    
	    if(wSec < 10){
	    	wDate.append("0");
	    }
	    wDate.append("" + wSec);

	    return wDate.toString();

	}

	/**
	 *  현재일+24시간 리턴하기(구분값 없음)
	 */
	public static String getDate24Time() {
		StringBuffer wDate = new StringBuffer();
		
		Calendar wNow = Calendar.getInstance();
		int wYear    = wNow.get(Calendar.YEAR);
		int wMonth   = wNow.get(Calendar.MONTH) + 1;
		int wDay     = wNow.get(Calendar.DATE);
		int wHour     = wNow.get(Calendar.HOUR_OF_DAY);
		int wMin     = wNow.get(Calendar.MINUTE);
		int wSec     = wNow.get(Calendar.SECOND);
		
		wDate.append("" + wYear);
		
		if(wMonth < 10){
			wDate.append("0");
		}
		wDate.append("" + wMonth);
		
		if(wDay < 10){
			wDate.append("0");
		}
		wDate.append("" + wDay);
		
		if(wHour < 10) {
			wDate.append("0");
		}
		wDate.append("" + wHour);
		
		if(wMin < 10){
			wDate.append("0");
		}
		wDate.append("" + wMin);
		
		if(wSec < 10){
			wDate.append("0");
		}
		wDate.append("" + wSec);
		
		return wDate.toString();
		
	}
	
	/**
	 *  현재일+24시간 리턴하기(yyyy-MM-dd HH:mm:ss)
	 */
	public static String getDate24Time2() {
		StringBuffer wDate = new StringBuffer();
		
		Calendar wNow = Calendar.getInstance();
		int wYear    = wNow.get(Calendar.YEAR);
		int wMonth   = wNow.get(Calendar.MONTH) + 1;
		int wDay     = wNow.get(Calendar.DATE);
		int wHour     = wNow.get(Calendar.HOUR_OF_DAY);
		int wMin     = wNow.get(Calendar.MINUTE);
		int wSec     = wNow.get(Calendar.SECOND);
		
		wDate.append("" + wYear);
		wDate.append("-");
		if(wMonth < 10){
			wDate.append("0");
		}

		wDate.append("" + wMonth);
		wDate.append("-");
		if(wDay < 10){
			wDate.append("0");
		}
		
		wDate.append("" + wDay);
		wDate.append(" ");
		
		if(wHour < 10) {
			wDate.append("0");
		}
		wDate.append("" + wHour);
		
		wDate.append(":");
		if(wMin < 10){
			wDate.append("0");
		}
		wDate.append("" + wMin);
		
		wDate.append(":");
		if(wSec < 10){
			wDate.append("0");
		}
		wDate.append("" + wSec);
		
		return wDate.toString();
		
	}
	  
	/**
	 *  전일 리턴하기
	 */
	public static String getYestesrDate() {
	    StringBuffer wDate = new StringBuffer();

	    Calendar wNow = Calendar.getInstance();
	    int wYear    = wNow.get(Calendar.YEAR);
	    int wMonth   = wNow.get(Calendar.MONTH) + 1;
	    wNow.add(Calendar.DATE, -1);
	    int wDay     = wNow.get(Calendar.DATE);

	    wDate.append(wYear + "/");

	    if(wMonth < 10){
	    	wDate.append("0");
	    }
	    wDate.append(wMonth + "/");

	    if(wDay < 10){
	    	wDate.append("0");
	    }
	    wDate.append("" + wDay);

	    return wDate.toString();

	}
	
	/**
	 *  다음일 리턴하기
	 */
	public static String getTomorrowDate() {
		Calendar calendar = Calendar.getInstance();
		
		calendar.add(Calendar.DATE, 1);
		int month = calendar.get(calendar.MONTH)+1;
		
		String wDate = "";
		wDate += calendar.get(Calendar.YEAR) +"/";
		wDate += month < 10 ? "0"+month : month;
		wDate += "/";
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		wDate += day < 10 ? "0"+day : day;
		
		return wDate.toString();
	}
	
	/**
	 *  월 첫째날
	 */
	public static String getMonthFirst() {
		
		Calendar calendar = Calendar.getInstance();
		int month = calendar.get(calendar.MONTH)+1;
		String wDate = "";
		wDate += calendar.get(calendar.YEAR)+"/";
		wDate += month < 10 ? "0"+month : month;
		wDate += "/";
		int day = calendar.getMinimum(Calendar.DAY_OF_MONTH);
		wDate += day < 10 ? "0"+day : day;
		
		return wDate.toString();
		
	}
	
	/**
	 *  입력월 첫째날
	 */
	public static String getMonthFirst(String date) {
		
		if(date != null && !date.equals("")){
			
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(new Date(date));
			int month = calendar.get(calendar.MONTH)+1;
			String wDate = "";
			wDate += calendar.get(calendar.YEAR)+"/";
			wDate += month < 10 ? "0"+month : month;
			wDate += "/";
			int day = calendar.getMinimum(Calendar.DAY_OF_MONTH);
			wDate += day < 10 ? "0"+day : day;
			
			return wDate.toString();
		}else{
			return "";
		}
		
	}
	
	/**
	 *  월 말  날짜
	 */
	public static String getMonthEnmt() {
		
		Calendar calendar = Calendar.getInstance();
		int month = calendar.get(calendar.MONTH)+1;
		String wDate = "";
		wDate  += calendar.get(calendar.YEAR)+"/";
		wDate += month < 10 ? "0"+month : month;
		wDate += "/";
		int day = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
		wDate += day < 10 ? "0"+day : day;
		
		return wDate.toString();
		
	}
	
	/**
	 *  매월 말  날짜
	 */
	public static String getMonthEnmt(String date) {
		String dateAr[] = date.split("/");
		int year = Integer.parseInt(dateAr[0]);
		int month = Integer.parseInt(dateAr[1]);
		
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.MONTH, month-1);
		calendar.set(Calendar.DATE, 1);
		
		String wDate = "";
		wDate  += year+"/";
		wDate += month < 10 ? "0"+month : month;
		wDate += "/";
		int day = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
		wDate += day < 10 ? "0"+day : day;
		
		return wDate.toString();
	}
	
	/**
	 *  이번주 첫날(일요일)
	 */
	public static String getWeekFirst() {
		
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
		int month = calendar.get(calendar.MONTH)+1;
		String wDate = "";
		wDate += calendar.get(calendar.YEAR)+"/";
		wDate += month < 10 ? "0"+month : month;
		wDate += "/";
		int day = calendar.get(Calendar.DATE);
		wDate += day < 10 ? "0"+day : day;
		
		return wDate.toString();
		
	}
	
	/**
	 *  입력일 기준 주 첫날(일요일)
	 */
	public static String getWeekFirst(String date) {
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date(date));
		calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
		int month = calendar.get(calendar.MONTH)+1;
		String wDate = "";
		wDate += calendar.get(calendar.YEAR)+"/";
		wDate += month < 10 ? "0"+month : month;
		wDate += "/";
		int day = calendar.get(Calendar.DATE);
		wDate += day < 10 ? "0"+day : day;
		
		return wDate.toString();
		
	}
	
	/**
	 *  이번주 마지막날(토요일)
	 */
	public static String getWeekEnmt() {
		
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
		int month = calendar.get(calendar.MONTH)+1;
		String wDate = "";
		wDate += calendar.get(calendar.YEAR)+"/";
		wDate += month < 10 ? "0"+month : month;
		wDate += "/";
		int day = calendar.get(Calendar.DATE);
		wDate += day < 10 ? "0"+day : day;
		
		return wDate.toString();
		
	}
	
	/**
	 *  입력일 기준 주 마지막날(토요일)
	 */
	public static String getWeekEnmt(String date) {
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date(date));
		calendar.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
		int month = calendar.get(calendar.MONTH)+1;
		String wDate = "";
		wDate += calendar.get(calendar.YEAR)+"/";
		wDate += month < 10 ? "0"+month : month;
		wDate += "/";
		int day = calendar.get(Calendar.DATE);
		wDate += day < 10 ? "0"+day : day;
		
		return wDate.toString();
		
	}
	
	/**
	 *  입력일로 부터 num일 이후 날짜 리턴하기
	 */
	public static String getAfterDate(String bgnde, int num) {
		bgnde = bgnde.substring(0,10);
		Date currDate = new Date(bgnde);
		Calendar cal = Calendar.getInstance();
		cal.setTime(currDate);
		cal.add(Calendar.DATE, +num);
		
		int wYear    = cal.get(Calendar.YEAR);
		int wMonth   = cal.get(Calendar.MONTH) + 1;
		int wDay     = cal.get(Calendar.DATE);
		
		StringBuffer wDate = new StringBuffer();
		
		wDate.append(wYear + "/");
		
		if(wMonth < 10){
			wDate.append("0");
		}
		wDate.append(wMonth + "/");
		
		if(wDay < 10){
			wDate.append("0");
		}
		wDate.append("" + wDay);
		
		return wDate.toString();
	}
	
	/**
	 *  두 날짜 사이 날짜(List) 구하기 
	 */
	public static List getDiffOfDays(String b, String e){
		
		List diffList = new ArrayList();
		Map map = new HashMap();
		SimpleDateFormat fm = new SimpleDateFormat("yyyy/MM/dd");

		try {
			b = b.substring(0,10);
			e = e.substring(0,10);
			
			Date bDate = fm.parse(b);
			Date eDate = fm.parse(e);
			
			if(bDate.compareTo(eDate) == 0){
				map.put("bgnde", b);
				diffList.add(map);
				map = new HashMap();
			}else{
				
				long diff = eDate.getTime() - bDate.getTime();
				long diffDays = diff / (24*60*60*1000) +1;

				Calendar cal = Calendar.getInstance();
				
				for(int i=1; i<=diffDays; i++){
					if(i == 1){
						map.put("bgnde", b);
						cal.setTime(bDate);
					}else{
						
						bDate = fm.parse(b);
						cal.setTime(bDate);
						cal.add(Calendar.DATE, 1);
						b = fm.format(cal.getTime());
						map.put("bgnde", b);
					}
					diffList.add(map);
					map = new HashMap();
				}
			}
			
		} catch (ParseException e1) {
			log.error("[getDiffOfDay] "+ e1.getClass().getName() + " : " + e1.getMessage());
		}
		
		return diffList;
	}
	
	/**
	 *  두 날짜 사이 날짜, 요일(List) 구하기 
	 */
	public static List getDiffOfList(String b, String e){
		
		List diffList = new ArrayList();
		Map map = new HashMap();
		SimpleDateFormat fm = new SimpleDateFormat("yyyy/MM/dd");
		
		try {
			b = b.substring(0,10);
			e = e.substring(0,10);
			
			Date bDate = fm.parse(b);
			Date eDate = fm.parse(e);
			
			int chk = 0;
			String[] days = {"일요일", "월요일", "화요일", "수요일", "목요일", "금요일", "토요일"};
			String[] daysEngTxt = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
			String[] daysEngClass = {"sun", "mon", "tue", "wed", "thu", "fri", "sat"};
			if(bDate.compareTo(eDate) == 0){
				map.put("bgnde", b);
				diffList.add(map);
				map = new HashMap();
			}else{
				
				long diff = eDate.getTime() - bDate.getTime();
				long diffDays = diff / (24*60*60*1000) +1;
				
				Calendar cal = Calendar.getInstance();
				
				for(int i=1; i<=diffDays; i++){
					if(i == 1){
						map.put("bgnde", b);
						cal.setTime(bDate);
						chk = cal.get(Calendar.DAY_OF_WEEK);
						map.put("days", days[chk-1]);
						map.put("daysEngTxt", daysEngTxt[chk-1]);
						map.put("daysEngClass", daysEngClass[chk-1]);
					}else{
						
						bDate = fm.parse(b);
						cal.setTime(bDate);
						cal.add(Calendar.DATE, 1);
						b = fm.format(cal.getTime());
						map.put("bgnde", b);
						chk = cal.get(Calendar.DAY_OF_WEEK);
						map.put("days", days[chk-1]);
						map.put("daysEngTxt", daysEngTxt[chk-1]);
						map.put("daysEngClass", daysEngClass[chk-1]);
					}
					diffList.add(map);
					map = new HashMap();
				}
			}
			
		} catch (ParseException e1) {
			log.error("[getDiffOfDay] "+ e1.getClass().getName() + " : " + e1.getMessage());
		}
		
		return diffList;
	}
	
	/**
	 *  전월 마지막날
	 */
	public static String getLastMonthEmnt(String strDate) {
		
		Calendar calendar = Calendar.getInstance();
//		int month = calendar.get(calendar.MONTH)+2;
		calendar.setTime(new Date(strDate));
		calendar.add(Calendar.MONTH, -1);
		int month = calendar.get(calendar.MONTH)+1 ;
		String wDate = "";
		wDate += calendar.get(calendar.YEAR)+"/";
		wDate += month < 10 ? "0"+month : month;
		wDate += "/";
		int day = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
		wDate += day < 10 ? "0"+day : day;
		
		return wDate.toString();
		
	}
	
	/**
	 *  익월 첫째날
	 */
	public static String getNextMonthFirst(String strDate) {
		
		Calendar calendar = Calendar.getInstance();
//		int month = calendar.get(calendar.MONTH)+2;
		calendar.setTime(new Date(strDate));
		calendar.add(Calendar.MONTH, 1);
		int month = calendar.get(calendar.MONTH)+1 ;
		String wDate = "";
		wDate += calendar.get(calendar.YEAR)+"/";
		wDate += month < 10 ? "0"+month : month;
		wDate += "/";
		int day = calendar.getMinimum(Calendar.DAY_OF_MONTH);
		wDate += day < 10 ? "0"+day : day;
		
		return wDate.toString();
		
	}
	
	/**
	 *  요일 반환
	 */
	public static String getDays(String compDate){
		
		if(compDate == null) return "";
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
		Calendar cal = Calendar.getInstance();
		
		//날짜설정
		Date date;
		try {
			date = format.parse(compDate);
			cal.setTime(date);
		} catch (ParseException e) {
			log.debug("CommonUtil [getDays] "+e.getClass().getName()+" : "+e.getMessage());
			return "";
		}
		int days = cal.get(Calendar.DAY_OF_WEEK);
		if(days == 1){
			compDate = "일";
		}else if(days == 2){
			compDate = "월";
		}else if(days == 3){
			compDate = "화";
		}else if(days == 4){
			compDate = "수";
		}else if(days == 5){
			compDate = "목";
		}else if(days == 6){
			compDate = "금";
		}else if(days == 7){
			compDate = "토";
		}else{
			compDate = "";
		}
		
		return compDate;
		
	}
	
	/**
	 *  브라우저 정보
	 *  2022/09/22 sylee 브라우저 체크 수정(엣지, 웨일, 크롬, 파이어폭스 추가 및 수정)
	 */
	public static String getBrwsr(HttpServletRequest request) {

		// 헤더 null 값 체크
		if (request.getHeader("User-Agent") != null) {
			String header = request.getHeader("User-Agent");

			// 브라우저 체크
			if (header.indexOf("Edg") > -1) {
				return "Edge";
				
			} else if (header.indexOf("Whale") > -1) {
				return "Whale";
				
			} else if (header.indexOf("Trident") > -1 || header.indexOf("MSIE") > -1) {
				return "IE";

			} else if (header.indexOf("Chrome") > -1) {
				return "Chrome";

			} else if (header.indexOf("Safari") > -1) {
				return "Safari";

			} else if (header.indexOf("Opera") > -1) {
				return "Opera";
				
			} else if (header.indexOf("Firefox") > -1) {
				return "Firefox";
				
			} else {
				return "etc";
			}
			
		} else {
			return "etc";
		}
	}
	
	/**
	 *  브라우저 버전 정보
	 *  2022/09/22 sylee 브라우저 버전 체크 수정(엣지, 웨일, 크롬 추가 및 수정)
	 */
	public static String getBrwsrVer(HttpServletRequest request) {
		
		// 헤더 null 값 체크
		if (request.getHeader("User-Agent") != null) {

			// 헤더
			String header = request.getHeader("User-Agent");

			// edge 버전 체크
			if (header.indexOf("Edg") > -1) {
				int splitVal = header.indexOf("Edg") + 4;
				String brwsrVer = "";
				brwsrVer = header.substring(splitVal, header.length());
				return brwsrVer;

			// Whale 버전 체크
			} else if (header.indexOf("Whale") > -1) {
				int splitVal = header.indexOf("Whale") + 6;
				int splitValLast;
				if (header.indexOf("Mobile") > -1) {
					splitValLast = header.indexOf("Mobile") - 1;
				} else {
					splitValLast = header.indexOf("Safari") - 1;
				}
				String brwsrVer = "";
				brwsrVer = header.substring(splitVal, splitValLast);
				return brwsrVer;

			// Trident 버전 체크
			} else if (header.indexOf("Trident") > -1) {
				String brwsrVer = "";
				String trident = "Trident/7.0%Trident/6.0%Trident/5.0%Trident/4.0%";
				String tridentAr[] = trident.split("%");

				for (int tr = 0; tr < tridentAr.length; tr++) {
					if (header.indexOf(tridentAr[tr] + ";") > -1) {
						brwsrVer = tridentAr[tr];
					}
				}
				return brwsrVer;

			// MSIE 버전 체크
			} else if (header.indexOf("MSIE") > -1) {
				String brwsrVer = "";
				String msie = "MSIE 10.0%MSIE 9.0%MSIE 8.0%MSIE 7.0%MSIE 6.0%";
				String msieAr[] = msie.split("%");

				for (int tr = 0; tr < msieAr.length; tr++) {
					if (header.indexOf(msieAr[tr] + ";") > -1) {
						brwsrVer = msieAr[tr];
					}
				}
				return brwsrVer;

			// Chrome 버전 체크
			} else if (header.indexOf("Chrome") > -1) {
				int splitVal = header.indexOf("Chrome") + 7;
				int splitValLast;
				if (header.indexOf("Mobile") > -1) {
					splitValLast = header.indexOf("Mobile") - 1;
				} else {
					splitValLast = header.indexOf("Safari") - 1;
				}
				String brwsrVer = "";
				brwsrVer = header.substring(splitVal, splitValLast);
				return brwsrVer;

			// Firefox 버전 체크
			} else if (header.indexOf("Firefox") > -1) {
				int splitVal = header.indexOf("Firefox") + 8;
				String brwsrVer = "";
				brwsrVer = header.substring(splitVal, header.length());
				return brwsrVer;

			// Opera 체크
			} else if (header.indexOf("Opera") > -1) {
				return "Opera";

			// Safari 체크
			} else if (header.indexOf("Safari") > -1) {
				return "Safari";

			} else {
				return "etc";
			}
		}
		return "etc";
	}
	
	/**
	 *  단말기 정보
	 */
	public static String getTrmnl(HttpServletRequest request) {

		// 헤더 null 값 체크
		if (request.getHeader("User-Agent") != null) {
			String header = request.getHeader("User-Agent");

			// 단말기 체크
			if (header.indexOf("Mobile") > -1) {
				return "Mobile";
			} else {
				return "PC";
			}

		} else {
			return "etc";
		}
	}
	
	/**
	 *  OS 정보
	 */
	public static String getOs(HttpServletRequest request) {

		String header = request.getHeader("User-Agent");

		// null 값 체크
		if (header != null) {

			// 모바일 체크
			if (header.indexOf("Mobile") > -1 && header.indexOf("Android") > -1) {
				return "Android";

			} else if (header.indexOf("Mobile") > -1 && header.indexOf("iPhone") > -1) {
				return "iOS";
			}

			// os 정보 호출
			if (header.indexOf("Windows") > -1) {
				return "Windows";

			} else if (header.indexOf("Mac OS") > -1) {
				return "Mac";

			} else if (header.indexOf("Linux") > -1) {
				return "Linux";

			} else if (header.indexOf("Unix") > -1) {
				return "Unix";

			} else if (header.indexOf("Sunos") > -1 || header.indexOf("Solaris") > -1) {
				return "Solaris";

			} else {
				return "etc";
			}

		} else {
			return "none";
		}
	}
	
	/**
	 * OS 버전 확인
	 */
	public static String getOsVer(HttpServletRequest request) {
		
		// 헤더 null 값 체크
		if (request.getHeader("User-Agent") != null) {
			
			String header = request.getHeader("User-Agent");
			
			//모바일 체크
		    if (header.indexOf("Mobile") > -1 && header.indexOf("Android") > -1) {
		    	 return "Android";
		    	 
			} else if (header.indexOf("Mobile") > -1 && header.indexOf("iPhone") > -1) {
				 return "iOS";
			}
			
			if(header.indexOf("NT 10.0") != -1) 
				return "Windows 10";
			else if(header.indexOf("NT 6.1") != -1 || header.indexOf("NT 7.0") != -1) 
				return "Windows 7";
			else if(header.indexOf("NT 6.0") != -1) 
				return "Windows Vista/Server 2008";
			else if(header.indexOf("NT 5.2") != -1) 
				return "Windows Server 2003";
			else if(header.indexOf("NT 5.1") != -1) 
				return "Windows XP";
			else if(header.indexOf("NT 5.0") != -1) 
				return "Windows 2000";
			else if(header.indexOf("NT") != -1) 
				return "Windows NT";
			else if(header.indexOf("9x 4.90") != -1) 
				return "Windows Me";
			else if(header.indexOf("98") != -1)
				return "Windows 98";
			else if(header.indexOf("95") != -1) 
				return "Windows 95";
			else if(header.indexOf("Win16") != -1) 
				return "Windows 3.x";
			else if(header.indexOf("Linux") != -1) 
				return "Linux";
			else if(header.indexOf("Macintosh") != -1) 
				return "Macintosh";

		} else {
			return "etc";
		}

	 return "etc";
	}
	
	/**
	 * Reference Adress 정보
	 * @param request
	 * @return
	 */
	public static String getRefer(HttpServletRequest request) {
		
		String referer = request.getHeader("referer");
		
		if(referer == null){
			return "";
		} else {
			return referer;
		}
	}
	
    /**
     * AES 방식의 암호화
     * 
     * @param message
     * @return
     * @throws Exception
     */
    public static String encryptAes(String message) {

        // use key coss2
    	String aesSecretKey = "nForUv3AesSecret";
    	if(aesSecretKey == null) return null;
        SecretKeySpec skeySpec = new SecretKeySpec(aesSecretKey.getBytes(), "AES");

        // Instantiate the cipher
        Cipher cipher = null;
		try {
			cipher = Cipher.getInstance("AES");
		} catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
			log.debug("CommonUtil [encryptAes] "+e.getClass().getName()+" : "+e.getMessage());
		}
        try {
			cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
		} catch (InvalidKeyException e) {
			log.debug("CommonUtil [encryptAes] "+e.getClass().getName()+" : "+e.getMessage());
		}

        byte[] encrypted = null;
		try {
			encrypted = cipher.doFinal(message.getBytes());
		} catch (IllegalBlockSizeException | BadPaddingException e) {
			log.debug("CommonUtil [encryptAes] "+e.getClass().getName()+" : "+e.getMessage());
		}
        return byteArrayToHex(encrypted);
    }

    /**
     * AES 방식의 복호화
     * 
     * @param message
     * @return
     * @throws Exception
     */
    public static String decryptAes(String encrypted) {
    	
        // use key coss2
    	String aesSecretKey = "nForUv3AesSecret";
    	if(aesSecretKey == null) return null;
        SecretKeySpec skeySpec = new SecretKeySpec(aesSecretKey.getBytes(), "AES");

        Cipher cipher = null;
		try {
			cipher = Cipher.getInstance("AES");
		} catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
			log.debug("CommonUtil [decryptAes] "+e.getClass().getName()+" : "+e.getMessage());
		}
        try {
			cipher.init(Cipher.DECRYPT_MODE, skeySpec);
		} catch (InvalidKeyException e) {
			log.debug("CommonUtil [decryptAes] "+e.getClass().getName()+" : "+e.getMessage());
		}
        
        byte[] original = null;
		try {
			original = cipher.doFinal(hexToByteArray(encrypted));
		} catch (IllegalBlockSizeException | BadPaddingException e) {
			log.debug("CommonUtil [decryptAes] "+e.getClass().getName()+" : "+e.getMessage());
		}
        String originalString = new String(original);
        return originalString;
    }
    
    /**
     * hex to byte[] : 16진수 문자열을 바이트 배열로 변환한다.
     * 
     * @param hex    hex string
     * @return
     */
    public static byte[] hexToByteArray(String hex) {
        if (hex == null || hex.length() == 0) {
            return null;
        }

        byte[] ba = new byte[hex.length() / 2];
        for (int i = 0; i < ba.length; i++) {
            ba[i] = (byte) Integer.parseInt(hex.substring(2 * i, 2 * i + 2), 16);
        }
        return ba;
    }

    /**
     * byte[] to hex : unsigned byte(바이트) 배열을 16진수 문자열로 바꾼다.
     * 
     * @param ba        byte[]
     * @return
     */
    public static String byteArrayToHex(byte[] ba) {
        if (ba == null || ba.length == 0) {
            return null;
        }

        StringBuffer sb = new StringBuffer(ba.length * 2);
        String hexNumber;
        for (int x = 0; x < ba.length; x++) {
            hexNumber = "0" + Integer.toHexString(0xff & ba[x]);

            sb.append(hexNumber.substring(hexNumber.length() - 2));
        }
        return sb.toString();
    } 
    
    /**
     * Null 인 경우 "" 처리
     * @param String data
     * @return String 
     */
	public static String nvl(String data) {
        if (data == null) {
            return "";
        } else {
        	return data;
        }
    }
    
    /**
    * Null 인 경우 "" 처리
    * @param Object data
    * @return String 
    */
	public static Object nvl(Object data) {
       if (data == null) {
           return "";
       } else {
       	   return data;
       }
   }
   
	/**
    * Textarea 문자 변환( html 출력시 )
    * @param String reqValue
    * @return String 
    */
	public static String getTextareaPrint(String reqValue){
		
		if(reqValue == null) return "";
	    String wResult = reqValue;

	    wResult = wResult.replaceAll("\r\n", "<br>");
	    wResult = wResult.replaceAll("\u0020", "&nbsp;");

	    return wResult;
	}
	
	/**
	 * hwp2018 이후 복사된 문자 변환( html 출력시 )
	 * @param String reqValue
	 * @return String 
	 */
	public static String getHwpPrint(String reqValue){
		
		if(reqValue == null) return "";
		String wResult = reqValue;
		
		wResult = wResult.replaceAll("<(/)?([a-zA-Z]*)(\\s[a-zA-Z]*=[^>]*)?(\\s)*(/)?>", "");
		
		return wResult;
	}
	
	
	/**
	 *   브라우저 세팅
	 *
	 * @param request
	 * @param commonMap
	 * @param model
	 * @return 
	 */
	public static void setDisposition(String filename, HttpServletRequest request, HttpServletResponse response)
			throws IOException {

		String browser = CommonUtil.getBrwsr(request);

		String dispositionPrefix = "attachment; filename=";
		String encodedFilename = null;

		if ( browser.equals("IE")) {
			 encodedFilename = URLEncoder.encode(filename, "UTF-8").replaceAll("\\+", "%20");
			 
		} else if (browser.equals("Firefox") || browser.equals("Safari")) {
			 encodedFilename = new String(filename.getBytes("UTF-8"), "8859_1");
			 response.setHeader("Expires", "0");
			 response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
			 response.setHeader("Content-type", "application-download");
			 response.setHeader("Content-Transfer-Encoding", "binary");
			 
		} else if (browser.equals("Opera")) {
			 encodedFilename = "\"" + new String(filename.getBytes("UTF-8"), "8859_1") + "\"";
			 
		} else if (browser.equals("Chrome") || browser.equals("Edge") || browser.equals("Whale")) {
			StringBuffer sb = new StringBuffer();
			
			for ( int i = 0; i < filename.length(); i++) {
				  char c = filename.charAt(i);
				  
				if (c > '~') {
					sb.append(URLEncoder.encode(String.valueOf(c), "UTF-8"));
				} else {
					sb.append(c);
				}
			}
			encodedFilename = sb.toString();
		} else if ("Opera".equals(browser)) {
			response.setContentType("application/octet-stream;charset=UTF-8");
		
		} else {
			throw new IOException("Not supported browser");
		}
		response.setHeader("Content-Disposition", dispositionPrefix + "\"" + encodedFilename + "\"");
		
	}
	
	/**
	 * 
	 * 첨부파일 byte를 받아 [bytes,KB,MB,GB,TB]으로 변환
	 */
	public static String getByteCalcualtion(String bytes){
		String retFormat = "0";
		Double size = Double.parseDouble(bytes);
		
		String[] s = {"BYTE","KB","MB","GB","TB"};
		
		if(bytes != "0"){
			long idx = (long)Math.floor(Math.log(size) / Math.log(1024));
			DecimalFormat df = new DecimalFormat("#");
			double ret = ((size / Math.pow(1024,Math.floor(idx))));
			retFormat = df.format(ret) + " " + s[(int)idx];
		}else{
			retFormat += " " + s[0];
		}
		
		return retFormat;
	}
}
