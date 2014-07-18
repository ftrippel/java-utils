package ftrippel.utils;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

/**
 * 
 * @author ftrippel
 * 
 */
public class XmlUtils {

	public static void prettyFormatXml(File in, int indent) {
		try {
			File out = new File(in.getAbsolutePath() + "_out");
			prettyFormatXml(new FileReader(in), new FileWriter(out), indent);
			try {
				if (in.exists()) {
					in.delete();
				}
				org.apache.commons.io.FileUtils.copyFile(out, in);
				if (out.exists()) {
					out.delete();
				}
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/** @author Found this one on StackOverflow */
	public static void prettyFormatXml(Reader reader, Writer writer, int indent) {
		try {
			StreamSource xmlInput = new StreamSource(reader);
			StreamResult xmlOutput = new StreamResult(writer);
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			transformerFactory.setAttribute("indent-number", indent);
			Transformer transformer = transformerFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.transform(xmlInput, xmlOutput);
			xmlOutput.getWriter().close();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
