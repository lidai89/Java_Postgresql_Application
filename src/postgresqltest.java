import java.sql.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.io.*;
import java.util.Map;
import java.lang.Object;
import java.util.*;
import java.sql.Date;
public class postgresqltest {
   //constructor
	public Connection c = null;
    public Statement stmt = null;
   public postgresqltest(){
	   
       try{
	   Class.forName("org.postgresql.Driver");
       c = DriverManager
          .getConnection("jdbc:postgresql://localhost:5432/testdb",
          "ricedb", "lidai19890416");
       System.out.println("Opened database successfully");

       
       
       
       //read in sql scripts
       Statement st = c.createStatement();
       try{
		   Scanner infile = new Scanner(new File("schemas.sql"));
		   infile.useDelimiter("\\Z");
		   String sql = infile.next();
		   st.executeUpdate(sql);
		   }
	       catch(Exception ex){
				System.out.println(ex);
				}
	       }
       catch (Exception err) {
 	      err.printStackTrace();
 	    }}
   
   //read user specified csv file
   public void readcsv(String path){
       
       try{
           Statement st = c.createStatement();
           String currentTable="";
           String query="";
           Scanner scanner = new Scanner(new File(path));
           //scanner.useDelimiter("\n");
           while(scanner.hasNextLine()){
        	   query=scanner.nextLine();
               //System.out.print(query+"\n");
               String[] params=query.split(",");
               if(params[0].length()>0){
            	   //System.out.println(params[0]);
            	   //System.out.println(params[0].substring(0, 1));
            	   String test=params[0].substring(0, 1);
            	   if(test.equals("*")){
            		   currentTable=params[0].substring(1);
            		   //System.out.println(params[0]);
            	   }
            	   else{
            		   insert_params(currentTable,params);
            	   }
               }
               
           }
           
           scanner.close();
   		   
       }
       catch(Exception e){System.out.println(e);}
   }
   //insert values into tables
   public void test_show_leg(){
	   try{
	   Statement st = c.createStatement();
	   System.out.println("Insertion Over");
       st.execute("select * from Org" );
       ResultSet rs = st.getResultSet();
       while (rs.next()) {
     	    System.out.println("leg= " + rs.getString(2));}}
	   catch(Exception e){System.out.println(e);}
   }
   public void insert_params(String tablename, String[] params){
	   
	   if(tablename.equals("Org")){insert_org(params);}
	   if(tablename.equals("Leg"))insert_leg(params);
	   if(tablename.equals("Stroke"))insert_stroke(params);
	   if(tablename.equals("Distance"))insert_distance(params);
	   if(tablename.equals("Meet"))insert_meet(params);
	   if(tablename.equals("Participant"))insert_participant(params);
	   if(tablename.equals("Event"))insert_event(params);
	   if(tablename.equals("Strokeof"))insert_strokeof(params);
	   if(tablename.equals("Heat"))insert_heat(params);
	   if(tablename.equals("Swim"))insert_swim(params);
	   
   }
   //insert orgs
   public void insert_org(String[] params){
	   try{
	   CallableStatement cstmt = c.prepareCall("{CALL InsertOrg(?,?,?)}");
       
       cstmt.setString(1, params[0]);
       cstmt.setString(2, params[1]);
       cstmt.setBoolean(3, params[2].equals("TRUE"));
       cstmt.executeUpdate();
       }
	   catch(Exception e){
		   System.out.println(e);
	   }
   }
   //insert legs
   public void insert_leg(String[] params){
	   try{
		   CallableStatement cstmt = c.prepareCall("{CALL InsertLeg(?)}");
	       cstmt.setInt(1, Integer.parseInt(params[0]));
	       cstmt.executeUpdate();
	       }
		   catch(Exception e){
			   System.out.println(e);
		   }
   }
   
