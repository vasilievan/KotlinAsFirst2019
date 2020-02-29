package lesson11.task1

import java.lang.IllegalArgumentException
import kotlin.math.abs

/**
 * Класс "беззнаковое большое целое число".
 *
 * Общая сложность задания -- очень сложная.
 * Объект класса содержит целое число без знака произвольного размера
 * и поддерживает основные операции над такими числами, а именно:
 * сложение, вычитание (при вычитании большего числа из меньшего бросается исключение),
 * умножение, деление, остаток от деления,
 * преобразование в строку/из строки, преобразование в целое/из целого,
 * сравнение на равенство и неравенство
 */
val MAXINTEGER = UnsignedBigInteger(Int.MAX_VALUE)
val ZEROINTEGER = UnsignedBigInteger(0)

class UnsignedBigInteger(private var data: MutableList<Int>) : Comparable<UnsignedBigInteger> {
    // исключаем случай "012345"
    init {
        if ((data.size > 1) && (data[0] == 0)) {
            throw IllegalArgumentException()
        }
    }

    /**
     * Конструктор из строки
     */

    constructor(s: String) : this(s.map { it.toString().toInt() }.toMutableList())

    /**
     * Конструктор из целого
     */
    constructor(i: Int) : this(i.toString())

    /**
     * Сложение
     */

    private fun MutableList<Int>.addInReversed(another: MutableList<Int>): MutableList<Int> {
        val diff = abs(this.size - another.size)
        for (i in another.size - 1 downTo 0) {
            this[i + diff] += another[i]
        }
        return this
    }

    operator fun plus(other: UnsignedBigInteger): UnsignedBigInteger {
        // answer = массив наибольшей длины
        val answer: MutableList<Int> = if (data.size > other.data.size) {
            data.subList(0, data.size)
        } else {
            other.data.subList(0, other.data.size)
        }
        answer.add(0, 0)
        // добваляем элементы другого массива
        if (answer.size == data.size) {
            answer.addInReversed(other.data)
        } else {
            answer.addInReversed(data)
        }
        return UnsignedBigInteger(answer.normalisation())
    }

    private fun MutableList<Int>.normalisation(): MutableList<Int> {
        val copy = this.take(this.size).toMutableList()
        for (i in copy.lastIndex downTo 1) {
            copy[i - 1] += copy[i] / 10
            copy[i] = copy[i] % 10
        }
        val beginning = copy.indexOfFirst { it != 0 }
        if (beginning == -1) {
            return mutableListOf(0)
        }
        return copy.subList(beginning, copy.size)
    }

    private fun MutableList<Int>.deleteInReversed(another: MutableList<Int>): MutableList<Int> {
        val diff = abs(this.size - another.size)
        for (i in another.size - 1 downTo 0) {
            this[i + diff] -= another[i]
        }
        return this
    }

    /**
     * Вычитание (бросить ArithmeticException, если this < other)
     */
    operator fun minus(other: UnsignedBigInteger): UnsignedBigInteger {
        if (data.size < other.data.size) {
            throw ArithmeticException()
        }
        if ((data.size == other.data.size) && (data[0] < other.data[0])) {
            throw ArithmeticException()
        }
        // вычитаем элементы другого массива
        val answer = this.data.take(data.size).toMutableList().deleteInReversed(other.data)
        for (i in 0 until answer.size) {
            if (answer[i] < 0) {
                var j = i
                while (answer[j] <= 0) {
                    j--
                }
                answer[j]--
                for (k in j + 1 until i) {
                    answer[k] += 9
                }
                answer[i] += 10
            }
        }
        if (answer.all { it == 0 }) {
            return ZEROINTEGER
        }
        return UnsignedBigInteger(answer.normalisation())
    }

    /**
     * Умножение
     */

    private fun MutableList<Int>.moveLeft(): MutableList<Int> = (this + mutableListOf(0)).toMutableList()

    operator fun times(other: UnsignedBigInteger): UnsignedBigInteger {
        if ((this == ZEROINTEGER) || (other == ZEROINTEGER)) {
            return ZEROINTEGER
        }
        val answer = MutableList((data.size) * (other.data.size)) { 0 }
        var multiplied = data.subList(0, data.size)
        val multiplier = other.data.subList(0, other.data.size)
        for (element in multiplier.reversed()) {
            answer.addInReversed(multiplied.map { it * element }.toMutableList())
            multiplied = multiplied.moveLeft()
        }
        return UnsignedBigInteger(answer.normalisation())
    }

    /**
     * Деление
     */

    fun moveRight(n: Int): UnsignedBigInteger =
        UnsignedBigInteger((data + MutableList(n) { 0 }).joinToString("") { it.toString() })

    operator fun div(other: UnsignedBigInteger): UnsignedBigInteger {
        require(other != ZEROINTEGER)
        if (this == ZEROINTEGER) {
            return ZEROINTEGER
        }
        if (other == UnsignedBigInteger(1)) {
            return this
        }
        if (this < other) {
            return ZEROINTEGER
        }
        val answer = MutableList(this.data.size) { 0 }
        fun recursiveSubtraction(dividend: UnsignedBigInteger, divisor: UnsignedBigInteger) {
            if (dividend >= divisor) {
                val n = dividend.data.size - divisor.data.size
                if (dividend >= divisor.moveRight(n)) {
                    answer[n] += 1
                    recursiveSubtraction(dividend - divisor.moveRight(n), divisor)
                } else {
                    answer[n - 1] += 1
                    recursiveSubtraction(dividend - divisor.moveRight(n - 1), divisor)
                }
            }
        }
        recursiveSubtraction(this, other)
        return UnsignedBigInteger(answer.divNormalisation())
    }

    private fun MutableList<Int>.divNormalisation(): MutableList<Int> {
        val lastNotZero = this.indexOfLast { it != 0 }
        if (lastNotZero == -1) {
            return mutableListOf(0)
        }
        val correctCopy = mutableListOf<Int>()
        for (index in lastNotZero downTo 0) {
            correctCopy.add(this[index])
        }
        return correctCopy
    }

    /**
     * Взятие остатка
     */
    operator fun rem(other: UnsignedBigInteger): UnsignedBigInteger {
        require(other != ZEROINTEGER)
        val n = UnsignedBigInteger(data)
        val fullPart = this / other
        val mult = other * fullPart
        return n - mult
    }

    /**
     * Сравнение на равенство (по контракту Any.equals)
     */
    override fun equals(other: Any?): Boolean {
        if ((other is UnsignedBigInteger) && (data.size == other.data.size)) {
            return (this - other).data.all { it == 0 }
        }
        return false
    }

    /**
     * Сравнение на больше/меньше (по контракту Comparable.compareTo)
     */
    override fun compareTo(other: UnsignedBigInteger): Int {
        if (data.size > other.data.size) {
            return 1
        }
        if (data.size < other.data.size) {
            return -1
        }
        for (i in 0 until data.size) {
            if (data[i] < other.data[i]) {
                return -1
            } else if (data[i] > other.data[i]) {
                return 1
            }
        }
        return 0
    }

    /**
     * Преобразование в строку
     */
    override fun toString(): String = data.joinToString("") { it.toString() }

    /**
     * Преобразование в целое
     * Если число не влезает в диапазон Int, бросить ArithmeticException
     */
    fun toInt(): Int {
        if (this <= MAXINTEGER) {
            return this.data.joinToString("") { it.toString() }.toInt()
        } else {
            throw ArithmeticException()
        }
    }
}