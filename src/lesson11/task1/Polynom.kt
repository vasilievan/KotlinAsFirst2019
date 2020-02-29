@file:Suppress("UNUSED_PARAMETER")

package lesson11.task1

import kotlin.math.abs
import kotlin.math.pow

/**
 * Класс "полином с вещественными коэффициентами".
 *
 * Общая сложность задания -- сложная.
 * Объект класса -- полином от одной переменной (x) вида 7x^4+3x^3-6x^2+x-8.
 * Количество слагаемых неограничено.
 *
 * Полиномы можно складывать -- (x^2+3x+2) + (x^3-2x^2-x+4) = x^3-x^2+2x+6,
 * вычитать -- (x^3-2x^2-x+4) - (x^2+3x+2) = x^3-3x^2-4x+2,
 * умножать -- (x^2+3x+2) * (x^3-2x^2-x+4) = x^5+x^4-5x^3-3x^2+10x+8,
 * делить с остатком -- (x^3-2x^2-x+4) / (x^2+3x+2) = x-5, остаток 12x+16
 * вычислять значение при заданном x: при x=5 (x^2+3x+2) = 42.
 *
 * В конструктор полинома передаются его коэффициенты, начиная со старшего.
 * Нули в середине и в конце пропускаться не должны, например: x^3+2x+1 --> Polynom(1.0, 2.0, 0.0, 1.0)
 * Старшие коэффициенты, равные нулю, игнорировать, например Polynom(0.0, 0.0, 5.0, 3.0) соответствует 5x+3
 */
class Polynom(vararg coeffs: Double) {

    val listOfCoeffs = if (coeffs.indexOfFirst { it != 0.0 } != -1) coeffs.toList().subList(
        coeffs.indexOfFirst { it != 0.0 },
        coeffs.size
    ) else listOf(0.0)

    /**
     * Геттер: вернуть значение коэффициента при x^i
     */
    fun coeff(i: Int): Double = listOfCoeffs[i]

    /**
     * Расчёт значения при заданном x
     */
    fun getValue(x: Double): Double {
        var summary = 0.0
        for (i in listOfCoeffs.indices) {
            summary += listOfCoeffs[i] * x.pow(listOfCoeffs.size - 1 - i)
        }
        return summary
    }

    /**
     * Степень (максимальная степень x при ненулевом слагаемом, например 2 для x^2+x+1).
     *
     * Степень полинома с нулевыми коэффициентами считать равной 0.
     * Слагаемые с нулевыми коэффициентами игнорировать, т.е.
     * степень 0x^2+0x+2 также равна 0.
     */
    fun degree(): Int {
        for (i in listOfCoeffs.indices) {
            if (listOfCoeffs[i] != 0.0) {
                return listOfCoeffs.size - 1 - i
            }
        }
        return 0
    }

    /**
     * Сложение
     */
    operator fun plus(other: Polynom): Polynom {
        val minSize = minOf(listOfCoeffs.size, other.listOfCoeffs.size)
        val difference = abs(listOfCoeffs.size - other.listOfCoeffs.size)
        val resultList =
            if (listOfCoeffs.size > other.listOfCoeffs.size) listOfCoeffs.take(difference).toMutableList() else other.listOfCoeffs.take(
                difference
            ).toMutableList()
        for (index in 0 until minSize) {
            if (listOfCoeffs.size > other.listOfCoeffs.size) {
                resultList.add(listOfCoeffs[index + difference] + other.listOfCoeffs[index])
            } else {
                resultList.add(listOfCoeffs[index] + other.listOfCoeffs[index + difference])
            }
        }
        return Polynom(*resultList.toDoubleArray())
    }

    /**
     * Смена знака (при всех слагаемых)
     */
    operator fun unaryMinus(): Polynom = Polynom(*listOfCoeffs.map { -it }.toDoubleArray())

    /**
     * Вычитание
     */
    operator fun minus(other: Polynom): Polynom = this + other.unaryMinus()

    /**
     * Умножение
     */
    private fun timesNum(num: Double): Polynom = Polynom(*listOfCoeffs.map { it * num }.toDoubleArray())

    private fun moveRight(): Polynom =
        Polynom(*(this.listOfCoeffs.toMutableList() + mutableListOf(0.0)).toDoubleArray())


    operator fun times(other: Polynom): Polynom {
        var firstPolynom = this
        var answer = Polynom(0.0)
        for (element in other.listOfCoeffs.reversed()) {
            answer += firstPolynom.timesNum(element)
            firstPolynom = firstPolynom.moveRight()
        }
        return answer
    }

