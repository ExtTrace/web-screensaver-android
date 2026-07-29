package com.web.screensaver

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.webkit.URLUtil
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.web.screensaver.R
import com.web.screensaver.databinding.ActivityDreamSettingsBinding

/**
 * DreamSettingsActivity - Allows the user to configure the screensaver URL.
 *
 * Launched via the "Settings" button in Android's screensaver configuration screen.
 */
class DreamSettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDreamSettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDreamSettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Adjust top padding to account for status bar / notch dynamically
        androidx.core.view.ViewCompat.setOnApplyWindowInsetsListener(binding.rootScrollView) { view, insets ->
            val statusBarTop = insets.getInsets(androidx.core.view.WindowInsetsCompat.Type.statusBars()).top
            view.setPadding(view.paddingLeft, statusBarTop + 16, view.paddingRight, view.paddingBottom)
            insets
        }

        // Load saved URL (or default)
        val prefs = getSharedPreferences(ClockDreamService.PREFS_NAME, MODE_PRIVATE)
        val savedUrl = prefs.getString(ClockDreamService.PREF_URL, ClockDreamService.DEFAULT_URL)
        binding.editTextUrl.setText(savedUrl)

        // Save button: validate and persist the URL synchronously
        binding.btnSave.setOnClickListener {
            val inputUrl = binding.editTextUrl.text.toString().trim()

            if (!URLUtil.isValidUrl(inputUrl)) {
                Toast.makeText(this, getString(R.string.invalid_url_message), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Use commit() (synchronous) instead of apply() to guarantee write before use
            val saved = prefs.edit()
                .putString(ClockDreamService.PREF_URL, inputUrl)
                .commit()

            if (saved) {
                Toast.makeText(this, getString(R.string.saved_message), Toast.LENGTH_SHORT).show()
                // Do NOT call finish() — let user click Preview immediately after saving
            } else {
                Toast.makeText(this, "Failed to save URL", Toast.LENGTH_SHORT).show()
            }
        }

        // Preview button: always reads from EditText (reflects what user typed, saved or not)
        binding.btnPreview.setOnClickListener {
            val inputUrl = binding.editTextUrl.text.toString().trim()
            if (URLUtil.isValidUrl(inputUrl)) {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(inputUrl)))
            } else {
                Toast.makeText(this, getString(R.string.invalid_url_message), Toast.LENGTH_SHORT).show()
            }
        }

        // Open Android System Screensaver Settings
        binding.btnSystemScreensaver.setOnClickListener {
            try {
                startActivity(Intent(android.provider.Settings.ACTION_DREAM_SETTINGS))
            } catch (e: Exception) {
                Toast.makeText(this, "Could not open Screensaver Settings", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
