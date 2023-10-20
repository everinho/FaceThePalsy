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
        strokeWidth = 20.0f
    }

    override fun draw(canvas: Canvas?) {

        //eyes
        val left_eye = face.getContour(FaceContour.LEFT_EYE)?.points
        val right_eye = face.getContour(FaceContour.RIGHT_EYE)?.points

        val index1 = 0
        val index2 = 8

        val point1 = left_eye?.getOrNull(index1)
        val point2 = left_eye?.getOrNull(index2)

        val point3 = right_eye?.getOrNull(index1)
        val point4 = right_eye?.getOrNull(index2)

        xyz(canvas, point1, point2)
        xyz(canvas, point3, point4)

        //nose
        val nose = face.getContour(FaceContour.NOSE_BOTTOM)?.points

        val point5 = nose?.getOrNull(0)
        val point6 = nose?.getOrNull(2)

        xyz(canvas, point5, point6)

        //face
        val facial = face.getContour(FaceContour.FACE)?.points
        val point7 = facial?.getOrNull(18)
        val point8 = facial?.getOrNull(29)
        val point9 = facial?.getOrNull(7)

        //mouth
        val mouth = face.getContour(FaceContour.LOWER_LIP_BOTTOM)?.points
        val point10 = mouth?.getOrNull(4)

        xyz(canvas, point7, point8)
        xyz(canvas, point9, point10)
    }

    private fun calculateDistance_simple(x1: Float, y1: Float, x2: Float, y2: Float): Float {
        return sqrt((x2 - x1).pow(2) + (y2 - y1).pow(2))
    }
    private fun xyz(canvas: Canvas?,point1: PointF?, point2: PointF?){
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
    }

}
