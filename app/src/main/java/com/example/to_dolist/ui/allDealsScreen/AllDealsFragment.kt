package com.example.to_dolist.ui.allDealsScreen

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.to_dolist.databinding.FragmentAllDealsBinding

class AllDealsFragment : Fragment() {

    private var _binding: FragmentAllDealsBinding? = null
    private val binding get() = _binding!!

    private var _adapter: AllDealsAdapter? = null
    private val adapter get() = _adapter!!

    private val viewModel: AllDealsViewModel by viewModels { AllDealsViewModel.Factory }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAllDealsBinding.inflate(inflater, container, false)

        _adapter = createAdapter()
        viewModel.deals.observe(viewLifecycleOwner) { deals ->
            adapter.deals = deals
        }
        binding.recyclerView.adapter = adapter
        binding.recyclerView.setOnScrollChangeListener { _, _, _, y1, y2 ->
            if (y1 > y2) binding.addButton.hide()
            else binding.addButton.show()
        }


        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        _adapter = null
    }

    private fun createAdapter(): AllDealsAdapter {
        return AllDealsAdapter()
    }
}