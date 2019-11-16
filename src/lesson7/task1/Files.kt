@file:Suppress("UNUSED_PARAMETER", "ConvertCallChainIntoSequence")

package lesson7.task1

import java.io.File
import kotlin.math.ceil

/**
 * Пример
 *
 * Во входном файле с именем inputName содержится некоторый текст.
 * Вывести его в выходной файл с именем outputName, выровняв по левому краю,
 * чтобы длина каждой строки не превосходила lineLength.
 * Слова в слишком длинных строках следует переносить на следующую строку.
 * Слишком короткие строки следует дополнять словами из следующей строки.
 * Пустые строки во входном файле обозначают конец абзаца,
 * их следует сохранить и в выходном файле
 */
fun alignFile(inputName: String, lineLength: Int, outputName: String) {
    val outputStream = File(outputName).bufferedWriter()
    var currentLineLength = 0
    for (line in File(inputName).readLines()) {
        if (line.isEmpty()) {
            outputStream.newLine()
            if (currentLineLength > 0) {
                outputStream.newLine()
                currentLineLength = 0
            }
            continue
        }
        for (word in line.split(" ")) {
            if (currentLineLength > 0) {
                if (word.length + currentLineLength >= lineLength) {
                    outputStream.newLine()
                    currentLineLength = 0
                } else {
                    outputStream.write(" ")
                    currentLineLength++
                }
            }
            outputStream.write(word)
            currentLineLength += word.length
        }
    }
    outputStream.close()
}

/**
 * Средняя
 *
 * Во входном файле с именем inputName содержится некоторый текст.
 * На вход подаётся список строк substrings.
 * Вернуть ассоциативный массив с числом вхождений каждой из строк в текст.
 * Регистр букв игнорировать, то есть буквы е и Е считать одинаковыми.
 *
 */
fun countSubstrings(inputName: String, substrings: List<String>): Map<String, Int> {
    val inputFile = File(inputName)
    val resultMap = mutableMapOf<String, Int>()
    for (str in substrings) {
        resultMap[str] = 0
    }
    for (line in inputFile.readLines()) {
        for (i in line.indices) {
            for (key in resultMap.keys) {
                val lowkey = key.toLowerCase()
                if (i + key.length <= line.length) {
                    if (lowkey == line.toLowerCase().substring(i, i + key.length)) {
                        val tmp = resultMap[key]
                        if (tmp != null) {
                            resultMap[key] = tmp + 1
                        }
                    }
                }
            }
        }
    }
    return resultMap
}


/**
 * Средняя
 *
 * В русском языке, как правило, после букв Ж, Ч, Ш, Щ пишется И, А, У, а не Ы, Я, Ю.
 * Во входном файле с именем inputName содержится некоторый текст на русском языке.
 * Проверить текст во входном файле на соблюдение данного правила и вывести в выходной
 * файл outputName текст с исправленными ошибками.
 *
 * Регистр заменённых букв следует сохранять.
 *
 * Исключения (жюри, брошюра, парашют) в рамках данного задания обрабатывать не нужно
 *
 */
fun sibilants(inputName: String, outputName: String) {
    val inputfile = File(inputName)
    val outputfile = File(outputName).bufferedWriter()
    val triggers = setOf('ж', 'ч', 'ш', 'щ')
    val alsotriggers = setOf('ы', 'я', 'ю')
    for (line in inputfile.readLines()) {
        val strtoapp = StringBuilder()
        strtoapp.append(line[0])
        for (position in 1 until line.length) {
            if ((line[position - 1].toLowerCase() in triggers) && (line[position].toLowerCase() in alsotriggers)) {
                val tmp = when (line[position].toLowerCase()) {
                    'ы' -> 'и'
                    'я' -> 'а'
                    else -> 'у'
                }
                if (line[position].isUpperCase()) {
                    strtoapp.append(tmp.toUpperCase())
                } else {
                    strtoapp.append(tmp)
                }
            } else {
                strtoapp.append(line[position])
            }
        }
        outputfile.write(strtoapp.toString() + "\n")
    }
    outputfile.close()
}

