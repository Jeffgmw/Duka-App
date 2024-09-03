package com.example.somalearn.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.somalearn.adapters.BookCategoryAdapter
import com.example.somalearn.databinding.FragmentBooksBinding
import com.example.somalearn.models.Book
import com.example.somalearn.ui.viewmodels.MainViewModel
import java.util.Calendar

class BooksFragment : Fragment() {

    private var _binding: FragmentBooksBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: MainViewModel

    private lateinit var readingAdapter : BookCategoryAdapter
    private lateinit var completedAdapter: BookCategoryAdapter


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBooksBinding.inflate(inflater, container, false)

        setGreetings()
        viewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)

        setupRecyclerViews()
        setObservers()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        postponeEnterTransition()

        binding.root.viewTreeObserver
            .addOnDrawListener {
                startPostponedEnterTransition()
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private fun setGreetings() {
        val now = Calendar.getInstance()
        val greetings =  when (now.get(Calendar.HOUR_OF_DAY)) {
            in 5..11 -> "Good Morning"
            in 12..16 -> "Good Afternoon"
            else -> "Good Evening"
        }
        binding.greetings.text = greetings
    }

    private fun navigateToDetails(book: Book, extras: FragmentNavigator.Extras) {
        val action = BooksFragmentDirections.actionBooksFragmentToDetailsFragment(book)
        findNavController().navigate(action, extras)
    }

    private fun setupRecyclerViews() {
        binding.readingRecyclerView.layoutManager = LinearLayoutManager(
            requireContext(),
            RecyclerView.HORIZONTAL,
            false
        )
        readingAdapter = BookCategoryAdapter(::navigateToDetails)
        binding.readingRecyclerView.adapter = readingAdapter

        binding.completedRecyclerView.layoutManager = LinearLayoutManager(
            requireContext(),
            RecyclerView.HORIZONTAL,
            false
        )
        completedAdapter = BookCategoryAdapter(::navigateToDetails)
        binding.completedRecyclerView.adapter = completedAdapter
    }

    private fun setObservers() {
        viewModel.reading.observe(viewLifecycleOwner) {
            readingAdapter.updateBooks(it)
            binding.readingCount.text = it.size.toString()

            val isEmpty = it.isNullOrEmpty()
            binding.readingEmpty.visibility = if (isEmpty) View.VISIBLE else View.GONE
            binding.readingRecyclerView.visibility = if (isEmpty) View.GONE else View.VISIBLE
        }

        viewModel.completed.observe(viewLifecycleOwner) {
            completedAdapter.updateBooks(it)
            binding.completedCount.text = it.size.toString()

            val isEmpty = it.isNullOrEmpty()
            binding.completedEmpty.visibility = if (isEmpty) View.VISIBLE else View.GONE
            binding.completedRecyclerView.visibility = if (isEmpty) View.GONE else View.VISIBLE
        }
    }
}