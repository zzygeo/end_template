package com.zzy.endtemplate.controller;

import com.zzy.endtemplate.common.BaseResponse;
import com.zzy.endtemplate.common.ErrorCode;
import com.zzy.endtemplate.common.ResultUtils;
import com.zzy.endtemplate.exception.BusinessException;
import com.zzy.endtemplate.model.vo.Server;
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