/**
 * Средняя
 *
 * Во входном файле с именем inputName содержится некоторый текст (в том числе, и на русском языке).
 * Вывести его в выходной файл с именем outputName, выровняв по центру
 * относительно самой длинной строки.
 *
 * Выравнивание следует производить путём добавления пробелов в начало строки.
 *
 *
 * Следующие правила должны быть выполнены:
 * 1) Пробелы в начале и в конце всех строк не следует сохранять.
 * 2) В случае невозможности выравнивания строго по центру, строка должна быть сдвинута в ЛЕВУЮ сторону
 * 3) Пустые строки не являются особым случаем, их тоже следует выравнивать
 * 4) Число строк в выходном файле должно быть равно числу строк во входном (в т. ч. пустых)
 *
 */
fun centerFile(inputName: String, outputName: String) {
    val inputFile = File(inputName).readLines()
    val outputFile = File(outputName).bufferedWriter()
    var maximumLength = 0
    for (line in inputFile) {
        val deletedSpaces = line.trim()
        if (deletedSpaces.length > maximumLength) {
            maximumLength = deletedSpaces.length
        }
    }
    for (line in inputFile) {
        val deletedSpaces = line.trim()
        val tmp = StringBuilder()
        val space = ceil(((maximumLength - deletedSpaces.length) / 2).toDouble()).toInt()
        tmp.append(" ".repeat(space))
        tmp.append(deletedSpaces)
        outputFile.write(tmp.toString() + "\n")
    }
    outputFile.close()
}

/**
 * Сложная
 *
 * Во входном файле с именем inputName содержится некоторый текст (в том числе, и на русском языке).
 * Вывести его в выходной файл с именем outputName, выровняв по левому и правому краю относительно
 * самой длинной строки.
 * Выравнивание производить, вставляя дополнительные пробелы между словами: равномерно по всей строке
 *
 * Слова внутри строки отделяются друг от друга одним или более пробелом.
 *
 * Следующие правила должны быть выполнены:
 * 1) Каждая строка входного и выходного файла не должна начинаться или заканчиваться пробелом.
 * 2) Пустые строки или строки из пробелов трансформируются в пустые строки без пробелов.
 * 3) Строки из одного слова выводятся без пробелов.
 * 4) Число строк в выходном файле должно быть равно числу строк во входном (в т. ч. пустых).
 *
 * Равномерность определяется следующими формальными правилами:
 * 5) Число пробелов между каждыми двумя парами соседних слов не должно отличаться более, чем на 1.
 * 6) Число пробелов между более левой парой соседних слов должно быть больше или равно числу пробелов
 *    между более правой парой соседних слов.
 *
 * Следует учесть, что входной файл может содержать последовательности из нескольких пробелов  между слвоами. Такие
 * последовательности следует учитывать при выравнивании и при необходимости избавляться от лишних пробелов.
 * Из этого следуют следующие правила:
 * 7) В самой длинной строке каждая пара соседних слов должна быть отделена В ТОЧНОСТИ одним пробелом
 * 8) Если входной файл удовлетворяет требованиям 1-7, то он должен быть в точности идентичен выходному файлу
 */
fun alignFileByWidth(inputName: String, outputName: String) {
    val inputFile = File(inputName).readLines()
    val outputFile = File(outputName).bufferedWriter()
    var itsLongitude = 0
    val separateWords = mutableListOf<MutableList<String>>()
    val strLengthes = mutableListOf<Int>()
    val strWordsAmounts = mutableListOf<Int>()
    for (line in inputFile) {
        val tmp = Regex("""\S+""").findAll(line)
        val wordsInLine = mutableListOf<String>()
        var currentLength = 0
        for (item in tmp) {
            wordsInLine.add(item.value)
            currentLength += item.value.length
        }
        strLengthes.add(currentLength)
        if (currentLength + wordsInLine.size - 1 > itsLongitude) {
            itsLongitude = currentLength + wordsInLine.size - 1
        }
        separateWords.add(wordsInLine)
        strWordsAmounts.add(wordsInLine.size)
    }
    for (differentLength in 0 until strLengthes.size) {
        if (strLengthes[differentLength] + strWordsAmounts[differentLength] - 1 == itsLongitude) {
            val strToAdd = StringBuilder()
            for (item in separateWords[differentLength]) {
                strToAdd.append("$item ")
            }
            outputFile.write(strToAdd.toString().trim() + "\n")
        } else {
            if (separateWords[differentLength].size > 1) {
                val spacedToAdd =
                    (itsLongitude - strLengthes[differentLength]) / (separateWords[differentLength].size - 1)
                var divisionRest =
                    (itsLongitude - strLengthes[differentLength]) % (separateWords[differentLength].size - 1)
                val strToAdd = StringBuilder()
                for (item in separateWords[differentLength]) {
                    if (divisionRest > 0) {
                        strToAdd.append(item)
                        divisionRest -= 1
                        strToAdd.append(" ".repeat(spacedToAdd + 1))
                    } else {
                        strToAdd.append(item)
                        strToAdd.append(" ".repeat(spacedToAdd))
                    }
                }
                outputFile.write(strToAdd.toString().trim() + "\n")
            } else if (separateWords[differentLength].size == 0) {
                outputFile.write("\n")
            } else if (separateWords[differentLength].size == 1) {
                outputFile.write(separateWords[differentLength][0].trim() + "\n")
            }
        }
    }
    outputFile.close()
}

