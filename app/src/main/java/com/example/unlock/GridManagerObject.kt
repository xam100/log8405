package com.example.unlock

import android.graphics.*
import androidx.core.graphics.minus
import androidx.core.graphics.plus
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlin.math.roundToInt

/**
 * Represents the game area where the rectangles are (6x6) grid.
 */
object GridManagerObject {
    var blockSize: PointF = PointF(1f, 1f)
    private val _moves: MutableLiveData<Int> = MutableLiveData<Int>()
    val moves: LiveData<Int>
        get() = _moves

    private val _fresh: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    val fresh: LiveData<Boolean>
        get() = _fresh

    var win: MutableLiveData<Boolean> = MutableLiveData<Boolean>()

    private var grabbed: Boolean = false
    private var grabbedPosition: PointF = PointF(0f, 0f)
    private var currentRectangle: Rectangle? = null
    private var actions: MutableList<GridCommand> =  ArrayList()

    private var rectangles: Array<Array<Rectangle?>> =
        Array(6){arrayOfNulls<Rectangle?>(6)}

    init {
        _moves.value = 0
        _fresh.value = true
        win.value = false
    }

    /**
     * Undo last action with command design pattern
     * pops last action from actions, the array of actions
     */
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

    /**
     * Cleans all actions and removes all rectangles
     */
    fun deleteActions(){
        rectangles = Array(6){arrayOfNulls<Rectangle?>(6)}
        actions.clear()
        _moves.value = 0
        _fresh.value = true
        win.value = false
    }

    /**
     * @param gridIndex index of the rectangle in the grid
     * @param dimensions dimensions of the rectangle on the grid eg. 1x3
     * @param rectangle the rectangle to add
     * @param fromUser boolean to see if the command is added by user interaction
     *
     * Creates a command from a rectangle that we want to add to the canvas and grid
     * and increments moves if the action was made by user interaction.
     */
    private fun addCommand(gridIndex: Point,
                           dimensions: Point,
                           rectangle: Rectangle?,
                           fromUser: Boolean = false) {
        val gridCommand: GridCommand = GridCommand(gridIndex, dimensions, rectangle)
        gridCommand.exec(rectangles)
        actions += gridCommand

        if(fromUser && rectangle != null) {
            _moves.value = _moves.value!!.toInt() + 1
            _fresh.value = _moves.value == 0
        }
    }

    /**
     * @param gridIndex index of the rectangle in the grid
     * @param dimensions dimensions of the rectangle on the grid eg. 1x3
     * @param stuck boolean to know if the added rectangle is the stucked one
     * @param fromUser boolean to see if the command is added by user interaction
     *
     * Creates a rectangle with the correct parameters and calls addCommand
     */
    fun addRectangle(gridIndex: Point,
                     dimensions: Point,
                     stuck: Boolean = false,
                     fromUser: Boolean = false) {
        val rectangle: Rectangle = Rectangle(gridIndex, dimensions, blockSize, stuck)
        addCommand(gridIndex, dimensions, rectangle, fromUser)
    }

    /**
     * @param touchPosition the touch position on the canvas when grabbing the rectangle
     *
     * Saves the grabbed position, gets the grid index from the touch position
     * and sets the current rectangle to the rectangle on the grid (null if no rectangle)
     */
    fun grab(touchPosition: PointF) {
        grabbedPosition.set(touchPosition)

        val gridIndex: Point = (grabbedPosition / blockSize)

        if (gridIndex.x < 6 && gridIndex.y < 6) {
            currentRectangle = rectangles[gridIndex.x][gridIndex.y]
            grabbed = currentRectangle != null
        }
    }

    /**
     * @param canvasPosition desired position on the canvas for the rectangle
     * @param gridDimensions dimensions of the rectangle on the grid
     *
     * @return the nearest gridIndex where its possible to put the rectangle
     */
    private fun snapIntoPlace(canvasPosition: PointF, gridDimensions: Point): Point {
        val indexX: Int = (canvasPosition.x / blockSize.x).roundToInt()
        val indexY: Int = (canvasPosition.y / blockSize.y).roundToInt()

        val maxX = 6 - gridDimensions.x
        val maxY = 6 - gridDimensions.y

        return Point(
            indexX.coerceIn(0..maxX),
            indexY.coerceIn(0..maxY)
        )
    }

    /**
     * @param touchPosition the touch position on the canvas when releasing the rectangle
     *
     * Removes the rectangle from its initial position in the grid. Adds the rectangle in
     * the position where released.
     */
    fun release(touchPosition: PointF) {
        if (currentRectangle == null)
            return

        val canvasPosition: PointF =
            translateRectangle(currentRectangle, touchPosition - grabbedPosition)


        val gridDimensions: Point = currentRectangle!!.gridDimensions

        val gridIndex: Point = snapIntoPlace(canvasPosition, gridDimensions)

        if(rectangles[gridIndex.x][gridIndex.y] != null &&
            rectangles[gridIndex.x][gridIndex.y]!!.gridIndex == gridIndex) {
            currentRectangle = null
            return
        }

        // Removes rectangle from its initial position (adding null rectangle)
        addCommand(currentRectangle!!.gridIndex, gridDimensions, null, fromUser = true)

        if (currentRectangle!!.stuck && gridIndex.x == 4){
            win.value = true
        }else if (currentRectangle!!.stuck){
            win.value = false
        }
        addRectangle(gridIndex, gridDimensions, fromUser = true, stuck = currentRectangle!!.stuck)

        currentRectangle = null
    }

