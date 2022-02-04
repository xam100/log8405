package com.example.unlock

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.unlock.databinding.FragmentMenuBinding

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class MenuFragment : Fragment() {

private var _binding: FragmentMenuBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

      _binding = FragmentMenuBinding.inflate(inflater, container, false)
      return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonPopup.setOnClickListener{
            val popUpClass = Success()
            popUpClass.showPopupWindow(view)
        }

        binding.buttonAbout.setOnClickListener {
            findNavController().navigate(R.id.action_MenuFragment_to_AboutFragment)
        }

        binding.buttonPlay.setOnClickListener {
            findNavController().navigate(R.id.action_MenuFragment_to_play1Fragment)
        }

        binding.buttonExit.setOnClickListener {
            System.exit(0)
        }
    }

override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}