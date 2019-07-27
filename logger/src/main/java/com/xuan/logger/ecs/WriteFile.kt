package com.xuan.logger.ecs

import com.xuan.logger.ECS
import java.io.*


/**
 * @packageName com.xuan.logger.ecs
 * @fileName WriteFile
 * @date 2018/12/11 0011 13:16
 * @author 宋杰
 * @describe TODO
 */
open class WriteFile(filePath: String, maxSize: Int) {

    private var mMaxSize = maxSize.toLong()
    private val mFilePath = filePath
    private var isCreateFile = true

    companion object {
        fun inputStreamToFile(inputStream: InputStream?, file: File?) {
            if (inputStream != null && file != null) {
                var br: BufferedReader? = null
                var bw: BufferedWriter? = null
                var line: String?

                try {
                    br = BufferedReader(InputStreamReader(inputStream))
                    bw = BufferedWriter(FileWriter(file))

                    do {
                        line = br.readLine()
                        if (line != null) {
                            bw.write(line)
                            bw.write("\n")
                        }
                    } while (true)

                } catch (e: IOException) {
                    e.printStackTrace()
                } finally {
                    closeBufferedFileReader(br)
                    closeBufferedFileWriter(bw)
                }
            }
        }

        fun createFile(file: String?): File? {
            if (file != null && "" != file) {
                val createFile = File(file)
                if (createFile.exists()) {
                    return createFile
                } else {
                    val parent = createFile.parentFile
                    if (!parent.exists()) {
                        parent.mkdir()
                    }

                    try {
                        createFile.createNewFile()
                    } catch (var4: IOException) {
                        var4.printStackTrace()
                    }

                    return createFile
                }
            } else {
                return null
            }
        }

        fun copyFile(oldFile: File?, newFile: File?) {
            if (oldFile != null && newFile != null) {
                try {
                    inputStreamToFile(FileInputStream(oldFile), newFile)
                } catch (var3: FileNotFoundException) {
                    var3.printStackTrace()
                }

            }
        }

        private fun closeBufferedFileWriter(bw: BufferedWriter?) {
            if (bw != null) {
                try {
                    bw.close()
                } catch (var2: IOException) {
                    var2.printStackTrace()
                }

            }

        }

        private fun closeBufferedFileReader(br: BufferedReader?) {
            if (br != null) {
                try {
                    br.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }

    fun write(sb: String) {
        val file = this.checkFile(this.mFilePath)
        if (file != null) {
            var raf: RandomAccessFile? = null

            try {
                raf = RandomAccessFile(file, "rw")
                raf.seek(file.length())
                raf.write(sb.toByteArray())
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            } finally {
                if (raf != null) {
                    try {
                        raf.close()
                    } catch (var14: IOException) {
                        var14.printStackTrace()
                    }
                }
            }
        }
    }


    private fun createCpyFile(oldFile: File?, deleteOld: Boolean): File? {
        if (oldFile == null) {
            return null
        } else {
            val newFile: File?

            try {
                newFile = File(oldFile.canonicalPath + ".bak")
                if (newFile.exists()) {
                    newFile.delete()
                }

                copyFile(oldFile, newFile)
                if (deleteOld) {
                    oldFile.delete()
                }
            } catch (var4: IOException) {
                var4.printStackTrace()
            }

            return oldFile
        }
    }

    private fun checkFile(filePath: String?): File? {
        if (filePath != null && "" != filePath && this.isCreateFile) {
            val fileName =
                if (filePath.endsWith("/")) filePath + ECS.LOG_FILE_NAME else filePath + "/" + ECS.LOG_FILE_NAME
            var file: File? = File(fileName)
            if (!file!!.exists()) {
                try {
                    if (!file.parentFile.exists()) {
                        file.parentFile.mkdirs()
                    }

                    val result = file.createNewFile()
                    if (!result) {
                        return null
                    }
                } catch (var6: IOException) {
                    var6.printStackTrace()
                    this.isCreateFile = false
                }

            }

            val size = file.length()
            if (size > this.mMaxSize) {
                file = createCpyFile(file, true)
            }

            return if (file!!.exists()) file else null
        } else {
            return null
        }
    }
}