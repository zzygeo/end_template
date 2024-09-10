package com.zzy.endtemplate.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zzy.endtemplate.model.dto.operlog.OperlogQueryRequest;
import com.zzy.endtemplate.model.entity.OperLog;

/**
* @author zzy
* @description 针对表【oper_log(日志操作表)】的数据库操作Service
* @createDate 2024-09-09 17:48:58
*/
public interface OperLogService extends IService<OperLog> {
    /**
     * 分页查询日志
     * @param pageOperLog
     * @return
     */
    public Page<OperLog> pageOperLog(OperlogQueryRequest pageOperLog);

    /**
     * 添加日志
     * @param operLog
     * @return
     */
    public boolean addOperLog(OperLog operLog);

    /**
     * 批量删除日志
     * @param ids
     * @return
     */
    public boolean deleteOperLog(Long[] ids);

    /**
     * 根据id查询日志
     * @param id
     * @return
     */
    public OperLog getOperLogById(Long id);

    /**
     * 清空日志
     * @return
     */
    public boolean clearOperLog();

    // todo 导出日志
}
