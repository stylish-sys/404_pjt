<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<% response.setStatus(400); %>
<!DOCTYPE html>
<html lang="ko">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" >
		<meta http-equiv="content-language" content="ko">
		<title>홈페이지 오류 알림</title>

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
		ERROR
	</body>
</html>