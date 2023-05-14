package com.example.teamprojectlogin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast

import com.example.teamprojectlogin.databinding.ActivityMypageBinding

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class MypageActivity : AppCompatActivity() {
    lateinit var binding: ActivityMypageBinding
    lateinit var database : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMypageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // pw: String? = null, val nickname: String? = null, val dep:String? = null, val studentnum:String? = null
        binding.btnRegisterMypage.setOnClickListener{
            val email = binding.etEmail.text.toString()
            val pw = binding.etPw.text.toString()
            val nickname = binding.etNickname.text.toString()
            val name = binding.etName.text.toString()
            val department = binding.etKaudepartment.text.toString()
            val kauid = binding.etKauID.text.toString()

            database = FirebaseDatabase.getInstance().getReference("Users")
            val User = User_Model(email, pw, nickname, name, department,kauid)

            database.child(nickname).setValue(User).addOnSuccessListener {
                binding.etEmail.text.clear()
                binding.etPw.text.clear()
                binding.etNickname.text.clear()
                binding.etName.text.clear()
                binding.etKaudepartment.text.clear()
                binding.etKauID.text.clear()

                Toast.makeText(this, "Successfully saved",Toast.LENGTH_SHORT).show()
            }.addOnFailureListener{
                Toast.makeText(this, "Failed",Toast.LENGTH_SHORT).show()
            }

        }
    }
}