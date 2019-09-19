@file:Suppress("UNUSED_PARAMETER")

package lesson3.task1

import kotlin.math.*

/**
 * Пример
 *
 * Вычисление факториала
 */
fun factorial(n: Int): Double {
    var result = 1.0
    for (i in 1..n) {
        result = result * i // Please do not fix in master
    }
    return result
}

/**
 * Пример
 *
 * Проверка числа на простоту -- результат true, если число простое
 */
fun isPrime(n: Int): Boolean {
    if (n < 2) return false
    if (n == 2) return true
    if (n % 2 == 0) return false
    for (m in 3..sqrt(n.toDouble()).toInt() step 2) {
        if (n % m == 0) return false
    }
    return true
}

/**
 * Пример
 *
 * Проверка числа на совершенность -- результат true, если число совершенное
 */
fun isPerfect(n: Int): Boolean {
    var sum = 1
    for (m in 2..n / 2) {
        if (n % m > 0) continue
        sum += m
        if (sum > n) break
    }
    return sum == n
}

/**
 * Пример
 *
 * Найти число вхождений цифры m в число n
 */
fun digitCountInNumber(n: Int, m: Int): Int =
    when {
        n == m -> 1
        n < 10 -> 0
        else -> digitCountInNumber(n / 10, m) + digitCountInNumber(n % 10, m)
    }

/**
 * Простая
 *
 * Найти количество цифр в заданном числе n.
 * Например, число 1 содержит 1 цифру, 456 -- 3 цифры, 65536 -- 5 цифр.
 *
 * Использовать операции со строками в этой задаче запрещается.
 */
// расширил из множества N на множество Z чисел
fun digitNumber(n: Int): Int {
    var variablen = abs(n)
    var counter = 0
    while (variablen > 0) {
        variablen /= 10
        counter += 1
    }
    return if (n != 0) counter
    else 1
}

/**
 * Простая
 *
 * Найти число Фибоначчи из ряда 1, 1, 2, 3, 5, 8, 13, 21, ... с номером n.
 * Ряд Фибоначчи определён следующим образом: fib(1) = 1, fib(2) = 1, fib(n+2) = fib(n) + fib(n+1)
 */
fun fib(n: Int): Int {
    var firstNum = 1
    var secondNum = 1
    var thirdNum = firstNum + secondNum
    for (i in 1..n - 3) {
        firstNum = secondNum
        secondNum = thirdNum
        thirdNum = firstNum + secondNum
    }
    return if (n < 3) 1
    else thirdNum
}

/**
 * Простая
 *
 * Для заданных чисел m и n найти наименьшее общее кратное, то есть,
 * минимальное число k, которое делится и на m и на n без остатка
 */

// НОД по Евклиду
fun greatestCommonDivision(m: Int, n: Int): Int {
    var variablem = m
    var variablen = n
    while (true) {
        when {
            variablem > variablen -> variablem -= variablen
            variablem < variablen -> variablen -= variablem
            else -> return variablen
        }
    }
}

// НОК и НОД связаны отношением: a*b/НОД = НОК. Сначала найдем НОД по
// алгоритму Евклида (выше), а затем найдем НОК.
fun lcm(m: Int, n: Int): Int = m * n / greatestCommonDivision(m, n)

/**
 * Простая
 *
 * Для заданного числа n > 1 найти минимальный делитель, превышающий 1
 */
fun minDivisor(n: Int): Int {
    for (i in 2 until n) {
        if (n % i == 0) return i
    }
    return n
}

/**
 * Простая
 *
 * Для заданного числа n > 1 найти максимальный делитель, меньший n
 */
fun maxDivisor(n: Int): Int = n / minDivisor(n)

/**
 * Простая
 *
 * Определить, являются ли два заданных числа m и n взаимно простыми.
 * Взаимно простые числа не имеют общих делителей, кроме 1.
 * Например, 25 и 49 взаимно простые, а 6 и 8 -- нет.
 */

// если числа взаимно простые, то их НОД == 1. Задача сводится к поиску НОД.
fun isCoPrime(m: Int, n: Int): Boolean = greatestCommonDivision(m, n) == 1

/**
 * Простая
 *
 * Для заданных чисел m и n, m <= n, определить, имеется ли хотя бы один точный квадрат между m и n,
 * то есть, существует ли такое целое k, что m <= k*k <= n.
 * Например, для интервала 21..28 21 <= 5*5 <= 28, а для интервала 51..61 квадрата не существует.
 */

fun squareBetweenExists(m: Int, n: Int): Boolean = floor(sqrt(n.toDouble())) - ceil(sqrt(m.toDouble())) >= 0


/**
 * Средняя
 *
 * Гипотеза Коллатца. Рекуррентная последовательность чисел задана следующим образом:
 *
 *   ЕСЛИ (X четное)
 *     Xслед = X /2
 *   ИНАЧЕ
 *     Xслед = 3 * X + 1
 *
 * например
 *   15 46 23 70 35 106 53 160 80 40 20 10 5 16 8 4 2 1 4 2 1 4 2 1 ...
 * Данная последовательность рано или поздно встречает X == 1.
 * Написать функцию, которая находит, сколько шагов требуется для
 * этого для какого-либо начального X > 0.
 */
fun collatzSteps(x: Int): Int {
    var variablex = x
    var counter = 0
    while (variablex != 1) {
        if (variablex % 2 == 0) variablex /= 2
        else variablex = 3 * variablex + 1
        counter += 1
    }
    return counter
}

