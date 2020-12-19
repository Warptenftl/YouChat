package com.idn.adniwhatsappclone.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.idn.adniwhatsappclone.R
import com.idn.adniwhatsappclone.StatusActivity
import com.idn.adniwhatsappclone.adapter.StatusListAdapter
import com.idn.adniwhatsappclone.listener.StatusItemClickListener
import com.idn.adniwhatsappclone.util.StatusListElement
import kotlinx.android.synthetic.main.fragment_status_list.*
import com.idn.adniwhatsappclone.util.DATA_USERS
import com.idn.adniwhatsappclone.util.DATA_USER_CHATS
import com.idn.adniwhatsappclone.util.User

class StatusListFragment : Fragment(), StatusItemClickListener {

    private val firebaseDb = FirebaseFirestore.getInstance()
    private val userId = FirebaseAuth.getInstance().currentUser?.uid

    private var statusListAdapter =
        StatusListAdapter(arrayListOf())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_status_list, container, false)
    }

    override fun onItemClicked(statusElement: StatusListElement) {
        startActivity(StatusActivity.getIntent(context, statusElement))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        statusListAdapter.setOnItemClickListener(this)
        rv_status_list.apply {
            setHasFixedSize(false)
            layoutManager = LinearLayoutManager(context)
            adapter = statusListAdapter
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }
        onVisible()

        fab_status_list.setOnClickListener {
            onVisible()
        }
    }

    fun onVisible() {
        statusListAdapter.onRefresh()
        refreshList()
    }


    fun refreshList() {
        firebaseDb.collection(DATA_USERS).document(userId!!).get() // ambil collection user, ambil dokumen berdasarkan userId
            .addOnSuccessListener {
                if (it.contains(DATA_USER_CHATS)) { // jika data mengandung dataUserChat
                    val partners = it[DATA_USER_CHATS] // document userChats ditampung di variable partners untuk di-looping
                    for (partner in (partners as HashMap<String, String>).keys) // di-looping untuk ambil key dari HashMap
                                                                                // dimana key jika kita gunakan untuk mendapatkan document
                        firebaseDb.collection(DATA_USERS).document(partner).get() // kita gunakan key tersebut disini untuk mengambil datanya
                            .addOnSuccessListener {
                                val partner = it.toObject(User::class.java) // data dari document userChatPartner ditampung di Model User
                                if (partner != null){ // jika partner (model user yang sekarang) tidak boleh kosong
                                    if (!partner.status.isNullOrEmpty() || !partner.statusUrl.isNullOrEmpty()){ // data status atau statusUrl  di model tidak boleh kosong
                                        val newElement = StatusListElement(
                                            partner.name,
                                            partner.imageUrl,  // ngisi model StatusListElement
                                            partner.status, // dengan  data dari Model User
                                            partner.statusUrl,
                                            partner.statusTime
                                        )
                                        statusListAdapter.addElement(newElement)
                                    }

                                }
                            }
                }
            }

    }









}