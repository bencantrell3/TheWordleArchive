import java.util.ArrayList;
import java.util.Scanner;
import java.io.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.*;
////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class Wordle1 implements ActionListener,FocusListener,KeyListener{
	
	
	JFrame frame = new JFrame("The Wordle Archive");
	JFrame dateFrame = new JFrame("Select Date");
	
	JPanel board = new JPanel();
	JPanel titlePanel = new JPanel();
	JPanel dateFramePanel = new JPanel();
	
	JLabel titleLabel = new JLabel("The Wordle Archive");//The Wordle Archive
	String gameMode = "August 28, 2023";
	JLabel date = new JLabel(gameMode);
	
	JButton one = new JButton("Click Here");
	JButton playAgainButton = new JButton("Random");
	JButton selectDateButton = new JButton("Select Date");
	JButton confirmButton = new JButton("Confirm");
	
	JTextField oneField = new JTextField("");
	
	String[] months = {"January","February","March","April","May","June","July","August","September","October","November","December"};
	Integer[] days = {1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31};
	Integer[] years = {2021,2022,2023};
	
	JComboBox<String> monthBox = new JComboBox<String>(months);
	JComboBox<Integer> dayBox = new JComboBox<Integer>(days);
	JComboBox<Integer> yearBox = new JComboBox<Integer>(years);
	
	final String FILEPATH = "\\C:\\Users\\Benja\\OneDrive\\Documents\\wordle_words.txt\\";//file path to wordle_words.txt here
	
	File wordFile = new File(FILEPATH);
	
	JPanel[][] boardArray = new JPanel[6][5];
	JLabel[][] labelArray = new JLabel[6][5];
	Color[][] colorArray = new Color[6][5];
	
	Timer time = new Timer(150,this);
	
	static int row = 0;
	static int oneFieldY = 0;
	
	String answer = "error";

	
	boolean gameStarted = false;
	
	static ArrayList<WordleInstance1> list = new ArrayList<WordleInstance1>();

	///////////////////////////////////////////////////////////////////////////////////////////////////////////
	public Wordle1(){
		makeUI();
	}
	
	
	public static void main(String[] args){
		
		

		try {
			URL webpage = new URL("https://www.fiveforks.com/wordle/");
			URL webpage1 = new URL("https://www.stockq.org/life/wordle-answers.php");
			URLConnection conn = webpage.openConnection();
			URLConnection conn1 = webpage1.openConnection();
			InputStreamReader reader = new InputStreamReader(conn.getInputStream());
			InputStreamReader reader1 = new InputStreamReader(conn1.getInputStream());
			BufferedReader buffer = new BufferedReader(reader);
			BufferedReader buffer1 = new BufferedReader(reader1);
			String line = "";
			String line1 = "";
			boolean start = false;
			
			
			while(true) {
				line = buffer.readLine();
				if(line != null) {
					if(line.indexOf("Chronological") >= 0) {
						start = true;
					}
					else if(start){
						
						WordleInstance1 x = new WordleInstance1(line);
						if(x.word != null) {
							list.add(x);
							//System.out.println(x.word);
						}	
						if(line.indexOf("CIGAR") >=0 ) {
							break;
						}
					}
					
				}
				else break;
			}
			for(int i = 0; i < 200; i++) {
				line1 = buffer1.readLine();
				if(line1.indexOf("<pre>") >= 0) {
					line1 = buffer1.readLine();
					WordleInstance1 x = new WordleInstance1(line1,"");
					list.add(x);
					//System.out.println(x.word);
					i = 200;
				}
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		Wordle1 run = new Wordle1();
	}
	///////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	
	
	//makes the UI/////////////////////////////////////////////////////////////////////////////////////////////
	public void makeUI()
	{
		getTodaysWord();
		//frame
		frame.setLayout(null);
		frame.setVisible(true);
		frame.setBounds(300,225,518,714);
		frame.getContentPane().setBackground(Color.LIGHT_GRAY);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//board
		frame.add(board);
		board.setBounds(25,150,450,500);
		board.setLayout(new GridLayout(6,5));
		//title panel
		frame.add(titlePanel);
		titlePanel.setBounds(25,5,450,130);
		titlePanel.setBackground(Color.DARK_GRAY);
		titlePanel.setLayout(null);
		//title label
		titlePanel.add(titleLabel);
		titleLabel.setBounds(0,10,450,65);
		titleLabel.setFont(new Font(Font.SANS_SERIF,Font.PLAIN,50));
		titleLabel.setForeground(Color.green);
		titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
		//date
		titlePanel.add(date);
		date.setBounds(0,60,450,65);
		date.setFont(new Font(Font.SANS_SERIF,Font.PLAIN,25));
		date.setForeground(Color.green);
		date.setHorizontalAlignment(SwingConstants.CENTER);
		//start button
		one.setFont(new Font(Font.SANS_SERIF,Font.PLAIN,17));
		Insets margin = one.getMargin();
        margin.left = 0;
        margin.right = 0;
        one.setMargin(margin);
		one.setHorizontalAlignment(JButton.LEADING);
		one.setBackground(Color.white);
		one.addActionListener(this);
		//select Date Button
		titlePanel.add(selectDateButton);
		selectDateButton.setBounds(7,70,100,50);
		selectDateButton.setBackground(Color.black);
		selectDateButton.setForeground(Color.green);
		selectDateButton.addActionListener(this);
		titlePanel.setComponentZOrder(titleLabel, 0);
		//play again button
		titlePanel.add(playAgainButton);
		playAgainButton.setBounds(343,70,100,50);
		playAgainButton.addActionListener(this);
		playAgainButton.setBackground(Color.black);
		playAgainButton.setForeground(Color.green);
		//creating word board
		for(int i = 0; i < 6; i++) {
			for(int j = 0; j < 5; j++) {
				JPanel panel = new JPanel();
				panel.setLayout(new GridLayout(1,1));
				panel.setBackground(Color.white);
				panel.setBorder(BorderFactory.createLineBorder(Color.gray));
				board.add(panel);
				boardArray[i][j] = panel;
				if(!(i == 0 && j == 2)) {
					labelArray[i][j] = new JLabel();
					labelArray[i][j].setFont(new Font(Font.SANS_SERIF,Font.PLAIN,70));//////
					labelArray[i][j].setHorizontalAlignment(SwingConstants.CENTER);
					panel.add(labelArray[i][j]);
				}
			}
		}
		frame.setBounds(300,25,519,714);
		frame.setBounds(300,25,518,714);
		
		boardArray[0][2].add(one);
	}
	

	
	//action method//////////////////////////////////////////////////////////////////////////////////////////////
	public void actionPerformed(ActionEvent e) {
		//start button
		if(e.getSource() == one) {
			startGame();
			titleLabel.setForeground(Color.green);
			titleLabel.setText("The Wordle Archive");
			titleLabel.setFont(new Font(Font.SANS_SERIF,Font.PLAIN,50));
		}
		//timer for win animation///////////////////////////////////////////////////////////////////////////////
		else if(e.getSource() == time) {	
			if(row <6) {
				for(int i = 0; i < 5; i++) {
					if(row > 0) {
						for(int j = 0; j < 5; j++) {
							boardArray[row-1][j].setBackground(Color.white);
						}
					}
					boardArray[row][i].setBackground(Color.green);
				}
			}
			row++;
			if(row == 7) {
				for(int j = 0; j < 5; j++) {
					boardArray[5][j].setBackground(Color.white);
				}
				time.stop();
				for(int i = 0; i < 6; i++) {
					for(int j = 0; j < 5; j++) {
						boardArray[i][j].setBackground(colorArray[i][j]);
					}
				}
			}
		}
		//play again button/////////////////////////////////////////////////////////////////////////////////////
		else if(e.getSource() == playAgainButton) {
			gameMode = "Random";
			startGame();
			reset();
		}
		else if(e.getSource() == selectDateButton) {//Date Frame///////////////////////////////////////////////////
			dateFrame.setLayout(null);
			dateFrame.setVisible(true);
			dateFrame.setBounds(415,315,286,100);
			dateFrame.getContentPane().setBackground(Color.LIGHT_GRAY);
			dateFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			dateFrame.setLayout(new GridLayout(2,1));
			dateFrame.add(dateFramePanel);
			dateFramePanel.setLayout(new GridLayout(1,3));
			dateFramePanel.add(monthBox);
			dateFramePanel.add(dayBox);
			dateFramePanel.add(yearBox);
			dateFrame.add(confirmButton);
			confirmButton.addActionListener(this);
		}
		else if(e.getSource() == confirmButton) {
			String month = (String) monthBox.getSelectedItem();
			int day = (int) dayBox.getSelectedItem();
			int year = (int) yearBox.getSelectedItem();
			
			boolean dateFound = false;
			for(int i = 0; i < list.size(); i++) {
				if(month.equals(list.get(i).month) && day == list.get(i).day && year == list.get(i).year) {
					dateFound = true;
					titleLabel.setForeground(Color.green);
					titleLabel.setText("The Wordle Archive");
					titleLabel.setFont(new Font(Font.SANS_SERIF,Font.PLAIN,50));
					startGame();
					reset();
					answer = (list.get(i).word).toLowerCase();
					gameMode = list.get(i).month + " " + list.get(i).day + ", " + list.get(i).year;
					date.setText(gameMode);
					i = list.size();
				}
			}
			if(!dateFound) {
				titleLabel.setForeground(Color.red);
				titleLabel.setText("Invalid Date; Wordle Began 6/19/21");
				titleLabel.setFont(new Font(Font.SANS_SERIF,Font.PLAIN,25));
			}
			dateFrame.dispose();
		}
	}
	
	
	

	//detects enter key and calls isValidWord()//////////////////////////////////////////////////////////////////
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_ENTER) {
			String input = oneField.getText();
			if(input.length() != 5) {
				titleLabel.setForeground(Color.red);
				titleLabel.setText("Word Must be Five Letters");
				titleLabel.setFont(new Font(Font.SANS_SERIF,Font.PLAIN,35));
			}
			else if(isValidWord(input)) {
				titleLabel.setForeground(Color.green);
				titleLabel.setText("The Wordle Archive");
				titleLabel.setFont(new Font(Font.SANS_SERIF,Font.PLAIN,50));
				oneField.setVisible(false);
				color(input,answer);
			}
			else {
				titleLabel.setForeground(Color.red);
				titleLabel.setText("Not in Word List");
				titleLabel.setFont(new Font(Font.SANS_SERIF,Font.PLAIN,50));
			}
		}
	}

	
	//checks validity of word against word list///////////////////////////////////////////////////////////////////
	public boolean isValidWord(String input) {
		if(input.length() != 5) {
			return false;
		}
		try {
			Scanner scanFile = new Scanner(wordFile);
			while(scanFile.hasNextLine()) {
				String str = scanFile.nextLine();
				if(str.equals(input)) {
					return true;
				}
			}
		}
		catch(Exception x){
			System.out.println("File Error");
		}
		return false;
	}
	
	//updates UI to the correct color of tiles and moves the text field along//////////////////////////////////////
	public void color(String guess, String ans) {
		String ansDupe = ans;
		int greenCount = 0;
		if(row == 0) {
			labelArray[0][2] = new JLabel();
			labelArray[0][2].setFont(new Font(Font.SANS_SERIF,Font.PLAIN,70));
			labelArray[0][2].setHorizontalAlignment(SwingConstants.CENTER);
			boardArray[0][2].add(labelArray[0][2]);
		}
		for(int i = 0; i < 5; i++) {
			if(guess.charAt(i) == ans.charAt(i)) {
				boardArray[row][i].setBackground(Color.green);
				ansDupe = ansDupe.substring(0,ansDupe.indexOf(guess.charAt(i))) + ansDupe.substring(ansDupe.indexOf(guess.charAt(i))+1);
				greenCount++;
			}
		}
		for(int i = 0; i < 5; i++) {
			if(ansDupe.indexOf(guess.charAt(i)) >= 0 && boardArray[row][i].getBackground() != Color.green) {
				boardArray[row][i].setBackground(Color.yellow);
				ansDupe = ansDupe.substring(0,ansDupe.indexOf(guess.charAt(i))) + ansDupe.substring(ansDupe.indexOf(guess.charAt(i))+1);
			}
			////
			labelArray[row][i].setText((guess.charAt(i)+"").toUpperCase());
			labelArray[row][i].setFont(new Font(Font.SANS_SERIF,Font.PLAIN,70));
		}
		//moves text field down a row
		row++;
		oneField.setVisible(true);
		oneFieldY += (500/6);
		oneField.setBounds(0,oneFieldY,450,(500/6));
		oneField.setText(""); 
		oneField.requestFocus();
		board.setComponentZOrder(oneField, 0);
		//checking win
		if(greenCount == 5) {
			//saving tile color info to display again after win animation
			for(int i = 0; i < 6; i++) {
				for(int j = 0; j < 5; j++) {
					colorArray[i][j] = boardArray[i][j].getBackground();
				}
			}
			oneField.setVisible(false);
			row = 0;
			time.start();//starting win animation
		}
		//checking loss
		else if(row == 6 ) {
			date.setText(answer.toUpperCase());
			date.setForeground(Color.red);
			for(int i = 0; i < 5; i++) {
				boardArray[5][i].setBackground(Color.red);
			}
		}
	}
	
	public void reset() {
		for(int i = 0; i < 6; i++) {
			for(int j = 0; j < 5; j++) {
				if(labelArray[i][j] != null) {
					boardArray[i][j].setBackground(Color.white);
					labelArray[i][j].setText("");
				}
			}
		}
		titlePanel.repaint();
		row = 0;
		date.setText(gameMode);
		date.setForeground(Color.green);
		oneFieldY = 0;
		oneField.setBounds(0,oneFieldY,450,(500/6));
		if(boardArray[0][2] != null)
			boardArray[0][2].removeAll();
		answer = getWord();
		oneField.setVisible(true);
		oneField.requestFocus();
	}
	
	public void startGame() {
		if(!gameStarted) {
			boardArray[0][2].remove(one);
			board.setLayout(null);
			board.add(oneField);
			oneField.setBounds(0,oneFieldY,450,(500/6));
			oneField.setFont(new Font(Font.SANS_SERIF,Font.PLAIN,70));
			oneField.setHorizontalAlignment(SwingConstants.CENTER);
			oneField.addFocusListener(this);
			oneField.addKeyListener(this);
			oneField.requestFocus();
			gameStarted = true;
		}
	}
	
	//generating random answer from word list///////////////////////////////////////////////////////////////////////
	public String getWord() {
		int randNum = (int)(Math.random()*2500)+1;
		String word = "";
		try {
			Scanner scanFile = new Scanner(wordFile);
			for(int i = 0; i < randNum && scanFile.hasNextLine(); i++) {
				word =  scanFile.nextLine();
			}
		}
		catch(Exception x){
			System.out.println("File Error");
		}
		return word;
	}
	public void getTodaysWord() {
		answer = (list.get(list.size()-1).word).toLowerCase();
		gameMode = list.get(list.size()-1).month + " " + list.get(list.size()-1).day + ", " + list.get(list.size()-1).year;
		date.setText(gameMode);
	}

	//unused abstract methods from ActionListener//////////////////////////////////////////////////////////////////////
	public void focusGained(FocusEvent e) {}
	public void focusLost(FocusEvent e) {}
	public void keyTyped(KeyEvent e) {}
	public void keyReleased(KeyEvent e) {}
		
}
