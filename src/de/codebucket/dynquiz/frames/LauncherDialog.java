package de.codebucket.dynquiz.frames;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextPane;

import java.awt.Font;
import javax.swing.ImageIcon;
import javax.swing.border.TitledBorder;

import de.codebucket.dynquiz.FrameRunner;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Cursor;

public class LauncherDialog extends JFrame 
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) 
	{
		FrameRunner.run(LauncherDialog.class);
	}

	/**
	 * Create the dialog.
	 */
	public LauncherDialog()
	{
		setTitle("DynQuiz Launcher v1.5");
		setResizable(false);
		setBounds(100, 100, 450, 300);
		setLocationRelativeTo(null);
		getContentPane().setLayout(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JLabel lblHelp = new JLabel("What do you want to do?");
		lblHelp.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblHelp.setBounds(10, 11, 173, 17);
		getContentPane().add(lblHelp);
		
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(null, "Description", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel.setBounds(210, 45, 224, 157);
		getContentPane().add(panel);
		panel.setLayout(new BorderLayout(0, 0));
		
		final JTextPane lblDescription = new JTextPane();
		lblDescription.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblDescription.setBackground(new Color(240, 240, 240));
		lblDescription.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		lblDescription.setEditable(false);
		lblDescription.setVisible(true);
		lblDescription.setText("No description available!");
		panel.add(lblDescription);
		
		JButton btnViewer = new JButton("Open Viewer");
		btnViewer.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				dispose();
				new Thread(new Runnable()
				{
					public void run()
					{
						try 
						{
							Thread.sleep(500);
						}
						catch (InterruptedException e) {}
						FrameRunner.run(ViewerWindow.class);
					}
				}).start();
			}
		});
		btnViewer.setIcon(new ImageIcon(LauncherDialog.class.getResource("/images/icon_open.png")));
		btnViewer.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btnViewer.setFocusable(false);
		btnViewer.setBounds(20, 45, 180, 45);
		getContentPane().add(btnViewer);
		
		JButton btnEditor = new JButton("Open Editor");
		btnEditor.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				dispose();
				new Thread(new Runnable()
				{
					public void run()
					{
						try 
						{
							Thread.sleep(500);
						}
						catch (InterruptedException e) {}
						FrameRunner.run(EditorWindow.class);
					}
				}).start();
			}
		});
		btnEditor.setIcon(new ImageIcon(LauncherDialog.class.getResource("/images/icon_edit.png")));
		btnEditor.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btnEditor.setFocusable(false);
		btnEditor.setBounds(20, 101, 180, 45);
		getContentPane().add(btnEditor);
		
		JButton btnConverter = new JButton("Open Converter");
		btnConverter.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				dispose();
				new Thread(new Runnable()
				{
					public void run()
					{
						try 
						{
							Thread.sleep(500);
						}
						catch (InterruptedException e) {}
						FrameRunner.run(ConverterWindow.class);
					}
				}).start();
			}
		});
		btnConverter.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btnConverter.setFocusable(false);
		btnConverter.setIcon(new ImageIcon(LauncherDialog.class.getResource("/images/icon_convert.png")));
		btnConverter.setBounds(20, 157, 180, 45);
		getContentPane().add(btnConverter);
		
		JButton btnExit = new JButton("Exit");
		btnExit.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent arg0) 
			{
				System.exit(0);
			}
		});
		btnExit.setFocusable(false);
		btnExit.setIcon(new ImageIcon(LauncherDialog.class.getResource("/images/icon_exit.png")));
		btnExit.setFont(new Font("Tahoma", Font.BOLD, 14));
		btnExit.setBounds(319, 213, 115, 40);
		getContentPane().add(btnExit);
		
		JLabel lblUpdate = new JLabel("Check for Update...");
		lblUpdate.addMouseListener(new MouseAdapter() 
		{
			@Override
			public void mouseClicked(MouseEvent arg0)
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
		lblUpdate.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblUpdate.setForeground(Color.BLUE);
		lblUpdate.setBounds(20, 227, 289, 15);
		getContentPane().add(lblUpdate);
	}
}
