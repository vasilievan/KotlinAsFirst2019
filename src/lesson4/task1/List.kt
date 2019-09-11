@file:Suppress("UNUSED_PARAMETER", "ConvertCallChainIntoSequence")

package lesson4.task1

import lesson1.task1.discriminant
import kotlin.math.max
import kotlin.math.pow
import kotlin.math.sqrt

/**
 * Пример
 *
 * Найти все корни уравнения x^2 = y
 */
fun sqRoots(y: Double) =
    when {
        y < 0 -> listOf()
        y == 0.0 -> listOf(0.0)
        else -> {
            val root = sqrt(y)
            // Результат!
            listOf(-root, root)
        }
    }

/**
 * Пример
 *
 * Найти все корни биквадратного уравнения ax^4 + bx^2 + c = 0.
 * Вернуть список корней (пустой, если корней нет)
 */
fun biRoots(a: Double, b: Double, c: Double): List<Double> {
    if (a == 0.0) {
        return if (b == 0.0) listOf()
        else sqRoots(-c / b)
    }
    val d = discriminant(a, b, c)
    if (d < 0.0) return listOf()
    if (d == 0.0) return sqRoots(-b / (2 * a))
    val y1 = (-b + sqrt(d)) / (2 * a)
    val y2 = (-b - sqrt(d)) / (2 * a)
    return sqRoots(y1) + sqRoots(y2)
}

/**
 * Пример
 *
 * Выделить в список отрицательные элементы из заданного списка
 */
fun negativeList(list: List<Int>): List<Int> {
    val result = mutableListOf<Int>()
    for (element in list) {
        if (element < 0) {
            result.add(element)
        }
    }
    return result
}

/**
 * Пример
 *
 * Изменить знак для всех положительных элементов списка
 */
fun invertPositives(list: MutableList<Int>) {
    for (i in 0 until list.size) {
        val element = list[i]
        if (element > 0) {
            list[i] = -element
        }
    }
}

/**
 * Пример
 *
 * Из имеющегося списка целых чисел, сформировать список их квадратов
 */
fun squares(list: List<Int>) = list.map { it * it }

/**
 * Пример
 *
 * Из имеющихся целых чисел, заданного через vararg-параметр, сформировать массив их квадратов
 */
fun squares(vararg array: Int) = squares(array.toList()).toTypedArray()

/**
 * Пример
 *
 * По заданной строке str определить, является ли она палиндромом.
 * В палиндроме первый символ должен быть равен последнему, второй предпоследнему и т.д.
 * Одни и те же буквы в разном регистре следует считать равными с точки зрения данной задачи.
 * Пробелы не следует принимать во внимание при сравнении символов, например, строка
 * "А роза упала на лапу Азора" является палиндромом.
 */
fun isPalindrome(str: String): Boolean {
    val lowerCase = str.toLowerCase().filter { it != ' ' }
    for (i in 0..lowerCase.length / 2) {
        if (lowerCase[i] != lowerCase[lowerCase.length - i - 1]) return false
    }
    return true
}

/**
 * Пример
 *
 * По имеющемуся списку целых чисел, например [3, 6, 5, 4, 9], построить строку с примером их суммирования:
 * 3 + 6 + 5 + 4 + 9 = 27 в данном случае.
 */
fun buildSumExample(list: List<Int>) = list.joinToString(separator = " + ", postfix = " = ${list.sum()}")

/**
 * Простая
 *
 * Найти модуль заданного вектора, представленного в виде списка v,
 * по формуле abs = sqrt(a1^2 + a2^2 + ... + aN^2).
 * Модуль пустого вектора считать равным 0.0.
 */
fun abs(v: List<Double>): Double =
    sqrt(v.fold(0.0) { previousElement, currentElement -> previousElement + currentElement.pow(2) })

/**
 * Простая
 *
 * Рассчитать среднее арифметическое элементов списка list. Вернуть 0.0, если список пуст
 */
fun mean(list: List<Double>): Double {
    return if (list.isNotEmpty()) {
        list.fold(0.0) { previousElement, currentElement -> previousElement + currentElement } / list.size
    } else 0.0
}


