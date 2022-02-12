package com.example.unlock.com.example.unlock

import android.content.Context
import android.media.AudioManager
import android.media.MediaPlayer
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import com.example.unlock.R
import java.io.IOException


class Success {
    //PopupWindow display method
    private var mp : MediaPlayer ? = null;

    fun showPopupWindow(view: View) {

        //Create a View object yourself through inflater
        val inflater =
            view.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val popupView: View = inflater.inflate(R.layout.fragment_success, null)

        //Specify the length and width through constants
        val width = LinearLayout.LayoutParams.MATCH_PARENT
        val height = LinearLayout.LayoutParams.MATCH_PARENT

        //Make Inactive Items Outside Of PopupWindow
        val focusable = true

        //Create a window with our parameters
        val popupWindow = PopupWindow(popupView, width, height, focusable)

        //Set the location of the window on the screen
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0)

        //Initialize the elements of our window, install the handler
        val popupMessage = popupView.findViewById<TextView>(R.id.successtitle)
        popupMessage.setText(R.string.puzzlesuccess)
        val playButton = popupView.findViewById<Button>(R.id.btnPlaySound)
        playButton.setOnClickListener { //As an example, display the message
            playAudio(view)
        }
        val pauseButton = popupView.findViewById<Button>(R.id.btnPauseSound)
        pauseButton.setOnClickListener { //As an example, display the message
            pauseAudio(view)
        }
        //Handler for clicking on the inactive zone of the window
        popupView.setOnTouchListener { v, event -> //Close the window when clicked
            popupWindow.dismiss()
            true
        }
    }

    private fun playAudio(view: View) {
        // val audioUrl = "https://www.bensound.com/bensound-music/bensound-ukulele.mp3"
        // mp = MediaPlayer()
        // mp!!.setAudioStreamType(AudioManager.STREAM_MUSIC)

        try{
            // mp!!.setDataSource(R.raw.crackkid)
            // mediaPlayer!!.setDataSource(audioUrl)
            if (mp == null) {
                val mp = MediaPlayer.create(view.context, R.raw.crackkid)
                // mp.prepare()
                mp.start()
            }
        } catch(e: IOException) {
            e.printStackTrace()
        }

        Toast.makeText(view.context, "Audio started playing", Toast.LENGTH_SHORT).show()
    }

    private fun pauseAudio(view:View) {
        try {
            if (mp != null) {
                mp!!.stop()
                mp!!.reset()
            }
        } catch(e: IOException) {
            e.printStackTrace()
        }
        Toast.makeText(view.context, "Audio stopped", Toast.LENGTH_SHORT).show()
    }

    private fun closePopup() {
        // popupWindow.dismiss
    }

}