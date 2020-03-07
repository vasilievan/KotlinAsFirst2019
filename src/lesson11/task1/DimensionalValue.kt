@file:Suppress("UNUSED_PARAMETER")

package lesson11.task1

/**
 * Класс "Величина с размерностью".
 *
 * Предназначен для представления величин вроде "6 метров" или "3 килограмма"
 * Общая сложность задания - средняя.
 * Величины с размерностью можно складывать, вычитать, делить, менять им знак.
 * Их также можно умножать и делить на число.
 *
 * В конструктор передаётся вещественное значение и строковая размерность.
 * Строковая размерность может:
 * - либо строго соответствовать одной из abbreviation класса Dimension (m, g)
 * - либо соответствовать одной из приставок, к которой приписана сама размерность (Km, Kg, mm, mg)
 * - во всех остальных случаях следует бросить IllegalArgumentException
 */

class DimensionalValue(private val valueRaw: Double, private val dimensionStr: String) : Comparable<DimensionalValue> {
    companion object {
        fun findTheValue(s: String): Pair<Double, String> {
            require(s.matches(Regex("""-?\d+(\.\d+)? [A-Za-z]+""")))
            val separatedString = s.split(" ")
            val suffix = Dimension.values().map { it.abbreviation }.find { it in separatedString[1] }
            val prefix = DimensionPrefix.values().map { it.abbreviation }.find { it in separatedString[1] }
            if (suffix == null) {
                throw IllegalArgumentException()
            }
            if (prefix == null) {
                return Pair(separatedString[0].toDouble(), separatedString[1])
            }
            if (separatedString[1].startsWith(prefix) && separatedString[1].endsWith(suffix)) {
                return Pair(separatedString[0].toDouble(), separatedString[1])
            } else {
                throw IllegalArgumentException()
            }
        }
    }

    constructor(p: Pair<Double, String>) : this(p.first, p.second)

    constructor(s: String) : this(findTheValue(s))

    /**
     * Величина с БАЗОВОЙ размерностью (например для 1.0Kg следует вернуть результат в граммах -- 1000.0)
     */
    val value: Double
        get() {
            val dimension = this.dimension.abbreviation
            val multi = dimensionStr.replaceFirst(dimension, "")
            if (multi.isEmpty()) {
                return valueRaw
            }
            return valueRaw * DimensionPrefix.values().find { it.abbreviation == multi }!!.multiplier
        }

    /**
     * БАЗОВАЯ размерность (опять-таки для 1.0Kg следует вернуть GRAM)
     */
    val dimension: Dimension
        get() {
            val dimensions = Dimension.values()
            val dimensionsAbbreviations = dimensions.map { it.abbreviation }
            val suff = dimensionsAbbreviations.find { it in dimensionStr }
            if (suff != null) {
                return dimensions[dimensionsAbbreviations.indexOf(suff)]
            }
            throw IllegalArgumentException()
        }

    /**
     * Конструктор из строки. Формат строки: значение пробел размерность (1 Kg, 3 mm, 100 g и так далее).
     */


    /**
     * Сложение с другой величиной. Если базовая размерность разная, бросить IllegalArgumentException
     * (нельзя складывать метры и килограммы)
     */
    operator fun plus(other: DimensionalValue): DimensionalValue {
        require(dimension == other.dimension)
        return DimensionalValue(value + other.value, dimension.abbreviation)
    }

    /**
     * Смена знака величины
     */
    operator fun unaryMinus(): DimensionalValue = DimensionalValue(-value, dimension.abbreviation)

    /**
     * Вычитание другой величины. Если базовая размерность разная, бросить IllegalArgumentException
     */
    operator fun minus(other: DimensionalValue): DimensionalValue {
        require(dimension == other.dimension)
        return DimensionalValue(value - other.value, dimension.abbreviation)
    }

    /**
     * Умножение на число
     */
    operator fun times(other: Double): DimensionalValue = DimensionalValue(value * other, dimension.abbreviation)

    /**
     * Деление на число
     */
    operator fun div(other: Double): DimensionalValue {
        require(other != 0.0)
        return DimensionalValue(value / other, dimension.abbreviation)
    }

    /**
     * Деление на другую величину. Если базовая размерность разная, бросить IllegalArgumentException
     */
    operator fun div(other: DimensionalValue): Double {
        require(dimension == other.dimension)
        return value / other.value
    }

    /**
     * Сравнение на равенство
     */
    override fun equals(other: Any?): Boolean =
        (other is DimensionalValue) && (dimension == other.dimension) && (value == other.value)

    /**
     * Сравнение на больше/меньше. Если базовая размерность разная, бросить IllegalArgumentException
     */
    override fun compareTo(other: DimensionalValue): Int {
        require(dimension == other.dimension)
        return this.value.compareTo(other.value)
    }

    override fun hashCode(): Int {
        var result = valueRaw.hashCode()
        result = 31 * result + dimensionStr.hashCode()
        return result
    }
}

/**
 * Размерность. В этот класс можно добавлять новые варианты (секунды, амперы, прочие), но нельзя убирать
 */
enum class Dimension(val abbreviation: String) {
    METER("m"),
    GRAM("g");
}

/**
 * Приставка размерности. Опять-таки можно добавить новые варианты (деци-, санти-, мега-, ...), но нельзя убирать
 */
enum class DimensionPrefix(val abbreviation: String, val multiplier: Double) {
    KILO("K", 1000.0),
    MILLI("m", 0.001);
}