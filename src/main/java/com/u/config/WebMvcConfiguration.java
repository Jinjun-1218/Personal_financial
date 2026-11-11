package com.u.config;

//import com.sky.interceptor.JwtTokenAdminInterceptor;
//import com.sky.interceptor.JwtTokenUserInterceptor;
//import com.sky.json.JacksonObjectMapper;
import com.u.interceptor.LoginInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import com.u.interceptor.RateLimitInterceptor;
/**
 * 配置类，注册web层相关组件
 */
@Configuration
@Slf4j
public class WebMvcConfiguration extends WebMvcConfigurationSupport {

    @Autowired
    private LoginInterceptor loginInterceptor;

    /**
     * 注册拦截器
     * @param registry
     */
//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        // 配置拦截路径和排除路径
//        registry.addInterceptor(loginInterceptor)
//                .addPathPatterns("/**") // 拦截所有请求
//                .excludePathPatterns("/user/login",
//                        "/user/register",
//                        "/doc.html",
//                        "/webjars/**",
//                        "/v2/api-docs",
//                        "/swagger-resources/configuration/ui",
//                        "/swagger-resources/configuration/security",
//                        "/swagger-resources",
//                        "/swagger-ui.html",
//                        "/swagger-ui/**",
//                         "/user/resetPassword",
//                        "/user/loginWithPhoneNumber"); // 排除不需要拦截的路径
//    }

    @Autowired
    private RateLimitInterceptor rateLimitInterceptor;

    // 修改addInterceptors方法
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(rateLimitInterceptor)
                .addPathPatterns("/user/login_v2", "/user/register"); // 需要限流的接口

        registry.addInterceptor(loginInterceptor)
                .addPathPatterns("/**")
                        .excludePathPatterns("/user/login",
                                "/user/login_v1",
                                "/user/login_v2",
                        "/user/register",
                        "/doc.html",
                        "/webjars/**",
                        "/v2/api-docs",
                        "/swagger-resources/configuration/ui",
                        "/swagger-resources/configuration/security",
                        "/swagger-resources",
                        "/swagger-ui.html",
                        "/swagger-ui/**",
                         "/user/resetPassword",
                        "/user/loginWithPhoneNumber");
    }
    /**
     * 通过knife4j生成接口文档
     * @return
     */
    @Bean
    public Docket docket1() {
        log.info("准备生成接口文档1");
        ApiInfo apiInfo = new ApiInfoBuilder()
                .title("个人财务管理")
                .version("2.0")
                .description("个人财务管理")
                .build();
        Docket docket = new Docket(DocumentationType.SWAGGER_2)
                .groupName("管理端接口")
                .apiInfo(apiInfo)
                .select()
                // 指定要扫描的包
                .apis(RequestHandlerSelectors.basePackage("com.u.controller"))
                .paths(PathSelectors.any())
                .build();
        return docket;
    }

    @Bean
    public Docket docket2() {
        log.info("准备生成接口文档2");
        ApiInfo apiInfo = new ApiInfoBuilder()
                .title("个人财务管理")
                .version("2.0")
                .description("个人财务管理")
                .build();
        Docket docket = new Docket(DocumentationType.SWAGGER_2)
                .groupName("用户端接口")
                .apiInfo(apiInfo)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.u.controller"))
                .paths(PathSelectors.any())
                .build();
        return docket;
    }

    /**
     * 设置静态资源映射
     * @param registry
     */
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        log.info("开始设置静态资源映射");
        registry.addResourceHandler("/doc.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
    }
}

