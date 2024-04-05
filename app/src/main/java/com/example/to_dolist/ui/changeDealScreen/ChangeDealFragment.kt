package com.example.to_dolist.ui.changeDealScreen

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.example.to_dolist.databinding.FragmentChangeDealBinding

class ChangeDealFragment : Fragment() {

    private val args: ChangeDealFragmentArgs by navArgs()

    private val viewModel: ChangeDealViewModel by viewModels { ChangeDealViewModel.Factory }

    private var _binding: FragmentChangeDealBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentChangeDealBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            whatToDo.setText(viewModel.deal.value?.deal ?: "")
        }
    }

}