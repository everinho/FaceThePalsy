package net.simplifiedcoding.FaceThePalsy.exercises

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PointF
import android.graphics.Rect
import android.graphics.Typeface
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
        var isExercising_left_2: Boolean = false
        var left_repeats_2: Int = 0
        var isExercising_right_2: Boolean = false
        var right_repeats_2: Int = 0
        var prog_1: Float = 0F
        var prog_2: Float = 0F
        var prog_3: Float = 0F
        var prog_4: Float = 0F
        var suma_1: Float = 0F
        var suma_2: Float = 0F
        var suma_3: Float = 0F
        var suma_4: Float = 0F
        var sumat: Float = 0F
        var usrednianie_1: Int = 0
        var usrednianie_2: Int = 0
        var usrednianie_3: Int = 0
        var usrednianie_4: Int = 0
    }

    private val paint = Paint().apply {
        color = Color.BLUE
        style = Paint.Style.STROKE
        strokeWidth = 6.0f
    }

    private val paint_text = Paint().apply {
        color = Color.BLACK
        textSize = 55.0f
        setShadowLayer(5f, 5f, 5f, Color.BLACK)
        typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
    }

    private val paint_text_success = Paint().apply {
        color = Color.GREEN
        textSize = 55.0f
        setShadowLayer(5f, 5f, 5f, Color.DKGRAY)
        typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
    }
    private val paint_punkt = Paint().apply {
        color = Color.RED
        style = Paint.Style.FILL
        strokeWidth = 10.0f
    }

    override fun draw(canvas: Canvas?) {
//        val faceContours = face.getContour(FaceContour.FACE)?.points
//        val point1 = faceContours?.getOrNull(18)
//        val point2 = faceContours?.getOrNull(35)

        val leftEye = face.getContour(FaceContour.LEFT_EYE)?.points
        val rightEye = face.getContour(FaceContour.RIGHT_EYE)?.points

        val point1 = leftEye?.getOrNull(0)
        val point2 = rightEye?.getOrNull(8)

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
        //canvas?.drawPoint(mappedPoint1.x, mappedPoint1.y, paint_punkt)

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
        //canvas?.drawPoint(mappedPoint2.x, mappedPoint2.y, paint_punkt)
        val length = calculateDistancesimple(mappedPoint1.x,mappedPoint1.y, mappedPoint2.x,mappedPoint2.y)

        if(id == 0 || id == 2)
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

            val leftEyebrowBottomPoints = face.getContour(FaceContour.LEFT_EYEBROW_BOTTOM)?.points
            leftEyebrowBottomPoints?.forEach { point ->
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

            if(id == 0)
            {
                if(usrednianie_1<30)
                {
                    if(left_repeats==0 && prog_1==0F)
                    {
                        sumat = calculateDistance(
                            leftEyebrowTopPoints,
                            leftEye
                        )!!
                        sumat /= length
                        suma_1 += sumat
                        usrednianie_1++
                    }
                }
                else {
                    prog_1= suma_1/30
                    sumat = 0F
                    val proportion = leftEyebrowEyeDistance!! / length
                    if (proportion > 1.1 * prog_1) {
                        if (!isExercising_left) {
                            isExercising_left = true
                            if (left_repeats < 10) {
                                left_repeats += 1
                            }
                        }
                    } else if(proportion <= prog_1) {
                        isExercising_left = false
                    }

                    if (left_repeats >= 10) {
                        canvas?.drawText("Ćwiczenie ukończone!", 50F, 650F, paint_text_success)
                    }
                }
                val proportion = leftEyebrowEyeDistance!! / length
                val leftDistanceText = "Dystans: ${proportion?.toString() ?: "N/A"}"
                //canvas?.drawText("Ćwiczenie 1 - unoszenie prawej powieki", 50F, 250F, paint_text)
                canvas?.drawText("Iteracja: ${usrednianie_1?.toString() ?: "N/A"}", 50F, 250F, paint_text)
                canvas?.drawText("Prog: ${prog_1?.toString() ?: "N/A"}", 50F, 350F, paint_text)
                canvas?.drawText(leftDistanceText, 50F, 450F, paint_text)

                if(left_repeats<10)
                {
                    if(!isExercising_left) canvas?.drawText("Unieś powiekę", 600F, 350F, paint_text)
                    else canvas?.drawText("Opuść powiekę", 600F, 350F, paint_text)
                }

                val repetitionsText = "Powtórzenia: $left_repeats / 10"
                canvas?.drawText(repetitionsText, 50F, 550F, paint_text)

            }
            else if(id == 2)
            {
                if(usrednianie_3<30)
                {
                    if(left_repeats_2==0 && prog_3==0F)
                    {
                        sumat = calculateDistance(
                            leftEyebrowTopPoints,
                            leftEye
                        )!!
                        sumat /= length
                        suma_3 += sumat
                        usrednianie_3++
                    }
                }
                else
                {
                    prog_3 = suma_3 / 30
                    sumat = 0F
                    val proportion = leftEyebrowEyeDistance!! / length

                    if (proportion < 0.9*prog_3) {
                        if (!isExercising_left_2) {
                            isExercising_left_2 = true
                            if (left_repeats_2 < 10) {
                                left_repeats_2 += 1
                            }
                        }
                    } else if(proportion >=prog_3) {
                        isExercising_left_2 = false
                    }

                    if (left_repeats_2 >= 10) {
                        canvas?.drawText("Ćwiczenie ukończone!", 50F, 650F, paint_text_success)
                    }
                }
                val proportion = leftEyebrowEyeDistance!! / length
                val leftDistanceText = "Dystans: ${proportion?.toString() ?: "N/A"}"
                //canvas?.drawText("Ćwiczenie 3 - mróżenie prawego oka", 50F, 250F, paint_text)
                canvas?.drawText("Iteracja: ${usrednianie_3?.toString() ?: "N/A"}", 50F, 250F, paint_text)
                canvas?.drawText(leftDistanceText, 50F, 350F, paint_text)
                canvas?.drawText("Prog: ${prog_3?.toString() ?: "N/A"}", 50F, 450F, paint_text)

                if(left_repeats_2<10)
                {
                    if(!isExercising_left_2) canvas?.drawText("Opuść powiekę", 600F, 350F, paint_text)
                    else canvas?.drawText("Unieś powiekę", 600F, 350F, paint_text)
                }

                val repetitionsText = "Powtórzenia: $left_repeats_2 / 10"
                canvas?.drawText(repetitionsText, 50F, 550F, paint_text)
            }
        }

        if(id == 1 || id == 3)
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

            val rightEyebrowBottomPoints = face.getContour(FaceContour.RIGHT_EYEBROW_BOTTOM)?.points
            rightEyebrowBottomPoints?.forEach { point ->
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

            if(id == 1)
            {
                if(usrednianie_2<30)
                {
                    if(left_repeats_2==0 && prog_2==0F)
                    {
                        sumat = calculateDistance(
                            rightEyebrowTopPoints,
                            rightEye
                        )!!
                        sumat /= length
                        suma_2 += sumat
                        usrednianie_2++
                    }
                }else
                {
                    prog_2 = suma_2 / 30
                    sumat = 0F
                    val proportion = rightEyebrowEyeDistance!! /length

                    if (proportion > 1.1*prog_2) {
                        if (!isExercising_right) {
                            isExercising_right = true
                            if (right_repeats < 10) {
                                right_repeats += 1
                            }
                        }
                    } else if(proportion <= prog_2) {
                        isExercising_right = false
                    }
                    if (right_repeats >= 10) {
                        canvas?.drawText("Ćwiczenie ukończone!", 50F, 650F, paint_text_success)
                    }
                }
                val proportion = rightEyebrowEyeDistance!! /length
                val rightDistanceText = "Dystans: ${proportion?.toString() ?: "N/A"}"
                //canvas?.drawText("Ćwiczenie 2 - Unoszenie lewej powieki", 50F, 250F, paint_text)
                canvas?.drawText("Iteracja: ${usrednianie_2?.toString() ?: "N/A"}", 50F, 250F, paint_text)
                canvas?.drawText("Prog: ${prog_2?.toString() ?: "N/A"}", 50F, 350F, paint_text)
                canvas?.drawText(rightDistanceText, 50F, 450F, paint_text)

                val repetitionsText = "Powtórzenia: $right_repeats / 10"
                canvas?.drawText(repetitionsText, 50F, 550F, paint_text)

                if(right_repeats<10)
                {
                    if(!isExercising_right) canvas?.drawText("Unieś powiekę", 600F, 350F, paint_text)
                    else canvas?.drawText("Opuść powiekę", 600F, 350F, paint_text)
                }

            }
            else if (id == 3)
            {
                if(usrednianie_4<30)
                {
                    if(right_repeats_2==0 && prog_4==0F)
                    {
                        sumat = calculateDistance(
                            rightEyebrowTopPoints,
                            rightEye
                        )!!
                        sumat /= length
                        suma_4 += sumat
                        usrednianie_4++
                    }
                }
                else
                {
                    prog_4 = suma_4 / 30
                    sumat = 0f
                    val proportion = rightEyebrowEyeDistance!! /length
                    if (proportion <= 0.9*prog_4) {
                        if (!isExercising_right_2) {
                            isExercising_right_2 = true
                            if (right_repeats_2 < 10) {
                                right_repeats_2 += 1
                            }
                        }
                    } else if(proportion >= prog_4) {
                        isExercising_right_2 = false
                    }
                    if (right_repeats_2 >= 10) {
                        canvas?.drawText("Ćwiczenie ukończone!", 50F, 650F, paint_text_success)
                    }
                }
                val proportion = rightEyebrowEyeDistance!! /length
                val rightDistanceText = "Dystans: ${proportion?.toString() ?: "N/A"}"
                //canvas?.drawText("Ćwiczenie 4 - mróżenie lewego oka", 50F, 250F, paint_text)
                canvas?.drawText("Iteracja: ${usrednianie_4?.toString() ?: "N/A"}", 50F, 250F, paint_text)
                canvas?.drawText("Prog: ${prog_4?.toString() ?: "N/A"}", 50F, 350F, paint_text)
                canvas?.drawText(rightDistanceText, 50F, 450F, paint_text)

                if(right_repeats_2<10)
                {
                    if(!isExercising_right_2) canvas?.drawText("Opuść powiekę", 600F, 350F, paint_text)
                    else canvas?.drawText("Unieś powiekę", 600F, 350F, paint_text)
                }

                val repetitionsText = "Powtórzenia: $right_repeats_2 / 10"
                canvas?.drawText(repetitionsText, 50F, 550F, paint_text)

            }
        }

        if (id == 4) {
            val smilingProbability = face.smilingProbability
            //val smileText = "Smiling probability: $smilingProbability"
            //canvas?.drawText(smileText, 50F, 350F, paint_text)
            val repetitionsText = "Powtórzenia: $smile_repeats / 10"

            canvas?.drawText("Ćwiczenie 5 - Uśmiech", 50F, 250F, paint_text)
            canvas?.drawText(repetitionsText, 50F, 450F, paint_text)

            if(smile_repeats<10)
            {
                if(!isSmiling) canvas?.drawText("Uśmiechnij się", 50F, 350F, paint_text)
                else canvas?.drawText("Neutralna mimika", 50F, 350F, paint_text)
            }

            if (smilingProbability != null) {
                if (smilingProbability > 0.9) {
                    if (!isSmiling) {
                        isSmiling = true
                        if (smile_repeats < 10) {
                            smile_repeats += 1
                        }
                    }
                } else if (smilingProbability < 0.1) {
                    isSmiling = false
                }
            }

            if (smile_repeats >= 10) {
                canvas?.drawText("Ćwiczenie ukończone!", 50F, 550F, paint_text_success)
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
            distance /= points1.size
            return distance
        }
        return null
    }

    private fun calculateDistancesimple(x1: Float, y1: Float, x2: Float, y2: Float): Float {
        return sqrt((x2 - x1).pow(2) + (y2 - y1).pow(2))
    }
}