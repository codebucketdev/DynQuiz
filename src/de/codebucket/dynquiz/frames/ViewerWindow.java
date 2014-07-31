package de.codebucket.dynquiz.frames;

import java.awt.Desktop;
import java.awt.SystemColor;
import java.awt.Toolkit;

import javax.swing.Action;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField;

import java.awt.Font;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.swing.SwingConstants;
import javax.swing.JButton;
import javax.swing.border.LineBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import de.codebucket.dynquiz.CustomQuiz;
import de.codebucket.dynquiz.CustomQuiz.Answer;
import de.codebucket.dynquiz.CustomQuiz.Question;
import de.codebucket.dynquiz.CustomQuiz.Result;
import de.codebucket.dynquiz.FrameRunner;
import de.codebucket.dynquiz.awt.JFrameScreen;
import de.codebucket.dynquiz.util.AliveTask;
import de.codebucket.dynquiz.util.FileManager;
import de.codebucket.dynquiz.util.ClassSerialiser;

import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.Cursor;
import javax.swing.JFrame;

public class ViewerWindow extends JFrameScreen implements KeyListener, ActionListener
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1L;
	private static ViewerWindow instance;
	private JPanel contentPane;
	
	private File file;
	private CustomQuiz quiz;
	private JLabel lblTitle;
	private JTextField lblQuestion;
	private JButton btnAnswerA, btnAnswerB, btnAnswerC;
	private List<JButton> answers;
	
	private List<Result> results;
	private Question question;
	private boolean finished;
	private boolean success;
	private int index;
	
	private static final Color SKYBLUE = new Color(135, 206, 235);
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) 
	{
		FrameRunner.run(ViewerWindow.class);
	}
	
	/**
	 * Create the frame.
	 */
	public ViewerWindow()
	{
		super(true, 1280, 1024);
		setWidth(1280);
		setHeight(1024);
	}
	
	@Override
	public void build() 
	{
		setInstance(this);	
		JFileChooser dialog = new JFileChooser();
		dialog.setFileFilter(new FileNameExtensionFilter("Custom Quiz File (.quiz)", "quiz"));
		int res = dialog.showOpenDialog(contentPane);
		if(res != JFileChooser.APPROVE_OPTION) 
		{
			JOptionPane.showMessageDialog(null, "You have to select a file to start the quiz!", "Quiz Viewer v1.5", JOptionPane.WARNING_MESSAGE);
			this.exit();
			return;
		}
		
		this.file = dialog.getSelectedFile();
		if(!file.exists())
		{
			JOptionPane.showMessageDialog(null, "Cannot open file: File not exists!", "Quiz Viewer v1.5", JOptionPane.ERROR_MESSAGE);
			this.exit();
			return;
		}
		
		if(!file.getName().endsWith(".quiz"))
		{
			JOptionPane.showMessageDialog(null, "Cannot open file: Wrong or unsupported type!", "Quiz Viewer v1.5", JOptionPane.ERROR_MESSAGE);
			this.exit();
			return;
		}
		
		try
		{
			byte[] data = FileManager.readBytes(file);
			CustomQuiz quiz = (CustomQuiz) ClassSerialiser.readData(data, CustomQuiz.class, "UTF-8");
			this.quiz = quiz;
		}
		catch(IOException ex)
		{
			JOptionPane.showMessageDialog(null, "Cannot open file: Invalid or broken file!", "Quiz Viewer v1.5", JOptionPane.ERROR_MESSAGE);
			this.exit();
			return;
		}
		
		setFocusable(true);
		addKeyListener(this);
		setResizable(false);
	    setTitle("Quiz Viewer v1.5: $title");
	    setBounds(100, 100, 1280, 1024);
	    setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
	    FrameRunner.centerWindow(this);
	    
	    this.finished = false;
		addWindowListener(new WindowAdapter() 
		{
			@Override
			public void windowClosing(WindowEvent arg0) 
			{
				if(finished == false)
				{
					success = false;
					index = quiz.getQuestions().size();
					nextQuestion();
					return;
				}
				
				if(getWindowState() == WindowState.FULLSCREEN)
				{
					switchFullscreen();
				}			
				setVisible(false);
				exit();
			}
		});
	    
	    this.contentPane = new JPanel();
	    this.contentPane.setLayout(null);
	    setContentPane(this.contentPane);
	    
	    lblTitle = new JLabel("$title");
	    lblTitle.setFocusable(false);
	    lblTitle.setForeground(new Color(0, 0, 0));
	    lblTitle.setFont(new Font("Tahoma", Font.PLAIN, 46));
	    lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
	    lblTitle.setBounds(10, 35, 1244, 100);
	    contentPane.add(lblTitle);
	    
	    lblQuestion = new JTextField("$question");
	    lblQuestion.setBackground(Color.WHITE);
	    lblQuestion.setForeground(Color.BLACK);
	    lblQuestion.setHorizontalAlignment(SwingConstants.CENTER);
	    lblQuestion.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
	    lblQuestion.setEditable(false);
	    lblQuestion.setFocusable(false);
	    lblQuestion.setBorder(new LineBorder(new Color(0, 0, 255), 3, true));
	    lblQuestion.setFont(new Font("Arial", Font.PLAIN, 32));
	    lblQuestion.setBounds(37, 175, 1200, 575);
	    contentPane.add(lblQuestion);
	     
	    this.answers = new ArrayList<JButton>();
	    btnAnswerA = new JButton("$answerA");
	    btnAnswerA.setFocusable(false);
	    btnAnswerA.addActionListener(this);
	    btnAnswerA.setBackground(SKYBLUE);
	    btnAnswerA.setForeground(Color.BLACK);
	    btnAnswerA.setBorder(new LineBorder(SystemColor.textHighlight, 3, true));
	    btnAnswerA.setFont(new Font("Dialog", Font.PLAIN, 20));
	    btnAnswerA.setBounds(40, 793, 375, 150);
	    answers.add(0, btnAnswerA);
	    contentPane.add(btnAnswerA);
	    
	    btnAnswerB = new JButton("$answerB");
	    btnAnswerB.setFocusable(false);
	    btnAnswerB.addActionListener(this);
	    btnAnswerB.setBackground(SKYBLUE);
	    btnAnswerB.setForeground(Color.BLACK);
	    btnAnswerB.setBorder(new LineBorder(SystemColor.textHighlight, 3, true));
	    btnAnswerB.setFont(new Font("Dialog", Font.PLAIN, 20));
	    btnAnswerB.setBounds(449, 793, 375, 150);
	    answers.add(1, btnAnswerB);
	    contentPane.add(btnAnswerB);
	    
	    btnAnswerC = new JButton("$answerC");
	    btnAnswerC.setFocusable(false);
	    btnAnswerC.addActionListener(this);
	    btnAnswerC.setBackground(SKYBLUE);
	    btnAnswerC.setForeground(Color.BLACK);
	    btnAnswerC.setBorder(new LineBorder(SystemColor.textHighlight, 3, true));
	    btnAnswerC.setFont(new Font("Dialog", Font.PLAIN, 20));
	    btnAnswerC.setBounds(859, 793, 375, 150);
	    answers.add(2, btnAnswerC);
	    contentPane.add(btnAnswerC);
	    this.loadQuiz();
	    
	    this.switchFullscreen();
	}
	
	public void loadQuiz()
	{
		this.resetQuiz();
	}
	
	public void resetQuiz()
	{
		this.index = 0;
		this.success = false;
		this.finished = false;
		this.results = new ArrayList<Result>();
		this.question = quiz.getQuestions().get(index);
		
		setTitle(getTitle().replace("$title", quiz.getTitle()));
		lblTitle.setText(quiz.getTitle());
		lblQuestion.setText(question.getQuestion());
		for(int i = 0; i < answers.size(); i++)
		{
			JButton btn = answers.get(i);
			Answer ans = question.getAnswers()[i];
			btn.setText(ans.getAnswer());
			btn.setBackground(SKYBLUE);
			btn.setForeground(Color.BLACK);
		}
	}
	
	public void nextQuestion()
	{
		this.index++;
		this.success = false;
		if((index +1) > quiz.getQuestions().size())
		{
			this.finished = true;
			
			List<Question> questions = quiz.getQuestions();
			String[] actions = { "Close Application", "Back to Launcher", "Save Results"};
			for(int i = 0; i < answers.size(); i++)
			{
				JButton btn = answers.get(i);
				btn.setBackground(SKYBLUE);
				btn.setForeground(Color.BLACK);
				btn.setFont(new Font("Dialog", Font.PLAIN, 24));
				btn.setText(actions[i]);
			}
			
			int size = 0;
			int total = questions.size();
			for(Result result : results)
			{
				if(result.isRight())
				{
					size++;
				}
			}
			
			lblQuestion.setText("Quiz finished! You have " + size + " of " + total + " questions answered correctly.");	
			return;
		}
		
		this.question = quiz.getQuestions().get(index);
		lblTitle.setText(quiz.getTitle());
		lblQuestion.setText(question.getQuestion());
		for(int i = 0; i < answers.size(); i++)
		{
			JButton btn = answers.get(i);
			btn.setBackground(SKYBLUE);
			btn.setForeground(Color.BLACK);
			btn.setText(question.getAnswers()[i].getAnswer());
		}
	}
	
	public void checkAnswer(final int id)
	{
		if(finished == true)
		{
			executeAction(id);
			return;
		}
		
		final JButton btn = (JButton) answers.get(id);
		Question q = quiz.getQuestions().get(index);
		final int right = q.getRightAnswer();
		
		if(isAnswerChoosen())
		{
			Toolkit.getDefaultToolkit().beep();
			return;
		}
		
		new Thread(new Runnable() 
		{
			public void run()
			{
				boolean result = false;
				
				if(btn.getBackground() != SKYBLUE)
				{
					Toolkit.getDefaultToolkit().beep();
					return;
				}
				
				btn.setBackground(Color.ORANGE);
				btn.setForeground(Color.WHITE);
				
				try
				{
					Thread.sleep((long) (2.5D * 1000L));
				} 
				catch (InterruptedException e) {}
				
				if(btn.getBackground() == SKYBLUE)
				{
					return;
				}
				
				if(answers.get(right).getText().equals(btn.getText()))
				{
					result = true;
					btn.setBackground(Color.GREEN);
					btn.setForeground(Color.WHITE);
				}
				else
				{
					result = false;
					btn.setBackground(Color.RED);
					btn.setForeground(Color.WHITE);
					
					JButton rtn = answers.get(right);
					rtn.setBackground(Color.GREEN);
					rtn.setForeground(Color.WHITE);
				}
				
				results.add(new Result(id, quiz.getQuestions().get(index), result));
				success = true;
			}
		}).start();
	}
	
	
	public void keyPressed(KeyEvent e) 
	{
		if(e.getKeyCode() == KeyEvent.VK_ENTER)
		{
			if(success == true)
			{
				nextQuestion();
				return;
			}
			Toolkit.getDefaultToolkit().beep();
			e.consume();
			return;
		}
		
		if(e.getKeyCode() == KeyEvent.VK_F5)
		{
			switchFullscreen();
		}
		
		if(e.getKeyCode() == KeyEvent.VK_F6)
		{
			checkAnswer(0);
		}
		
		if(e.getKeyCode() == KeyEvent.VK_F7)
		{
			checkAnswer(1);
		}
		
		if(e.getKeyCode() == KeyEvent.VK_F8)
		{
			checkAnswer(2);
		}
		
		if(e.getKeyCode() == KeyEvent.VK_F9)
		{
			if(finished == false)
			{
				resetQuiz();
				return;
			}
			Toolkit.getDefaultToolkit().beep();
			e.consume();
			return;
		}
	}

	public void keyReleased(KeyEvent e) {}

	public void keyTyped(KeyEvent e) {}
	
	public static class AnswerListener implements ActionListener
	{
		private Action action;
		private JButton button;
		private int index;
		
		public AnswerListener(Action action, JButton btn, int index)
		{
			this.button = btn;
			this.index = index;
		}
		
		public Action getAction()
		{
			return action;
		}
		
		public JButton getButton()
		{
			return button;
		}
		
		public int getIndex()
		{
			return index;
		}

		public void actionPerformed(ActionEvent e)
		{
			action.actionPerformed(e);
		}
	}
	
	public void executeAction(int id)
	{
		if(finished == false)
		{
			return;
		}
		
		if(id == 0)
		{
			if(getWindowState() == WindowState.FULLSCREEN)
			{
				this.switchFullscreen();
			}
			
			this.setVisible(false);
			this.exit();
			return;
		}
		
		if(id == 1)
		{
			if(getWindowState() == WindowState.FULLSCREEN)
			{
				this.switchFullscreen();
			}
			
			this.setVisible(false);
			this.backLauncher();
			return;
		}
		
		if(id == 2)
		{
			boolean fullscreen = (getWindowState() == WindowState.FULLSCREEN);
			if(fullscreen = true)
			{
				this.switchFullscreen();
			}
			this.setVisible(false);
			
			JFileChooser dialog = new JFileChooser();
			final int res = dialog.showSaveDialog(contentPane);
			if(res == JFileChooser.APPROVE_OPTION) 
			{
				try
				{
					File file = dialog.getSelectedFile();
					if(!file.exists())
					{
						file.createNewFile();
					}
					
					List<String> lines = new ArrayList<String>();
					
					DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
					Date today = Calendar.getInstance().getTime();        
					String reportDate = df.format(today);
					
					lines.add("Date: " + reportDate);
					lines.add("Version: 1.5 Release");
					lines.add("Working directory: " + System.getProperty("user.dir"));
					lines.add("---------------------------------------------------------------");
					lines.add("Quiz v1.5 Information:");
					lines.add("File: " + this.file.getAbsolutePath());
					lines.add("User: " + System.getProperty("user.name"));
					lines.add(" ");
					lines.add("Title: " + quiz.getTitle());
					lines.add("Author: " + quiz.getAuthor());
					lines.add("Version: " + quiz.getVersion());
					lines.add("Tags: " + getTags(quiz.getTags()));
					lines.add("Questions: " + quiz.getQuestions().size());
					lines.add(" ");
					
					for(int i = 0; i < quiz.getQuestions().size(); i++)
					{
						Result r = null;
						if(results.size() > i)
						{
							r = results.get(i);
						}
						
						Question q = quiz.getQuestions().get(i);
						lines.add("Question #" + (i + 1) + ": " + q.getQuestion());
						if(r != null)
						{
							lines.add("Your answer: " + q.getAnswers()[r.getAnswerId()].getAnswer());
						}
						else
						{
							lines.add("Your answer: [This question is not answered]");
						}
						lines.add("Right answer: " + q.getAnswers()[q.getRightAnswer()].getAnswer());
						lines.add(" ");
					}
					
					FileManager.clearFile(file);
					FileManager.writeFile(file, lines.toArray(new String[lines.size()]));
					Desktop.getDesktop().open(file);
				}
				catch(Exception ex)
				{
					JOptionPane.showMessageDialog(null, "Could not save results: " + ex.getMessage(), "Quiz Viewer v1.5", JOptionPane.ERROR_MESSAGE, null);
				}
			}
			
			this.setVisible(true);
			if(fullscreen == true)
			{
				this.switchFullscreen();
			}
		}
	}
	
	private String getTags(String[] tags)
	{
		String out = "";
		for(String tag : tags)
		{
			if(out.length() == 0)
	    	{
				out = (out + tag);
	    	}
	    	else
	    	{
	    		out = (out + ", " + tag);
	    	}
		}
		
		return out;
	}
	
	public boolean isAnswerChoosen()
	{
		for(int i = 0; i < answers.size(); i++)
		{
			if(answers.get(i).getBackground() != SKYBLUE)
				return true;
		}
		return false;
	}

	public void actionPerformed(ActionEvent e)
	{
		if(success == true)
		{
			nextQuestion();
			return;
		}
		
		for(int i = 0; i < answers.size(); i++)
		{
			JButton btn = answers.get(i);
			if(btn.equals(e.getSource()))
			{
				checkAnswer(i);
				return;
			}
		}
	}
	
	public void backLauncher()
	{
		dispose();
		new AliveTask() 
		{
			@Override
			public void run()
			{
				try 
				{
					Thread.sleep(500);
				}
				catch (InterruptedException e) {}
				FrameRunner.run(LauncherDialog.class);
			}
		}.start();
	}
	
	public static ViewerWindow getInstance() 
	{
		return instance;
	}
	
	private static void setInstance(ViewerWindow instance)
	{
		ViewerWindow.instance = instance;
	}
}
