package com.shieldfilter.v2.processor

import android.content.Context
import android.graphics.Bitmap
import org.tensorflow.lite.Interpreter
import org.tensorflow.lite.support.image.TensorImage
import java.io.FileInputStream
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel

class GenderFilter(private val interpreter: Interpreter?) {
    private val FEMALE_THRESHOLD = 0.65f
    data class GenderScore(val female: Float, val male: Float)

    companion object {
        fun loadFromAsset(ctx: Context): Interpreter? {
            return try {
                val fd = ctx.assets.openFd("gender_classifier.tflite")
                val fis = FileInputStream(fd.fileDescriptor)
                val buf: MappedByteBuffer = fis.channel.map(
                    FileChannel.MapMode.READ_ONLY, fd.startOffset, fd.declaredLength
                )
                Interpreter(buf)
            } catch (_: Exception) { null }
        }
    }

    fun predict(bmp: Bitmap): GenderScore {
        if(interpreter == null) return GenderScore(0.5f,0.5f)
        return try {
            val img = TensorImage.fromBitmap(bmp)
            val out = Array(1){FloatArray(2)}
            interpreter.run(img.buffer, out)
            GenderScore(out[0][0], out[0][1])
        } catch (_: Exception) {
            GenderScore(0.5f,0.5f)
        }
    }

    fun shouldTrigger(score: GenderScore): Boolean = score.female >= FEMALE_THRESHOLD
}
