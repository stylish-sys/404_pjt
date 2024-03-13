package egovframework.com.fof.utl;

import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

public class CommonPaging {

	// 첫페이지
	private int firstPage;
	// 마지막페이지
	private int lastPage;
	// 현재페이지
	private int currPage;
	// 이전페이지
	private int prevPage;
	// 다음페이지
	private int nextPage;
	// 페이징
	private String pageTag;
	// 전체페이지
	private int totalPage;
	// 페이징 단위
	private int pageDefaultCo = 10;

	public CommonPaging(HttpServletRequest request, int totalCo, int pageSn, int listCo, Map paramMap) {
		// 현재페이지
		currPage = pageSn;
		// 첫페이지
		firstPage = 1;
		// 마지막페이지
		totalPage = totalCo / listCo;
		int addList = totalCo % listCo;
		if (addList > 0)
			totalPage += 1;
		lastPage = totalPage;
		// 이전페이지
		prevPage = 0;
		if (currPage > pageDefaultCo) {
			if (currPage % pageDefaultCo > 0) {
				prevPage = currPage / pageDefaultCo * pageDefaultCo;
			} else {
				prevPage = (currPage / pageDefaultCo - 1) * pageDefaultCo;
			}
		}
		// 다음페이지
		nextPage = 0;
		if (lastPage > pageDefaultCo) {
			if (currPage % pageDefaultCo > 0) {
				nextPage = (currPage / pageDefaultCo + 1) * pageDefaultCo + 1;
			} else {
				nextPage = (currPage / pageDefaultCo) * pageDefaultCo + 1;
			}
			if (nextPage > lastPage) {
				nextPage = 0;
			}
		}
		// 페이징 그리기
		pageTag = createPageTag(request, totalCo, listCo, paramMap);
	}

