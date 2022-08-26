import java.util.HashMap;

public class Main {

  public static String[] HEBREW_DAYS = {"Saturday", "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday" };
  public static int[] LEAPYEARS = { 3, 6, 8, 11, 14, 17, 19 };
  public static int MONTHS_IN_SYCLE = 12 * 19 + LEAPYEARS.length;

  public static int HOUR_AS_PARTS = 1080;
  public static int DAY_AS_PARTS = HOUR_AS_PARTS * 24;
  public static int WEEK_AS_PARTS = DAY_AS_PARTS * 7; // 181440
  public static int MONTH_AS_PARTS = (DAY_AS_PARTS * 29) + (HOUR_AS_PARTS * 12) + 793;

  // Remainder of MONTH after removal of the entire weeks. (Week Modulo)
  public static int REMNANT_MONTH = MONTH_AS_PARTS % WEEK_AS_PARTS; // 39673

  // Molad "BaHaRad": (2 days. 5 hours. 204 parts )
  public static int BAHARAD = (DAY_AS_PARTS * 2) + (HOUR_AS_PARTS * 5) + 204; // 57444

  public static void main(String[] args) {

    int year = 5790;

    System.out.println(formatDay(getRoshHashana(year)));
    // System.out.println(getMoladTishrei(year));
    // System.out.println(getMonthsSinceBaharad(year));

  }

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
    
    if (dayMt == 2) {
      System.out.println("dayMt == 2");
    }

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


  public static String formatDay(int day) {
    return HEBREW_DAYS[day];
  }

}