/**
 * Средняя
 *
 * Во входном файле с именем inputName содержится некоторый текст (в том числе, и на русском языке).
 *
 * Вернуть ассоциативный массив, содержащий 20 наиболее часто встречающихся слов с их количеством.
 * Если в тексте менее 20 различных слов, вернуть все слова.
 *
 * Словом считается непрерывная последовательность из букв (кириллических,
 * либо латинских, без знаков препинания и цифр).
 * Цифры, пробелы, знаки препинания считаются разделителями слов:
 * Привет, привет42, привет!!! -привет?!
 * ^ В этой строчке слово привет встречается 4 раза.
 *
 * Регистр букв игнорировать, то есть буквы е и Е считать одинаковыми.
 * Ключи в ассоциативном массиве должны быть в нижнем регистре.
 *
 */
fun top20Words(inputName: String): Map<String, Int> {
    val inputFile = File(inputName).readLines()
    val wordsMap = mutableMapOf<String, Int>()
    for (line in inputFile) {
        val wordsLine = Regex("""([A-Za-zА-Яа-яёЁ]+)""").findAll(line)
        for (word in wordsLine) {
            val current = word.value.toLowerCase()
            if (current in wordsMap) {
                val tmp = wordsMap[current]
                if (tmp != null) {
                    wordsMap[current] = tmp + 1
                }
            } else {
                wordsMap[current] = 1
            }
        }
    }
    if (wordsMap.size >= 20) {
        val answer = mutableMapOf<String, Int>()
        val sortedWordsMap = wordsMap.toList().sortedByDescending { (_, value) -> value }.toMap()
        var i = 0
        for ((key, value) in sortedWordsMap) {
            if (i < 20) {
                answer[key] = value
                i++
            } else {
                return answer
            }
        }
    }
    return wordsMap
}

/**
 * Средняя
 *
 * Реализовать транслитерацию текста из входного файла в выходной файл посредством динамически задаваемых правил.

 * Во входном файле с именем inputName содержится некоторый текст (в том числе, и на русском языке).
 *
 * В ассоциативном массиве dictionary содержится словарь, в котором некоторым символам
 * ставится в соответствие строчка из символов, например
 * mapOf('з' to "zz", 'р' to "r", 'д' to "d", 'й' to "y", 'М' to "m", 'и' to "yy", '!' to "!!!")
 *
 * Необходимо вывести в итоговый файл с именем outputName
 * содержимое текста с заменой всех символов из словаря на соответствующие им строки.
 *
 * При этом регистр символов в словаре должен игнорироваться,
 * но при выводе символ в верхнем регистре отображается в строку, начинающуюся с символа в верхнем регистре.
 *
 * Пример.
 * Входной текст: Здравствуй, мир!
 *
 * заменяется на
 *
 * Выходной текст: Zzdrавствуy, mир!!!
 *
 * Пример 2.
 *
 * Входной текст: Здравствуй, мир!
 * Словарь: mapOf('з' to "zZ", 'р' to "r", 'д' to "d", 'й' to "y", 'М' to "m", 'и' to "YY", '!' to "!!!")
 *
 * заменяется на
 *
 * Выходной текст: Zzdrавствуy, mир!!!
 *
 * Обратите внимание: данная функция не имеет возвращаемого значения
 */
fun transliterate(inputName: String, dictionary: Map<Char, String>, outputName: String) {
    val inputFile = File(inputName).readText()
    val outputFile = File(outputName).bufferedWriter()
    val dictionaryCopy = mutableMapOf<Char, String>()
    for ((key, value) in dictionary) {
        dictionaryCopy[key.toLowerCase()] = value.toLowerCase()
    }
    for (letter in inputFile) {
        val toAppend = dictionaryCopy[letter.toLowerCase()] ?: letter.toString()
        if (letter.isUpperCase()) {
            outputFile.append(toAppend.capitalize())
        } else {
            outputFile.append(toAppend)
        }
    }
    outputFile.close()
}

