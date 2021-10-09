package com.plentastudio.colorgame.ui

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.plentastudio.colorgame.R

class ColourGameFragment : Fragment() {

    companion object {
        fun newInstance() = ColourGameFragment()
    }

    private lateinit var viewModel: ColourGameViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.colour_game_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ColourGameViewModel::class.java)
        // TODO: Use the ViewModel
    }

}