package com.example.carrotmarket

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login)
        auth = Firebase.auth
        database=Firebase.database.reference
        val nameSet=findViewById<EditText>(R.id.setName)
        val birthSet=findViewById<EditText>(R.id.editBirth)
        val emailSet=findViewById<EditText>(R.id.email)
        val passwordSet=findViewById<EditText>(R.id.passwd)


        val regiBtn=findViewById<Button>(R.id.btnRegister)
        var loginBtn=findViewById<Button>(R.id.btnLogin)
        regiBtn.setOnClickListener {
            val name=nameSet.text.toString()
            val birth=birthSet.text.toString()
            val email=emailSet.text.toString()
            val password=passwordSet.text.toString()

            signUp(name,email,password,birth)
        }

        loginBtn.setOnClickListener {
            signIn(emailSet.text.toString(),passwordSet.text.toString())
        }
    }

    fun signUp(name:String, email: String, password: String, birth:String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    addUserToFirestore(name,email,birth, auth.currentUser?.uid.toString())
                    Toast.makeText(this, "회원가입이 완료되었습니다.", Toast.LENGTH_LONG).show()
                } else if (task.exception?.message.isNullOrEmpty()) {
                    Toast.makeText(this, task.exception?.message, Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this, "이미 가입된 회원입니다.", Toast.LENGTH_LONG).show()
                    signIn(email, password)
                }
            }
    }

    private fun addUserToFirestore(name: String, email: String, birth: String, uid: String) {
        val db = FirebaseFirestore.getInstance()
        val user=User(name,email,birth,uid)
        db.collection("users")
            .document(uid)
            .set(user)
            .addOnSuccessListener {
            }
            .addOnFailureListener {
            }
    }

    fun signIn(email: String, password: String) {
        if (email.isEmpty() || password.isEmpty()) {//null값 처리
            Toast.makeText(this, "이메일과 비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show()
            return
        }
        auth?.signInWithEmailAndPassword(email, password)
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    goToMainActivity(task.result?.user)
                }
                else {
                    Toast.makeText(this, "존재하지 않는 회원입니다.", Toast.LENGTH_LONG).show()
                }
            }
    }

    fun goToMainActivity(user: FirebaseUser?) {
        if (user != null) {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }
}

