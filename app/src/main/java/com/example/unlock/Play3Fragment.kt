package com.example.unlock

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.unlock.databinding.FragmentPlay3Binding

/**
 * A simple [Fragment] subclass as the Play destination in the navigation.
 */
class Play3Fragment : Fragment() {

    private var _binding: FragmentPlay3Binding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentPlay3Binding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonGame3to2.setOnClickListener {
            findNavController().navigate(R.id.action_play3Fragment_to_play2Fragment)
        }

        binding.buttonGame3home.setOnClickListener {
            findNavController().navigate(R.id.action_play3Fragment_to_MenuFragment)
        }

    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}