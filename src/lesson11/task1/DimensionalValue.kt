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

fun findTheValue(s: String): Pair<Double, String> {
    require(s.matches(Regex("""-?\d+(\.\d+)? [A-Za-z]{1,2}""")))
    val temp = s.split(" ")
    val dimPr = DimensionPrefix.values().map { it.abbreviation }.toSet()
    val dim = Dimension.values().map { it.abbreviation }.toSet()
    if (((temp[1].length == 2) && (temp[1][0].toString() in dimPr) && (temp[1][1].toString() in dim)) ||
        ((temp[1].length == 1) && (temp[1] in dim))) {
        return Pair(temp[0].toDouble(), temp[1])
    }
    throw IllegalArgumentException()
}

class DimensionalValue(private val valueRaw: Double, private val dimensionStr: String) : Comparable<DimensionalValue> {
    constructor(s: String) : this(findTheValue(s).first, findTheValue(s).second)

    /**
     * Величина с БАЗОВОЙ размерностью (например для 1.0Kg следует вернуть результат в граммах -- 1000.0)
     */
    val value: Double
        get() {
            if (dimensionStr.length == 2) {
                for (element in DimensionPrefix.values()) {
                    if (element.abbreviation == dimensionStr[0].toString()) {
                        return valueRaw * element.multiplier
                    }
                }
                throw IllegalArgumentException()
            } else {
                return valueRaw
            }
        }

    /**
     * БАЗОВАЯ размерность (опять-таки для 1.0Kg следует вернуть GRAM)
     */
    val dimension: Dimension
        get() {
            for (element in Dimension.values()) {
                if (dimensionStr.last().toString() == element.abbreviation) {
                    return element
                }
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