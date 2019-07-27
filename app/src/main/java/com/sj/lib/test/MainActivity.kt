package com.sj.lib.test

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.xuan.logger.EFLogger

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        EFLogger.d("测试日志打印库")
    }
}
