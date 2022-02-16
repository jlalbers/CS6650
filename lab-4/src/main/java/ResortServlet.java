import com.google.gson.Gson;
import com.google.gson.JsonObject;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "ResortServlet", value = "/ResortServlet")
public class ResortServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {
            Thread.sleep(1000);
        } catch (Exception e) {
            e.printStackTrace();
        }

        PrintWriter writer = response.getWriter();

        String url = request.getRequestURL().toString();
        if (url == null || url.length() == 0) {
            writer.write("{\"message\" : \"url is missing\"}");
            return;
        }

        String[] urlArray = url.split("/");
        if (!isValid(urlArray)) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            writer.write("{\"message\" : \"url is invalid\"}");
            return;
        } else {
            if (urlArray.length == 6 && urlArray[5].equals("resorts")) {
                Resorts bean = new Resorts();
                Gson gson = new Gson();
                String json = gson.toJson(bean);

                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().write(json);
            } else if (urlArray.length == 12
                    && urlArray[5].equals("resorts")
            && urlArray[7].equals("seasons")
            && urlArray[9].equals("day")) {
                JsonObject json = new JsonObject();

                // put some value pairs into the JSON object .
                json.addProperty("time", "Mission Ridge");
                json.addProperty("numSkiers", 78999);
                response.setStatus(HttpServletResponse.SC_OK);
                writer.write(json.toString());
            } else if (urlArray.length == 8
                    && urlArray[5].equals("resorts")
                    && urlArray[7].equals("seasons")) {
                Seasons season = new Seasons();
                Gson gson = new Gson();
                String json = gson.toJson(season);

                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().write(json);
            }
        }

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {
            Thread.sleep(1000);
        } catch (Exception e) {
            e.printStackTrace();
        }

        PrintWriter writer = response.getWriter();

        String url = request.getRequestURL().toString();
        if (url == null || url.length() == 0) {
            writer.write("{\"message\" : \"url is missing\"}");
            return;
        }

        String[] urlArray = url.split("/");
        if (!isValid(urlArray)) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            writer.write("{\"message\" : \"url is invalid\"}");
            return;
        } else {
            if (urlArray.length == 8
                    && urlArray[5].equals("resorts")
                    && urlArray[7].equals("seasons")) {
                writer.write("{\"message\" : \"new season is created\"}");
            }
        }
    }

    private boolean isValid (String[] urlArray) {
        return true;
    }

    public class Resorts {

        private List<InnerBean> resorts;

        class InnerBean
        {
            private String resortName = "string";
            private int resortID = 0;

        }
        public Resorts() {
            resorts = new ArrayList<>();
            resorts.add(new InnerBean());
        }
    }

    public class Seasons {
        private List<String> seasons;

        public Seasons() {
            seasons = new ArrayList<>();
            seasons.add("string");
        }
    }

}
