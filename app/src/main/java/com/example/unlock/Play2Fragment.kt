package com.example.unlock

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.unlock.databinding.FragmentPlay2Binding

/**
 * A simple [Fragment] subclass as the Play destination in the navigation.
 */
class Play2Fragment : Fragment() {

    private var _binding: FragmentPlay2Binding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentPlay2Binding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonGame2to1.setOnClickListener {
            findNavController().navigate(R.id.action_play2Fragment_to_play1Fragment)
        }

        binding.buttonGame2to3.setOnClickListener {
            findNavController().navigate(R.id.action_play2Fragment_to_play3Fragment)
        }

        binding.buttonPlay2home.setOnClickListener {
            findNavController().navigate(R.id.action_play2Fragment_to_FirstFragment)
        }

    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}