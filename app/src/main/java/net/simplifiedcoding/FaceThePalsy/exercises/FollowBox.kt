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

            val leftDistanceText = "Right Distance: ${leftEyebrowEyeDistance?.toString() ?: "N/A"}"
            canvas?.drawText("Unoszenie powiek, prawe oko", 50F, 250F, paint_text)
            canvas?.drawText(leftDistanceText, 50F, 350F, paint_text)
            val repetitionsText = "Powtórzenia: $left_repeats / 10"
            canvas?.drawText(repetitionsText, 50F, 450F, paint_text)

            if (leftEyebrowEyeDistance != null) {
                if (leftEyebrowEyeDistance > 300) {
                    if (!isExercising_left) {
                        isExercising_left = true
                        if (left_repeats < 10) {
                            left_repeats += 1
                        }
                    }
                } else if(leftEyebrowEyeDistance < 260) {
                    isExercising_left = false
                }
            }

            if (left_repeats >= 10) {
                canvas?.drawText("Ćwiczenie unoszenia powiek ukończone!", 50F, 550F, paint_text_success)
            }
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

            val rightDistanceText = "Left Distance: ${rightEyebrowEyeDistance?.toString() ?: "N/A"}"
            canvas?.drawText("Unoszenie powiek, lewe oko", 50F, 250F, paint_text)
            canvas?.drawText(rightDistanceText, 50F, 350F, paint_text)
            val repetitionsText = "Powtórzenia: $right_repeats / 10"
            canvas?.drawText(repetitionsText, 50F, 450F, paint_text)

            if (rightEyebrowEyeDistance != null) {
                if (rightEyebrowEyeDistance > 380) {
                    if (!isExercising_right) {
                        isExercising_right = true
                        if (right_repeats < 10) {
                            right_repeats += 1
                        }
                    }
                } else if(rightEyebrowEyeDistance < 340) {
                    isExercising_right = false
                }
            }

            if (right_repeats >= 10) {
                canvas?.drawText("Ćwiczenie unoszenia powiek ukończone!", 50F, 550F, paint_text_success)
            }

        }

//        val rect = getBoxRect(
//            imageRectWidth = imageRect.width().toFloat(),
//            imageRectHeight = imageRect.height().toFloat(),
//            faceBoundingBox = face.boundingBox
//        )
//        canvas?.drawRect(rect, paint_box)

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
}
