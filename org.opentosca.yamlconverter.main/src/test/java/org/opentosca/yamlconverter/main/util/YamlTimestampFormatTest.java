package org.opentosca.yamlconverter.main.util;

import static org.junit.Assert.fail;

import java.text.ParseException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import org.junit.Assert;
import org.junit.Test;
import org.opentosca.yamlconverter.main.utils.YamlTimestampFormatter;

public class YamlTimestampFormatTest {

	@Test
	public void parse() {
		Calendar date;
		try {
			date = YamlTimestampFormatter.parse("2001-12-15T02:59:43.1Z");
			Assert.assertEquals(getDate(2001, 12, 15, 2, 59, 43, 100, TimeZone.getTimeZone("GMT+0")).getTime(), date.getTime());

			date = YamlTimestampFormatter.parse("2001-12-14t21:59:43.10-05:00");
			Assert.assertEquals(getDate(2001, 12, 14, 21, 59, 43, 100, TimeZone.getTimeZone("GMT-5")).getTime(), date.getTime());

			date = YamlTimestampFormatter.parse("2001-12-14 21:59:43.10 -5");
			Assert.assertEquals(getDate(2001, 12, 14, 21, 59, 43, 100, TimeZone.getTimeZone("GMT-5")).getTime(), date.getTime());

			date = YamlTimestampFormatter.parse("2001-12-15 02:59:43.10");
			Assert.assertEquals(getDate(2001, 12, 15, 2, 59, 43, 100, TimeZone.getDefault()).getTime(), date.getTime());

			date = YamlTimestampFormatter.parse("2002-12-14");
			Assert.assertEquals(getDate(2002, 12, 14, 0, 0, 0, 0, TimeZone.getDefault()).getTime(), date.getTime());

			date = YamlTimestampFormatter.parse("1902-12-14");
			Assert.assertEquals(getDate(1902, 12, 14, 0, 0, 0, 0, TimeZone.getDefault()).getTime(), date.getTime());

			date = YamlTimestampFormatter.parse("2001-12-14 21:59:43.10");
			Assert.assertEquals(getDate(2001, 12, 14, 21, 59, 43, 100, TimeZone.getDefault()).getTime(), date.getTime());

		} catch (final ParseException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	/**
	 * Convenience method for creating a calendar which is initialized with the given values
	 */
	private Calendar getDate(int year, int month, int day, int hour, int minute, int second, int millisecond, TimeZone timezone) {
		final GregorianCalendar cal = new GregorianCalendar(timezone);
		cal.set(year, month - 1, day, hour, minute, second);
		cal.set(Calendar.MILLISECOND, millisecond);
		return cal;
	}

	@Test
	public void format() {
		final Calendar date = getDate(2001, 12, 14, 10, 59, 43, 100, TimeZone.getTimeZone("GMT+0"));
		final String actual = YamlTimestampFormatter.format(date);
		final String expected = "2001-12-14T10:59:43.100Z";
		Assert.assertEquals(expected, actual);
	}
}