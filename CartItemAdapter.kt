package com.example.bcashoppingapplication.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.bcashoppingapplication.R
import com.example.bcashoppingapplication.databinding.RvCartItemRowBinding
import com.example.bcashoppingapplication.model.Crochet
import android.content.Context
import android.widget.Toast
import com.example.bcashoppingapplication.util.UtilObject
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class CartItemAdapter(private var context: Context,
                      private val cartItemList: MutableList<Crochet>,
                      private val firestore: FirebaseFirestore,
                      private val firebaseAuth: FirebaseAuth,
                      private val cartUpdateListener: CartItemViewHolder.CartUpdateListener
)
    : RecyclerView.Adapter<CartItemAdapter.CartItemViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): CartItemViewHolder {

        val binding : RvCartItemRowBinding = RvCartItemRowBinding.inflate(LayoutInflater.from(parent.context),parent,false)

        return CartItemViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder : CartItemViewHolder, position : Int
    ) {
        val cartItem = cartItemList[position]

        val drawableResId = context.resources.getIdentifier(
            cartItem.crochetDrawableName,
            "drawable",
            context.packageName
        )

        if (drawableResId != 0) {
            holder.binding.ivCartItemProduct.setImageResource(drawableResId)
        } else {
            holder.binding.ivCartItemProduct.setImageResource(R.drawable.ic_launcher_background)
        }

        holder.binding.tvCartProductName.text = cartItem.crochetName

        val userId = firebaseAuth.currentUser?.uid ?: return
        val productId = cartItem.documentId ?: return

        holder.binding.btnCartQtyIncrease.setOnClickListener {
            val newQty = (cartItem.crochetQuantity ?: 0) + 1
            updateQuantityInFirestore(userId, productId, newQty) {
                cartItem.crochetQuantity = newQty
                holder.binding.tvCartProductQty.text = "Qty: $newQty"

                updateTotal(holder, cartItem)
                cartUpdateListener.onCartUpdate(cartItemList)
            }
        }

        holder.binding.btnCartQtyDecrease.setOnClickListener {
            val currentQty = cartItem.crochetQuantity ?: 1
            if (currentQty > 1) {
                val newQty = currentQty - 1
                updateQuantityInFirestore(userId, productId, newQty) {
                    cartItem.crochetQuantity = newQty
                    holder.binding.tvCartProductQty.text = "Qty: $newQty"
                    updateTotal(holder, cartItem)
                    cartUpdateListener.onCartUpdate(cartItemList)
                }
            }
        }

        holder.binding.tvCartProductQty.text = "Qty: ${cartItem.crochetQuantity}"
        val priceWithSymbol = holder.itemView.context.getString(  R.string.rupee_sign ,
            cartItem.crochetPrice. toString())

        holder.binding.tvCartProductPrice.text = priceWithSymbol
        updateTotal(holder, cartItem)
        holder.binding.tvCartProductItemRemove.setOnClickListener {
            val currentPosition = holder.bindingAdapterPosition
            if (currentPosition == RecyclerView.NO_POSITION) return@setOnClickListener

            val userId = firebaseAuth.currentUser?.uid ?: return@setOnClickListener
            val product = cartItemList[currentPosition]
            val productId = cartItem.documentId
            if (productId == null) {
                Toast.makeText(context, "Invalid product", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            firestore.collection("Users")
                .document(userId)
                .collection("Cart")
                .document(productId)
                .delete()
                .addOnCompleteListener { task ->
                    val position = holder.bindingAdapterPosition
                    if (task.isSuccessful) {
                        cartItemList.removeAt(position)
                        notifyItemRemoved(position)
                        cartUpdateListener.onCartUpdate(cartItemList)

                        UtilObject.snackBar(it,
                                "Product Removed")

                    } else {
                        Toast.makeText(
                            context,
                                "Failed to remove the product",
                                Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }
    }


    override fun getItemCount(): Int {

        return cartItemList.size

    }
    private fun updateQuantityInFirestore(
        userId: String,
        productId: String,
        newQty: Int,
        onSuccess: () -> Unit) {

        firestore.collection("Users")
            .document( userId)
            .collection( "Cart")
            .document( productId)
            .update("crochetQuantity", newQty)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener {
                Toast.makeText(
                context, "Failed to update quantity",
                 Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateTotal(holder: CartItemViewHolder, cartItem: Crochet) {
        val totalAmount = (cartItem.crochetQuantity?: 0) * (cartItem.crochetPrice ?: 0)
        holder.binding.tvCartProductTotal.text = "₹$totalAmount"
    }

    class CartItemViewHolder (var binding: RvCartItemRowBinding) : RecyclerView.ViewHolder(binding.root) {

        interface CartUpdateListener{
            fun onCartUpdate(itemList: List<Crochet>)
        }


    }

}



