<%@ page import="java.awt.*" %>
<%@ page import="pointManager.PointsArr" %>
<%@ page import="pointManager.PointManager" %>
<%@ page import="pointManager.DopAnswer" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="ru">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Веб лаба 2</title>
  <link rel="stylesheet" href=style.css>
  <script src=script.js defer></script>
  <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
</head>
<body>
<header>
  <H1>Лабораторная работа №2 | Веб-программирование</H1>
  <a>Морозов Ярослав Валерьевич</a><br>
  <a>Вариант №409178</a>
</header>

<table>
  <tr>
    <td><label>Выберите X (От -3 до 5):</label></td>
    <td>
      <input type="text" id="xInput">
      <div id="xError" class="error"></div>
    </td>
  </tr>
  <tr>
    <td><label>Выберите Y (От -3 до 5):</label></td>
    <td>
      <input type="text" id="yInput">
      <div id="yError" class="error"></div>
    </td>
  </tr>
  <tr>
    <td><label>Выберите R:</label></td>
    <td>
      <input type="checkbox" name="rValue" value="1">1
      <input type="checkbox" name="rValue" value="2">2
      <input type="checkbox" name="rValue" value="3">3
      <input type="checkbox" name="rValue" value="4">4
      <input type="checkbox" name="rValue" value="5">5
      <div id="rError" class="error"></div>
    </td>
  </tr>
  <tr>
    <td><button onclick="drawGraph()">Проверить точку</button></td>
  </tr>
  <tr>
    <td><canvas id="graphCanvas" width="400" height="400"></canvas></td>

  </tr>

</table>
<tfoot>
<tr>
  <td colspan="5" id="outputContainer">
    <% PointsArr points = (PointsArr) request.getSession().getAttribute("points");
      if (points == null) {
    %>
    <h4>
      <span id="notifications" class="outputStub notification">Нет результатов</span>
    </h4>
    <table id="outputTable">
      <tr>
        <th>X</th>
        <th>Y</th>
        <th>R</th>
        <th>Точка входит в ОДЗ</th>
      </tr>
    </table>
    <% } else { %>
    <h4>
      <span class="notification"></span>
    </h4>
    <table id="outputTable">
      <tr>
        <th>X</th>
        <th>Y</th>
        <th>R</th>
        <th>Точка входит в ОДЗ</th>
      </tr>
      <% for (PointManager point : points.getPoints()) { %>
      <tr>
        <td>
          <%= point.getX() %>
        </td>
        <td>
          <%= point.getY() %>
        </td>
        <td>
          <%= point.getR() %>
        </td>
        <td>
          <%= point.isInArea() ? "<span class=\"success\">Попал</span>" : "<span class=\"fail\">Промазал</span>" %>
        </td>
      </tr>
      <% } %>
    </table>
    <% } %>
  </td>
</tr>
</tfoot>
<a href="http://localhost:8080/web2-1.0-SNAPSHOT/result.jsp">Перейти к результатам</a>.

<script src="script.js"></script>


<h1>График функций</h1>
<textarea id="functionInput" rows="5" cols="40" placeholder="Введите функции, по одной на строке (например: y = x)"></textarea>
<button id="plotButton">Построить график</button>
<div id="plot"></div>
<script src="https://cdn.plot.ly/plotly-2.24.1.min.js"></script>
<script src="graph.js"></script>
<div id="result-dop">
  <p>
    <% DopAnswer answer = (DopAnswer) request.getSession().getAttribute("answer");
      if (answer != null && !answer.getAnswer().isEmpty()) {%>
       <%= answer.getAnswer() %>
    <% } %>
  </p>
</div>


</body>
</html>