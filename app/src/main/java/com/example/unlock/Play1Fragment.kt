package com.example.unlock

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.unlock.databinding.FragmentPlay1Binding


/**
 * A simple [Fragment] subclass as the Play destination in the navigation.
 */
class Play1Fragment : Fragment() {

    private var _binding: FragmentPlay1Binding? = null
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

        _binding = FragmentPlay1Binding.inflate(inflater, container, false)
        return binding.root

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonGame1to2.setOnClickListener {
            findNavController().navigate(R.id.action_play1Fragment_to_play2Fragment)
        }

        binding.buttonPlay1home.setOnClickListener {
            findNavController().navigate(R.id.action_play1Fragment_to_MenuFragment)
        }

        binding.buttonMockPlay.setOnClickListener {
            binding.currentMoves1.text = (binding.currentMoves1.text.toString().toInt()+1).toString()
        }

        binding.buttonUndo1.setOnClickListener {
            GridManagerObject.undo()
        }

        binding.buttonReset1.setOnClickListener {
            binding.currentMoves1.text = (0).toString()
        }

        binding.buttonMockSetRecord.setOnClickListener {
            //todo add condition to check that the the number of current moves is less that the current record and that it is indeed a record
            binding.currentReccord1.text = binding.currentMoves1.text.toString()
        }

    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}