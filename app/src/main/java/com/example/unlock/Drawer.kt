package com.example.unlock

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

//data class Position (var x: Float, var y: Float)

class Drawer(con: Context?, attr: AttributeSet?) : View(con, attr) {

        private val gridManager = GridManagerObject
        private val paint:Paint = Paint()
        init {
            paint.isFilterBitmap = true
            paint.isAntiAlias = true
            paint.color = Color.parseColor("#FF0000FF")
        }

        private var touchDown: Boolean = false
        private var position: PointF = PointF(0f, 0f)

    /**
     * Initially draw the rectangles
     *
     * @param w width of rectangle in grid
     * @param h height of rectangle in grid
     * @param oldw old width of rectangle in grid
     * @param oldh old height of rectangle in grid
     */
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
            super.onSizeChanged(w, h, oldw, oldh)

            gridManager.blockSize =
                PointF((width / 6).toFloat(), (height / 6).toFloat())
            gridManager.addRectangle(Point(0, 0), Point(3, 1))
            gridManager.addRectangle(Point(0, 2), Point(2, 1), stuck = true)
            gridManager.addRectangle(Point(0, 3), Point(1, 2))
            gridManager.addRectangle(Point(0, 5), Point(3, 1))
            gridManager.addRectangle(Point(2, 1), Point(1, 3))
            gridManager.addRectangle(Point(5, 0), Point(1, 3))
            gridManager.addRectangle(Point(4, 3), Point(2, 1))
            gridManager.addRectangle(Point(4, 4), Point(1, 2))
        }

    /**
     * Draws our rectangles when we have movements.
     *
     * @param canvas canvas
     */
    override fun draw(canvas: Canvas?) {
            super.draw(canvas)

            canvas?.drawColor(Color.GRAY)

            if (touchDown)
                gridManager.moveTo(position, canvas)
            else
                gridManager.redrawRectangles(canvas)
            invalidate()
        }

    /**
     * Respond to touch events. Here we are interested in events where the touch position changed.
     *
     * @param event MotionEvent reports input details from the touch screen.
     */
    override fun onTouchEvent(event: MotionEvent?): Boolean {
            when (event!!.action) {
                MotionEvent.ACTION_DOWN -> {
                    touchDown = true
                    position.set(event!!.x, event!!.y)

                    gridManager.grab(position)
                }
                MotionEvent.ACTION_MOVE -> {
                    position.set(event!!.x, event!!.y)
                }
                MotionEvent.ACTION_UP -> {
                    touchDown = false
                    gridManager.release(position)
                }
            }

            invalidate()
            return true
        }
    }

