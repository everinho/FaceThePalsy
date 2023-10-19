package net.simplifiedcoding.FaceThePalsy.facedetector

import FaceLandmarks
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PointF
import android.graphics.Rect
import android.media.FaceDetector
import android.util.Log
import com.google.mlkit.vision.face.Face
import com.google.mlkit.vision.face.FaceContour
import com.google.mlkit.vision.face.FaceLandmark
import kotlin.math.sqrt
import kotlin.math.pow


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

    private val paint_punkt = Paint().apply {
        color = Color.RED
        style = Paint.Style.STROKE
        strokeWidth = 10.0f
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

        val leftEyebrowEyeDistance = calculateDistance(
            leftEyebrowTopPoints,
            leftEye
        )

        val rightEyebrowEyeDistance = calculateDistance(
            rightEyebrowTopPoints,
            rightEye
        )

        val leftDistanceText = "Left Eyebrow to Left Eye Distance: ${leftEyebrowEyeDistance?.toString() ?: "N/A"}"
        canvas?.drawText(leftDistanceText, 50F, 250F, paint_text)

        val rightDistanceText = "Right Eyebrow to Right Eye Distance: ${rightEyebrowEyeDistance?.toString() ?: "N/A"}"
        canvas?.drawText(rightDistanceText, 50F, 350F, paint_text)

//        val rect = getBoxRect(
//            imageRectWidth = imageRect.width().toFloat(),
//            imageRectHeight = imageRect.height().toFloat(),
//            faceBoundingBox = face.boundingBox
//        )
//        canvas?.drawRect(rect, paint_box)

//        val SmileText = "Smiling probability: ${face.smilingProbability?.toString() ?: "N/A"}"
//        canvas?.drawText(SmileText, 50F, 450F, paint_text)

//        val punkt = face.getLandmark(FaceLandmark.NOSE_BASE)
//        val punkt_x = punkt?.position?.x
//        val punkt_y = punkt?.position?.y
//        val adjustedPoint = PointF(punkt_x!!, punkt_y!!)
//        val cos = getBoxRect(
//            imageRectWidth = imageRect.width().toFloat(),
//            imageRectHeight = imageRect.height().toFloat(),
//            faceBoundingBox = Rect(
//                adjustedPoint.x.toInt(),
//                adjustedPoint.y.toInt(),
//                adjustedPoint.x.toInt(),
//                adjustedPoint.y.toInt()
//            )
//        )
//        val mappedPoint = PointF(cos.centerX(), cos.centerY())
//        canvas?.drawPoint(mappedPoint.x, mappedPoint.y, paint_punkt)

//        val faceContours = face.getContour(FaceContour.FACE)?.points
//
//        val index1 = 18
//        val index2 = 35
//
//        if (faceContours != null) {
//            val point1 = faceContours.getOrNull(index1)
//            val point2 = faceContours.getOrNull(index2)
//
//            if (point1 != null && point2 != null) {
//                val adjustedPoint1 = PointF(point1.x, point1.y)
//                val rect1 = getBoxRect(
//                    imageRectWidth = imageRect.width().toFloat(),
//                    imageRectHeight = imageRect.height().toFloat(),
//                    faceBoundingBox = Rect(
//                        adjustedPoint1.x.toInt(),
//                        adjustedPoint1.y.toInt(),
//                        adjustedPoint1.x.toInt(),
//                        adjustedPoint1.y.toInt()
//                    )
//                )
//                val mappedPoint1 = PointF(rect1.centerX(), rect1.centerY())
//                canvas?.drawPoint(mappedPoint1.x, mappedPoint1.y, paint_punkt)
//
//                val adjustedPoint2 = PointF(point2.x, point2.y)
//                val rect2 = getBoxRect(
//                    imageRectWidth = imageRect.width().toFloat(),
//                    imageRectHeight = imageRect.height().toFloat(),
//                    faceBoundingBox = Rect(
//                        adjustedPoint2.x.toInt(),
//                        adjustedPoint2.y.toInt(),
//                        adjustedPoint2.x.toInt(),
//                        adjustedPoint2.y.toInt()
//                    )
//                )
//                val mappedPoint2 = PointF(rect2.centerX(), rect2.centerY())
//                canvas?.drawPoint(mappedPoint2.x, mappedPoint2.y, paint_punkt)
//                val length = calculateDistance_simple(mappedPoint1.x,mappedPoint1.y, mappedPoint2.x,mappedPoint2.y)
//                canvas?.drawText(length.toString(), 50F, 450F, paint_text)
//            }
//        }

        val punkt1 = face.getLandmark(FaceLandmark.MOUTH_LEFT)
        val punkt1_x = punkt1?.position?.x
        val punkt1_y = punkt1?.position?.y
        val adjustedPoint1 = PointF(punkt1_x!!, punkt1_y!!)
        val cos1 = getBoxRect(
            imageRectWidth = imageRect.width().toFloat(),
            imageRectHeight = imageRect.height().toFloat(),
            faceBoundingBox = Rect(
                adjustedPoint1.x.toInt(),
                adjustedPoint1.y.toInt(),
                adjustedPoint1.x.toInt(),
                adjustedPoint1.y.toInt()
            )
        )
        val mappedPoint1 = PointF(cos1.centerX(), cos1.centerY())
        canvas?.drawPoint(mappedPoint1.x, mappedPoint1.y, paint_punkt)

        val punkt2 = face.getLandmark(FaceLandmark.MOUTH_RIGHT)
        val punkt2_x = punkt2?.position?.x
        val punkt2_y = punkt2?.position?.y
        val adjustedPoint2 = PointF(punkt2_x!!, punkt2_y!!)
        val cos2 = getBoxRect(
            imageRectWidth = imageRect.width().toFloat(),
            imageRectHeight = imageRect.height().toFloat(),
            faceBoundingBox = Rect(
                adjustedPoint2.x.toInt(),
                adjustedPoint2.y.toInt(),
                adjustedPoint2.x.toInt(),
                adjustedPoint2.y.toInt()
            )
        )
        val mappedPoint2 = PointF(cos2.centerX(), cos2.centerY())
        canvas?.drawPoint(mappedPoint2.x, mappedPoint2.y, paint_punkt)

//        val test = face.getContour(FaceContour.FACE)?.points
//        test?.forEach { point ->
//            val adjustedPoint = PointF(point.x, point.y)
//            val rect = getBoxRect(
//                imageRectWidth = imageRect.width().toFloat(),
//                imageRectHeight = imageRect.height().toFloat(),
//                faceBoundingBox = Rect(
//                    adjustedPoint.x.toInt(),
//                    adjustedPoint.y.toInt(),
//                    adjustedPoint.x.toInt(),
//                    adjustedPoint.y.toInt()
//                )
//            )
//            val mappedPoint = PointF(rect.centerX(), rect.centerY())
//            canvas?.drawPoint(mappedPoint.x, mappedPoint.y, paint_punkt)
//        }

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
