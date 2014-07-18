package ftrippel.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * 
 * @author ftrippel
 * 
 */
public class NumberUtils extends org.apache.commons.lang.math.NumberUtils {

	@SuppressWarnings("unchecked")
	public static <T extends Number> T add(T a, T b) {
		if (a instanceof Integer) {
			return (T) new Integer((Integer) a + (Integer) b);
		}
		if (a instanceof Long) {
			return (T) new Long((Long) a + (Long) b);
		}
		if (a instanceof Double) {
			return (T) new Double((Double) a + (Double) b);
		}
		if (a instanceof BigDecimal) {
			return (T) ((BigDecimal) a).add((BigDecimal) b);
		}
		throw new IllegalArgumentException("Type not supported: " + a.getClass().getSimpleName());
	}

	@SuppressWarnings("unchecked")
	public static <T extends Number> T subtract(T a, T b) {
		if (a instanceof Integer) {
			return (T) new Integer((Integer) a - (Integer) b);
		}
		if (a instanceof Long) {
			return (T) new Long((Long) a - (Long) b);
		}
		if (a instanceof Double) {
			return (T) new Double((Double) a - (Double) b);
		}
		if (a instanceof BigDecimal) {
			return (T) ((BigDecimal) a).subtract((BigDecimal) b);
		}
		throw new IllegalArgumentException("Type not supported: " + a.getClass().getSimpleName());
	}

	@SuppressWarnings("unchecked")
	public static <T extends Number> T initialValue(Class<T> clazz) {
		if (clazz.equals(Integer.class)) {
			return (T) new Integer(0);
		}
		if (clazz.equals(Long.class)) {
			return (T) new Long(0L);
		}
		if (clazz.equals(Double.class)) {
			return (T) new Double(0.0d);
		}
		if (clazz.equals(BigDecimal.class)) {
			return (T) new BigDecimal("0.0");
		}
		throw new IllegalArgumentException("Type not supported: " + clazz.getSimpleName());
	}

	protected static Map<String, DecimalFormat> dfs = new HashMap<String, DecimalFormat>();

	enum DecimalFormatSign {
		NONE, START, END
	};

	public static DecimalFormat getDecimalFormat(int vorkomma, char decimalSeparator, int nachkomma, DecimalFormatSign sign) {
		String key = Integer.toString(vorkomma) + decimalSeparator + Integer.toString(nachkomma) + sign.toString();
		DecimalFormat df;
		if (dfs.containsKey(key)) {
			df = dfs.get(key);
		} else {
			df = new DecimalFormat(StringUtils.leftPad("0", vorkomma, '0') + (nachkomma > 0 ? "." + StringUtils.leftPad("0", nachkomma, '0') : ""));
			DecimalFormatSymbols fs = new DecimalFormatSymbols(Locale.ENGLISH);
			fs.setDecimalSeparator(decimalSeparator);
			df.setDecimalFormatSymbols(fs);
			switch (sign) {
			case START:
				df.setNegativePrefix("-");
				df.setPositivePrefix("+");
				df.setNegativeSuffix("");
				df.setPositiveSuffix("");
				break;
			case END:
				df.setNegativePrefix("");
				df.setPositivePrefix("");
				df.setNegativeSuffix("-");
				df.setPositiveSuffix("+");
				break;
			case NONE:
				df.setNegativePrefix("");
				df.setPositivePrefix("");
				df.setNegativeSuffix("");
				df.setPositiveSuffix("");
				break;
			}
			dfs.put(key, df);
		}
		return df;
	}

}
