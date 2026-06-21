
***

## **Objectives**

- practice building collection pipelines
- understand the difference between eager and lazy processing
- implement custom collection operations in both eager and lazy styles

## Table of Contents

1. [Setup](#0-setup)
2. [Practice building collection pipelines](#1-practice-building-collection-pipelines)
3. [Eager Processing](#2-eager-processing)
4. [Lazy Processing](#3-lazy-processing)

## 0. Setup

Include the contents of this folder in your repository as a new module of your
Gradle multi-module project.
You can do this by copying the `lesson7-lab-guide` folder into the root of your
repository, **renaming it according to your preference**, and then adding the
right `include` statement to your `settings.gradle` file.
Confirm that everything compiles.
You may have some tests failing with the error: `kotlin.NotImplementedError: An operation is not implemented.`

***

## 1. Practice building collection pipelines

A collection pipeline, as defined in Martin Fowler’s renowned article [Fowler, 2015](https://martinfowler.com/articles/collection-pipeline/):

> _Collection pipelines are a programming pattern where you organize some
> computation as a sequence of operations which compose by taking a collection
> as output of one operation and feeding it into the next. (Common operations
> are filter, map, and reduce.)_

In `TestWeatherTemperatures.kt`, observe the `loadNaiveCsv()` function, shown
below, and how it builds a processing pipeline over a collection of lines read
from a CSV file:

```kotlin
ClassLoader
    .getSystemResource(weatherPath)
    .openStream()
    .reader()
    .readLines()                    // List<String>
    .filter { !it.startsWith('#') } // Remove comment lines
    .drop(1)                        // Skip header or unavailable line
    .filterIndexed { index, _ -> index % 2 != 0 } // Keep only hourly entries
    .map { it.fromCsvToWeather() } // Convert each row into a Weather object
```

This demonstrates how data is progressively transformed through a sequence of
operations, moving from raw text input to a structured `List<Weather>`,
illustrating the concept of a collection pipeline as described by Fowler.

With the help of IntelliJ, you can step into the implementation of methods such
as `filter`, `map`, or `drop` and observe how each of them creates a new
collection containing the resulting items, **without modifying the original
collection**.

***

### 2. Eager Processing

1. Copy paste the next test and confirm it runs successfully.  
**NOTE** how this pipeline reproduces the idea of an SQL query. The map operation
selects the weather descriptions, similarly to a SELECT clause, the filter
operation works like a WHERE condition that keeps only descriptions containing
"rain", distinct removes duplicates like DISTINCT in SQL, and count computes the
final number of results.

```kotlin
@Test
fun `count distinct descriptions in rainy days map filter`() {
    val size =
        weatherData
            .map { it.weatherDesc }
            .filter { it.lowercase().contains("rain") }
            .distinct()
            .count()
    assertEquals(5, size)
}
```

This pipeline is "conceptually similar" to the following SQL query, where
`weatherData` and `weatherDesc` instead of being a table and a column, are a
collection and a property:

```sql
SELECT COUNT(DISTINCT weatherDesc)
FROM weatherData
WHERE LOWER(weatherDesc) LIKE '%rain%';
```

2. Copy and paste the previous test into a new one named `count distinct
   descriptions in rainy days map filter Custom`, and replace the call to
   `map()` and `distinct()` with `eagerMap()` and `eagerDistinct()`, which is
   already defined in the `Queries.kt` file. Confirm that it runs successfully.

3. Implement the `eagerFilter()` function in `Queries.kt` and replace the call
   to `filter()` with it. Confirm that it runs successfully.

4. Modify your test with the following code and observe the value of `iters`
   after the execution of the pipeline:

```kotlin
var iters = 0
val size =
    weatherData
        .eagerMap {
            iters++
            it.weatherDesc
        }.eagerFilter {
            iters++
            it.lowercase().contains("rain")
        }.eagerDistinct()
        .count()
```

5. Make the same modification in the `count distinct descriptions in rainy days map filter`
   test and observe the value of `iters` after the execution of the pipeline is
   the same as in the previous test.

6. Copy paste the previous two tests with the new names: 
  * `count distinct descriptions in rainy days filter map`
  * `count distinct descriptions in rainy days filter map Custom`

7. In both tests of 5. exchange the order between the `map` and `filter`
   operations in the pipeline. Confirm that they run successfully and observe
   the value of `iters`. What differences do you notice? Explain why?

***

### 3. Lazy Processing

1. Copy paste the next test and confirm it runs successfully.

```kotlin
    @Test
    fun `first description in windy days`() {
        var iters = 0
        val desc =
            weatherData
                .filter { // List<Weather>
                    iters++
                    it.windspeedKmph > 22
                }.map { // List<String>
                    iters++
                    it.weatherDesc
                }.first()
        assertEquals("Light rain shower", desc)
        assertEquals(37, iters)
    }
```

2. Copy and paste the previous test into a new one named `first description in windy days Lazy`.
   Interleave a call to `asSequence()` between the `weatherData` and the first
   `filter()`, such as:
```kotlin
        ...
            weatherData
                .asSequence()
                .filter { // List<Weather>
                ...
```

3. Observe the value of `iters` after the execution of the pipeline. What differences
   do you notice?

4. Navigate to the implementation of `filter` and `map` and observe how they are
   implemented differently for a `Sequence`, rather than for an `Iterable`.

5. In both tests 1. and 2, add a `println("Filtering $it")` statement inside the
   `filter` lambda and a `println("Mapping $it")` statement inside the `map`
   lambda. Run both tests and observer how the print statements are
   **interleaved** in the second test, while they are **grouped** together in
   the first test.  
   Reference [Sequence Processing](https://kotlinlang.org/docs/sequences.html#sequence-processing-example)

6. Copy paste the previous test into a new one named `first description in windy days Lazy Custom`.
   Replace the call to `filter()` with `lazyFilter()`, and confirm that it runs successfully with the same behavior as the previous test.

7. Implement the `lazyMap()` function in `Queries.kt` and replace the call to
   `map()` with it in the previous test of 6. Confirm that it runs successfully
   and keeps the same behavior.

8. Create a new test named `count distinct descriptions in rainy days filter map Custom Lazy`
   based on the `count distinct descriptions in rainy days filter map Custom`
   test, but interleaving `asSequence()` at the beginning of the pipeline and
   replacing the calls to `eagerFilter()`, `eagerMap()`, and `eagerDistinct()`
   with their lazy counterparts.  
   **NOTE**: you must implement the `lazyDistinct()` function in `Queries.kt`. Before that,
   you can use the `distinct()` function to check that your pipeline is correct.

9. Implement the rest of methods in `Queries` namely `lazyConcat`, `lazyCollapse`
   and `lazyZip` according to the specification verified in the tests of
   `TestQueries.kt`. Confirm that all tests run successfully.

