package pt.isel

import kotlin.test.Test
import kotlin.test.assertEquals



/*
 * Located on testes resources
 */
private const val weatherPath = "q-Lisbon_format-csv_date-2020-05-08_enddate-2020-06-11.csv"

class TestWeatherTemperatures {
    fun loadNaiveCsv(): List<Weather> =
        ClassLoader
            .getSystemResource(weatherPath)  // URL
            .openStream()   //Stream
            .reader()       // Reader
            .readLines()    // List<String>
            .filter { !it.startsWith('#') } // List<String> Filter comments
            .drop(1) // List<String> - Skip line: Not available
            .filterIndexed { index, _ -> index % 2 != 0 } // Filter hourly info
            .map { it.fromCsvToWeather() } // List<Weather>

    private val weatherData = loadNaiveCsv()

    @Test
    fun `check data`() {
        weatherData
            .forEach { println(it) }
    }

    @Test
    fun `count distinct descriptions in rainy days map filter`() {
        val size =
            weatherData
                .map { it.weatherDesc }
                .filter { it.lowercase().contains("rain") }
                .take(2)
                .distinct()
                .count()
        assertEquals(5, size)
    }
}
