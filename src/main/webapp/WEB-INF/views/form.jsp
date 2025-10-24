<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<!DOCTYPE html>
<html lang="en_US">
<head>
    <meta charset="UTF-8">
    <title>Web Lab #2 - Point Hit Checker</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/styles/app.css">
</head>
<body>
<main class="screen">
    <div class="banner">+--------------------------------------------------------------+
| WEB LAB 2 | VARIANT 6422                                     |
| STUDENT   | MUKHAMEDYAROV ARTUR ALBERTOVICH | GROUP P3209    |
+--------------------------------------------------------------+</div>
    <div class="box">
        <div class="box-title">input console</div>
        <div class="grid">
            <div class="grid-item">
                <form id="hit-form" method="post" action="${pageContext.request.contextPath}/area-check" novalidate>
                    <fieldset>
                        <legend>X coordinate</legend>
                        <div class="choices">
                            <c:forEach var="xValue" items="${fn:split('-4,-3,-2,-1,0,1,2,3,4', ',')}">
                                <label class="choice">
                                    <input type="radio" name="x" value="${xValue}"
                                           <c:if test="${formValues['x'] == xValue}">checked</c:if>>
                                    <span>${xValue}</span>
                                </label>
                            </c:forEach>
                        </div>
                    </fieldset>

                    <fieldset>
                        <legend>Y coordinate</legend>
                        <label for="y-input"></label><input
                                class="field-input"
                                type="text"
                                id="y-input"
                                name="y"
                                placeholder="Allowed range: -5 ... 3"
                                value="<c:out value='${formValues["y"]}'/>"
                                autocomplete="off"
                                inputmode="decimal">
                    </fieldset>

                    <fieldset>
                        <legend>Radius R</legend>
                        <div class="choices">
                            <c:forEach var="rValue" items="${fn:split('1,2,3,4,5', ',')}">
                                <label class="choice">
                                    <input type="radio" name="r" value="${rValue}"
                                           <c:if test="${formValues['r'] == rValue}">checked</c:if>>
                                    <span>${rValue}</span>
                                </label>
                            </c:forEach>
                        </div>
                    </fieldset>

                    <div class="submit-row">
                        <button type="submit" class="button">Submit request</button>
                        <p class="selection" id="selection-readout">Selected: none</p>
                    </div>

                    <div class="alert" id="client-errors" role="alert" hidden tabindex="-1">
                        <h3>Please fix input values</h3>
                        <ul></ul>
                    </div>

                    <c:if test="${not empty errors}">
                        <div class="alert" role="alert">
                            <h3>Server side validation</h3>
                            <ul>
                                <c:forEach var="error" items="${errors}">
                                    <li><c:out value="${error}"/></li>
                                </c:forEach>
                            </ul>
                        </div>
                    </c:if>
                </form>
            </div>

            <div class="grid-item">
                <fieldset>
                    <legend>Area preview</legend>
                    <div class="plot" id="interactive-plot">
                        <svg viewBox="-140 -140 280 280" data-plot role="img" aria-label="Target area">
                            <g class="axis">
                                <line x1="-130" y1="0" x2="130" y2="0"></line>
                                <line x1="0" y1="130" x2="0" y2="-130"></line>
                            </g>
                            <g class="area">
                                <path class="area-shape" d="M0,0 L60,0 L60,-120 L0,-120 Z"></path>
                                <path class="area-shape" d="M0,0 L-120,0 L0,120 Z"></path>
                                <path class="area-shape" d="M 0,0 L 0,60 A 60 60 0 0 0 60,0 Z"></path>
                            </g>
                            <g class="axis-labels">
                                <text class="axis-label" x="122" y="-6">X</text>
                                <text class="axis-label" x="6" y="-122">Y</text>
                            </g>
                            <g id="history-points"></g>
                            <circle id="selected-point" r="6" hidden></circle>
                        </svg>
                    </div>
                    <p class="plot__note">Click sets coordinates when R is chosen. Submit via the button.</p>
                </fieldset>
            </div>
        </div>
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
            <tbody id="history-body">
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

<script src="${pageContext.request.contextPath}/resources/scripts/app.js" defer></script>
</body>
</html>
