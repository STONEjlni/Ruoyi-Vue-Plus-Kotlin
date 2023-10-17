package com.blank.common.mybatis.core.page

import cn.hutool.core.util.ObjectUtil
import com.mybatisflex.core.paginate.Page
import java.io.Serial
import java.io.Serializable

/**
 * 分页查询实体类
 */
class PageQuery : Serializable {
    companion object {
        @Serial
        private const val serialVersionUID = 1L
    }

    /**
     * 分页大小
     */
    var pageSize: Int? = null

    /**
     * 当前页数
     */
    var pageNum: Int? = null

    /**
     * 排序列
     */
    var orderByColumn: String? = null

    /**
     * 排序的方向desc或者asc
     */
    var isAsc: String? = null

    /**
     * 当前记录起始索引 默认值
     */
    var DEFAULT_PAGE_NUM = 1

    /**
     * 每页显示记录数 默认值 默认查全部
     */
    var DEFAULT_PAGE_SIZE = Int.MAX_VALUE

    fun <T> build(): Page<T> {
        var pageNum: Int =
            ObjectUtil.defaultIfNull(pageNum, DEFAULT_PAGE_NUM)
        val pageSize: Int =
            ObjectUtil.defaultIfNull(pageSize, DEFAULT_PAGE_SIZE)
        if (pageNum <= 0) {
            pageNum = DEFAULT_PAGE_NUM
        }
        return Page(pageNum, pageSize)
    }

    /**
     * 构建排序
     *
     * 支持的用法如下:
     * {isAsc:"asc",orderByColumn:"id"} order by id asc
     * {isAsc:"asc",orderByColumn:"id,createTime"} order by id asc,create_time asc
     * {isAsc:"desc",orderByColumn:"id,createTime"} order by id desc,create_time desc
     * {isAsc:"asc,desc",orderByColumn:"id,createTime"} order by id asc,create_time desc
     */
    /*private List<OrderItem> buildOrderItem() {
        if (StrUtil.isBlank(orderByColumn) || StrUtil.isBlank(isAsc)) {
            return null;
        }
        String orderBy = SqlUtil.escapeOrderBySql(orderByColumn);
        orderBy = StringUtils.toUnderScoreCase(orderBy);

        // 兼容前端排序类型
        isAsc = StringUtils.replaceEach(isAsc, new String[]{"ascending", "descending"}, new String[]{"asc", "desc"});

        String[] orderByArr = orderBy.split(StringUtilsExtend.SEPARATOR);
        String[] isAscArr = isAsc.split(StringUtilsExtend.SEPARATOR);
        if (isAscArr.length != 1 && isAscArr.length != orderByArr.length) {
            throw new ServiceException("排序参数有误");
        }

        List<OrderItem> list = new ArrayList<>();
        // 每个字段各自排序
        for (int i = 0; i < orderByArr.length; i++) {
            String orderByStr = orderByArr[i];
            String isAscStr = isAscArr.length == 1 ? isAscArr[0] : isAscArr[i];
            if ("asc".equals(isAscStr)) {
                list.add(OrderItem.asc(orderByStr));
            } else if ("desc".equals(isAscStr)) {
                list.add(OrderItem.desc(orderByStr));
            } else {
                throw new ServiceException("排序参数有误");
            }
        }
        return list;
    }*/
}