/**
 * Средняя
 *
 * Центрировать заданный список list, уменьшив каждый элемент на среднее арифметическое всех элементов.
 * Если список пуст, не делать ничего. Вернуть изменённый список.
 *
 * Обратите внимание, что данная функция должна изменять содержание списка list, а не его копии.
 */
fun center(list: MutableList<Double>): MutableList<Double> {
    val arithmeticMean: Double =
        (list.fold(0.0) { previousElement, currentElement -> previousElement + currentElement }) / list.size
    for (i in 0 until list.size) {
        list[i] -= arithmeticMean
    }
    return list
}

/**
 * Средняя
 *
 * Найти скалярное произведение двух векторов равной размерности,
 * представленные в виде списков a и b. Скалярное произведение считать по формуле:
 * C = a1b1 + a2b2 + ... + aNbN. Произведение пустых векторов считать равным 0.
 */
fun times(a: List<Int>, b: List<Int>): Int {
    var summary = 0
    if (a.isEmpty() || b.isEmpty()) return 0
    for (i in 0 until max(a.size, b.size)) {
        summary += a[i] * b[i]
    }
    return summary
}

/**
 * Средняя
 *
 * Рассчитать значение многочлена при заданном x:
 * p(x) = p0 + p1*x + p2*x^2 + p3*x^3 + ... + pN*x^N.
 * Коэффициенты многочлена заданы списком p: (p0, p1, p2, p3, ..., pN).
 * Значение пустого многочлена равно 0 при любом x.
 */
fun polynom(p: List<Int>, x: Int): Int {
    var summary = 0.0
    return if (p.isEmpty()) 0
    else {
        for (i in 1 until p.size) {
            summary += p[i] * x.toDouble().pow(i).toInt()
        }
        summary.toInt() + p[0]
    }
}

/**
 * Средняя
 *
 * В заданном списке list каждый элемент, кроме первого, заменить
 * суммой данного элемента и всех предыдущих.
 * Например: 1, 2, 3, 4 -> 1, 3, 6, 10.
 * Пустой список не следует изменять. Вернуть изменённый список.
 *
 * Обратите внимание, что данная функция должна изменять содержание списка list, а не его копии.
 */
fun accumulate(list: MutableList<Int>): MutableList<Int> {
    if (list.size == 0) return list
    else {
        for (i in 1 until list.size) {
            list[i] += list[i - 1]
        }
    }
    return list
}

/**
 * Средняя
 *
 * Разложить заданное натуральное число n > 1 на простые множители.
 * Результат разложения вернуть в виде списка множителей, например 75 -> (3, 5, 5).
 * Множители в списке должны располагаться по возрастанию.
 */
fun factorize(n: Int): List<Int> {
    var variablen: Int = n
    val list = mutableListOf<Int>()
    var currentNumber = 2
    while (variablen > 1) {
        if (variablen % currentNumber == 0) {
            list.add(currentNumber)
            variablen /= currentNumber
        } else currentNumber += 1
    }
    return list
}

/**
 * Сложная
 *
 * Разложить заданное натуральное число n > 1 на простые множители.
 * Результат разложения вернуть в виде строки, например 75 -> 3*5*5
 * Множители в результирующей строке должны располагаться по возрастанию.
 */
fun factorizeToString(n: Int): String {
    var variablen: Int = n
    val list = mutableListOf<Int>()
    var currentNumber = 2
    while (variablen > 1) {
        if (variablen % currentNumber == 0) {
            list.add(currentNumber)
            variablen /= currentNumber
        } else currentNumber += 1
    }
    return list.joinToString(separator = "*") { it -> "$it" }
}

/**
 * Средняя
 *
 * Перевести заданное целое число n >= 0 в систему счисления с основанием base > 1.
 * Результат перевода вернуть в виде списка цифр в base-ичной системе от старшей к младшей,
 * например: n = 100, base = 4 -> (1, 2, 1, 0) или n = 250, base = 14 -> (1, 3, 12)
 */
fun convert(n: Int, base: Int): List<Int> {
    var variablen = n
    if (variablen == 0) return listOf(0)
    val arrOfNumbers = mutableListOf<Int>()
    while (variablen > 0) {
        arrOfNumbers.add(variablen % base)
        variablen /= base
    }
    return arrOfNumbers.reversed()
}

