package com.example.teamprojectlogin

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.teamprojectlogin.databinding.FragmentTodoBinding
import com.example.teamprojectlogin.db.AppDatabase
import com.example.teamprojectlogin.db.ToDoDao
import com.example.teamprojectlogin.db.ToDoEntity

class TodoFragment : Fragment(), OnItemLongClickListener {
    private lateinit var binding: FragmentTodoBinding
    private lateinit var db: AppDatabase
    private lateinit var todoDao: ToDoDao
    private lateinit var todoList: ArrayList<ToDoEntity>
    private lateinit var adapter: TodoRecyclerViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTodoBinding.inflate(inflater, container, false)
        binding.btnAdd.setOnClickListener {
            val intent = Intent(activity, AddTodoActivity::class.java)
            startActivity(intent)
        }
        db = AppDatabase.getInstance(requireContext())!!
        todoDao = db.getTodoDao()
        getAllTodoList()
        return binding.root
    }

    private fun getAllTodoList() {
        Thread {
            todoList = ArrayList(todoDao.getAll())
            setRecyclerView()
        }.start()
    }

    private fun setRecyclerView() {
        activity?.runOnUiThread {
            adapter = TodoRecyclerViewAdapter(todoList, this, todoDao)
            binding.recyclerView.adapter = adapter
            binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        }
    }

    override fun onLongClick(position: Int) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
        builder.setTitle("할 일 삭제")
        builder.setMessage("정말 삭제하시겠습니까?")
        builder.setNegativeButton("취소", null)
        builder.setPositiveButton("네", object : DialogInterface.OnClickListener {
            override fun onClick(p0: DialogInterface?, p1: Int) {
                deleteTodo(position)
            }
        })
        builder.show()
    }

    private fun deleteTodo(position: Int) {
        Thread {
            todoDao.deleteTodo(todoList[position])
            todoList.removeAt(position)
            activity?.runOnUiThread {
                adapter.notifyDataSetChanged()
                Toast.makeText(requireContext(), "삭제되었습니다.", Toast.LENGTH_SHORT).show()
            }
        }.start()
    }
}