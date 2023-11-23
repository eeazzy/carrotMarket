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
            val email=emailSet.text.toString()
            val password=passwordSet.text.toString()
            val birth=birthSet.text.toString()
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
                    //Firebase DB에 저장 되어 있는 계정이 아닐 경우
                    //입력한 계정을 새로 등록한다
                    addUserToFirestore(name,email,birth, auth.currentUser?.uid.toString())
                    Toast.makeText(this, "회원가입이 완료되었습니다.", Toast.LENGTH_LONG).show()
                } else if (task.exception?.message.isNullOrEmpty()) {
                    Toast.makeText(this, task.exception?.message, Toast.LENGTH_LONG).show()
                } else {
                    //입력한 계정 정보가 이미 Firebase DB에 있는 경우
                    Toast.makeText(this, "이미 가입된 회원입니다.", Toast.LENGTH_LONG).show()
                    signIn(email, password)
                }
            }
    }

    private fun addUserToFirestore(name: String, email: String, phoneNumber: String, uid: String) {
        val db = FirebaseFirestore.getInstance()
        val user=User(name,email,phoneNumber,uid)
        db.collection("users")
            .document(uid)
            .set(user)
            .addOnSuccessListener {
                // 데이터가 성공적으로 추가될 때 실행할 작업
            }
            .addOnFailureListener {
                // 데이터 추가가 실패했을 때 실행할 작업
            }
    }

    fun signIn(email: String, password: String) { // 로그인
        auth?.signInWithEmailAndPassword(email, password)
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // 로그인에 성공한 경우 메인 화면으로 이동
                    goToMainActivity(task.result?.user)
                }
                else {
                    // 로그인에 실패한 경우 Toast 메시지로 에러를 띄워준다
                    Toast.makeText(this, "존재하지 않는 회원입니다.", Toast.LENGTH_LONG).show()
                }
            }
    }

    fun goToMainActivity(user: FirebaseUser?) {// 로그인에 성공했을떄 메인 엑티비티로 이동을 허용
        if (user != null) {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }
}

