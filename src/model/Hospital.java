package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class Hospital {

	//A common method to connect to the DB 
		private Connection connect() {
			Connection con = null;
			
			try {
				 Class.forName("com.mysql.jdbc.Driver");
				 //Provide the correct details: DBServer/DBName, username, password 
				 con= DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/healthcare?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC", "root", "");

				//For testing          
				 System.out.print("Successfully connected");
				 
			}catch(Exception e) {
				e.printStackTrace();
			}
			
			return con; 
		}
		
		public String readAppointment() {  
			String output = "";  
			
			try {  
				Connection con = connect();  
				if (con == null)  {   
					return "Error while connecting to the database for reading.";  
				} 

				// Prepare the html table to be displayed   
				output = "<table border='1'><tr><th>hosName</th>"
						+ "<th>location</th><th>email</th>"
				
						+ "<th>Update</th><th>Remove</th></tr>";


				  String query = "select * from hospital";   
				  Statement stmt = con.createStatement();   
				  ResultSet rs = stmt.executeQuery(query); 

				  // iterate through the rows in the result set   
				  while (rs.next())   {  

					  	String appID = Integer.toString(rs.getInt("hospitalID"));
						String fullName = rs.getString("hosName");
						String address = rs.getString("location");
		
						String email = rs.getString("email");
					
					  // Add into the html table    

					  output += "<tr><td><input id='hidAppIDUpdate' name='hidAppIDUpdate' type='hidden' value='" + appID + "'>" + fullName + "</td>"; 

					  output += "<td>" + address + "</td>";
						output += "<td>" + email + "</td>";
						
						
					
					// buttons     
					  output += "<td><input name='btnUpdate' type='button' value='Update' class='btnUpdate btn btn-secondary'></td>"
					  		+ "<td><input name='btnRemove' type='button' value='Remove' class='btnRemove btn btn-danger' data-hospitalID='"+ appID +"'>"+"</td></tr>";

					} 
				  
				  con.close(); 

				  // Complete the html table   
				  output += "</table>"; 
				}
				catch (Exception e) {  
					output = "Error while reading the Appointment.";  
					System.err.println(e.getMessage()); 
				}

				return output;
			}
		
		//Insert appointment
		public String insertAppointment(String hosName, String location, String email) {
			String output = "";

			try {
				Connection con = connect();  

				if (con == null) {
					return "Error while connecting to the database";
				}

				// create a prepared statement   
				String query = " insert into hospital (`hospitalID`,`hosName`,`location`,`email`)"+" values (?, ?, ?, ?)";


				PreparedStatement preparedStmt = con.prepareStatement(query);

				// binding values 
				preparedStmt.setInt(1, 0);
				preparedStmt.setString(2, hosName);
				preparedStmt.setString(3, location);
				preparedStmt.setString(4, email);
		
				
				//execute the statement   
				preparedStmt.execute();   
				con.close(); 

				//Create JSON Object to show successful msg.
				String newAppointment = readAppointment();
				output = "{\"status\":\"success\", \"data\": \"" + newAppointment + "\"}";
			}
			catch (Exception e) {  
				//Create JSON Object to show Error msg.
				output = "{\"status\":\"error\", \"data\": \"Error while Inserting Appointment.\"}";   
				System.err.println(e.getMessage());  
			} 

			 return output; 
		}
		
		//Update appointment
		public String updateAppointment(String hospitalID, String hosName, String location, String email)  {   
			String output = ""; 
		 
		  try   {   
			  Connection con = connect();
		 
			  if (con == null)    {
				  return "Error while connecting to the database for updating."; 
			  } 
		 
		   // create a prepared statement    
			   String query = "UPDATE hospital SET hosName=?,location=?,email=? WHERE hospitalID=?";
				 
		   PreparedStatement preparedStmt = con.prepareStatement(query); 
		 
		   // binding values    
		   preparedStmt.setString(1, hosName);
		   preparedStmt.setString(2, location);
			preparedStmt.setString(3, email);
			
			preparedStmt.setInt(4, Integer.parseInt(hospitalID));
		   
		 
		   // execute the statement    
		   preparedStmt.execute();    
		   con.close(); 
		 
		   //create JSON object to show successful msg
		   String newAppointment = readAppointment();
		   output = "{\"status\":\"success\", \"data\": \"" + newAppointment + "\"}";
		   }   catch (Exception e)   {    
			   output = "{\"status\":\"error\", \"data\": \"Error while Updating Appointment Details.\"}";      
			   System.err.println(e.getMessage());   
		   } 
		 
		  return output;  
		  }
		
		public String deleteAppointment(String hospitalID) {  
			String output = ""; 
		 
		 try  {   
			 Connection con = connect();
		 
		  if (con == null)   {    
			  return "Error while connecting to the database for deleting.";   
		  } 
		 
		  // create a prepared statement   
		  String query = "DELETE FROM hospital WHERE hospitalID=?"; 
		 
		  PreparedStatement preparedStmt = con.prepareStatement(query); 
		 
		  // binding values   
		  preparedStmt.setInt(1, Integer.parseInt(hospitalID));       
		  // execute the statement   
		  preparedStmt.execute();   
		  con.close(); 
		 
		  //create JSON Object
		  String newAppointment = readAppointment();
		  output = "{\"status\":\"success\", \"data\": \"" + newAppointment + "\"}";
		  }  catch (Exception e)  {  
			  //Create JSON object 
			  output = "{\"status\":\"error\", \"data\": \"Error while Deleting Appointment.\"}";
			  System.err.println(e.getMessage());  
			  
		 } 
		 
		 return output; 
		 }
}
