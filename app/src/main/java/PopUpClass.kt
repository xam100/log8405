package com.example.unlock

import android.content.Context
import android.media.AudioManager
import android.media.MediaPlayer
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import java.io.IOException


class Success {
    //PopupWindow display method
    private var mediaPlayer : MediaPlayer ? = null

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
        val test2 = popupView.findViewById<TextView>(R.id.successtitle)
        test2.setText(R.string.puzzlesuccess)
        val buttonEdit = popupView.findViewById<Button>(R.id.btnSoundTest)
        buttonEdit.setOnClickListener { //As an example, display the message
            playAudio(view)

        }

        //Handler for clicking on the inactive zone of the window
        popupView.setOnTouchListener { v, event -> //Close the window when clicked
            popupWindow.dismiss()
            true
        }
    }

    private fun playAudio(view: View) {
        val audioUrl = "https://www.bensound.com/bensound-music/bensound-ukulele.mp3"
        mediaPlayer = MediaPlayer()
        mediaPlayer!!.setAudioStreamType(AudioManager.STREAM_MUSIC)

        try{
            mediaPlayer!!.setDataSource(audioUrl)
            mediaPlayer!!.prepare()
            mediaPlayer!!.start()

        } catch(e: IOException) {
            e.printStackTrace()
        }

        Toast.makeText(view.context, "Audio started playing", Toast.LENGTH_SHORT).show()
    }
}