package com.example.unlock

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.unlock.databinding.ActivityMainBinding

/**
 * MainActivity class used as the entrypoint of the app
 */
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    /**
     * Sets the main view of the app
     *
     * @param savedInstanceState bundle
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}