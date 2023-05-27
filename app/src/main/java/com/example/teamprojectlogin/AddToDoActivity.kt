package com.example.teamprojectlogin

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.teamprojectlogin.databinding.ActivityAddTodoBinding
import com.example.teamprojectlogin.db.AppDatabase
import com.example.teamprojectlogin.db.ToDoDao
import com.example.teamprojectlogin.db.ToDoEntity
import java.util.Calendar

/**
 * 할 일 추가 클래스
 * */
class AddTodoActivity : AppCompatActivity() {
    lateinit var binding: ActivityAddTodoBinding
    lateinit var db: AppDatabase
    lateinit var todoDao: ToDoDao

    private var todoHour: Int = 0
    private var todoMinute: Int = 0
    private var todoMonth: Int = 0
    private var todoDay: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddTodoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = AppDatabase.getInstance(this)!!
        todoDao = db.getTodoDao()

        val calendar = Calendar.getInstance()
        todoMonth = calendar.get(Calendar.MONTH)
        todoDay = calendar.get(Calendar.DAY_OF_MONTH)
        todoHour = calendar.get(Calendar.HOUR_OF_DAY)
        todoMinute = calendar.get(Calendar.MINUTE)

        binding.timePicker.setIs24HourView(true)
        binding.timePicker.setOnTimeChangedListener { _, hourOfDay, minute ->
            todoHour = hourOfDay
            todoMinute = minute
            val time = String.format("%02d:%02d", hourOfDay, minute)
            binding.timeView.text = time
        }


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
                // insertTodo 메소드를 수정하여 새로운 ToDoEntity를 데이터베이스에 삽입할 때 선택된 날짜와 시간을 포함하도록 합니다.
                todoDao.insertTodo(ToDoEntity(null, todoTitle, todoMonth, todoDay, todoHour, todoMinute, todoImportance, isChecked = false))
                runOnUiThread { // 아래 작업은 UI 스레드에서 실행해주어야 합니다.
                    // 알람을 설정합니다.
                    val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
                    val intent = Intent(this, AlarmReceiver::class.java)
                    val pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0)

                    // 알람 시간을 설정합니다.
                    val calendar = Calendar.getInstance()
                    calendar.set(Calendar.DAY_OF_MONTH, todoDay)
                    calendar.set(Calendar.HOUR_OF_DAY, todoHour)
                    calendar.set(Calendar.MINUTE, todoMinute)
                    calendar.set(Calendar.SECOND, 0)



                    // 알람을 설정합니다.
                    alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)

                    Toast.makeText(this, "추가되었습니다.", Toast.LENGTH_SHORT).show()
                    finish() // AddTodoActivity 종료, 다시 MainActivity로 돌아감
                }
            }.start()
        }
    }
}