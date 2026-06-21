# Laboratory Guide — Types Requiring Special Cleanup and Garbage Collection

## **Objectives**

* understand why some resources are not managed by GC
* practice using `Closeable` types safely
* observe how `use` expands into `try/finally`
* implement custom `Closeable` types
* observe the behavior and drawbacks of finalization
* experiment with the Java Cleaner API
* understand why Cleaner avoids some problems of finalization
* observe how captured references may prevent cleanup

***

# Table of Contents

1. [Setup](#1-setup)
2. [Using Closeable Types](#2-using-closeable-types)
3. [Implementing a Custom Closeable Type](#3-implementing-a-custom-closeable-type)
4. [Finalization](#4-finalization)
5. [Cleaner API](#5-cleaner-api)
6. [Captured References and Cleaner Pitfalls](#6-captured-references-and-cleaner-pitfalls)

***

# 1. Setup

Include the contents of this folder in your repository as a new module of your
Gradle multi-module project.
You can do this by copying the `lab8` folder into the root of your
repository, **renaming it according to your preference**, and then adding the
right `include` statement to your `settings.gradle` file.
Confirm that everything compiles.

The examples in this lab download temporary image files from:

```text
https://dev.java/assets/images/duke/duke_star7.png
```

***

# 2. Using Closeable Types

Some objects hold native resources that are **not managed by garbage collection**.
These resources must be **explicitly released**.
In the JVM ecosystem, this is typically done through `close()`.
Examples include:
* sockets
* files
* database connections
* etc


1. Create the following test, run it and confirm that the file is created, but
   nothing is written to it.

```kotlin
@Test
fun `write file`() {
    File("temp.txt").delete() // Ensure file does not exist before test
    val writer = FileWriter("temp.txt")
    writer.write("Hello JVM")
    assertTrue(File("temp.txt").exists())
}
```

2. Create the following test, run it and confirm that even though an exception
   occurs, the writer is properly closed and written content is flushed to the
   file.

```kotlin
@Test(expected = IllegalStateException::class)
    fun `write file using finally`() {
       File("temp.txt").delete() // Ensure file does not exist before test
        var writer: FileWriter? = null
        try {
            writer = FileWriter("temp.txt")
            writer.write("Hello JVM")
            // Force failure
            error("Simulated failure")
        } finally {
            println("Closing writer")
            writer?.close()
        }
    }
```

3. Create the following test, run it and confirm the same behavior as the
   previous test, despite the much **more concise code**.

```kotlin
@Test(expected = IllegalStateException::class)
fun `write file using use`() {
   File("temp2.txt").delete() // Ensure file does not exist before test
   FileWriter("temp2.txt").use { writer ->
      writer.write("Hello JVM 2")
      // Force failure
      error("Simulated failure")
   }
}
```

# 3. Implementing a Custom Closeable Type

Analyze the source code of the class `TempImage` provided in the starter
project. This class is a custom `Closeable` type that manages temporary image
files downloaded from the web.

`TempImage` is instantiated with a URL pointing to an image. The image is
downloaded only if it is not already present in the local filesystem; otherwise,
the existing file is reused.
The `close` method is responsible for releasing the associated native resource
by deleting the image file from the filesystem when it is invoked.
The class also exposes a `downloaded` property, which indicates whether the
image was downloaded during the creation of the instance or loaded from an
existing local file.

Internally, `TempImage` uses an `InputStream` obtained from the URL connection
to download the image data. Since `InputStream` is itself a `Closeable`
resource, it is managed using a try-with-resources block (or Kotlin’s `use`
function) to ensure it is properly closed after use. The `close` method does not
manage the stream; it is solely responsible for deleting the temporary file if
it exists.

1. Create the following test, run it and confirm that the file is downloaded and
   deleted as expected. Also confirm that the second image is not downloaded,
   since the file already exists in the first `use` block.

```kotlin
@Test
fun `using TempImage with use`() {
    File("duke_star7.png").delete() // Ensure file does not exist before test
    TempImage("https://dev.java/assets/images/duke/duke_star7.png")
        .use {
            assertTrue(it.downloaded)
            TempImage("https://dev.java/assets/images/duke/duke_star7.png")
                .use { second ->
                    assertFalse(second.downloaded)
                }
        }
    assertFalse(File("duke_star7.png").exists())
}
```

2. Why does the file no longer exist outside the `use` blocks?

3. Create the following test, run it and confirm that the file is downloaded but not
   deleted, since `close()` is never called. 

```kotlin
@Test
fun `using TempImage and forgetting to close`() {
   File("duke_star7.png").delete() // Ensure file does not exist before test
   TempImage("https://dev.java/assets/images/duke/duke_star7.png").also {
      assertTrue(it.downloaded)
   }
   TempImage("https://dev.java/assets/images/duke/duke_star7.png").also {
      assertFalse(it.downloaded)
   }
   assertTrue(File("duke_star7.png").exists())
}
```

# 4. Finalization

Finalization allows an object to execute code after it becomes unreachable.
However, finalization has several drawbacks:
* it requires additional GC work
* finalization runs on **another thread**
* memory reclamation requires at least two GC cycles
* finalization timing is unpredictable

1. Add finalization to `TempImage` by adding the following code to the class:

```kotlin
override fun close() {
    println("Try deleting file...")
    if(file.exists()) {
        file.delete()
    }
}

protected fun finalize() = close()
```

2. Ignore the deprecation warning for `finalize()` that reports "_Overrides
   method that is deprecated and marked for removal in 'java.lang.Object'_".
   This warning is expected because `finalize()` has been deprecated in Java 9
   and marked for removal in future versions. The `finalize()` method is no
   longer recommended for cleanup tasks due to its unpredictability and
   performance issues. In this lab, we are using it solely for educational
   purposes to demonstrate the concept of finalization and its drawbacks.
 

3. Create the following test, run it and confirm that the file is deleted
   through finalization after the object becomes unreachable. You may need to
   run the test several times to observe the behavior due to the
   non-deterministic nature of finalization.

```kotlin
@Test
fun `using TempImage and forgetting to close but file is deleted through finalization`() {
   fun loadImageAndForgetClose() {
      TempImage("https://dev.java/assets/images/duke/duke_star7.png")
            .also {
               // Not calling close !!!
               assertTrue(it.downloaded)
            }
   }
   File("duke_star7.png").delete() // Ensure file does not exist before test
   loadImageAndForgetClose()
   assertTrue(File("duke_star7.png").exists())
   /*
   * Once an object is eligible for GC, finalization may occur on a different thread,
   * so you might need to pause briefly to observe the changes.
   */
   System.gc()
   Thread.sleep(100)
   assertFalse(File("duke_star7.png").exists())
}
```

4. Why does the file still exist immediately after `loadImageAndForgetClose()`?

5. Why is `Thread.sleep(100)` necessary to observe the effect of finalization?

6. Create the following test, run it and confirm now that the file is deleted
   immediately after `loadImageWithUse()`, since `use` is used to manage the
   resource.
   Still observe the console output to confirm that the finalization logic is
   executed, even though the file has already been deleted by the explicit
   `close()` call in `use`.

```kotlin
@Test
fun `using TempImage with use and still observe finalization`() {
   fun loadImageWithUse() {
      TempImage("https://dev.java/assets/images/duke/duke_star7.png")
            .use {
               // Calling close !!!
               assertTrue(it.downloaded)
            }
   }
   File("duke_star7.png").delete() // Ensure file does not exist before test
   loadImageWithUse()
   assertFalse(File("duke_star7.png").exists())
   /*
   * Once an object is eligible for GC, finalization may occur on a different thread,
   * so you might need to pause briefly to observe the changes.
   */
   System.gc()
   Thread.sleep(100)
   assertFalse(File("duke_star7.png").exists())
}
```

7. How many times does `"Try deleting file..."` get printed in the last test? Why?

8. Why do finalizable objects require additional GC work?

***

# 5. Cleaner API

The Cleaner API provides a safer alternative to finalization.
Unlike finalization:
* cleanup **logic is separated** from the object
* cleanup actions **can be cancelled**
* explicit cleanup avoids redundant cleaner execution


1. Create the following class `TempImageCleanable`, with same properties and
   `init` implementation as `TempImage`, but without the `finalize()` method and
   with a different `close()` implementation that uses the Cleaner API to
   perform cleanup.

```kotlin
class TempImageCleanable(url: String) : Closeable {
    companion object {
        val cleaner: Cleaner = Cleaner.create()
    }

    private val cleanable =
        cleaner.register(
            this,
            object : Runnable {
                /*
                 * Duplicate properties to NOT capture a reference to the enclosing
                 * object, which will prevent GC to collect that object.
                 * The only variable is the url parameter that will be copied.
                 */
                private val file = File(url.substringAfterLast('/'))

                override fun run() {
                    println("Try deleting file...")
                    if (file.exists()) {
                        file.delete()
                    }
                }
            },
        )
    /*
     * Replace the former `close()` implementation with a call to `cleanable.clean()`, 
     * which will execute the registered cleaning action immediately and prevent it
     * from being executed again during finalization.
     */
    override fun close() {
        cleanable.clean()
    }
    /*
     * Copy and paste the rest of the implementation from TempImage, 
     * including the properties and the init block, but without the finalize() method.
     */
     ...
}
```

2. Copy paste the former two unit tests for `TempImage` to test
   `TempImageCleanable` instead, and register your observations and the
   differences in behavior compared to `TempImage`.

3. How many times is `"Try deleting file..."` printed for each test? Why?

4. What happens internally when `clean()` is invoked?

5. Why is this behavior better than finalization?



# 6. Captured References and Cleaner Pitfalls

The Java documentation warns that cleaning actions must avoid capturing the
enclosing instance.
Otherwise, the object may never become unreachable.

1. Remove the duplicated `file` property inside the anonymous `object :
   Runnable` and instead capture a reference to the enclosing instance by
   directly accessing the `file` property of the `TempImageCleanable` class.

2. Run the tests again and observe the differences in behavior compared to the
   previous observations.
   
3. Explain why the file is not deleted in the test that does not call `close()`,
   even after invoking `System.gc()` and waiting.  
   Justify your explanation based on the structure of `TempImageCleanable$cleanable$1.class`,
   which corresponds to the cleaner action implementing `Runnable`.  
   Inspect it using: `javap -p 'TempImageCleanable$cleanable$1.class'`
