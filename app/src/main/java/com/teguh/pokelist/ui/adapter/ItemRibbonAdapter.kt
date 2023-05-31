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
import java.lang.Exception
import java.util.*
import android.graphics.BitmapFactory
import androidx.palette.graphics.Palette
import com.teguh.pokelist.data.model.Types
import com.teguh.pokelist.databinding.ItemRibbonBinding
import com.teguh.pokelist.utils.Functions
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL

class ItemRibbonAdapter(private val listener: ListItemListener) :
        RecyclerView.Adapter<ItemRibbonAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemRibbonBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if(listener.getEntriesRibbon().size > position) {
            val item = listener.getEntriesRibbon()[position]
            holder.populate(item)
        }
    }

    override fun getItemCount(): Int = listener.getEntriesRibbon().size

    interface ListItemListener {
        fun getEntriesRibbon(): List<Types>
    }

    inner class ViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        private val binding = ItemRibbonBinding.bind(view)

        fun populate(item: Types) {

            val context = view.context

            binding.name.text = item.type?.name

        }

    }
}