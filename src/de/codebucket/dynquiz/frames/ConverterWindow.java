package de.codebucket.dynquiz.frames;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import de.codebucket.dynquiz.CustomQuiz;
import de.codebucket.dynquiz.FrameRunner;
import de.codebucket.dynquiz.util.AliveTask;
import de.codebucket.dynquiz.util.FileManager;
import de.codebucket.dynquiz.util.ClassSerialiser;

import java.awt.Component;
import java.awt.Desktop;
import java.awt.Font;
import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.JMenuBar;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JCheckBox;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JMenu;

import org.json.simple.parser.JSONParser;
import javax.swing.JMenuItem;
import java.awt.Color;

public class ConverterWindow extends JFrame
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextField txtInput, txtOutput;
	private JRadioButton rbJson, rbQuiz, rbText;
	private ButtonGroup btnGroup;
	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args)
	{
		FrameRunner.run(ConverterWindow.class);
	}

	/**
	 * Create the frame.
	 */
	public ConverterWindow()
	{
		addWindowListener(new WindowAdapter() 
		{
			@Override
			public void windowClosing(WindowEvent arg0) 
			{
				exit();
			}
		});
		
		setResizable(false);
		setLocationRelativeTo(null);
		setTitle("Quiz Converter v1.6");
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setBounds(100, 100, 450, 325);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mnFile = new JMenu("File");
		mnFile.setFont(UIManager.getFont("TextField.font"));
		menuBar.add(mnFile);
		
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
		mClose.setFont(new Font("Dialog", Font.BOLD, 12));
		mClose.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent arg0) 
			{
				dispose();
			}
		});
		mnWindow.add(mClose);
		
		JMenuItem mLauncher = new JMenuItem("Back to Launcher");
		mLauncher.setFont(new Font("Dialog", Font.BOLD, 12));
		mLauncher.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent arg0) 
			{
				int result = JOptionPane.showConfirmDialog(null, "Do you want return to Launcher? All changes will be lost!", "Quiz Converter v1.6", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
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
		mRestart.setFont(new Font("Dialog", Font.BOLD, 12));
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
						FrameRunner.run(ConverterWindow.class);
					}
				}.start();
			}
		});
		mnWindow.add(mRestart);
		
		JMenu mnHelp = new JMenu("Help");
		mnHelp.setFont(UIManager.getFont("TextField.font"));
		menuBar.add(mnHelp);
		
		JMenuItem mSystem = new JMenuItem("System Information");
		mSystem.setFont(new Font("Dialog", Font.BOLD, 12));
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
				lines.add("Version: 1.6 Release");
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
		mUpdate.setFont(new Font("Dialog", Font.BOLD, 12));
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
		mAbout.setFont(new Font("Dialog", Font.BOLD, 12));
		mAbout.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent arg0) 
			{
				showMsgDialog(null, "Quiz Converter\nBy Codebucket. Version 1.6\n\nSupport: support@codebucket.de", "About DynQuiz v1.6", JOptionPane.INFORMATION_MESSAGE, null);
			}
		});
		mnHelp.add(mAbout);
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblTitle = new JLabel("Quiz Converter");
		lblTitle.setFont(new Font("Segoe UI", Font.PLAIN, 28));
		lblTitle.setBounds(10, 12, 344, 36);
		contentPane.add(lblTitle);
		
		JLabel lblVersion = new JLabel("By Codebucket. Version 1.6");
		lblVersion.setFont(new Font("Tahoma", Font.PLAIN, 11));
		lblVersion.setBounds(13, 56, 239, 14);
		contentPane.add(lblVersion);
		
		JLabel lblBrowse = new JLabel("Choose a File to convert:");
		lblBrowse.setFont(new Font("Dialog", Font.PLAIN, 13));
		lblBrowse.setBounds(10, 85, 145, 16);
		contentPane.add(lblBrowse);
		
		txtInput = new JTextField();
		txtInput.setBackground(Color.WHITE);
		txtInput.setFont(new Font("Tahoma", Font.PLAIN, 13));
		txtInput.setEditable(false);
		txtInput.setBounds(160, 81, 180, 25);
		contentPane.add(txtInput);
		txtInput.setColumns(10);
		
		JButton btnInput = new JButton("Browse...");
		btnInput.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent arg0)
			{
				JFileChooser dialog = new JFileChooser();
				int res = dialog.showOpenDialog(contentPane);
				if(res == JFileChooser.APPROVE_OPTION)
				{
					txtInput.setText(dialog.getSelectedFile().getPath());
				}
			}
		});
		btnInput.setBounds(345, 81, 87, 25);
		contentPane.add(btnInput);
		
		JButton btnConvert = new JButton("Start Convert");
		btnConvert.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent arg0) 
			{
				new Thread(new Runnable()
				{
					public void run() 
					{
						convertFiles();
					}
				}).start();
			}
		});
		btnConvert.setFont(UIManager.getFont("Button.font"));
		btnConvert.setBounds(297, 232, 135, 30);
		contentPane.add(btnConvert);
		
		JPanel optionsPanel = new JPanel();
		optionsPanel.setBorder(new TitledBorder(null, "File options", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		optionsPanel.setBounds(10, 120, 420, 100);
		contentPane.add(optionsPanel);
		optionsPanel.setLayout(null);
		
		JLabel lblType = new JLabel("Type:");
		lblType.setFont(new Font("Dialog", Font.BOLD, 13));
		lblType.setBounds(10, 24, 55, 16);
		optionsPanel.add(lblType);
		
		rbJson = new JRadioButton("to Json");
		rbJson.setFont(new Font("Tahoma", Font.PLAIN, 13));
		rbJson.setBounds(64, 20, 70, 24);
		optionsPanel.add(rbJson);
		
		rbText = new JRadioButton("to Text");
		rbText.setFont(new Font("Tahoma", Font.PLAIN, 13));
		rbText.setBounds(138, 20, 65, 24);
		optionsPanel.add(rbText);
		
		rbQuiz = new JRadioButton("to Quiz");
		rbQuiz.setFont(new Font("Tahoma", Font.PLAIN, 13));
		rbQuiz.setBounds(207, 20, 65, 24);
		optionsPanel.add(rbQuiz);
		
		btnGroup = new ButtonGroup();
		btnGroup.add(rbJson);
		btnGroup.add(rbText);
		btnGroup.add(rbQuiz);
		
		JCheckBox chkCrypted = new JCheckBox("Crypted?");
		chkCrypted.setEnabled(false);
		chkCrypted.setHorizontalAlignment(SwingConstants.RIGHT);
		chkCrypted.setBounds(319, 20, 90, 24);
		optionsPanel.add(chkCrypted);
		
		JLabel lblOutput = new JLabel("Output:");
		lblOutput.setFont(new Font("Dialog", Font.BOLD, 13));
		lblOutput.setBounds(10, 56, 52, 16);
		optionsPanel.add(lblOutput);
		
		txtOutput = new JTextField();
		txtOutput.setBackground(Color.WHITE);
		txtOutput.setFont(new Font("Tahoma", Font.PLAIN, 13));
		txtOutput.setEditable(false);
		txtOutput.setColumns(10);
		txtOutput.setBounds(67, 52, 250, 25);
		optionsPanel.add(txtOutput);
		
		JButton btnOutput = new JButton("Browse...");
		btnOutput.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent arg0)
			{
				JFileChooser dialog = new JFileChooser();
				int res = dialog.showSaveDialog(contentPane);
				if(res == JFileChooser.APPROVE_OPTION)
				{
					txtOutput.setText(dialog.getSelectedFile().getPath());
				}
			}
		});
		btnOutput.setBounds(322, 52, 87, 25);
		optionsPanel.add(btnOutput);
		
		FrameRunner.centerWindow(this);
		setVisible(true);
		validate();
	}
	
	private void convertFiles()
	{
		if(txtInput.getText().length() == 0 || btnGroup.getSelection() == null || txtOutput.getText().length() == 0)
		{
			Toolkit.getDefaultToolkit().beep();
			return;
		}
		
		if(getConvertType() == ConvertType.TO_JSON)
		{
			File input = new File(txtInput.getText());
			if(!input.exists())
			{
				JOptionPane.showMessageDialog(null, "Cannot open file: File not exists!", "Quiz Converter v1.6", JOptionPane.ERROR_MESSAGE);
				return;
			}
			
			CustomQuiz quiz = null;
			byte[] read, write = null;
			try
			{
				read = FileManager.readBytes(input);
				quiz = (CustomQuiz) ClassSerialiser.readData(read, CustomQuiz.class, "UTF-8");
				write = writeData(quiz, "UTF-8");
			}
			catch (Exception ex)
			{
				JOptionPane.showMessageDialog(null, "Cannot open file: Invalid or broken file!", "Quiz Converter v1.6", JOptionPane.ERROR_MESSAGE);
				return;
			}
			
			try
			{
				File output = new File(txtOutput.getText());
				if(!output.exists())
				{
					output.createNewFile();
				}
				
				FileManager.writeBytes(output, write);
				JOptionPane.showMessageDialog(null, "File sucessfully converted!\nOutput: " + output.getPath(), "Quiz Converter v1.6", JOptionPane.INFORMATION_MESSAGE);
				return;
			}
			catch (Exception ex)
			{
				JOptionPane.showMessageDialog(null, "Cannot save file: " + ex.getMessage(), "Quiz Converter v1.6", JOptionPane.ERROR_MESSAGE);
				return;
			}
		}
		else if(getConvertType() == ConvertType.TO_TEXT)
		{
			File input = new File(txtInput.getText());
			if(!input.exists())
			{
				JOptionPane.showMessageDialog(null, "Cannot open file: File not exists!", "Quiz Converter v1.6", JOptionPane.ERROR_MESSAGE);
				return;
			}
			
			CustomQuiz quiz = null;
			byte[] read, write = null;
			try
			{
				read = FileManager.readBytes(input);
				quiz = (CustomQuiz) ClassSerialiser.readData(read, CustomQuiz.class, "UTF-8");
				write = writeData(quiz, "UTF-8");
			}
			catch (Exception ex)
			{
				JOptionPane.showMessageDialog(null, "Cannot open file: Invalid or broken file!", "Quiz Converter v1.6", JOptionPane.ERROR_MESSAGE);
				return;
			}
			
			try
			{
				File output = new File(txtOutput.getText());
				if(!output.exists())
				{
					output.createNewFile();
				}
				
				FileManager.writeFile(output, new JSONParser().parse(new String(write)).toString());
				JOptionPane.showMessageDialog(null, "File sucessfully converted!\nOutput: " + output.getPath(), "Quiz Converter v1.6", JOptionPane.INFORMATION_MESSAGE);
				return;
			}
			catch (Exception ex)
			{
				JOptionPane.showMessageDialog(null, "Cannot save file: " + ex.getMessage(), "Quiz Converter v1.6", JOptionPane.ERROR_MESSAGE);
				return;
			}
		}
		else if(getConvertType() == ConvertType.TO_QUIZ)
		{
			File input = new File(txtInput.getText());
			if(!input.exists())
			{
				JOptionPane.showMessageDialog(null, "Cannot open file: File not exists!", "Quiz Converter v1.6", JOptionPane.ERROR_MESSAGE);
				return;
			}
			
			CustomQuiz quiz = null;
			byte[] read, write = null;
			try
			{
				read = FileManager.readBytes(input);
				quiz = (CustomQuiz) readData(read, CustomQuiz.class, "UTF-8");
				write = ClassSerialiser.writeData(quiz, "UTF-8");
			}
			catch (Exception ex)
			{
				JOptionPane.showMessageDialog(null, "Cannot open file: Invalid or broken file!", "Quiz Converter v1.6", JOptionPane.ERROR_MESSAGE);
				return;
			}
			
			try
			{
				File output = new File(txtOutput.getText());
				if(!output.exists())
				{
					output.createNewFile();
				}
				
				FileManager.writeBytes(output, write);
				JOptionPane.showMessageDialog(null, "File sucessfully converted!\nOutput: " + output.getPath(), "Quiz Converter v1.6", JOptionPane.INFORMATION_MESSAGE);
				return;
			}
			catch (Exception ex)
			{
				JOptionPane.showMessageDialog(null, "Cannot save file: " + ex.getMessage(), "Quiz Converter v1.6", JOptionPane.ERROR_MESSAGE);
				return;
			}
		}
	}
	
	private ConvertType getConvertType()
	{
		if(rbJson.isSelected())
		{
			return ConvertType.TO_JSON;
		}
		
		if(rbText.isSelected())
		{
			return ConvertType.TO_TEXT;
		}
		
		if(rbQuiz.isSelected())
		{
			return ConvertType.TO_QUIZ;
		}
		
		return null;
	}
	
	public static byte[] writeData(Object object, String charset) throws UnsupportedEncodingException
	{
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		String content = gson.toJson(object);
		return content.getBytes(charset);
	}
	
	public static Object readData(byte[] data, Class<?> clazz, String charset) throws UnsupportedEncodingException
	{
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		String content = new String(data, charset);
		return gson.fromJson(content, clazz);
	}
	
	private void exit()
	{
		int result = JOptionPane.showConfirmDialog(null, "Do you want to exit application? All changes will be lost!", "Quiz Converter v1.6", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
		if(result == JOptionPane.YES_OPTION)
		{
			System.exit(1);
		}
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
	
	public enum ConvertType
	{
		TO_JSON,
		TO_TEXT,
		TO_QUIZ;
	}
}
