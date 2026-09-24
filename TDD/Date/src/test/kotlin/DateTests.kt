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

    @Test fun `Test equality of dates that represent the same date`() {
        val sut = Date(2025, 3, 2)
        assertTrue(sut == sut)             //   sut.equals(sut)
        assertTrue(sut == Date(2025, 3, 2))
    }

    @Test fun `Tests inequality between dates and dates with another type`() {
        val sut = Date(2025, 3, 2)
        assertNotEquals(sut, Date(2025, 4, 1))
        val any :Any = 2025
        assertNotEquals(sut, any)  // Compare with Int
        val dn :Date? = null
        assertNotEquals(sut, dn)   // Compare with null
        assertNotEquals(dn, sut)   // Compare null with Date
    }

    @Test
    fun `Coherence between equals and hashCode`() {
        val sut = Date(2025, 3, 2)
        assertEquals(sut.hashCode(), sut.hashCode())

        val d2 = Date(2025, 3, 2)
        assertEquals(sut, d2)
        assertEquals(sut.hashCode(), d2.hashCode()) // Devem ser iguais

        val d3 = Date(2025, 4, 2)
        assertNotEquals(sut, d3)
        assertNotEquals(sut.hashCode(), d3.hashCode()) // Devem ser diferentes
    }

    @Test
    fun `Compare dates`() {
        val sut = Date(2025, 3, 2)
        assertTrue(sut < Date(2025, 3, 4)) // sut.compareTo(Date(...))
        assertTrue(Date(2025, 4, 2) >= sut)
        assertTrue(sut <= Date(2050, 3, 2))
        assertTrue(sut > Date(2025, 3, 1))
    }

    @Test
    fun `String representation of a date`() {
        val sut = Date(2025, 3, 2)
        assertEquals("2025-03-02", sut.toString())
    }
}