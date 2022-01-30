package com.example.unlock

import android.content.Context
import android.graphics.*
import android.os.Bundle
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.unlock.databinding.FragmentBoardBinding
import com.example.unlock.databinding.FragmentPlay1Binding

//data class Position (var x: Float, var y: Float)

class BoardFragment : Fragment() {
    private var _binding: FragmentBoardBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentBoardBinding.inflate(inflater, container, false)
        return binding.root

    }

    class Drawer(con: Context?, attr: AttributeSet?) : View(con, attr) {
        private val paint:Paint = Paint()
        init {
            paint.isFilterBitmap = true
            paint.isAntiAlias = true
            paint.color = Color.parseColor("#FF3F51B5")
        }
        private val gridManager: GridManager = GridManager(PointF(1f, 1f))

        private var touchDown: Boolean = false
        private var position: PointF = PointF(0f, 0f)


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

        override fun draw(canvas: Canvas?) {
            super.draw(canvas)

            canvas?.drawColor(Color.GRAY)

            if (touchDown)
                gridManager.moveTo(position, canvas, paint)
            else
                gridManager.redrawRectangles(canvas, paint)
        }

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
}
