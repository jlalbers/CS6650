import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet(name = "StatsServlet", value = "/StatsServlet")
public class StatsServlet extends HttpServlet {
    /**
     * Validates the URL for GET/POST requests to SkierServlet. URL must be in the form [WEB_APP]/skiers/{resortID}
     * /seasons/{seasonID}/days/{dayID}/skiers/{skierID}
     * @param urlPath String array containing each URL parameter between pairs of "/" (i.e. ".../skiers/1/..."
     *                 becomes [...,"skiers","1",...]
     * @return true if URL is valid, false otherwise
     */
    private boolean isUrlValid(String urlPath) {
        return urlPath == null;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        String urlPath = request.getPathInfo();

        if (isUrlValid(urlPath)) {
            response.setStatus(HttpServletResponse.SC_OK);
            // do any sophisticated processing with urlParts which contains all the url params
            // TODO: process url params in `urlParts`
            response.getWriter().write(
                    "{" +
                            "\"endpointStats\": [" +
                                "{" +
                                    "\"URL\": \"/resorts\"," +
                                    "\"operation\": \"GET\"," +
                                    "\"mean\": 11," +
                                    "\"max\": 198" +
                                "}, {" +
                                    "\"URL\": \"/resorts/{resortID}/seasons/{seasonID}/day/{dayID}/skiers\"," +
                                    "\"operation\": \"GET\"," +
                                    "\"mean\": 10," +
                                    "\"max\": 157" +
                                "}, {" +
                                    "\"URL\": \"/resorts/{resortID}/seasons\"," +
                                    "\"operation\": \"GET\"," +
                                    "\"mean\": 14," +
                                    "\"max\": 201" +
                                "}, {" +
                                    "\"URL\": \"/resorts/{resortID}/seasons/{seasonID}/day/{dayID}/skiers\"," +
                                    "\"operation\": \"GET\"," +
                                    "\"mean\": 10," +
                                    "\"max\": 157" +
                                "}]}"
            );
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"message\":\"Invalid inputs supplied\"}");
        }
    }
}
