package com.teguh.pokelist.ui.adapter

import android.graphics.Bitmap
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import com.teguh.pokelist.data.model.Results
import com.teguh.pokelist.databinding.ItemPokemonBinding
import java.lang.Exception
import java.util.*
import android.graphics.BitmapFactory
import androidx.palette.graphics.Palette
import com.teguh.pokelist.data.model.PokemonListLocal
import com.teguh.pokelist.utils.Functions
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL

class ItemMyPokemonAdapter(private val listener: ListItemListener) :
        RecyclerView.Adapter<ItemMyPokemonAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemPokemonBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if(listener.getEntries().size > position) {
            val item = listener.getEntries()[position]
            holder.populate(item)
        }
    }

    override fun getItemCount(): Int = listener.getEntries().size

    interface ListItemListener {
        fun onItemClick(name: String)
        fun onDeleteItemClick(id: String)
        fun getEntries(): List<PokemonListLocal>
    }

    inner class ViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        private val binding = ItemPokemonBinding.bind(view)

        fun populate(item: PokemonListLocal) {

            val context = view.context
            val progressView : ProgressBar = binding.progressImage
            val index = item.id
            val url = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/$index.png"

            binding.imageDelete.visibility = View.VISIBLE
            binding.name.text = item.name?:""
            binding.no.text = "Nick Name: ${item.nickName}"
            binding.cardView.setOnClickListener(){
                listener.onItemClick(item.name?:"")
            }
            binding.imageDelete.setOnClickListener(){
                listener.onDeleteItemClick(item.id?:"")
            }
            Picasso.get()
                .load(url)
                .into(binding.image, object : Callback {
                    override fun onSuccess() {
                        progressView.visibility = View.GONE
                    }

                    override fun onError(e: Exception?) {

                    }

                })
            Functions().getColorPaletteFromUrl(url) { palette ->
                // Handle the extracted palette
                if (palette != null) {
                    val dominantSwatch = palette.dominantSwatch

                    val dominantColor = dominantSwatch?.rgb ?: Color.TRANSPARENT

                    // Use the colors as needed in your application
                    // For example, set the background color of a view
                    binding.cardView.setCardBackgroundColor(dominantColor)
                } else {
                    // Error handling when palette extraction fails
                }
            }
        }

    }
}