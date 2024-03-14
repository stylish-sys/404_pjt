<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>


<script>
	console.log(${selectSpList});
</script>

<div class="imsi">
	<table border="1">
        <tr>
            <th>Sample SN</th>
            <th>User ID</th>
            <th>Create Date</th>
            <th>Create ID</th>
            <th>Modify Date</th>
            <th>Modify ID</th>
        </tr>
        <c:forEach var="sample" items="${selectSpList}">
            <tr>
                <td><c:out value="${sample.sampleSn}"/></td>
                <td><c:out value="${sample.userId}"/></td>
                <td><c:out value="${sample.createDt}"/></td>
                <td><c:out value="${sample.createId}"/></td>
                <td><c:out value="${sample.modifyDt}"/></td>
                <td><c:out value="${sample.modifyId}"/></td>
            </tr>
        </c:forEach>
    </table>
</div>