package pt.isel

import java.io.File
import java.io.FileWriter
import kotlin.test.Test
import kotlin.test.assertTrue

class TestCleanup {
    @Test
    fun `write file`() {
        File("temp.txt").delete() // Ensure file does not exist before test
        val writer = FileWriter("temp.txt")
        writer.write("Hello JVM")

        writer.close()
        assertTrue(File("temp.txt").exists())
    }
}
