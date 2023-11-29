package net.simplifiedcoding.FaceThePalsy.facedetector

import android.content.ContentValues.TAG
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PointF
import android.graphics.Rect
import android.graphics.Typeface
import android.media.MediaPlayer
import android.util.Log
import android.widget.Toast
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.JsonSyntaxException
import com.google.mlkit.vision.face.Face
import com.google.mlkit.vision.face.FaceContour
import net.simplifiedcoding.FaceThePalsy.ProfileActivity
import net.simplifiedcoding.FaceThePalsy.R
import net.simplifiedcoding.FaceThePalsy.ScheduleActivity
import net.simplifiedcoding.FaceThePalsy.exercises.FollowActivity
import net.simplifiedcoding.FaceThePalsy.exercises.FollowBox
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.sqrt

class FaceBox(
    overlay: FaceBoxOverlay,
    private val context: Context,
    private val face: Face,
    private val imageRect: Rect
) : FaceBoxOverlay.FaceBox(overlay) {

    companion object{
        var usrednianie: Int = 0
        //distances
        //val distance_A = distance(point8,point9) //useless
        var distance_Bl = 0F
        var distance_Br = 0F
        //val distance_C = distance(point10,point7) //useless
        var distance_D = 0F
        var distance_E = 0F
        var distance_F = 0F
        var distance_G = 0F
        var distance_H = 0F
        var distance_I = 0F
        var distance_J = 0F
        var distance_K = 0F
        var distance_R = 0F
        var distance_S = 0F
        //var distance_X = distance(point21,point22) //useless
        var distance_T = 0F
        var distance_U = 0F
        var distance_Vl = 0F
        var distance_Vr = 0F
        //var distance_W = distance(point23,point24) //useless
        var distance_Nl = 0F
        var distance_Nr = 0F
        var distance_Ol = 0F
        var distance_Or = 0F
        var distance_Pi = 0F
        var distance_Pu = 0F
        var distance_Qi = 0F
        var distance_Qu = 0F
        var calculated: Boolean = false
        var asymmetry = 0f
        var saved: Boolean = false
        var saved_training: Boolean = false
        var soundPlayed: Boolean = false
        var mediaPlayer: MediaPlayer? = null
    }

    private val paint_text = Paint().apply {
        color = Color.GREEN
        textSize = 72.0f
        typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
    }
    private val paint_text2 = Paint().apply {
        color = Color.GREEN
        textSize = 42.0f
    }

    private val paintPoint = Paint().apply {
        color = Color.BLUE
        style = Paint.Style.FILL
        strokeWidth = 7.0f
        isAntiAlias = true
    }

    override fun draw(canvas: Canvas?) {

        val asymmetryDataFile = File(context.getExternalFilesDir(null), "asymmetry_data.json")
        val isFirstScan = !asymmetryDataFile.exists() || asymmetryDataFile.readText().isEmpty()

        //eyes
        val left_eye = face.getContour(FaceContour.LEFT_EYE)?.points
        val right_eye = face.getContour(FaceContour.RIGHT_EYE)?.points

        val point1 = left_eye?.getOrNull(0)
        val point2 = left_eye?.getOrNull(8)

        val point3 = right_eye?.getOrNull(0)
        val point4 = right_eye?.getOrNull(8)

        drawFacialLandmarks(canvas, point1, point2)
        drawFacialLandmarks(canvas, point3, point4)

        val point25 = left_eye?.getOrNull(3)
        val point26 = left_eye?.getOrNull(5)

        val point27 = left_eye?.getOrNull(13)
        val point28 = left_eye?.getOrNull(11)

        val point29 = right_eye?.getOrNull(3)
        val point30 = right_eye?.getOrNull(5)

        val point31 = right_eye?.getOrNull(13)
        val point32 = right_eye?.getOrNull(11)

        drawFacialLandmarks(canvas, point25, point26)
        drawFacialLandmarks(canvas, point27, point28)
        drawFacialLandmarks(canvas, point29, point30)
        drawFacialLandmarks(canvas, point31, point32)

        //nose
        val nose = face.getContour(FaceContour.NOSE_BOTTOM)?.points

        val point5 = nose?.getOrNull(0)
        val point6 = nose?.getOrNull(2)
        val point21 = nose?.getOrNull(1)

        drawFacialLandmarks(canvas, point5, point6)

        //eyebrows
        val left_eyebrow = face.getContour(FaceContour.LEFT_EYEBROW_TOP)?.points
        val right_eyebrow = face.getContour(FaceContour.RIGHT_EYEBROW_TOP)?.points
        val point11 = left_eyebrow?.getOrNull(0)
        val point12 = left_eyebrow?.getOrNull(1)
        val point13 = left_eyebrow?.getOrNull(2)
        val point14 = left_eyebrow?.getOrNull(3)
        val point15 = left_eyebrow?.getOrNull(4)

        val point16 = right_eyebrow?.getOrNull(4)
        val point17 = right_eyebrow?.getOrNull(3)
        val point18 = right_eyebrow?.getOrNull(2)
        val point19 = right_eyebrow?.getOrNull(1)
        val point20 = right_eyebrow?.getOrNull(0)

        drawFacialLandmarks(canvas, point11, point12)
        drawFacialLandmarks(canvas, point13, point14)
        drawFacialLandmarks(canvas, point15, point16)
        drawFacialLandmarks(canvas, point17, point18)
        drawFacialLandmarks(canvas, point19, point20)

        //face
        val facial = face.getContour(FaceContour.FACE)?.points
        val point7 = facial?.getOrNull(18)
        val point8 = facial?.getOrNull(29)
        val point9 = facial?.getOrNull(7)

        //mouth
        val mouth_lower = face.getContour(FaceContour.LOWER_LIP_BOTTOM)?.points
        val mouth_upper = face.getContour(FaceContour.UPPER_LIP_TOP)?.points
        val point10 = mouth_lower?.getOrNull(4)
        val point22 = mouth_upper?.getOrNull(5)
        val point23 = mouth_upper?.getOrNull(0)
        val point24 = mouth_upper?.getOrNull(10)
        val point33 = mouth_upper?.getOrNull(3)
        val point34 = mouth_upper?.getOrNull(4)
        val point35 = mouth_lower?.getOrNull(6)
        val point36 = mouth_lower?.getOrNull(5)
        val point37 = mouth_upper?.getOrNull(6)
        val point38 = mouth_upper?.getOrNull(7)
        val point39 = mouth_lower?.getOrNull(3)
        val point40 = mouth_lower?.getOrNull(2)

        drawFacialLandmarks(canvas, point7, point8)
        drawFacialLandmarks(canvas, point9, point10)
        drawFacialLandmarks(canvas, point21, point22)
        drawFacialLandmarks(canvas, point23, point24)
        drawFacialLandmarks(canvas, point33, point34)
        drawFacialLandmarks(canvas, point35, point36)
        drawFacialLandmarks(canvas, point37, point38)
        drawFacialLandmarks(canvas, point39, point40)

        //canvas?.drawText("Iteracja: ${usrednianie?.toString() ?: "N/A"}", 50F, 250F, paint_text)

        val iterations = 30
        if(usrednianie<iterations)
        {
            //val distance_A = distance(point8,point9) //useless
            distance_Bl += distance(point1,point2)
            distance_Br += distance(point3,point4)
            //val distance_C = distance(point10,point7) //useless
            distance_D += distance(point8,point1)
            distance_E += distance(point4,point9)
            distance_F += distance(point1,point10)
            distance_G += distance(point4,point10)
            distance_H += distance(point1,point5)
            distance_I += distance(point4,point6)
            distance_J += distance(point5,point10)
            distance_K += distance(point6,point10)
            distance_R += distance(point14,point10)
            distance_S += distance(point17,point10)
            //val distance_X = distance(point21,point22) //useless
            distance_T += distance(point13,point10)
            distance_U += distance(point18,point10)
            distance_Vl += distance(point23,point10)
            distance_Vr += distance(point24,point10)
            //val distance_W = distance(point23,point24) //useless
            distance_Nl += distance(point25,point27)
            distance_Nr += distance(point26,point28)
            distance_Ol += distance(point29,point31)
            distance_Or += distance(point30,point32)
            distance_Pi += distance(point33,point35)
            distance_Pu += distance(point34,point36)
            distance_Qi += distance(point37,point38)
            distance_Qu += distance(point39,point40)
            usrednianie++
        }
        else
        {
            if(!calculated)
            {
                //val distance_A = distance(point8,point9) //useless
                distance_Bl /= iterations
                distance_Br /= iterations
                //val distance_C = distance(point10,point7) //useless
                distance_D /= iterations
                distance_E /= iterations
                distance_F /= iterations
                distance_G /= iterations
                distance_H /= iterations
                distance_I /= iterations
                distance_J /= iterations
                distance_K /= iterations
                distance_R /= iterations
                distance_S /= iterations
                //val distance_X = distance(point21,point22) //useless
                distance_T /= iterations
                distance_U /= iterations
                distance_Vl /= iterations
                distance_Vr /= iterations
                //val distance_W = distance(point23,point24) //useless
                distance_Nl /= iterations
                distance_Nr /= iterations
                distance_Ol /= iterations
                distance_Or /= iterations
                distance_Pi /= iterations
                distance_Pu /= iterations
                distance_Qi /= iterations
                distance_Qu /= iterations

                val pointA = left_eye?.getOrNull(0)
                val pointB = right_eye?.getOrNull(8)

                val adjustedPoint1 = PointF(pointA!!.x, pointA.y)
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

                val adjustedPoint2 = PointF(pointB!!.x, pointB.y)
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

                val length = calculateDistancesimple(mappedPoint1.x,mappedPoint1.y, mappedPoint2.x,mappedPoint2.y)

                val distancesLeft = listOf(
                    distance_Bl, distance_D, distance_H, distance_F, distance_J, distance_T, distance_R, distance_Vl, distance_Pi, distance_Pu, distance_Nl, distance_Nr
                )

                val distancesRight = listOf(
                    distance_Br, distance_E, distance_I, distance_G, distance_K, distance_U, distance_S, distance_Vr, distance_Qi, distance_Qu, distance_Or, distance_Ol
                )

                val normalizedDistancesLeft = distancesLeft.map { it / length }
                val normalizedDistancesRight = distancesRight.map { it / length }

                val differences = normalizedDistancesLeft.mapIndexed { index, distanceLeft ->
                    abs(distanceLeft - normalizedDistancesRight[index])
                }

                val sumOfDifferences = differences.sum()

                asymmetry = sumOfDifferences / distancesLeft.size

                asymmetry *= 100
                calculated = true
            }
            else
            {
                if(!soundPlayed)
                {
                    playSound()
                    soundPlayed = true
                }

                val asymmetrical = "Asymetria: $asymmetry"
                val textX = 80F
                val textY = 250F
                val textColor: Int

                val asymmetryText: String

                if (asymmetry < 2.2) {
                    asymmetryText = "Niewielka asymetria"
                    textColor = Color.GREEN
                } else if (asymmetry < 2.95) {
                    asymmetryText = "Zauważalna asymetria"
                    textColor = Color.YELLOW
                } else {
                    asymmetryText = "Duża asymetria"
                    textColor = Color.RED
                }
                canvas?.drawText(asymmetrical, textX, textY, paint_text.apply { color = textColor })
                canvas?.drawText(asymmetryText, textX, textY + 80, paint_text.apply { color = textColor })

                if (isFirstScan) {
                    val trainingMessage = when {
                        asymmetry < 2.2 -> "Zalecany 1 trening dziennie"
                        asymmetry in 2.2..2.95 -> "Zalecane 2 treningi dziennie"
                        else -> "Zalecane 3 treningi dziennie"
                    }
                    canvas?.drawText(trainingMessage, textX, textY + 180, paint_text.apply { color = textColor })
                }

                val resetMessage = "Możesz teraz ponownie zeskanować twarz."
                val resetMessage2 = "W tym celu użyj przycisku reset!"
                canvas?.drawText(resetMessage, textX, textY + 980, paint_text2.apply { color = Color.BLACK })
                canvas?.drawText(resetMessage2, textX, textY + 1030, paint_text2.apply { color = Color.BLACK })

                if(!saved) {
                    saveAsymmetryToJson(asymmetry)
                    saved = true
                }

                if(!saved_training) {
                    saveTrainingDataToJson(asymmetry)
                    saved_training = true
                }

            }
        }
    }

    private fun saveAsymmetryToJson(asymmetry: Float) {
        try {
            val gson = Gson()

            val jsonObject = JsonObject()
            jsonObject.addProperty("asymmetry", asymmetry)
            jsonObject.addProperty("date", getCurrentDate())

            val fileName = "asymmetry_data.json"
            val file = File(context.getExternalFilesDir(null), fileName)

            val jsonArray: JsonArray

            if (file.exists()) {
                val existingJson = file.readText()
                try {
                    jsonArray = gson.fromJson(existingJson, JsonArray::class.java)
                } catch (e: JsonSyntaxException) {
                    Log.e(TAG, "Błąd podczas parsowania JSON jako JsonArray", e)
                    return
                }
            } else {
                jsonArray = JsonArray()
            }

            jsonArray.add(jsonObject)

            val jsonString = gson.toJson(jsonArray)

            val fileWriter = FileWriter(file, false)
            fileWriter.use {
                it.write(jsonString)
            }

            Log.d(TAG, "Dopisano dane do pliku JSON: $jsonString")
        } catch (e: Exception) {
            Log.e(TAG, "Błąd podczas zapisywania danych do pliku JSON", e)
        }
    }

    private fun getCurrentDate(): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val date = Date()
        return dateFormat.format(date)
    }

    private fun drawFacialLandmarks(canvas: Canvas?,point1: PointF?, point2: PointF?){
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
        canvas?.drawPoint(mappedPoint1.x, mappedPoint1.y, paintPoint)

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
        canvas?.drawPoint(mappedPoint2.x, mappedPoint2.y, paintPoint)
    }

    private fun calculateDistancesimple(x1: Float, y1: Float, x2: Float, y2: Float): Float {
        return sqrt((x2 - x1).pow(2) + (y2 - y1).pow(2))
    }

    private fun saveTrainingDataToJson(asymmetry: Float) {
        try {
            val gson = Gson()

            val fileName = "training_data.json"
            val file = File(context.getExternalFilesDir(null), fileName)

            if (!file.exists()) {
                val jsonArray = JsonArray()

                val jsonObject = JsonObject()
                jsonObject.addProperty("asymmetry", asymmetry)
                jsonObject.addProperty("date", getCurrentDate())
                jsonObject.addProperty("total_training", getTotalTraining(asymmetry))
                jsonObject.addProperty("daily_training", getDailyTraining(asymmetry))
                jsonObject.addProperty("repetitions", getRepetitions(asymmetry))
                jsonObject.addProperty("completed_trainings", 0)

                jsonArray.add(jsonObject)

                val jsonString = gson.toJson(jsonArray)

                val fileWriter = FileWriter(file, false)
                fileWriter.use {
                    it.write(jsonString)
                }

                Log.d(TAG, "Data appended to JSON file: $jsonString")
            } else {
                Log.d(TAG, "File training_data.json already exists. Data not appended.")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error saving data to JSON file", e)
        }
    }


    private fun getTotalTraining(asymmetry: Float): Int {
        return when {
            asymmetry < 2.2 -> 7
            asymmetry in 2.2..2.95 -> 14
            else -> 21
        }
    }

    private fun getDailyTraining(asymmetry: Float): Int {
        return when {
            asymmetry < 2.2 -> 1
            asymmetry in 2.2..2.95 -> 2
            else -> 3
        }
    }

    private fun getRepetitions(asymmetry: Float): Int {
        return when {
            asymmetry < 2.2 -> 8
            asymmetry in 2.2..2.95 -> 10
            else -> 12
        }
    }

    private fun playSound() {
        try {
            if (mediaPlayer == null) {
                mediaPlayer = MediaPlayer.create(context, R.raw.skan)
            }
            mediaPlayer?.start()
        } catch (e: Exception) {
            Log.e(TAG, "Error playing sound", e)
        }
    }

    private fun distance(point1: PointF?, point2: PointF?): Float {
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

        return calculateDistancesimple(
            mappedPoint1.x,
            mappedPoint1.y,
            mappedPoint2.x,
            mappedPoint2.y
        )
    }
}

