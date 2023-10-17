package com.blank.system.listener

import cn.hutool.crypto.digest.BCrypt
import cn.hutool.extra.spring.SpringUtil
import com.blank.common.core.annotation.Slf4j
import com.blank.common.satoken.utils.LoginHelper.getUserId
import com.blank.system.service.ISysConfigService
import com.blank.system.service.ISysUserService

/**
 * 系统用户自定义导入
 */
@Slf4j
class SysUserImportListener /*extends AnalysisEventListener<SysUserImportVo> implements ExcelListener<SysUserImportVo>*/(
    isUpdateSupport: Boolean
) {
    private val userService: ISysUserService
    private val password: String
    private val isUpdateSupport: Boolean
    private val operUserId: Long?
    private val successNum = 0
    private val failureNum = 0
    private val successMsg = StringBuilder()
    private val failureMsg = StringBuilder()

    init {
        val initPassword = SpringUtil.getBean(ISysConfigService::class.java).selectConfigByKey("sys.user.initPassword")
        userService = SpringUtil.getBean(ISysUserService::class.java)
        password = BCrypt.hashpw(initPassword)
        this.isUpdateSupport = isUpdateSupport
        operUserId = getUserId()
    } /*@Override
    public void invoke(SysUserImportVo userVo, AnalysisContext context) {
        SysUserVo sysUser = this.userService.selectUserByUserName(userVo.getUserName());
        try {
            // 验证是否存在这个用户
            if (ObjectUtil.isNull(sysUser)) {
                SysUserBo user = BeanUtil.toBean(userVo, SysUserBo.class);
                ValidatorUtils.validate(user);
                user.setPassword(password);
                user.setCreateBy(operUserId);
                userService.insertUser(user);
                successNum++;
                successMsg.append("<br/>").append(successNum).append("、账号 ").append(user.getUserName()).append(" 导入成功");
            } else if (isUpdateSupport) {
                Long userId = sysUser.getUserId();
                SysUserBo user = BeanUtil.toBean(userVo, SysUserBo.class);
                user.setUserId(userId);
                ValidatorUtils.validate(user);
                userService.checkUserAllowed(user.getUserId());
                userService.checkUserDataScope(user.getUserId());
                user.setUpdateBy(operUserId);
                userService.updateUser(user);
                successNum++;
                successMsg.append("<br/>").append(successNum).append("、账号 ").append(user.getUserName()).append(" 更新成功");
            } else {
                failureNum++;
                failureMsg.append("<br/>").append(failureNum).append("、账号 ").append(sysUser.getUserName()).append(" 已存在");
            }
        } catch (Exception e) {
            failureNum++;
            String msg = "<br/>" + failureNum + "、账号 " + userVo.getUserName() + " 导入失败：";
            failureMsg.append(msg).append(e.getMessage());
            log.error(msg, e);
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {

    }

    @Override
    public ExcelResult<SysUserImportVo> getExcelResult() {
        return new ExcelResult<>() {

            @Override
            public String getAnalysis() {
                if (failureNum > 0) {
                    failureMsg.insert(0, "很抱歉，导入失败！共 " + failureNum + " 条数据格式不正确，错误如下：");
                    throw new ServiceException(failureMsg.toString());
                } else {
                    successMsg.insert(0, "恭喜您，数据已全部导入成功！共 " + successNum + " 条，数据如下：");
                }
                return successMsg.toString();
            }

            @Override
            public List<SysUserImportVo> getList() {
                return null;
            }

            @Override
            public List<String> getErrorList() {
                return null;
            }
        };
    }*/
}