   //insert stroke
   public void insert_stroke(String[] params){
	   try{
		   CallableStatement cstmt = c.prepareCall("{CALL InsertStroke(?)}");
	       
	       cstmt.setString(1, params[0]);
	       cstmt.executeUpdate();
	       }
		   catch(Exception e){
			   System.out.println(e);
		   }
   }
   //insert distance
   public void insert_distance(String[] params){
	   try{
		   CallableStatement cstmt = c.prepareCall("{CALL InsertDistance(?)}");
	       
	       cstmt.setInt(1, Integer.parseInt(params[0]));
	       cstmt.executeUpdate();
	       }
		   catch(Exception e){
			   System.out.println(e);
		   }
   }
   //insert meet
   public void insert_meet(String[] params){
	   try{
		   CallableStatement cstmt = c.prepareCall("{CALL InsertMeet(?,?,?,?)}");
		   //DateFormat df = new SimpleDateFormat("MM/dd/yyyy"); 
		   Date startDate =  Date.valueOf(params[1]);
	       cstmt.setString(1, params[0]);
	       cstmt.setDate(2, startDate);
	       cstmt.setInt(3, Integer.parseInt(params[2]));
	       cstmt.setString(4, params[3]);
	       cstmt.executeUpdate();
	       }
		   catch(Exception e){
			   System.out.println(e);
		   }
   }
   //insert participant
   public void insert_participant(String[] params){
	   try{
		   CallableStatement cstmt = c.prepareCall("{CALL InsertParticipant(?,?,?,?)}");
	       
	       cstmt.setString(1, params[0]);
	       cstmt.setString(2, params[1]);
	       cstmt.setString(3, params[2]);
	       cstmt.setString(4, params[3]);
	       cstmt.executeUpdate();
	       }
		   catch(Exception e){
			   System.out.println(e);
		   }
	   
   }
   //insert event
   public void insert_event(String[] params){
	   try{
		   CallableStatement cstmt = c.prepareCall("{CALL InsertEvent(?,?,?)}");
	       
	       cstmt.setString(1, params[0]);
	       cstmt.setString(2, params[1]);
	       cstmt.setInt(3, Integer.parseInt(params[2]));
	       cstmt.executeUpdate();
	       }
		   catch(Exception e){
			   System.out.println(e);
		   }
	   
   }
   //insert strokeof
   public void insert_strokeof(String[] params){
	   try{
		   CallableStatement cstmt = c.prepareCall("{CALL InsertStrokeOf(?,?,?)}");
	       
	       cstmt.setString(1, params[0]);
	       cstmt.setInt(2, Integer.parseInt(params[1]));
	       cstmt.setString(3, params[2]);
	       cstmt.executeUpdate();
	       }
		   catch(Exception e){
			   System.out.println(e);
		   }
	   
   }
   //insert heat
   public void insert_heat(String[] params){
	   try{
		   CallableStatement cstmt = c.prepareCall("{CALL InsertHeat(?,?,?)}");
	       
	       cstmt.setString(1, params[0]);
	       cstmt.setString(2, params[1]);
	       cstmt.setString(3, params[2]);
	       cstmt.executeUpdate();
	       }
		   catch(Exception e){
			   System.out.println(e);
		   }
	   
   }
   //insert swim
   public void insert_swim(String[] params){
	   try{
		   CallableStatement cstmt = c.prepareCall("{CALL InsertSwim(?,?,?,?,?,?)}");
	       
	       cstmt.setString(1, params[0]);
	       cstmt.setString(2, params[1]);
	       cstmt.setString(3, params[2]);
	       cstmt.setString(4, params[3]);
	       cstmt.setInt(5, Integer.parseInt(params[4]));
	       cstmt.setFloat(6, Float.parseFloat(params[5]));
	       cstmt.executeUpdate();
	       }
		   catch(Exception e){
			   System.out.println(e);
		   }
	   
   }
   //part 2 display selected sheet
   public String display_sheet(String type, String[] params){
	   if (type.equals("DisplayHeat")){return display_heat(params);}
	   else if (type.equals("DisplayHeatOfMeetPlayer")){return display_heatofmeetplayer(params);}
	   else if (type.equals("DisplayHeatOfMeetSchool")){return display_heatofmeetschool(params);}
	   else if (type.equals("DisplayNameBySchool")){return display_namebyschool(params);}
	   else if (type.equals("DisplayResultByEventMeet")){return display_resultbyeventmeet(params);}
	   else if (type.equals("DisplayRank")){return display_rank(params);}
	   else return "";
   }
   //display heat
	public String display_heat( String[] params){
		String result="Heat_ID Event_ID Meet_Name Participant_ID Name Org_ID R_Timt R_Relay_Time R_Rank\n";
		try{
			   CallableStatement cstmt = c.prepareCall("{CALL DisplayHeat(?)}");
		       
		       cstmt.setString(1, params[0]);
		       cstmt.executeUpdate();
		       ResultSet rs = cstmt.getResultSet();
		       while (rs.next()) {
		     	    result=result+rs.getString(1)+ " "+ rs.getString(2)+ " "+ rs.getString(3)+ " "+ rs.getString(4)+ " "
		     	    		+ rs.getString(5)+ " "+ rs.getString(6)+ " "+String.valueOf(rs.getFloat(7))+String.valueOf(rs.getFloat(8))+
		     	    		String.valueOf(rs.getInt(9)+"\n");}
		       }
		
			   catch(Exception e){
				   System.out.println(e);
			   }  
		return result;
   }
	public String display_heatofmeetplayer( String[] params){
		   
		String result="Heat_ID Event_ID Meet_Name Participant_ID Name Org_ID R_Timt R_Relay_Time R_Rank\n";
		try{
			   CallableStatement cstmt = c.prepareCall("{CALL DisplayHeatOfMeetPlayer(?,?)}");
		       
		       cstmt.setString(1, params[0]);
		       cstmt.setString(1, params[1]);
		       cstmt.executeUpdate();
		       ResultSet rs = cstmt.getResultSet();
		       while (rs.next()) {
		     	    result=result+rs.getString(1)+ " "+ rs.getString(2)+ " "+ rs.getString(3)+ " "+ rs.getString(4)+ " "
		     	    		+ rs.getString(5)+ " "+ rs.getString(6)+ " "+String.valueOf(rs.getFloat(7))+String.valueOf(rs.getFloat(8))+
		     	    		String.valueOf(rs.getInt(9)+"\n");}
		       }
		
			   catch(Exception e){
				   System.out.println(e);
			   }  
		return result;
   }
	public String display_heatofmeetschool( String[] params){
		   
		String result="Heat_ID Event_ID Meet_Name Participant_ID Name Org_ID R_Timt R_Relay_Time R_Rank\n";
		try{
			   CallableStatement cstmt = c.prepareCall("{CALL DisplayHeatOfMeetSchool(?,?)}");
		       
		       cstmt.setString(1, params[0]);
		       cstmt.setString(1, params[1]);
		       cstmt.executeUpdate();
		       ResultSet rs = cstmt.getResultSet();
		       while (rs.next()) {
		     	    result=result+rs.getString(1)+ " "+ rs.getString(2)+ " "+ rs.getString(3)+ " "+ rs.getString(4)+ " "
		     	    		+ rs.getString(5)+ " "+ rs.getString(6)+ " "+String.valueOf(rs.getFloat(7))+String.valueOf(rs.getFloat(8))+
		     	    		String.valueOf(rs.getInt(9)+"\n");}
		       }
		
			   catch(Exception e){
				   System.out.println(e);
			   }  
		return result;
   }
	public String display_namebyschool( String[] params){
		   
		String result="Participant_Name\n";
		try{
			   CallableStatement cstmt = c.prepareCall("{CALL DisplayNameBySchool(?,?)}");
		       
		       cstmt.setString(1, params[0]);
		       cstmt.setString(1, params[1]);
		       cstmt.executeUpdate();
		       ResultSet rs = cstmt.getResultSet();
		       while (rs.next()) {
		     	    result=result+rs.getString(1)+"\n";}
		       }
		
			   catch(Exception e){
				   System.out.println(e);
			   }  
		return result;
   }
	public String display_resultbyeventmeet( String[] params){
		   
		String result="Heat_ID Swimmer_Name Rank\n";
		try{
			   CallableStatement cstmt = c.prepareCall("{CALL DisplayResultByEventMeet(?,?)}");
		       
		       cstmt.setString(1, params[0]);
		       cstmt.setString(2, params[1]);
		       cstmt.executeUpdate();
		       ResultSet rs = cstmt.getResultSet();
		       while (rs.next()) {
		     	    result=result+rs.getString(1)+" "+rs.getString(2)+" "+String.valueOf(rs.getInt(3))+"\n";}
		       }
		
			   catch(Exception e){
				   System.out.println(e);
			   }  
		return result;
   }
	public String display_rank( String[] params){
		   
		String result="Meet_Name Org_ID Org_Name Org_Score Rank\n";
		try{
			   CallableStatement cstmt = c.prepareCall("{CALL DisplayRank(?)}");
		       
		       cstmt.setString(1, params[0]);
		       cstmt.executeUpdate();
		       ResultSet rs = cstmt.getResultSet();
		       while (rs.next()) {
		     	    result=result+rs.getString(1)+" "+rs.getString(2)+" "+rs.getString(3)+" "+String.valueOf(rs.getInt(4))+" "+String.valueOf(rs.getInt(5))+"\n";}
		       }
		
			   catch(Exception e){
				   System.out.println(e);
			   }  
		return result;
   }
	
