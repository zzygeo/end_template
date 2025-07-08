package com.zzy.biaohui.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zzy.biaohui.common.ErrorCode;
import com.zzy.biaohui.exception.BusinessException;
import com.zzy.biaohui.mapper.MenuMapper;
import com.zzy.biaohui.model.entity.Menu;
import com.zzy.biaohui.model.vo.MenuTree;
import com.zzy.biaohui.service.MenuService;
import com.zzy.biaohui.service.SysFileService;
import com.zzy.biaohui.utils.ThrowUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;


/**
* @author zzy
* @description 针对表【menu】的数据库操作Service实现
* @createDate 2025-07-04 15:25:32
*/
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu>
    implements MenuService{

    @Autowired
    private SysFileService sysFileService;

    @Override
    public boolean addMenu(Menu menu) {
        ThrowUtils.throwIf(menu == null, ErrorCode.PARAMS_ERROR);
        Long parentId = menu.getParentId();
        String name = menu.getName();
        ThrowUtils.throwIf(parentId == null, ErrorCode.PARAMS_ERROR, "parentId不能为空");
        ThrowUtils.throwIf(StringUtils.isBlank(name), ErrorCode.PARAMS_ERROR, "name不能为空");
        String iconUrl = menu.getIconUrl();
        if (StringUtils.isNotBlank(iconUrl) && iconUrl.length() > 255) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "iconUrl长度不能超过255个字符");
        }
        return this.save(menu);
    }

    @Override
    public boolean updateMenu(Menu menu) {
        ThrowUtils.throwIf(menu == null, ErrorCode.PARAMS_ERROR);
        Long id = menu.getId();
        ThrowUtils.throwIf(id == null, ErrorCode.PARAMS_ERROR, "id不能为空");
        Menu menuDB = this.getById(id);
        ThrowUtils.throwIf(menuDB == null, ErrorCode.NOT_FOUND_ERROR, "数据不存在");
        if (menu.getName() != null && StringUtils.isBlank(menu.getName())) {
            // 空白或者空字符是不支持的
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "name不能为空白字符");
        }
        return this.updateById(menu);
    }

    @Override
    public boolean deleteMenu(Menu menu) {
        ThrowUtils.throwIf(menu == null, ErrorCode.PARAMS_ERROR);
        Long id = menu.getId();
        ThrowUtils.throwIf(id == null, ErrorCode.PARAMS_ERROR, "id不能为空");
        Menu menuDB = this.getById(id);
        ThrowUtils.throwIf(menuDB == null, ErrorCode.NOT_FOUND_ERROR, "数据不存在");
        // 如果该菜单下绑定了文件，那么也不能删除
        boolean b = sysFileService.containsFile(id);
        ThrowUtils.throwIf(b, ErrorCode.FORBIDDEN_ERROR, "该菜单下存在了文件，不能删除");
        // 如果有子菜单也不能删除
        return this.removeById(id);
    }

    @Override
    public List<MenuTree> getMenuTree() {
        List<Menu> list = this.list();
        return buildMenuTree(list, 0L);
    }


    private List<MenuTree> buildMenuTree(List<Menu> menus, long parentId) {
        // 如果没有子菜单，就返回
        List<Menu> collect = menus.stream().filter(menu -> menu.getParentId().equals(parentId)).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(collect)) {
            return null;
        }
        List<MenuTree> menuTrees = collect.stream().map(menu -> {
            MenuTree menuTree = new MenuTree();
            BeanUtils.copyProperties(menu, menuTree);
            List<MenuTree> children = buildMenuTree(menus, menu.getId());
            menuTree.setChildren(children);
            return menuTree;
        }).collect(Collectors.toList());
        return menuTrees;
    }
}




