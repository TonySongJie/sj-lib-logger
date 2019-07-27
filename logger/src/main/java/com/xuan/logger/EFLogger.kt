package com.xuan.logger

import android.text.TextUtils

/**
 * @packageName com.xuan.logger
 * @fileName EFLogger
 * @date 2018/12/11 0011 13:02
 * @author 宋杰
 * @describe TODO
 */
object EFLogger {

    const val sCustomTagPrefix = "EF_Logger"

    private var DEBUG: Boolean = false

    fun init(filePath: String) {
        ECS.init(filePath)
    }

    fun isDebug(debug: Boolean) {
        DEBUG = debug
    }

    fun e(desc: String) {
        if (DEBUG) {
            val tag = generateTag()
            ECS.e(tag, desc)
        }
    }

    fun e(tag: String, desc: String) {
        if (DEBUG) {
            ECS.e(tag, desc)
        }
    }

    fun w(tag: String, desc: String) {
        if (DEBUG) {
            ECS.w(tag, desc)
        }
    }

    fun w(desc: String) {
        if (DEBUG) {
            val tag = generateTag()
            ECS.w(tag, desc)
        }
    }

    fun i(tag: String, desc: String) {
        if (DEBUG) {
            ECS.i(tag, desc)
        }
    }

    fun i(desc: String) {
        if (DEBUG) {
            val tag = generateTag()
            ECS.i(tag, desc)
        }
    }

    fun d(tag: String, desc: String) {
        if (DEBUG) {
            ECS.d(tag, desc)
        }
    }

    fun d(desc: String) {
        if (DEBUG) {
            val tag = generateTag()
            ECS.d(tag, desc)
        }
    }

    fun v(tag: String, desc: String) {
        if (DEBUG) {
            ECS.v(tag, desc)
        }
    }

    fun v(desc: String) {
        if (DEBUG) {
            val tag = generateTag()
            ECS.v(tag, desc)
        }
    }

    private fun generateTag(): String {
        val caller = Throwable().stackTrace[2]
        var tag = "%s.%s(L:%d)"
        var callerClazzName = caller.className
        callerClazzName = callerClazzName.substring(callerClazzName.lastIndexOf(".") + 1)
        tag = String.format(tag, callerClazzName, caller.methodName, caller.lineNumber)
        tag = if (TextUtils.isEmpty(sCustomTagPrefix)) tag else "$sCustomTagPrefix:$tag"
        return tag
    }
}