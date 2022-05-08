package ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.Timer;

import database.Database;

public class Quiz extends JFrame implements ActionListener {

	// Referenz zu Datenbank
	public Database db;

	// questionId
	private int index = 1;

	private int correct_guesses = 0;
	private int total_questions;
	private int seconds = 10;

	// Fragerunde
	private JTextField textfield = new JTextField();
	// Frage
	private JTextArea textarea = new JTextArea();

	private JButton buttonA = new JButton();
	private JButton buttonB = new JButton();
	private JButton buttonC = new JButton();
	private JButton buttonD = new JButton();
	private JButton[] buttons = { buttonA, buttonB, buttonC, buttonD };

	private JLabel answer_labelA = new JLabel();
	private JLabel answer_labelB = new JLabel();
	private JLabel answer_labelC = new JLabel();
	private JLabel answer_labelD = new JLabel();
	private JLabel[] answerLabels = { answer_labelA, answer_labelB, answer_labelC, answer_labelD };

	private JLabel time_label = new JLabel();
	private JLabel seconds_left = new JLabel();
	private JTextField number_right = new JTextField();
	private JTextField percentage = new JTextField();

	// 10 Sek. countdown bis zur nächsten Frage
	private Timer timer = new Timer(1000, new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			seconds--;
			seconds_left.setText(String.valueOf(seconds));
			// Falls die Zeit um ist, zeige richtige Antwort an
			if (seconds <= 0) {
				int correctAnswerId = db.getSolutionIdAnswerId(index);
				displayAnswer(correctAnswerId);
			}
		}
	});

	private void initialisiereTextField() {
		textfield.setBounds(0, 0, 650, 40);
		textfield.setBackground(new Color(00, 33, 66));
		textfield.setForeground(new Color(252, 202, 3));
		textfield.setFont(new Font("Ink Free", Font.BOLD, 40));
		textfield.setBorder(BorderFactory.createBevelBorder(1));
		textfield.setHorizontalAlignment(JTextField.CENTER);
		textfield.setEditable(false);
	}

	private void initialisiereTextArea() {
		textarea.setBounds(0, 40, 650, 60);
		textarea.setLineWrap(true);
		textarea.setWrapStyleWord(true);
		textarea.setBackground(new Color(00, 33, 66));
		textarea.setForeground(new Color(252, 202, 3));
		textarea.setFont(new Font("MV Boli", Font.BOLD, 20));
		textarea.setBorder(BorderFactory.createBevelBorder(1));
		textarea.setEditable(false);
	}

	private void initialisiereButtons() {
		for (int i = 0; i < buttons.length; i++) {
			buttons[i].setBounds(0, (i + 1) * 100, 100, 100);
			buttons[i].setFont(new Font("MV Boli", Font.BOLD, 25));
			buttons[i].setFocusable(false);
			buttons[i].addActionListener(this);
			buttons[i].setText(String.valueOf((char)('A' + i)));
		}
		// 0 -> A (i+1)*100
		// 1 -> B
		// 2 -> C
		// 3 -> D
	}

	private void initialisiereAnswerLabels() {
		for (int i = 0; i < answerLabels.length; i++) {
			answerLabels[i].setBounds(125, (i + 1) * 100, 500, 100);
			answerLabels[i].setBackground(new Color(50, 50, 50));
			answerLabels[i].setForeground(new Color(25, 255, 0));
			answerLabels[i].setFont(new Font("MV Boli", Font.PLAIN, 25));
		}
	}

	private void initialisiereSecLeft() {
		seconds_left.setBounds(535, 510, 100, 100);
		seconds_left.setBackground(new Color(00, 33,66));
		seconds_left.setForeground(new Color(255, 0, 0));
		seconds_left.setFont(new Font("Ink Free", Font.BOLD, 50));
		seconds_left.setBorder(BorderFactory.createBevelBorder(1));
		seconds_left.setOpaque(true);
		seconds_left.setHorizontalAlignment(JTextField.CENTER);
		seconds_left.setText(String.valueOf(seconds));
	}

	private void initTimeLabel() {
		time_label.setBounds(535, 475, 100, 25);
		time_label.setBackground(new Color(00, 33, 66));
		time_label.setForeground(new Color(252, 157, 3));
		time_label.setFont(new Font("MV Boli", Font.PLAIN, 16));
		time_label.setHorizontalAlignment(JTextField.CENTER);
	}

	private void initNumberRight() {
		number_right.setBounds(225, 225, 200, 100);
		number_right.setBackground(new Color(00, 33, 66));
		number_right.setForeground(new Color(25, 255, 0));
		number_right.setFont(new Font("Ink Free", Font.BOLD, 50));
		number_right.setBorder(BorderFactory.createBevelBorder(1));
		number_right.setHorizontalAlignment(JTextField.CENTER);
		number_right.setEditable(false);
	}

	private void initPercentage() {
		percentage.setBounds(225, 325, 200, 100);
		percentage.setBackground(new Color(00, 33, 66));
		percentage.setForeground(new Color(25, 255, 0));
		percentage.setFont(new Font("Ink Free", Font.BOLD, 50));
		percentage.setBorder(BorderFactory.createBevelBorder(1));
		percentage.setHorizontalAlignment(JTextField.CENTER);
		percentage.setEditable(false);
	}

	public Quiz(Database db) throws SQLException {
		this.db = db;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(670, 670);
		getContentPane().setBackground(new Color(0, 0, 255));
		setLayout(null);
		setResizable(false);

		initialisiereTextField();

		initialisiereTextArea();

		initialisiereButtons();

		initialisiereAnswerLabels();

		initialisiereSecLeft();

		initTimeLabel();

		initNumberRight();

		initPercentage();

		add(time_label);
		add(seconds_left);
		for (int i = 0; i < answerLabels.length; i++) {
			add(answerLabels[i]);
		}
		for (int i = 0; i < buttons.length; i++) {
			add(buttons[i]);
		}
		add(textarea);
		add(textfield);
		setVisible(true);
		total_questions = db.getAllData();
		nextQuestion();
	}

	public void nextQuestion() throws SQLException {
		// Alle Fragen durch, Zeige Ergebnisse an
		if (index > total_questions) {
			results();
		} else {
			// Zeige nächste Frage an
			textfield.setText("Question " + (index));
			String question = this.db.getQuestionById(index);
			textarea.setText(question);
			//System.out.println("11111111111"+this.db.getPossibleAnswers(index));
			
			
			String[] possibleAnswers = this.db.getPossibleAnswers(index);
			// Zeige mögliche Antworten an
			for (int i = 0; i < possibleAnswers.length; i++) {
				answerLabels[i].setText(possibleAnswers[i]);
			}
			
			timer.start();
		}
	}

	private void setEnabledButton(boolean enable) {
		for (int i = 0; i < buttons.length; i++) {
			buttons[i].setEnabled(enable);
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		setEnabledButton(false);
		int correctAnswerId = -1;

		if (e.getSource() == buttonA) {
			correctAnswerId = db.getSolutionIdAnswerId(index);

			switch (index) {
			case 1:
				if (correctAnswerId == 1) {
					correct_guesses++;
				}
				break;
			case 2:
				if (correctAnswerId == 5) {
					correct_guesses++;
				}
				break;
			case 3:
				if (correctAnswerId == 9) {
					correct_guesses++;
				}
				break;
			case 4:
				if (correctAnswerId == 13) {
					correct_guesses++;
				}
				break;
			default:
				System.out.println("FEHLER");
				break;
			}
		}

		if (e.getSource() == buttonB) {
			correctAnswerId = db.getSolutionIdAnswerId(index);

			switch (index) {
			case 1:
				if (correctAnswerId == 2) {
					correct_guesses++;
				}
				break;
			case 2:
				if (correctAnswerId == 6) {
					correct_guesses++;
				}
				break;
			case 3:
				if (correctAnswerId == 10) {
					correct_guesses++;
				}
				break;
			case 4:
				if (correctAnswerId == 14) {
					correct_guesses++;
				}
				break;
			default:
				System.out.println("FEHLER");
				break;
			}
		}
		if (e.getSource() == buttonC) {
			correctAnswerId = db.getSolutionIdAnswerId(index);

			switch (index) {
			case 1:
				if (correctAnswerId == 3) {
					correct_guesses++;
				}
				break;
			case 2:
				if (correctAnswerId == 7) {
					correct_guesses++;
				}
				break;
			case 3:
				if (correctAnswerId == 11) {
					correct_guesses++;
				}
				break;
			case 4:
				if (correctAnswerId == 15) {
					correct_guesses++;
				}
				break;
			default:
				System.out.println("FEHLER");
				break;
			}
		}
		if (e.getSource() == buttonD) {
			correctAnswerId = db.getSolutionIdAnswerId(index);

			switch (index) {
			case 1:
				if (correctAnswerId == 4) {
					correct_guesses++;
				}
				break;
			case 2:
				if (correctAnswerId == 8) {
					correct_guesses++;
				}
				break;
			case 3:
				if (correctAnswerId == 12) {
					correct_guesses++;
				}
				break;
			case 4:
				if (correctAnswerId == 16) {
					correct_guesses++;
				}
				break;
			default:
				System.out.println("FEHLER");
				break;
			}
		}

		displayAnswer(correctAnswerId);

	}

	public void displayAnswer(int solutionId) {
		// Stoppe Zeit
		timer.stop();
		// Deaktiviere Buttons
		setEnabledButton(false);
		// Markiere falsche Antworten
		int idLabel = (solutionId - 1) % 4;
		for (int i = 0; i < answerLabels.length; i++) {
			if (i != idLabel) {
				answerLabels[i].setForeground(new Color(255, 0, 0));
			}
		}
		// Warte 3 Sec. bis nächste Frage kommt
		Timer pause = new Timer(3000, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				for (int i = 0; i < answerLabels.length; i++) {
					answerLabels[i].setForeground(new Color(25, 255, 0));
				}
				seconds = 10;
				seconds_left.setText(String.valueOf(seconds));
				setEnabledButton(true);
				index++;
				try {
					nextQuestion();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		pause.setRepeats(false);
		pause.start();

	}

	// Ergebnis anzeigen
	public void results() {
		setEnabledButton(false);

		int result = (int) ((correct_guesses / (double) total_questions) * 100);
		textfield.setText("RESULTS!");
		textarea.setText("");

		for (int i = 0; i < answerLabels.length; i++) {
			answerLabels[i].setText("");
		}

		number_right.setText("(" + correct_guesses + "/" + (total_questions) + ")");
		percentage.setText(result + "%");
		add(number_right);
		add(percentage);
	}

}