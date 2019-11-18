@file:Suppress("UNUSED_PARAMETER")

package lesson2.task2

import lesson1.task1.sqr
import kotlin.math.max
import kotlin.math.min
import kotlin.math.pow
import kotlin.math.sqrt

/**
 * Пример
 *
 * Лежит ли точка (x, y) внутри окружности с центром в (x0, y0) и радиусом r?
 */
fun pointInsideCircle(x: Double, y: Double, x0: Double, y0: Double, r: Double) =
    sqr(x - x0) + sqr(y - y0) <= sqr(r)

/**
 * Простая
 *
 * Четырехзначное число назовем счастливым, если сумма первых двух ее цифр равна сумме двух последних.
 * Определить, счастливое ли заданное число, вернуть true, если это так.
 */

// просто сложим первую и вторую цифру числа, а также третью и четвертую, и сравним суммы
fun isNumberHappy(number: Int): Boolean =
    (number / 100) % 10 + ((number / 100) % 100 - (number / 100) % 10) / 10 == (number % 100) % 10 + ((number % 100) % 100 - (number % 100) % 10) / 10

/**
 * Простая
 *
 * На шахматной доске стоят два ферзя (ферзь бьет по вертикали, горизонтали и диагоналям).
 * Определить, угрожают ли они друг другу. Вернуть true, если угрожают.
 * Считать, что ферзи не могут загораживать друг друга.
 */
fun queenThreatens(x1: Int, y1: Int, x2: Int, y2: Int): Boolean =
    (x1 == x2) || (y1 == y2) || ((y1 - y2).toDouble().pow(2) == (x1 - x2).toDouble().pow(2))


/**
 * Простая
 *
 * Дан номер месяца (от 1 до 12 включительно) и год (положительный).
 * Вернуть число дней в этом месяце этого года по григорианскому календарю.
 */
fun daysInMonth(month: Int, year: Int): Int {
    if ((month != 2) && (month <= 7)) {
        return if (month % 2 == 0) {
            30
        } else {
            31
        }
    } else if ((month != 2) && (month > 7)) {
        return if (month % 2 == 0) {
            31
        } else {
            30
        }
    } else {
        // високосный год
        return if (year % 400 == 0) {
            29
        } // високосный год
        else if ((year % 100 != 0) && (year % 4 == 0)) {
            29
        } // невисокосный год
        else {
            28
        }
    }
}

/**
 * Средняя
 *
 * Проверить, лежит ли окружность с центром в (x1, y1) и радиусом r1 целиком внутри
 * окружности с центром в (x2, y2) и радиусом r2.
 * Вернуть true, если утверждение верно
 */
fun circleInside(
    x1: Double, y1: Double, r1: Double,
    x2: Double, y2: Double, r2: Double
): Boolean = (x1 - x2).pow(2) + (y1 - y2).pow(2) <= (r2 - r1).pow(2)

/**
 * Средняя
 *
 * Определить, пройдет ли кирпич со сторонами а, b, c сквозь прямоугольное отверстие в стене со сторонами r и s.
 * Стороны отверстия должны быть параллельны граням кирпича.
 * Считать, что совпадения длин сторон достаточно для прохождения кирпича, т.е., например,
 * кирпич 4 х 4 х 4 пройдёт через отверстие 4 х 4.
 * Вернуть true, если кирпич пройдёт
 **/
// изменил идею проверки. Сначала выбираем две наименьшие грани кирпича. Если наибольшая из этих двух граней
// меньше или равна по длине наибольшей из граней отверстия и наименьшая из этих двух граней меньше или равна
// наименьшей из граней отверстия, то кирпич пройдет
fun brickPasses(a: Int, b: Int, c: Int, r: Int, s: Int): Boolean {
    val minimum: Int = min(min(a, b), min(b, c))
    var minimumTwo = 0
    when (minimum) {
        a -> minimumTwo = min(b, c)
        b -> minimumTwo = min(a, c)
        c -> minimumTwo = min(a, b)
    }
    return (max(minimum, minimumTwo) <= max(r, s)) && (min(minimum, minimumTwo) <= min(r, s))
}
