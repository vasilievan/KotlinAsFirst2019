@file:Suppress("UNUSED_PARAMETER")

package lesson8.task1

import lesson1.task1.sqr
import kotlin.math.*

/**
 * Точка на плоскости
 */
data class Point(val x: Double, val y: Double) {
    /**
     * Пример
     *
     * Рассчитать (по известной формуле) расстояние между двумя точками
     */
    fun distance(other: Point): Double = sqrt(sqr(x - other.x) + sqr(y - other.y))
}

/**
 * Треугольник, заданный тремя точками (a, b, c, см. constructor ниже).
 * Эти три точки хранятся в множестве points, их порядок не имеет значения.
 */
@Suppress("MemberVisibilityCanBePrivate")
class Triangle private constructor(private val points: Set<Point>) {

    private val pointList = points.toList()

    val a: Point get() = pointList[0]

    val b: Point get() = pointList[1]

    val c: Point get() = pointList[2]

    constructor(a: Point, b: Point, c: Point) : this(linkedSetOf(a, b, c))

    /**
     * Пример: полупериметр
     */
    fun halfPerimeter() = (a.distance(b) + b.distance(c) + c.distance(a)) / 2.0

    /**
     * Пример: площадь
     */
    fun area(): Double {
        val p = halfPerimeter()
        return sqrt(p * (p - a.distance(b)) * (p - b.distance(c)) * (p - c.distance(a)))
    }

    /**
     * Пример: треугольник содержит точку
     */
    fun contains(p: Point): Boolean {
        val abp = Triangle(a, b, p)
        val bcp = Triangle(b, c, p)
        val cap = Triangle(c, a, p)
        return abp.area() + bcp.area() + cap.area() <= area()
    }

    override fun equals(other: Any?) = other is Triangle && points == other.points

    override fun hashCode() = points.hashCode()

    override fun toString() = "Triangle(a = $a, b = $b, c = $c)"
}

/**
 * Окружность с заданным центром и радиусом
 */
data class Circle(val center: Point, val radius: Double) {
    /**
     * Простая
     *
     * Рассчитать расстояние между двумя окружностями.
     * Расстояние между непересекающимися окружностями рассчитывается как
     * расстояние между их центрами минус сумма их радиусов.
     * Расстояние между пересекающимися окружностями считать равным 0.0.
     */
    fun distance(other: Circle): Double {
        return if (this.center.distance(other.center) - (this.radius + other.radius) > 0) {
            this.center.distance(other.center) - (this.radius + other.radius)
        } else {
            0.0
        }
    }

    /**
     * Тривиальная
     *
     * Вернуть true, если и только если окружность содержит данную точку НА себе или ВНУТРИ себя
     */
    fun contains(p: Point): Boolean {
        return center.distance(p) <= radius
    }
}

/**
 * Отрезок между двумя точками
 */
data class Segment(val begin: Point, val end: Point) {
    override fun equals(other: Any?) =
        other is Segment && (begin == other.begin && end == other.end || end == other.begin && begin == other.end)

    override fun hashCode() =
        begin.hashCode() + end.hashCode()
}

/**
 * Средняя
 *
 * Дано множество точек. Вернуть отрезок, соединяющий две наиболее удалённые из них.
 * Если в множестве менее двух точек, бросить IllegalArgumentException
 */
fun diameter(vararg points: Point): Segment {
    val setOfPoint = points.toSet()
    var current = Segment(Point(0.0, 0.0), Point(0.0, 0.0))
    for (point in setOfPoint) {
        for (another in setOfPoint) {
            if ((point.hashCode() != another.hashCode()) && (point.distance(another) > current.begin.distance(current.end))) {
                current = Segment(point, another)
            }
        }
    }
    return current
}

/**
 * Простая
 *
 * Построить окружность по её диаметру, заданному двумя точками
 * Центр её должен находиться посередине между точками, а радиус составлять половину расстояния между ними
 */
fun circleByDiameter(diameter: Segment): Circle {
    return Circle(
        Point((diameter.begin.x + diameter.end.x) / 2, (diameter.begin.y + diameter.end.y) / 2),
        diameter.begin.distance(diameter.end) / 2
    )
}

/**
 * Прямая, заданная точкой point и углом наклона angle (в радианах) по отношению к оси X.
 * Уравнение прямой: (y - point.y) * cos(angle) = (x - point.x) * sin(angle)
 * или: y * cos(angle) = x * sin(angle) + b, где b = point.y * cos(angle) - point.x * sin(angle).
 * Угол наклона обязан находиться в диапазоне от 0 (включительно) до PI (исключительно).
 */
class Line private constructor(val b: Double, val angle: Double) {
    init {
        require(angle >= 0 && angle < PI) { "Incorrect line angle: $angle" }
    }

