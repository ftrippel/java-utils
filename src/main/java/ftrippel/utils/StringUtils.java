package ftrippel.utils;

/**
 * 
 * @author ftrippel
 * 
 */
public class StringUtils extends org.apache.commons.lang.StringUtils {

	public static Boolean stringAsBoolean(String s) {
		String bool = s.trim();
		if (bool.equals("1") || bool.equalsIgnoreCase("true")) {
			return true;
		} else if (bool.equals("0") || bool.equalsIgnoreCase("false")) {
			return false;
		} else {
			return null;
		}
	}

	public static String upperCaseFirstLetter(String s) {
		char c = Character.toUpperCase(s.charAt(0));
		s = c + s.substring(1);
		return s;
	}

	/**
	 * To be used with (open,middle,close)=('[',',',']') for the output of ToStringBuilder.reflectionToString
	 */
	public static String indentString(String s, char open, char middle, char close) {
		StringBuilder b = new StringBuilder();
		int level = 0;
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			if (c == open) {
				b.append(c);
				b.append("\n");
				++level;
				for (int j = level; j > 0; --j)
					b.append("  ");
			} else if (c == close) {
				b.append("\n");
				--level;
				for (int j = level; j > 0; --j)
					b.append("  ");
				b.append(c);
			} else if (c == middle) {
				b.append(c);
				b.append("\n");
				for (int j = level; j > 0; --j)
					b.append("  ");
			} else {
				b.append(c);
			}
		}
		return b.toString();
	}

}
