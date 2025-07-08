package com.zzy.biaohui.service;

import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

/**
 * 用户服务测试
 *
 * @author zzy
 */
@SpringBootTest
class UserServiceTest {

    @Resource
    private UserService userService;

    // 待测试

//    @Test
    public void test() {
        long l = userService.userRegister("admin", "12345678", "12345678");
        System.out.println(l);
    }
}