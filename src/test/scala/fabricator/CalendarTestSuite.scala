package fabricator

import com.github.nscala_time.time.Imports._
import fabricator.enums.{DateRangeType, DateFormat}
import fabricator.enums.DateFormat._
import fabricator.enums.DateRangeType._
import org.testng.annotations.{DataProvider, Test}

class CalendarTestSuite extends BaseTestSuite {

  @Test
  def testCustomConstructor()  {
    val customCalendar = fabricator.Calendar("us")
    assert(customCalendar != null)
  }

  @Test
  def testDefaultDateGetter() = {
    val date = calendar.date.asString()
    if (debugEnabled) logger.debug("Checking default date value " + date)
    assert(date.matches("\\d{2}-\\d{2}-\\d{4}"))
  }

  @DataProvider(name = "dateFormats")
  def dateFormats() = {
    Array(Array(dd_mm_yyyy_SEMICOLON, "\\d{2}:\\d{2}:\\d{4}"),
      Array(mm_dd_yyyy_SEMICOLON, "\\d{2}:\\d{2}:\\d{4}"),
      Array(dd_MM_yyyy_SEMICOLON, "\\d{2}:\\d{2}:\\d{4}"),
      Array(dd_MM_YYYYY_SEMICOLON, "\\d{2}:\\d{2}:\\d{4}"),
      Array(dd_MM_YYYY_BACKSLASH, "\\d{2}/\\d{2}/\\d{4}"),
      Array(dd_MM_YY_BACKSLASH, "\\d{2}/\\d{2}/\\d{2}"),
      Array(dd_MM_yyyy, "\\d{2}-\\d{2}-\\d{4}"),
      Array(dd_MM_yyyy_DOT, "\\d{2}\\.\\d{2}\\.\\d{4}"),
      Array(dd_M_yyyy_DOT, "\\d{2}\\.\\d{1,2}\\.\\d{4}"),
      Array(dd_MM_yyyy_HH, "\\d{2}-\\d{2}-\\d{4} \\d{2}"),
      Array(dd_MM_yyyy_HH_mm, "\\d{2}-\\d{2}-\\d{4} \\d{2}:\\d{2}"),
      Array(dd_MM_yyyy_HH_mm_ss, "\\d{2}-\\d{2}-\\d{4} \\d{2}:\\d{2}:\\d{2}"),
      Array(dd_MM_yyyy_H_m_s, "\\d{1,2}-\\d{1,2}-\\d{4} \\d{1,2}:\\d{1,2}:\\d{1,2}"),
      Array(dd_MM_yyyy_H_m_s_a, "\\d{1,2}-\\d{1,2}-\\d{4} \\d{1,2}:\\d{1,2}:\\d{1,2} \\w{2}"),
      Array(dd_MM_yy_HH_mm, "\\d{1,2}-\\d{1,2}-\\d{2} \\d{1,2}:\\d{1,2}"),
      Array(yyMMdd, "\\d{1,6}"),
      Array(yyyyMMdd, "\\d{1,8}"),
      Array(yyyyMMddHHmm, "\\d{1,12}"),
      Array(yyyy_MM_dd, "\\d{4}-\\d{1,2}-\\d{1,2}"),
      Array(MM_yyyy, "\\d{1,2}-\\d{4}"),
      Array(MM_yy, "\\d{1,2}-\\d{1,2}"),
      Array(HH_mm, "\\d{1,2}:\\d{1,2}"),
      Array(dd_MMMM_yyyy_SPACE, "\\d{1,2} \\w{1,10} \\d{4}"),
      Array(d_MMM_SPACE, "\\d{1,2} \\w{1,12}"),
      Array(dd_MM_yy, "\\d{1,2}-\\d{1,2}-\\d{2}"),
      Array(dd_MM, "\\d{1,2}-\\w{1,2}"),
      Array(dd, "\\d{1,2}")
    )
  }

  @Test(dataProvider = "dateFormats")
  def testDateGetterWithDifferentFormats(format: DateFormat, regex: String) = {
    val date = calendar.date.asString(format)
    if (debugEnabled) logger.debug("Checking date value with " + format + " format :" + date)
    assert(date.matches(regex))
  }

  @Test
  def testDateGetterAsDate() = {
    val year = calendar.year.toInt
    val month = calendar.month.toInt
    val day = calendar.day(year, month).toInt
    val hour = calendar.hour12h.toInt
    val minute = calendar.minute.toInt
    val expectedDate = new DateTime(year, month, day, hour, minute)
    val date = calendar.date
      .inYear(year)
      .inMonth(month)
      .inDay(day)
      .inHour(hour)
      .inMinute(minute)
      .asDate()
    assertResult(expectedDate)(date)
  }


