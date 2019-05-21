<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Pages</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
</head>
<body>
<div>
    <table>
        <jsp:useBean id="searchedPages" scope="request" type="java.util.List"/>
        <c:forEach items="${searchedPages}" var="item">
            <p><b>${item.title}</b><br>
            ${item.url}</p>
        </c:forEach>
    </table>
</div>
</body>
</html>
