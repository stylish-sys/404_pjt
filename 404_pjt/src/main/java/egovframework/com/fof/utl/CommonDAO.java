package egovframework.com.fof.utl;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import org.egovframe.rte.psl.dataaccess.EgovAbstractMapper;
import org.springframework.stereotype.Repository;

@Repository("commonDAO")
public class CommonDAO extends EgovAbstractMapper {
	/**
	 * 단건 Object 조회
	 * 
	 * @param sqlId
	 * @param paramMap
	 * @return Object
	 * @thorws Exception
	 */

	public Object selectObject(String sqlId, Map paramMap) throws IOException, SQLException {
		return getSqlSession().selectOne(sqlId, paramMap);
	}

	/**
	 * 다건 조회 페이징
	 * 
	 * @param sqlId
	 * @param paramMap
	 * @return List
	 * @thorws Exception
	 */

	public Map selectPageList(String sqlId, Map paramMap, int listCo, HttpServletRequest request)
			throws IOException, SQLException {
		int totalCo = (int) selectObject(sqlId + "PagingCount", paramMap);
		int pageSn = 1;
		String mapPageSn = (String) paramMap.get("currPage");
		if (mapPageSn != null && !mapPageSn.equals(""))
			pageSn = Integer.parseInt(mapPageSn);
		paramMap.put("maxSn", pageSn * listCo);
		paramMap.put("minSn", (pageSn - 1) * listCo);

		Map<String, Object> returnMap = new HashMap<String, Object>();
		returnMap.put("list", getSqlSession().selectList(sqlId + "Paging", paramMap));
		CommonPaging commonPaging = new CommonPaging(request, totalCo, pageSn, listCo, paramMap);
		returnMap.put("pageTag", commonPaging.getPageTag());
		returnMap.put("totalCo", totalCo);
		int maxIdx = totalCo - (pageSn - 1) * listCo;
		returnMap.put("maxIdx", maxIdx);
		returnMap.put("totalCoComma", this.getCommaString("" + totalCo));
		returnMap.put("currPage", pageSn);
		returnMap.put("totalPage", commonPaging.getTotalPage());

		return returnMap;
	}

	/**
	 * 문자의 세자리마다 ,를 삽입한다.<br>
	 * 즉, '1000000'를 '1,000,000'으로 만들어 준다.
	 * 
	 * @param str 원래 문자열
	 * @return ,를 삽입한 문자열
	 */
	String getCommaString(String str) {
		if (str == null)
			return "";
		// 소수점을 잘라둔다.
		Vector veNum = this.GetEleFromString(str, ".");
		String strExt = "";
		if (veNum.size() > 1) {
			str = "" + veNum.get(0);
			strExt = "" + veNum.get(veNum.size() - 1);
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
		if (!strExt.equals(""))
			strResult += "." + strExt;

		return strResult;
	}

	/**
	 * 문자열 분리
	 * 
	 * @param _strData
	 * @param _strSplit
	 * @return
	 */
	Vector GetEleFromString(String _strData, String _strSplit) {
		Vector veResult = new Vector();
		String strBuf = "";

		if (_strData == null || _strData.equals("")) {
			return veResult;
		}
		for (int i = 0; i < _strData.length(); i++) {
			if (_strSplit.equals("" + _strData.charAt(i))) {
				veResult.add(strBuf);
				strBuf = "";
			} else {
				strBuf += _strData.charAt(i);
			}
		}

		veResult.add(strBuf);

		return veResult;
	}
}
