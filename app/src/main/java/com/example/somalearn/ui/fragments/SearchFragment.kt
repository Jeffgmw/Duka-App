package com.example.somalearn.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.somalearn.adapters.SearchResultsAdapter
import com.example.somalearn.databinding.FragmentSearchBinding
import com.example.somalearn.models.Book
import com.example.somalearn.models.NetworkResult
import com.example.somalearn.ui.viewmodels.MainViewModel

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: MainViewModel
    private lateinit var adapter: SearchResultsAdapter


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)

        viewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)

        setupRecyclerView()
        setObservers()
        setupSearchView()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        postponeEnterTransition()
        binding.searchResultsRecyclerView.viewTreeObserver
            .addOnPreDrawListener {
                startPostponedEnterTransition()
                true
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupSearchView() {
        binding.searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if(query != null && query.trim().isNotEmpty())
                    viewModel.listBooks(query)
                return true
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
        })
    }

    private fun navigateToDetails(book: Book, extras: FragmentNavigator.Extras) {
        val action = SearchFragmentDirections.actionSearchFragmentToDetailsFragment(book)
        findNavController().navigate(action, extras)
    }

    private fun setupRecyclerView() {
        binding.searchResultsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = SearchResultsAdapter(::navigateToDetails)
        binding.searchResultsRecyclerView.adapter = adapter
    }


    private fun setObservers() {
        viewModel.books.observe(viewLifecycleOwner) { networkResult ->
            binding.hintWrapper.visibility = View.GONE

            binding.searchResultsRecyclerView.visibility =
                if (networkResult is NetworkResult.Success) View.VISIBLE else View.GONE
            binding.errorWrapper.visibility =
                if (networkResult is NetworkResult.Failure) View.VISIBLE else View.GONE
            binding.loadingProgressBar.visibility =
                if (networkResult is NetworkResult.Loading) View.VISIBLE else View.GONE

            if (networkResult is NetworkResult.Success) {
                adapter.updateData(networkResult.data!!)
            }
            if (networkResult is NetworkResult.Failure) {
                binding.errorTextView.text = networkResult.error
            }
        }
    }
}
