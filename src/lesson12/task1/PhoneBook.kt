@file:Suppress("UNUSED_PARAMETER")

package lesson12.task1

/**
 * Класс "Телефонная книга".
 *
 * Общая сложность задания -- средняя.
 * Объект класса хранит список людей и номеров их телефонов,
 * при чём у каждого человека может быть более одного номера телефона.
 * Человек задаётся строкой вида "Фамилия Имя".
 * Телефон задаётся строкой из цифр, +, *, #, -.
 * Поддерживаемые методы: добавление / удаление человека,
 * добавление / удаление телефона для заданного человека,
 * поиск номера(ов) телефона по заданному имени человека,
 * поиск человека по заданному номеру телефона.
 *
 * Класс должен иметь конструктор по умолчанию (без параметров).
 */

class PhoneBook {
    val dataBase = mutableMapOf<String, Set<String>>()
    /**
     * Добавить человека.
     * Возвращает true, если человек был успешно добавлен,
     * и false, если человек с таким именем уже был в телефонной книге
     * (во втором случае телефонная книга не должна меняться).
     */
    fun addHuman(name: String): Boolean {
        require(name.matches(Regex("""([A-ZА-ЯЁ][a-zа-яё]*) [A-ZА-ЯЁ][a-zа-я]*""")))
        if (name in dataBase) {
            return false
        }
        dataBase[name] = mutableSetOf()
        return true
    }

    /**
     * Убрать человека.
     * Возвращает true, если человек был успешно удалён,
     * и false, если человек с таким именем отсутствовал в телефонной книге
     * (во втором случае телефонная книга не должна меняться).
     */
    fun removeHuman(name: String): Boolean {
        require(name.matches(Regex("""([A-ZА-ЯЁ][a-zа-яё]*) [A-ZА-ЯЁ][a-zа-я]*""")))
        if (name in dataBase) {
            dataBase.remove(name)
            return true
        }
        return false
    }

    /**
     * Добавить номер телефона.
     * Возвращает true, если номер был успешно добавлен,
     * и false, если человек с таким именем отсутствовал в телефонной книге,
     * либо у него уже был такой номер телефона,
     * либо такой номер телефона зарегистрирован за другим человеком.
     */
    fun addPhone(name: String, phone: String): Boolean {
        if (phone.contains(Regex("""[^\d+*#\-]""")) || dataBase.values.any { phone in it }) return false
        val who = dataBase[name]
        if (who != null) {
            dataBase[name] = who + phone
            return true
        }
        return false
    }

    /**
     * Убрать номер телефона.
     * Возвращает true, если номер был успешно удалён,
     * и false, если человек с таким именем отсутствовал в телефонной книге
     * либо у него не было такого номера телефона.
     */
    fun removePhone(name: String, phone: String): Boolean {
        if (phone.contains(Regex("""[^\d+*#\-]"""))) return false
        val who = dataBase[name]
        if ((who != null) && (phone in who)) {
            dataBase[name] = who - name
            return true
        }
        return false
    }

    /**
     * Вернуть все номера телефона заданного человека.
     * Если этого человека нет в книге, вернуть пустой список
     */
    fun phones(name: String): Set<String> {
        require(name.matches(Regex("""([A-ZА-ЯЁ][a-zа-яё]*) [A-ZА-ЯЁ][a-zа-я]*""")))
        val phoneNumbers = dataBase[name]
        if (phoneNumbers != null) {
            return phoneNumbers
        }
        return setOf()
    }

    /**
     * Вернуть имя человека по заданному номеру телефона.
     * Если такого номера нет в книге, вернуть null.
     */
    fun humanByPhone(phone: String): String? {
        if (phone.contains(Regex("""[^\d+*#\-]"""))) {
            return null
        }
        for ((key, value) in dataBase) {
            if (phone in value) {
                return key
            }
        }
        return null
    }

    /**
     * Две телефонные книги равны, если в них хранится одинаковый набор людей,
     * и каждому человеку соответствует одинаковый набор телефонов.
     * Порядок людей / порядок телефонов в книге не должен иметь значения.
     */
    override fun equals(other: Any?): Boolean {
        if (other is PhoneBook) {
            return this.dataBase == other.dataBase
        }
        return false
    }

    override fun hashCode(): Int = dataBase.hashCode()
}