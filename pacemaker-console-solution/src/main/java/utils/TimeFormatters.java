package utils;

import org.joda.time.format.PeriodFormatterBuilder;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.PeriodFormatter;

import org.joda.time.DateTime;
import org.joda.time.Duration;

public class TimeFormatters {

  static PeriodFormatter periodFormatter = new PeriodFormatterBuilder().printZeroAlways()
      .appendHours()
      .appendSeparator(":")
      .appendMinutes()
      .appendSeparator(":")
      .appendSeconds()
      .toFormatter();

  static DateTimeFormatter dateFormatter = DateTimeFormat.forPattern("dd:MM:yyyy HH:mm:ss");

  public static DateTime parseDateTime(String dateTime) {
    return new DateTime(dateFormatter.parseDateTime(dateTime));
  }

  public static String parseDateTime(DateTime dateTime) {
    return dateFormatter.print(dateTime);
  }

  public static Duration parseDuration(String duration) {
    return periodFormatter.parsePeriod(duration).toStandardDuration();
  }

  public static String parseDuration(Duration duration) {
    return periodFormatter.print(duration.toPeriod());
  }
}