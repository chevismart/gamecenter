<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>疯狂的娃娃欢迎您</title>
</head>
<body>
<a href="">加币</a>
<c:choose>
    <c:when test="${hasSubscribeBonus}">
        <a href="tryPlay.action">免费试玩</a>
    </c:when>
    <c:when test="${!hasSubscribeBonus && !isSubscribing && !hasSubscribed}">
        <a href="">关注公众号（可获试玩机会）</a>
    </c:when>
    <c:when test="${!hasSubscribeBonus && !isSubscribing && hasSubscribed}">
        <a href="">关注公众号</a>
    </c:when>
    <c:otherwise>
    </c:otherwise>
</c:choose>
<a href="scan.action">二维码扫一扫</a>

</body>
</html>