/**
 * Сложная
 *
 * Перевести заданное целое число n >= 0 в систему счисления с основанием 1 < base < 37.
 * Результат перевода вернуть в виде строки, цифры более 9 представлять латинскими
 * строчными буквами: 10 -> a, 11 -> b, 12 -> c и так далее.
 * Например: n = 100, base = 4 -> 1210, n = 250, base = 14 -> 13c
 *
 * Использовать функции стандартной библиотеки, напрямую и полностью решающие данную задачу
 * (например, n.toString(base) и подобные), запрещается.
 */
fun convertToString(n: Int, base: Int): String {
    val strOfAlphabet = "0123456789abcdefghijklmnopqrstuvwxyz"
    var variablen = n
    if (variablen == 0) return "0"
    val list = mutableListOf<Char>()
    while (variablen > 0) {
        list.add(strOfAlphabet[variablen % base])
        variablen /= base
    }
    return list.joinToString(separator = "") { "$it" }.reversed()
}

/**
 * Средняя
 *
 * Перевести число, представленное списком цифр digits от старшей к младшей,
 * из системы счисления с основанием base в десятичную.
 * Например: digits = (1, 3, 12), base = 14 -> 250
 */
fun decimal(digits: List<Int>, base: Int): Int {
    var resulOfFun = 0.0
    val variableBase = base.toDouble()
    var currentPosition = 0
    for (i in digits.size - 1 downTo 0) {
        resulOfFun += digits[currentPosition] * variableBase.pow(i)
        currentPosition++
    }
    return resulOfFun.toInt()
}

/**
 * Сложная
 *
 * Перевести число, представленное цифровой строкой str,
 * из системы счисления с основанием base в десятичную.
 * Цифры более 9 представляются латинскими строчными буквами:
 * 10 -> a, 11 -> b, 12 -> c и так далее.
 * Например: str = "13c", base = 14 -> 250
 *
 * Использовать функции стандартной библиотеки, напрямую и полностью решающие данную задачу
 * (например, str.toInt(base)), запрещается.
 */

fun decimalFromString(str: String, base: Int): Int {
    val list = "0123456789abcdefghijklmnopqrstuvwxyz"
    var counter = 0.0
    var j = 0
    for (i in str.length - 1 downTo 0) {
        counter += list.indexOf(str[i]) * base.toDouble().pow(j)
        j++
    }
    return counter.toInt()
}

/**
 * Сложная
 *
 * Перевести натуральное число n > 0 в римскую систему.
 * Римские цифры: 1 = I, 4 = IV, 5 = V, 9 = IX, 10 = X, 40 = XL, 50 = L,
 * 90 = XC, 100 = C, 400 = CD, 500 = D, 900 = CM, 1000 = M.
 * Например: 23 = XXIII, 44 = XLIV, 100 = C
 */
fun roman(n: Int): String {
    // делаю аналог String*Int из Python, ибо без него совсем грустно...((
    fun multipleString(str: String, counter: Int): String {
        var variablestring = ""
        for (i in 1..counter) {
            variablestring += str
        }
        return variablestring
    }

    var resultString = ""
    val alphabet = "IVXLCDM"
    var j = 0
    // или как получить цифры числа в обратном порядке
    val servingArray = n.toString().reversed().toCharArray().map { it.toString().toInt() }
    // здесь просто по правилам Римской СС
    for (i in servingArray.indices) {
        if (servingArray[i] in 1..3) {
            resultString = multipleString(alphabet[j].toString(), servingArray[i]) + resultString
            j += 2
        } else if (servingArray[i] in 6..8) {
            resultString =
                alphabet[j + 1].toString() + multipleString(alphabet[j].toString(), servingArray[i] - 5) + resultString
            j += 2
        } else if (servingArray[i] == 5) {
            resultString = alphabet[j + 1].toString() + resultString
            j += 2
        } else if (servingArray[i] == 4) {
            resultString = alphabet[j] + alphabet[j + 1].toString() + resultString
            j += 2
        } else if (servingArray[i] == 9) {
            resultString = alphabet[j] + alphabet[j + 2].toString() + resultString
            j += 2
        } else j += 2
    }
    return resultString
}

