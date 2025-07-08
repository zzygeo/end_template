package com.zzy.biaohui.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zzy.biaohui.model.entity.Menu;
import com.zzy.biaohui.model.vo.MenuTree;

import java.util.List;

/**
* @author zzy
* @description 针对表【menu】的数据库操作Service
* @createDate 2025-07-04 15:25:32
*/
public interface MenuService extends IService<Menu> {
    boolean addMenu(Menu menu);

    boolean updateMenu(Menu menu);

    boolean deleteMenu(Menu menu);

    // 目录树
    List<MenuTree> getMenuTree();
}
