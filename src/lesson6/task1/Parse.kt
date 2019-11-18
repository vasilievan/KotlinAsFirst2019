@file:Suppress("UNUSED_PARAMETER", "ConvertCallChainIntoSequence")

package lesson6.task1

import lesson2.task2.daysInMonth
import lesson3.task1.cos

/**
 * Пример
 *
 * Время представлено строкой вида "11:34:45", содержащей часы, минуты и секунды, разделённые двоеточием.
 * Разобрать эту строку и рассчитать количество секунд, прошедшее с начала дня.
 */
fun timeStrToSeconds(str: String): Int {
    val parts = str.split(":")
    var result = 0
    for (part in parts) {
        val number = part.toInt()
        result = result * 60 + number
    }
    return result
}

/**
 * Пример
 *
 * Дано число n от 0 до 99.
 * Вернуть его же в виде двухсимвольной строки, от "00" до "99"
 */
fun twoDigitStr(n: Int) = if (n in 0..9) "0$n" else "$n"

/**
 * Пример
 *
 * Дано seconds -- время в секундах, прошедшее с начала дня.
 * Вернуть текущее время в виде строки в формате "ЧЧ:ММ:СС".
 */
fun timeSecondsToStr(seconds: Int): String {
    val hour = seconds / 3600
    val minute = (seconds % 3600) / 60
    val second = seconds % 60
    return String.format("%02d:%02d:%02d", hour, minute, second)
}

/**
 * Пример: консольный ввод
 */
fun main() {
    println("Введите время в формате ЧЧ:ММ:СС")
    val line = readLine()
    if (line != null) {
        val seconds = timeStrToSeconds(line)
        if (seconds == -1) {
            println("Введённая строка $line не соответствует формату ЧЧ:ММ:СС")
        } else {
            println("Прошло секунд с начала суток: $seconds")
        }
    } else {
        println("Достигнут <конец файла> в процессе чтения строки. Программа прервана")
    }
}


/**
 * Средняя
 *
 * Дата представлена строкой вида "15 июля 2016".
 * Перевести её в цифровой формат "15.07.2016".
 * День и месяц всегда представлять двумя цифрами, например: 03.04.2011.
 * При неверном формате входной строки вернуть пустую строку.
 *
 * Обратите внимание: некорректная с точки зрения календаря дата (например, 30.02.2009) считается неверными
 * входными данными.
 */

fun dateStrToDigit(str: String): String {
    if (!str.matches(Regex("""\d{1,2} [а-я]+ \d+"""))) {
        return ""
    }
    val monthesnames = listOf(
        "января",
        "февраля",
        "марта",
        "апреля",
        "мая",
        "июня",
        "июля",
        "августа",
        "сентября",
        "октября",
        "ноября",
        "декабря"
    )
    val converted = str.split(" ")
    if (converted[1] !in monthesnames) {
        return ""
    }
    val appropriateDaysInMonth = daysInMonth(monthesnames.indexOf(converted[1]) + 1, converted[2].toInt()) >= converted[0].toInt()
    return if (appropriateDaysInMonth) {
        "%02d.%02d.%s".format(converted[0].toInt(), monthesnames.indexOf(converted[1]) + 1, converted[2])
    } else {
        ""
    }
}

/**
 * Средняя
 *
 * Дата представлена строкой вида "15.07.2016".
 * Перевести её в строковый формат вида "15 июля 2016".
 * При неверном формате входной строки вернуть пустую строку
 *
 * Обратите внимание: некорректная с точки зрения календаря дата (например, 30 февраля 2009) считается неверными
 * входными данными.
 */
fun dateDigitToStr(digital: String): String {
    if (!digital.matches(Regex("""\d{2}.\d{2}.\d+"""))) {
        return ""
    }
    val converted = digital.split(".")
    val monthesnames = listOf(
        "января",
        "февраля",
        "марта",
        "апреля",
        "мая",
        "июня",
        "июля",
        "августа",
        "сентября",
        "октября",
        "ноября",
        "декабря"
    )
    val day = converted[0].removePrefix("0").toInt()
    val month = converted[1].removePrefix("0").toInt()
    if ((month > 12) || (month < 1)) {
        return ""
    }
    return if (daysInMonth(month, converted[2].toInt()) >= day) {
        "$day ${monthesnames[month - 1]} ${converted[2]}"
    } else {
        ""
    }
}

/**
 * Средняя
 *
 * Номер телефона задан строкой вида "+7 (921) 123-45-67".
 * Префикс (+7) может отсутствовать, код города (в скобках) также может отсутствовать.
 * Может присутствовать неограниченное количество пробелов и чёрточек,
 * например, номер 12 --  34- 5 -- 67 -89 тоже следует считать легальным.
 * Перевести номер в формат без скобок, пробелов и чёрточек (но с +), например,
 * "+79211234567" или "123456789" для приведённых примеров.
 * Все символы в номере, кроме цифр, пробелов и +-(), считать недопустимыми.
 * При неверном формате вернуть пустую строку.
 *
 * PS: Дополнительные примеры работы функции можно посмотреть в соответствующих тестах.
 */
