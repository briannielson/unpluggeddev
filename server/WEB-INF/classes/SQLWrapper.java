package UD_mapRequest;

import java.sql.*;
import javax.json.*;

public class SQLWrapper {
	
	private String driver = "com.mysql.jdbc.Driver";
	private String dbUrl = "jdbc:mysql://localhost";
	private String user = "root"; // Need to encrypt this info at some point
	private String pass = "unplugged"; // REALLY need to encrypt this
	public boolean valid = true;
	private double[] upperleftbd  = {0., 0.};
	private double[] lowerrightbd = {0., 0.};
	private double[] latlong = {0., 0.};

	public SQLWrapper() {
		try {
			Class.forName(driver).newInstance();
		} catch (Exception e) {
			this.valid = false;
			System.err.println("Invalid driver defined for SQL connection (FATAL)");
			System.err.println(e.getMessage());
		}
	}

	public void setBounds(double[] northeast, double[] southwest, double[] location) {
		if (location.length != 2) {
			System.err.println("Invalid array sent for location");
			System.err.println("Expected array of length 2, got length " + location.length);
			return;
		}
		else if (northeast.length != 2 || southwest.length != 2) {
                        System.err.println("Invalid array sent for scrnSize");
                        return;
                }

		System.arraycopy(northeast, 0, upperleftbd, 0, 2);
		System.arraycopy(southwest, 0, lowerrightbd, 0, 2);
		System.arraycopy(location, 0, latlong, 0, 2);

		return;
	}

	public JsonObject runQuery() {
		JsonArrayBuilder arrBuilder = Json.createArrayBuilder();
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
		    conn = DriverManager.getConnection(dbUrl + "?user=" + this.user + "&password=" + this.pass);
		} catch (SQLException ex) {
		    // handle any errors
		    System.err.println("SQLException: " + ex.getMessage());
		    System.err.println("SQLState: " + ex.getSQLState());
		    System.err.println("VendorError: " + ex.getErrorCode());
		}

		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery(this.getQuery());

			while (rs.next()) {
				arrBuilder.add(Json.createObjectBuilder().
					add("id", rs.getInt("id")).
					add("date", rs.getDate("date").toString()).
					add("venue", rs.getString("venue")).
					add("artist", rs.getString("artist")).
					add("latitude", rs.getFloat("latitude")).
					add("longitude", rs.getFloat("longitude"))
				);
			}
		}
		catch (SQLException ex){
			// handle any errors
			System.err.println("SQLException: " + ex.getMessage());
			System.err.println("SQLState: " + ex.getSQLState());
			System.err.println("VendorError: " + ex.getErrorCode());
		}
		finally {
			if (rs != null) {
			    try {
			        rs.close();
			    } catch (SQLException sqlEx) { }
			    rs = null;
			}
			if (stmt != null) {
			    try {
			        stmt.close();
			    } catch (SQLException sqlEx) { }
			    stmt = null;
			}
		}
		return arrBuilder.build();
	}

	private String getQuery() {
		return "select * from performances left join venues on performances.venue_id = venues.id left join artists on performances.artist_id = artists.id where latitude between " 
			+ upperleftbd[0] + " and " + lowerrightbd[0]
			+ " and longitude between " + upperleftbd[1] + " and " + lowerrightbd[1] + ";";
	}
}