	public String createPageTag(HttpServletRequest request, int totalCo, int listCo, Map paramMap) {
		String currentURL = request.getRequestURI();
		StringBuffer sb = new StringBuffer();

		String pagingForm = "pagingForm";
		String goPaging = "goPaging";
		String pagingHref = "javascript:";
		String currPageNm = "currPage";

		if (paramMap.containsKey("pagingFormNm")) {
			pagingForm = paramMap.get("pagingFormNm").toString();
			goPaging = "go" + paramMap.get("pagingFormNm");
		}

		if (paramMap.containsKey("pagingHref")) {
			pagingHref = "#" + paramMap.get("pagingHref").toString();
		}

		if (paramMap.containsKey("currPageNm")) {
			currPageNm = paramMap.get("currPageNm").toString();
		}

		// 1건 이상일경우 페이징 처리
		if (totalCo > 0) {
			sb.append("<form name=\"" + pagingForm + "\" id=\"" + pagingForm + "\" action=\"" + currentURL
					+ "\" method=\"post\">\n");
			sb.append("<input type=\"hidden\" name=\"" + currPageNm + "\" value=\"" + currPage + "\" />\n");
			Set<String> keySet = paramMap.keySet();

			Iterator<String> keyIterator = keySet.iterator();

			while (keyIterator.hasNext()) {
				String key = keyIterator.next();

				if (!key.equals("currPage") && !key.equals("currPageNm") && !key.equals("pagingFormNm")
						&& !key.equals("goPaging") && !key.equals(currPageNm)) {
					String value = paramMap.get(key).toString();
					
					if (value.indexOf("<") == -1 && value.indexOf(">") == -1 && key.indexOf("<") == -1
							&& key.indexOf(">") == -1) {
						sb.append("<input type=\"hidden\" name=\"" + key + "\" value=\"" + value + "\" />\n");
					}
				}
			}

			String pageClassNm = "bbs_pagerA";
			String parPageClassNm = Objects.toString(paramMap.get("pageClass"), "");
			if (!parPageClassNm.equals("")) {
				pageClassNm = parPageClassNm;
			}

			sb.append("<div class=\"" + pageClassNm + "\">\n");
			if (totalPage > pageDefaultCo) {
				int sIdx = 1;
				int eIdx = lastPage;
				if (currPage % pageDefaultCo > 0)
					sIdx = currPage / pageDefaultCo * pageDefaultCo + 1;
				else
					sIdx = (currPage / pageDefaultCo - 1) * pageDefaultCo + 1;
				eIdx = sIdx + (pageDefaultCo - 1);
				if (eIdx > lastPage)
					eIdx = lastPage;
				if (currPage == 1) {
					sb.append("<a href=\"" + pagingHref + "\" class=\"bbs_arr pgeL2\">첫 페이지</a>\n");
				} else {
					sb.append("<a href=\"" + pagingHref + "\" onclick=\"" + goPaging
							+ "(1)\" class=\"bbs_arr pgeL2\">첫 페이지</a>\n");
					sb.append("<a href=\"" + pagingHref + "\" onclick=\"" + goPaging + "(" + (currPage - 1)
							+ ")\" class=\"bbs_arr m_paging pgeL1\">이전 페이지1</a>\n");
				}
				if (prevPage > 0) {
					sb.append("<a href=\"" + pagingHref + "\" onclick=\"" + goPaging + "(" + prevPage
							+ ")\" class=\"bbs_arr w_paging pgeL1\">이전 페이지2</a>\n");
				}
				int nextIdx = 1;
				for (int idx = sIdx; idx <= eIdx; idx++) {
					if (currPage == idx) {
						sb.append("<strong class=\"bbs_pge_num\" title=\"현재페이지\">" + idx + "</strong>\n");
						nextIdx = idx + 1;
					} else {
						sb.append("<a href=\"" + pagingHref + "\" onclick=\"" + goPaging + "(" + idx
								+ ")\" class=\"bbs_pge_num\" >" + idx + "</a>\n");
					}
				}
				if (nextPage > 0) {
					sb.append("<a href=\"" + pagingHref + "\" onclick=\"" + goPaging + "(" + nextPage
							+ ")\" class=\"bbs_arr w_paging pgeR1\">다음 페이지</a>\n");
				}
				if (currPage == lastPage) {
					sb.append("<a href=\"" + pagingHref + "\" class=\"bbs_arr pgeR2\" >끝 페이지</a>\n");
				} else {
					if (lastPage >= nextIdx) {
						sb.append("<a href=\"" + pagingHref + "\" onclick=\"" + goPaging + "(" + nextIdx
								+ ")\" class=\"bbs_arr m_paging pgeR1\">다음 페이지</a>\n");
					}
					sb.append("<a href=\"" + pagingHref + "\" onclick=\"" + goPaging + "(" + lastPage
							+ ")\" class=\"bbs_arr pgeR2\" >끝 페이지</a>\n");
				}
			} else if (totalPage > 1 && totalPage < (pageDefaultCo + 1)) {
				sb.append("<a href=\"" + pagingHref + "\" onclick=\"" + goPaging
						+ "(1)\" class=\"bbs_arr pgeL2\">첫 페이지</a>\n");
				if (currPage != 1) {
					sb.append("<a href=\"" + pagingHref + "\" onclick=\"" + goPaging + "(" + (currPage - 1)
							+ ")\" class=\"bbs_arr m_paging pgeL1\">이전 페이지3</a>\n");
				}
				int nextIdx = 1;
				for (int idx = 1; idx <= lastPage; idx++) {
					if (currPage == idx) {
						sb.append("<strong class=\"bbs_pge_num\" title=\"현재페이지\">" + idx + "</strong>\n");
						nextIdx = idx + 1;
					} else {
						sb.append("<a href=\"" + pagingHref + "\" onclick=\"" + goPaging + "(" + idx
								+ ")\" class=\"bbs_pge_num\" >" + idx + "</a>\n");
					}
				}
				if (lastPage >= nextIdx) {
					sb.append("<a href=\"" + pagingHref + "\" onclick=\"" + goPaging + "(" + nextIdx
							+ ")\" class=\"bbs_arr m_paging pgeR1\">다음 페이지</a>\n");
				}
				sb.append("<a href=\"" + pagingHref + "\" onclick=\"" + goPaging + "(" + lastPage
						+ ")\" class=\"bbs_arr pgeR2\" >끝 페이지</a>\n");
			} else {
				sb.append("<strong class=\"bbs_pge_num\" title=\"현재페이지\">1</strong>\n");
			}
			sb.append("</div>\n");
			sb.append("</form>\n");

			// 교수소개 상세보기 탭 페이징처리
			String pagingscript = "<script>function " + goPaging + "(pageSn){document." + pagingForm + "." + currPageNm
					+ ".value=pageSn;document." + pagingForm + ".submit();}</script>\n";
			if (paramMap.containsKey("scriptType") && paramMap.get("scriptType").equals("prfsr")) {
				pagingscript = "<script>function " + goPaging + "(pageSn){document." + pagingForm + "." + currPageNm
						+ ".value=pageSn;prfsrPaging('" + pagingForm + "','" + goPaging.split("_")[1] + "','"
						+ goPaging.split("_")[2] + "');}</script>\n";
			}
			sb.append(pagingscript);
		}
		return sb.toString();
	}

	public int getFirstPage() {
		return firstPage;
	}

	public void setFirstPage(int firstPage) {
		this.firstPage = firstPage;
	}

	public int getLastPage() {
		return lastPage;
	}

	public void setLastPage(int lastPage) {
		this.lastPage = lastPage;
	}

	public int getCurrPage() {
		return currPage;
	}

	public void setCurrPage(int currPage) {
		this.currPage = currPage;
	}

	public int getPrevPage() {
		return prevPage;
	}

	public void setPrevPage(int prevPage) {
		this.prevPage = prevPage;
	}

	public int getNextPage() {
		return nextPage;
	}

	public void setNextPage(int nextPage) {
		this.nextPage = nextPage;
	}

	public String getPageTag() {
		return pageTag;
	}

	public int getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}

}
