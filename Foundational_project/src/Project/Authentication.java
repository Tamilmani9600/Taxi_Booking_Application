package Project;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class Authentication
{
	final static int n=0;
	public static Connection getCon() throws ClassNotFoundException, SQLException
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
    public static void bookTaxi(int customerID,char pickupPoint,char dropPoint,int pickupTime,List<Taxi> freeTaxis) throws ClassNotFoundException, SQLException
    {
        // to find nearest
        int min = 999;
        //distance between pickup and drop
        int distance2 = 0;
        //this trip earning
        int earning = 0;
        //when taxi will be free next
        int nextfreeTime = 0;
        //where taxi is after trip is over
        char nextSpot = 7;
        //booked taxi
        Taxi bookedTaxi = null;
        //all details of current trip as string
        String tripDetail = "";

        for(Taxi t : freeTaxis)
        {
            int distance1 = Math.abs((t.currentSpot - 0) - (pickupPoint - 0)) * 15;
            if(distance1 < min)
            {
                bookedTaxi = t;
                //distance between pickup and drop = (drop - pickup) * 15KM
                distance2 = Math.abs((dropPoint - 0) - (pickupPoint - 0)) * 15;
                //trip earning = 100 + (distanceBetweenpickUpandDrop-5) * 10
                earning = (distance2-5) * 10 + 100;
                
                //drop time calculation
                int dropTime  = pickupTime + distance2/15;
                
                //when taxi will be free next
                nextfreeTime = dropTime;

                //taxi will be at drop point after trip is finished
                nextSpot = dropPoint;

                // creating trip detail
                tripDetail = customerID + "               " + customerID + "          " + pickupPoint +  "      " + dropPoint + "       " + pickupTime + "          " +dropTime + "           " + earning;
                min = distance1;
            }
            
        }

        //setting corresponding details to allotted taxi
        bookedTaxi.setDetails(true,nextSpot,nextfreeTime,bookedTaxi.totalEarnings + earning,tripDetail);
        //BOOKED SUCCESSFULLY
        Connection con =getCon();
		PreparedStatement ps =null;
		ps=con.prepareStatement("insert into details(earning) values =? ");
		ps.setInt(1,earning);
		ps.executeUpdate();
        System.out.println(earning);
        
        System.out.println("Taxi " + bookedTaxi.id + " booked");
        System.out.println("\n");

    }

    public static List<Taxi> createTaxis(int n)
    {
        List<Taxi> taxis = new ArrayList<Taxi>();
        // create taxis
        for(int i=1 ;i<=n;i++)
        {
            Taxi t = new Taxi();
            taxis.add(t);
        }
        return taxis;
    }

    public static List<Taxi> getFreeTaxis(List<Taxi> taxis,int pickupTime,char pickupPoint)
    {
        List<Taxi> freeTaxis = new ArrayList<Taxi>();
        for(Taxi t : taxis)
        {   
            if(t.freeTime <= pickupTime && (Math.abs((t.currentSpot - 0) - (pickupPoint - 0)) <= pickupTime - t.freeTime))
            freeTaxis.add(t);

        }
        return freeTaxis;
    }

    public void login() throws ClassNotFoundException, SQLException,IOException
	{
    	{
    		try  {
    			System.out.println("\t LOGIN");
    			System.out.println("***********");
    			
    			BufferedReader t1 = new BufferedReader(new InputStreamReader(System.in));
				System.out.println("Enter  the username: ");
				String set = t1.readLine();
				System.out.println("Enter  the password: ");
				String pas = t1.readLine();
				
				Connection con =getCon();
				PreparedStatement ps = null;
				ps =con.prepareStatement("SELECT Username,Password0 FROM User1 WHERE Username = ? and Password0 = ?");
				ps.setString(1, set);
				
				ps.setString(2, pas);
				ResultSet rs = ps.executeQuery();
				System.out.println(rs);
				if(rs.next())
				{
				    System.out.println("Authentication Successful");
				    System.out.println("\n");
				}
				else
				{
				    System.out.println("\n  Authentication Failed");
				    System.out.println("   please do recheck it ");
				    
				}
    		}
catch(Exception e) {
	e.printStackTrace();
}
        }

    }
    public void Register() throws SQLException, ClassNotFoundException
	{
    	
		try  {
			System.out.println("\t REGISTER");
			System.out.println("*********");
			Scanner s = new Scanner(System.in);
			System.out.println("Enter your Username: ");
			String username = s.nextLine();
			System.out.println("Enter your Password: ");
			String password0 = s.nextLine();
			System.out.println("Enter your PhoneNo: ");
			String phoneNo = s.nextLine();
			Connection con =getCon();
			PreparedStatement ps =null;
			ps=con.prepareStatement("insert into User1 values(?,?,?)");
			ps.setString(1, username);
			ps.setString(2, password0);
			ps.setString(3, phoneNo);
			System.out.println(ps);
			ps.executeUpdate();
			
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		}
    public void book() throws ClassNotFoundException, SQLException
    {

    	System.out.println("\t BOOK");
    	System.out.println("**********");
    	List<Taxi> taxis = createTaxis(n);
    	try  {
    		Scanner s1 = new Scanner(System.in);
			int id =1;

			int customerID =id;
			System.out.println("\n");
			System.out.println("*booking Enabled*");
			System.out.println("\n");
			System.out.println("Enter Pickup point");
			char pickupPoint = s1.next().charAt(0);
			String s=Character.toString(pickupPoint);
			System.out.println("Enter Drop point");
			char dropPoint = s1.next().charAt(0);
			String S1=Character.toString(dropPoint);
			System.out.println("Enter Pickup time");
			int pickupTime = s1.nextInt();
			Connection con =getCon();
			PreparedStatement ps =null;
			ps=con.prepareStatement("insert into details(customerID,id,pickupPoint,dropPoint,pickupTime)values(?,?,?,?,?)");
			ps.setInt(1,customerID);
			ps.setInt(2, id);
			ps.setString(3, s);
			ps.setString(4,S1);
			ps.setInt(5, pickupTime);
			System.out.println(ps);
			ps.executeUpdate();

			//check if pickup and drop points are valid
			if(pickupPoint < 'A' || dropPoint > 'F' || pickupPoint > 'F' || dropPoint < 'A')
			{
			    System.out.println("Valid pickup and drop are A, B, C, D, E, F. Exitting");
			    return;
			}
			// get all freed taxis that can reach customer on or before pickup time
			List<Taxi> freeTaxis = getFreeTaxis(taxis,pickupTime,pickupPoint);

			//no free taxi means we cannot allot, exit!
			if(freeTaxis.size() == 0)
			{
			    System.out.println("No Taxi can be alloted. Exitting");
			    return;
			}    

			//sort taxis based on earnings 
			 Collections.sort(freeTaxis,(a,b)->a.totalEarnings - b.totalEarnings); 
			

			//print free Taxi nearest to us
			bookTaxi(id,pickupPoint,dropPoint,pickupTime,freeTaxis);
			
			
			id++;		   
			
			}
    	catch(Exception e) {
    		e.printStackTrace();
    	}
    	
    }
    public void Payment_Details()
    {
    		System.out.println("\tPAYMENT DETAILS");
    		System.out.println("************");
    		Scanner as = new Scanner(System.in);
			System.out.println("Payment method");
			System.out.println("please selcet COT(Cash On Trip)");
			String payment = as.nextLine();
			if(payment.equals("COT"))
			{
				System.out.println("Cash On trip is Selected");
				
			}
			else
			{
				System.out.println("payment cannot be taken");
				System.out.println("please select only cash ");
			}
    }
    public void printTaxiDetails() throws ClassNotFoundException, SQLException
    {
        //print total earning and taxi details like current location and free time
    	Connection con =getCon();
		PreparedStatement ps = null;
		ps =con.prepareStatement("SELECT * FROM details");
		ResultSet rs = ps.executeQuery();
		if(rs.next())
		{
			System.out.println("customerID      "+"id       "+"pickupPoint       "+"dropPoint       "+"pickupTime       "+" Earnings       ");
			System.out.println(rs.getInt("customerID") + "\t\t" + rs.getInt("id") + "\t\t" + rs.getString("pickupPoint") +  "\t\t" + rs.getString("dropPoint") + "\t\t" + rs.getString("pickupTime") );
		}
		
        //System.out.println("Taxi - "+ this.id + " Total Earnings - " + this.totalEarnings + " Current spot - " + this.currentSpot +" Free Time - " + this.freeTime);
    }
  
		
    
    public static void main(String[] args) throws ClassNotFoundException, SQLException,IOException
    {

    	Authentication db = new Authentication();
        try {
        	Scanner scan = new Scanner(System.in);
        	while(true)
			{
        		System.out.println(" \n      CAB booking");
        		System.out.println("***********");
        		System.out.println("\n");
				System.out.println("\n*Select option*");
				System.out.println("login");
				System.out.println("Register");
				System.out.println("Book");
				System.out.println("Details");
				System.out.println("Payment Details");
				String choice = scan.nextLine();
      
			switch(choice){ 
			case "login":
			{

			    if (choice.equals("login"))
			    {
			    	
			    	db.login();
			    	}
			    break;
			    }
			case "Register":
			{
				if(choice.equals("Register"))
				{
					db.Register();
					System.out.println("                     Registation sucessfull");
					System.out.println("////////////////////////////////////////////////////////////////////////////");
					System.out.println("                       please login now ");	
			    	db.login();
					}
				
				else {
					System.out.println("please enter valid option");
					}
				}
			break;
			
			case "Book":
			{
				if(choice.equals("Book"))
				{
					db.book();
					}
				break;
				}
			case "Details":
			{
			        db.printTaxiDetails();
			     System.out.println("\n");
			    break;
			    }
			case "Payment Details":
			{
				if(choice.equals("Payment Details"))
				{
					db.Payment_Details();
					}
				break;
				}
			}


			}
        	
        
		}
        catch(Exception e) {
        	e.printStackTrace();
        }
       
    }
}