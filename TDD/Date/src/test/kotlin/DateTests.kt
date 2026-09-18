import kotlin.test.*

class DateTests {

    @Test
    fun createDate() {
        val sut = Date(2026, 9, 18)
        assertEquals(2026, sut.year, "Erro na avaliação do ano")
        assertEquals(9, sut.month)
        assertEquals(18, sut.day)
    }

    @Test
    fun `Create Date only with year and month`() {
        val sut = Date(2026, 9)
        assertEquals(2026, sut.year, "Erro na avaliação do ano")
        assertEquals(9, sut.month)
        assertEquals(1, sut.day)
    }

    @Test
    fun `Create Date only with yearh`() {
        val sut = Date(2026)
        assertEquals(2026, sut.year, "Erro na avaliação do ano")
        assertEquals(1, sut.month)
        assertEquals(1, sut.day)
    }

    @Test
    fun `Create Date only with year and day`() {
        val sut = Date(2025, day = 20)
        assertEquals(2025, sut.year)
        assertEquals(1, sut.month)
        assertEquals(20, sut.day)
    }

    @Test
    fun `Date with a leap year`() {
        val sut = Date(2020)
        assertTrue(sut.leapYear) // Equiv. a: assertEquals(true, sut.leapYear)
    }
    @Test
    fun `Date with a leap year 400`() {
        val sut = Date(2400)
        assertTrue(sut.leapYear) // Equiv. a: assertEquals(true, sut.leapYear)
    }    @Test
    fun `Date with a non-leap year`() {
        val sut = Date(2025)
        assertFalse(sut.leapYear) // Equiv. a: assertEquals(false, sut.leapYear)
    }

    @Test fun `February has 28 days in non-leap year`() {
        val sut = Date(2025, 2)
        assertEquals(sut.lastDayOfMonth, 28)
    }

    @Test fun `February has 29 days in leap year`() {
        val sut = Date(2020, 2)
        assertEquals(sut.lastDayOfMonth, 29)
    }

    @Test
    fun `Invalid month`() {
        val ex = assertFailsWith<IllegalArgumentException> { Date(2025, 0) }
        assertEquals("Invalid month=0", ex.message)
    }

    @Test
    fun `Invalid day`() {
        val ex = assertFailsWith<IllegalArgumentException> { Date(2025, 2, 29) }
        assertEquals("Invalid day=29", ex.message)
    }

    @Test fun `Add days to a date in the same month`() {
        val sut = Date(2025, 3, 2) + 5 // Date(2025, 3, 2).plus(5)
        assertEquals(2025, sut.year)
        assertEquals(3, sut.month)
        assertEquals(7, sut.day)
    }
    @Test fun `Add days to a date in the same year`() {
        val sut = 33 + Date(2025, 3, 2) //Int(33).plus(Date(2025, 3, 2)))
        assertEquals(2025, sut.year)
        assertEquals(4, sut.month)
        assertEquals(4, sut.day)
    }
    @Test fun `Add days to a date in the next year`() {
        val sut = Date(2025, 3, 2) + 366
        assertEquals(2026, sut.year)
        assertEquals(3, sut.month)
        assertEquals(3, sut.day)
    }
}