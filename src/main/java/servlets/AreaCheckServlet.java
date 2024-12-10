package servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import pointManager.PointManager;
import pointManager.PointsArr;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import com.google.gson.*;

@WebServlet("/checkArea")
public class AreaCheckServlet extends HttpServlet {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        System.out.println("Привет от сервлета проверки попадания");
        Gson gson = new Gson();

        String xStr = (String) request.getAttribute("x");
        String yStr = (String) request.getAttribute("y");
        String rStr = (String) request.getAttribute("r");
        System.out.println(xStr);
        if (xStr == null || yStr == null || rStr == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Не все параметры были переданы");
            return;
        }

        try {
            System.out.println("проВВеРРка");
            System.out.println(rStr);
            System.out.println(xStr);
            System.out.println(yStr);
            double x = Double.parseDouble(xStr.replaceAll("\"", ""));
            double y = Double.parseDouble(yStr.replaceAll("\"", ""));
            double r = Double.parseDouble(rStr);

            System.out.println("Получены значения: x = " + x + ", y = " + y + ", r = " + r);

            PointManager point = new PointManager(x, y, r);

            HttpSession session = request.getSession();
            PointsArr points = (PointsArr) session.getAttribute("points");

            if (points == null) {
                points = new PointsArr();
                session.setAttribute("points", points);
            }

            points.addPoint(point);

            session.setAttribute("points", points);

            Map<String, Object> jsonResponse = new HashMap<>();
            jsonResponse.put("x", point.getX());
            jsonResponse.put("y", point.getY());
            jsonResponse.put("r", point.getR());
            jsonResponse.put("isInArea", point.isInArea());

            PrintWriter out = response.getWriter();
            out.print(gson.toJson(jsonResponse));
            out.flush();

        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Некорректный формат данных. X, Y и R должны быть числами.");
            System.err.println("Ошибка парсинга чисел: " + e.getMessage());
        }
    }
}