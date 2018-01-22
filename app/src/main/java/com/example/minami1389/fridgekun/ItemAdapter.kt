package com.example.minami1389.fridgekun

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.minami1389.fridgekun.model.Item

/**
 * Created by minami.baba on 2018/01/23.
 */

class ItemAdapter(private val context: Context) : BaseAdapter() {
    private val layoutInflater: LayoutInflater
    private var items: ArrayList<Item> = ArrayList()

    private class ViewHolder {
        var itemNameTextView: TextView? = null
        var itemExpireTextView: TextView? = null
    }

    init {
        this.layoutInflater = LayoutInflater.from(context)
    }

    fun addItem(item: Item) {
        items.add(item)
    }

    override fun getCount(): Int {
        return items.size
    }

    override fun getItem(position: Int): Any {
        return items[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view = convertView

        val holder: ViewHolder
        if (view == null) {
            view = layoutInflater.inflate(R.layout.grid_item_fridge, null)
            holder = ViewHolder()
            holder.itemNameTextView = view.findViewById<TextView>(R.id.itemNameTextView)
            holder.itemExpireTextView = view.findViewById<TextView>(R.id.itemExpireTextView)
            view.tag = holder
        } else {
            holder = view.tag as ViewHolder
        }

        holder.itemNameTextView?.setText(items[position].name)
        holder.itemExpireTextView?.setText(items[position].expired)

        return view!!
    }
}
