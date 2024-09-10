package com.zzy.endtemplate.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zzy.endtemplate.model.dto.logininfo.LoginInfoQueryRequest;
import com.zzy.endtemplate.model.entity.LoginInfo;

/**
* @author zzy
* @description 针对表【login_info】的数据库操作Service
* @createDate 2024-09-09 17:49:54
*/
public interface LoginInfoService extends IService<LoginInfo> {
    /**
     * 分页查询
     * @param loginForPageRequest
     * @return
     */
    Page<LoginInfo> pageLoginFor(LoginInfoQueryRequest loginForPageRequest);

    // todo 导出日志
}
