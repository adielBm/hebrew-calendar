import math

LEAP_YEARS = (3, 6, 8, 11, 14, 17, 19)

def isLeapYear(year):
    (cycles, re) = getCycles(year)
    return re + 1 in LEAP_YEARS

def getCycles(year):
    year -= 1
    (re, cycles) = math.modf(year / 19)
    return (cycles, round(re * 19))

def getMonthsSinceCycle(num):
    months = 0
    for y in range(num):
        months += 12
        if y+1 in LEAP_YEARS:
            months += 1
    return months

def getMonthsSinceBaharad(year):
    (cycles, re) = getCycles(year)
    return getMonthsSinceCycle(re) + (cycles * 235)

def getMoladTishrei(year):
    HOUR_AS_PARTS = 1080
    DAY_AS_PARTS = HOUR_AS_PARTS * 24
    WEEK_AS_PARTS = DAY_AS_PARTS * 7  # 181440
    MONTH_AS_PARTS = (DAY_AS_PARTS * 29) + (HOUR_AS_PARTS * 12) + 793

    # Remainder of MONTH after removal of the entire weeks. (Week Modulo)
    REMNANT_MONTH = MONTH_AS_PARTS % WEEK_AS_PARTS  # 39673

    # Molad "Baharad": (2 days. 5 hours. 204 parts )
    BAHARAD = (DAY_AS_PARTS * 2) + (HOUR_AS_PARTS * 5) + 204  # 57444
    # Molad Tishrei as parts.
    mtParts = BAHARAD + (getMonthsSinceBaharad(year) * REMNANT_MONTH)

    # Molad Tishrei after Week Modulo:
    mtParts = mtParts % WEEK_AS_PARTS

    (re, dayMt) = math.modf(mtParts / DAY_AS_PARTS)
    (re, hoursMt) = math.modf(re * 24)
    partsMt = re * 1080

    return (round(dayMt), round(hoursMt), round(partsMt))

def getRh(year):
    (dayMt, hoursMt, partsMt) = getMoladTishrei(year)

    if dayMt == 3 and isLeapYear(year) == False:
        if hoursMt >= 10 or (hoursMt == 9 and partsMt >= 204):
            dayMt += 1
    elif dayMt == 2 and isLeapYear(year-1) == True:
        if hoursMt >= 16 or (hoursMt == 15 and partsMt >= 589):
            dayMt += 1
    elif hoursMt >= 18:
        dayMt += 1

    if dayMt == 1 or dayMt == 4 or dayMt == 6:
        dayMt += 1

    if dayMt == 7:
        dayMt = 0

    return dayMt


def getYearType(year):
  diff = getRh(year+1) - getRh(year)

  if diff < 0: diff += 7
  diff = diff % 7

  if isLeapYear(year):
    if diff == 5:
      return 'ח'
    if diff == 6:
      return 'כ'
    if diff == 0:
      return 'ש'
  else:
    if diff == 3:
      return 'ח'
    if diff == 4:
      return 'כ'
    if diff == 5:
      return 'ש'


def getYearLength(year):
  len = 354
  type = getYearType(year)
  if type == 'ח': 
    len -= 1
  if type == 'ש': 
    len += 1
  if isLeapYear(year): 
    len += 30
  return len

print(getYearLength(5785)) 
print(getMoladTishrei(5785)) 
print(getYearType(5785)) 
print(getRh(5785)) 
print(getCycles(5785)) 
print(getMonthsSinceCycle(5785))
print(getMonthsSinceBaharad(5785)) 



