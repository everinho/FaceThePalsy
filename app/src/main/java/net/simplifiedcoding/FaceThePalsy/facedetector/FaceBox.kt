package net.simplifiedcoding.FaceThePalsy.facedetector

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PointF
import android.graphics.Rect
import com.google.mlkit.vision.face.Face
import com.google.mlkit.vision.face.FaceContour


class FaceBox(
    overlay: FaceBoxOverlay,
    private val face: Face,
    private val imageRect: Rect
) : FaceBoxOverlay.FaceBox(overlay) {

    private val paint = Paint().apply {
        color = Color.BLUE
        style = Paint.Style.STROKE
        strokeWidth = 6.0f
    }

    private val paint_box = Paint().apply {
        color = Color.RED
        style = Paint.Style.STROKE
        strokeWidth = 4.0f
    }

    private val paint_text = Paint().apply {
        color = Color.GREEN
        textSize = 47.0f
    }

    override fun draw(canvas: Canvas?) {
        val leftEyebrowTopPoints = face.getContour(FaceContour.LEFT_EYEBROW_TOP)?.points
        leftEyebrowTopPoints?.forEach { point ->
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
            val mappedPoint = PointF(rect.centerX(), rect.centerY())
            canvas?.drawPoint(mappedPoint.x, mappedPoint.y, paint)
        }

        val leftEye = face.getContour(FaceContour.LEFT_EYE)?.points
        leftEye?.forEach { point ->
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
            val mappedPoint = PointF(rect.centerX(), rect.centerY())
            canvas?.drawPoint(mappedPoint.x, mappedPoint.y, paint)
        }

        val rightEyebrowTopPoints = face.getContour(FaceContour.RIGHT_EYEBROW_TOP)?.points
        rightEyebrowTopPoints?.forEach { point ->
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
            val mappedPoint = PointF(rect.centerX(), rect.centerY())
            canvas?.drawPoint(mappedPoint.x, mappedPoint.y, paint)
        }

        val rightEye = face.getContour(FaceContour.RIGHT_EYE)?.points
        rightEye?.forEach { point ->
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
            val mappedPoint = PointF(rect.centerX(), rect.centerY())
            canvas?.drawPoint(mappedPoint.x, mappedPoint.y, paint)
        }

        // Obliczenie odległości i przekazanie wyników do słuchacza
        val leftEyebrowEyeDistance = calculateDistance(
            leftEyebrowTopPoints,
            leftEye
        )

        val rightEyebrowEyeDistance = calculateDistance(
            rightEyebrowTopPoints,
            rightEye
        )

        val leftDistanceText = "Left Eyebrow to Left Eye Distance: ${leftEyebrowEyeDistance?.toString() ?: "N/A"}"
        canvas?.drawText(leftDistanceText, 120F, 250F, paint_text)

        val rightDistanceText = "Right Eyebrow to Right Eye Distance: ${rightEyebrowEyeDistance?.toString() ?: "N/A"}"
        canvas?.drawText(rightDistanceText, 120F, 350F, paint_text)

        // Rysowanie prostokąta wokół twarzy
        val rect = getBoxRect(
            imageRectWidth = imageRect.width().toFloat(),
            imageRectHeight = imageRect.height().toFloat(),
            faceBoundingBox = face.boundingBox
        )
        canvas?.drawRect(rect, paint_box)
    }

    fun calculateDistance(points1: List<PointF>?, points2: List<PointF>?): Float? {
        if (points1 != null && points2 != null) {
            var distance = 0f
            for (i in points1.indices) {
                val dx = points1[i].x - points2[i].x
                val dy = points1[i].y - points2[i].y
                distance += Math.sqrt((dx * dx + dy * dy).toDouble()).toFloat()
            }
            return distance
        }
        return null
    }
}
