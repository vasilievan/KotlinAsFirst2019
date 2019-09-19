@file:Suppress("UNUSED_PARAMETER", "ConvertCallChainIntoSequence")

package lesson5.task1

import javax.swing.text.html.HTML.Attribute.N
import java.lang.Integer
import kotlin.math.*


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
    val resultMap = mutableMapOf<Int, MutableList<String>>()
    for ((name, points) in grades) {
        if (points in resultMap) {
            val currentList = resultMap[points]
            if (currentList != null) {
                currentList.add(name)
                resultMap[points] = currentList
            }
        } else {
            resultMap[points] = mutableListOf(name)
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
fun containsIn(a: Map<String, String>, b: Map<String, String>): Boolean = a.all { (key, value) -> b[key] == value }

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
    for ((keyb, valueb) in b) {
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
fun whoAreInBoth(a: List<String>, b: List<String>): List<String> = (a.toSet().intersect(b.toSet())).toList()

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
    val resultMap = mutableMapOf<String, MutableSet<String>>()
    for ((key, value) in mapA) {
        resultMap[key] = mutableSetOf(value)
    }
    for ((key, value) in mapB) {
        if (key in resultMap) {
            val currentValue = resultMap[key]
            if (currentValue != null) {
                if (value !in currentValue) {
                    resultMap[key]?.add(value)
                }
            }
        } else {
            resultMap[key] = mutableSetOf(value)
        }
    }
    val last = mutableMapOf<String, String>()
    for ((key, value) in resultMap) {
        last[key] = value.toString().removeSuffix("]").removePrefix("[")
    }
    return last
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
    val resultarr = mutableMapOf<String, Pair<Int, Double>>()
    for ((first, second) in stockPrices) {
        if (first in resultarr) {
            val cost = resultarr[first]
            if (cost != null) {
                resultarr[first] = Pair(cost.first + 1, cost.second + second)
            }
        } else {
            resultarr[first] = Pair(1, second)
        }
    }
    val resultMap = mutableMapOf<String, Double>()
    for ((key, value) in resultarr) {
        resultMap[key] = value.second / value.first
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
    var nameWeAreLookingFor: String? = null
    var minimumCost = Double.POSITIVE_INFINITY
    for ((key, value) in stuff) {
        if ((value.first == kind) && (value.second < minimumCost)) {
            nameWeAreLookingFor = key
            minimumCost = value.second
        }
    }
    return nameWeAreLookingFor
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
    val resultArr = mutableMapOf<String, Int>()
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
    return resultArr.filter { it.value != 1 }.toMap()
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
    val helperArr = mutableListOf<List<Int>>()
    for (i in words.indices) {
        val currentValue = words[i]
        if (currentValue != "") {
            helperArr.add(currentValue.map { it.toInt() }.sorted())
        } else {
            helperArr.add(listOf(-78557759))
        }

    }
    if (helperArr.isEmpty() || (helperArr.size == 1)) return false
    for (item in 0..helperArr.size - 2) {
        val q = item + 1
        for (itemTwo in q until helperArr.size) {
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
    val resultArr = mutableMapOf<String, Set<String>>()
    for (item in friends) {
        for (itemvalue in item.value) {
            resultArr[itemvalue] = mutableSetOf()
        }
    }
    for ((key, value) in friends) {
        resultArr[key] = value.toMutableSet()
    }
    for (i in 0..(ceil(log2(friends.size.toDouble())) + 1).toInt()) {
        for ((key, value) in friends) {
            for (itemvalue in value) {
                for ((key1, value1) in resultArr) {
                    if (itemvalue == key1) {
                        val helpervalue = resultArr[key]
                        if (helpervalue != null) {
                            resultArr[key] = helpervalue.union(value1 - key)
                        }
                    }
                }
            }
        }
    }
    return resultArr
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

fun bagPacking(treasures: Map<String, Pair<Int, Int>>, capacity: Int): Set<String> {
    if (treasures.size == 1) {
        return treasures.keys
    }
    val arrayForLimits = mutableListOf<Int>()

    for ((key, value) in treasures) {
        arrayForLimits.add(value.first)
    }

    arrayForLimits.sortByDescending { it }
    var down = 0
    var varcapacity = capacity

    for (item in arrayForLimits) {
        if (varcapacity > item) {
            varcapacity -= item
            down++
        }
    }

    arrayForLimits.sortBy { it }
    var up = 0
    varcapacity = capacity


    for (item in arrayForLimits) {
        if (varcapacity > item) {
            varcapacity -= item
            up++
        }
    }

    fun combinations(list: List<String>, down: Int, up: Int): MutableList<MutableList<String>> {
        val resultFun = mutableListOf<MutableList<String>>()
        for (i in 1 until 2.0.pow(list.size).toInt()) {
            val tempList = mutableListOf<String>()
            var binaryi = i.toString(2)
            binaryi = "0".repeat(list.size - binaryi.length) + binaryi
            val counter = binaryi.filter { it != '0' }.length
            if (counter in down..up) {
                for (item in binaryi.indices) {
                    if (binaryi[item] == '1') {
                        tempList.add(list[item])
                    }
                }
                resultFun.add(tempList)
            }
        }
        return resultFun
    }

    val allPossibleCombinations = combinations(treasures.keys.toList(), down, up)
    var resultArray = setOf<String>()
    var maxPrice = 0

    for (item in allPossibleCombinations) {
        var weight = 0
        var price = 0
        for (insideOfList in item) {
            weight += treasures[insideOfList]?.first!!
            price += treasures[insideOfList]?.second!!
            if (weight > capacity) {
                price = 0
                break
            }
        }
        if (price > maxPrice) {
            maxPrice = price
            resultArray = item.toSet()
        }
    }
    return resultArray
}
