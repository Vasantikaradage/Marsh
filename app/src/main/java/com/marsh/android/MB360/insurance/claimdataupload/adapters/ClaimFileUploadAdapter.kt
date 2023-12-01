package com.marsh.android.MB360.insurance.claimdataupload.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.marsh.android.MB360.databinding.ItemClaimsFileUploadBinding
import com.marsh.android.MB360.insurance.claimdataupload.interfaces.FileUploadListerner
import com.marsh.android.MB360.insurance.claimdataupload.responseclass.ClaimFileUpload

class ClaimFileUploadAdapter(private val items: ArrayList<ClaimFileUpload>, private val fileUploadListerner: FileUploadListerner) : RecyclerView.Adapter<ClaimsFileUploadViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClaimsFileUploadViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemClaimsFileUploadBinding.inflate(inflater, parent, false)
        return ClaimsFileUploadViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ClaimsFileUploadViewHolder, position: Int) {
        val item: ClaimFileUpload = items[position]
        //set the item here

        if (item.fileName.equals("", false)) {
            holder.binding.itemFileName.text = item.items
        } else {
            holder.binding.itemFileName.text = item.fileName
        }

        if (item.status) {
            holder.binding.fileRemove.visibility = View.VISIBLE

            holder.binding.fileAdd.visibility = (View.INVISIBLE)
            "${item.fileSize} Kb".also { holder.binding.itemFileSize.text = it }
            holder.itemView.setOnClickListener {
                fileUploadListerner.fileOnClickListerner(position)

            }
        } else {
            holder.binding.fileAdd.visibility = (View.VISIBLE)
            holder.binding.fileRemove.visibility = (View.GONE)
            holder.binding.itemFileSize.text = ""
            holder.itemView.setOnClickListener {
                fileUploadListerner.fileOnClickListerner(position)

            }
        }


        if (item.fileSize.equals("", ignoreCase = true)){
            holder.binding.itemFileSize.visibility = View.GONE
        }else{
            holder.binding.itemFileSize.visibility = View.VISIBLE
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }
}


class ClaimsFileUploadViewHolder(binding: ItemClaimsFileUploadBinding) : RecyclerView.ViewHolder(binding.root) {
    val binding: ItemClaimsFileUploadBinding

    init {
        this.binding = binding
    }
}