  @Test
  def testAmPm() = {
    var amCount = 0
    var pmCount = 0
    for (i <- 1 to 50){
      val ampm = calendar.ampm
      if (ampm.equals("am")) amCount = amCount + 1 else pmCount = pmCount + 1
      if (debugEnabled) logger.debug("Testing random amPm value : " + ampm)
    }
    assert(amCount > 0 && pmCount > 0)
  }

  @Test
  def testSecond() = {
    val second = calendar.second
    if (debugEnabled) logger.debug("Testing random second value: " + second)
    assert(second.toInt >= 0 && second.toInt <= 60)
  }

  @Test
  def testMinute() = {
    val minute = calendar.minute
    if (debugEnabled) logger.debug("Testing random minute value: " + minute)
    assert(minute.toInt >= 0 && minute.toInt < 60)
  }

  @Test
  def testHour() = {
    val hour24 = calendar.hour24h
    val hour12 = calendar.hour12h
    assert(hour24.toInt >= 0 && hour24.toInt < 24)
    assert(hour12.toInt >= 0 && hour12.toInt < 12)
  }

  @Test
  def testTime() = {
    val time24 = calendar.time24h
    val hour24 = time24.split(":")(0).toInt
    val minute24 = time24.split(":")(1).toInt
    assert(hour24 >= 0 && hour24 < 24 && minute24 >= 0 && minute24 < 60)
    val time = calendar.time12h
    val hour = time.split(":")(0).toInt
    val minute = time.split(":")(1).toInt
    assert(hour >= 0 && hour < 12 && minute >= 0 && minute < 60)
  }

  @Test
  def testDefaultDay() = {
    val day = calendar.day.toInt
    assert(day >= 1 && day <= 31)
  }
  
  @Test
  def testDay() = {
    val year = calendar.year
    val month = calendar.month
    val day = calendar.day(year.toInt, month.toInt)
    if (debugEnabled) logger.debug("Testing random day value: " + day)
    assert(day.toInt >= 0 && day.toInt < 31)
  }
  
  @DataProvider(name = "dayExceptionDP")
  def dayExceptionDP():Array[Array[Any]] = {
    Array(Array(0, 12),
      Array(1, 32),
      Array(32, 10)
    )
  }
  
  @Test(dataProvider = "dayExceptionDP")
  def testDayException(min: Int, max: Int) = {
    try {
      val date = calendar.day(2000, 2, min, max)
    } catch  {
      case e: IllegalArgumentException => assertResult("min and max values should be in [1,31] range")(e.getMessage)
    }
  }

  @Test
  def testCustomDay() = {
    val year = calendar.year.toInt
    val month = calendar.month.toInt
    val day = calendar.day(year, month, 10, 20 ).toInt
    if (debugEnabled) logger.debug("Testing random custom day value in range [10,20]: " + day)
    assert(day >= 10 && day <= 20)
  }

  @Test
  def testMonth() = {
    val monthNumber = calendar.month(asNumber = true)
    if (debugEnabled) logger.debug("Checking random month value numeric: " + monthNumber)
    assert(monthNumber.toInt > 0 && monthNumber.toInt < 12)
    val monthLettered = calendar.month(asNumber = false)
    val months = util.getArrayFromJson("month")
    assert(months.contains(monthLettered))
  }

  @Test
  def testYear() = {
    val year = calendar.year.toInt
    if (debugEnabled) logger.debug("Testing random year value: " + year)
    assert(year >= 1970 && year < 2015)
  }

  @DataProvider
  def dateDP():Array[Array[Any]] = {
    Array(Array(2014, 2, 30, 0, 0, "28-02-2014 00:00"),
      Array(1000, 2, 30, 0, 0, "28-02-1000 00:00"),
      Array(1980, 1, 50, 12, 30, "31-01-1980 12:30"),
      Array(2250, 2, 30, 0, 0, "28-02-2250 00:00"))
  }

  @Test(dataProvider = "dateDP")
  def testDate(year: Int, month: Int, day: Int, hour: Int, minute: Int, expectedResult: String) = {
    val date = calendar.date
                        .inYear(year)
                        .inMonth(month)
                        .inDay(day)
                        .inHour(hour)
                        .inMinute(minute)
                        .asString(dd_MM_yyyy_HH_mm)
    if (debugEnabled) logger.debug("Testing random date: " + date)
    assert(date.equals(expectedResult))
  }

  @Test
  def testDateInTime() = {
    val date = calendar.date.inTime(10, 10).asDate()
    assert(date.getHourOfDay == 10)
    assert(date.getMinuteOfHour == 10)
  }


