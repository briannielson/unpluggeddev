import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.json.*;
 
public class MapRequest extends HttpServlet {
   @Override
   public void doGet(HttpServletRequest request, HttpServletResponse response)
         throws IOException, ServletException {

      SQLWrapper r = new SQLWrapper();

      String northeast = request.getParameter("northeast");
      String southwest = request.getParameter("southwest");
      String location  = request.getParameter("location");
      String[] northeastarr = new String[2];
      String[] southwestarr = new String[2];
      String[] locationarr = new String[2];
      double[] northeastdbl = new double[2];
      double[] southwestdbl = new double[2];
      double[] locationdbl = new double[2];

      response.setContentType("application/json");
      PrintWriter out = response.getWriter();

      if (northeast.contains(",") && southwest.contains(",") && location.contains(",")) {
         northeastarr = northeast.split(",");
         southwestarr = southwest.split(",");
         locationarr  = location.split(",");

         northeastdbl[0] = Double.parseDouble(northeastarr[0]);
         northeastdbl[1] = Double.parseDouble(northeastarr[1]);
         southwestdbl[0] = Double.parseDouble(southwestarr[0]);
         southwestdbl[1] = Double.parseDouble(southwestarr[1]);
         locationdbl[0]  = Double.parseDouble(locationarr[0]);
         locationdbl[1]  = Double.parseDouble(locationarr[1]);

         r.setBounds(northeastdbl, southwestdbl, locationdbl);
	 out.print(r.runQuery().toString());
      }
      else {
	 out.print("<html><body><p>Hey I need some post data...</p></body></html>");
      }
 
      out.close();  // Always close the output writer
   }
}
