package io.github.reservationbytom.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import io.github.reservationbytom.R
import io.github.reservationbytom.service.model.Rest
import io.github.reservationbytom.view.callback.RestClickCallback

class RestAdapter (private val projectClickCallback: RestClickCallback?) :
    RecyclerView.Adapter<RestAdapter.RestViewHolder>() {

    private var restList: List<Rest>? = null

    fun setRestList(restList: List<Rest>) {

        if (this.restList == null) {
            this.restList = restList

            notifyItemRangeInserted(0, restList.size)

        } else {

            val result = DiffUtil.calculateDiff(object : DiffUtil.Callback() {
                override fun getOldListSize(): Int {
                    return requireNotNull(this@RestAdapter.restList).size
                }

                override fun getNewListSize(): Int {
                    return restList.size
                }

                override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                    val oldList = this@RestAdapter.restList
                    return oldList?.get(oldItemPosition)?.id == restList[newItemPosition].id
                }

                override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                    val project = restList[newItemPosition]
                    val old = restList[oldItemPosition]

                    return project.id == old.id && project.git_url == old.git_url
                }
            })
            this.restList = restList

            result.dispatchUpdatesTo(this)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewtype: Int): RestViewHolder {
        val binding =
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.rest_list_item, parent,
                false) as RestListBinding

        binding.callback = projectClickCallback

        return RestViewHolder(
            binding
        )
    }

    override fun onBindViewHolder(holder: RestViewHolder, position: Int) {
        holder.binding.rest = restList?.get(position)
        holder.binding.executePendingBindings()
    }

    override fun getItemCount(): Int {
        return restList?.size ?: 0
    }

    open class RestViewHolder(val binding: RestListBinding) : RecyclerView.ViewHolder(binding.root)

}