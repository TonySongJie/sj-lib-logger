package com.xuan.logger.ecs

import android.util.Log
import com.xuan.logger.base.BasePrint
import com.xuan.logger.bean.LoggerBean

/**
 * @packageName com.xuan.logger.ecs
 * @fileName PrintTask
 * @date 2018/12/11 0011 13:35
 * @author 宋杰
 * @describe TODO
 */
class PrintTask(filePath: String, maxSize: Int) : BasePrint(filePath, maxSize) {

    override fun doTask(logger: LoggerBean) {
    }

    override fun addTask(level: Int, tag: String, msg: String) {
        Log.println(level, tag, msg)
        super.addTask(level, tag, msg)
    }
}