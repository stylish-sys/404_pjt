<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!doctype html>
<html lang="ko">

	<head>
		<meta charset="utf-8">
		<meta http-equiv="X-UA-Compatible" content="IE=edge">
		<meta name="viewport" content="width=device-width, initial-scale=1.0">
		<title> test 홈페이지 </title>
	</head>
	
	<body>
		<!-- 헤더 및 레프트-->
		<c:import url="header.jsp"/>
		<!-- //헤더 및 레프트 -->
		<c:import url="sns.jsp"/>
		<c:import url="navi.jsp"/>
		
		<!-- container -->
		<div id="container">
			<c:import url="main.jsp"/>
		</div>
		<!-- //container --> 				

		<!-- footer -->
		<c:import url="footer.jsp"/>
		<!-- // footer -->
	</body>
</html>
