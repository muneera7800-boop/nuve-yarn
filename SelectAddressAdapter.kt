package com.example.bcashoppingapplication.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.TextView
import com.example.bcashoppingapplication.R
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.example.bcashoppingapplication.model.SelectedAddress

class SelectAddressAdapter (val context: Context,
                            var addressList: MutableList<SelectedAddress>) :
Adapter<SelectAddressAdapter.SelectedAddressViewHolder>() {



    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SelectedAddressViewHolder {

        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.rv_select_address_row_item, parent, false)

        return SelectedAddressViewHolder(view)
}

override fun onBindViewHolder(
    holder: SelectedAddressViewHolder,
    position: Int){

    val address = addressList[position]

            holder.addressTv.text = address.address

            holder.selectRbAddress.isChecked = address.isSelected

            holder. selectRbAddress. setOnClickListener {

                for (selectedAddress in addressList) {

                    selectedAddress.isSelected = false
                }

                address.isSelected = true
                notifyDataSetChanged()

            }

}

    override fun getItemCount(): Int {

        return addressList.size
    }

        fun getSelectedAddress(): SelectedAddress? {

            return addressList.firstOrNull() { it.isSelected }

    }

    class SelectedAddressViewHolder (itemView: View) : RecyclerView. ViewHolder (itemView) {

        val addressTv : TextView = itemView.findViewById(R.id.tv_address)
        val selectRbAddress : RadioButton = itemView.findViewById(R.id.rb_select)

    }
}


