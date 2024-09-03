package com.example.somalearn.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.somalearn.R
import com.example.somalearn.data.local.entities.CachedBook
import com.example.somalearn.databinding.FragmentBottomSheetBinding
import com.example.somalearn.models.Book
import com.example.somalearn.ui.viewmodels.MainViewModel
import com.example.somalearn.utils.Constants
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomSheetFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentBottomSheetBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: MainViewModel

    private lateinit var book: Book
    private var bookStatus = Constants.STATUS.NONE


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBottomSheetBinding.inflate(inflater, container, false)

        viewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)

        book = arguments?.getParcelable("book")!!
        bookStatus = arguments?.getInt("bookStatus")!!

        setupChips()
        setButtonsListeners()

        return binding.root
    }


    private fun setupChips() {
        when(bookStatus) {
            Constants.STATUS.READING -> binding.readingChip.isChecked = true
            Constants.STATUS.COMPLETED -> binding.completedChip.isChecked = true
        }

        binding.chipGroup.setOnCheckedChangeListener { _, checkedId ->
            bookStatus = when(checkedId) {
                binding.readingChip.id -> Constants.STATUS.READING
                binding.completedChip.id -> Constants.STATUS.COMPLETED
                else -> Constants.STATUS.NONE
            }
        }
    }


    private fun navigateUp() {
        findNavController().navigateUp()
    }


    private fun setButtonsListeners() {
        binding.doneButton.setOnClickListener {
            if(bookStatus == Constants.STATUS.NONE) {
                navigateUp()
                return@setOnClickListener
            }

            val newBook = when(bookStatus) {
                Constants.STATUS.READING -> CachedBook(book.trackId, Constants.STATUS.READING, book)
                else -> CachedBook(book.trackId, Constants.STATUS.COMPLETED, book)
            }
            viewModel.insertBook(newBook)
            navigateUp()
        }

        binding.removeButton.setOnClickListener {
            if(bookStatus == Constants.STATUS.NONE) {
                navigateUp()
                return@setOnClickListener
            }

            viewModel.removeBook(book.trackId)
            navigateUp()
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
