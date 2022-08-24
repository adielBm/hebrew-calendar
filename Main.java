import java.util.HashMap;

public class Main {

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
    System.out.println(getMoladTishrei(5783));
  }

  public static int getMonthsSinceBaharad(int currentYear) {
    currentYear--;
    int sycles = currentYear / 19;
    int remained_years = currentYear % 19;

    int months = sycles * MONTHS_IN_SYCLE;
    months += remained_years * 12;

    for (int i = 0; i < LEAPYEARS.length; i++) {
      if (LEAPYEARS[i] > remained_years) {
        break;
      }
      months += 1;
    }

    return months;
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


  public static HashMap<String, Object> getMoladTishrei(int currentYear) {
    int MT = moduloWeek(getMtParts(currentYear));
    int dayMt = MT / DAY_AS_PARTS;
    int hoursMt = (MT % DAY_AS_PARTS) / HOUR_AS_PARTS;
    int partsMt = (MT % DAY_AS_PARTS) % HOUR_AS_PARTS;

    HashMap<String, Object> moladTishrei = new HashMap<String, Object>();
    moladTishrei.put("day", dayMt);
    moladTishrei.put("hours", hoursMt);
    moladTishrei.put("parts", partsMt);
    
    return moladTishrei;
  }

}