/**
 * Средняя
 *
 * Во входном файле с именем inputName имеется словарь с одним словом в каждой строчке.
 * Выбрать из данного словаря наиболее длинное слово,
 * в котором все буквы разные, например: Неряшливость, Четырёхдюймовка.
 * Вывести его в выходной файл с именем outputName.
 * Если во входном файле имеется несколько слов с одинаковой длиной, в которых все буквы разные,
 * в выходной файл следует вывести их все через запятую.
 * Регистр букв игнорировать, то есть буквы е и Е считать одинаковыми.
 *
 * Пример входного файла:
 * Карминовый
 * Боязливый
 * Некрасивый
 * Остроумный
 * БелогЛазый
 * ФиолетОвый

 * Соответствующий выходной файл:
 * Карминовый, Некрасивый
 *
 * Обратите внимание: данная функция не имеет возвращаемого значения
 */
fun chooseLongestChaoticWord(inputName: String, outputName: String) {
    val inputFile = File(inputName).readLines()
    val outputFile = File(outputName).bufferedWriter()
    val wordsWithDifferentLetters = mutableListOf<String>()
    var maximumLength = 0
    for (line in inputFile) {
        if (line.toLowerCase().toSet().size == line.length) {
            wordsWithDifferentLetters.add(line)
            if (line.length > maximumLength) {
                maximumLength = line.length
            }
        }
    }
    val strToApp = StringBuilder()
    for (element in wordsWithDifferentLetters) {
        if (element.length == maximumLength) {
            strToApp.append("$element, ")
        }
    }
    outputFile.append(strToApp.toString().removeSuffix(", "))
    outputFile.close()
}

/**
 * Сложная
 *
 * Реализовать транслитерацию текста в заданном формате разметки в формат разметки HTML.
 *
 * Во входном файле с именем inputName содержится текст, содержащий в себе элементы текстовой разметки следующих типов:
 * - *текст в курсивном начертании* -- курсив
 * - **текст в полужирном начертании** -- полужирный
 * - ~~зачёркнутый текст~~ -- зачёркивание
 *
 * Следует вывести в выходной файл этот же текст в формате HTML:
 * - <i>текст в курсивном начертании</i>
 * - <b>текст в полужирном начертании</b>
 * - <s>зачёркнутый текст</s>
 *
 * Кроме того, все абзацы исходного текста, отделённые друг от друга пустыми строками, следует обернуть в теги <p>...</p>,
 * а весь текст целиком в теги <html><body>...</body></html>.
 *
 * Все остальные части исходного текста должны остаться неизменными с точностью до наборов пробелов и переносов строк.
 * Отдельно следует заметить, что открывающая последовательность из трёх звёздочек (***) должна трактоваться как "<b><i>"
 * и никак иначе.
 *
 * При решении этой и двух следующих задач полезно прочитать статью Википедии "Стек".
 *
 * Пример входного файла:
Lorem ipsum *dolor sit amet*, consectetur **adipiscing** elit.
Vestibulum lobortis, ~~Est vehicula rutrum *suscipit*~~, ipsum ~~lib~~ero *placerat **tortor***,

Suspendisse ~~et elit in enim tempus iaculis~~.
 *
 * Соответствующий выходной файл:
<html>
<body>
<p>
Lorem ipsum <i>dolor sit amet</i>, consectetur <b>adipiscing</b> elit.
Vestibulum lobortis. <s>Est vehicula rutrum <i>suscipit</i></s>, ipsum <s>lib</s>ero <i>placerat <b>tortor</b></i>.
</p>
<p>
Suspendisse <s>et elit in enim tempus iaculis</s>.
</p>
</body>
</html>
 *
 * (Отступы и переносы строк в примере добавлены для наглядности, при решении задачи их реализовывать не обязательно)
 */

