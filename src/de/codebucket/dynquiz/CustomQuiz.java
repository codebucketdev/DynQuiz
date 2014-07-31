package de.codebucket.dynquiz;

import java.util.List;

public class CustomQuiz 
{
	private String title;
	private String author;
	private String version;
	private String[] tags;
	private List<Question> questions;
	
	public CustomQuiz(String title, String author, String version, String[] tags, List<Question> questions)
	{
		this.title = title;
		this.author = author;
		this.version = version;
		this.tags = tags;
		this.questions = questions;
	}
	
	public String getTitle()
	{
		return title;
	}
	
	public String getAuthor()
	{
		return author;
	}
	
	public String getVersion()
	{
		return version;
	}
	
	public String[] getTags()
	{
		return tags;
	}
	
	public List<Question> getQuestions()
	{
		return questions;
	}
	
	/*private String getTags(String[] tags)
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
	}*/
	
	public static class Question
	{
		private String question;
		private Answer[] answers;
		private int rightAnswer;
		
		public Question(String question, Answer[] answers, int rightAnswer)
		{
			this.question = question;
			this.answers = answers;
			this.rightAnswer = rightAnswer;
		}
		
		public String getQuestion()
		{
			return question;
		}
		
		public Answer[] getAnswers()
		{
			return answers;
		}
		
		public int getRightAnswer()
		{
			return rightAnswer;
		}
	}
	
	public static class Answer
	{
		private int id;
		private String answer;
		private boolean right;
		
		public Answer(int id, String answer, boolean right)
		{
			this.id = id;
			this.answer = answer;
			this.right = right;
		}
		
		public int getId()
		{
			return id;
		}
		
		public String getAnswer()
		{
			return answer;
		}
		
		public boolean isRight()
		{
			return right;
		}
	}
	
	public static class Result
	{
		private int id;
		private Question question;
		private boolean right;
		
		public Result(int id, Question question, boolean right)
		{
			this.id = id;
			this.question = question;
			this.right = right;
		}
		
		public int getAnswerId()
		{
			return id;
		}
		
		public Question getQuestion()
		{
			return question;
		}
		
		public boolean isRight()
		{
			return right;
		}
	}
	
	public static boolean isValid(CustomQuiz quiz)
	{
		return (quiz.getTitle() != null && quiz.getAuthor() != null && quiz.getVersion() != null && quiz.getTags() != null && quiz.getQuestions() != null && quiz.getQuestions().size() != 0);
	}
}
