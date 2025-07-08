package com.zzy.biaohui.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.Arrays;
import java.util.List;

import static java.util.Collections.singletonList;

/**
 * Knife4j 接口文档配置
 * https://doc.xiaominfo.com/knife4j/documentation/get_start.html
 *
 * @author zzy
 */
@Configuration
public class SwaggerConfig {

    @Value("${server.servlet.context-path}")
    private String contextPath;

    @Bean
    public Docket defaultApi2() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(new ApiInfoBuilder()
                        .title("biaohui")
                        .description("biaohui")
                        .version("1.0")
                        .build())
                .select()
                // 指定 Controller 扫描包路径
                .apis(RequestHandlerSelectors.basePackage("com.zzy.biaohui.controller"))
                .paths(PathSelectors.any())
                .build()
                .securitySchemes(Arrays.asList(new ApiKey("Authorization", "Authorization", "header")))
                .securityContexts(Arrays.asList(securityContext()));
    }

    /**
     * 表示哪些路径是需要认证的
     *
     * @return
     */
    private SecurityContext securityContext() {
        return SecurityContext.builder()
                .securityReferences(defaultAuth()).forPaths(path -> {
                    List<String> whiteList = Arrays.asList(
                            contextPath + "/user/login",
                            contextPath + "/user/register");
                    // 如果路径在白名单中，返回false（不需要认证）
                    return !whiteList.contains(path);
                }).build();
    }




    /**
     * 认证规则
     *
     * @return
     */
    private List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope
                = new AuthorizationScope("global", "auth everything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return singletonList(
                new SecurityReference("Authorization", authorizationScopes));
    }
}