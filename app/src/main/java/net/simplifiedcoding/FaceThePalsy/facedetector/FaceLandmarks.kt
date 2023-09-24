import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PointF
import android.graphics.Rect
import com.google.mlkit.vision.face.Face
import com.google.mlkit.vision.face.FaceLandmark
import net.simplifiedcoding.FaceThePalsy.facedetector.FaceBoxOverlay

class FaceLandmarks(
    overlay: FaceBoxOverlay,
    private val face: Face,
    private val imageRect: Rect
) : FaceBoxOverlay.FaceBox(overlay) {

    private val paint = Paint().apply {
        color = Color.GREEN
        style = Paint.Style.FILL
        strokeWidth = 6.0f
    }

    override fun draw(canvas: Canvas?) {
        val landmarks = face.allLandmarks
        landmarks.forEach { landmark ->
            val point = landmark.position
            val adjustedPoint = PointF(point.x, point.y)
            val rect = getBoxRect(
                imageRectWidth = imageRect.width().toFloat(),
                imageRectHeight = imageRect.height().toFloat(),
                faceBoundingBox = Rect(
                    adjustedPoint.x.toInt(),
                    adjustedPoint.y.toInt(),
                    adjustedPoint.x.toInt(),
                    adjustedPoint.y.toInt()
                )
            )
            canvas?.drawCircle(rect.centerX(), rect.centerY(), 10f, paint)
        }
    }
}
