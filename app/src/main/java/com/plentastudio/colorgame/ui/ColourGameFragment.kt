package com.plentastudio.colorgame.ui

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import com.plentastudio.colorgame.adapter.ColorGameListener
import com.plentastudio.colorgame.adapter.ColourGameAdapter
import com.plentastudio.colorgame.databinding.ColourGameFragmentBinding
import androidx.appcompat.app.AppCompatActivity
import com.plentastudio.colorgame.entity.ColourBox
import androidx.recyclerview.widget.SimpleItemAnimator
import org.apache.commons.lang3.BooleanUtils


class ColourGameFragment : Fragment() {
    private val TAG = ColourGameFragment::class.java.simpleName

    private lateinit var viewModel: ColourGameViewModel
    private val binding by lazy { ColourGameFragmentBinding.inflate(layoutInflater) }
    private var colourGameAdapter: ColourGameAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (requireActivity() as AppCompatActivity?)!!.supportActionBar!!.hide()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel = ViewModelProvider(this).get(ColourGameViewModel::class.java)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        setUpRecyclerView()

        viewModel.colours.observe(viewLifecycleOwner, {
            colourGameAdapter?.addHeaderAndColorItem(it, BooleanUtils.toBoolean(viewModel.isCorrect.value))
            Log.d("ColourGameViewModel", "Observer")
            Log.d("ColourGameViewModel", "Observer => $it")
        })
    }

    private fun setUpRecyclerView() = with(binding.rvColorGame) {
        colourGameAdapter = ColourGameAdapter(ColorGameListener { colorId ->
            if (BooleanUtils.toBoolean(viewModel.isCorrect.value)) return@ColorGameListener
            Log.d(TAG, colorId.toString())
            if (colorId == viewModel.selectedColor.value?.id) {
                viewModel._isCorrect.value = true
                viewModel.modifiedColors()
                Toast.makeText(requireContext(), "Correct!!", Toast.LENGTH_SHORT).show() //Get the right context
                return@ColorGameListener
            }
            viewModel._isCorrect.value = false
            itemAnimator?.changeDuration = 0
            (itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
            Toast.makeText(requireContext(), "InCorrect!!", Toast.LENGTH_SHORT).show()
        })
        colourGameAdapter?.addHeaderAndColorItem(viewModel.colours.value)
        colourGameAdapter?.appActivity = requireActivity()
        colourGameAdapter?.viewModel = viewModel
        colourGameAdapter?.lifecycle = viewLifecycleOwner
        val manager = GridLayoutManager(activity, 2)
        manager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int) = when (position) {
                0 -> 2
                else -> 1
            }

        }
        adapter = colourGameAdapter
        layoutManager = manager
    }
}