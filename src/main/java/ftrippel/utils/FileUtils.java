package ftrippel.utils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;

/**
 * 
 * @author ftrippel
 * 
 */
public class FileUtils extends org.apache.commons.io.FileUtils {

	public static int head(File f, PrintStream w, int numLines) {
		BufferedReader br = null;
		String line = null;
		int i = 0;
		try {
			br = new BufferedReader(new FileReader(f));
			while (i < numLines && (line = br.readLine()) != null) {
				w.println(line);
				++i;
			}
			br.close();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return i;
	}

	public static boolean fileEquals(File in1, File in2) throws IOException {
		return streamEquals(new BufferedInputStream(new FileInputStream(in1)), new BufferedInputStream(new FileInputStream(in2)));
	}

	public static boolean streamEquals(InputStream in1, InputStream in2) throws IOException {
		boolean ok = true;
		int b = 0;
		while ((ok = ((b = in1.read()) == in2.read()))) {
			if (b < 0)
				break;
		}
		in1.close();
		in2.close();
		return ok;
	}

}
