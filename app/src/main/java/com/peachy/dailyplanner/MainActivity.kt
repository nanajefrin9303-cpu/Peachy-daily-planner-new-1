
package com.peachy.dailyplanner

import android.app.Activity
import android.os.Bundle
import android.graphics.Color
import android.view.Gravity
import android.widget.*
import android.content.Context
import org.json.JSONArray

class MainActivity : Activity() {

    private val prefs by lazy {
        getSharedPreferences("PeachyPlanner", Context.MODE_PRIVATE)
    }

    private val purple = Color.rgb(145, 110, 190)
    private val dark = Color.rgb(75, 55, 100)
    private val background = Color.rgb(248, 244, 255)
    private val white = Color.WHITE

    private lateinit var content: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        showHome()
    }

    private fun setup(titleText: String) {
        val root = LinearLayout(this)
        root.orientation = LinearLayout.VERTICAL
        root.setPadding(18, 20, 18, 18)
        root.setBackgroundColor(background)

        val title = TextView(this)
        title.text = "🌷 Peachy Daily Planner\n$titleText"
        title.textSize = 23f
        title.setTextColor(dark)
        title.gravity = Gravity.CENTER
        title.setPadding(8, 12, 8, 20)
        root.addView(title)

        val scroll = ScrollView(this)
        content = LinearLayout(this)
        content.orientation = LinearLayout.VERTICAL
        scroll.addView(content)
        root.addView(scroll, LinearLayout.LayoutParams(-1, 0, 1f))

        val nav = LinearLayout(this)
        nav.gravity = Gravity.CENTER
        listOf("Home", "Tasks", "Notes", "Trackers", "More").forEach {
            val b = Button(this)
            b.text = it
            b.textSize = 10f
            b.setOnClickListener { openPage(it) }
            nav.addView(b, LinearLayout.LayoutParams(0, -2, 1f))
        }
        root.addView(nav)
        setContentView(root)
    }

    private fun button(text: String, action: () -> Unit) {
        val b = Button(this)
        b.text = text
        b.setTextColor(white)
        b.setBackgroundColor(purple)
        b.setOnClickListener { action() }
        content.addView(b)
    }

    private fun heading(text: String) {
        val t = TextView(this)
        t.text = text
        t.textSize = 18f
        t.setTextColor(dark)
        t.setPadding(4, 16, 4, 8)
        content.addView(t)
    }

    private fun showHome() {
        setup("Lavender Dream")
        heading("✨ Welcome to your happy space!")
        heading("Your planner, your way 💜")
        button("📝 Daily Tasks") { openPage("Tasks") }
        button("📅 Calendar") { openPage("Calendar") }
        button("💌 My Notes") { openPage("Notes") }
        button("🎯 Goals & Habits") { openPage("Goals") }
        button("📚 Study Planner") { openPage("Study") }
        button("💰 Budget Tracker") { openPage("Budget") }
        button("💧 Water Tracker") { openPage("Water") }
        button("😊 Mood Tracker") { openPage("Mood") }
        button("🎂 Birthdays") { openPage("Birthdays") }
        button("🎨 Themes & Wallpaper") { openPage("Themes") }
        heading("💜 Made with love")
    }

    private fun openPage(page: String) {
        when (page) {
            "Home" -> showHome()
            "Tasks" -> showTasks()
            "Notes" -> showNotes()
            "Trackers" -> showTrackers()
            "More" -> showMore()
            "Calendar" -> showSimple("Calendar", "📅 Your calendar")
            "Goals" -> showSimple("Goals & Habits", "🎯 Your goals")
            "Study" -> showSimple("Study Planner", "📚 Your study schedule")
            "Budget" -> showSimple("Budget Tracker", "💰 Your expenses")
            "Water" -> showSimple("Water Tracker", "💧 Your water intake")
            "Mood" -> showSimple("Mood Tracker", "😊 How are you feeling?")
            "Birthdays" -> showSimple("Birthdays", "🎂 Remember special days")
            "Themes" -> showThemes()
        }
    }

    private fun showTasks() {
        setup("My Daily Tasks")
        val input = EditText(this)
        input.hint = "Write a task..."
        content.addView(input)
        button("＋ Add My Task") {
            val value = input.text.toString().trim()
            if (value.isNotEmpty()) {
                addSaved("tasks", value)
                input.text.clear()
                showTasks()
            }
        }
        heading("Your saved tasks")
        showSaved("tasks", true)
    }

    private fun showNotes() {
        setup("My Notes")
        val input = EditText(this)
        input.hint = "Write your notes here..."
        input.minLines = 3
        content.addView(input)
        button("Save Note 💌") {
            val value = input.text.toString().trim()
            if (value.isNotEmpty()) {
                addSaved("notes", value)
                input.text.clear()
                showNotes()
            }
        }
        showSaved("notes", false)
    }

    private fun showSaved(key: String, checkable: Boolean) {
        val array = getArray(key)
        for (i in 0 until array.length()) {
            val item = array.optString(i)
            val row = LinearLayout(this)
            row.orientation = LinearLayout.HORIZONTAL
            val text = TextView(this)
            text.text = item
            text.textSize = 16f
            text.setTextColor(dark)
            row.addView(text, LinearLayout.LayoutParams(0, -2, 1f))
            if (checkable) {
                val check = CheckBox(this)
                check.text = "Done"
                row.addView(check)
            }
            val delete = Button(this)
            delete.text = "✕"
            delete.setOnClickListener {
                array.remove(i)
                prefs.edit().putString(key, array.toString()).apply()
                if (key == "tasks") showTasks() else showNotes()
            }
            row.addView(delete)
            content.addView(row)
        }
    }

    private fun getArray(key: String): JSONArray {
        return try {
            JSONArray(prefs.getString(key, "[]") ?: "[]")
        } catch (e: Exception) {
            JSONArray()
        }
    }

    private fun addSaved(key: String, value: String) {
        val array = getArray(key)
        array.put(value)
        prefs.edit().putString(key, array.toString()).apply()
    }

    private fun showSimple(title: String, message: String) {
        setup(title)
        heading(message)
        val input = EditText(this)
        input.hint = "Add something..."
        content.addView(input)
        button("Save 💜") {
            val value = input.text.toString().trim()
            if (value.isNotEmpty()) {
                addSaved(title, value)
                input.text.clear()
                showSimple(title, message)
            }
        }
        showSaved(title, false)
    }

    private fun showTrackers() {
        setup("My Trackers")
        button("🎯 Goals & Habits") { openPage("Goals") }
        button("💰 Budget") { openPage("Budget") }
        button("💧 Water") { openPage("Water") }
        button("😊 Mood") { openPage("Mood") }
    }

    private fun showMore() {
        setup("More Features")
        button("📅 Calendar") { openPage("Calendar") }
        button("📚 Study Planner") { openPage("Study") }
        button("🎂 Birthdays") { openPage("Birthdays") }
        button("🎨 Themes & Wallpaper") { openPage("Themes") }
    }

    private fun showThemes() {
        setup("Themes & Wallpaper")
        heading("Choose your planner background")
        button("💜 Lavender Dream") {
            prefs.edit().putString("theme", "lavender").apply()
            Toast.makeText(this, "Lavender Dream selected!", Toast.LENGTH_SHORT).show()
        }
        button("🌸 Peach Pink") {
            prefs.edit().putString("theme", "pink").apply()
            Toast.makeText(this, "Peach Pink selected!", Toast.LENGTH_SHORT).show()
        }
        button("🤍 Soft Cream") {
            prefs.edit().putString("theme", "cream").apply()
            Toast.makeText(this, "Soft Cream selected!", Toast.LENGTH_SHORT).show()
        }
        heading("🌷 Stickers, fonts and wallpaper")
        heading("More customization coming soon!")
    }
}
