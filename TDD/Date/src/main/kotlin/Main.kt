private val daysOfMonth = listOf(31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31)
private const val GREGORIAN_START_YEAR = 1582
private const val YEAR_LIMIT = 3000
private val YEAR_RANGE = GREGORIAN_START_YEAR until YEAR_LIMIT
private val MONTHS_IN_YEAR = daysOfMonth.size

private const val DAY_BITS = 5   // 0..31
private const val MONTH_BITS = 4 // 0..15
private const val YEAR_BITS = 12 // 0..4095

@JvmInline
value class Date private constructor(val bits: Int){

    constructor(year: Int, month: Int = 1, day: Int = 1): this(
        day or (month shl DAY_BITS) or (year shl (MONTH_BITS + DAY_BITS))
    )

    val day: Int get() = bits and ((1 shl DAY_BITS) - 1)
    val month: Int get() = (bits shr DAY_BITS) and ((1 shl MONTH_BITS) - 1)
    val year: Int get() = bits shr (DAY_BITS + MONTH_BITS)

    val leapYear get() = (year % 4 == 0 && year % 100 != 0) || year % 400 == 0
    val lastDayOfMonth get() = if (month == 2 && leapYear) 29 else daysOfMonth[month - 1]

    init {
        require(year in YEAR_RANGE) { "Invalid year=$year" }
        require(month in 1..MONTHS_IN_YEAR) { "Invalid month=$month" }
        require(day in 1..lastDayOfMonth) { "Invalid day=$day" }
    }

    operator tailrec fun plus(days: Int): Date {
        require(days >= 0)

        if (day + days <= lastDayOfMonth)  return Date(year, month, day + days)

        return (if (month < MONTHS_IN_YEAR) Date(year, month + 1, 1)
        else Date(year+1, 1, 1)).plus(days - (lastDayOfMonth - day + 1))
    }

    //override operator fun equals(other: Any?) =
    //    other is Date && year == other.year && month == other.month && day == other.day

    //override fun hashCode(): Int = day + month*31 + year*366
    operator fun compareTo(other: Date): Int = this.bits - other.bits
    override fun toString() = "%04d-%02d-%02d".format(year, month, day)
}

operator fun Int.plus(date: Date) = date.plus(this)


fun main() {


}