package com.example.to_dolist.ui.changeDealScreen

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.example.to_dolist.R
import com.example.to_dolist.ui.allDealsScreen.AllDealsFragmentDirections

class ChangeDealFragment : Fragment() {

    private val args: ChangeDealFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_change_deal, container, false)
    }

}