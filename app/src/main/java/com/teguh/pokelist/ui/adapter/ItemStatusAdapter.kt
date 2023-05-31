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
import com.teguh.pokelist.data.model.Stats
import com.teguh.pokelist.databinding.ItemBaseStatBinding
import com.teguh.pokelist.utils.Functions
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL

class ItemStatusAdapter(private val listener: ListItemListener) :
        RecyclerView.Adapter<ItemStatusAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemBaseStatBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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
        fun getEntries(): List<Stats>
    }

    inner class ViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        private val binding = ItemBaseStatBinding.bind(view)

        fun populate(item: Stats) {

            val context = view.context
            binding.name.text = item.stat?.name?:""
            binding.value.text = item.baseStat.toString()
        }

    }
}