package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.ArrayList;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Database {

	private Connection conn;

	public Database() throws SQLException {
		try {
			Class.forName("org.sqlite.JDBC");
			String db = "jdbc:sqlite:Quiz.db";
			this.conn = DriverManager.getConnection(db);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		


		
	}

//	public void getAllCategories() throws SQLException {
//		String query = "SELECT * FROM Question";
//		Statement stmt = this.conn.createStatement();
//		ResultSet rs = stmt.executeQuery(query);
//
//		while (rs.next()) {
//			System.out.println("Question:" + rs.getString(2));
//		}
//		rs.close();
//		stmt.close();
//
//	}



	public int getAllData() throws SQLException {
		// TODO: select count(*) from Question
		String query = "SELECT * FROM Question";
		Statement stmt = this.conn.createStatement();
		ResultSet rs =stmt.executeQuery(query);
		int counter = 0;
		while(rs.next()) {
			System.out.println("ID:"+rs.getString("ID"));
			System.out.println("Question:"+rs.getString("Question"));
			System.out.println("Number:"+rs.getString("Number"));
			counter++;
		}
		rs.close();
        stmt.close();
		return counter;
		
	}
	
	public String getQuestionById(int id) throws SQLException {
		 String query="SELECT * FROM Question WHERE ID=?";
		 //String idString = "" + id;
		// int id = 1;
		 PreparedStatement pstmt = this.conn.prepareStatement( query );
		 pstmt.setInt( 1, id);
		 
		 ResultSet rs = pstmt.executeQuery();
		 String q = null;
		 while(rs.next()) {
			 System.out.println("Question:"+rs.getString("Question"));
			 q = rs.getString("Question");
		 }
		 rs.close();
	     pstmt.close();
	     return q;
	
	}
	
	public String[] getPossibleAnswers(int id) throws SQLException {
		// TODO: select answer from answer where id = id;
		 String query="SELECT * FROM Answer WHERE Number=?";
		 PreparedStatement pstmt = this.conn.prepareStatement( query );
		 pstmt.setInt( 1, id);
		 
		 ResultSet rs = pstmt.executeQuery();
		 String []a=new String[4];
//		 ArrayList<String> list = new ArrayList<>();
		 int i=0;
		 while(rs.next()) {
			 System.out.println("Answer:"+rs.getString("ID"));
			 a[i] = rs.getString("answer");
			 i++;
//			 list.add(rs.getString("answer"));
		 }
		 
		 rs.close();
	     pstmt.close();
	     //for(int j=0;j<a.length;j++) {System.out.println("......."+a[j]);}
	     return a;
//	     return list;
		//return answers;
		
	}
	
	public int getSolutionIdAnswerId(int id) {
		// TODO: select correctid from solution where id = id;
		String query = "SELECT Answer FROM Solution WHERE ID=?";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int result = -1;
		try {
			pstmt = this.conn.prepareStatement(query);
			pstmt.setInt(1, id);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				System.out.println("Answer:" + rs.getInt("Answer"));
				result = rs.getInt("Answer");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				rs.close();
				pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return result;
	}
	
//	public String getSolutionAsText(int solutionid) {
//		// TODO: select solution from sultion where id = solutionid
//		return null;
	//Test
}
	

