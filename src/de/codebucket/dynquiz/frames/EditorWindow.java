package de.codebucket.dynquiz.frames;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import de.codebucket.dynquiz.CustomQuiz;
import de.codebucket.dynquiz.FrameRunner;
import de.codebucket.dynquiz.CustomQuiz.Answer;
import de.codebucket.dynquiz.CustomQuiz.Question;
import de.codebucket.dynquiz.util.AliveTask;
import de.codebucket.dynquiz.util.FileManager;
import de.codebucket.dynquiz.util.ClassSerialiser;

import javax.swing.JLabel;

import java.awt.Component;
import java.awt.Desktop;
import java.awt.Font;
import java.awt.Toolkit;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.JTextField;
import javax.swing.JRadioButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;

import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.UIManager;

public class EditorWindow extends JFrame
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private JPanel contentPane;
	private JLabel lblQuestions, lblStatus;
	private JProgressBar barProcess;
	private JTextField txtTitle, txtVersion, txtAuthors, txtTags, txtQuestion, txtAnswerA, txtAnswerB, txtAnswerC;
	private JRadioButton rightA, rightB, rightC;
	private ButtonGroup btnGroup;
	private List<Question> questions = new ArrayList<Question>();
	private File recent;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) 
	{
		FrameRunner.run(EditorWindow.class);
	}

	/**
	 * Create the frame.
	 */
	public EditorWindow()
	{
		addWindowListener(new WindowAdapter() 
		{
			@Override
			public void windowClosing(WindowEvent arg0) 
			{
				exit();
			}
		});
		
		setTitle("Quiz Editor v1.5");
		setResizable(false);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setLocationRelativeTo(null);
		setBounds(100, 100, 500, 525);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mnFile = new JMenu("File");
		mnFile.setFont(UIManager.getFont("TextField.font"));
		menuBar.add(mnFile);
		
		JMenuItem mNewFile = new JMenuItem("New...");
		mNewFile.setFont(new Font("Dialog", Font.BOLD, 12));
		mNewFile.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent arg0) 
			{
				int result = JOptionPane.showConfirmDialog(null, "Do you want to continue? All changes will be lost!", "Quiz Editor v1.5", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
				if(result == JOptionPane.YES_OPTION)
				{
					newQuiz();
				}
			}
		});
		mnFile.add(mNewFile);
		
		JSeparator separator1 = new JSeparator();
		mnFile.add(separator1);
		
		JMenuItem mSaveFile = new JMenuItem("Save...");
		mSaveFile.setFont(new Font("Dialog", Font.BOLD, 12));
		mSaveFile.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent arg0) 
			{
				if(recent != null)
				{
					saveQuiz(recent);
				}
				else
				{
					saveQuizAs();
				}
			}
		});
		mnFile.add(mSaveFile);
		
		JMenuItem mSaveAs = new JMenuItem("Save As...");
		mSaveAs.setFont(new Font("Dialog", Font.BOLD, 12));
		mSaveAs.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent arg0) 
			{
				saveQuizAs();
			}
		});
		mnFile.add(mSaveAs);
		
		JMenuItem mRevert = new JMenuItem("Revert");
		mRevert.setFont(new Font("Dialog", Font.BOLD, 12));
		mRevert.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent arg0) 
			{
				new Thread(new Runnable()
				{
					public void run()
					{
						int result = JOptionPane.showConfirmDialog(null, "Do you want to revert last changes?", "Quiz Editor v1.5", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
						if(result == JOptionPane.YES_OPTION)
						{
							revertChanges();
						}
					}
				});
				revertChanges();
			}
		});
		mnFile.add(mRevert);
		
		JSeparator separator2 = new JSeparator();
		mnFile.add(separator2);
		
		JMenuItem mExit = new JMenuItem("Exit");
		mExit.setFont(new Font("Dialog", Font.BOLD, 12));
		mExit.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent arg0) 
			{
				exit();
			}
		});
		mnFile.add(mExit);
		
		JMenu mnWindow = new JMenu("Window");
		mnWindow.setFont(UIManager.getFont("TextField.font"));
		menuBar.add(mnWindow);
		
		JMenuItem mClose = new JMenuItem("Close Window");
		mClose.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent arg0) 
			{
				dispose();
			}
		});
		mnWindow.add(mClose);
		
		JMenuItem mLauncher = new JMenuItem("Back to Launcher");
		mLauncher.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent arg0) 
			{
				int result = JOptionPane.showConfirmDialog(null, "Do you want return to Launcher? All changes will be lost!", "Quiz Editor v1.5", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
				if(result == JOptionPane.YES_OPTION)
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
			}
		});
		mnWindow.add(mLauncher);
		
		JMenuItem mRestart = new JMenuItem("Restart Application");
		mRestart.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent arg0) 
			{
				dispose();
				new AliveTask() 
				{
					@Override
					public void run()
					{
						try 
						{
							Thread.sleep(2500);
						}
						catch (InterruptedException e) {}
						FrameRunner.run(EditorWindow.class);
					}
				}.start();
			}
		});
		mnWindow.add(mRestart);
		
		JMenu mnHelp = new JMenu("Help");
		mnHelp.setFont(UIManager.getFont("TextField.font"));
		menuBar.add(mnHelp);
		
		JMenuItem mSystem = new JMenuItem("System Information");
		mSystem.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent arg0) 
			{
				File temp = null;
				try 
				{
					temp = File.createTempFile("tmp", ".txt");
				} 
				catch (IOException e) {}
				
				List<String> lines = new ArrayList<String>();
				RuntimeMXBean runtimeBean = ManagementFactory.getRuntimeMXBean();
				
				DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
				Date today = Calendar.getInstance().getTime();        
				String reportDate = df.format(today);
				lines.add("Date: " + reportDate);
				lines.add("Version: 1.5 Release");
				lines.add("Working directory: " + System.getProperty("user.dir"));
				lines.add("---------------------------------------------------------------");
				lines.add("Obtained system information:");
				lines.add("");
				 
		        Map<String, String> systemProperties = runtimeBean.getSystemProperties();
		        Set<String> keys = systemProperties.keySet();
		        for (String key : keys) 
		        {
		        	if(key.startsWith("file") || key.startsWith("line") || key.startsWith("path"))
		        	{
		        		continue;
		        	}
		        	
		            String value = systemProperties.get(key);
		            if(value.length() <= 1)
		            {
		            	continue;
		            }
		            
		            lines.add((key + " = " + value + "."));
		        }
				
		        FileManager.clearFile(temp);
				FileManager.writeFile(temp, lines.toArray(new String[lines.size()]));
				try 
				{
					Desktop.getDesktop().open(temp);
				} 
				catch (IOException e) {}
			}
		});
		mnHelp.add(mSystem);
		
		JMenuItem mUpdate = new JMenuItem("Check for Updates");
		mUpdate.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent arg0) 
			{
				new Thread(new Runnable()
				{
					public void run() 
					{
						FrameRunner.run(UpdaterWindow.class);
					}
				}).start();
			}
		});
		mnHelp.add(mUpdate);
		
		JMenuItem mAbout = new JMenuItem("About DynQuiz");
		mAbout.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent arg0) 
			{
				showMsgDialog(null, "Quiz Editor\nBy Codebucket. Version 1.5\n\nSupport: support@codebucket.de", "About DynQuiz v1.5", JOptionPane.INFORMATION_MESSAGE, null);
			}
		});
		mnHelp.add(mAbout);
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblName = new JLabel("Quiz Editor");
		lblName.setFont(new Font("Segoe UI", Font.PLAIN, 28));
		lblName.setBounds(10, 11, 344, 36);
		contentPane.add(lblName);
		
		JLabel lblAbout = new JLabel("By Codebucket. Version 1.5");
		lblAbout.setFont(new Font("Tahoma", Font.PLAIN, 11));
		lblAbout.setBounds(13, 55, 239, 14);
		contentPane.add(lblAbout);
		
		JLabel lblTitle = new JLabel("Title:");
		lblTitle.setFont(new Font("Dialog", Font.PLAIN, 12));
		lblTitle.setBounds(10, 85, 39, 15);
		contentPane.add(lblTitle);
		
		txtTitle = new JTextField();
		txtTitle.setBounds(50, 83, 250, 20);
		contentPane.add(txtTitle);
		txtTitle.setColumns(10);
		
		JLabel lblAuthors = new JLabel("Authors:");
		lblAuthors.setFont(new Font("Dialog", Font.PLAIN, 12));
		lblAuthors.setBounds(10, 115, 55, 15);
		contentPane.add(lblAuthors);
		
		txtAuthors = new JTextField();
		txtAuthors.setBounds(67, 113, 150, 20);
		contentPane.add(txtAuthors);
		txtAuthors.setColumns(10);
		
		JLabel lblVersion = new JLabel("Version:");
		lblVersion.setFont(new Font("Dialog", Font.PLAIN, 12));
		lblVersion.setBounds(320, 85, 50, 15);
		contentPane.add(lblVersion);
		
		txtVersion = new JTextField();
		txtVersion.setBounds(374, 83, 110, 20);
		contentPane.add(txtVersion);
		txtVersion.setColumns(10);
		
		JLabel lblTags = new JLabel("Tags:");
		lblTags.setFont(new Font("Dialog", Font.PLAIN, 12));
		lblTags.setBounds(235, 115, 35, 15);
		contentPane.add(lblTags);
		
		txtTags = new JTextField();
		txtTags.setBounds(277, 113, 207, 20);
		contentPane.add(txtTags);
		txtTags.setColumns(10);
		
		JPanel questionPanel = new JPanel();
		questionPanel.setBorder(new TitledBorder(null, "Add new Question...", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		questionPanel.setBounds(10, 169, 474, 250);
		contentPane.add(questionPanel);
		questionPanel.setLayout(null);
		
		JLabel lblQuestion = new JLabel("Question:");
		lblQuestion.setFont(new Font("Dialog", Font.PLAIN, 13));
		lblQuestion.setBounds(10, 30, 60, 17);
		questionPanel.add(lblQuestion);
		
		txtQuestion = new JTextField();
		txtQuestion.setBounds(75, 29, 389, 20);
		questionPanel.add(txtQuestion);
		txtQuestion.setColumns(10);
		
		JLabel lblAnswers = new JLabel("Answers:");
		lblAnswers.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblAnswers.setBounds(10, 75, 60, 17);
		questionPanel.add(lblAnswers);
		
		JLabel lblAnswerA = new JLabel("Answer A:");
		lblAnswerA.setFont(new Font("Dialog", Font.PLAIN, 12));
		lblAnswerA.setBounds(10, 103, 60, 14);
		questionPanel.add(lblAnswerA);
		
		txtAnswerA = new JTextField();
		txtAnswerA.setBounds(80, 101, 307, 20);
		questionPanel.add(txtAnswerA);
		txtAnswerA.setColumns(10);
		
		rightA = new JRadioButton("Right?");
		rightA.setFont(new Font("Dialog", Font.BOLD, 12));
		rightA.setBounds(393, 100, 75, 23);
		questionPanel.add(rightA);
		
		JLabel lblAnswerB = new JLabel("Answer B:");
		lblAnswerB.setFont(new Font("Dialog", Font.PLAIN, 12));
		lblAnswerB.setBounds(10, 128, 60, 14);
		questionPanel.add(lblAnswerB);
		
		txtAnswerB = new JTextField();
		txtAnswerB.setBounds(80, 126, 307, 20);
		questionPanel.add(txtAnswerB);
		txtAnswerB.setColumns(10);
		
		rightB = new JRadioButton("Right?");
		rightB.setFont(new Font("Dialog", Font.BOLD, 12));
		rightB.setBounds(393, 125, 75, 23);
		questionPanel.add(rightB);
		
		JLabel lblAnswerC = new JLabel("Answer C:");
		lblAnswerC.setFont(new Font("Dialog", Font.PLAIN, 12));
		lblAnswerC.setBounds(10, 153, 60, 14);
		questionPanel.add(lblAnswerC);
		
		txtAnswerC = new JTextField();
		txtAnswerC.setBounds(80, 151, 307, 20);
		questionPanel.add(txtAnswerC);
		txtAnswerC.setColumns(10);
		
		rightC = new JRadioButton("Right?");
		rightC.setFont(new Font("Dialog", Font.BOLD, 12));
		rightC.setBounds(393, 150, 75, 23);
		questionPanel.add(rightC);
		
		lblQuestions = new JLabel("Question #1");
		lblQuestions.setFont(new Font("Dialog", Font.PLAIN, 13));
		lblQuestions.setBounds(10, 222, 85, 17);
		questionPanel.add(lblQuestions);
		
		btnGroup = new ButtonGroup();
		btnGroup.add(rightA);
		btnGroup.add(rightB);
		btnGroup.add(rightC);
		
		JButton btnAdd = new JButton("Add Question");
		btnAdd.setFont(new Font("Dialog", Font.BOLD, 13));
		btnAdd.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				if(txtQuestion.getText().length() == 0 || txtAnswerA.getText().length() == 0 || txtAnswerB.getText().length() == 0 || txtAnswerC.getText().length() == 0 || btnGroup.getSelection() == null)
				{
					Toolkit.getDefaultToolkit().beep();
					return;
				}
				
				String question = txtQuestion.getText();
				
				int rightAnswer = -1;
				Answer[] answers = new Answer[3];
				answers[0] = new Answer(0, txtAnswerA.getText(), rightA.isSelected());
				answers[1] = new Answer(1, txtAnswerB.getText(), rightB.isSelected());
				answers[2] = new Answer(2, txtAnswerC.getText(), rightC.isSelected());
				rightAnswer = getRightAnswer(answers);					
				
				Question quest = new Question(question, answers, rightAnswer);
				questions.add(quest);
				resetQuestionPane();
			}
		});
		btnAdd.setBounds(339, 209, 125, 30);
		questionPanel.add(btnAdd);
		
		JButton btnSave = new JButton("Save Quiz");
		btnSave.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0) 
			{
				saveQuizAs();
			}
		});
		btnSave.setFont(new Font("Dialog", Font.BOLD, 13));
		btnSave.setBounds(10, 430, 100, 30);
		contentPane.add(btnSave);
		
		lblStatus = new JLabel("Ready.");
		lblStatus.setHorizontalAlignment(SwingConstants.RIGHT);
		lblStatus.setFont(new Font("DialogInput", Font.PLAIN, 12));
		lblStatus.setBounds(120, 439, 194, 15);
		contentPane.add(lblStatus);
		
		barProcess = new JProgressBar();
		barProcess.setBounds(324, 434, 160, 25);
		contentPane.add(barProcess);
		
		FrameRunner.centerWindow(this);
		this.newQuiz();
		validate();
	}
	
	public void newQuiz()
	{
		this.recent = null;
		txtTitle.setText("");
		txtVersion.setText("");
		txtAuthors.setText("");
		txtTags.setText("");
		this.questions = new ArrayList<Question>();
		resetQuestionPane();
	}
	
	public void saveQuizAs()
	{
		if(txtTitle.getText().length() == 0 || txtAuthors.getText().length() == 0 || txtVersion.getText().length() == 0 || txtTags.getText().length() == 0 || questions.size() == 0)
		{
			Toolkit.getDefaultToolkit().beep();
			return;
		}
		
		changeStatus("Waiting for user...", true);
		final JFileChooser dialog = new JFileChooser();
		dialog.setFileFilter(new FileNameExtensionFilter("Custom Quiz File (.quiz)","quiz"));
		final int res = dialog.showSaveDialog(contentPane);
		if(res == JFileChooser.APPROVE_OPTION) 
		{
			saveQuiz(new File(dialog.getSelectedFile() + ".quiz"));
			return;
		}
		changeStatus("Ready.", 0, false);
	}
	
	private void saveQuiz(final File file)
	{
		new Thread(new Runnable() 
		{
			public void run()
			{	
				try
				{
					changeStatus("Creating new file...", 10, false);
					if(!file.exists())
					{
						try 
						{
							file.createNewFile();
						} 
						catch (IOException e)
						{
							e.printStackTrace();
							changeStatus("Ready.", 0, false);
							return;
						}
					}
					completeProgress(10);
					
					changeStatus("Writing quiz to file...", 40, false);
					CustomQuiz quiz = new CustomQuiz(txtTitle.getText(), txtAuthors.getText(), txtVersion.getText(), txtTags.getText().split(", "), getQuestions());
					completeProgress(37);
					
					changeStatus("Building quiz...", 60, false);
					completeProgress(25);
					
					changeStatus("Converting into File...", 80, false);
					byte[] content;
					try 
					{
						content = ClassSerialiser.writeData(quiz, "UTF-8");
					} 
					catch (IOException e)
					{
						e.printStackTrace();
						changeStatus("Ready.", 0, false);
						return;
					}
					completeProgress(75);
									
					changeStatus("Compressing file size...", 90, false);
					FileManager.writeBytes(file, content);
					completeProgress(35);	
				}
				catch(Exception ex)
				{
					changeStatus("Ready.", 0, false);
					showMsgDialog(null, "Could not save quiz: " + ex.getMessage(), "Quiz Editor v1.5", JOptionPane.ERROR_MESSAGE, null);
					return;
				}
				
				changeStatus("Finished!", 100, false);
				completeProgress(5);
				recent = file;
		
				try 
				{
					Thread.sleep(7500);
				} 
				catch (InterruptedException e) {}
				
				if(lblStatus.getText().equals("Finished!"))
				{
					changeStatus("Ready.", 0, false);
				}
			}
		}).start();
	}
	
	public void revertChanges()
	{
		if(recent == null)
		{
			showMsgDialog(null, "Could not revert last changes, because the file was not saved.", "Quiz Editor v1.5", JOptionPane.WARNING_MESSAGE, null);
			Toolkit.getDefaultToolkit().beep();
			return;
		}
		
		try 
		{
			CustomQuiz quiz = (CustomQuiz) ClassSerialiser.readData(FileManager.readBytes(recent), CustomQuiz.class, "UTF-8");
			txtTitle.setText(quiz.getTitle());
			txtVersion.setText(quiz.getVersion());
			txtAuthors.setText(quiz.getAuthor());
			txtTags.setText(getTags(quiz.getTags()));
			this.questions = quiz.getQuestions();
			resetQuestionPane();
		} 
		catch(IOException ex)
		{
			JOptionPane.showMessageDialog(null, "Cannot open file: Invalid or broken file!", "Quiz Editor v1.5", JOptionPane.ERROR_MESSAGE);
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
	
	public void resetQuestionPane()
	{
		clearSelection(btnGroup);
		txtQuestion.setText("");
		txtAnswerA.setText("");
		txtAnswerB.setText("");
		txtAnswerC.setText("");
		lblQuestions.setText("Question #" + (questions.size() +1));
	}
	
	public List<Question> getQuestions()
	{
		return questions;
	}
	
	private int getRightAnswer(Answer[] answers)
	{
		for(int id = 0; id < answers.length; id++)
		{
			if(answers[id].isRight())
			{
				return id;
			}
		}
		return -1;
	}
	
	private void exit()
	{
		if(recent != null)
		{
			int result = JOptionPane.showConfirmDialog(null, "Do you want to exit application? All changes will be lost!", "Quiz Editor v1.2", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
			if(result != JOptionPane.YES_OPTION)
			{
				return;
			}
		}
		
		try 
		{
			Thread.sleep(500);
		}
		catch (InterruptedException e) {}
		System.exit(1);
	}
	
	private void changeStatus(String status, boolean load)
	{
		this.changeStatus(status, 5, load);
	}
	
	private void changeStatus(String status, int percent, boolean load)
	{
		lblStatus.setText(status);
		barProcess.setIndeterminate(load);
		barProcess.setValue(percent);
	}
	
	private void completeProgress(int time)
	{
		try 
		{
			Thread.sleep(new Random().nextInt((int) (time * 100)) + 1000);
		} 
		catch (InterruptedException e) {}
	}
	
	private void clearSelection(ButtonGroup bg) 
	{
		Enumeration<AbstractButton> e = bg.getElements();
		while (e.hasMoreElements()) 
		{
			e.nextElement().setSelected(false);
		}
		bg.clearSelection();
	}
	
	private void showMsgDialog(final Component component, final Object msg, final String title, final int type, final Icon icon)
	{
		new Thread(new Runnable()
		{
			public void run() 
			{
				JOptionPane.showMessageDialog(component, msg, title, type, icon);
			}
		}).start();
	}
}
