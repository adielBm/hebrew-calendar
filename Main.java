import java.util.HashMap;

public class Main {

  public static void main(String[] args) {
    System.out.println(formatDay(getWeekDayByDate(5783, "nisan", 15)));
  }

  public static final String[] HEBREW_DAYS = { "Saturday", "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday",
      "Friday" };
  public static final boolean[] LEAPS_MONTHS = { true, false, true, false, true, true, false, true, false, true, false,
      true, false };

  public static final int[] LEAPYEARS = { 3, 6, 8, 11, 14, 17, 19 };
  public static final int MONTHS_IN_SYCLE = 12 * 19 + LEAPYEARS.length;

  public static final int HOUR_AS_PARTS = 1080;
  public static final int DAY_AS_PARTS = HOUR_AS_PARTS * 24;
  public static final int WEEK_AS_PARTS = DAY_AS_PARTS * 7; // 181440
  public static final int MONTH_AS_PARTS = (DAY_AS_PARTS * 29) + (HOUR_AS_PARTS * 12) + 793;

  // Remainder of MONTH after removal of the entire weeks. (Week Modulo)
  public static final int REMNANT_MONTH = MONTH_AS_PARTS % WEEK_AS_PARTS; // 39673

  // Molad "BaHaRad": (2 days. 5 hours. 204 parts )
  public static final int BAHARAD = (DAY_AS_PARTS * 2) + (HOUR_AS_PARTS * 5) + 204; // 57444



  public static int getMonthsSinceBaharad(int currentYear) {
    currentYear--;
    int sycles = currentYear / 19;
    int remained_years = currentYear % 19;

    int months = sycles * MONTHS_IN_SYCLE;

    for (int i = 1; i <= remained_years; i++) {
      months += 12;
      if (isLeapYear(i)) {
        months += 1;
      }
    }
    // months += remained_years * 12;

    // for (int i = 0; i < LEAPYEARS.length; i++) {
    // if (LEAPYEARS[i] > remained_years) {
    // break;
    // }
    // months += 1;
    // }

    return months;
  }

  /**
   * returns true if the year is an Hebrew leap year.
   */
  public static boolean isLeapYear(int year) {
    if ((((7 * year) + 1) % 19) < 7)
      return true;
    else
      return false;
  }

  public static long getMtParts(int currentYear) {
    long mt;
    mt = (long) BAHARAD + (long) getMonthsSinceBaharad(currentYear) * (long) REMNANT_MONTH;
    return mt;
  }

  public static int moduloWeek(Object parts) {
    long partsModulo = (long) parts % WEEK_AS_PARTS;
    return (int) partsModulo;
  }

  public static HashMap<String, Integer> getMoladTishrei(int currentYear) {
    int MT = moduloWeek(getMtParts(currentYear));
    int dayMt = MT / DAY_AS_PARTS;
    int hoursMt = (MT % DAY_AS_PARTS) / HOUR_AS_PARTS;
    int partsMt = (MT % DAY_AS_PARTS) % HOUR_AS_PARTS;

    HashMap<String, Integer> moladTishrei = new HashMap<String, Integer>();
    moladTishrei.put("day", (int) dayMt);
    moladTishrei.put("hours", (int) hoursMt);
    moladTishrei.put("parts", (int) partsMt);

    return moladTishrei;
  }

  public static int getRoshHashana(int year) {
    HashMap<String, Integer> MT = getMoladTishrei(year);

    int dayMt = (int) MT.get("day");
    int hoursMt = (int) MT.get("hours");
    int partsMt = (int) MT.get("parts");

    if (dayMt == 3 && isLeapYear(year) == false) {
      if (hoursMt >= 10 || (hoursMt == 9 && partsMt >= 204)) {
        dayMt += 1;
      }
    } else if (dayMt == 2 && isLeapYear(year - 1) == true) {
      if (hoursMt >= 16 || (hoursMt == 15 && partsMt >= 589)) {
        dayMt += 1;
      }
    } else if (hoursMt >= 18) {
      dayMt += 1;
    }

    if (dayMt == 1 || dayMt == 4 || dayMt == 6) {
      dayMt += 1;
    }

    if (dayMt == 7) {
      dayMt = 0;
    }

    return dayMt;
  }