    constructor(point: Point, angle: Double) : this(point.y * cos(angle) - point.x * sin(angle), angle)

    /**
     * Средняя
     *
     * Найти точку пересечения с другой линией.
     * Для этого необходимо составить и решить систему из двух уравнений (каждое для своей прямой)
     */

    fun crossPoint(other: Line): Point {
        val x = (b * cos(other.angle) - other.b * cos(angle)) / sin(other.angle - angle)
        return if (angle != PI / 2) {
            Point(x, (b + x * sin(angle)) / cos(angle))
        } else {
            Point(x, (other.b + x * sin(other.angle)) / cos(other.angle))
        }
    }

    override fun equals(other: Any?) = other is Line && angle == other.angle && b == other.b

    override fun hashCode(): Int {
        var result = b.hashCode()
        result = 31 * result + angle.hashCode()
        return result
    }

    override fun toString() = "Line(${cos(angle)} * y = ${sin(angle)} * x + $b)"
}

/**
 * Средняя
 *
 * Построить прямую по отрезку
 */
fun lineBySegment(s: Segment): Line = lineByPoints(s.begin, s.end)

/**
 * Средняя
 *
 * Построить прямую по двум точкам
 */
fun lineByPoints(a: Point, b: Point): Line {
    val angle = (atan((b.y - a.y) / (b.x - a.x)) + 2 * PI) % PI
    return Line(a, angle)
}

/**
 * Сложная
 *
 * Построить серединный перпендикуляр по отрезку или по двум точкам
 */
fun bisectorByPoints(a: Point, b: Point): Line {
    val midPoint = Point((a.x + b.x) / 2, (a.y + b.y) / 2)
    val angle = (atan((a.y - b.y) / (a.x - b.x)) + PI / 2) % PI
    return Line(midPoint, angle)
}

/**
 * Средняя
 *
 * Задан список из n окружностей на плоскости. Найти пару наименее удалённых из них.
 * Если в списке менее двух окружностей, бросить IllegalArgumentException
 */
fun findNearestCirclePair(vararg circles: Circle): Pair<Circle, Circle> {
    val circlesSet = circles.toSet()
    var distance = Double.POSITIVE_INFINITY
    if (circlesSet.size < 2) {
        throw IllegalArgumentException()
    }
    var nearest = Pair(circles[0], circles[1])
    for (item in circlesSet) {
        for (anotherItem in circlesSet) {
            val current = item.distance(anotherItem)
            if ((item.hashCode() != anotherItem.hashCode()) && (current < distance)) {
                distance = current
                nearest = Pair(item, anotherItem)
            }
        }
    }
    return nearest
}

/**
 * Сложная
 *
 * Дано три различные точки. Построить окружность, проходящую через них
 * (все три точки должны лежать НА, а не ВНУТРИ, окружности).
 * Описание алгоритмов см. в Интернете
 * (построить окружность по трём точкам, или
 * построить окружность, описанную вокруг треугольника - эквивалентная задача).
 */
// центр описанной окружности - точка пересечения срединных перпендикуляров
fun circleByThreePoints(a: Point, b: Point, c: Point): Circle = Circle(
    bisectorByPoints(a, c).crossPoint(bisectorByPoints(b, c)),
    (bisectorByPoints(a, c).crossPoint(bisectorByPoints(b, c))).distance(a)
)

/**
 * Очень сложная
 *
 * Дано множество точек на плоскости. Найти круг минимального радиуса,
 * содержащий все эти точки. Если множество пустое, бросить IllegalArgumentException.
 * Если множество содержит одну точку, вернуть круг нулевого радиуса с центром в данной точке.
 *
 * Примечание: в зависимости от ситуации, такая окружность может либо проходить через какие-либо
 * три точки данного множества, либо иметь своим диаметром отрезок,
 * соединяющий две самые удалённые точки в данном множестве.
 */
fun minContainingCircle(vararg points: Point): Circle {
    val pointsSet = points.toSet()
    var distance = 0.0
    if (pointsSet.isEmpty()) {
        throw IllegalArgumentException()
    } else if (pointsSet.size == 1) {
        return Circle(points[0], 0.0)
    }
    var seg = Segment(points[0], points[1])
    for (point in pointsSet) {
        for (anotherPoint in pointsSet) {
            if ((point.hashCode() != anotherPoint.hashCode()) && (point.distance(anotherPoint) > distance)) {
                distance = point.distance(anotherPoint)
                seg = Segment(point, anotherPoint)
            }
        }
    }
    var answer = circleByDiameter(seg)
    var indicator = true
    while (indicator) {
        indicator = false
        for (point in pointsSet) {
            if (answer.center.distance(point) > answer.radius) {
                answer = circleByThreePoints(seg.begin, seg.end, point)
                indicator = true
                break
            }
        }
    }
    return answer
}