fun markdownToHtmlSimple(inputName: String, outputName: String) {
    val outputFile = File(outputName).bufferedWriter()
    val inputFile = File(inputName).readLines()
    val alreadyIn = mutableSetOf<String>()
    var closeP = true
    outputFile.write("<html><body><p>")
    for (line in inputFile) {
        println(line)
        if ((line.isEmpty()) && !closeP) {
            outputFile.write("<p>")
            closeP = true
        } else if ((line.isEmpty()) && closeP) {
            outputFile.write("</p>")
            outputFile.write("<p>")
            closeP = false
        } else if (line.isNotEmpty()) {
            val toAdd = StringBuilder()
            val subs = Regex("""([^~*]+)|([~*]+)""").findAll(line)
            for (element in subs) {
                if (Regex("""[~*]+""").matches(element.value)) {
                    if ((element.value == "*") && ("*" !in alreadyIn)) {
                        toAdd.append("<i>")
                        alreadyIn.add("*")
                    } else if ((element.value == "*") && ("*" in alreadyIn)) {
                        toAdd.append("</i>")
                        alreadyIn.remove("*")
                    }

                    if ((element.value == "**") && ("**" !in alreadyIn)) {
                        toAdd.append("<b>")
                        alreadyIn.add("**")
                    } else if ((element.value == "**") && ("**" in alreadyIn)) {
                        toAdd.append("</b>")
                        alreadyIn.remove("**")
                    }

                    if ((element.value == "~~") && ("~~" !in alreadyIn)) {
                        toAdd.append("<s>")
                        alreadyIn.add("~~")
                    } else if ((element.value == "~~") && ("~~" in alreadyIn)) {
                        toAdd.append("</s>")
                        alreadyIn.remove("~~")
                    }

                    if ((element.value == "***") && ("**" !in alreadyIn) && ("*" !in alreadyIn)) {
                        toAdd.append("<b><i>")
                        alreadyIn.add("**")
                        alreadyIn.add("*")
                    } else if ((element.value == "***") && ("**" in alreadyIn) && ("*" !in alreadyIn)) {
                        toAdd.append("</b><i>")
                        alreadyIn.remove("**")
                        alreadyIn.add("*")
                    } else if ((element.value == "***") && ("**" !in alreadyIn) && ("*" in alreadyIn)) {
                        toAdd.append("<b></i>")
                        alreadyIn.add("**")
                        alreadyIn.remove("*")
                    } else if ((element.value == "***") && ("**" in alreadyIn) && ("*" in alreadyIn)) {
                        toAdd.append("</b></i>")
                        alreadyIn.remove("**")
                        alreadyIn.remove("*")
                    }

                    if ((element.value == "~~*") && ("~~" in alreadyIn) && ("*" in alreadyIn)) {
                        toAdd.append("</s></i>")
                        alreadyIn.remove("~~")
                        alreadyIn.remove("*")
                    } else if ((element.value == "~~*") && ("~~" in alreadyIn) && ("*" !in alreadyIn)) {
                        toAdd.append("</s><i>")
                        alreadyIn.remove("~~")
                        alreadyIn.add("*")
                    } else if ((element.value == "~~*") && ("~~" !in alreadyIn) && ("*" in alreadyIn)) {
                        toAdd.append("<s></i>")
                        alreadyIn.add("~~")
                        alreadyIn.remove("*")
                    } else if ((element.value == "~~*") && ("~~" !in alreadyIn) && ("*" !in alreadyIn)) {
                        toAdd.append("<s><i>")
                        alreadyIn.add("~~")
                        alreadyIn.add("*")
                    }

                    if ((element.value == "*~~") && ("~~" in alreadyIn) && ("*" in alreadyIn)) {
                        toAdd.append("</i></s>")
                        alreadyIn.remove("~~")
                        alreadyIn.remove("*")
                    } else if ((element.value == "*~~") && ("~~" in alreadyIn) && ("*" !in alreadyIn)) {
                        toAdd.append("<i></s>")
                        alreadyIn.remove("~~")
                        alreadyIn.add("*")
                    } else if ((element.value == "*~~") && ("~~" !in alreadyIn) && ("*" in alreadyIn)) {
                        toAdd.append("</i><s>")
                        alreadyIn.add("~~")
                        alreadyIn.remove("*")
                    } else if ((element.value == "*~~") && ("~~" !in alreadyIn) && ("*" !in alreadyIn)) {
                        toAdd.append("<i><s>")
                        alreadyIn.add("~~")
                        alreadyIn.add("*")
                    }
                } else {
                    toAdd.append(element.value)
                }
            }
            outputFile.write(toAdd.toString())
        }
    }
    outputFile.write("</p></body></html>")
    outputFile.close()
}
/**
 * Сложная
 *
 * Реализовать транслитерацию текста в заданном формате разметки в формат разметки HTML.
 *
 * Во входном файле с именем inputName содержится текст, содержащий в себе набор вложенных друг в друга списков.
 * Списки бывают двух типов: нумерованные и ненумерованные.
 *
 * Каждый элемент ненумерованного списка начинается с новой строки и символа '*', каждый элемент нумерованного списка --
 * с новой строки, числа и точки. Каждый элемент вложенного списка начинается с отступа из пробелов, на 4 пробела большего,
 * чем список-родитель. Максимально глубина вложенности списков может достигать 6. "Верхние" списки файла начинются
 * прямо с начала строки.
 *
 * Следует вывести этот же текст в выходной файл в формате HTML:
 * Нумерованный список:
 * <ol>
 *     <li>Раз</li>
 *     <li>Два</li>
 *     <li>Три</li>
 * </ol>
 *
 * Ненумерованный список:
 * <ul>
 *     <li>Раз</li>
 *     <li>Два</li>
 *     <li>Три</li>
 * </ul>
 *
 * Кроме того, весь текст целиком следует обернуть в теги <html><body>...</body></html>
 *
 * Все остальные части исходного текста должны остаться неизменными с точностью до наборов пробелов и переносов строк.
 *
 * Пример входного файла:
///////////////////////////////начало файла/////////////////////////////////////////////////////////////////////////////
 * Утка по-пекински
 * Утка
 * Соус
 * Салат Оливье
1. Мясо
 * Или колбаса
2. Майонез
3. Картофель
4. Что-то там ещё
 * Помидоры
 * Фрукты
1. Бананы
23. Яблоки
1. Красные
2. Зелёные
///////////////////////////////конец файла//////////////////////////////////////////////////////////////////////////////
 *
 *
 * Соответствующий выходной файл:
///////////////////////////////начало файла/////////////////////////////////////////////////////////////////////////////
<html>
<body>
<ul>
<li>
Утка по-пекински
<ul>
<li>Утка</li>
<li>Соус</li>
</ul>
</li>
<li>
Салат Оливье
<ol>
<li>Мясо
<ul>
<li>
Или колбаса
</li>
</ul>
</li>
<li>Майонез</li>
<li>Картофель</li>
<li>Что-то там ещё</li>
</ol>
</li>
<li>Помидоры</li>
<li>
Фрукты
<ol>
<li>Бананы</li>
<li>
Яблоки
<ol>
<li>Красные</li>
<li>Зелёные</li>
</ol>
</li>
</ol>
</li>
</ul>
</body>
</html>
///////////////////////////////конец файла//////////////////////////////////////////////////////////////////////////////
 * (Отступы и переносы строк в примере добавлены для наглядности, при решении задачи их реализовывать не обязательно)
 */
