@file:Suppress("UNUSED_PARAMETER", "unused")

package lesson9.task1

/**
 * Ячейка матрицы: row = ряд, column = колонка
 */
data class Cell(val row: Int, val column: Int)

/**
 * Интерфейс, описывающий возможности матрицы. E = тип элемента матрицы
 */
interface Matrix<E> {
    /** Высота */
    val height: Int

    /** Ширина */
    val width: Int

    /**
     * Доступ к ячейке.
     * Методы могут бросить исключение, если ячейка не существует или пуста
     */
    operator fun get(row: Int, column: Int): E

    operator fun get(cell: Cell): E

    /**
     * Запись в ячейку.
     * Методы могут бросить исключение, если ячейка не существует
     */
    operator fun set(row: Int, column: Int, value: E)

    operator fun set(cell: Cell, value: E)
}

/**
 * Простая
 *
 * Метод для создания матрицы, должен вернуть РЕАЛИЗАЦИЮ Matrix<E>.
 * height = высота, width = ширина, e = чем заполнить элементы.
 * Бросить исключение IllegalArgumentException, если height или width <= 0.
 */
fun <E> createMatrix(height: Int, width: Int, e: E): Matrix<E> = MatrixImpl(height, width, e)

/**
 * Средняя сложность
 *
 * Реализация интерфейса "матрица"
 * */
class MatrixImpl<E>(override val height: Int, override val width: Int, e: E) : Matrix<E> {
    init {
        require((height > 0) && (width > 0))
    }

    infix fun notInside(arg: E): Boolean {
        for (row in 0 until height) {
            for (column in 0 until width){
                if (list[row][column] == arg) {
                    return false
                }
            }
        }
        return true
    }


    private val list = MutableList(height) {  MutableList(width) { e } }

    override fun get(row: Int, column: Int): E = list[row][column]

    override fun get(cell: Cell): E = list[cell.row][cell.column]

    override fun set(row: Int, column: Int, value: E) {
        list[row][column] = value
    }

    override fun set(cell: Cell, value: E) = set(cell.row, cell.column, value)

    override fun equals(other: Any?): Boolean {
        if (other == null) {
            return false
        }
        if ((other is MatrixImpl<*>) && (other.width == width) && (other.height == height)) {
            for (row in 0 until height) {
                for (column in 0 until width) {
                    if (list[row][column] != other.list[row][column]) {
                        return false
                    }
                }
            }
            return true
        } else {
            return false
        }
    }

    override fun toString(): String {
        val answer = StringBuilder()
        for (row in list) {
            answer.append("[")
            for (element in 0 until row.size) {
                if (element < row.size - 1) {
                    answer.append("${row[element]}, ")
                } else {
                    answer.append("${row[element]}")
                }
            }
            answer.append("], ")
        }
        return answer.removeSuffix(", ").toString()
    }
}

