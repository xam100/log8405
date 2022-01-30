package com.example.unlock

import android.graphics.*
import androidx.core.graphics.minus
import androidx.core.graphics.plus
import kotlin.math.roundToInt

class GridManager(var blockSize: PointF) {

    private var grabbed: Boolean = false
    private var grabbedPosition: PointF = PointF(0f, 0f)
    private var currentRectangle: Rectangle? = null
    private var actions: MutableList<GridCommand> =  ArrayList()

    private var rectangles: Array<Array<Rectangle?>> =
        Array(6){arrayOfNulls<Rectangle?>(6)}

    fun undo() {
        rectangles = Array(6){arrayOfNulls<Rectangle?>(6)}

        if (actions.count() < 1)
            return

        actions.pop()

        if (actions[actions.count() - 1].rectangle == null)
            actions.pop()

        for (action in actions) {
            action.exec(rectangles)
        }
    }

    private fun addCommand(position: Point,
                           dimensions: Point,
                           rectangle: Rectangle?) {
        val gridCommand: GridCommand = GridCommand(position, dimensions, rectangle)
        gridCommand.exec(rectangles)
        actions += gridCommand
    }

    fun addRectangle(position: Point,
                     dimensions: Point, stuck: Boolean = false) {
        val rectangle: Rectangle = Rectangle(position, dimensions, blockSize)
        addCommand(position, dimensions, rectangle)
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
        addCommand(currentRectangle!!.gridIndex, gridDimensions, null)
        addRectangle(gridIndex, gridDimensions)

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

    fun moveTo(touchPosition: PointF, canvas: Canvas?, paint: Paint) {
        var newCanvasPosition: PointF = PointF(0f, 0f)

        if (grabbed) {
            newCanvasPosition =
                translateRectangle(currentRectangle,
                    touchPosition - grabbedPosition)
        }

        if (grabbed)
            updateRectangle(currentRectangle!!.gridIndex,
                newCanvasPosition, canvas, paint)
        else
            updateRectangle(Point(0, 0), PointF(0f, 0f), canvas, paint)
    }

    fun redrawRectangles(canvas: Canvas?, paint: Paint) {
        for (rows in rectangles) {
            for (rectangle in rows) {
                rectangle?.draw(canvas, paint)
            }
        }
    }


    private fun updateRectangle(gridIndex: Point,
                                canvasPosition: PointF,
                                canvas: Canvas?,
                                paint: Paint) {
        for (row in rectangles) {
            for (rectangle in row) {
                if ((rectangle?.gridIndex != gridIndex)) {
                    rectangle?.draw(canvas, paint)
                }
            }
        }
        rectangles[gridIndex.x][gridIndex.y]?.redraw(canvasPosition, canvas, paint)
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
            println("rectangle: " + rectangle == null)
            addRemoveRectangle(rectangles)
        }
    }

    class Rectangle(val gridIndex: Point,
                    val gridDimensions: Point,
                    bSize: PointF) {

        private val blockSize: PointF = PointF(bSize.x, bSize.y)

        val canvasDimensions: PointF = gridDimensions * blockSize
        var canvasPosition: PointF = gridIndex * blockSize

        private var right: Float = canvasPosition.x + canvasDimensions.x
        private var bottom: Float = canvasPosition.y + canvasDimensions.y

        private fun update(newX: Float, newY: Float) {
            canvasPosition.set(newX, newY)
            right = canvasPosition.x + canvasDimensions.x
            bottom = canvasPosition.y + canvasDimensions.y
        }

        fun redraw(canvasPosition: PointF, canvas: Canvas?, paint: Paint) {
            canvas?.drawRoundRect(canvasPosition.x, canvasPosition.y,
                canvasPosition.x + canvasDimensions.x,
                canvasPosition.y + canvasDimensions.y,50.0F, 50.0F, paint)
        }

        fun draw(canvas: Canvas?, paint: Paint) {
            //canvas?.drawRect(canvasPosition.x, canvasPosition.y, right, bottom, paint)
            canvas?.drawRoundRect(canvasPosition.x, canvasPosition.y, right, bottom,
                50.0F, 50.0F, paint)
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
