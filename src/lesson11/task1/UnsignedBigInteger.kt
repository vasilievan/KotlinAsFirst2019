package lesson11.task1

import java.lang.IllegalArgumentException
import kotlin.math.abs
import kotlin.math.pow

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
    constructor(i: Int) : this(i.toString().map { it.toString().toInt() }.toMutableList())

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

        for (i in answer.size - 1 downTo 1) {
            answer[i - 1] += answer[i] / 10
            answer[i] = answer[i] % 10
        }
        if (answer[0] == 0) {
            answer.removeAt(0)
        }

        return UnsignedBigInteger(answer.joinToString("") { it.toString() })
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
        val answer = this.data.deleteInReversed(other.data)
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
        for (i in answer.size - 1 downTo 1) {
            answer[i - 1] += answer[i] / 10
            answer[i] = answer[i] % 10
        }
        if (answer.all { it == 0 }) {
            return UnsignedBigInteger(0)
        }
        var firstNotZero = 0
        for (i in 0 until answer.size) {
            if (answer[i] != 0) {
                firstNotZero = i
                break
            }
        }
        return UnsignedBigInteger(answer.subList(firstNotZero, answer.size).joinToString("") { it.toString() })
    }

    /**
     * Умножение
     */

    private fun MutableList<Int>.moveLeft(): MutableList<Int> = (this + mutableListOf(0)).toMutableList()

    operator fun times(other: UnsignedBigInteger): UnsignedBigInteger {
        if ((this == UnsignedBigInteger(0)) || (other == UnsignedBigInteger(0))) {
            return UnsignedBigInteger(0)
        }
        val answer = MutableList((data.size) * (other.data.size)) { 0 }
        var multiplied = data.subList(0, data.size)
        val multiplier = other.data.subList(0, other.data.size)
        for (element in multiplier.reversed()) {
            answer.addInReversed(multiplied.map { it * element }.toMutableList())
            multiplied = multiplied.moveLeft()
        }
        for (i in answer.size - 1 downTo 1) {
            answer[i - 1] += answer[i] / 10
            answer[i] = answer[i] % 10
        }
        var firstNotZero = 0
        for (i in 0 until answer.size) {
            if (answer[i] != 0) {
                firstNotZero = i
                break
            }
        }
        return UnsignedBigInteger(answer.subList(firstNotZero, answer.size).joinToString("") { it.toString() })
    }

    /**
     * Деление
     */

    fun moveRight(n: Int): UnsignedBigInteger =
        UnsignedBigInteger((data + MutableList(n) { 0 }).joinToString("") { it.toString() })

    operator fun div(other: UnsignedBigInteger): UnsignedBigInteger {
        require(other != UnsignedBigInteger(0))
        if (this == UnsignedBigInteger(0)) {
            return UnsignedBigInteger(0)
        }
        if (other == UnsignedBigInteger(1)) {
            return this
        }
        if (this < other) {
            return UnsignedBigInteger(0)
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
        var last = answer.size
        for (i in answer.size - 1 downTo 0) {
            if (answer[i] != 0) {
                last = i + 1
                break
            }
        }
        return UnsignedBigInteger(
            answer.subList(0, last).reversed().joinToString("") { it.toString() })
    }

    /**
     * Взятие остатка
     */
    operator fun rem(other: UnsignedBigInteger): UnsignedBigInteger {
        require(other != UnsignedBigInteger(0))
        val n = UnsignedBigInteger(data.joinToString("") { it.toString() })
        val temp = this / other
        val mult = other * temp
        return n - mult
    }

    /**
     * Сравнение на равенство (по контракту Any.equals)
     */
    override fun equals(other: Any?): Boolean {
        if ((other is UnsignedBigInteger) && (data.size == other.data.size)) {
            for (i in 0 until data.size) {
                if (data[i] != other.data[i]) {
                    return false
                }
            }
            return true
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
        if (this <= UnsignedBigInteger(Int.MAX_VALUE)) {
            return this.data.joinToString("") { it.toString() }.toInt()
        } else {
            throw ArithmeticException()
        }
    }
}