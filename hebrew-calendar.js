/**
 * Hebrew Calendar
 * This program calculates the Hebrew calendar for a given year.
 * It calculates the Molad Tishrei, Rosh Hashanah, Year type, Year length, and Passover day.
 * The program is based on the Hebrew calendar rules.
 */

/**
 * Leap years in the 19-year cycle.
 */
const LEAP_YEARS = [3, 6, 8, 11, 14, 17, 19];

/**
 * Split a floating number into two parts, the first part is the decimal part and the second part is the integer part.
 * @param {number} num 
 * @returns {number[]} Array of two numbers.
 */
function splitFloat(num) {
  return [num - Math.floor(num), Math.floor(num)];
}

/**
 * Check if a given year is a leap year.
 * Leap years in the Hebrew calendar are years 3, 6, 8, 11, 14, 17, and 19 in the 19-year cycle.
 * @param {*} year 
 * @returns {boolean} True if the year is a leap year, otherwise false.
 */
function isLeapYear(year) {
  return LEAP_YEARS.includes(year % 19);
}

/**
 * Get the cycles and remainder of a given year in the 19-year cycle.
 * @param {*} year The year to get its cycles and remainder.
 * @returns {number[]} Array of two numbers, the first number is the cycles and the second number is the remainder.
 */
function getCycles(year) {
  year -= 1;
  const [re, cycles] = splitFloat(year / 19);
  return [cycles, Math.round(re * 19)];
}

/**
 * Get the number of months since the beginning of the cycle.
 * @param {*} num The number of the cycle.
 * @returns {number} The number of months since the beginning of the cycle.
 */
function getMonthsSinceCycle(num) {
  let months = 0;
  for (let y = 1; y <= num; y++) {
    months += 12;
    if (LEAP_YEARS.includes(y)) {
      months += 1;
    }
  }
  return months;
}

/**
 * Get the number of months since the Molad Baharad.
 * 
 * **Molad Baharad** (2 days, 5 hours, and 204 parts) is the time of the new moon of the month of Tishrei in the year 1 of the Hebrew calendar.
 * @param {*} year The year to get the number of months since the Molad Baharad.
 * @returns {number} The number of months since the Molad Baharad.
 */
function getMonthsSinceBaharad(year) {
  const [cycles, re] = getCycles(year);
  return getMonthsSinceCycle(re) + cycles * 235;
}

/**
 * Get the Molad Tishrei for a given year.
 * @param {*} year The year to get its Molad Tishrei.
 * @returns {number[]} Array of three numbers, the first number is the day, the second number is the hours, and the third number is the parts.
 */
function getMoladTishrei(year) {
  const HOUR_AS_PARTS = 1080;
  const DAY_AS_PARTS = HOUR_AS_PARTS * 24;
  const WEEK_AS_PARTS = DAY_AS_PARTS * 7; // 181440
  const MONTH_AS_PARTS = DAY_AS_PARTS * 29 + HOUR_AS_PARTS * 12 + 793;

  // Remainder of MONTH after removal of the entire weeks. (Week Modulo)
  const REMNANT_MONTH = MONTH_AS_PARTS % WEEK_AS_PARTS; // 39673

  // Molad "Baharad": (2 days. 5 hours. 204 parts )
  const BAHARAD = DAY_AS_PARTS * 2 + HOUR_AS_PARTS * 5 + 204; // 57444

  // Molad Tishrei as parts.
  let mtParts = BAHARAD + getMonthsSinceBaharad(year) * REMNANT_MONTH;

  // Molad Tishrei after Week Modulo:
  mtParts = mtParts % WEEK_AS_PARTS;

  let re, dayMt, hoursMt, partsMt;
  [re, dayMt] = splitFloat(mtParts / DAY_AS_PARTS);
  [re, hoursMt] = splitFloat(re * 24);
  partsMt = re * 1080;

  return [Math.round(dayMt), Math.round(hoursMt), Math.round(partsMt)];
}

/**
 * Get the weekday of Rosh Hashanah for a given year.
 * @param {*} year The year to get the weekday of Rosh Hashanah.
 * @returns {number} The weekday of Rosh Hashanah. (0: Saturday, 1: Sunday, ..., 6: Friday)  
 */
function getRh(year) {
  // Get the Molad Tishrei (Mt) for the given year. 
  let [dayMt, hoursMt, partsMt] = getMoladTishrei(year);

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


/**
 * Get the type of the year for a given year.
 * 
 * The type of the year is one of the following:
 * - **Deficient (חסרה)**: Heshvan & Kislev are both 29 days. (353/383 days)
 * - **Regular (כסדרה)**: Heshvan is 29 days and Kislev is 30 days. (354/384 days)
 * - **Complete (שלמה)**: Heshvan & Kislev are both 30 days. (355/385 days)
 * 
 * @param {*} year 
 * @returns {string} The type of the year.
 * - "ח" for Deficient year.
 * - "כ" for Regular year.
 * - "ש" for Complete year.
 */
function getYearType(year) {
  let diff = getRh(year + 1) - getRh(year);

  if (diff < 0) diff += 7;
  diff = diff % 7;

  if (isLeapYear(year)) {
    if (diff == 5) return "ח";
    if (diff == 6) return "כ";
    if (diff == 0) return "ש";
  } else {
    if (diff == 3) return "ח";
    if (diff == 4) return "כ";
    if (diff == 5) return "ש";
  }
}

/**
 * Get the length of the year for a given year.
 * @param {*} year 
 * @returns {number} The length of the year.
 */
function getYearLength(year) {
  let len = 354;
  const type = getYearType(year);
  if (type == "ח") len -= 1;
  if (type == "ש") len += 1;
  if (isLeapYear(year)) len += 30;
  return len;
}

/**
 * Get the day of Passover for a given year.
 * @param {number} year
 * @returns {number} Day of Passover.
 */
function getPassoverDay(year) {
  const nextRoshHashana = getRh(year + 1);
  let passover = nextRoshHashana + 5;
  passover = passover % 7;
  return passover;
}

/**
 * Print in fancy table deatails calculated in this program for a given year.
 * @param {number} year
 */
function printHebrewCalendar(year) {
  const [cycles, re] = getCycles(year);
  const monthsSinceCycle = getMonthsSinceCycle(re);
  const monthsSinceBaharad = getMonthsSinceBaharad(year);
  const moladTishrei = getMoladTishrei(year);
  const rh = getRh(year);
  const yearType = getYearType(year);
  const yearLength = getYearLength(year);
  const passoverDay = getPassoverDay(year);
  const leapYear = isLeapYear(year);

  console.log(`Hebrew Calendar for year ${year}`);
  console.table({
    Cycles: cycles,
    Remainder: re,
    "Months since cycle": monthsSinceCycle,
    "Months since Baharad": monthsSinceBaharad,
    "Molad Tishrei": moladTishrei.join(","),
    "Rosh Hashanah": rh,
    "Year type": yearType,
    "Year length": yearLength,
    "Passover day": passoverDay,
    "Leap year": leapYear,
  });
}

printHebrewCalendar(5788);
