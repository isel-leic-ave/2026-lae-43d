package pt.isel

import java.awt.image.BufferedImage
import java.io.Closeable
import java.io.File
import java.net.URI
import javax.imageio.ImageIO

class TempImage(
    url: String,
) : Closeable {
    val img: BufferedImage
    val downloaded: Boolean
    private val file = File(url.substringAfterLast('/'))

    init {
        if (file.exists()) {
            img = ImageIO.read(file)
            downloaded = false
        } else {
            val conn = URI(url).toURL().openConnection()
            val kind = conn.getHeaderField("Content-Type").substringAfterLast('/')
            conn.getInputStream().use { stream ->
                img = ImageIO.read(stream)
                ImageIO.write(img, kind, file)
                downloaded = true
            }
        }
    }

    override fun close() {
        println("Try deleting file...")
        if (file.exists()) {
            file.delete()
        }
    }
}
