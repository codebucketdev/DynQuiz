package de.codebucket.dynquiz.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterInputStream;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class ClassSerialiser 
{
	public static byte[] writeData(Object object, String charset) throws IOException
	{
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		String content = gson.toJson(object);
		return compress(content, Charset.forName(charset));
	}
	
	public static Object readData(byte[] data, Class<?> clazz, String charset) throws IOException
	{
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		String content = decompress(data, Charset.forName(charset));
		return gson.fromJson(content, clazz);
	}
	
	private static byte[] compress(String text, Charset charset) throws IOException
	{
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        OutputStream out = new DeflaterOutputStream(baos);
        out.write(text.getBytes(charset));
        out.close();
        return baos.toByteArray();
    }

	private static String decompress(byte[] bytes, Charset charset) throws IOException 
    {
        InputStream in = new InflaterInputStream(new ByteArrayInputStream(bytes));
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[8192];
        int len;
        while((len = in.read(buffer)) > 0)
            baos.write(buffer, 0, len);
        return new String(baos.toByteArray(), charset);
    }
}
