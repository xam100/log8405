package com.example.unlock

import android.graphics.Point
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.unlock.databinding.FragmentPlay1Binding


/**
 * A simple [Fragment] subclass as the Play destination in the navigation.
 */
class PlayFragment : Fragment(R.layout.fragment_play1) {

    private var _binding: FragmentPlay1Binding? = null
    private val viewModel: MainViewModel by viewModels()
    private var currentPuzzleNumber: Int = 1

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

        binding.viewmodel = viewModel
        binding.lifecycleOwner = this
        return binding.root

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonGame1to2.setOnClickListener {
            //todo
        }

        binding.buttonPlay1home.setOnClickListener {
            findNavController().navigate(R.id.action_play1Fragment_to_MenuFragment)
        }

        binding.buttonMockPlay.setOnClickListener {
            //binding.currentMoves1.text = (binding.currentMoves1.text.toString().toInt()+1).toString()
        }

        binding.buttonUndo1.setOnClickListener {
            GridManagerObject.undo()
        }

        binding.buttonReset1.setOnClickListener {
            GridManagerObject.deleteActions()
            loadPuzzle(1)
        }

        binding.buttonMockSetRecord.setOnClickListener {
            //todo add condition to check that the the number of current moves is less that the current record and that it is indeed a record
            binding.currentReccord1.text = binding.currentMoves1.text.toString()
        }

    }



    override fun onDestroyView() {
        super.onDestroyView()
        GridManagerObject.deleteActions()
        _binding = null
    }

    private fun loadPuzzle(puzzleNumber: Int){
        when (puzzleNumber) {
            1 -> {
                currentPuzzleNumber = 1
                GridManagerObject.addRectangle(Point(0, 0), Point(3, 1))
                GridManagerObject.addRectangle(Point(0, 2), Point(2, 1), stuck = true)
                GridManagerObject.addRectangle(Point(0, 3), Point(1, 2))
                GridManagerObject.addRectangle(Point(0, 5), Point(3, 1))
                GridManagerObject.addRectangle(Point(2, 1), Point(1, 3))
                GridManagerObject.addRectangle(Point(5, 0), Point(1, 3))
                GridManagerObject.addRectangle(Point(4, 3), Point(2, 1))
                GridManagerObject.addRectangle(Point(4, 4), Point(1, 2))
            }
        }
    }
}