fun flattenPhoneNumber(phone: String): String {
    return if (phone.matches(Regex("""^(\+)?(\d)+ *(\((\d)+ *-* *(\d)*\))?((\d)*-* *)*$"""))) {
        phone.replace(Regex("""[()\- ]"""), "")
    } else {
        ""
    }
}

/**
 * Средняя
 *
 * Результаты спортсмена на соревнованиях в прыжках в длину представлены строкой вида
 * "706 - % 717 % 703".
 * В строке могут присутствовать числа, черточки - и знаки процента %, разделённые пробелами;
 * число соответствует удачному прыжку, - пропущенной попытке, % заступу.
 * Прочитать строку и вернуть максимальное присутствующее в ней число (717 в примере).
 * При нарушении формата входной строки или при отсутствии в ней чисел, вернуть -1.
 */
fun bestLongJump(jumps: String): Int {
    if (!jumps.matches(Regex("""^((\d)+[ %\-]* )*((\d)+[ %\-]*)$"""))) {
        return -1
    }
    val found = Regex("""(\d)+""").findAll(jumps)
    val answer = found.map { it.value.toInt() }.max()
    return answer ?: -1
}

/**
 * Сложная
 *
 * Результаты спортсмена на соревнованиях в прыжках в высоту представлены строкой вида
 * "220 + 224 %+ 228 %- 230 + 232 %%- 234 %".
 * Здесь + соответствует удачной попытке, % неудачной, - пропущенной.
 * Высота и соответствующие ей попытки разделяются пробелом.
 * Прочитать строку и вернуть максимальную взятую высоту (230 в примере).
 * При нарушении формата входной строки, а также в случае отсутствия удачных попыток,
 * вернуть -1.
 */
fun bestHighJump(jumps: String): Int {
    if (!jumps.matches(Regex("""(\d+ [+%\-]{1,3} ?)+"""))) {
        return -1
    }
    val found = Regex("""\d+ %?-?\++%?-?""").findAll(jumps)
    val maximum = found.map { it.value.replace(Regex("""[ +%\-]"""), "").toInt() }.max()
    return maximum ?: -1
}

/**
 * Сложная
 *
 * В строке представлено выражение вида "2 + 31 - 40 + 13",
 * использующее целые положительные числа, плюсы и минусы, разделённые пробелами.
 * Наличие двух знаков подряд "13 + + 10" или двух чисел подряд "1 2" не допускается.
 * Вернуть значение выражения (6 для примера).
 * Про нарушении формата входной строки бросить исключение IllegalArgumentException
 */
fun plusMinus(expression: String): Int {
    if (!expression.matches(Regex("""(\d+ [+-] )*\d+"""))) {
        throw IllegalArgumentException()
    }
    val numbers = Regex("""\d+""").findAll(expression)
    val plandmi = Regex("""[+\-]""").findAll(expression)
    val listOfNumbers = mutableListOf<Int>()
    val listOfPaM = mutableListOf("+")
    for (number in numbers) {
        listOfNumbers.add(number.value.toInt())
    }
    for (plusOrMinus in plandmi) {
        listOfPaM.add(plusOrMinus.value)
    }
    var answer = 0
    for (i in listOfNumbers.indices) {
        if (listOfPaM[i] == "+") {
            answer += listOfNumbers[i]
        } else {
            answer -= listOfNumbers[i]
        }
    }
    return answer
}

/**
 * Сложная
 *
 * Строка состоит из набора слов, отделённых друг от друга одним пробелом.
 * Определить, имеются ли в строке повторяющиеся слова, идущие друг за другом.
 * Слова, отличающиеся только регистром, считать совпадающими.
 * Вернуть индекс начала первого повторяющегося слова, или -1, если повторов нет.
 * Пример: "Он пошёл в в школу" => результат 9 (индекс первого 'в')
 */
fun firstDuplicateIndex(str: String): Int {
    val words = Regex("""\S+""").findAll(str)
    val indexedArr = mutableListOf<String>()
    var currentPosition = 0
    for (item in words) {
        indexedArr.add(item.value.toLowerCase())
    }
    for (i in 1 until indexedArr.size) {
        if (indexedArr[i - 1] == indexedArr[i]) {
            return currentPosition
        } else {
            currentPosition += indexedArr[i - 1].length + 1
        }
    }
    return -1
}

/**
 * Сложная
 *
 * Строка содержит названия товаров и цены на них в формате вида
 * "Хлеб 39.9; Молоко 62; Курица 184.0; Конфеты 89.9".
 * То есть, название товара отделено от цены пробелом,
 * а цена отделена от названия следующего товара точкой с запятой и пробелом.
 * Вернуть название самого дорогого товара в списке (в примере это Курица),
 * или пустую строку при нарушении формата строки.
 * Все цены должны быть больше либо равны нуля.
 */
