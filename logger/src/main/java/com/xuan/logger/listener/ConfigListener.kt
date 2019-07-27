package com.xuan.logger.listener

import com.xuan.logger.utils.Config

/**
 * @packageName com.xuan.logger.listener
 * @fileName ConfigListener
 * @date 2018/12/11 0011 14:02
 * @author 宋杰
 * @describe TODO
 */
interface ConfigListener {
    fun configChange(config: Config)
}