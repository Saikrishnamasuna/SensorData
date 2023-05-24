package com.example.sensordata.ui

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log
import android.view.Gravity
import android.view.View

class SensorGraphView(context: Context) : View(context), SensorEventListener {
    private val sensorManager: SensorManager =
        context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private val gyroscope: Sensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)
    private val accelerometer: Sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

    private val dataPointsSize = 100
    private val dataPoints = Array(dataPointsSize) { 0f }
    private val paint = Paint().apply {
        color = Color.RED
        strokeWidth = 5f
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        sensorManager.registerListener(this, gyroscope, SensorManager.SENSOR_DELAY_NORMAL)
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent) {
        if (event.sensor == gyroscope || event.sensor == accelerometer) {
            val x = event.values[0]

            updateDataPoints(x)
            invalidate()
        }
    }

    private fun updateDataPoints(value: Float) {
        dataPoints[0] = value
        for (i in dataPointsSize - 1 downTo 1) {
            dataPoints[i] = dataPoints[i - 1]
        }

    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // Do nothing
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawColor(Color.WHITE)

        val width = width.toFloat()
        val height = height.toFloat()

        val startX = 0f
        val endX = width
        val startY = height / 2f
        val endY = height / 3f

        for (i in 1 until dataPointsSize) {
            val prevX = startX + (i - 1) * (endX - startX) / (dataPointsSize - 1)
            val prevY = startY - dataPoints[i - 1] * (endY - startY) / 3f
            val currX = startX + i * (endX - startX) / (dataPointsSize - 1)
            val currY = startY - dataPoints[i] * (endY - startY) / 3f

            canvas.drawLine(prevX, prevY, currX, currY, paint).apply { Gravity.CENTER }
            Log.d(
                "Lines vALUES : ",
                "prevX :" + prevX + "prevY :" + prevY + "cuurX :" + currX + "currY :" + currY
            )
        }
    }
}



