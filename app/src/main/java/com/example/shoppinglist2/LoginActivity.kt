package com.example.shoppinglist2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val auth = FirebaseAuth.getInstance()

        bt_loginRegister.setOnClickListener{
            auth.signInWithEmailAndPassword(et_login.text.toString(), et_password.text.toString())
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        Toast.makeText(this, "Zalogowano", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, MainActivity::class.java).also {
                            it.putExtra("user", auth.currentUser?.email)
                        })
                    }else{
                        Toast.makeText(this, "Błąd logowania", Toast.LENGTH_SHORT).show()
                    }
                }
        }

        bt_loginRegister.setOnLongClickListener{
            auth.createUserWithEmailAndPassword(et_login.text.toString(), et_password.text.toString())
                .addOnCompleteListener{
                    if (it.isSuccessful) {
                        Toast.makeText(this, "Rejestracja przebiegła prawidłowo", Toast.LENGTH_SHORT).show()
                    }else{
                        Toast.makeText(this, "Błąd rejestracji", Toast.LENGTH_SHORT).show()

                    }
                }
            true
        }
    }


}