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
    <a href="?q=<%=request.getParameter("q")%>&sortType=relevant&pageNum=1">Sort by relevant</a> | Sort by alphabet
    <%
    } else {
    %>
    Sort by relevant | <a href="?q=<%=request.getParameter("q")%>&sortType=alphabet&pageNum=1">Sort by alphabet</a>
    <%
        }
    %>
</div>
<div class="left-block">
</div>
<div class="outer-search">
    <div class="inner-search">
        <c:choose>
            <c:when test="${page.pageItems.isEmpty()}">
                <div class="outer">
                    <div class="inner">
                        <h2>No matches</h2>
                    </div>
                </div>
            </c:when>
            <c:otherwise>
                <c:forEach items="${page.pageItems}" var="item">
                    <p>
                        <a href="${item.url}"><b>${item.title}</b></a><br>
                        <a href="${item.url}"><span>${item.url}</span></a>
                    </p>
                </c:forEach>
            </c:otherwise>
        </c:choose>
    </div>
</div>
</br></br></br></br>
<c:set var="lastPage" value="${page.numberOfDocs}" />
<c:set var="firstPage" value="1" />

<div class="pagination">
    <c:if test="${currentPage != 1}">
        <a href="?q=${param.q}&sortType=${param.sortType}&pageNum=${currentPage - 1}">&laquo;</a>
    </c:if>

    <c:forEach begin="${firstPage}" end="${lastPage}" step="1" varStatus="status">
        <c:choose>
            <c:when test="${param.pageNum eq status.index}">
                <a class="active" href="?q=${param.q}&sortType=${param.sortType}&pageNum=${status.index}">${status.index}</a>
            </c:when>
            <c:otherwise>
                <a href="?q=${param.q}&sortType=${param.sortType}&pageNum=${status.index}">${status.index}</a>
            </c:otherwise>
        </c:choose>
    </c:forEach>

    <c:if test="${currentPage lt lastPage}">
        <a href="?q=${param.q}&sortType=${param.sortType}&pageNum=${currentPage + 1}">&raquo;</a>
    </c:if>
</div>
</body>
</html>
