package com.example.somalearn.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.RoundedCornersTransformation
import com.example.somalearn.data.local.entities.CachedBook
import com.example.somalearn.databinding.BookCategoryRowLayoutBinding
import com.example.somalearn.models.Book
import com.example.somalearn.utils.BookUtils
import com.example.somalearn.utils.DiffUtilCallback


class BookCategoryAdapter(
    private val navigateToDetails: (book: Book, extras: FragmentNavigator.Extras) -> Unit
): RecyclerView.Adapter<BookCategoryAdapter.CategoryViewHolder>() {

    inner class CategoryViewHolder(
        private val binding: BookCategoryRowLayoutBinding
    ): RecyclerView.ViewHolder(binding.root) {
        fun bind(book: Book) {
            binding.authorTextView.text = book.artistName
            binding.bookCover
                .load(BookUtils.getImageURL(book.artworkUrl100)) {
                    crossfade(true)
                    transformations(RoundedCornersTransformation(BookUtils.COVER_BORDER_RADIUS))
                }
            setClickListener(book)
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

    private var books = listOf<CachedBook>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val binding = BookCategoryRowLayoutBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return CategoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val book = books[position]
        holder.bind(book.book)
    }

    override fun getItemCount(): Int {
        return books.size
    }

    fun updateBooks(newBooks: List<CachedBook>) {
        val diffUtilCallback = DiffUtilCallback(books, newBooks)
        val diffResult = DiffUtil.calculateDiff(diffUtilCallback)
        books = newBooks

        diffResult.dispatchUpdatesTo(this)
    }
}
