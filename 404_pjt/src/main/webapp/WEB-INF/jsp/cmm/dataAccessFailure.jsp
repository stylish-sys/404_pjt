<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<% response.setStatus(400); %>
<!DOCTYPE html>
<html lang="ko">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" >
		<meta http-equiv="content-language" content="ko">
		<meta name="dummy1" content="요청하신 페이지가 정상적으로 처리되지 않았습니다. 불편을 드려 죄송합니다."/>
		<meta name="dummy2" content="요청하신 페이지가 정상적으로 처리되지 않았습니다. 불편을 드려 죄송합니다."/>
		<meta name="dummy3" content="요청하신 페이지가 정상적으로 처리되지 않았습니다. 불편을 드려 죄송합니다."/>
		<meta name="dummy4" content="요청하신 페이지가 정상적으로 처리되지 않았습니다. 불편을 드려 죄송합니다."/>
		<title>홈페이지 오류 알림</title>

		<link rel="stylesheet" href="/00_common/css/basic.css" media="all">
		<link rel="stylesheet" href="/00_common/css/con_com.css" media="all">
		
		<script>
			function goHome() {
				location.href="/${ctx}/main.do";
			}
		</script>
	</head>
	<style type="text/css">
		body { text-align: center; opacity: 1;}
		.btnWrap { margin-bottom:80px; }
		
	</style>
	<body>
		<img src="/00_common/images/con_com/error.png" alt="요청하신 페이지가 정상적으로 처리되지 않았습니다. 불편을 드려 죄송합니다.">
		<div class="btnWrap">
			<c:if test="${!empty ctx}">
				<a href="javascript:goHome();" class="btn_gr">처음화면<i class="xi-home-o"></i></a>
			</c:if>
			<a href="javascript:history.back();" class="btn_bl">이전화면</a>
		</div>
	</body>
</html>