package com.blank.system.mapper

import com.blank.common.core.constant.UserConstants
import com.blank.common.mybatis.core.mapper.BaseMapperPlus
import com.blank.system.domain.SysMenu
import com.blank.system.domain.table.SysMenuDef
import com.blank.system.domain.vo.SysMenuVo
import com.mybatisflex.core.query.QueryWrapper
import org.apache.ibatis.annotations.Param

/**
 * 菜单表 数据层
 */
interface SysMenuMapper : BaseMapperPlus<SysMenu, SysMenuVo> {
    /**
     * 根据用户所有权限
     *
     * @return 权限列表
     */
    fun selectMenuPerms(): MutableList<String>?

    /**
     * 根据用户查询系统菜单列表
     *
     * @param queryWrapper 查询条件
     * @return 菜单列表
     */
    fun selectMenuListByUserId(queryWrapper: QueryWrapper): MutableList<SysMenu>?

    /**
     * 根据用户ID查询权限
     *
     * @param userId 用户ID
     * @return 权限列表
     */
    fun selectMenuPermsByUserId(userId: Long): MutableList<String>?

    /**
     * 根据角色ID查询权限
     *
     * @param roleId 角色ID
     * @return 权限列表
     */
    fun selectMenuPermsByRoleId(roleId: Long): MutableList<String>?

    /**
     * 根据用户ID查询菜单
     *
     * @return 菜单列表
     */
    fun selectMenuTreeAll(): MutableList<SysMenu>? {
        val def = SysMenuDef.SYS_MENU
        return this.selectListByQuery(
            QueryWrapper().select()
                .where {
                    def.MENU_TYPE.`in`(
                        UserConstants.TYPE_DIR, UserConstants.TYPE_MENU
                    )
                    def.STATUS.eq(UserConstants.MENU_NORMAL)
                }
                .orderBy(def.PARENT_ID, true)
                .orderBy(def.ORDER_NUM, true)
        )
    }

    /**
     * 根据用户ID查询菜单
     *
     * @param userId 用户ID
     * @return 菜单列表
     */
    fun selectMenuTreeByUserId(userId: Long): MutableList<SysMenu>?

    /**
     * 根据角色ID查询菜单树信息
     *
     * @param roleId            角色ID
     * @param menuCheckStrictly 菜单树选择项是否关联显示
     * @return 选中菜单列表
     */
    fun selectMenuListByRoleId(
        @Param("roleId") roleId: Long,
        @Param("menuCheckStrictly") menuCheckStrictly: Boolean
    ): MutableList<Long>?
}
