
package com.peachy.dailyplanner

import android.app.Activity
import android.os.Bundle
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.view.Gravity
import android.widget.*

class MainActivity : Activity() {
    private val purple = Color.rgb(135, 105, 190)
    private val prefs by lazy { getSharedPreferences("peachy", MODE_PRIVATE) }
    private lateinit var root: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        showHome()
    }

    private fun dp(n: Int) = (n * resources.displayMetrics.density).toInt()

    private fun rounded(color: Int): GradientDrawable {
        return GradientDrawable().apply {
            setColor(color)
            cornerRadius = dp(20).toFloat()
        }
    }

    private fun setup(title: String) {
        root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(dp(18), dp(18), dp(18), dp(12))
            setBackgroundColor(Color.rgb(250, 246, 255))
        }

        val scroll = ScrollView(this)
        scroll.addView(root)
        setContentView(scroll)

        val heading = TextView(this).apply {
            text = title
            textSize = 26f
            typeface = Typeface.DEFAULT_BOLD
            setTextColor(Color.rgb(91, 65, 130))
            setPadding(0, dp(8), 0, dp(18))
        }
        root.addView(heading)
    }

    private fun text(value: String, size: Float = 16f): TextView {
        return TextView(this).apply {
            text = value
            textSize = size
            setTextColor(Color.rgb(80, 65, 95))
            setPadding(dp(10), dp(10), dp(10), dp(10))
        }
    }

    private fun button(value: String, action: () -> Unit) {
        val b = Button(this).apply {
            text = value
            setTextColor(Color.WHITE)
            background = rounded(purple)
            setOnClickListener { action() }
        }
        root.addView(b)
    }

    private fun card(value: String, action: () -> Unit) {
        val b = TextView(this).apply {
            text = value
            textSize = 17f
            typeface = Typeface.DEFAULT_BOLD
            setTextColor(Color.rgb(91, 65, 130))
            setPadding(dp(18), dp(20), dp(18), dp(20))
            background = rounded(Color.WHITE)
            setOnClickListener { action() }
        }
        val params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        params.bottomMargin = dp(10)
        root.addView(b, params)
    }

    private fun showHome() {
        setup("Peachy Daily Planner 🌷")
        root.addView(text("Your lovely space to plan, dream and grow.", 15f))
        card("📅  My Planner\nDaily tasks and calendar") {
            listPage("My Planner", "Add a task or plan")
        }
        card("📝  Notes & Journal\nSave your thoughts") {
            listPage("Notes & Journal", "Write a note")
        }
        card("🎯  Goals & Habits\nTrack your progress") {
            listPage("Goals & Habits", "Add a goal or habit")
        }
        card("📚  Study Planner\nOrganise your studies") {
            listPage("Study Planner", "Add a study plan")
        }
        card("💰  Budget Tracker\nRecord your expenses") {
            listPage("Budget Tracker", "Enter an expense")
        }
        card("💧  Water Tracker\nTrack your water intake") {
            listPage("Water Tracker", "Example: 1 glass of water")
        }
        card("💗  Mood Tracker\nRecord how you feel") {
            listPage("Mood Tracker", "How are you feeling today?")
        }
        card("🎂  Birthday Reminders\nRemember special days") {
            listPage("Birthdays", "Name and birthday")
        }
        card("🎨  Themes & Wallpapers\nPersonalise your planner") {
            showThemes()
        }
        card("🔎  Search\nFind your saved entries") {
            searchPage()
        }
        navigation()
    }

    private fun navigation() {
        val bar = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER
        }
        val names = listOf("Home", "Tasks", "Notes", "More")
        names.forEach { name ->
            val b = TextView(this).apply {
                text = name
                textSize = 13f
                gravity = Gravity.CENTER
                setTextColor(purple)
                setPadding(dp(9), dp(16), dp(9), dp(16))
                setOnClickListener {
                    when (name) {
                        "Home" -> showHome()
                        "Tasks" -> listPage("My Planner", "Add a task")
                        "Notes" -> listPage("Notes & Journal", "Write a note")
                        "More" -> showMore()
                    }
                }
            }
            bar.addView(b, LinearLayout.LayoutParams(0, dp(55), 1f))
        }
        root.addView(bar)
    }

    private fun showMore() {
        setup("More Features ✨")
        card("📆 Calendar") {
            listPage("Calendar Events", "Add an event and date")
        }
        card("🎨 Themes & Wallpapers") { showThemes() }
        card("🔎 Search") { searchPage() }
        card("🎀 Stickers & Fonts") {
            setup("Stickers & Fonts")
            root.addView(text("Cute stickers and font choices will be added in a future update."))
            button("Back Home") { showHome() }
        }
        button("Back Home") { showHome() }
    }

    private fun listPage(title: String, hint: String) {
        setup(title)
        val key = title
        val input = EditText(this).apply {
            this.hint = hint
            textSize = 16f
            setSingleLine(false)
            background = rounded(Color.WHITE)
            setPadding(dp(14), dp(12), dp(14), dp(12))
        }
        root.addView(input)

        button("＋ Save") {
            val value = input.text.toString().trim()
            if (value.isNotEmpty()) {
                val data = JSONArray(prefs.getString(key, "[]"))
                data.put(value)
                prefs.edit().putString(key, data.toString()).apply()
                input.text.clear()
                showItems(key, hint)
            } else {
                Toast.makeText(this, "Please enter something first", Toast.LENGTH_SHORT).show()
            }
        }

        root.addView(text("Your saved entries", 18f))
        showSaved(key)
        button("Back Home") { showHome() }
    }

    private fun showSaved(key: String) {
        val data = JSONArray(prefs.getString(key, "[]"))
        for (i in 0 until data.length()) {
            val value = data.optString(i)
            val item = TextView(this).apply {
                text = "♡  $value"
                textSize = 15f
                setTextColor(Color.rgb(80, 65, 95))
                setPadding(dp(12), dp(14), dp(12), dp(14))
                background = rounded(Color.WHITE)
            }
            root.addView(item)
        }
    }

    private fun showItems(key: String, hint: String) {
        listPage(key, hint)
    }

    private fun showThemes() {
        setup("Choose Your Theme 🎨")
        val themes = listOf(
            "Lavender Dream" to Color.rgb(250, 246, 255),
            "Pink Blossom" to Color.rgb(255, 240, 247),
            "Peach Cream" to Color.rgb(255, 244, 230),
            "Mint Garden" to Color.rgb(235, 250, 242)
        )
        themes.forEach { theme ->
            card("♡  ${theme.first}") {
                prefs.edit().putInt("theme", theme.second).apply()
                Toast.makeText(this, "${theme.first} selected!", Toast.LENGTH_SHORT).show()
                showHome()
            }
        }
        button("Back Home") { showHome() }
    }

    private fun searchPage() {
        setup("Search 🔎")
        val input = EditText(this).apply {
            hint = "Search your saved entries"
            background = rounded(Color.WHITE)
            setPadding(dp(12), dp(12), dp(12), dp(12))
        }
        root.addView(input)
        button("Search") {
            val query = input.text.toString().trim().lowercase()
            val result = StringBuilder()
            val keys = listOf(
                "My Planner", "Notes & Journal", "Goals & Habits",
                "Study Planner", "Budget Tracker", "Water Tracker",
                "Mood Tracker", "Birthdays", "Calendar Events"
            )
            keys.forEach { key ->
                val data = JSONArray(prefs.getString(key, "[]"))
                for (i in 0 until data.length()) {
                    val value = data.optString(i)
                    if (value.lowercase().contains(query) && query.isNotEmpty()) {
                        result.append("$key: $value\n\n")
                    }
                }
            }
            root.addView(text(if (result.isEmpty()) "No results found." else result.toString()))
        }
        button("Back Home") { showHome() }
    }
}
