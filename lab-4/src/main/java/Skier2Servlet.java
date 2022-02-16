import com.google.gson.Gson;
import com.google.gson.JsonObject;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.BufferedReader;
import java.io.IOException;

@WebServlet(name = "Skier2Servlet", value = "/Skier2Servlet")
public class Skier2Servlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        res.setContentType("application/json");
        String urlPath = req.getPathInfo();

        SkierData skierData = new SkierData();
        Gson gson = new Gson();
        String skierDataJsonString = gson.toJson(skierData);

        // check we have a URL!
        if (urlPath == null || urlPath.isEmpty()) {
            res.setStatus(HttpServletResponse.SC_NOT_FOUND);
            res.getWriter().write("missing parameters");
            return;
        }

        String[] urlParts = urlPath.split("/");

        if (!isUrlValid(urlParts)) {
            res.setStatus(HttpServletResponse.SC_NOT_FOUND);
            res.getWriter().write("\n 404: Not Found");
        } else {
            if(urlParts.length == 8){
                res.setStatus(HttpServletResponse.SC_OK);
                res.getWriter().write(skierDataJsonString);
            }else {
                res.setStatus(HttpServletResponse.SC_OK);
                JsonObject json = new JsonObject();

                // put some value pairs into the JSON object .
                json.addProperty("seasonID", 5236);
                json.addProperty("totalVert", 0);
                res.getWriter().write(json.toString());
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
        res.setContentType("text/plain");
        String urlPath = req.getPathInfo();

        // check we have a URL!
        if (urlPath == null || urlPath.isEmpty()) {
            res.setStatus(HttpServletResponse.SC_NOT_FOUND);
            res.getWriter().write("missing parameters");
            return;
        }

        String[] urlParts = urlPath.split("/");

        if (!isUrlValid(urlParts)) {
            res.setStatus(HttpServletResponse.SC_NOT_FOUND);
            res.getWriter().write("\n 404: Not Found");
        } else {
            res.setStatus(HttpServletResponse.SC_OK);
            res.getWriter().write("\n202: Accepted POST request");
            BufferedReader body = req.getReader();
            StringBuilder bodyString = new StringBuilder();
            String line;
            while( (line = body.readLine()) != null) {
                bodyString.append(line);
            }
            res.getWriter().write("\nData passed: "+ bodyString);
        }
    }

    private boolean isUrlValid(String[] urlPath) {
        // TODO: validate the request url path according to the API spec
//         urlPath  = "/1/seasons/2019/day/1/skier/123";
//         urlParts = [, 1, seasons, 2019, day, 1, skier, 123];
//        if(urlPath.length != 8)
//            return false;
//        if(!urlPath[2].equals("seasons") || !urlPath[4].equals("days") || !urlPath[6].equals("skier"))
//            return false;
//        int day = Integer.parseInt(urlPath[5]);
//        if(day < 0 && day > 32)
//            return false;
//        if (!isNumeric(urlPath[1]) || !isNumeric(urlPath[7]))
//            return false;
        return true;
    }

    public static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            double d = Double.parseDouble(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }
}
