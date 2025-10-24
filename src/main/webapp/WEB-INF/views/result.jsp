<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en_US">
<head>
    <meta charset="UTF-8">
    <title>Point check result</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/styles/app.css">
</head>
<body>
<main class="screen">
    <div class="banner">+-------------------------------+
| WEB LAB 2 RESULT CONSOLE      |
+-------------------------------+</div>
    <div class="box">
        <div class="box-title">latest calculation</div>
        <c:choose>
            <c:when test="${not empty result}">
                <table>
                    <thead>
                    <tr>
                        <th>X</th>
                        <th>Y</th>
                        <th>R</th>
                        <th>Hit</th>
                        <th>Timestamp</th>
                        <th>Processing time, ms</th>
                    </tr>
                    </thead>
                    <tbody>
                    <tr>
                        <td><c:out value="${result.getXFormatted()}"/></td>
                        <td><c:out value="${result.getYFormatted()}"/></td>
                        <td><c:out value="${result.getRFormatted()}"/></td>
                        <td><c:out value="${result.isHit() ? 'Yes' : 'No'}"/></td>
                        <td class="timestamp" data-timestamp="<c:out value='${result.processedAt}'/>"><c:out
                                value="${result.processedAt}"/></td>
                        <td><c:out value="${result.getProcessingTimeFormatted()}"/></td>
                    </tr>
                    </tbody>
                </table>
            </c:when>
            <c:otherwise>
                <p class="note">No data available. Submit a point from the main form.</p>
            </c:otherwise>
        </c:choose>
        <p class="note">Data lives in application scope.</p>
        <form method="post" action="${pageContext.request.contextPath}/controller" style="display: inline;">
            <button type="submit" class="button">Back to form</button>
        </form>
    </div>

    <div class="box">
        <div class="box-title">history</div>
        <p class="note">Newest entries are on top. Data lives in an application scoped bean.</p>
        <table>
            <thead>
            <tr>
                <th>X</th>
                <th>Y</th>
                <th>R</th>
                <th>Hit</th>
                <th>Timestamp</th>
                <th>Processing time, ms</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="item" items="${results}" varStatus="status">
                <tr>
                    <td><c:out value="${item.getXFormatted()}"/></td>
                    <td><c:out value="${item.getYFormatted()}"/></td>
                    <td><c:out value="${item.getRFormatted()}"/></td>
                    <td><c:out value="${item.isHit() ? 'Yes' : 'No'}"/></td>
                    <td class="timestamp" data-timestamp="<c:out value='${item.processedAt}'/>"><c:out
                            value="${item.processedAt}"/></td>
                    <td><c:out value="${item.getProcessingTimeFormatted()}"/></td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
</main>

<script>
    function formatTimestamps() {
        document.querySelectorAll('.timestamp').forEach(element => {
            const timestamp = element.getAttribute('data-timestamp');
            if (timestamp) {
                try {
                    const date = new Date(timestamp);
                    element.textContent = date.toLocaleString();
                } catch (e) {
                }
            }
        });
    }

    document.addEventListener('DOMContentLoaded', formatTimestamps);
</script>
</body>
</html>
