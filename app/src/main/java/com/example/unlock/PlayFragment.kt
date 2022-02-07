package com.example.unlock

import android.graphics.Color
import android.graphics.Point
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.unlock.databinding.FragmentPlayBinding

/**
 * A simple [Fragment] subclass as the Play destination in the navigation.
 */
class PlayFragment : Fragment(R.layout.fragment_play) {

    private var _binding: FragmentPlayBinding? = null
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

        _binding = FragmentPlayBinding.inflate(inflater, container, false)

        binding.viewmodel = viewModel
        binding.lifecycleOwner = this
        loadPuzzle(currentPuzzleNumber)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonNext.setOnClickListener {
            when (currentPuzzleNumber){
                1 -> loadPuzzle(2)
                2 -> loadPuzzle(3)
            }
        }

        binding.buttonPrevious.setOnClickListener {
            when (currentPuzzleNumber){
                2 -> loadPuzzle(1)
                3 -> loadPuzzle(2)
            }
        }

        binding.buttonHome.setOnClickListener {
            findNavController().navigate(R.id.action_play1Fragment_to_MenuFragment)
        }

        binding.buttonMockPlay.setOnClickListener {
            //binding.currentMoves1.text = (binding.currentMoves1.text.toString().toInt()+1).toString()
        }

        binding.buttonUndo.setOnClickListener {
            GridManagerObject.undo()
        }

        binding.buttonReset.setOnClickListener {
            GridManagerObject.deleteActions()
            loadPuzzle(currentPuzzleNumber)
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
                GridManagerObject.deleteActions()
                currentPuzzleNumber = 1
                binding.toolbarPlayTitle.text = "PUZZLE 1"
                binding.reccordMin.text = "/15"
                binding.buttonPrevious.setBackgroundColor(resources.getColor(R.color.grey))
                binding.buttonPrevious.isClickable = false
                binding.buttonNext.setBackgroundColor(resources.getColor(R.color.blue))
                binding.buttonNext.isClickable = true
                GridManagerObject.addRectangle(Point(0, 0), Point(3, 1))
                GridManagerObject.addRectangle(Point(0, 2), Point(2, 1), stuck = true)
                GridManagerObject.addRectangle(Point(0, 3), Point(1, 2))
                GridManagerObject.addRectangle(Point(0, 5), Point(3, 1))
                GridManagerObject.addRectangle(Point(2, 1), Point(1, 3))
                GridManagerObject.addRectangle(Point(5, 0), Point(1, 3))
                GridManagerObject.addRectangle(Point(4, 3), Point(2, 1))
                GridManagerObject.addRectangle(Point(4, 4), Point(1, 2))
            }
            2 -> {
                GridManagerObject.deleteActions()
                currentPuzzleNumber = 2
                binding.toolbarPlayTitle.text = "PUZZLE 2"
                binding.reccordMin.text = "/17"
                binding.buttonPrevious.setBackgroundColor(resources.getColor(R.color.blue))
                binding.buttonPrevious.isClickable = true
                binding.buttonNext.setBackgroundColor(resources.getColor(R.color.blue))
                binding.buttonNext.isClickable = true
                GridManagerObject.addRectangle(Point(0, 3), Point(2, 1))
                GridManagerObject.addRectangle(Point(0, 2), Point(2, 1), stuck = true)
                GridManagerObject.addRectangle(Point(1, 4), Point(1, 2))
                GridManagerObject.addRectangle(Point(2, 1), Point(1, 2))
                GridManagerObject.addRectangle(Point(2, 3), Point(1, 2))
                GridManagerObject.addRectangle(Point(2, 5), Point(2, 1))
                GridManagerObject.addRectangle(Point(3, 1), Point(1, 3))
                GridManagerObject.addRectangle(Point(4, 1), Point(1, 3))
            }
            3 -> {
                GridManagerObject.deleteActions()
                currentPuzzleNumber = 3
                binding.toolbarPlayTitle.text = "PUZZLE 3"
                binding.reccordMin.text = "/15"
                binding.buttonPrevious.setBackgroundColor(resources.getColor(R.color.blue))
                binding.buttonPrevious.isClickable = true
                binding.buttonNext.setBackgroundColor(resources.getColor(R.color.grey))
                binding.buttonNext.isClickable = false
                GridManagerObject.addRectangle(Point(0, 0), Point(1, 2))
                GridManagerObject.addRectangle(Point(0, 2), Point(2, 1), stuck = true)
                GridManagerObject.addRectangle(Point(0, 4), Point(3, 1))
                GridManagerObject.addRectangle(Point(1, 0), Point(2, 1))
                GridManagerObject.addRectangle(Point(2, 1), Point(1, 2))
                GridManagerObject.addRectangle(Point(3, 0), Point(2, 1))
                GridManagerObject.addRectangle(Point(3, 2), Point(1, 3))
                GridManagerObject.addRectangle(Point(4, 2), Point(1, 3))
            }
        }
    }
}