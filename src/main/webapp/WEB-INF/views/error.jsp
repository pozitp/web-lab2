<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en_US">
<head>
    <meta charset="UTF-8">
    <title>Application error</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/styles/app.css">
</head>
<body>
<main class="screen">
    <div class="banner">+----------------+
| ERROR CONSOLE  |
+----------------+</div>
    <div class="box">
        <div class="box-title">details</div>
        <p>Status code: <c:out value="${requestScope['jakarta.servlet.error.status_code']}"/></p>
        <p>Message:
            <c:choose>
                <c:when test="${not empty requestScope['jakarta.servlet.error.message']}">
                    <c:out value="${requestScope['jakarta.servlet.error.message']}"/>
                </c:when>
                <c:otherwise>
                    Unknown error.
                </c:otherwise>
            </c:choose>
        </p>
        <form method="post" action="${pageContext.request.contextPath}/controller" style="display: inline;">
            <button type="submit" class="button">Back to form</button>
        </form>
    </div>
</main>
</body>
</html>
