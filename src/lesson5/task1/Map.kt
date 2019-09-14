@file:Suppress("UNUSED_PARAMETER", "ConvertCallChainIntoSequence")

package lesson5.task1

import kotlin.math.max
import kotlin.math.min

/**
 * Пример
 *
 * Для заданного списка покупок `shoppingList` посчитать его общую стоимость
 * на основе цен из `costs`. В случае неизвестной цены считать, что товар
 * игнорируется.
 */
fun shoppingListCost(
    shoppingList: List<String>,
    costs: Map<String, Double>
): Double {
    var totalCost = 0.0

    for (item in shoppingList) {
        val itemCost = costs[item]
        if (itemCost != null) {
            totalCost += itemCost
        }
    }

    return totalCost
}

/**
 * Пример
 *
 * Для набора "имя"-"номер телефона" `phoneBook` оставить только такие пары,
 * для которых телефон начинается с заданного кода страны `countryCode`
 */
fun filterByCountryCode(
    phoneBook: MutableMap<String, String>,
    countryCode: String
) {
    val namesToRemove = mutableListOf<String>()

    for ((name, phone) in phoneBook) {
        if (!phone.startsWith(countryCode)) {
            namesToRemove.add(name)
        }
    }

    for (name in namesToRemove) {
        phoneBook.remove(name)
    }
}

/**
 * Пример
 *
 * Для заданного текста `text` убрать заданные слова-паразиты `fillerWords`
 * и вернуть отфильтрованный текст
 */
fun removeFillerWords(
    text: List<String>,
    vararg fillerWords: String
): List<String> {
    val fillerWordSet = setOf(*fillerWords)

    val res = mutableListOf<String>()
    for (word in text) {
        if (word !in fillerWordSet) {
            res += word
        }
    }
    return res
}

/**
 * Пример
 *
 * Для заданного текста `text` построить множество встречающихся в нем слов
 */
fun buildWordSet(text: List<String>): MutableSet<String> {
    val res = mutableSetOf<String>()
    for (word in text) res.add(word)
    return res
}


/**
 * Простая
 *
 * По заданному ассоциативному массиву "студент"-"оценка за экзамен" построить
 * обратный массив "оценка за экзамен"-"список студентов с этой оценкой".
 *
 * Например:
 *   buildGrades(mapOf("Марат" to 3, "Семён" to 5, "Михаил" to 5))
 *     -> mapOf(5 to listOf("Семён", "Михаил"), 3 to listOf("Марат"))
 */
fun buildGrades(grades: Map<String, Int>): Map<Int, List<String>> {
    val resultMap: MutableMap<Int, List<String>> = mutableMapOf()
    for (item in grades) {
        val points = item.value
        val name = item.key
        if (points in resultMap) {
            var currentList = resultMap[points]
            if (currentList != null) {
                currentList += listOf(name)
                resultMap[points] = currentList
            }
        } else {
            resultMap[points] = listOf(name)
        }
    }
    return resultMap
}

/**
 * Простая
 *
 * Определить, входит ли ассоциативный массив a в ассоциативный массив b;
 * это выполняется, если все ключи из a содержатся в b с такими же значениями.
 *
 * Например:
 *   containsIn(mapOf("a" to "z"), mapOf("a" to "z", "b" to "sweet")) -> true
 *   containsIn(mapOf("a" to "z"), mapOf("a" to "zee", "b" to "sweet")) -> false
 */
fun containsIn(a: Map<String, String>, b: Map<String, String>): Boolean {
    for (item in a) {
        val keya = item.key
        val valuea = item.value
        if (b[keya] != valuea) {
            return false
        }
    }
    return true
}

/**
 * Простая
 *
 * Удалить из изменяемого ассоциативного массива все записи,
 * которые встречаются в заданном ассоциативном массиве.
 * Записи считать одинаковыми, если и ключи, и значения совпадают.
 *
 * ВАЖНО: необходимо изменить переданный в качестве аргумента
 *        изменяемый ассоциативный массив
 *
 * Например:
 *   subtractOf(a = mutableMapOf("a" to "z"), mapOf("a" to "z"))
 *     -> a changes to mutableMapOf() aka becomes empty
 */
fun subtractOf(a: MutableMap<String, String>, b: Map<String, String>): Unit {
    for (item in b) {
        val keyb = item.key
        val valueb = item.value
        if (a[keyb] == valueb) {
            a.remove(keyb)
        }
    }
}

