<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ page import="java.util.*"%>
<%@ page import="java.net.*"%>
<%@ page import="java.util.Enumeration"%>

=======================================================
<br />
<%
		Enumeration se = session.getAttributeNames();

		while (se.hasMoreElements()) {
			String getse = se.nextElement() + "";
			out.println("@@@@@@@ session : " + getse + " : " + session.getAttribute(getse) + " <br /><br />");
		}
%>
<br />
=======================================================
