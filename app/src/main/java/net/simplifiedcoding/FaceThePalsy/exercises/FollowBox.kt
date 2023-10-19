package net.simplifiedcoding.FaceThePalsy.exercises

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PointF
import android.graphics.Rect
import com.google.mlkit.vision.face.Face
import com.google.mlkit.vision.face.FaceContour
import net.simplifiedcoding.FaceThePalsy.facedetector.FaceBoxOverlay
import kotlin.math.pow
import kotlin.math.sqrt


class FollowBox(
    overlay: FaceBoxOverlay,
    private val face: Face,
    private val imageRect: Rect,
    private val id: Int,
) : FaceBoxOverlay.FaceBox(overlay) {

    companion object {
        var smile_repeats: Int = 0
        var isSmiling: Boolean = false
        var isExercising_left: Boolean = false
        var left_repeats: Int = 0
        var isExercising_right: Boolean = false
        var right_repeats: Int = 0
    }

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
        color = Color.BLACK
        textSize = 55.0f
        setShadowLayer(5f, 5f, 5f, Color.BLACK)
    }

    private val paint_text_success = Paint().apply {
        color = Color.GREEN
        textSize = 55.0f
        setShadowLayer(5f, 5f, 5f, Color.DKGRAY)
    }
    private val paint_punkt = Paint().apply {
        color = Color.RED
        style = Paint.Style.STROKE
        strokeWidth = 10.0f
    }

    override fun draw(canvas: Canvas?) {
        if(id == 0)
        {
            val faceContours = face.getContour(FaceContour.FACE)?.points

            val index1 = 18
            val index2 = 35

            val point1 = faceContours?.getOrNull(index1)
            val point2 = faceContours?.getOrNull(index2)


            val adjustedPoint1 = PointF(point1!!.x, point1.y)
            val rect1 = getBoxRect(
                imageRectWidth = imageRect.width().toFloat(),
                imageRectHeight = imageRect.height().toFloat(),
                faceBoundingBox = Rect(
                    adjustedPoint1.x.toInt(),
                    adjustedPoint1.y.toInt(),
                    adjustedPoint1.x.toInt(),
                    adjustedPoint1.y.toInt()
                )
            )
            val mappedPoint1 = PointF(rect1.centerX(), rect1.centerY())
            canvas?.drawPoint(mappedPoint1.x, mappedPoint1.y, paint_punkt)

            val adjustedPoint2 = PointF(point2!!.x, point2.y)
            val rect2 = getBoxRect(
                imageRectWidth = imageRect.width().toFloat(),
                imageRectHeight = imageRect.height().toFloat(),
                faceBoundingBox = Rect(
                    adjustedPoint2.x.toInt(),
                    adjustedPoint2.y.toInt(),
                    adjustedPoint2.x.toInt(),
                    adjustedPoint2.y.toInt()
                )
            )
            val mappedPoint2 = PointF(rect2.centerX(), rect2.centerY())
            canvas?.drawPoint(mappedPoint2.x, mappedPoint2.y, paint_punkt)
            val length = calculateDistance_simple(mappedPoint1.x,mappedPoint1.y, mappedPoint2.x,mappedPoint2.y)

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

            val leftDistanceText = "Right Distance: ${leftEyebrowEyeDistance?.toString() ?: "N/A"}"
            val proporcja = leftEyebrowEyeDistance!! / length
            canvas?.drawText("Unoszenie powiek, prawe oko", 50F, 250F, paint_text)
            canvas?.drawText(leftDistanceText, 50F, 350F, paint_text)
            val repetitionsText = "Powtórzenia: $left_repeats / 10"
            canvas?.drawText(repetitionsText, 50F, 450F, paint_text)

            if (proporcja > 0.29) {
                if (!isExercising_left) {
                    isExercising_left = true
                    if (left_repeats < 10) {
                        left_repeats += 1
                    }
                }
            } else if(proporcja < 0.26) {
                isExercising_left = false
            }

            if (left_repeats >= 10) {
                canvas?.drawText("Ćwiczenie unoszenia powiek ukończone!", 50F, 550F, paint_text_success)
            }
        }

        if(id == 1)
        {
            val faceContours = face.getContour(FaceContour.FACE)?.points

            val index1 = 18
            val index2 = 35

            val point1 = faceContours?.getOrNull(index1)
            val point2 = faceContours?.getOrNull(index2)


            val adjustedPoint1 = PointF(point1!!.x, point1.y)
            val rect1 = getBoxRect(
                imageRectWidth = imageRect.width().toFloat(),
                imageRectHeight = imageRect.height().toFloat(),
                faceBoundingBox = Rect(
                    adjustedPoint1.x.toInt(),
                    adjustedPoint1.y.toInt(),
                    adjustedPoint1.x.toInt(),
                    adjustedPoint1.y.toInt()
                )
            )
            val mappedPoint1 = PointF(rect1.centerX(), rect1.centerY())
            canvas?.drawPoint(mappedPoint1.x, mappedPoint1.y, paint_punkt)

            val adjustedPoint2 = PointF(point2!!.x, point2.y)
            val rect2 = getBoxRect(
                imageRectWidth = imageRect.width().toFloat(),
                imageRectHeight = imageRect.height().toFloat(),
                faceBoundingBox = Rect(
                    adjustedPoint2.x.toInt(),
                    adjustedPoint2.y.toInt(),
                    adjustedPoint2.x.toInt(),
                    adjustedPoint2.y.toInt()
                )
            )
            val mappedPoint2 = PointF(rect2.centerX(), rect2.centerY())
            canvas?.drawPoint(mappedPoint2.x, mappedPoint2.y, paint_punkt)
            val length = calculateDistance_simple(mappedPoint1.x,mappedPoint1.y, mappedPoint2.x,mappedPoint2.y)

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

            val rightDistanceText = "Left Distance: ${rightEyebrowEyeDistance?.toString() ?: "N/A"}"
            val proporcja = rightEyebrowEyeDistance!! /length
            canvas?.drawText("Unoszenie powiek, lewe oko", 50F, 250F, paint_text)
            canvas?.drawText(proporcja.toString(), 50F, 550F, paint_text)
            canvas?.drawText(rightDistanceText, 50F, 350F, paint_text)
            val repetitionsText = "Powtórzenia: $right_repeats / 10"
            canvas?.drawText(repetitionsText, 50F, 450F, paint_text)

            if (proporcja > 0.36) {
                if (!isExercising_right) {
                    isExercising_right = true
                    if (right_repeats < 10) {
                        right_repeats += 1
                    }
                }
            } else if(proporcja < 0.33) {
                isExercising_right = false
            }

            if (right_repeats >= 10) {
                canvas?.drawText("Ćwiczenie unoszenia powiek ukończone!", 50F, 550F, paint_text_success)
            }

        }

        if (id == 2) {
            val smilingProbability = face.smilingProbability
            val smileText = "Smiling probability: $smilingProbability"
            val repetitionsText = "Powtórzenia: $smile_repeats / 10"

            canvas?.drawText("Uśmiech", 50F, 250F, paint_text)
            canvas?.drawText(smileText, 50F, 350F, paint_text)
            canvas?.drawText(repetitionsText, 50F, 450F, paint_text)

            if (smilingProbability != null) {
                if (smilingProbability > 0.95) {
                    if (!isSmiling) {
                        isSmiling = true
                        if (smile_repeats < 10) {
                            smile_repeats += 1
                        }
                    }
                } else if (smilingProbability < 0.05) {
                    isSmiling = false
                }
            }

            if (smile_repeats >= 10) {
                canvas?.drawText("Ćwiczenie uśmiechu ukończone!", 50F, 550F, paint_text_success)
            }
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
    private fun calculateDistance_simple(x1: Float, y1: Float, x2: Float, y2: Float): Float {
        return sqrt((x2 - x1).pow(2) + (y2 - y1).pow(2))
    }
}
