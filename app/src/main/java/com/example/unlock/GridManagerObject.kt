package com.example.unlock

import android.graphics.*
import androidx.core.graphics.minus
import androidx.core.graphics.plus
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlin.math.roundToInt

object GridManagerObject {
    var blockSize: PointF = PointF(1f, 1f)
    private val _moves: MutableLiveData<Int> = MutableLiveData<Int>()
    val test: LiveData<Int>
        get() = _moves

    private val _fresh: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    val test2: LiveData<Boolean>
        get() = _fresh

    private var grabbed: Boolean = false
    private var grabbedPosition: PointF = PointF(0f, 0f)
    private var currentRectangle: Rectangle? = null
    private var actions: MutableList<GridCommand> =  ArrayList()

    private var rectangles: Array<Array<Rectangle?>> =
        Array(6){arrayOfNulls<Rectangle?>(6)}

    init {
        _moves.value = 0
        _fresh.value = true
    }

    fun undo() {
        if (actions.count() < 1 || _moves.value == 0)
            return

        rectangles = Array(6){arrayOfNulls<Rectangle?>(6)}

        actions.pop()

        if (actions[actions.count() - 1].rectangle == null)
            actions.pop()

        for (action in actions) {
            action.exec(rectangles)
        }
        _moves.value = _moves.value!!.toInt() - 1
        _fresh.value = _moves.value == 0
    }

    fun deleteActions(){
        rectangles = Array(6){arrayOfNulls<Rectangle?>(6)}
        actions.clear()
        _moves.value = 0
        _fresh.value = true
    }

    private fun addCommand(position: Point,
                           dimensions: Point,
                           rectangle: Rectangle?,
                           fromUser: Boolean = false) {
        val gridCommand: GridCommand = GridCommand(position, dimensions, rectangle)
        gridCommand.exec(rectangles)
        actions += gridCommand

        if(fromUser && rectangle != null) {
            _moves.value = _moves.value!!.toInt() + 1
            _fresh.value = _moves.value == 0
        }
    }

    fun addRectangle(position: Point,
                     dimensions: Point,
                     stuck: Boolean = false,
                     fromUser: Boolean = false) {
        val rectangle: Rectangle = Rectangle(position, dimensions, blockSize, stuck)
        addCommand(position, dimensions, rectangle, fromUser)
    }

    private fun addRemoveRectangle(position: Point,
                                   dimensions: Point,
                                   rectangle: Rectangle?,
                                   rectangles: Array<Array<Rectangle?>>) {
        if (dimensions.x > dimensions.y) {
            val range : Int = dimensions.x - 1
            for (i in 0..range) {
                rectangles[position.x + i][position.y] = rectangle
            }
        } else {
            val range : Int = dimensions.y - 1
            for (i in 0..range) {
                rectangles[position.x][position.y + i] = rectangle
            }
        }
    }

    fun grab(touchPosition: PointF) {
        grabbedPosition.set(touchPosition)

        val gridIndex: Point = (grabbedPosition / blockSize)

        if (gridIndex.x < 6 && gridIndex.y < 6) {
            currentRectangle = rectangles[gridIndex.x][gridIndex.y]
            grabbed = currentRectangle != null
        }
    }

    fun release(touchPosition: PointF) {
        if (currentRectangle == null)
            return

        val canvasPosition: PointF =
            translateRectangle(currentRectangle, touchPosition - grabbedPosition)


        val gridDimensions: Point = currentRectangle!!.gridDimensions

        val indexX: Int = (canvasPosition.x / blockSize.x).roundToInt()
        val indexY: Int = (canvasPosition.y / blockSize.y).roundToInt()
        val maxX = 6 - gridDimensions.x
        val maxY = 6 - gridDimensions.y
        val gridIndex: Point =
            Point(indexX.coerceIn(0..maxX),
                indexY.coerceIn(0..maxY))

        if(rectangles[gridIndex.x][gridIndex.y] != null &&
            rectangles[gridIndex.x][gridIndex.y]!!.gridIndex == gridIndex) {
            currentRectangle = null
            return
        }

        addCommand(currentRectangle!!.gridIndex, gridDimensions, null, fromUser = true)
        addRectangle(gridIndex, gridDimensions, fromUser = true, stuck = currentRectangle!!.stuck)

        currentRectangle = null
    }

    private fun getNextObstacle(array: Array<Rectangle?>, startIndex: Int, endIndex: Int): Int {
        var index: Int = 0
        for (i in startIndex..endIndex) {
            if (array[i] != null) {
                break;
            }
            index++
        }

        return index
    }

    private fun getPrevObstacle(array: Array<Rectangle?>, startIndex: Int, endIndex: Int): Int {
        var index: Int = 0
        for (i in startIndex downTo endIndex) {
            if (array[i] != null) {
                break;
            }
            index--
        }

        return index
    }

    private fun getColumn(matrix: Array<Array<Rectangle?>>, col: Int):  Array<Rectangle?>{
        val column = arrayOfNulls<Rectangle?>(matrix.size)

        for (i in column.indices) {
            column[i] = matrix[i][col]
        }

        return column
    }

