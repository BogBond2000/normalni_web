package servlets;

import jakarta.jms.Session;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.*;
import java.util.*;
import com.google.gson.*;
import pointManager.DopAnswer;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

@WebServlet("/dop")
public class CheckAreaDopServlet extends HttpServlet {

    private String alp = "0123456789";
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        BufferedReader reader = request.getReader();
        Gson gson = new Gson();
        Map<String, Object> requestData = gson.fromJson(reader, Map.class);
        String answer = "";
        HttpSession session = request.getSession();

        List<String> functions = (List<String>) requestData.get("functions");

        double x = (double) requestData.get("x");
        double y;
        if(requestData.get("y") == null){
            y = 0;
        }
        else{
            y = (double) requestData.get("y");
        }

        String xStr = (x < 0) ? "(" + String.valueOf(x) + ")" : String.valueOf(x);

        List<Double> valuesFunc = new ArrayList<>();
        List<String> results = new ArrayList<>();

        for (String func : functions) {
            func = func.replaceAll("x", xStr);
            func = func.replaceAll(" ", "").replaceAll("y=", "");
            double doubleX = evaluateExpression(func);
            valuesFunc.add(doubleX);
        }
        double maxY = Collections.max(valuesFunc);
        System.out.println(maxY);
        double minY = Collections.min(valuesFunc);
        System.out.println(minY);

        double intersectionY;
        double intersectionX;

        if(maxY >= y && y >= minY){
            int indexMax = valuesFunc.indexOf(maxY);
            int indexMin = valuesFunc.indexOf(minY);

            String maxFunc = functions.get(indexMax);
            String minFunc = functions.get(indexMin);

            maxFunc = maxFunc.replaceAll(" ", "").replaceAll("y=", "");
            minFunc = minFunc.replaceAll(" ", "").replaceAll("y=", "");

            Map<String, Double> coefficients_max = CalcCoef(maxFunc);
            Map<String, Double> coefficients_min = CalcCoef(minFunc);
            double a =0 ;
            double b=0;
            double c=0;
            double d=0;


            if (coefficients_max!= null & coefficients_min != null) {
                a = coefficients_max.get("a");
                b = coefficients_max.get("b");
                c = coefficients_min.get("a");
                d = coefficients_min.get("b");
            } else {
                Map<String, String> jsonResponse = new HashMap<>();
                jsonResponse.put("answer", "ошибка в коэффициентах");

                PrintWriter out = response.getWriter();
                out.print(gson.toJson(jsonResponse));
                out.flush();
            }



            double kA = d - b;
            double kB = a -c  ;
            double ansX = kA/kB;

            maxFunc = functions.get(indexMax);
            maxFunc = maxFunc.replaceAll(" ", "").replaceAll("y=", "").replaceAll("x", String.valueOf(ansX));
            System.out.println("Interception");
            System.out.println(maxFunc);

            intersectionY = evaluateExpression(maxFunc);
            System.out.println(intersectionY);
            intersectionX = ansX;

            maxFunc = functions.get(indexMax);
            minFunc = functions.get(indexMin);

            functions.remove(maxFunc);
            functions.remove(minFunc);

            for (String func : functions) {
                func = func.replaceAll(" ", "").replaceAll("y=", "");
                double b1 = 0;
                double c1 = 0;
                Map<String, Double> coeffs = CalcCoef(func);

                if (coeffs != null) {
                    b1 = coeffs.get("a");
                    c1 = coeffs.get("b");
                } else {
                    Map<String, String> jsonResponse = new HashMap<>();
                    jsonResponse.put("answer", "ошибка в коэффициентах");

                    PrintWriter out = response.getWriter();
                    out.print(gson.toJson(jsonResponse));
                    out.flush();
                }

                System.out.println("eq coeff");

                System.out.println(b1);
                System.out.println(c1);

                System.out.println("checks");


                boolean firstCheck = (-1*intersectionY + b1 * intersectionX + c1 >= 0) && (-1*y + b1 * x + c1 >= 0);
                System.out.println(firstCheck);
                System.out.println(intersectionY + b1 * intersectionX + c1);
                System.out.println(y + b1 * x + c1);
                boolean secCheck = (-1* intersectionY + b1 * intersectionX + c1 <= 0) && (-1 *y + b1 * x + c1 <= 0);
                System.out.println(secCheck);
                if ((firstCheck || secCheck) && y <= maxY && y >= minY) {
                    answer = "попал";
                } else {
                    answer = "не попал";
                }


            }
        } else {
            answer = "не попал";
        }

        Map<String, String> jsonResponse = new HashMap<>();
        jsonResponse.put("answer", answer);

        PrintWriter out = response.getWriter();
        out.print(gson.toJson(jsonResponse));
        out.flush();
    }


    private static double evaluateExpression(String expression) {
        Expression exp = new ExpressionBuilder(expression).build();
        return exp.evaluate();
    }
    private static Map<String, Double> CalcCoef(String expression) {
        Map<String, Double> coefficients = new HashMap<>();
        expression = expression.replace(" ", "");

        double a = 1, b = 0;
        int xIndex = expression.indexOf('x');

        if (xIndex != -1) {
            String aStr = expression.substring(0, xIndex).replace("*", "");
            if (!aStr.isEmpty() && !aStr.equals("+")) {
                if (aStr.equals("-")) {
                    a = -1;
                } else {
                    a = isValidDouble(aStr) ? Double.parseDouble(aStr) : 0;
                }
            }

            if (xIndex + 1 < expression.length()) {
                String bStr = expression.substring(xIndex + 1);
                b = isValidDouble(bStr) ? Double.parseDouble(bStr) : 0;
            }

            coefficients.put("a", a);
            coefficients.put("b", b);
        } else {
            return null;
        }

        return coefficients;
    }

    private static boolean isValidDouble(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}