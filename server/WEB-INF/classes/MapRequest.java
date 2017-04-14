package UD_mapRequest;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.json.*;
 
public class MapRequest extends HttpServlet {
   @Override
   public void doGet(HttpServletRequest request, HttpServletResponse response)
         throws IOException, ServletException {

      SQLWrapper r = new SQLWrapper();
      JsonArray j = new JsonArray();

      String northeast = request.getParameter("northeast");
      String southwest = request.getParameter("southwest");
      String location  = request.getParameter("location");
      String[] northeastarr = new String[2];
      String[] southwestarr = new String[2];
      String[] locationarr = new String[2];
      double[] northeastdbl = new double[2];
      double[] southwestdbl = new double[2];
      double[] locationdbl = new double[2];

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

         j = r.runQuery();
      }
 
      // Set the response MIME type of the response message
      response.setContentType("application/json");
      // Allocate a output writer to write the response message into the network socket
      PrintWriter out = response.getWriter();
 
      // Write the response message, in an HTML page
      try {
         out.print(j);
         out.flush();
      } finally {
         out.close();  // Always close the output writer
      }
   }
}