/**
 * Простая
 *
 * Для двух списков людей найти людей, встречающихся в обоих списках.
 * В выходном списке не должно быть повторяюихся элементов,
 * т. е. whoAreInBoth(listOf("Марат", "Семён, "Марат"), listOf("Марат", "Марат")) == listOf("Марат")
 */
fun whoAreInBoth(a: List<String>, b: List<String>): List<String> {
    val setA: MutableSet<String> = mutableSetOf()
    val setB: MutableSet<String> = mutableSetOf()
    setA.addAll(a)
    setB.addAll(b)
    return (setA.intersect(setB)).toList()
}

/**
 * Средняя
 *
 * Объединить два ассоциативных массива `mapA` и `mapB` с парами
 * "имя"-"номер телефона" в итоговый ассоциативный массив, склеивая
 * значения для повторяющихся ключей через запятую.
 * В случае повторяющихся *ключей* значение из mapA должно быть
 * перед значением из mapB.
 *
 * Повторяющиеся *значения* следует добавлять только один раз.
 *
 * Например:
 *   mergePhoneBooks(
 *     mapOf("Emergency" to "112", "Police" to "02"),
 *     mapOf("Emergency" to "911", "Police" to "02")
 *   ) -> mapOf("Emergency" to "112, 911", "Police" to "02")
 */
fun mergePhoneBooks(mapA: Map<String, String>, mapB: Map<String, String>): Map<String, String> {
    var resultMap: MutableMap<String, String> = mutableMapOf()
    for (item in mapA) {
        resultMap[item.key] = item.value
    }
    for (item in mapB) {
        if (item.key in resultMap) {
            val currentValue = resultMap[item.key]
            if (currentValue != null) {
                if ((item.value !in currentValue) || (item.value == "")) {
                    resultMap[item.key] = currentValue + ", " + item.value
                }
            }

        } else {
            resultMap[item.key] = item.value
        }
    }
    return resultMap.toMap()
}

/**
 * Средняя
 *
 * Для заданного списка пар "акция"-"стоимость" вернуть ассоциативный массив,
 * содержащий для каждой акции ее усредненную стоимость.
 *
 * Например:
 *   averageStockPrice(listOf("MSFT" to 100.0, "MSFT" to 200.0, "NFLX" to 40.0))
 *     -> mapOf("MSFT" to 150.0, "NFLX" to 40.0)
 */
fun averageStockPrice(stockPrices: List<Pair<String, Double>>): Map<String, Double> {
    val resultarr: MutableMap<String, Pair<Double, Double>> = mutableMapOf()
    for (item in stockPrices) {
        if (item.first in resultarr) {
            val cost = resultarr[item.first]
            if (cost != null) {
                resultarr[item.first] = Pair(cost.first + 1.0, cost.second + item.second)
            }
        } else {
            resultarr[item.first] = Pair(1.0, item.second)
        }
    }
    val resultMap: MutableMap<String, Double> = mutableMapOf()
    println(resultMap)
    for (item in resultarr) {
        resultMap[item.key] = item.value.second / item.value.first
    }
    return resultMap
}

/**
 * Средняя
 *
 * Входными данными является ассоциативный массив
 * "название товара"-"пара (тип товара, цена товара)"
 * и тип интересующего нас товара.
 * Необходимо вернуть название товара заданного типа с минимальной стоимостью
 * или null в случае, если товаров такого типа нет.
 *
 * Например:
 *   findCheapestStuff(
 *     mapOf("Мария" to ("печенье" to 20.0), "Орео" to ("печенье" to 100.0)),
 *     "печенье"
 *   ) -> "Мария"
 */

// воспользуемся стандартным поиском наименьшего

fun findCheapestStuff(stuff: Map<String, Pair<String, Double>>, kind: String): String? {
    var nameWeAreLookingFor = "element wasn\'t found, sorry("
    var minimumCost: Double = Double.MAX_VALUE
    for (item in stuff) {
        if ((item.value.first == kind) && (item.value.second < minimumCost)) {
            nameWeAreLookingFor = item.key
            minimumCost = item.value.second
        }
    }
    return if (nameWeAreLookingFor != "element wasn\'t found, sorry(") {
        nameWeAreLookingFor
    } else {
        null
    }
}

