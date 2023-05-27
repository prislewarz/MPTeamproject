package com.example.teamprojectlogin

import android.annotation.SuppressLint
import android.content.Context.MODE_NO_LOCALIZED_COLLATORS
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CalendarView
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth

class CalendarFragment : Fragment() {
    private var userID: String = "userID"
    private lateinit var fname: String
    private lateinit var str: String
    private lateinit var calendarView: CalendarView
    private lateinit var updateBtn: Button
    private lateinit var deleteBtn: Button
    private lateinit var saveBtn: Button
    private lateinit var dateTextView: TextView
    private lateinit var diaryContent: TextView
    private lateinit var title: TextView
    private lateinit var contextEditText: EditText
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private lateinit var uid: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_calendar, container, false)
        calendarView = view.findViewById(R.id.calendarView)
        dateTextView = view.findViewById(R.id.diaryTextView)
        saveBtn = view.findViewById(R.id.saveBtn)
        deleteBtn = view.findViewById(R.id.deleteBtn)
        updateBtn = view.findViewById(R.id.updateBtn)
        diaryContent = view.findViewById(R.id.diaryContent)
        title = view.findViewById(R.id.title)
        contextEditText = view.findViewById(R.id.contextEditText)

        // 현재 로그인된 유저의 UID 확인
        val currentUser = auth.currentUser
        if (currentUser != null) {
            uid = currentUser.uid
            Log.d("IISE", "User UID: $uid")
        } else {
            Log.d("IISE", "User is not logged in.")
        }

        userID = uid
        title.text = "루틴 캘린더"

        //날짜 변경할 때
        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            dateTextView.visibility = View.VISIBLE
            saveBtn.visibility = View.VISIBLE
            contextEditText.visibility = View.VISIBLE
            diaryContent.visibility = View.INVISIBLE
            updateBtn.visibility = View.INVISIBLE
            deleteBtn.visibility = View.INVISIBLE
            dateTextView.text = String.format("%d / %d / %d", year, month + 1, dayOfMonth)
            contextEditText.setText("")
            checkDay(year, month, dayOfMonth, userID)
            Log.d("IISE", "USER ID: $userID")
        }

        //// 저장 버튼 눌렀을 때
        saveBtn.setOnClickListener {
            saveDiary(fname)
            contextEditText.visibility = View.INVISIBLE
            saveBtn.visibility = View.INVISIBLE
            updateBtn.visibility = View.VISIBLE
            deleteBtn.visibility = View.VISIBLE
            str = contextEditText.text.toString()
            diaryContent.text = str
            diaryContent.visibility = View.VISIBLE
        }

        return view
    }

    // 달력 내용 조회, 수정
    private fun checkDay(cYear: Int, cMonth: Int, cDay: Int, userID: String) {
        //저장할 파일 이름 설정
        fname = "" + userID + cYear + "-" + (cMonth + 1) + "" + "-" + cDay + ".txt"
        Log.d("IISE", "fname: $fname")

        try {
            val fileInputStream = requireContext().openFileInput(fname)
            val fileData = ByteArray(fileInputStream.available())
            fileInputStream.read(fileData)
            fileInputStream.close()
            str = String(fileData)
            contextEditText.visibility = View.INVISIBLE
            diaryContent.visibility = View.VISIBLE
            diaryContent.text = str
            saveBtn.visibility = View.INVISIBLE
            updateBtn.visibility = View.VISIBLE
            deleteBtn.visibility = View.VISIBLE

            //수정버튼
            updateBtn.setOnClickListener {
                contextEditText.visibility = View.VISIBLE
                diaryContent.visibility = View.INVISIBLE
                contextEditText.setText(str)
                saveBtn.visibility = View.VISIBLE
                updateBtn.visibility = View.INVISIBLE
                deleteBtn.visibility = View.INVISIBLE
                diaryContent.text = contextEditText.text
            }
            //삭제버튼
            deleteBtn.setOnClickListener {
                diaryContent.visibility = View.INVISIBLE
                updateBtn.visibility = View.INVISIBLE
                deleteBtn.visibility = View.INVISIBLE
                contextEditText.setText("")
                contextEditText.visibility = View.VISIBLE
                saveBtn.visibility = View.VISIBLE
                removeDiary(fname)
            }

            if (diaryContent.text == null) {
                diaryContent.visibility = View.INVISIBLE
                updateBtn.visibility = View.INVISIBLE
                deleteBtn.visibility = View.INVISIBLE
                dateTextView.visibility = View.VISIBLE
                saveBtn.visibility = View.VISIBLE
                contextEditText.visibility = View.VISIBLE
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // 달력 내용 제거
    @SuppressLint("WrongConstant")
    private fun removeDiary(readDay: String?) {
        try {
            val fileOutputStream =
                requireContext().openFileOutput(readDay, MODE_NO_LOCALIZED_COLLATORS)
            val content = ""
            fileOutputStream.write(content.toByteArray())
            fileOutputStream.close()
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    // 달력 내용 추가
    @SuppressLint("WrongConstant")
    private fun saveDiary(fname: String?) {
        try {
            val fileOutputStream =
                requireContext().openFileOutput(fname, MODE_NO_LOCALIZED_COLLATORS)
            val content = contextEditText.text.toString()
            fileOutputStream.write(content.toByteArray())
            fileOutputStream.close()
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }
}