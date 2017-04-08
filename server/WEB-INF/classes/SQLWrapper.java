package UD_mapRequest;

import java.sql.*;

public class SQLWrapper {
	
	private String driver;
	private String dbUrl;
	private String user; // Need to encrypt this info at some point
	private String pass; // REALLY need to encrypt this
	public boolean valid = true;
	private double[] upperleftbd  = {0., 0.};
	private double[] lowerrightbd = {0., 0.};
	private double[] latlong = {0., 0.};

	public SQLWrapper() {
		try {
			Class.forName(driver);
		} catch (Exception e) {
			this.valid = false;
			System.err.println("Invalid driver defined for SQL connection (FATAL)");
			System.err.println(e.getMessage());
		}
	}

	public void setBounds(double[] topleft, double[] botright, double[] location) {
		if (location.length != 2) {
			System.err.println("Invalid array sent for location");
			System.err.println("Expected array of length 2, got length " + location.length);
			return;
		}
		else if (topleft.length != 2 || botright.length != 2) {
                        System.err.println("Invalid array sent for scrnSize");
                        System.err.println("Expected array of length 2, got length " + scrnSize.length);
                        return;
                }

		System.arraycopy(topleft, 0, upperleftbd, 0, 2);
		System.arraycopy(botright, 0, lowerrightbd, 0, 2);
		System.arraycopy(location, 0, latlong, 0, 2);

		return;
	}

	private Connection getConnection() throws SQLException {
		Connection con = null;
		Properties
	} 
}
