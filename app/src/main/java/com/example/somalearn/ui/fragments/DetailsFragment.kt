package com.example.somalearn.ui.fragments

import android.os.Bundle
import android.transition.Fade
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import coil.load
import coil.transform.BlurTransformation
import coil.transform.RoundedCornersTransformation
import com.example.somalearn.R
import com.example.somalearn.databinding.FragmentDetailsBinding
import com.example.somalearn.models.Book
import com.example.somalearn.ui.viewmodels.MainViewModel
import com.example.somalearn.utils.BookUtils
import com.example.somalearn.utils.Constants
import org.jsoup.Jsoup

class DetailsFragment : Fragment() {

    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: MainViewModel

    private lateinit var book: Book
    private var bookStatus = Constants.STATUS.NONE  // will be updated


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val fade = Fade()
        fade.duration = 300

        enterTransition = fade
        exitTransition = fade

        val transition = TransitionInflater
            .from(requireContext())
            .inflateTransition(android.R.transition.move)

        sharedElementEnterTransition = transition
        sharedElementReturnTransition = transition
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)

        viewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)

        book = arguments?.getParcelable("book")!!
        setData(book)
        setClickListeners()
        setObservers()

        binding.bookCoverBlurred.doOnLayout {
            val height = it.measuredHeight
            setScrollListener(height)
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private fun getStatusDescription(status: Int): String {
        return when(status) {
            Constants.STATUS.NONE -> getString(R.string.none)
            Constants.STATUS.COMPLETED -> getString(R.string.completed)
            Constants.STATUS.READING -> getString(R.string.reading)
            else -> getString(R.string.unknown)
        }
    }

    private fun setData(book: Book) {
        val imageUrl = BookUtils.getImageURL(book.artworkUrl100)

        binding.bookCoverBlurred
            .load(imageUrl) {
                crossfade(200)
                transformations(BlurTransformation(requireContext(), 20F))
            }
        binding.bookCover
            .load(imageUrl) {
                transformations(RoundedCornersTransformation(BookUtils.COVER_BORDER_RADIUS))
            }

        binding.bookTitle.text = book.trackName
        val author = "by ${book.artistName}"
        binding.bookAuthor.text = author

        binding.rating.text = book.averageUserRating.toString()
        binding.price.text = book.formattedPrice
        binding.status.text = getStatusDescription(bookStatus)

        binding.description.text = Jsoup.parse(book.description).text()


        binding.headerBackground
            .load(imageUrl) {
                transformations(BlurTransformation(requireContext(), 20F))
            }
        binding.headerBookTitle.text = book.trackName
    }

    private fun setClickListeners() {
        binding.fab.setOnClickListener {
            val action = DetailsFragmentDirections.actionDetailsFragmentToBottomSheetFragment(book, bookStatus)
            findNavController().navigate(action)
        }
        binding.goBackArrow.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun setObservers() {
        viewModel.reading.observe(viewLifecycleOwner) { reading ->
            val isReading = reading.any{ readingObj -> readingObj.id == book.trackId }
            if(isReading) {
                binding.status.text = getStatusDescription(Constants.STATUS.READING)
                bookStatus = Constants.STATUS.READING
            }
        }
        viewModel.completed.observe(viewLifecycleOwner) { completed ->
            val isCompleted = completed.any{ completedObj -> completedObj.id == book.trackId }
            if(isCompleted) {
                binding.status.text = getStatusDescription(Constants.STATUS.COMPLETED)
                bookStatus = Constants.STATUS.COMPLETED
            }
        }
        viewModel.latestBookRemoved.observe(viewLifecycleOwner) { latestBookRemoved ->
            if(latestBookRemoved == book.trackId) {
                binding.status.text = getStatusDescription(Constants.STATUS.NONE)
                bookStatus = Constants.STATUS.NONE
            }
        }
    }

    private fun setScrollListener(triggerY: Int) {
        if(android.os.Build.VERSION.SDK_INT < 23)
            return

        binding.header.translationY = -binding.header.height.toFloat()

        binding.scrollView.setOnScrollChangeListener { _, _, scrollY, _, _ ->
            val headerY = binding.header.translationY
            val headerHeight = binding.header.height.toFloat()

            if((scrollY >= triggerY && headerY == -headerHeight) || (scrollY < triggerY && headerY == 0F)) {
                binding.header.animate()
                    .translationY(if (scrollY >= triggerY) 0F else -headerHeight)
            }
        }
    }
}