/**
 * Очень сложная
 *
 * Записать заданное натуральное число 1..999999 прописью по-русски.
 * Например, 375 = "триста семьдесят пять",
 * 23964 = "двадцать три тысячи девятьсот шестьдесят четыре"
 */
fun russian(n: Int): String {
    var resultstr = ""
    val hundreds = listOf(
        "сто",
        "двести",
        "триста",
        "четыреста",
        "пятьсот",
        "шестьсот",
        "семьсот",
        "восемьсот",
        "девятьсот"
    )
    val thousandsnum =
        listOf("одна", "две", "три", "четыре", "пять", "шесть", "семь", "восемь", "девять")
    val fromelventonineteen = listOf(
        "одиннадцать",
        "двенадцать",
        "тринадцать",
        "четырнадцать",
        "пятнадцать",
        "шестнадцать",
        "семнадцать",
        "восемнадцать",
        "девятнадцать"
    )
    val decades = listOf(
        "десять",
        "двадцать",
        "тридцать",
        "сорок",
        "пятьдесят",
        "шестьдесят",
        "семьдесят",
        "восемьдесят",
        "девяносто"
    )
    val tofirstdecade = listOf("один", "два", "три", "четыре", "пять", "шесть", "семь", "восемь", "девять")
    val thousandsends =
        listOf("тысяча", "тысячи", "тысячи", "тысячи", "тысяч", "тысяч", "тысяч", "тысяч", "тысяч")

    val servingArray = n.toString().reversed().toCharArray().map { it.toString().toInt() }

    var checkingforelevennineteen = 0

    // коварные 11-19
    if ((servingArray.size >= 2) && (servingArray.size <= 4)) {
        println("yes")
        if ((servingArray[1] * 10 + servingArray[0]) in 11..19) {
            checkingforelevennineteen += 1
        }
    } else if (servingArray.size >= 5) {
        if ((servingArray[1] * 10 + servingArray[0]) in 11..19) {
            checkingforelevennineteen += 1
        }
        if ((servingArray[3] + 10 * servingArray[4]) in 11..19) {
            checkingforelevennineteen += 2
        }
    }

    for (i in servingArray.indices) {
        if (i == 0) {
            if (((checkingforelevennineteen != 3) && (checkingforelevennineteen != 1)) && (servingArray[i] != 0)) {
                resultstr = tofirstdecade[servingArray[i] - 1] + " " + resultstr
            }
        }
        if (i == 1) {
            if ((checkingforelevennineteen == 1) || (checkingforelevennineteen == 3)) {
                resultstr = fromelventonineteen[servingArray[0] - 1] + " " + resultstr
            } else if ((checkingforelevennineteen != 1) && (servingArray[1] != 0)) {
                resultstr = decades[servingArray[i] - 1] + " " + resultstr
            }
        }
        if (i == 2) {
            if (servingArray[2] != 0) {
                resultstr = hundreds[servingArray[i] - 1] + " " + resultstr
            }
        }
        if (i == 3) {
            if ((servingArray[3] != 0) && !((checkingforelevennineteen == 2) || (checkingforelevennineteen == 3))) {
                resultstr = thousandsnum[servingArray[i] - 1] + " " + thousandsends[servingArray[i]-1] + " " + resultstr
            }
        }
        if (i == 4) {
            if ((checkingforelevennineteen == 2) || (checkingforelevennineteen == 3)) {
                resultstr = fromelventonineteen[servingArray[3] - 1] + " " + "тысяч" + " " + resultstr
            } else if ((servingArray[4] != 0) && (servingArray[3] != 0)){
                resultstr = decades[servingArray[i] - 1] + " " + resultstr
            } else if ((servingArray[4] != 0) && (servingArray[3] == 0)){
                resultstr = decades[servingArray[i] - 1] + " " + "тысяч" + " " + resultstr
            }
        }
        if (i == 5) {
            if ((servingArray[5] != 0) && (servingArray[4] == 0) && (servingArray[3] == 0)) {
                resultstr = hundreds[servingArray[5] - 1] + " " + "тысяч" + " " + resultstr
            }
            else {
                resultstr = hundreds[servingArray[5] - 1] + " " + resultstr
            }
        }
    }
    return resultstr.trim()
}