/**
 * Средняя
 *
 * Для заданного набора символов определить, можно ли составить из него
 * указанное слово (регистр символов игнорируется)
 *
 * Например:
 *   canBuildFrom(listOf('a', 'b', 'o'), "baobab") -> true
 */

fun canBuildFrom(chars: List<Char>, word: String): Boolean {
    if (word == "") return true
    val wordToLowerCase = word.toLowerCase().toList().toSet()
    val charsSet = chars.map { it.toLowerCase() }.toSet()
    return wordToLowerCase == wordToLowerCase.intersect(charsSet)
}

/**
 * Средняя
 *
 * Найти в заданном списке повторяющиеся элементы и вернуть
 * ассоциативный массив с информацией о числе повторений
 * для каждого повторяющегося элемента.
 * Если элемент встречается только один раз, включать его в результат
 * не следует.
 *
 * Например:
 *   extractRepeats(listOf("a", "b", "a")) -> mapOf("a" to 2)
 */

// считаем, сколько раз встречается данный элемент в списке
// создаем список с кандидатами "на вылет"
// убираем кандидатов из конечного ассоциативного массива

fun extractRepeats(list: List<String>): Map<String, Int> {
    val resultArr: MutableMap<String, Int> = mutableMapOf()
    val listToRemove: MutableList<String> = mutableListOf()
    for (item in list) {
        if (item in resultArr) {
            val currentValue = resultArr[item]
            if (currentValue != null) {
                resultArr[item] = currentValue + 1
            }
        } else {
            resultArr[item] = 1
        }
    }
    for (item in resultArr) {
        val currentValueRes = item.value
        if (currentValueRes == 1) {
            listToRemove.add(item.key)
        }
    }
    for (item in listToRemove) {
        resultArr.remove(item)
    }
    return resultArr.toMap()
}

/**
 * Средняя
 *
 * Для заданного списка слов определить, содержит ли он анаграммы
 * (два слова являются анаграммами, если одно можно составить из второго)
 *
 * Например:
 *   hasAnagrams(listOf("тор", "свет", "рот")) -> true
 */

// представляем слово как отсортированный массив Int = значению Char
// сортируем вспомогательный массив, содержащий все данные слова
// проверяем два соседних элемента на эквивалентность

fun hasAnagrams(words: List<String>): Boolean {
    var counter = 0
    for (i in words) {
        if (i == "") counter += 1
    }
    if (counter > 1) return true
    val helperArr: MutableList<List<Int>> = mutableListOf()
    for (i in words.indices) {
        val currentValue = words[i]
        if (currentValue != "") {
            helperArr.add(currentValue.toCharArray().map { it.toInt() }.sorted())
        } else {
            helperArr.add(listOf(-78557759))
        }

    }
    if (helperArr.isEmpty() || (helperArr.size == 1)) return false
    for (item in 0..helperArr.size - 2) {
        var q = item + 1
        for (itemTwo in q..helperArr.size - 1) {
            if (helperArr[item] == helperArr[itemTwo]) {
                return true
            }
        }
    }
    return false
}

/**
 * Сложная
 *
 * Для заданного ассоциативного массива знакомых через одно рукопожатие `friends`
 * необходимо построить его максимальное расширение по рукопожатиям, то есть,
 * для каждого человека найти всех людей, с которыми он знаком через любое
 * количество рукопожатий.
 * Считать, что все имена людей являются уникальными, а также что рукопожатия
 * являются направленными, то есть, если Марат знает Свету, то это не означает,
 * что Света знает Марата.
 *
 * Например:
 *   propagateHandshakes(
 *     mapOf(
 *       "Marat" to setOf("Mikhail", "Sveta"),
 *       "Sveta" to setOf("Marat"),
 *       "Mikhail" to setOf("Sveta")
 *     )
 *   ) -> mapOf(
 *          "Marat" to setOf("Mikhail", "Sveta"),
 *          "Sveta" to setOf("Marat", "Mikhail"),
 *          "Mikhail" to setOf("Sveta", "Marat")
 *        )
 */
