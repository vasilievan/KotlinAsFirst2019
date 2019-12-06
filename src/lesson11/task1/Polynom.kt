@file:Suppress("UNUSED_PARAMETER")

package lesson11.task1

import java.lang.IllegalArgumentException
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

    val arrayOfCoeffs = coeffs.toList()

    /**
     * Геттер: вернуть значение коэффициента при x^i
     */
    fun coeff(i: Int): Double = arrayOfCoeffs[i]

    /**
     * Расчёт значения при заданном x
     */
    fun getValue(x: Double): Double {
        var summary = 0.0
        for (i in arrayOfCoeffs.indices) {
            summary += arrayOfCoeffs[i] * x.pow(arrayOfCoeffs.size - 1 - i)
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
        for (i in arrayOfCoeffs.indices) {
            if (arrayOfCoeffs[i] != 0.0) {
                return arrayOfCoeffs.size - 1 - i
            }
        }
        return 0
    }

    /**
     * Сложение
     */
    operator fun plus(other: Polynom): Polynom {
        val temp = arrayOfCoeffs.reversed().toMutableList()
        val otherTemp = other.arrayOfCoeffs.reversed().toMutableList()
        return if (arrayOfCoeffs.size < other.arrayOfCoeffs.size) {
            for (element in temp.indices) {
                otherTemp[element] += temp[element]
            }
            Polynom(*(otherTemp.reversed()).toDoubleArray())
        } else {
            for (element in otherTemp.indices) {
                temp[element] += otherTemp[element]
            }
            Polynom(*(temp.reversed()).toDoubleArray())
        }
    }

    /**
     * Смена знака (при всех слагаемых)
     */
    operator fun unaryMinus(): Polynom = Polynom(*arrayOfCoeffs.map { -it }.toDoubleArray())

    /**
     * Вычитание
     */
    operator fun minus(other: Polynom): Polynom = this + other.unaryMinus()

    /**
     * Умножение
     */
    private fun timesNum(num: Double): Polynom = Polynom(*arrayOfCoeffs.map { it * num }.toDoubleArray())

    private fun moveRight(): Polynom =
        Polynom(*(this.arrayOfCoeffs.toMutableList() + mutableListOf(0.0)).toDoubleArray())


    operator fun times(other: Polynom): Polynom {
        var temp = this
        var answer = Polynom(0.0)
        for (element in other.arrayOfCoeffs.reversed()) {
            answer += temp.timesNum(element)
            temp = temp.moveRight()
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
        Polynom(*this.arrayOfCoeffs.toMutableList().subList(1, this.arrayOfCoeffs.size).toDoubleArray())

    operator fun div(other: Polynom): Polynom {
        require((this.degree() >= other.degree()) && (other.arrayOfCoeffs.any { it != 0.0 }))
        if (this.arrayOfCoeffs.all { it == 0.0 }) {
            return Polynom(0.0)
        }
        val answer = mutableListOf<Double>()
        var temp = this
        var tempOther = other
        while (temp.arrayOfCoeffs[0] == 0.0) {
            temp = temp.moveLeft()
        }
        while (tempOther.arrayOfCoeffs[0] == 0.0) {
            tempOther = tempOther.moveLeft()
        }
        while (temp.arrayOfCoeffs.size >= tempOther.arrayOfCoeffs.size) {
            val mult = temp.arrayOfCoeffs[0] / tempOther.arrayOfCoeffs[0]
            var diff = tempOther.timesNum(mult).unaryMinus()
            answer.add(mult)
            if (diff.arrayOfCoeffs.size < temp.arrayOfCoeffs.size) {
                diff =
                    Polynom(*(diff.arrayOfCoeffs + Array(temp.arrayOfCoeffs.size - diff.arrayOfCoeffs.size) { 0.0 }).toDoubleArray())
            }
            temp += diff
            temp = temp.moveLeft()
        }
        return Polynom(*answer.toDoubleArray())
    }

    /**
     * Взятие остатка
     */
    operator fun rem(other: Polynom): Polynom {
        require((this.degree() >= other.degree()) && (other.arrayOfCoeffs.any { it != 0.0 }))
        var answer = this + (other * this.div(other)).unaryMinus()
        while (answer.arrayOfCoeffs[0] == 0.0) {
            answer = answer.moveLeft()
        }
        println(answer)
        return answer
    }

    /**
     * Сравнение на равенство
     */
    override fun equals(other: Any?): Boolean {
        if (other is Polynom) {
            for (i in 0..10) {
                if (other.getValue(i.toDouble()) != this.getValue(i.toDouble())) {
                    return false
                }
            }
            return true
        }
        return false
    }

    override fun toString(): String {
        return arrayOfCoeffs.toString()
    }

    /**
     * Получение хеш-кода
     */
    override fun hashCode(): Int = arrayOfCoeffs.hashCode()
}
