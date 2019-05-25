<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<html>
<head>
    <title>Pages</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <link href="css/searchedPages.css" rel="stylesheet" type="text/css">
    <link href="css/main.css" rel="stylesheet" type="text/css">
</head>
<body>
<div class="right-block">
    <%
        if("alphabet".equals(request.getParameter("sortType"))) {
    %>
    <a href="?q=<%=request.getParameter("q")%>&sortType=relevant">Sort by relevant</a> | Sort by alphabet
    <%
        } else {
    %>
    Sort by relevant | <a href="?q=<%=request.getParameter("q")%>&sortType=alphabet">Sort by alphabet</a>
    <%
        }
    %>

</div>
<div class="left-block">
</div>
<div class="outer">
    <div class="inner">
        <c:choose>
            <c:when test="${searchedPages.isEmpty()}">
                <div class="parent">
                    <div class="child">
                        <h2>No matches</h2>
                    </div>
                </div>
            </c:when>
            <c:otherwise>
                <jsp:useBean id="searchedPages" scope="request" type="java.util.List"/>
                <c:forEach items="${searchedPages}" var="item">
                    <p>
                        <a href="${item.url}"><b>${item.title}</b></a><br>
                        <a href="${item.url}"><span>${item.url}</span></a>
                     </p>
                </c:forEach>
            </c:otherwise>
        </c:choose>
    </div>
</div>
<div>
</div>
</body>
</html>
