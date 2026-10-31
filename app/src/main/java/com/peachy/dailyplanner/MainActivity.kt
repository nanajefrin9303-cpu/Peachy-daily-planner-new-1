package com.peachy.dailyplanner

import android.app.Activity
import android.os.Bundle
import android.graphics.Color
import android.view.Gravity
import android.widget.*

class MainActivity : Activity() {

    private val lavender = Color.rgb(245, 240, 255)
    private val purple = Color.rgb(145, 110, 190)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val layout = LinearLayout(this)
        layout.orientation = LinearLayout.VERTICAL
        layout.setPadding(24, 30, 24, 24)
        layout.setBackgroundColor(lavender)

        val title = TextView(this)
        title.text = "🌷 Peachy Daily Planner"
        title.textSize = 27f
        title.setTextColor(purple)
        title.gravity = Gravity.CENTER
        title.setPadding(0, 20, 0, 25)
        layout.addView(title)

        val subtitle = TextView(this)
        subtitle.text = "Make today lovely ♡"
        subtitle.textSize = 17f
        subtitle.gravity = Gravity.CENTER
        subtitle.setTextColor(Color.DKGRAY)
        layout.addView(subtitle)

        val input = EditText(this)
        input.hint = "Write your task here..."
        input.textSize = 16f
        layout.addView(input)

        val addButton = Button(this)
        addButton.text = "＋ Add My Task"
        addButton.setBackgroundColor(purple)
        addButton.setTextColor(Color.WHITE)
        layout.addView(addButton)

        val taskList = LinearLayout(this)
        taskList.orientation = LinearLayout.VERTICAL
        taskList.setPadding(0, 20, 0, 0)

        val scroll = ScrollView(this)
        scroll.addView(taskList)

        layout.addView(
            scroll,
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                0,
                1f
            )
        )

        addButton.setOnClickListener {
            val task = input.text.toString().trim()

            if (task.isNotEmpty()) {
                val checkbox = CheckBox(this)
                checkbox.text = task
                checkbox.textSize = 17f
                checkbox.setTextColor(Color.DKGRAY)
                taskList.addView(checkbox)
                input.text.clear()
            } else {
                Toast.makeText(
                    this,
                    "Please write a task first ♡",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        setContentView(layout)
    }
}
