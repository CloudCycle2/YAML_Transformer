package org.opentosca.yamlconverter.main.utils;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.xml.bind.DatatypeConverter;

/**
 * Tries to parse the given String in many different formats.
 */
public class YamlTimestampFormatter {
	// @formatter:off
	public final static DateFormat[] TIMEPATTERNS = {
		new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SS"),
		new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S"),
		new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"),
		new SimpleDateFormat("yyyy-MM-dd")
	};
	// @formatter:on

	private YamlTimestampFormatter() {

	}

	/**
	 * Outputs the given calendar in iso8601 format.
	 *
	 * @param calendar
	 * @return
	 */
	public static String format(Calendar calendar) {
		return DatatypeConverter.printDateTime(calendar);
	}

	/**
	 * Parses the given timestamp string into a Calendar object
	 *
	 * @param timestamp
	 * @return parsed calendar. Cannot be <code>NULL</code>.
	 * @throws ParseException if the parsing failed
	 */
	public static Calendar parse(final String timestamp) throws ParseException {
		final GregorianCalendar cal = new GregorianCalendar();

		// try milliseconds
		try {
			final long millis = Long.parseLong(timestamp);
			cal.setTimeInMillis(millis);
			return cal;
		} catch (final NumberFormatException e) {
			// ignore
		}

		// try iso8601
		try {
			return DatatypeConverter.parseDateTime(reformatForIso8601(timestamp));
		} catch (final IllegalArgumentException e) {
			// ignore
		}

		// try various other formats
		for (final DateFormat format : TIMEPATTERNS) {
			try {
				format.setLenient(true);
				cal.setTime(format.parse(timestamp));
				return cal;
			} catch (final ParseException e) {
				// ignore
			}
		}
		throw new ParseException("Could not parse the date " + timestamp, 0);
	}

	private static String reformatForIso8601(String timestamp) {
		// reformat the given string into an easier to parse iso8601 date string
		timestamp = timestamp.trim().toUpperCase();
		final int dateIndex = timestamp.indexOf("-");
		final int timeIndex = timestamp.indexOf(":");
		boolean hasDateAndTime = false;
		boolean hasSpaceBetweenDateAndTime = false;
		int gmtIndex = -1;
		// find out properties of the string
		if (dateIndex > 0 && dateIndex < timeIndex) {
			hasDateAndTime = true;
		}
		if (hasDateAndTime && timestamp.indexOf(" ") < timeIndex && timestamp.indexOf(" ") > 0) {
			hasSpaceBetweenDateAndTime = true;
		}
		// no space! We need a 'T' at this place for iso8601
		if (hasSpaceBetweenDateAndTime) {
			timestamp = timestamp.replaceFirst(" ", "T");
		}
		// no more empty space
		timestamp = timestamp.replaceAll(" ", "");
		// do we have a timezone given?
		int index = timestamp.lastIndexOf("-");
		if (index > timeIndex && timeIndex > 0) {
			gmtIndex = index;
		}
		index = timestamp.lastIndexOf("+");
		if (index > timeIndex && timeIndex > 0) {
			gmtIndex = index;
		}
		// reformat the timezone
		if (gmtIndex >= 0) {
			String gmtString = timestamp.substring(gmtIndex + 1);
			final DecimalFormat leadingZeros = new DecimalFormat("#00");
			// three cases: 03:00, 03 or 0300. But we want 03:00
			try {
				final int gmt = Integer.parseInt(gmtString);
				if (gmt > 24) {
					// case: 0300
					gmtString = leadingZeros.format(gmt / 100) + ":" + leadingZeros.format(gmt % 100);
				} else {
					// case: 03
					gmtString = leadingZeros.format(gmt) + ":00";
				}
				timestamp = timestamp.substring(0, gmtIndex + 1) + gmtString;
			} catch (final NumberFormatException e) {
				// case: 03:00
				// do nothing, it is already correct
			}
		}
		return timestamp;
	}
}