    /**
     * Деление
     *
     * Про операции деления и взятия остатка см. статью Википедии
     * "Деление многочленов столбиком". Основные свойства:
     *
     * Если A / B = C и A % B = D, то A = B * C + D и степень D меньше степени B
     */
    private fun moveLeft(): Polynom =
        Polynom(*this.listOfCoeffs.toMutableList().subList(1, this.listOfCoeffs.size).toDoubleArray())

    operator fun div(other: Polynom): Polynom {
        require((this.degree() >= other.degree()) && (other.listOfCoeffs.any { it != 0.0 }))
        if (this.listOfCoeffs.all { it == 0.0 }) {
            return Polynom(0.0)
        }
        val answer = mutableListOf<Double>()
        var firstCopy = listOfCoeffs.take(listOfCoeffs.size).toMutableList()
        val secondCopy = other.listOfCoeffs.take(other.listOfCoeffs.size)
        while (firstCopy.size >= secondCopy.size) {
            val mul = firstCopy[0] / secondCopy[0]
            val division =
                (secondCopy + MutableList(firstCopy.size - secondCopy.size) { 0.0 }).map { it * -mul }
            answer.add(mul)
            firstCopy = firstCopy.zip(division).map {it.first + it.second}.toMutableList()
            firstCopy.removeAt(0)
        }
        return Polynom(*answer.toDoubleArray())
    }

    private operator fun compareTo(other: Polynom): Int {
        if (this.degree() > other.degree()) {
            return 1
        } else if (this.degree() == other.degree()) {
            for (element in this.listOfCoeffs.indices) {
                if (this.listOfCoeffs[element] > other.listOfCoeffs[element]) {
                    return 1
                }
                if (this.listOfCoeffs[element] < other.listOfCoeffs[element]) {
                    return -1
                }
            }
            return 0
        }
        return -1
    }

    /**
     * Взятие остатка
     */
    operator fun rem(other: Polynom): Polynom {
        require((this.degree() >= other.degree()) && (other.listOfCoeffs.any { it != 0.0 }))
        var answer = this + (other * this.div(other)).unaryMinus()
        while (answer.listOfCoeffs[0] == 0.0) {
            answer = answer.moveLeft()
        }
        return answer
    }

    /**
     * Сравнение на равенство
     */
    override fun equals(other: Any?): Boolean {
        if ((other is Polynom) && (listOfCoeffs.size == other.listOfCoeffs.size)) {
            for (element in listOfCoeffs.indices) {
                if (listOfCoeffs[element] != other.listOfCoeffs[element]) {
                    return false
                }
            }
            return true
        }
        return false
    }

    override fun toString(): String {
        val sb = StringBuilder()
        for (element in listOfCoeffs.indices) {
            if (listOfCoeffs[element] == 0.0) {
                continue
            } else if (listOfCoeffs.size - element == 2) {
                if (listOfCoeffs[element] == -1.0) {
                    sb.append("-x^${listOfCoeffs.lastIndex - element}")
                } else if (listOfCoeffs[element] == 1.0) {
                    sb.append("+x^${listOfCoeffs.lastIndex - element}")
                } else if (listOfCoeffs[element] < 0.0) {
                    sb.append("${listOfCoeffs[element]}*x^${listOfCoeffs.lastIndex - element}")
                } else {
                    sb.append("+${listOfCoeffs[element]}*x^${listOfCoeffs.lastIndex - element}")
                }
            } else if (listOfCoeffs.size - element == 1) {
                sb.append("+${listOfCoeffs[element]}")
            } else {
                if (listOfCoeffs[element] == -1.0) {
                    sb.append("-x^${listOfCoeffs.lastIndex - element}")
                } else if (listOfCoeffs[element] == 1.0) {
                    sb.append("+x^${listOfCoeffs.lastIndex - element}")
                } else if (listOfCoeffs[element] < 0.0) {
                    sb.append("${listOfCoeffs[element]}*x^${listOfCoeffs.lastIndex - element}")
                } else {
                    sb.append("+${listOfCoeffs[element]}*x^${listOfCoeffs.lastIndex - element}")
                }
            }
        }
        return sb.toString().removePrefix("+")
    }

    /**
     * Получение хеш-кода
     */
    override fun hashCode(): Int = listOfCoeffs.hashCode()
}
