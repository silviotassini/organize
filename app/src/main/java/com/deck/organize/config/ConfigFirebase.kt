package com.deck.organize.config

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class ConfigFirebase {

    companion object {
        private var auth: FirebaseAuth? = null
        private var dbref: DatabaseReference? = null
        val firebaseDatabase: DatabaseReference
            get() {
                if (dbref == null){
                    dbref = FirebaseDatabase.getInstance().getReference()
                }
                return dbref!!
            }
        val firebaseAuth: FirebaseAuth
            get() {
                if (auth == null) {
                    auth = FirebaseAuth.getInstance()
                }
                return auth!!
            }
    }
}
