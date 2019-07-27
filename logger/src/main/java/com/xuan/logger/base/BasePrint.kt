package com.xuan.logger.base

import android.annotation.SuppressLint
import com.xuan.logger.bean.LoggerBean
import com.xuan.logger.ecs.WriteFile
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.Executors
import java.util.concurrent.LinkedBlockingQueue

/**
 * @packageName com.xuan.logger.base
 * @fileName BasePrint
 * @date 2018/12/11 0011 13:26
 * @author 宋杰
 * @describe TODO
 */
@SuppressLint("SimpleDateFormat")
abstract class BasePrint(filePath: String, maxSize: Int) : WriteFile(filePath, maxSize) {

    private val pool = Executors
        .newCachedThreadPool { r ->
            val t = Thread(r)
            t.isDaemon = true
            t
        }
    private val printQueue = LinkedBlockingQueue<LoggerBean>()
    private val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS")

    init {
        this.pool.execute {
            while (true) {
                try {
                    val logBean = this@BasePrint.printQueue.take() as LoggerBean
                    this@BasePrint.doTask(logBean)
                    this@BasePrint.write(logBean.toString())
                    Thread.yield()
                } catch (var2: InterruptedException) {
                    var2.printStackTrace()
                }

            }
        }
    }

    abstract fun doTask(logger: LoggerBean)

    @Synchronized
    open fun addTask(level: Int, tag: String, msg: String) {
        val t = Thread.currentThread().stackTrace[7]
        val date = Date()
        val bean = LoggerBean()
        bean.timeStr = this.sdf.format(date)
        bean.level = level
        bean.tag = tag
        bean.className = this.getClassName(t)
        bean.msg = msg
        bean.func = this.getFunName(t)
        bean.line = this.getLine(t)
        this.printQueue.add(bean)
    }

    private fun getClassName(t: StackTraceElement): String {
        var cls = t.className
        val clsInd = cls.lastIndexOf(".")
        cls = cls.substring(if (clsInd == -1) 0 else clsInd + 1)
        return cls
    }

    private fun getFunName(t: StackTraceElement): String {
        return t.methodName
    }

    private fun getLine(t: StackTraceElement): Int {
        return t.lineNumber
    }
}