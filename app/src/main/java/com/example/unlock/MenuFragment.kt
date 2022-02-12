package com.example.unlock

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.unlock.databinding.FragmentMenuBinding


/**
 * MenuFragment class:
 * controls the about fragment view
 * adds event listeners to buttons on its layout
 */
class MenuFragment : Fragment() {

    private var _binding: FragmentMenuBinding? = null
    private val binding get() = _binding!!

    /**
     * Instantiates the view and binding.
     *
     * @param inflater inflates the current layout
     * @param container container of the inflated layout
     * @param savedInstanceState bundle
     * @return created view
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

      _binding = FragmentMenuBinding.inflate(inflater, container, false)
      return binding.root

    }

    /**
     * Binds all pertinent view elements to listeners or observers.
     *
     * @param view view of the fragment
     * @param savedInstanceState bundle
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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

    /**
     * Clears the view and binding.
     */
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        }
    }