	//save tables into csv
	public void savecsv(String path){
		try{
		FileWriter file=new FileWriter(path);
		String[] tables={"Org","Leg","Stroke","Distance","Meet","Participant","Event","StrokeOf","Heat","Swim"};
		for(String s : tables){
			file.append("*"+s+"\n");
			Statement cstmt=c.createStatement();
			//cstmt.executeUpdate();
			ResultSet rs=cstmt.executeQuery("Select * FROM "+s);
			ResultSetMetaData rsmd = rs.getMetaData();
		    int numberOfColumns = rsmd.getColumnCount();
			while(rs.next()){
				for(int i=1;i<=numberOfColumns;i++)
				{
					file.append(rs.getString(i));
					file.append(",");
				}
				file.append("\n");
			}
		}
		file.close();}
		catch(Exception e){
			System.out.println(e);
		}
	}
   public static void main( String args[] )
     {
       Connection c = null;
       Statement stmt = null;
       System.out.println("test");
       System.out.println("PostgreSQL JDBC Driver Registered!");


         
       
       try {
    	   
//    	      final Runtime runtime = Runtime.getRuntime();
//    	      final String command = "psql -U ricedb -d testdb -f schemas.sql"; // cmd.exe
//
//    	      final Process p = runtime.exec(command, null, new File("."));
//
//    	      PrintWriter writer = new PrintWriter(new OutputStreamWriter(new BufferedOutputStream(p.getOutputStream())), true);
//              
//              /* Read the output of command prompt */
//              BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
//              String line = reader.readLine();
//              /* Read upto end of execution */
//              while (line != null) {
//                  /* Pass the value to command prompt/user input */
//                  writer.println("08-08-2014");
//                  System.out.println(line);
//                  line = reader.readLine();
//              }
//              /* The stream obtains data from the standard output stream of the process represented by this Process object. */
//              BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
//              /* The stream obtains data from the error output stream of the process represented by this Process object. */
//              BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));
//              
//              String Input;
//              while ((Input = stdInput.readLine()) != null) {
//                  System.out.println(Input);
//              }            
//              
//              String Error;
//              while ((Error = stdError.readLine()) != null) {
//                  System.out.println(Error);
//              }
              
              
              Class.forName("org.postgresql.Driver");
              c = DriverManager
                 .getConnection("jdbc:postgresql://localhost:5432/testdb",
                 "ricedb", "lidai19890416");
              System.out.println("Opened database successfully");

              
              
              
              //show if above statement works
              Statement st = c.createStatement();
              try{
       		   Scanner infile = new Scanner(new File("schemas.sql"));
       		   infile.useDelimiter("\\Z");
       		   String sql = infile.next();
       		   //System.out.println(sql);
       		   //while(infile.hasNext()){
       		   st.executeUpdate(sql);
       		   //System.out.println(sql);
       		   //}
       		   
       		   }
              catch(Exception ex){
       			System.out.println(ex);
       			}
              
              CallableStatement cstmt = c.prepareCall("{CALL InsertLeg(?)}");
              
              cstmt.setInt(1, 3);
             // cstmt.setString(2, "RiceU");
              //cstmt.setBoolean(3, true);
              cstmt.executeUpdate();
              cstmt.setInt(1, 2);
              // cstmt.setString(2, "RiceU");
               //cstmt.setBoolean(3, true);
               cstmt.executeUpdate();
              
              st.execute("select * from Leg" );
              ResultSet rs = st.getResultSet();
              while (rs.next()) {
            	    System.out.println("Leg= " + rs.getInt(1));}
              st.close();
              cstmt.close();
       		}
       	    
    	    catch (Exception err) {
    	      err.printStackTrace();
    	    }
       System.out.println("Table created successfully");
       
     }
}