  public static int getPassover(int year) {
    int nextRoshHashana = getRoshHashana(year + 1);
    int passover = nextRoshHashana + 5;
    passover = passover % 7;
    return passover;
  }

  /*
   * True if Heshvan is long in Hebrew year.
   */
  public static boolean isCheshvanLong(int year) {

    int diff = getDifferenceToNextYear(year);

    if (isLeapYear(year)) {
      if (diff == 0)
        return true;
    } else {
      if (diff == 5)
        return true;
    }
    return false;
  }

  /*
   * True if Kislev is short in Hebrew year.
   */
  public static boolean isKislevShort(int year) {

    int diff = getDifferenceToNextYear(year);

    if (isLeapYear(year)) {
      if (diff == 5)
        return true;
    } else {
      if (diff == 3)
        return true;
    }
    return false;
  }

  /*
   * The difference in days to next year. with week modulo.
   */
  public static int getDifferenceToNextYear(int year) {
    int diff = getRoshHashana(year + 1) - getRoshHashana(year);
    if (diff < 0)
      diff += 7;
    return diff % 7;
  }

  // public static char getYearType(int year) {

  // int diff = getRoshHashana(year + 1) - getRoshHashana(year);

  // if (diff < 0)
  // diff += 7;
  // diff = diff % 7;

  // if (isLeapYear(year)) {
  // if (diff == 5)
  // return 'ח';
  // if (diff == 6)
  // return 'כ';
  // if (diff == 0)
  // return 'ש';
  // } else {
  // if (diff == 3)
  // return 'ח';
  // if (diff == 4)
  // return 'כ';
  // if (diff == 5)
  // return 'ש';
  // }
  // return 0;
  // }

  public static String formatDay(int day) {
    return HEBREW_DAYS[day];
  }

  public static int getWeekDayByDate(int year, String monthStr, int day) {

    int month = getIndexMonth(year, monthStr);

    int roshHashana = getRoshHashana(year);

    int weekDay = roshHashana;

    for (int i = 1; i <= month; i++) {
      if (i > 1) {
        weekDay += getDaysInMonth(year, i - 1);
      }
    }
    weekDay += day - 1;
    weekDay = weekDay % 7;
    return weekDay;
  }

  public static boolean isLeapMonth(int year, int month) {

    if (isCheshvanLong(year) && month == 2) {
      return true;
    }

    if (isKislevShort(year) && month == 3) {
      return false;
    }

    if (month >= 6) {
      if (isLeapYear(year)) {
        return LEAPS_MONTHS[month - 1];
      } else {
        return LEAPS_MONTHS[month];
      }
    }

    return LEAPS_MONTHS[month - 1];

  }

  public static int getDaysInMonth(int year, int month) {
    if (isLeapMonth(year, month)) {
      return 30;
    } else {
      return 29;
    }
  }

  public static int getIndexMonth(int year, String month) {

    boolean isLeapYear = isLeapYear(year);
    int index;

    switch (month.toUpperCase()) {
      case "TISHREI": 
        index = 1;
        break;
      case "CHESHVAN": 
        index = 2;
        break;
      case "KISLEV": 
        index = 3;
        break;
      case "TEVET": 
        index = 4;
        break;
      case "SHVAT": 
        index = 5;
        break;
      case "ADAR": 
        index = 6;
        if (isLeapYear) index++;
        break;
      case "ADARI": 
        index = 6;
        break;
      case "ADARII": 
        index = 7;
        break;
      case "NISAN": 
        index = 7;
        if (isLeapYear) index++;
        break;
      case "IYAR": 
        index = 8;
        if (isLeapYear) index++;
        break;
      case "SIVAN": 
        index = 9;
        if (isLeapYear) index++;
        break;
      case "TAMMUZ": 
        index = 10;
        if (isLeapYear) index++;
        break;
      case "AV": 
        index = 11;
        if (isLeapYear) index++;
        break;
      case "ELUL": 
        index = 12;
        if (isLeapYear) index++;
        break;
      default:
        return 1;
    }

    return index;
  }

}