  @DataProvider
  def datesRangeDP():Array[Array[Any]] = {
    Array(Array(2001, 1, 1, 2010, 1, 1,YEARS, 1, 9),
      Array(2001, 1, 1, 2010, 1, 1,YEARS, 2, 5),
      Array(2001, 1, 1, 2010, 1, 1,MONTHS, 1, 108),
      Array(2001, 1, 1, 2010, 1, 1,MONTHS, 2, 54),
      Array(2001, 1, 1, 2010, 1, 1,WEEKS, 2, 235),
      Array(2001, 1, 1, 2001, 10, 1,DAYS, 10, 28),
      Array(2001, 1, 1, 2001, 10, 1,HOURS, 10, 656),
      Array(2001, 1, 1, 2001, 10, 1,MINUTES, 10, 39306)
    )
  }
  
  @Test(dataProvider = "datesRangeDP")
  def testDateRangeInYears(startYear: Int, startMonth: Int, startDay: Int, endYear: Int, endMonth: Int, endDay: Int, rangeType: DateRangeType, step: Int, expectedSize: Int ) = {
    val datesRange = calendar.datesRange
                             .startYear(startYear)
                             .startMonth(startMonth)
                             .startDay(startDay)
                             .stepEvery(step, rangeType)
                             .endYear(endYear)
                             .endMonth(endMonth)
                             .endDay(endDay)
                             .asList
    assertResult(expectedSize)(datesRange.length)
  }

  @Test
  def testDateRangeWithStartEndDates() = {
    val startDate = new DateTime(2001, 1, 1, 0, 0)
    val endDate = new DateTime(2010, 1, 1, 0, 0)
    val datesRange = calendar.datesRange.startDate(startDate).stepEvery(1, YEARS).endDate(endDate).asList
    assertResult(9)(datesRange.length)
  }

  
  @Test
  def testDateRangeInYearsWithException() = {
    try {
      val datesRange = calendar.datesRange
        .startYear(2010)
        .startMonth(1)
        .startDay(1)
        .stepEvery(0, DAYS)
        .endYear(2011)
        .endMonth(1)
        .endDay(1)
        .asList
    }catch {
      case e: IllegalArgumentException => assertResult("Step should be > 0")(e.getMessage)
    }
  }

  @Test
  def testDateRangeWithCustomFormat() = {
    val dateRange = calendar.datesRange
      .startYear(2001)
      .stepEvery(1, YEARS)
      .endYear(2010)
      .format(dd_MM_yy).asList
    assertResult(9)(dateRange.length)
    for (date <- dateRange) {
      assert(date.matches("\\d{1,2}-\\d{1,2}-\\d{2}"))
    }
  }

  @Test
  def testDateRangeAsArray() = {
    val dateRange = calendar.datesRange
      .startYear(2001)
      .stepEvery(1, YEARS)
      .endYear(2010)
      .format(dd_MM_yy).asArray()
    assertResult(9)(dateRange.length)
    for (date <- dateRange) {
      assert(date.matches("\\d{1,2}-\\d{1,2}-\\d{2}"))
    }
  }
  
  @Test
  def testDateWithFormat() = {
    val format = dd_MM_yyyy
    val year = calendar.year.toInt
    val month = calendar.month.toInt
    val day = calendar.day(year, month).toInt
    val hour = calendar.hour12h.toInt
    val minute = calendar.minute.toInt
    val date = calendar.date
                        .inYear(year)
                        .inMonth(month)
                        .inDay(day)
                        .inHour(hour)
                        .inMinute(minute)
                        .asString(format)
    assertResult(new DateTime(year, month, day, hour, minute).toString(format.getFormat))(date)
  }

