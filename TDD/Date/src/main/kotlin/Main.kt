private val daysOfMonth = listOf(31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31)
private const val GREGORIAN_START_YEAR = 1582
private const val YEAR_LIMIT = 3000
private val YEAR_RANGE = GREGORIAN_START_YEAR until YEAR_LIMIT
private val MONTHS_IN_YEAR = daysOfMonth.size

class Date (val year: Int, val month: Int = 1, val day: Int = 1){

    val leapYear get() = (year % 4 == 0 && year % 100 != 0) || year % 400 == 0
    val lastDayOfMonth get() = if (month == 2 && leapYear) 29 else daysOfMonth[month - 1]

    init {
        require(year in YEAR_RANGE) { "Invalid year=$year" }
        require(month in 1..MONTHS_IN_YEAR) { "Invalid month=$month" }
        require(day in 1..lastDayOfMonth) { "Invalid day=$day" }
    }

    operator fun plus(days: Int): Date {
        require(days >= 0)


        if (day + days <= lastDayOfMonth)  return Date(year, month, day + days)

        return (if (month < MONTHS_IN_YEAR) Date(year, month + 1, 1)
        else Date(year+1, 1, 1)).plus(days - (lastDayOfMonth - day +  + 1))
    }
}

operator fun Int.plus(date: Date) = date.plus(this)

fun main() {

    val d = Date(2025, 0, 18)
    println(d.lastDayOfMonth)
}