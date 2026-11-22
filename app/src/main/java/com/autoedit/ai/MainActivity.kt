
package com.autoedit.ai

import android.app.Activity
import android.os.Bundle
import android.content.Intent
import android.net.Uri
import android.graphics.Color
import android.view.Gravity
import android.view.ViewGroup
import android.widget.*

class MainActivity : Activity() {

    private var videoUri: Uri? = null
    private var selectedStyle = "Desi Comedy"
    private lateinit var statusText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val background = Color.rgb(15, 15, 28)

        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(24, 35, 24, 24)
            setBackgroundColor(background)
        }

        val scroll = ScrollView(this)
        scroll.addView(root)

        val title = TextView(this).apply {
            text = "AUTOEDIT AI"
            textSize = 30f
            setTextColor(Color.WHITE)
            gravity = Gravity.CENTER
        }
        root.addView(title)

        val subtitle = TextView(this).apply {
            text = "Record. Upload. Auto Edit."
            textSize = 15f
            setTextColor(Color.LTGRAY)
            gravity = Gravity.CENTER
            setPadding(0, 10, 0, 35)
        }
        root.addView(subtitle)

        addButton(root, "🎥  Record Video") {
            recordVideo()
        }

        addButton(root, "📁  Upload From Gallery") {
            chooseVideo()
        }

        val heading = TextView(this).apply {
            text = "CHOOSE YOUR STYLE"
            textSize = 18f
            setTextColor(Color.WHITE)
            setPadding(0, 30, 0, 15)
        }
        root.addView(heading)

        addButton(root, "😂  Desi Comedy") {
            selectedStyle = "Desi Comedy"
            updateStatus("Selected: $selectedStyle")
        }

        addButton(root, "🔥  Motivation") {
            selectedStyle = "Motivation"
            updateStatus("Selected: $selectedStyle")
        }

        addButton(root, "🎤  Roast & Reaction") {
            selectedStyle = "Roast & Reaction"
            updateStatus("Selected: $selectedStyle")
        }

        statusText = TextView(this).apply {
            text = "Please record or upload a video."
            textSize = 15f
            setTextColor(Color.WHITE)
            gravity = Gravity.CENTER
            setPadding(0, 25, 0, 20)
        }
        root.addView(statusText)

        addButton(root, "▶  Preview Video") {
            previewVideo()
        }

        addButton(root, "⚡  Auto Edit Video") {
            if (videoUri == null) {
                updateStatus("Please select a video first.")
            } else {
                updateStatus(
                    "Video ready for $selectedStyle editing. " +
                    "AI editing engine will be added in the next development stage."
                )
            }
        }

        val footer = TextView(this).apply {
            text = "AutoEdit AI • Version 1.0"
            textSize = 12f
            setTextColor(Color.GRAY)
            gravity = Gravity.CENTER
            setPadding(0, 30, 0, 10)
        }
        root.addView(footer)

        setContentView(scroll)
    }

    private fun addButton(
        parent: LinearLayout,
        label: String,
        action: () -> Unit
    ) {
        val button = Button(this).apply {
            text = label
            textSize = 16f
            setTextColor(Color.WHITE)
            setBackgroundColor(Color.rgb(91, 57, 190))
            isAllCaps = false
            setPadding(12, 12, 12, 12)
            setOnClickListener { action() }
        }

        val params = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        params.setMargins(0, 8, 0, 8)
        parent.addView(button, params)
    }

    private fun recordVideo() {
        val intent = Intent(Intent.ACTION_VIDEO_CAPTURE)
        if (intent.resolveActivity(packageManager) != null) {
            startActivityForResult(intent, 101)
        } else {
            updateStatus("No camera app found.")
        }
    }

    private fun chooseVideo() {
        val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
            type = "video/*"
        }
        startActivityForResult(
            Intent.createChooser(intent, "Select Video"),
            102
        )
    }

    private fun previewVideo() {
        val uri = videoUri
        if (uri == null) {
            updateStatus("Please select a video first.")
            return
        }

        val intent = Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(uri, "video/*")
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }

        try {
            startActivity(intent)
        } catch (e: Exception) {
            updateStatus("Unable to open video preview.")
        }
    }

    @Deprecated("Legacy activity result API")
    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK && data?.data != null) {
            videoUri = data.data
            updateStatus("Video selected successfully!")
        }
    }

    private fun updateStatus(message: String) {
        statusText.text = message
    }
}
