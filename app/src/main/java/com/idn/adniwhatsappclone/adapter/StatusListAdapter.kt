package com.idn.adniwhatsappclone.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.idn.adniwhatsappclone.R
import com.idn.adniwhatsappclone.listener.StatusItemClickListener
import com.idn.adniwhatsappclone.util.StatusListElement
import com.idn.adniwhatsappclone.util.populateImage
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_status.*

class StatusListAdapter(val statusList: ArrayList<StatusListElement>) :
    RecyclerView.Adapter<StatusListAdapter.StatusListViewHolder>() {

    private var clickListener: StatusItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        StatusListViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_status, parent, false)
        )

    override fun getItemCount() = statusList.size

    override fun onBindViewHolder(holder: StatusListViewHolder, position: Int) {
        holder.bindItem(statusList[position], clickListener)

    }

    fun onRefresh() {
        statusList.clear()
        notifyDataSetChanged()
    }

    fun addElement(element: StatusListElement) {
        statusList.add(element)
        notifyDataSetChanged()

    }

    fun setOnItemClickListener(listener: StatusItemClickListener){
        clickListener = listener
        notifyDataSetChanged()
    }

    class StatusListViewHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView), LayoutContainer {

        fun bindItem(statusElement: StatusListElement, listener: StatusItemClickListener?) {
            populateImage(
                img_status_photo.context,
                statusElement.userUrl,
                img_status_photo,
                R.drawable. ic_user
            )

            txt_status_name.text = statusElement.userName
            txt_status_time.text = statusElement.statusTime
            itemView.setOnClickListener{
                listener?.onItemClicked(statusElement)
            }
        }
    }
}