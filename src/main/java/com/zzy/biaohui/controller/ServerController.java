package com.zzy.biaohui.controller;

import com.zzy.biaohui.common.BaseResponse;
import com.zzy.biaohui.common.ErrorCode;
import com.zzy.biaohui.common.ResultUtils;
import com.zzy.biaohui.exception.BusinessException;
import com.zzy.biaohui.model.vo.Server;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/server")
public class ServerController {

    @GetMapping("/getServerInfo")
    public BaseResponse<Server> getServerInfo() {
        Server server = new Server();
        try {
            server.copyTo();
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR);
        }
        return ResultUtils.success(server);
    }
}