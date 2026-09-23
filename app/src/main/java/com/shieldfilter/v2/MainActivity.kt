package com.shieldfilter.v2

import android.content.Intent
import android.media.projection.MediaProjectionManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.shieldfilter.v2.model.*
import com.shieldfilter.v2.service.ScreenCaptureService

class MainActivity : AppCompatActivity() {
    private var password: String? = null
    private val REQ_SCREEN_CAPTURE = 1002

    private val styles = mapOf(
        FemaleBodyPart.EYES to PartStyle(FemaleBodyPart.EYES, true, BlockMode.BLACK_WITH_TEXT),
        FemaleBodyPart.CHEST to PartStyle(FemaleBodyPart.CHEST, true, BlockMode.MOSAIC),
        FemaleBodyPart.ARMPITS to PartStyle(FemaleBodyPart.ARMPITS, true, BlockMode.BLACK_SOLID),
        FemaleBodyPart.PELVIS to PartStyle(FemaleBodyPart.PELVIS, true, BlockMode.BLACK_WITH_TEXT),
        FemaleBodyPart.FEET to PartStyle(FemaleBodyPart.FEET, true, BlockMode.MOSAIC)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (!Settings.canDrawOverlays(this)) {
            val i = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:$packageName"))
            startActivity(i)
        }

        findViewById<android.widget.Button>(R.id.btnSetPwd).setOnClickListener {
            val et = EditText(this)
            AlertDialog.Builder(this)
                .setTitle("设置解锁密码")
                .setView(et)
                .setPositiveButton("确定") {_,_ -> password = et.text.toString() }
                .show()
        }

        findViewById<android.widget.Button>(R.id.btnStart).setOnClickListener {
            if(password.isNullOrEmpty()){
                AlertDialog.Builder(this).setMessage("请先设置解锁密码").show()
                return@setOnClickListener
            }
            val mpm = getSystemService(MEDIA_PROJECTION_SERVICE) as MediaProjectionManager
            startActivityForResult(mpm.createScreenCaptureIntent(), REQ_SCREEN_CAPTURE)
        }
    }

    override fun onActivityResult(rq: Int, res: Int, data: Intent?) {
        super.onActivityResult(rq,res,data)
        if(rq == REQ_SCREEN_CAPTURE && res == RESULT_OK && data != null){
            val svc = Intent(this, ScreenCaptureService::class.java).apply {
                putExtra("RESULT_CODE", res)
                putExtra("PROJECTION_DATA", data)
            }
            startForegroundService(svc)
        }
    }
}
