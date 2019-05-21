<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Pages</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <link href="css/searchedPages.css" rel="stylesheet" type="text/css">
</head>
<body>
<div class="outer">
    <div class="inner">
        <jsp:useBean id="searchedPages" scope="request" type="java.util.List"/>
        <c:forEach items="${searchedPages}" var="item">
            <a href="${item.url}" >
                <p>
                    <b>${item.title}</b><br>
                    <span>${item.url}</span>
                </p>
            </a>
        </c:forEach>
    </div>
</div>
</body>
</html>
