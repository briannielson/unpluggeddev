package UD_mapRequest;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
 
public class HelloServlet extends HttpServlet {



   @Override
   public void doGet(HttpServletRequest request, HttpServletResponse response)
         throws IOException, ServletException {

      SQLWrapper r = new SQLWrapper();
      JsonObject j = new JsonObject();

      String northeast = request.getParameter("northeast");
      String southwest = request.getParameter("southwest");
      String location  = request.getParameter("location");
      String[] northeastarr;
      String[] southwestarr;
      String[] locationarr;
      Double[] northeastdbl;
      Double[] southwestdbl;
      Double[] locationdbl;

      if (northeast.contains(",") && southwest.contains(",") && location.contains(",")) {
         northeastarr = northeast.split(",");
         southwestarr = southwest.split(",");
         locationarr  = location.split(",");

         northeastdbl = {Double.parseDouble(northeastarr[0]),Double.parseDouble(northeastarr[1])};
         southwestdbl = {Double.parseDouble(southwestarr[0]),Double.parseDouble(southwestarr[1])};
         locationdbl = {Double.parseDouble(locationarr[0]),Double.parseDouble(locationarr[1])};

         r.setBounds(northeastdbl, southwestdbl, locationdbl);

         j = r.runQuery();
      }
 
      // Set the response MIME type of the response message
      response.setContentType("application/json");
      // Allocate a output writer to write the response message into the network socket
      PrintWriter out = response.getWriter();
 
      // Write the response message, in an HTML page
      try {
         out.print(j)
         out.flush();
      } finally {
         out.close();  // Always close the output writer
      }
   }
}
