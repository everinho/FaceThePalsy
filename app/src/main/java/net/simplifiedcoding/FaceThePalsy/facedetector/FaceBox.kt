package net.simplifiedcoding.FaceThePalsy.facedetector

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PointF
import android.graphics.Rect
import com.google.mlkit.vision.face.Face

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
    //drawing Contours
//    override fun draw(canvas: Canvas?) {
//        val contours = face.allContours
//        contours.forEach { contour ->
//            val points = contour.points
//            val path = Path()
//            var isFirstPoint = true
//            points.forEach { point ->
//                val adjustedPoint = PointF(point.x, point.y)
//                val rect = getBoxRect(
//                    imageRectWidth = imageRect.width().toFloat(),
//                    imageRectHeight = imageRect.height().toFloat(),
//                    faceBoundingBox = Rect(
//                        adjustedPoint.x.toInt(),
//                        adjustedPoint.y.toInt(),
//                        adjustedPoint.x.toInt(),
//                        adjustedPoint.y.toInt()
//                    )
//                )
//                val mappedPoint = PointF(rect.centerX(), rect.centerY())
//                if (isFirstPoint) {
//                    path.moveTo(mappedPoint.x, mappedPoint.y)
//                    isFirstPoint = false
//                } else {
//                    path.lineTo(mappedPoint.x, mappedPoint.y)
//                }
//            }
//            canvas?.drawPath(path, paint)
//        }
//    }

    override fun draw(canvas: Canvas?) {
        //drawing points
        val contours = face.allContours
        contours.forEach { contour ->
            val points = contour.points
            points.forEach { point ->
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
                canvas?.drawPoint(mappedPoint.x, mappedPoint.y, paint) // Rysuj punkt
            }
        }
        //drawing a bounding Box
        val rect = getBoxRect(
            imageRectWidth = imageRect.width().toFloat(),
            imageRectHeight = imageRect.height().toFloat(),
            faceBoundingBox = face.boundingBox
        )
        canvas?.drawRect(rect, paint_box)
    }
}