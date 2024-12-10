package servlets;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;


@WebServlet("/controller")
public class ControllerServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        handleRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        handleRequest(request, response);
    }

    private void handleRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        BufferedReader reader = request.getReader();
        StringBuilder jsonBuilder = new StringBuilder();
        String line = "";
        System.out.println(line);
        while ((line = reader.readLine()) != null) {
            jsonBuilder.append(line);
        }

        ObjectMapper mapper = new ObjectMapper();

        JsonNode jsonNode = mapper.readTree(jsonBuilder.toString());

        String x = String.valueOf(jsonNode.get("x"));
        String y = String.valueOf(jsonNode.get("y"));
        String r = String.valueOf(jsonNode.get("r"));


        if (x != null && y != null && r != null) {
            request.setAttribute("x", x);
            request.setAttribute("y", y);
            request.setAttribute("r", r);
            System.out.println(request.getAttribute("x"));
            request.getRequestDispatcher("/checkArea").forward(request, response);
        } else {
            request.getRequestDispatcher("/index.jsp").forward(request, response);
        }
    }

}