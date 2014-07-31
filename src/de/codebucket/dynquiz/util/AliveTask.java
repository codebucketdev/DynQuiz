package de.codebucket.dynquiz.util;

public abstract class AliveTask implements Runnable
{
	public abstract void run();

	public void start()
	{
		new Thread(this).start();
	}
}