    private fun translateRectangle(rectangle: Rectangle?, translation: PointF): PointF {
        if (rectangle!!.gridDimensions.x >
            rectangle!!.gridDimensions.y) {
            translation.y = 0f

            val column: Array<Rectangle?> = getColumn(rectangles, rectangle!!.gridIndex.y)
            var maxX: Int =
                getNextObstacle(column,
                    (rectangle!!.gridIndex.x + rectangle!!.gridDimensions.x),
                    5)

            var minX: Int = getPrevObstacle(column, rectangle!!.gridIndex.x - 1, 0)


            translation.x = translation.x.coerceIn((minX * blockSize.x)..(maxX * blockSize.x))
        } else {
            translation.x = 0f
            val row: Array<Rectangle?> = rectangles[rectangle!!.gridIndex.x]

            var maxX: Int =
                getNextObstacle(row,
                    (rectangle!!.gridIndex.y + rectangle!!.gridDimensions.y),
                    5)

            var minX: Int = getPrevObstacle(row, rectangle!!.gridIndex.y - 1, 0)


            translation.y = translation.y.coerceIn((minX * blockSize.y)..(maxX * blockSize.y))

        }

        return currentRectangle!!.canvasPosition + translation
    }

    fun moveTo(touchPosition: PointF, canvas: Canvas?) {
        var newCanvasPosition: PointF = PointF(0f, 0f)

        if (grabbed) {
            newCanvasPosition =
                translateRectangle(currentRectangle,
                    touchPosition - grabbedPosition)
        }

        if (grabbed)
            updateRectangle(currentRectangle!!.gridIndex,
                newCanvasPosition, canvas)
        else
            updateRectangle(Point(0, 0), PointF(0f, 0f), canvas)
    }

    fun redrawRectangles(canvas: Canvas?) {
        for (rows in rectangles) {
            for (rectangle in rows) {
                rectangle?.draw(canvas)
            }
        }
    }


    private fun updateRectangle(gridIndex: Point,
                                canvasPosition: PointF,
                                canvas: Canvas?) {
        for (row in rectangles) {
            for (rectangle in row) {
                if ((rectangle?.gridIndex != gridIndex)) {
                    rectangle?.draw(canvas)
                }
            }
        }
        rectangles[gridIndex.x][gridIndex.y]?.redraw(canvasPosition, canvas)
    }

    class GridCommand(private val position: Point,
                      private val dimensions: Point,
                      val rectangle: Rectangle?) {


        private fun addRemoveRectangle(rectangles: Array<Array<Rectangle?>>) {
            if (dimensions.x > dimensions.y) {
                val range : Int = dimensions.x - 1
                for (i in 0..range) {
                    rectangles[position.x + i][position.y] = rectangle
                }
            } else {
                val range : Int = dimensions.y - 1
                for (i in 0..range) {
                    rectangles[position.x][position.y + i] = rectangle
                }
            }
        }

        fun exec(rectangles: Array<Array<Rectangle?>>) {
            addRemoveRectangle(rectangles)
        }
    }

    class Rectangle(val gridIndex: Point,
                    val gridDimensions: Point,
                    bSize: PointF, val stuck: Boolean) {

        private val blockSize: PointF = PointF(bSize.x, bSize.y)

        private val paint:Paint = Paint()
        private val strokePaint:Paint = Paint()

        init {
            paint.isFilterBitmap = true
            paint.isAntiAlias = true
            paint.color = Color.parseColor("#FF3F51B5")
            strokePaint.isFilterBitmap = true
            strokePaint.isAntiAlias = true
            strokePaint.style = Paint.Style.STROKE
            strokePaint.strokeWidth = 5f
            strokePaint.color = Color.parseColor("#ff000000")
        }
        init {
            paint.isFilterBitmap = true
            paint.isAntiAlias = true

            if(stuck)
                paint.color = Color.parseColor("#FFF00F00")
            else {
                paint.color = Color.parseColor("#FF3F51B5")
            }
        }

        val canvasDimensions: PointF = gridDimensions * blockSize
        var canvasPosition: PointF = gridIndex * blockSize

        private var right: Float = canvasPosition.x + canvasDimensions.x
        private var bottom: Float = canvasPosition.y + canvasDimensions.y

        private fun update(newX: Float, newY: Float) {
            canvasPosition.set(newX, newY)
            right = canvasPosition.x + canvasDimensions.x
            bottom = canvasPosition.y + canvasDimensions.y
        }

        fun redraw(canvasPosition: PointF, canvas: Canvas?) {
            canvas?.drawRoundRect(canvasPosition.x, canvasPosition.y,
                canvasPosition.x + canvasDimensions.x,
                canvasPosition.y + canvasDimensions.y,50.0F, 50.0F, this.paint)
            canvas?.drawRoundRect(canvasPosition.x, canvasPosition.y,
                canvasPosition.x + canvasDimensions.x,
                canvasPosition.y + canvasDimensions.y,50.0F, 50.0F, this.strokePaint)
        }

        fun draw(canvas: Canvas?) {
            //canvas?.drawRect(canvasPosition.x, canvasPosition.y, right, bottom, paint)
            canvas?.drawRoundRect(canvasPosition.x, canvasPosition.y, right, bottom,
                50.0F, 50.0F, this.paint)
            canvas?.drawRoundRect(canvasPosition.x, canvasPosition.y, right, bottom,
                50.0F, 50.0F, this.strokePaint)
        }
    }
}

private operator fun Point.times(pointF: PointF): PointF {
    return PointF(x * pointF.x, y * pointF.y)
}

private operator fun PointF.div(pointF: PointF): Point {
    return Point((x / pointF.x).toInt(), (y / pointF.y).toInt())
}

private operator fun PointF.div(blockWidth: Float): Point {
    return Point((x / blockWidth).toInt(), (y / blockWidth).toInt())
}

fun <T> MutableList<T>.push(item: T) = this.add(this.count(), item)
fun <T> MutableList<T>.pop(): T? = if(this.count() > 0) this.removeAt(this.count() - 1) else null
fun <T> MutableList<T>.peek(): T? = if(this.count() > 0) this[this.count() - 1] else null
fun <T> MutableList<T>.hasMore() = this.count() > 0
