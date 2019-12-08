package lesson11.task1

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Tag
import java.lang.ArithmeticException

internal class UnsignedBigIntegerTest {

    @Test
    @Tag("Normal")
    fun plus() {
        assertEquals(UnsignedBigInteger(4), UnsignedBigInteger(2) + UnsignedBigInteger(2))
        assertEquals(UnsignedBigInteger(198), UnsignedBigInteger(99) + UnsignedBigInteger(99))
        assertEquals(UnsignedBigInteger("9087654330"), UnsignedBigInteger("9087654329") + UnsignedBigInteger(1))
        assertEquals(UnsignedBigInteger("9087654330"), UnsignedBigInteger("1") + UnsignedBigInteger("9087654329"))
    }

    @Test
    @Tag("Normal")
    fun minus() {
        assertEquals(UnsignedBigInteger(0), UnsignedBigInteger(1000) - UnsignedBigInteger(1000))
        assertEquals(UnsignedBigInteger(1), UnsignedBigInteger(1000) - UnsignedBigInteger(999))
        assertEquals(UnsignedBigInteger(2), UnsignedBigInteger(21) - UnsignedBigInteger(19))
        assertEquals(UnsignedBigInteger(5), UnsignedBigInteger(19) - UnsignedBigInteger(14))
        assertEquals(UnsignedBigInteger(2), UnsignedBigInteger(4) - UnsignedBigInteger(2))
        assertEquals(UnsignedBigInteger("9087654329"), UnsignedBigInteger("9087654330") - UnsignedBigInteger(1))
        assertThrows(ArithmeticException::class.java) {
            UnsignedBigInteger(2) - UnsignedBigInteger(4)
        }
    }

    @Test
    @Tag("Hard")
    fun times() {
        assertEquals(
            UnsignedBigInteger("152399025"),
            UnsignedBigInteger("12345") * UnsignedBigInteger("12345")
        )
        assertEquals(
            UnsignedBigInteger("1500000"),
            UnsignedBigInteger("15") * UnsignedBigInteger("100000")
        )
        assertEquals(
            UnsignedBigInteger("1500000"),
            UnsignedBigInteger("100000") * UnsignedBigInteger("15")
        )
    }

    @Test
    @Tag("Impossible")
    fun div() {
        assertEquals(UnsignedBigInteger(1), UnsignedBigInteger(2) / UnsignedBigInteger(2))
        assertEquals(
            UnsignedBigInteger("12345"),
            UnsignedBigInteger("152399025") / UnsignedBigInteger("12345")
        )
        assertEquals(
            UnsignedBigInteger("50799675"),
            UnsignedBigInteger("152399025") / UnsignedBigInteger("3")
        )
        assertEquals(
            UnsignedBigInteger("2"),
            UnsignedBigInteger("19") / UnsignedBigInteger("7")
        )
        assertEquals(
            UnsignedBigInteger("0"),
            UnsignedBigInteger("19") / UnsignedBigInteger("21")
        )
        assertEquals(
            UnsignedBigInteger("1"),
            UnsignedBigInteger("19") / UnsignedBigInteger("19")
        )
    }

    @Test
    @Tag("Impossible")
    fun rem() {
        assertEquals(UnsignedBigInteger(1), UnsignedBigInteger(12) % UnsignedBigInteger(11))
        assertEquals(UnsignedBigInteger(5), UnsignedBigInteger(19) % UnsignedBigInteger(7))
    }

    @Test
    @Tag("Normal")
    fun equals() {
        assertEquals(UnsignedBigInteger(123456789), UnsignedBigInteger("123456789"))
    }

    @Test
    @Tag("Normal")
    fun compareTo() {
        assertTrue(UnsignedBigInteger(123456789) < UnsignedBigInteger("9876543210"))
        assertTrue(UnsignedBigInteger("9876543210") > UnsignedBigInteger(123456789))
    }

    @Test
    @Tag("Normal")
    fun toInt() {
        assertEquals(123456789, UnsignedBigInteger("123456789").toInt())
    }
}