package main;

import java.sql.SQLException;

import database.Database;
import ui.Quiz;

public class Main {

	public static void main(String[] args) {
		try {
			Database db=new Database();
		  //db.getAllDates();
		 // db.getAllCategories();
			new Quiz(db);
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}

}
