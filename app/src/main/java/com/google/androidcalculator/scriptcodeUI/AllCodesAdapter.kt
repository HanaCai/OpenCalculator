package com.google.androidcalculator.scriptcodeUI

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.androidcalculator.databinding.ItemRecyclercodesBinding
import com.google.androidcalculator.roomdatabase.ScriptCodes

class AllCodesAdapter : RecyclerView.Adapter<AllCodesAdapter.MyViewHolder>() {
    private var listener: ((ScriptCodes, View) -> Unit)? = null
    var list = mutableListOf<ScriptCodes>()

    fun setContentList(list: MutableList<ScriptCodes>) {
        this.list = list
        notifyDataSetChanged()
    }

    fun removeItem(item: ScriptCodes) {
        this.list.remove(item)
        notifyDataSetChanged()
    }

    class MyViewHolder(val viewHolder: ItemRecyclercodesBinding) :
        RecyclerView.ViewHolder(viewHolder.root)

    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): MyViewHolder {
        val binding =
            ItemRecyclercodesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    fun itemClickListener(l: (ScriptCodes, View) -> Unit) {
        listener = l
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.viewHolder.scriptscode = this.list[position]
        holder.viewHolder.imageEdit.setOnClickListener {
            listener?.let {listener->
                listener(this.list[position],it)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemCount(): Int {
        return this.list.size
    }
}