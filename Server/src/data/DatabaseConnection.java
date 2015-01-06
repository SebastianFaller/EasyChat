package data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
	
	public void closeDatabaseConnection() throws SQLException{
		
		if(connection!=null){
			connection.close();
		}
		
		if(preparedStatement!=null){
			preparedStatement.close();
		}
	}
		

}
