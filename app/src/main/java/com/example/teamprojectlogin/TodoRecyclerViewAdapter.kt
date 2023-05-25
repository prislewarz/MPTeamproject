package com.example.teamprojectlogin

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.teamprojectlogin.databinding.ItemTodoBinding
import com.example.teamprojectlogin.db.ToDoEntity
import com.example.teamprojectlogin.db.ToDoDao

class TodoRecyclerViewAdapter(
    private val todoList: ArrayList<ToDoEntity>,
    private val listener: OnItemLongClickListener,
    private val todoDao: ToDoDao
) :
    RecyclerView.Adapter<TodoRecyclerViewAdapter.MyViewHolder>() {

    inner class MyViewHolder(binding: ItemTodoBinding) : RecyclerView.ViewHolder(binding.root) {
        val checkBox: CheckBox = binding.checkBox
        val tv_importance = binding.tvImportance
        val tv_title = binding.tvTitle

        // timeView를 바인딩합니다.
        val timeView: TextView = binding.timeView

        // 뷰 바인딩에서 기본적으로 제공하는 root 변수는 레이아웃의 루트 레이아웃을 의미합니다.
        val root = binding.root
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        var todoData = todoList[position]

        holder.checkBox.isChecked = todoData.isChecked
        holder.checkBox.setOnCheckedChangeListener { _, isChecked ->
            todoData.isChecked = isChecked
            Thread {
                todoDao.updateIsChecked(todoData.id!!, isChecked)
            }.start()
        }

        // 색상 변경
        when (todoData.importance) {
            "△" -> {
                holder.tv_importance.setBackgroundResource(R.color.red)
            }

            "○" -> {
                holder.tv_importance.setBackgroundResource(R.color.yellow)
            }

            "☆" -> {
                holder.tv_importance.setBackgroundResource(R.color.green)
            }
        }
        holder.tv_importance.text = todoData.importance.toString()
        // 할 일 제목 변경
        holder.tv_title.text = todoData.title

        // 시간 정보를 표시하는 TextView를 업데이트합니다.
        val timeText = "${todoData.hour}:${todoData.minute}"
        holder.timeView.text = timeText

        // 할 일이 길게 클릭되었을 때 리스너 함수 실행
        holder.root.setOnLongClickListener {
            listener.onLongClick(position)
            false
        }
    }

    override fun getItemCount(): Int {
        // 리사이클러뷰 아이템 개수는 할 일 리스트의 크기
        return todoList.size
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        // item_todo.xml 바인딩 객체 생성
        val binding: ItemTodoBinding =
            ItemTodoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }
}