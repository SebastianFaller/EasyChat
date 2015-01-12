package data;

import java.io.ByteArrayInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.mysql.jdbc.Blob;

public class DatabaseConnection {
	
	private Connection connection;
	private PreparedStatement preparedStatement;
	
	private final String DatabaseUrl = "jdbc:mysql://localhost:3306/chatDatabase";
	private final String sqlUser = "root";
	private final String sqlPassword = "1234";
	
	public DatabaseConnection(){
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			connection = DriverManager.getConnection(DatabaseUrl, sqlUser, sqlPassword);	
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String[] getPwdToUserFromDB(String user) {
		try {
		preparedStatement = connection.prepareStatement("SELECT name, password FROM Users WHERE name = '"+user+"';");
		ResultSet result = preparedStatement.executeQuery();
		String[] s = new String[2];
		result.absolute(1);
		s[0] = result.getString(1);
		System.out.println("so[0]: "+s[0]);
		s[1] = result.getString(2);
		if(result!=null){
			result.close();
		}
		return s;
		} catch (SQLException e){
			System.out.println("SQLExceptionlol");
			return null;
		}
	}
	
	private void closeDatabaseConnection() throws SQLException{
		
		if(connection!=null){
			connection.close();
		}
		
		if(preparedStatement!=null){
			preparedStatement.close();
		}
	}
	
	public void makeNewRowInUsers(String name, String password,byte[] image){
		try {
			preparedStatement = connection.prepareStatement("INSERT INTO Users(name, password, images) VALUES('"+name+"', '"+password+"',?);");
			preparedStatement.setBinaryStream(1, new ByteArrayInputStream(image));
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public byte[] getImageToUser(String name){
		try {
			preparedStatement = connection.prepareStatement("SELECT images FROM Users WHERE name = '"+name+"';");
			ResultSet result = preparedStatement.executeQuery();
			result.next();
			Blob blob = (Blob) result.getBlob(1);
			System.out.println("blob lauft");
			return blob.getBytes(1l, (int)blob.length());
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("SQL EXCEPTION!!!!");
			return null;
//			e.printStackTrace();
		}
	}
	
	public boolean isNameTaken(String name){
		ResultSet result = null;
		try {
			preparedStatement = connection.prepareStatement("SELECT name FROM Users WHERE name = '"+name+"';");
			result = preparedStatement.executeQuery();
			result.next();
			
			try{
				String resultName = result.getString(1);
				return true;
				} catch (SQLException  | NullPointerException e2){
					System.out.println("exception result empty");
					return false;
				}

		} catch (SQLException e) {
			
			e.printStackTrace();
			return true;
		} 
		
		
		
	}

}
