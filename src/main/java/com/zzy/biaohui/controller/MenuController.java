package com.zzy.biaohui.controller;

import com.zzy.biaohui.common.BaseResponse;
import com.zzy.biaohui.common.DeleteRequest;
import com.zzy.biaohui.common.ErrorCode;
import com.zzy.biaohui.common.ResultUtils;
import com.zzy.biaohui.model.dto.menu.MenuAddRequest;
import com.zzy.biaohui.model.dto.menu.MenuUpdateRequest;
import com.zzy.biaohui.model.entity.Menu;
import com.zzy.biaohui.model.vo.MenuTree;
import com.zzy.biaohui.service.MenuService;
import com.zzy.biaohui.utils.ThrowUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/menu")
public class MenuController {
    @Autowired
    private MenuService menuService;

    @PostMapping("/add")
    public BaseResponse<Boolean> addMenu(@RequestBody MenuAddRequest menuAddRequest) {
        ThrowUtils.throwIf(menuAddRequest == null, ErrorCode.PARAMS_ERROR);
        Menu menu = new Menu();
        BeanUtils.copyProperties(menuAddRequest, menu);
        boolean b = menuService.addMenu(menu);
        ThrowUtils.throwIf(!b, ErrorCode.OPERATION_ERROR, "添加失败");
        return ResultUtils.success(true);
    }

    @PostMapping("/update")
    public BaseResponse<Boolean> updateMenu(@RequestBody MenuUpdateRequest menuUpdateRequest) {
        ThrowUtils.throwIf(menuUpdateRequest == null, ErrorCode.PARAMS_ERROR);
        Menu menu = new Menu();
        BeanUtils.copyProperties(menuUpdateRequest, menu);
        boolean b = menuService.updateMenu(menu);
        ThrowUtils.throwIf(!b, ErrorCode.OPERATION_ERROR, "更新失败");
        return ResultUtils.success(true);
    }

    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteMenu(@RequestBody DeleteRequest deleteRequest) {
        ThrowUtils.throwIf(deleteRequest == null, ErrorCode.PARAMS_ERROR);
        Menu menu = new Menu();
        BeanUtils.copyProperties(deleteRequest, menu);
        boolean b = menuService.deleteMenu(menu);
        ThrowUtils.throwIf(!b, ErrorCode.OPERATION_ERROR, "删除失败");
        return ResultUtils.success(true);
    }

    @GetMapping("/tree")
    public BaseResponse<List<MenuTree>> getMenuTree() {
        List<MenuTree> menuTreeList = menuService.getMenuTree();
        return ResultUtils.success(menuTreeList);
    }

}