fun mostExpensive(description: String): String {
    if (!description.matches(Regex("""^(\S+ (\d)+(\.)?(\d)*; )*(\S+ (\d)+(\.)?(\d)*)$"""))) {
        return ""
    }
    val goods = description.split("; ")
    val names = mutableListOf<String>()
    val costs = mutableListOf<Double>()
    for (anyGood in goods) {
        val splited = anyGood.split(" ")
        names.add(splited[0])
        costs.add(splited[1].toDouble())
    }
    var maximum = Double.NEGATIVE_INFINITY
    var answer = ""
    for (i in costs.indices) {
        if (costs[i] > maximum) {
            maximum = costs[i]
            answer = names[i]
        }
    }
    return answer
}

/**
 * Сложная
 *
 * Перевести число roman, заданное в римской системе счисления,
 * в десятичную систему и вернуть как результат.
 * Римские цифры: 1 = I, 4 = IV, 5 = V, 9 = IX, 10 = X, 40 = XL, 50 = L,
 * 90 = XC, 100 = C, 400 = CD, 500 = D, 900 = CM, 1000 = M.
 * Например: XXIII = 23, XLIV = 44, C = 100
 *
 * Вернуть -1, если roman не является корректным римским числом
 */
fun fromRoman(roman: String): Int {
    if (roman == "") {
        return -1
    }
    val indicator = roman.contains(Regex("""^M{0,3}((CM|CD|D)?C{0,3})((XC|XL|L)?X{0,3})((IX|IV|V)?I{0,3})$"""))
    val numbersArr = mutableListOf<Int>()
    var summaryResult = 0
    if (!indicator) {
        return -1
    }
    for (item in roman) {
        when (item) {
            'I' -> numbersArr.add(1)
            'V' -> numbersArr.add(5)
            'X' -> numbersArr.add(10)
            'L' -> numbersArr.add(50)
            'C' -> numbersArr.add(100)
            'D' -> numbersArr.add(500)
            'M' -> numbersArr.add(1000)
        }
    }
    for (i in 1 until numbersArr.size) {
        if (numbersArr[i - 1] < numbersArr[i]) {
            summaryResult -= numbersArr[i - 1]
        } else {
            summaryResult += numbersArr[i - 1]
        }
    }
    return summaryResult + numbersArr[numbersArr.size - 1]
}

/**
 * Очень сложная
 *
 * Имеется специальное устройство, представляющее собой
 * конвейер из cells ячеек (нумеруются от 0 до cells - 1 слева направо) и датчик, двигающийся над этим конвейером.
 * Строка commands содержит последовательность команд, выполняемых данным устройством, например +>+>+>+>+
 * Каждая команда кодируется одним специальным символом:
 *	> - сдвиг датчика вправо на 1 ячейку;
 *  < - сдвиг датчика влево на 1 ячейку;
 *	+ - увеличение значения в ячейке под датчиком на 1 ед.;
 *	- - уменьшение значения в ячейке под датчиком на 1 ед.;
 *	[ - если значение под датчиком равно 0, в качестве следующей команды следует воспринимать
 *  	не следующую по порядку, а идущую за соответствующей следующей командой ']' (с учётом вложенности);
 *	] - если значение под датчиком не равно 0, в качестве следующей команды следует воспринимать
 *  	не следующую по порядку, а идущую за соответствующей предыдущей командой '[' (с учётом вложенности);
 *      (комбинация [] имитирует цикл)
 *  пробел - пустая команда
 *
 * Изначально все ячейки заполнены значением 0 и датчик стоит на ячейке с номером N/2 (округлять вниз)
 *
 * После выполнения limit команд или всех команд из commands следует прекратить выполнение последовательности команд.
 * Учитываются все команды, в том числе несостоявшиеся переходы ("[" при значении под датчиком не равном 0 и "]" при
 * значении под датчиком равном 0) и пробелы.
 *
 * Вернуть список размера cells, содержащий элементы ячеек устройства после завершения выполнения последовательности.
 * Например, для 10 ячеек и командной строки +>+>+>+>+ результат должен быть 0,0,0,0,0,1,1,1,1,1
 *
 * Все прочие символы следует считать ошибочными и формировать исключение IllegalArgumentException.
 * То же исключение формируется, если у символов [ ] не оказывается пары.
 * Выход за границу конвейера также следует считать ошибкой и формировать исключение IllegalStateException.
 * Считать, что ошибочные символы и непарные скобки являются более приоритетной ошибкой чем выход за границу ленты,
 * то есть если в программе присутствует некорректный символ или непарная скобка, то должно быть выброшено
 * IllegalArgumentException.
 * IllegalArgumentException должен бросаться даже если ошибочная команда не была достигнута в ходе выполнения.
 *
 */
fun computeDeviceCells(cells: Int, commands: String, limit: Int): List<Int> = TODO()
