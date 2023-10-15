package net.simplifiedcoding.FaceThePalsy.exercises

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PointF
import android.graphics.Rect
import com.google.mlkit.vision.face.Face
import com.google.mlkit.vision.face.FaceContour
import net.simplifiedcoding.FaceThePalsy.facedetector.FaceBoxOverlay


class FollowBox(
    overlay: FaceBoxOverlay,
    private val face: Face,
    private val imageRect: Rect,
    private val id: Int
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
        color = Color.CYAN
        textSize = 47.0f
    }

    override fun draw(canvas: Canvas?) {
        if(id == 0)
        {
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

            val leftEyebrowEyeDistance = calculateDistance(
                leftEyebrowTopPoints,
                leftEye
            )

            val leftDistanceText = "Left Eyebrow to Left Eye Distance: ${leftEyebrowEyeDistance?.toString() ?: "N/A"}"
            canvas?.drawText("Podnoszenie powiek, lewe oko - 10 powtórzeń", 50F, 250F, paint_text)
            canvas?.drawText(leftDistanceText, 50F, 350F, paint_text)
        }

        if(id == 1)
        {
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

            val rightEyebrowEyeDistance = calculateDistance(
                rightEyebrowTopPoints,
                rightEye
            )

            val rightDistanceText = "Right Eyebrow to Right Eye Distance: ${rightEyebrowEyeDistance?.toString() ?: "N/A"}"
            canvas?.drawText("Podnoszenie powiek, prawe oko - 10 powtórzeń", 50F, 250F, paint_text)
            canvas?.drawText(rightDistanceText, 50F, 350F, paint_text)
        }

//        val rect = getBoxRect(
//            imageRectWidth = imageRect.width().toFloat(),
//            imageRectHeight = imageRect.height().toFloat(),
//            faceBoundingBox = face.boundingBox
//        )
//        canvas?.drawRect(rect, paint_box)

        if(id == 2)
        {
            val SmileText = "Smiling probability: ${face.smilingProbability?.toString() ?: "N/A"}"
            canvas?.drawText("Uśmiech - 10 powtórzeń", 50F, 250F, paint_text)
            canvas?.drawText(SmileText, 50F, 350F, paint_text)
        }
    }

    private fun calculateDistance(points1: List<PointF>?, points2: List<PointF>?): Float? {
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