fun markdownToHtmlLists(inputName: String, outputName: String) {
    TODO()
}

/**
 * Очень сложная
 *
 * Реализовать преобразования из двух предыдущих задач одновременно над одним и тем же файлом.
 * Следует помнить, что:
 * - Списки, отделённые друг от друга пустой строкой, являются разными и должны оказаться в разных параграфах выходного файла.
 *
 */
fun markdownToHtml(inputName: String, outputName: String) {
    TODO()
}

/**
 * Средняя
 *
 * Вывести в выходной файл процесс умножения столбиком числа lhv (> 0) на число rhv (> 0).
 *
 * Пример (для lhv == 19935, rhv == 111):
19935
 *    111
--------
19935
+ 19935
+19935
--------
2212785
 * Используемые пробелы, отступы и дефисы должны в точности соответствовать примеру.
 * Нули в множителе обрабатывать так же, как и остальные цифры:
235
 *  10
-----
0
+235
-----
2350
 *
 */
fun printMultiplicationProcess(lhv: Int, rhv: Int, outputName: String) {
    TODO()
}


/**
 * Сложная
 *
 * Вывести в выходной файл процесс деления столбиком числа lhv (> 0) на число rhv (> 0).
 *
 * Пример (для lhv == 19935, rhv == 22):
19935 | 22
-198     906
----
13
-0
--
135
-132
----
3

 * Используемые пробелы, отступы и дефисы должны в точности соответствовать примеру.
 *
 */
fun printDivisionProcess(lhv: Int, rhv: Int, outputName: String) {
    TODO()
}

