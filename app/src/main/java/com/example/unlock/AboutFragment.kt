package com.example.unlock

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.unlock.databinding.FragmentAboutBinding

/**
 * AboutFragment class:
 * controls the about fragment view
 * adds event listeners to buttons on its layout
 */
class AboutFragment : Fragment() {

    private var _binding: FragmentAboutBinding? = null
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

      _binding = FragmentAboutBinding.inflate(inflater, container, false)
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

        binding.buttonAboutTohome.setOnClickListener {
            findNavController().navigate(R.id.action_AboutFragment_to_MenuFragment)
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