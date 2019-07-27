package com.xuan.logger

import com.xuan.logger.ecs.PrintTask
import com.xuan.logger.utils.Config
import com.xuan.logger.utils.LoadConfig

/**
 * @packageName com.xuan.logger
 * @fileName ECS
 * @date 2018/12/11 0011 13:25
 * @author 宋杰
 * @describe TODO
 */
object ECS {
    const val LOG_FILE_NAME = "ECS.txt"

    private var config: Config? = null
    private var printTask: PrintTask? = null

    fun init(filePath: String) {
        init(filePath, 10485760)
    }

    fun init(filePath: String, maxSize: Int) {
        this.printTask = null
        this.config = LoadConfig.getConfig()
        if (this.printTask == null) {
            this.printTask = PrintTask(filePath, maxSize)
            val sb = StringBuffer("\n")
            sb.append("**********************************************************************\n")
                .append("*********************  ECS version 2.0.3  ****************************\n")
                .append("**********************************************************************\n")
            this.print(4, "ECS", sb.toString())
        }
    }

    fun d(tag: String, msg: String) {
        this.print(3, tag, msg)
    }

    fun e(tag: String, msg: String) {
        this.print(6, tag, msg)
    }

    fun i(tag: String, msg: String) {
        this.print(4, tag, msg)
    }

    fun w(tag: String, msg: String) {
        this.print(5, tag, msg)
    }

    fun v(tag: String, msg: String) {
        this.print(2, tag, msg)
    }

    private fun print(level: Int, tag: String, msg: String) {
        this.printTask!!.addTask(level, tag, msg)
    }
}