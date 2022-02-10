package com.example.unlock

import android.content.Context
import android.graphics.Point
import android.os.Bundle
import android.os.CountDownTimer
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.unlock.com.example.unlock.Success
import com.example.unlock.databinding.FragmentPlayBinding

/**
 * A simple [Fragment] subclass as the Play destination in the navigation.
 */
class PlayFragment : Fragment(R.layout.fragment_play) {

    private var _binding: FragmentPlayBinding? = null
    private val viewModel: MainViewModel by viewModels()
    private var currentPuzzleNumber: Int = 1
    private var record1: Int = 1000
    private var record2: Int = 1000
    private var record3: Int = 1000


    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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

        binding.viewmodel?.win?.observe(viewLifecycleOwner, Observer {
            if (it) {
                println("it value:" + it)
                val popUpClass = Success()
                popUpClass.showPopupWindow(view)
                saveRecord(currentPuzzleNumber)
                val timer = object: CountDownTimer(3000, 1000) {
                    override fun onTick(p0: Long) {}

                    override fun onFinish() {
                        when (currentPuzzleNumber){
                            1 -> loadPuzzle(2)
                            2 -> loadPuzzle(3)
                            3 -> {
                                GridManagerObject.deleteActions()
                                loadPuzzle(3)
                            }
                        }
                    }
                }.start()
            }
        })

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

        binding.buttonUndo.setOnClickListener {
            GridManagerObject.undo()
        }

        binding.buttonReset.setOnClickListener {
            GridManagerObject.deleteActions()
            loadPuzzle(currentPuzzleNumber)
        }


    }

    override fun onDestroyView() {
        super.onDestroyView()
        GridManagerObject.deleteActions()
        _binding = null
    }

    private fun saveRecord(puzzleNumber: Int){
        when (puzzleNumber){
            1-> {
                val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE)
                record1 = sharedPref!!.getInt(getString(R.string.record1_value_key), 1000)
                if (GridManagerObject.moves.value!!.toInt() < record1){
                    record1 = GridManagerObject.moves.value!!.toInt() + 1
                    binding.currentRecord.text = record1.toString()
                    val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE)
                    with (sharedPref!!.edit()) {
                        putInt(getString(R.string.record1_value_key), record1)
                        apply()
                    }
                }
            }
            2-> {
                val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE)
                record2 = sharedPref!!.getInt(getString(R.string.record2_value_key), 1000)
                if (GridManagerObject.moves.value!!.toInt() < record2){
                    record2 = GridManagerObject.moves.value!!.toInt() + 1
                    binding.currentRecord.text = record2.toString()
                    val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE)
                    with (sharedPref!!.edit()) {
                        putInt(getString(R.string.record2_value_key), record2)
                        apply()
                    }
                }
            }
            3-> {
                val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE)
                record3 = sharedPref!!.getInt(getString(R.string.record3_value_key), 1000)
                if (GridManagerObject.moves.value!!.toInt() < record3){
                    record3 = GridManagerObject.moves.value!!.toInt() + 1
                    binding.currentRecord.text = record3.toString()
                    val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE)
                    with (sharedPref!!.edit()) {
                        putInt(getString(R.string.record3_value_key), record3)
                        apply()
                    }
                }
            }
        }

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

                val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return
                if (sharedPref.getInt(getString(R.string.record1_value_key), 1000) == 1000){
                    binding.currentRecord.text = "--"
                }
                else{
                    binding.currentRecord.text = sharedPref.getInt(getString(R.string.record1_value_key), 1000).toString()
                }
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

                val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return
                if (sharedPref.getInt(getString(R.string.record2_value_key), 1000) == 1000){
                    binding.currentRecord.text = "--"
                }
                else{
                    binding.currentRecord.text = sharedPref.getInt(getString(R.string.record2_value_key), 1000).toString()
                }
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

                val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return
                if (sharedPref.getInt(getString(R.string.record3_value_key), 1000) == 1000){
                    binding.currentRecord.text = "--"
                }
                else{
                    binding.currentRecord.text = sharedPref.getInt(getString(R.string.record3_value_key), 1000).toString()
                }
            }
        }
    }
}