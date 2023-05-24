package com.example.teamprojectlogin

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.teamprojectlogin.databinding.ActivityAddTodoBinding
import com.example.teamprojectlogin.db.AppDatabase
import com.example.teamprojectlogin.db.ToDoDao
import com.example.teamprojectlogin.db.ToDoEntity

/**
 * 할 일 추가 클래스
 * */
class AddTodoActivity : AppCompatActivity() {

    lateinit var binding: ActivityAddTodoBinding
    lateinit var db: AppDatabase
    lateinit var todoDao: ToDoDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddTodoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = AppDatabase.getInstance(this)!!
        todoDao = db.getTodoDao()

        binding.btnCompletion.setOnClickListener {
            insertTodo()
        }
    }

    /**
     * 할 일 추가 함수
     * */
    private fun insertTodo() {
        val todoTitle = binding.edtTitle.text.toString() // 할 일의 제목
        var todoImportance: String // 할 일의 중요도

        // 어떤 버튼이 눌렸는지 확인하고 값을 지정해줍니다
        when (binding.radioGroup.checkedRadioButtonId) {
            R.id.btn_high -> {
                todoImportance = "☆"
            }

            R.id.btn_middle -> {
                todoImportance = "○"
            }

            R.id.btn_low -> {
                todoImportance = "△"
            }

            else -> {
                todoImportance = "None"
            }
        }

        if (todoImportance == "None" || todoTitle.isBlank()) { // 중요도가 선택되지 않거나, 제목이 작성되지 않는지 체크합니다.
            Toast.makeText(this, "모든 항목을 채워주세요.", Toast.LENGTH_SHORT).show()
        } else {
            Thread {
                todoDao.insertTodo(ToDoEntity(null, todoTitle, todoImportance,isChecked = false))
                runOnUiThread { // 아래 작업은 UI 스레드에서 실행해주어야 합니다.
                    Toast.makeText(this, "추가되었습니다.", Toast.LENGTH_SHORT).show()
                    finish() // AddTodoActivity 종료, 다시 MainActivity로 돌아감
                }
            }.start()
        }
    }
}