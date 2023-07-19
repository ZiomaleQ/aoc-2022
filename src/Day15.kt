fun main() {
  fun part1(input: List<String>, yAxis: Int): Int {
    val points = parseInput(input)
    
    var maxX = Int.MIN_VALUE
    var minX = Int.MAX_VALUE
    
    for ((sensor, beacon) in points) {
      val dist = sensor.distance(beacon)
      val distanceToY = dist - kotlin.math.abs(yAxis - sensor.y)
      
      if (distanceToY < 0) continue
      
      val middle = sensor.x
      
      maxX = maxOf(maxX, middle + dist)
      minX = minOf(minX, middle - dist)
    }
    
    return (minX..maxX).count {
      points.any { pnt ->
        Cords(it, yAxis).let { cord ->
          cord.inRadius(pnt) && pnt.second != cord
        }
      }
    }
  }
  
  fun part2(input: List<String>, max: Int): Long {
    val points = parseInput(input)
    
    val out = (0..max).mapNotNull {
      val ranges = getRowRanges(it, points)
      if (ranges.size < 2) null else Pair(it, ranges)
    }
    
    return out.first().let { (it.second.first().last + 1) * 4_000_000L + it.first.toLong() }
  }
  
  // test if implementation meets criteria from the description, like:
  val testInput = readInput("Day15_test")
  part1(testInput, 10).also { println(it); check(it == 26) }
  
  val input = readInput("Day15")
  println(part1(input, 2000000))
  
  part2(testInput, 20).also { println(it); check(it == 56000011L) }
  println(part2(input, 4_000_000))
}

private fun parseInput(input: List<String>): List<Pair<Cords, Cords>> = input.map {
  val sensorX = it.substring(it.indexOf("x=") + 2, it.indexOf(",")).trim().toInt()
  val sensorY = it.substring(it.indexOf("y=") + 2, it.indexOf(":")).trim().toInt()
  
  val nextCords = it.substring(it.indexOf("is at") + "is at".length)
  
  val beaconX = nextCords.substring(nextCords.indexOf("x=") + 2, nextCords.indexOf(",")).trim().toInt()
  val beaconY = nextCords.substring(nextCords.indexOf("y=") + 2).trim().toInt()
  
  Pair(Cords(sensorX, sensorY), Cords(beaconX, beaconY))
}

private fun Cords.inRadius(radius: Pair<Cords, Cords>): Boolean =
  radius.first.distance(this) <= radius.first.distance(radius.second)

private fun Cords.distance(other: Cords): Int =
  kotlin.math.abs(x - other.x) + kotlin.math.abs(y - other.y)

private fun getRowRanges(y: Int, list: List<Pair<Cords, Cords>>): MutableList<IntRange> {
  val ranges = list.mapNotNull {
    val dist = it.first.distance(it.second)
    val offset = dist - kotlin.math.abs(it.first.y - y)
    if (offset < 0) null
    else ((it.first.x - offset)..(it.first.x + offset))
  }.sortedBy { it.first }.toMutableList()
  
  val tempOut = mutableListOf(ranges.removeFirst())
  
  for (next in ranges) {
    val lastIdx = tempOut.lastIndex
    val last = tempOut.last()
    
    if (next.first <= last.last || (last.last + 1) == next.first) {
      if (next.last > last.last) {
        tempOut[lastIdx] = (last.first..next.last)
      }
    } else {
      tempOut.add(next)
    }
  }
  
  return tempOut
}