    /**
     * @param array array of the row or columns (of the grid) where we want to find the nearest obstacle
     * @param startIndex index in the array to start the search
     * @param endIndex index in the array where to end the search
     *
     * @return index of the nearest obstacle
     *
     * Search in the positive way from startIndex till endIndex
     */
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

    /**
     * @param array array of the row or columns (of the grid) where we want to find the nearest obstacle
     * @param startIndex index in the array to start the search
     * @param endIndex index in the array where to end the search
     *
     * @return index of the nearest obstacle
     *
     * Search in the negative way from startIndex till endIndex
     */
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

    /**
     * @param matrix the matrix to extract the column from
     * @param col the column index in the matrix
     *
     * @return the column at the specified index
     *
     * Extracts a column from a matrix
     */
    private fun getColumn(matrix: Array<Array<Rectangle?>>, col: Int):  Array<Rectangle?>{
        val column = arrayOfNulls<Rectangle?>(matrix.size)

        for (i in column.indices) {
            column[i] = matrix[i][col]
        }

        return column
    }

    /**
     * @param rectangle the rectangle to translate
     * @param translation the translation vector of the rectangle
     *
     * @return the unidirectional translated position in the canvas
     *
     * Gets the correct unidirectional translation (x, 0) or (y, 0). Adds it to the current
     * position of the rectangle and checks if there are obstacles then gets the legal position.
     */
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

    /**
     * @param touchPosition current position of the finger
     * @param canvas the canvas to draw the rectangles
     *
     * Translates the currently grabbed rectangle where the finger is currently touching
     * and redraws the current rectangle to its new position.
     */
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

    /**
     * @param canvas the canvas to draw the rectangles
     *
     * Draws all rectangles
     */
    fun redrawRectangles(canvas: Canvas?) {
        for (rows in rectangles) {
            for (rectangle in rows) {
                rectangle?.draw(canvas)
            }
        }
    }

    /**
     * @param gridIndex the grid index of the rectangle to redraw elsewhere
     * @param canvasPosition the canvas position where to redraw the rectale at gridIndex
     * @param canvas the canvas where to draw the rectangles
     *
     * Draws all rectangles besides the one at the gridIndex and draws the current rectangle
     * to its new position
     */
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

    /**
     * Represents the command implementing the command design pattern for the undo
     *
     * @param gridIndex the index in the matrix where to add or remove the rectangle
     * @param dimensions the dimensions of the rectangle
     * @param rectangle the rectangle to add (null to remove)
     */
    class GridCommand(private val gridIndex: Point,
                      private val dimensions: Point,
                      val rectangle: Rectangle?) {


        /**
         * @param rectangles the matrix of rectangles where to add or remove rectangle
         *
         * Adds or removes the current rectangle (null to remove) to the matrix in all
         * in all the indexes that it takes with its dimensions
         */
        private fun addRemoveRectangle(rectangles: Array<Array<Rectangle?>>) {
            if (dimensions.x > dimensions.y) {
                val range : Int = dimensions.x - 1
                for (i in 0..range) {
                    rectangles[gridIndex.x + i][gridIndex.y] = rectangle
                }
            } else {
                val range : Int = dimensions.y - 1
                for (i in 0..range) {
                    rectangles[gridIndex.x][gridIndex.y + i] = rectangle
                }
            }
        }

        /**
         * @param rectangles the matrix of rectangles where to add or remove rectangle
         *
         * The exec function of the command pattern to add or remove a rectangle
         */
        fun exec(rectangles: Array<Array<Rectangle?>>) {
            addRemoveRectangle(rectangles)
        }
    }

    /**
     * Represents the rectangles that are added to the canvas
     *
     * @param gridIndex the current grid index of the rectangle
     * @param gridDimensions the dimensions of the rectangle on the grid
     * @param bSize the dimensions of a block in the canvas
     * @param stuck boolean to know if its the stucked rectangle
     */
    class Rectangle(val gridIndex: Point,
                    val gridDimensions: Point,
                    bSize: PointF, val stuck: Boolean) {

        private val blockSize: PointF = PointF(bSize.x, bSize.y)

        private val paint:Paint = Paint()

        init {
            paint.isFilterBitmap = true
            paint.isAntiAlias = true

            if(stuck)
                paint.color = Color.parseColor("#FFF00F00")
            else {
                paint.color = Color.parseColor("#FF3F51B5")
            }
        }

        private val canvasDimensions: PointF = gridDimensions * blockSize
        var canvasPosition: PointF = gridIndex * blockSize

        private var right: Float = canvasPosition.x + canvasDimensions.x
        private var bottom: Float = canvasPosition.y + canvasDimensions.y

        private val offset = 5

        /**
         * @param newCanvasPosition the new position where to draw the rectangle
         * @param canvas the canvas where to draw the rectangle
         *
         * Draws the rectangle to its new position while its moving
         */
        fun redraw(newCanvasPosition: PointF, canvas: Canvas?) {
            canvas?.drawRoundRect(
                newCanvasPosition.x + offset,
                newCanvasPosition.y + offset,
                newCanvasPosition.x - offset + canvasDimensions.x,
                newCanvasPosition.y + canvasDimensions.y - offset,
                50.0F, 50.0F, this.paint)
        }

        /**
         * @param canvas the canvas where to draw the rectangle
         *
         * Draws the rectangle to its position
         */
        fun draw(canvas: Canvas?) {
            //canvas?.drawRect(canvasPosition.x, canvasPosition.y, right, bottom, paint)
            canvas?.drawRoundRect(
                canvasPosition.x + offset,
                canvasPosition.y + offset,
                right - offset,
                bottom - offset,
                50.0F, 50.0F, this.paint)
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
