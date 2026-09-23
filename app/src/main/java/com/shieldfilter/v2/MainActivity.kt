package com.shieldfilter.v2

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.shieldfilter.v2.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private var password: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (!Settings.canDrawOverlays(this)) {
            startActivity(Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:$packageName")))
        }

        findViewById<Button>(R.id.btnSetPwd).setOnClickListener {
            val et = EditText(this)
            AlertDialog.Builder(this)
                .setTitle("设置密码")
                .setView(et)
                .setPositiveButton("确定") { _, _ -> password = et.text.toString() }
                .show()
        }

        findViewById<Button>(R.id.btnStart).setOnClickListener {
            if (password.isNullOrEmpty()) {
                AlertDialog.Builder(this).setMessage("请先设置密码").show()
            } else {
                AlertDialog.Builder(this).setMessage("防护已启动（演示版）").show()
            }
        }
    }
}
