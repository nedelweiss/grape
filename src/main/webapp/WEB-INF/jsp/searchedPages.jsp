<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Pages</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <link href="css/searchedPages.css" rel="stylesheet" type="text/css">
    <link href="css/main.css" rel="stylesheet" type="text/css">
</head>
<body>
<div class="outer">
    <div class="inner">
        <c:choose>
            <c:when test="${searchedPages.size() == 0}">
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
</body>
</html>
