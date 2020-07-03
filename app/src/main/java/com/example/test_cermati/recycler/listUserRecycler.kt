package com.example.test_cermati.recycler

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.test_cermati.R
import kotlinx.android.synthetic.main.list_users.view.*

 class listUserRecycler(val context: Context, private val listViewType: List<Item>) : RecyclerView.Adapter<listUserRecycler.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return  ViewHolderItem(inflater.inflate(R.layout.list_users, null))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = listViewType.get(position)
        holder.itemView.tv_name.text = item.name
        Glide
            .with(context)
            .load(item.image)
            .centerCrop()
            .placeholder(R.drawable.ic_default_user)
            .error(R.drawable.ic_default_user)
            .into(holder.itemView.img_user)
    }



    override fun getItemCount(): Int = listViewType.size

    open inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    inner class ViewHolderItem(itemView: View) : ViewHolder(itemView)


    class Item {
        var id: String = ""
        var name: String = ""
        var image: String = ""
    }

}