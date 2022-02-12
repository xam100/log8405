package com.example.unlock

import android.content.Context
import android.media.MediaPlayer
import android.os.CountDownTimer
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.*

/**
 * PopUp class used to contain popup window parameters and routines
 * Used in play fragment to show a success window
 */
class PopUp {

    /**
     * On a given view, shows the popup window with preconfigured parameters:
     * layout.xml
     * animation
     * duration
     * sound
     *
     * @param view View on which the popup window will be displayed
     */
    fun showPopupWindow(view: View) {

        val inflater =
            view.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val popupView: View = inflater.inflate(R.layout.fragment_success, null)


        val width = LinearLayout.LayoutParams.MATCH_PARENT
        val height = LinearLayout.LayoutParams.MATCH_PARENT
        val popupWindow = PopupWindow(popupView, width, height, true)

        popupWindow.animationStyle = R.style.Animation
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0)

        val music: MediaPlayer = MediaPlayer.create(view.context, R.raw.impressive)
        music.start()

        val timer = object: CountDownTimer(3000, 1000) {
            override fun onTick(p0: Long) {}
            override fun onFinish() {
                popupWindow.dismiss()
            }
        }.start()
    }

}