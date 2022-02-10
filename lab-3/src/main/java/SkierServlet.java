import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

/**
 * This servlet class handles "/skiers/" requests in accordance with the
 * <a href="https://app.swaggerhub.com/apis/cloud-perf/SkiDataAPI/1.16#/info">SkiDataAPI</a> specification.
 */
@WebServlet(name = "SkierServlet", value = "/SkierServlet")
public class SkierServlet extends HttpServlet {

    /**
     * Validates the URL for GET/POST requests to SkierServlet. URL must be in the form [WEB_APP]/skiers/{resortID}
     * /seasons/{seasonID}/days/{dayID}/skiers/{skierID}
     * @param urlParts String array containing each URL parameter between pairs of "/" (i.e. ".../skiers/1/..."
     *                 becomes [...,"skiers","1",...]
     * @return true if URL is valid, false otherwise
     */
    private boolean isUrlValid(String[] urlParts) {
        if (urlParts.length != 8) {
            return false;
        }
        try {
            // Validate resortID
            Integer.parseInt(urlParts[1]);
            // Validate /seasons/ path
            boolean isSeasons = urlParts[2].equalsIgnoreCase("seasons");
            // Validate /days/ path
            boolean isDays = urlParts[4].equalsIgnoreCase("days");
            // Validate dayID
            int dayID = Integer.parseInt(urlParts[5]);
            boolean validDay = dayID > 0 && dayID < 367;
            // Validate /skiers/ path
            boolean isSkiers = urlParts[6].equalsIgnoreCase("skiers");
            // Validate skierID
            Integer.parseInt(urlParts[7]);
            // Return "true" if valid url
            return isSeasons && isDays && validDay && isSkiers;
        // Catch invalid integer resortID, dayID, skierID
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Handles GET requests to SkierServlet. Returns the LiftRide information for a skier in a particular day.
     * @param request GET request to the servlet
     * @param response response issued by the servlet
     * @throws ServletException to be implemented
     * @throws IOException if there is a system issue with the
     * <a href="https://docs.oracle.com/javaee/7/api/javax/servlet/ServletResponse.html#getWriter--">response writer</a>
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        String urlPath = request.getPathInfo();

        // check we have a URL!
        if (urlPath == null || urlPath.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response.getWriter().write("{\"message\":\"Data not found\"}");
            return;
        }

        String[] urlParts = urlPath.split("/");
        // and now validate url path and return the response status code
        // (and maybe also some value if input is valid)

        if (isUrlValid(urlParts)) {
            response.setStatus(HttpServletResponse.SC_OK);
            // do any sophisticated processing with urlParts which contains all the url params
            // TODO: process url params in `urlParts`
            response.getWriter().write("8675309");
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"message\":\"Invalid inputs supplied\"}");
        }
    }

    /**
     * Handles POST requests to SkierServlet. Updates the server with a LiftRide record.
     * @param request POST request to the servlet
     * @param response response issued by the servlet
     * @throws ServletException to be implemented
     * @throws IOException if there is a system issue with the
     * <a href="https://docs.oracle.com/javaee/7/api/javax/servlet/ServletResponse.html#getWriter--">response writer</a>
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        String urlPath = request.getPathInfo();
        // check we have a URL!
        if (urlPath == null || urlPath.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response.getWriter().write("{\"message\":\"Data not found\"");
            return;
        }
        // Split URL path
        String[] urlParts = urlPath.split("/");
        // and now validate url path and return the response status code
        // (and maybe also some value if input is valid)
        if (isUrlValid(urlParts)) {
            response.setStatus(HttpServletResponse.SC_CREATED);
            // do any sophisticated processing with urlParts which contains all the url params
            // TODO: process url params in `urlParts`
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"message\":\"Invalid inputs supplied\"}");
        }
    }
}