fun propagateHandshakes(friends: Map<String, Set<String>>): Map<String, Set<String>> {
    val resultMap: MutableMap<String, MutableSet<String>> = mutableMapOf()
    val toCountEmpty: MutableSet<String> = mutableSetOf()
    for (item in friends) {
        resultMap[item.key] = item.value.toMutableSet()
        toCountEmpty.add(item.key)
        if (item.value != setOf("")) {
            toCountEmpty.addAll(item.value)
        }
        val currentValue = resultMap[item.key]
        var modifier: MutableList<String> = mutableListOf()
        if ((currentValue != null && (currentValue != mutableListOf("")))) {
            for (i in currentValue) {
                val helper = friends[i]
                if (helper != null) {
                    modifier = (helper - setOf("") - item.key).toMutableList()
                }
            }
        }
        resultMap[item.key]?.addAll(modifier)
    }
    for (item in toCountEmpty) {
        if (item !in resultMap) {
            resultMap[item] = mutableSetOf()
        }
    }
    return resultMap.toMap()
}


/**
 * Сложная
 *
 * Для заданного списка неотрицательных чисел и числа определить,
 * есть ли в списке пара чисел таких, что их сумма равна заданному числу.
 * Если да, верните их индексы в виде Pair<Int, Int>;
 * если нет, верните пару Pair(-1, -1).
 *
 * Индексы в результате должны следовать в порядке (меньший, больший).
 *
 * Постарайтесь сделать ваше решение как можно более эффективным,
 * используя то, что вы узнали в данном уроке.
 *
 * Например:
 *   findSumOfTwo(listOf(1, 2, 3), 4) -> Pair(0, 2)
 *   findSumOfTwo(listOf(1, 2, 3), 6) -> Pair(-1, -1)
 */
fun findSumOfTwo(list: List<Int>, number: Int): Pair<Int, Int> {
    for (i in list.indices) {
        if (((number - list[i]) in list) && (list.indexOf((number - list[i])) != i)) {
            return Pair(list.indexOf(min(list[i], (number - list[i]))), list.indexOf(max(list[i], (number - list[i]))))
        }
    }
    return Pair(-1, -1)
}

/**
 * Очень сложная
 *
 * Входными данными является ассоциативный массив
 * "название сокровища"-"пара (вес сокровища, цена сокровища)"
 * и вместимость вашего рюкзака.
 * Необходимо вернуть множество сокровищ с максимальной суммарной стоимостью,
 * которые вы можете унести в рюкзаке.
 *
 * Перед решением этой задачи лучше прочитать статью Википедии "Динамическое программирование".
 *
 * Например:
 *   bagPacking(
 *     mapOf("Кубок" to (500 to 2000), "Слиток" to (1000 to 5000)),
 *     850
 *   ) -> setOf("Кубок")
 *   bagPacking(
 *     mapOf("Кубок" to (500 to 2000), "Слиток" to (1000 to 5000)),
 *     450
 *   ) -> emptySet()
 */
// сортируем сокровища по "плотности", исхищряясь с типами через неоднократную вложенность Pair
// упаковываем в рюкзак сокровища по плотности, учитывая постепенную наполняемость. Profit!

fun bagPacking(treasures: Map<String, Pair<Int, Int>>, capacity: Int): Set<String> {
    val resultSet: MutableSet<String> = mutableSetOf()
    val helperArr: MutableList<Pair<Pair<String, Double>, Pair<Int, Int>>> = mutableListOf()
    for (item in treasures) {
        helperArr.add(
            Pair(
                Pair(item.key, item.value.second.toDouble() / item.value.first.toDouble()),
                Pair(item.value.first, item.value.second)
            )
        )
    }
    helperArr.sortByDescending { it.first.second }
    var currentPrice: MutableMap<String, Int> = mutableMapOf()
    for (lostName in treasures) {
        var variableCapacity = capacity
        for (item in helperArr) {
            if ((item.first.first != lostName.key) && (item.second.first <= variableCapacity)) {
                val secondhelper = currentPrice[item.first.first]
                if (secondhelper != null) {
                    currentPrice[item.first.first] = secondhelper + item.second.second
                }
                variableCapacity -= item.second.first
            }
        }
    }
    var maximum = Int.MIN_VALUE
    var CurName = ""
    for (item in currentPrice) {
        if (item.value > maximum) {
            maximum = item.value
            CurName = item.key
        }
    }
    var variableCapacity = capacity
    for (item in helperArr) {
        if ((item.first.first != CurName) && (item.second.first <= variableCapacity)) {
            resultSet.add(item.first.first)
            variableCapacity -= item.second.first
        }
    }
    return resultSet
}