/**
 * Средняя
 *
 * Для заданного x рассчитать с заданной точностью eps
 * sin(x) = x - x^3 / 3! + x^5 / 5! - x^7 / 7! + ...
 * Нужную точность считать достигнутой, если очередной член ряда меньше eps по модулю.
 * Подумайте, как добиться более быстрой сходимости ряда при больших значениях x.
 * Использовать kotlin.math.sin и другие стандартные реализации функции синуса в этой задаче запрещается.
 */

// решим через ряд Тейлора, используя функцию факториала, написанную выше. Перед этим, используя принцип периодичности,
// приведем аргумент синуса к промежутку [0, PI * 2]
fun sin(x: Double, eps: Double): Double {
    var meaning = 0.0
    val variablex = x % (2 * PI)
    var term: Double
    var currentNum = 1
    do {
        term = (-1.0).pow(currentNum + 1) * variablex.pow(2 * currentNum - 1) / factorial(2 * currentNum - 1)
        meaning += term
        currentNum += 1
    } while (abs(term) >= eps)
    return meaning
}

/**
 * Средняя
 *
 * Для заданного x рассчитать с заданной точностью eps
 * cos(x) = 1 - x^2 / 2! + x^4 / 4! - x^6 / 6! + ...
 * Нужную точность считать достигнутой, если очередной член ряда меньше eps по модулю
 * Подумайте, как добиться более быстрой сходимости ряда при больших значениях x.
 * Использовать kotlin.math.cos и другие стандартные реализации функции косинуса в этой задаче запрещается.
 */
fun cos(x: Double, eps: Double): Double {
    var meaning = 0.0
    val variablex = x % (PI * 2)
    var term: Double
    var currentNum = 0
    do {
        term = (-1.0).pow(currentNum) * variablex.pow(2 * currentNum) / factorial(2 * currentNum)
        meaning += term
        currentNum += 1
    } while (abs(term) >= eps)
    return meaning
}

/**
 * Средняя
 *
 * Поменять порядок цифр заданного числа n на обратный: 13478 -> 87431.
 *
 * Использовать операции со строками в этой задаче запрещается.
 */

// сначала найдем степень десятки, которой описывается данное число, далее создадим новую переменную, в которую по сути
// будем записывать разложение числа n на множители, только в обратном порядке
fun revert(n: Int): Int {
    var variablen = n
    var resultNum = 0
    var counter = digitNumber(n) - 1
    while (variablen > 0) {
        resultNum += ((variablen % 10) * (10.0).pow(counter)).toInt()
        variablen /= 10
        counter--
    }
    return resultNum
}

/**
 * Средняя
 *
 * Проверить, является ли заданное число n палиндромом:
 * первая цифра равна последней, вторая -- предпоследней и так далее.
 * 15751 -- палиндром, 3653 -- нет.
 *
 * Использовать операции со строками в этой задаче запрещается.
 */

// по аналогии с предыдущей задачей с одним отличием:
// в конце вернуть результат сравнения начального и получившегося числа
fun isPalindrome(n: Int): Boolean = (revert(n) == n)

/**
 * Средняя
 *
 * Для заданного числа n определить, содержит ли оно различающиеся цифры.
 * Например, 54 и 323 состоят из разных цифр, а 111 и 0 из одинаковых.
 *
 * Использовать операции со строками в этой задаче запрещается.
 */

// будем сравнивать предыдущую цифру со следующей
fun hasDifferentDigits(n: Int): Boolean {
    var variablen = n
    var resultNumOne: Int
    var resultNumTwo: Int
    while (variablen > 9) {
        resultNumOne = variablen % 10
        variablen /= 10
        resultNumTwo = variablen % 10
        if (resultNumOne != resultNumTwo) return true
    }
    return false
}

/**
 * Сложная
 *
 * Найти n-ю цифру последовательности из квадратов целых чисел:
 * 149162536496481100121144...
 * Например, 2-я цифра равна 4, 7-я 5, 12-я 6.
 *
 * Использовать операции со строками в этой задаче запрещается.
 */
fun squareSequenceDigit(n: Int): Int {
    var currentPosition = 0
    var increment = 1
    var currentNum = 0
    while (currentPosition < n) {
        currentNum = increment * increment
        currentPosition += digitNumber(currentNum)
        increment++
    }
    return if (currentPosition == n) {
        currentNum % 10
    } else {
        for (i in 1..(currentPosition - n)) {
            currentNum /= 10
        }
        currentNum % 10
    }
}

/**
 * Сложная
 *
 * Найти n-ю цифру последовательности из чисел Фибоначчи (см. функцию fib выше):
 * 1123581321345589144...
 * Например, 2-я цифра равна 1, 9-я 2, 14-я 5.
 *
 * Использовать операции со строками в этой задаче запрещается.
 */

fun fibSequenceDigit(n: Int): Int {
    var currentPosition = 0
    var increment = 1
    var currentNum = 0
    while (currentPosition < n) {
        currentNum = fib(increment)
        currentPosition += digitNumber(currentNum)
        increment++
    }
    return if (currentPosition == n) {
        currentNum % 10
    } else {
        for (i in 1..(currentPosition - n)) {
            currentNum /= 10
        }
        currentNum % 10
    }
}