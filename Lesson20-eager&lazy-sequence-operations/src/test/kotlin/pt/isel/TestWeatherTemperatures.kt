package pt.isel

import pt.isel.lae41n.eagerDistinct
import pt.isel.lae41n.eagerFilter
import pt.isel.lae41n.eagerMap
import kotlin.test.Test
import kotlin.test.assertEquals

/*
 * Located on testes resources
 */
private const val weatherPath = "q-Lisbon_format-csv_date-2020-05-08_enddate-2020-06-11.csv"

class TestWeatherTemperatures {
    fun loadNaiveCsv(): List<Weather> =
        ClassLoader
            .getSystemResource(weatherPath)
            .openStream()
            .reader()
            .readLines() // List<String>
            .filter { !it.startsWith('#') } // Filter comments
            .drop(1) // Skip line: Not available
            .filterIndexed { index, _ -> index % 2 != 0 } // Filter hourly info
            .map { it.fromCsvToWeather() } // List<Weather>

    private val weatherData = loadNaiveCsv()

    @Test
    fun `check data`() {
        weatherData
            .forEach { println(it) }
        println("WeatherData has ${weatherData.size} weather objects")
    }

    @Test
    fun `count distinct descriptions in rainy days map filter`() {
        var iters = 0
        val size =
            weatherData
                .eagerFilter {
                    iters++
                    it.weatherDesc.lowercase().contains("rain")
                }.eagerMap {
                    iters++
                    it.weatherDesc
                }
                .eagerDistinct()
                .count()
        assertEquals(5, size)
        println("iters = ${iters}")
    }

    @Test
    fun `first description in windy days`() {
        var iters = 0
        var itersFilter = 0
        var itersMap = 0
        val desc =
            weatherData.asSequence()
                .filter { // List<Weather>
                    iters++
                    itersFilter++
                    it.windspeedKmph > 22
                }.map { // List<String>
                    iters++
                    itersMap++
                    it.weatherDesc
                }.first()
        //assertEquals("Light rain shower", desc)
        println("iters = ${iters}")
        println("itersFiler = ${itersFilter}")
        println("itersMap = ${itersMap}")
        //assertEquals(37, iters)
    }
}
