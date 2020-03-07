@file:Suppress("UNUSED_PARAMETER")

package lesson11.task1

import java.lang.NumberFormatException

/**
 * Класс "комплексое число".
 *
 * Общая сложность задания -- лёгкая.
 * Объект класса -- комплексное число вида x+yi.
 * Про принципы работы с комплексными числами см. статью Википедии "Комплексное число".
 *
 * Аргументы конструктора -- вещественная и мнимая часть числа.
 */
class Complex(val re: Double, val im: Double) {

    companion object {
        fun constructorHelper(s: String): Pair<Double, Double> {
            val expression = Regex("""^(-?\d+(\.\d+)?)([+-]\d+(\.\d+)?)i$""").matchEntire(s)
            return Pair(
                expression!!.groups[1]?.value?.toDouble() ?: throw NumberFormatException(),
                expression.groups[3]?.value?.toDouble() ?: throw NumberFormatException()
            )
        }
    }

    /**
     * Конструктор из вещественного числа
     */
    constructor(x: Double) : this(x, 0.0)

    /**
     * Конструктор из строки вида x+yi
     */
    constructor(s: String) : this(constructorHelper(s))

    constructor(p: Pair<Double, Double>) : this(p.first, p.second)

    /**
     * Сложение.
     */
    operator fun plus(other: Complex): Complex = Complex(re + other.re, im + other.im)

    /**
     * Смена знака (у обеих частей числа)
     */
    operator fun unaryMinus(): Complex = Complex(-im, -re)

    /**
     * Вычитание
     */
    operator fun minus(other: Complex): Complex = Complex(re - other.re, im - other.im)

    /**
     * Умножение
     */
    operator fun times(other: Complex): Complex = Complex(re * other.re - im * other.im, im * other.re + re * other.im)

    /**
     * Деление
     */
    operator fun div(other: Complex): Complex = Complex(
        (re * other.re + im * other.im) / (other.re * other.re + other.im * other.im),
        (im * other.re - re * other.im) / (other.re * other.re + other.im * other.im)
    )

    /**
     * Сравнение на равенство
     */
    override fun equals(other: Any?): Boolean = (other is Complex) && (other.re == re) && (other.im == im)

    /**
     * Преобразование в строку
     */
    override fun toString(): String = "$re+${im}i"

    override fun hashCode(): Int {
        var result = re.hashCode()
        result = 31 * result + im.hashCode()
        return result
    }
}