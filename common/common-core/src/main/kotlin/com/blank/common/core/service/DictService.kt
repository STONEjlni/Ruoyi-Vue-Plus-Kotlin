package com.blank.common.core.service

/**
 * 通用 字典服务
 */
@JvmDefaultWithoutCompatibility
interface DictService {

    companion object {
        /**
         * 分隔符
         */
        const val SEPARATOR = ","
    }

    /**
     * 根据字典类型和字典值获取字典标签
     *
     * @param dictType  字典类型
     * @param dictValue 字典值
     * @param separator 分隔符
     * @return 字典标签
     */
    fun getDictLabel(dictType: String, dictValue: String, separator: String): String?

    /**
     * 根据字典类型和字典标签获取字典值
     *
     * @param dictType  字典类型
     * @param dictLabel 字典标签
     * @param separator 分隔符
     * @return 字典值
     */
    fun getDictValue(dictType: String, dictLabel: String, separator: String): String?

    /**
     * 获取字典下所有的字典值与标签
     *
     * @param dictType 字典类型
     * @return dictValue为key，dictLabel为值组成的Map
     */
    fun getAllDictByDictType(dictType: String): MutableMap<String, String>?

    /**
     * 根据字典类型和字典值获取字典标签
     *
     * @param dictType  字典类型
     * @param dictValue 字典值
     * @return 字典标签
     */
    fun getDictLabel(dictType: String, dictValue: String): String? {
        return getDictLabel(dictType, dictValue, SEPARATOR)
    }

    /**
     * 根据字典类型和字典标签获取字典值
     *
     * @param dictType  字典类型
     * @param dictLabel 字典标签
     * @return 字典值
     */
    fun getDictValue(dictType: String, dictLabel: String): String? {
        return getDictValue(dictType, dictLabel, SEPARATOR)
    }
}
