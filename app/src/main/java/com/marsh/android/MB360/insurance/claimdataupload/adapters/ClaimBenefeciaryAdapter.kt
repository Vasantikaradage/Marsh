package com.marsh.android.MB360.insurance.claimdataupload.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.marsh.android.MB360.R
import com.marsh.android.MB360.databinding.ItemCoverageDepandantBinding
import com.marsh.android.MB360.insurance.claimdataupload.interfaces.BeneficiarySelectListener
import com.marsh.android.MB360.insurance.repository.responseclass.GroupGMCPolicyEmpDependantsDatum


class ClaimBenefeciaryAdapter(private var items: List<GroupGMCPolicyEmpDependantsDatum>, var context: Context, var beneficiarySelected: BeneficiarySelectListener) : RecyclerView.Adapter<ClaimsBeneficiaryViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClaimsBeneficiaryViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemCoverageDepandantBinding.inflate(inflater, parent, false)
        return ClaimsBeneficiaryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ClaimsBeneficiaryViewHolder, position: Int) {

        //set the item here
        val item = items[position]

        holder.binding.itemDependantName.text = item.personName
        holder.binding.itemDependantRelation.text = item.relationName
        holder.binding.userImage.setImageDrawable(
                if (item.gender.equals("MALE", ignoreCase = true)) {
                    if (item.relationName.equals("SON", false)) {
                        ContextCompat.getDrawable(context, R.drawable.ic_by_son)
                    } else {
                        ContextCompat.getDrawable(context, R.drawable.ic_by_male)
                    }

                } else if (item.gender.equals("FEMALE", ignoreCase = true)) {
                    if (item.relationName.equals("DAUGHTER", false)) {
                        ContextCompat.getDrawable(context, R.drawable.ic_by_daughter)
                    } else {
                        ContextCompat.getDrawable(context, R.drawable.ic_by_woman)
                    }

                } else {
                    ContextCompat.getDrawable(context, R.drawable.ic_by_user)
                }
        )

        holder.binding.itemDob.text = "DOB : ${item.dateOfBirth}"
        holder.binding.itemAge.text = "Age : ${item.age}(yrs)"
        holder.binding.itemGender.text = "Gender : ${item.gender}"
        holder.binding.itemMobile.text = "Mobile : ${item.cellphoneNumber}"

        holder.binding.dependantRadioBtn.setOnCheckedChangeListener { compoundButton, b ->
            if (b) {
                beneficiarySelected.onBeneficiarySelect(position, item)
            } else {

            }
        }

        holder.binding.itemCard.setOnClickListener {
            beneficiarySelected.onBeneficiarySelect(position, item)
        }


        holder.binding.dependantRadioBtn.isChecked = item.isSelected

        if (item.isSelected) {
            holder.binding.itemSecondaryLayout.visibility = View.VISIBLE
        } else {
            holder.binding.itemSecondaryLayout.visibility = View.GONE
        }

    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun updateData(newData: List<GroupGMCPolicyEmpDependantsDatum>) {
        items = newData
        notifyDataSetChanged()
    }

}

class ClaimsBeneficiaryViewHolder(binding: ItemCoverageDepandantBinding) : RecyclerView.ViewHolder(binding.root) {
    var binding: ItemCoverageDepandantBinding

    init {
        this.binding = binding
    }

}