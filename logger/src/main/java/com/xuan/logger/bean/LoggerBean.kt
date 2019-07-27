package com.xuan.logger.bean

import android.content.Context
import android.content.SharedPreferences

/**
 * @packageName com.xuan.logger.bean
 * @fileName LoggerBean
 * @date 2018/12/11 0011 13:04
 * @author 宋杰
 * @describe TODO
 */
data class LoggerBean(
    var seq: Int = 0,
    var time: Float = 0.0f,
    var timeStr: String = "",
    var msg: String = "",
    var className: String = "LoggerBean",
    var line: Int = 0,
    var level: Int = 0,
    var func: String = "",
    var tag: String = ""
) {

    var BEANCOUNT = 0

    lateinit var mSharedPreferences: SharedPreferences

    override fun toString(): String {
        val sb = StringBuffer(this.timeStr)
        sb.append(' ')
            .append('{')
            .append(this.className)
            .append('.')
            .append(this.func)
            .append('}')
            .append('[')
            .append(this.getLevel(this.level))
            .append(']')
            .append('[')
            .append(this.tag)
            .append(']')
            .append(' ')
            .append(this.msg)
            .append("\n")
        return sb.toString()
    }

    fun increment() {}

    fun INIT_SEQ(context: Context) {
        mSharedPreferences = context.getSharedPreferences("log_seq", 0)
        BEANCOUNT = mSharedPreferences.getInt("log_seq", 0)
    }

    fun SET_SEQ_SP(v: Int) {
        val editor = mSharedPreferences.edit()
        editor.putInt("log_seq", v)
        editor.apply()
    }

    private fun getLevel(level: Int): String {
        return when (level) {
            2 -> "V"
            3 -> "D"
            4 -> "I"
            5 -> "W"
            6 -> "E"
            7 -> "A"
            else -> "D"
        }
    }
}