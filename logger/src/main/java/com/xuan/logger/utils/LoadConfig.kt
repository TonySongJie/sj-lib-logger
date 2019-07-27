package com.xuan.logger.utils

import android.util.Log
import com.xuan.logger.ecs.WriteFile
import com.xuan.logger.listener.ConfigListener
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.io.InputStream
import java.util.*
import java.util.concurrent.Executors

/**
 * @packageName com.xuan.logger.utils
 * @fileName LoadConfig
 * @date 2018/12/11 0011 13:40
 * @author 宋杰
 * @describe TODO
 */
object LoadConfig {

    private const val TAG = "LoadConfig"
    private const val FILE_PATH = "/assets/"
    private const val FILE_NAME = "config.properties"

    private val properties = Properties()
    private val config = Config()
    private var filePath: String? = ""
    private val listeners = ArrayList<ConfigListener>()
    private var lastModified: Long = 0
    private var modifyListener: Thread? = null

    fun loadConfigFile(filePath: String?): Boolean {
        var file = filePath
        var result = false
        var isSystem = true
        if (file != null && "" != file) {
            if (!file.endsWith("config/config.properties")) {
                if (!file.endsWith("/")) {
                    file = "$file/"
                }

                if (file == "/assets/") {
                    file += "config.properties"
                } else {
                    file += "config/config.properties"
                    isSystem = false
                }

                this.filePath = file
            } else {
                isSystem = false
            }

            var inputStream: Any? = null

            try {
                inputStream = if (isSystem) {
                    LoadConfig::class.java.getResourceAsStream(file)
                } else {
                    FileInputStream(file)
                }

                if (inputStream != null) {
                    this.properties.load(inputStream)
                    this.parseProperty(this.properties)
                    this.properties.clear()
                    result = true
                }

                inputStream = LoadConfig.javaClass.getResourceAsStream("/assets/config.properties")
                val finalIs = inputStream
                val finalFile = file
                Executors.newCachedThreadPool().submit {
                    WriteFile.inputStreamToFile(finalIs as InputStream, WriteFile.createFile(finalFile))
                    this@LoadConfig.listenerEntyFile()
                }
            } catch (e: IOException) {
                e.printStackTrace()
                result = false
            } finally {
                if (inputStream != null) {
                    try {
                        (inputStream as InputStream).close()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            }

            this.listenerEntyFile()
            return result
        } else {
            return false
        }
    }

    private fun listenerEntyFile() {
        if (this.filePath != "/assets/config.properties") {
            this.lastModified = File(this.filePath!!).lastModified()
            if (this.modifyListener == null) {
                this.modifyListener = Thread(Runnable {
                    while (true) {
                        this@LoadConfig.sleep(5000L)
                        if (this@LoadConfig.filePath == null || "" == this@LoadConfig.filePath) {
                            return@Runnable
                        }

                        val checkFile = File(this@LoadConfig.filePath!!)
                        if (this@LoadConfig.lastModified != checkFile.lastModified()) {
                            this@LoadConfig.loadConfigFile(this@LoadConfig.filePath)
                            WriteFile.copyFile(checkFile, File("/assets/config.properties"))
                            this@LoadConfig.lastModified = checkFile.lastModified()
                            Log.d("LoadConfig", "file has modified")
                            this@LoadConfig.notifyConfigChange()
                        }
                    }
                })
                this.modifyListener!!.start()
                Log.d("LoadConfig", "file listener start")
            }
        }

    }

    private fun parseProperty(properties: Properties) {
        val debugStr = properties.getProperty("debug", "false").trim().toLowerCase()
        if (debugStr == "true" || debugStr == "t") {
            this.config.debug = true
        }

        this.config.registServer = properties.getProperty("regist_ip_addr", "")
        this.config.registServer = properties.getProperty("send_log_ip", "")
    }

    fun getConfig(): Config {
        return this.config
    }

    fun regConfigChange(listener: ConfigListener) {
        if (this.listeners.contains(listener)) {
            this.listeners.add(listener)
        }
    }

    fun unRegConfigChange(listener: ConfigListener) {
        if (this.listeners.contains(listener)) {
            this.listeners.remove(listener)
        }

    }

    private fun notifyConfigChange() {
        if (!this.listeners.isEmpty()) {
            val iterator = this.listeners.iterator()

            while (iterator.hasNext()) {
                val listener = iterator.next()
                listener.configChange(this.config)
            }

        }
    }

    private fun sleep(time: Long) {
        try {
            Thread.sleep(time)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }
}