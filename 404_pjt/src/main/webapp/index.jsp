<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%
	// server info
	String serverName = (String)request.getServerName();
	// port info 
	int serverPort = request.getServerPort();
	// domn info(scheme limit)
	String serverNames = serverName;
	if(serverPort>80 && serverPort != 443){
		serverNames += ":"+serverPort;
	}
	request.setAttribute("sNm", serverNames);
%>

<!DOCTYPE html>
<html lang="ko">
	<head>
		<title>Main</title>  
		<script type="text/javascript">
			location.href="/sampleList";
		</script>
	</head>
	<body>
	</body>
</html>
