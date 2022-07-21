package Project;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
public class Taxi
{
	public Connection getCon() throws ClassNotFoundException, SQLException
	{
		Connection con= null;
		Class.forName("com.mysql.cj.jdbc.Driver");
		con =DriverManager.getConnection("jdbc:mysql://localhost:3306/booking","root","Tamilchandru@9600");
		if(con!=null)
		{
			System.out.println("Connected!!");
		}
		return con;
	}

     static int taxicount = 0; // taxi number
     int id;
     boolean booked; //whether the taxi booked or not
     char currentSpot; //where taxi is now
     int freeTime; // when taxi becomes free(freetaxis)
     int totalEarnings; // total earnings of taxi
     List<String> trips; // all details of all trips by this taxi
     
    
    public Taxi()
    {
        booked = false;
        currentSpot = 'A';//start point A
        freeTime = 6;//example 6 AM
        totalEarnings = 0;
        trips = new ArrayList<String>();
        taxicount = taxicount + 1; // Everytime new taxi is created a new id will be assigned
        id = taxicount;
    }

    public void setDetails(boolean booked,char nextSpot,int freeTime,int totalEarnings,String tripDetail)
    {
            this.booked = booked;
            this.currentSpot = nextSpot;
            this.freeTime = freeTime;
            this.totalEarnings = totalEarnings;
            this.trips.add(tripDetail);
    }

	/*
	 * public void printDetails() { //print all trips details
	 * System.out.println("Taxi - "+ this.id + " Total Earnings = " +
	 * this.totalEarnings); System.out.
	 * println("TaxiID    BookingID    CustomerID    From    To    PickupTime    DropTime    Amount"
	 * ); for(String trip : trips) { System.out.println(id + "          " + trip); }
	 * System.out.println(
	 * "--------------------------------------------------------------------------------------"
	 * ); }
	 */
    public void printTaxiDetails() throws ClassNotFoundException, SQLException
    {
        //print total earning and taxi details like current location and free time
    	Connection con =getCon();
		PreparedStatement ps = null;
		ps =con.prepareStatement("SELECT * FROM details;");
		ResultSet rs = ps.executeQuery();
		if(rs.next())
		{
			
			System.out.println(rs.getInt("customerid") + "\t\t" + rs.getInt("id") + "\t\t" + rs.getString("pickupPoint") +  "\t\t" + rs.getString("dropPoint") + "\t\t" + rs.getString("pickupTime") );
		}
		
        //System.out.println("Taxi - "+ this.id + " Total Earnings - " + this.totalEarnings + " Current spot - " + this.currentSpot +" Free Time - " + this.freeTime);
    }
    

    
    
    
}