  @DataProvider
  def dateWithPeriodDP():Array[Array[Any]] = {
    Array(Array(0, 0, 0, 0, 0, 0, dd_MM_yyyy_SEMICOLON, DateTime.now.toString(dd_MM_yyyy_SEMICOLON.getFormat)),
      Array(1, 0, 0, 0, 0, 0, dd_MM_yyyy_SEMICOLON, DateTime.now.plusYears(1).toString(dd_MM_yyyy_SEMICOLON.getFormat)),
      Array(-1, 0, 0, 0, 0, 0, dd_MM_yyyy_SEMICOLON, DateTime.now.minusYears(1).toString(dd_MM_yyyy_SEMICOLON.getFormat)),
      Array(0, 1, 0, 0, 0, 0, dd_MM_yyyy_SEMICOLON, DateTime.now.plusMonths(1).toString(dd_MM_yyyy_SEMICOLON.getFormat)),
      Array(0, -1, 0, 0, 0, 0, dd_MM_yyyy_SEMICOLON, DateTime.now.minusMonths(1).toString(dd_MM_yyyy_SEMICOLON.getFormat)),
      Array(0, 0, 1, 0, 0, 0, dd_MM_yyyy, DateTime.now.plusWeeks(1).toString(dd_MM_yyyy.getFormat)),
      Array(0, 0, -1, 0, 0, 0, dd_MM_yyyy_SEMICOLON, DateTime.now.minusWeeks(1).toString(dd_MM_yyyy_SEMICOLON.getFormat)),
      Array(0, 0, 0, 1, 0, 0, dd_MM_yyyy_SEMICOLON, DateTime.now.plusDays(1).toString(dd_MM_yyyy_SEMICOLON.getFormat)),
      Array(0, 0, 0, -1, 0, 0, dd_MM_yyyy_SEMICOLON, DateTime.now.minusDays(1).toString(dd_MM_yyyy_SEMICOLON.getFormat)),
      Array(0, 0, 0, 0, 1, 0, dd_MM_yyyy_SEMICOLON, DateTime.now.plusHours(1).toString(dd_MM_yyyy_SEMICOLON.getFormat)),
      Array(0, 0, 0, 0, -1, 0, dd_MM_yyyy_SEMICOLON, DateTime.now.minusHours(1).toString(dd_MM_yyyy_SEMICOLON.getFormat)),
      Array(0, 0, 0, 0, 0, 1, dd_MM_yyyy_SEMICOLON, DateTime.now.plusMinutes(1).toString(dd_MM_yyyy_SEMICOLON.getFormat)),
      Array(0, 0, 0, 0, 0, -1, dd_MM_yyyy_SEMICOLON, DateTime.now.minusMinutes(1).toString(dd_MM_yyyy_SEMICOLON.getFormat)),
      Array(1, 1, 0, 0, 0, 0, dd_MM_yyyy_SEMICOLON, DateTime.now.plusYears(1).plusMonths(1).toString(dd_MM_yyyy_SEMICOLON.getFormat)),
      Array(-1, -1, 0, 0, 0, 0, dd_MM_yyyy_SEMICOLON, DateTime.now.minusYears(1).minusMonths(1).toString(dd_MM_yyyy_SEMICOLON.getFormat)),
      Array(3, 3, 3, 0, 0, 0, dd_MM_yyyy_SEMICOLON, DateTime.now.plusYears(3).plusMonths(3).plusWeeks(3).toString(dd_MM_yyyy_SEMICOLON.getFormat)),
      Array(-1, -1, -1, 0, 0, 0, dd_MM_yyyy_SEMICOLON, DateTime.now.minusYears(1).minusMonths(1).minusWeeks(1).toString(dd_MM_yyyy_SEMICOLON.getFormat)),
      Array(1, 1, 1, 1, 0, 0, dd_MM_yyyy_SEMICOLON, DateTime.now.plusYears(1).plusMonths(1).plusWeeks(1).plusDays(1).toString(dd_MM_yyyy_SEMICOLON.getFormat)),
      Array(-5, -5, -5, -5, 0, 0, dd_MM_yyyy_SEMICOLON, DateTime.now.minusYears(5).minusMonths(5).minusWeeks(5).minusDays(5).toString(dd_MM_yyyy_SEMICOLON.getFormat)),
      Array(1, 1, 1, 1, 1, 0, dd_MM_yyyy_HH_SEMICOLON, DateTime.now.plusYears(1).plusMonths(1).plusWeeks(1).plusDays(1).plusHours(1).toString(dd_MM_yyyy_HH_SEMICOLON.getFormat)),
      Array(-1, -1, -1, -1, -1, 0, dd_MM_yyyy_HH_SEMICOLON, DateTime.now.minusYears(1).minusMonths(1).minusWeeks(1).minusDays(1).minusHours(1).toString(dd_MM_yyyy_HH_SEMICOLON.getFormat)),
      Array(10, 10, 10, 10, 10, 10, dd_MM_yyyy_HH_SEMICOLON, DateTime.now.plusYears(10).plusMonths(10).plusWeeks(10).plusDays(10).plusHours(10).plusMinutes(10).toString(dd_MM_yyyy_HH_SEMICOLON.getFormat)),
      Array(-100, -100, -100, -100, -100, -100, dd_MM_yyyy_HH_SEMICOLON, DateTime.now.minusYears(100).minusMonths(100).minusWeeks(100).minusDays(100).minusHours(100).minusMinutes(100).toString(dd_MM_yyyy_HH_SEMICOLON.getFormat))
    )
  }

  @Test(dataProvider = "dateWithPeriodDP")
  def testDateWithPeriod(year: Int, month: Int, week: Int, day: Int, hour: Int, minute: Int, format: DateFormat, expectedDate: String) = {
    val date = calendar.dateRelative(year, month, week, day, hour, minute, format)
    if (debugEnabled) logger.debug("Testing random date with dateWithPeriod method: " + date)
    assertResult(expectedDate)(date)
  }

  @Test
  def testDateWithPeriodWithDefaultFormat() = {
    val date = calendar.dateRelative(0, 0, 0, 0, 0, 0)
    assertResult(DateTime.now.toString("dd-MM-yyyy HH:mm"))(date)
  }

}
