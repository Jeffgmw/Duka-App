package com.example.somalearn.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.RoundedCornersTransformation
import com.example.somalearn.databinding.SearchResultRowLayoutBinding
import com.example.somalearn.models.Book
import com.example.somalearn.utils.BookUtils

class SearchResultsAdapter(
    private val navigateToDetails: (book: Book, extras: FragmentNavigator.Extras) -> Unit
): RecyclerView.Adapter<SearchResultsAdapter.ResultViewHolder>() {

    inner class ResultViewHolder(
        private val binding: SearchResultRowLayoutBinding
    ): RecyclerView.ViewHolder(binding.root) {
        fun bind(book: Book) {
            setClickListener(book)

            binding.bookTitle.text = book.trackName
            binding.authorTextView.text = book.artistName

            val imageUrl = BookUtils.getImageURL(book.artworkUrl100)

            binding.bookCover
                .load(imageUrl) {
                    crossfade(300)
                    transformations(RoundedCornersTransformation(BookUtils.COVER_BORDER_RADIUS))
                }
        }

        private fun setClickListener(book: Book) {
            ViewCompat.setTransitionName(binding.bookCover, book.trackId.toString())
            val extras = FragmentNavigatorExtras(
                binding.bookCover to "details_book_cover"
            )
            binding.rowLayoutContainer.setOnClickListener {
                navigateToDetails(book, extras)
            }
        }
    }

    private var books = listOf<Book>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultViewHolder {
        val binding = SearchResultRowLayoutBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ResultViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ResultViewHolder, position: Int) {
        holder.bind(books[position])
    }

    override fun getItemCount(): Int {
        return books.size
    }


    fun updateData(newBooks: List<Book>) {
        books = newBooks
        notifyDataSetChanged()
    }
}
