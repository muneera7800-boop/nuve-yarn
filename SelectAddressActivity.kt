package com.example.bcashoppingapplication.activities

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bcashoppingapplication.R
import com.example.bcashoppingapplication.adapter.SelectAddressAdapter
import com.example.bcashoppingapplication.databinding.ActivitySelectAddressBinding
import com.example.bcashoppingapplication.model.Crochet
import com.example.bcashoppingapplication.model.SelectedAddress
import com.example.bcashoppingapplication.util.UtilObject
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import java.io.Serializable

class SelectAddressActivity : AppCompatActivity() {

    private var firebaseAuth: FirebaseAuth? = null
    private var fireStore: FirebaseFirestore? = null
    private var selectAddressAdapter: SelectAddressAdapter? = null
    private lateinit var addressList: MutableList<SelectedAddress>
    var fireStoreProductList: MutableList<Crochet>? = null

    lateinit var binding: ActivitySelectAddressBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySelectAddressBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = Firebase.auth
        fireStore = Firebase.firestore

        navigationOfViews()
        initRecyclerView()
        getProductListFromCartActivity()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    override fun onResume() {
        super.onResume()
        getAddressFromFireStore()
    }

    private fun navigationOfViews() {
        binding.btnAddAddress.setOnClickListener {
            val intent = Intent(this, AddAddressActivity::class.java)
            startActivity(intent)
        }

        binding.btnContinuePayment.setOnClickListener {
            val selectedAddress = selectAddressAdapter?.getSelectedAddress()

            if (selectedAddress != null) {
                val intent = Intent(this@SelectAddressActivity, PaymentActivity::class.java)
                intent.putExtra("selectedAddress", selectedAddress)
                
                if (fireStoreProductList != null) {
                    intent.putExtra("fireStoreProductList", ArrayList(fireStoreProductList!!))
                }
                
                startActivity(intent)
            } else {
                Toast.makeText(this, "Please Select An Address", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getProductListFromCartActivity() {
        val rawList = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getSerializableExtra("fireStoreProductList", ArrayList::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getSerializableExtra("fireStoreProductList")
        }

        fireStoreProductList = (rawList as? ArrayList<*>)?.filterIsInstance<Crochet>()?.toMutableList()
    }

    private fun initRecyclerView() {
        addressList = ArrayList()
        binding.rvAddress.layoutManager = LinearLayoutManager(this)
        selectAddressAdapter = SelectAddressAdapter(this, addressList)
        binding.rvAddress.adapter = selectAddressAdapter
    }

    private fun getAddressFromFireStore() {
        val currentUser = firebaseAuth?.currentUser
        if (currentUser != null) {
            fireStore?.collection("Users")
                ?.document(currentUser.uid)
                ?.collection("Address")
                ?.get()?.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val newAddressList = mutableListOf<SelectedAddress>()
                        for (documentSnapshot in task.result) {
                            val showAddress = documentSnapshot.toObject(SelectedAddress::class.java)
                            newAddressList.add(showAddress)
                        }

                        addressList.clear()
                        if (newAddressList.isNotEmpty()) {
                            newAddressList.last().isSelected = true
                            addressList.addAll(newAddressList)
                        }
                        selectAddressAdapter?.notifyDataSetChanged()
                    }
                }
        }
    }
}