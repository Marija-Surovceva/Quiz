package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Database {

	private Connection conn;

	public Database() throws ClassNotFoundException, SQLException {
		Class.forName("org.sqlite.JDBC");
		String db = "jdbc:sqlite:Quiz.db";
		this.conn = DriverManager.getConnection(db);
		


		
	}

	public void getAllCategories() throws SQLException {
		String query = "SELECT * FROM Question";
		Statement stmt = this.conn.createStatement();
		ResultSet rs = stmt.executeQuery(query);

		while (rs.next()) {
			System.out.println("Question:" + rs.getString(2));
		}
		rs.close();
		stmt.close();

	}



	public int getAllData() throws SQLException {
		// TODO: select count(*) from Question
		String query = "SELECT * FROM Question";
		Statement stmt = this.conn.createStatement();
		ResultSet rs =stmt.executeQuery(query);
		
		while(rs.next()) {
			System.out.println("ID:"+rs.getString("ID"));
			System.out.println("Question:"+rs.getString("Question"));
			System.out.println("Number:"+rs.getString("Number"));
		}
		rs.close();
        stmt.close();
		return 4;
		
	}
	
	public String getQuestionById(int id) throws SQLException {
		 String query="SELECT * FROM Question WHERE ID=?";
		 //String idString = "" + id;
		// int id = 1;
		 PreparedStatement pstmt = this.conn.prepareStatement( query );
		 pstmt.setInt( 1, id);
		 
		 ResultSet rs = pstmt.executeQuery();
		 while(rs.next()) {
			 System.out.println("Question:"+rs.getString("ID"));
		 }
		 rs.close();
	     pstmt.close();
	     return null;
	
	}
	
	public String[] getPossibleAnswers(int id) throws SQLException {
		// TODO: select answer from answer where id = id;
		 String query="SELECT * FROM Answer WHERE ID=?";
		 String idString = "" + id;
		 PreparedStatement pstmt = this.conn.prepareStatement( query );
		 pstmt.setString( 1, idString);
		 
		 ResultSet rs = pstmt.executeQuery(query);
		 while(rs.next()) {
			 System.out.println("Answer:"+rs.getString("ID"));
		 }
		 rs.close();
	     pstmt.close();
		return null;
	}
	
	public int getSolutionIdAnswerId(int id) {
		// TODO: select correctid from solution where id = id;
		return -1;
	}
	
//	public String getSolutionAsText(int solutionid) {
//		// TODO: select solution from sultion where id = solutionid
//		return null;
}
	

