package ftrippel.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;

import org.junit.Assert;
import org.junit.Test;

import ftrippel.utils.NumberUtils.DecimalFormatSign;

/**
 * 
 * @author ftrippel
 * 
 */
public class NumberUtilsTest {

	@Test
	public void testNumberEquals() {
		Integer i = 1;
		Integer j = 1;
		Assert.assertTrue(i == 1);
		Assert.assertTrue(i.equals(1));
		Assert.assertTrue(i.equals(j));

		BigDecimal d1 = new BigDecimal("0");
		BigDecimal d2 = new BigDecimal("0.0");
		Assert.assertEquals(BigDecimal.ZERO, d1);
		Assert.assertNotEquals(BigDecimal.ZERO, d2);
		Assert.assertTrue(!d1.equals(d2));
		Assert.assertTrue(d1.compareTo(d2) == 0);
	}

	@Test
	public void testInitialAddSubtract() {
		Assert.assertEquals(0, (int) NumberUtils.initialValue(Integer.class));
		Assert.assertEquals(new BigDecimal("0.0"), NumberUtils.initialValue(BigDecimal.class));

		Assert.assertEquals(3, (int) NumberUtils.add(1, 2));
		Assert.assertEquals(BigDecimal.ONE.add(BigDecimal.ONE), NumberUtils.add(BigDecimal.ONE, BigDecimal.ONE));

		Assert.assertEquals(-1, (int) NumberUtils.subtract(1, 2));
		Assert.assertEquals(BigDecimal.ONE.subtract(BigDecimal.ONE), NumberUtils.subtract(BigDecimal.ONE, BigDecimal.ONE));
	}

	public static DecimalFormat getDecimalFormat(int vorkomma, int nachkomma) {
		return NumberUtils.getDecimalFormat(vorkomma, ',', nachkomma, DecimalFormatSign.END);
	}

	public static DecimalFormat getDecimalFormatUnsigned(int vorkomma, int nachkomma) {
		return NumberUtils.getDecimalFormat(vorkomma, ',', nachkomma, DecimalFormatSign.NONE);
	}

	@Test
	public void testFormatDecimal() {
		Assert.assertEquals("1", getDecimalFormatUnsigned(1, 0).format(1L));
		Assert.assertEquals("1", getDecimalFormatUnsigned(1, 0).format(-1L));
		Assert.assertEquals("001", getDecimalFormatUnsigned(3, 0).format(1L));
		Assert.assertEquals("100", getDecimalFormatUnsigned(2, 0).format(100L));

		Assert.assertEquals("1+", getDecimalFormat(1, 0).format(1.1d));
		Assert.assertEquals("1-", getDecimalFormat(1, 0).format(-1.1d));
		Assert.assertEquals("1,0+", getDecimalFormat(1, 1).format(1d));
		Assert.assertEquals("0,0+", getDecimalFormat(1, 1).format(0d));
		Assert.assertEquals("01,00+", getDecimalFormat(2, 2).format(1d));
		Assert.assertEquals("01,00-", getDecimalFormat(2, 2).format(-1d));
		Assert.assertEquals("01,23+", getDecimalFormat(2, 2).format(1.23d));
	}

}
