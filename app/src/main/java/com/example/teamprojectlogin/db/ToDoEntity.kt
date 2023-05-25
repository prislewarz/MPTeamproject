package com.example.teamprojectlogin.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity // 어떤 구성요소인지를 알려주려면 꼭 어노테이션을 써주어야 합니다.
data class ToDoEntity (
    @PrimaryKey(autoGenerate = true) var id: Int? = null,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "month") val month: Int,
    @ColumnInfo(name = "day") val day: Int,
    @ColumnInfo(name = "hour") val hour: Int,
    @ColumnInfo(name = "minute") val minute: Int,
    @ColumnInfo(name = "importance") val importance: String,
    @ColumnInfo(name = "isChecked") var isChecked: Boolean
)