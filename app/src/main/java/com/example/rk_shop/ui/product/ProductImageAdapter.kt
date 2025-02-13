package com.example.rk_shop.ui.product

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.rk_shop.databinding.ItemProductImageBinding
import com.github.chrisbanes.photoview.PhotoView

class ProductImageAdapter : RecyclerView.Adapter<ProductImageAdapter.ImageViewHolder>() {

    private val images = mutableListOf<String>()
    private var onImageClickListener: ((Int) -> Unit)? = null

    fun setImages(newImages: List<String>) {
        images.clear()
        images.addAll(newImages)
        notifyDataSetChanged()
    }

    fun setOnImageClickListener(listener: (Int) -> Unit) {
        onImageClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val binding = ItemProductImageBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ImageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.bind(images[position], position)
    }

    override fun getItemCount(): Int = images.size

    inner class ImageViewHolder(
        private val binding: ItemProductImageBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.productImage.setOnClickListener {
                onImageClickListener?.invoke(bindingAdapterPosition)
            }

            // Configure PhotoView
            binding.productImage.apply {
                // Enable zooming
                maximumScale = 3f
                mediumScale = 2f
                minimumScale = 1f

                // Set scale type
                scaleType = PhotoView.ScaleType.FIT_CENTER

                // Reset zoom when image changes
                setOnViewTapListener { _, _, _ ->
                    if (scale != 1f) {
                        setScale(1f, true)
                    } else {
                        onImageClickListener?.invoke(bindingAdapterPosition)
                    }
                }
            }
        }

        fun bind(imageUrl: String, position: Int) {
            // Reset zoom level
            binding.productImage.setScale(1f, false)

            // Load image with animation
            Glide.with(itemView.context)
                .load(imageUrl)
                .thumbnail(0.1f)
                .into(binding.productImage)
        }
    }
}
