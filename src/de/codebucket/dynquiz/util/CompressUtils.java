package de.codebucket.dynquiz.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

public class CompressUtils 
{
	public static byte[] compress(byte[] data) throws IOException
	{
		Deflater deflater = new Deflater();
		deflater.setLevel(Deflater.BEST_COMPRESSION);
		deflater.setStrategy(Deflater.FILTERED);
		deflater.setInput(data);
		deflater.finish();

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
		byte[] buffer = new byte[1024];
		while (!deflater.finished()) 
		{
			int count = deflater.deflate(buffer);
			outputStream.write(buffer, 0, count);
		}
		outputStream.close();
		
		byte[] output = outputStream.toByteArray();
		deflater.end();
		return output;
	}

	public static byte[] decompress(byte[] data) throws IOException, DataFormatException
	{
		Inflater inflater = new Inflater();
		inflater.setInput(data);
		inflater.finished();

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
		byte[] buffer = new byte[1024];
		while (!inflater.finished()) 
		{
			int count = inflater.inflate(buffer);
			outputStream.write(buffer, 0, count);
		}
		outputStream.close();
		
		byte[] output = outputStream.toByteArray();
		inflater.end();
		return output;
	}
}
