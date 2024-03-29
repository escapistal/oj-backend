package com.xc.oj.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Configuration
@EnableSwagger2
public class SwaggerConfig extends WebMvcConfigurationSupport {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // 设置允许跨域的路径
        registry.addMapping("/**")
                // 设置允许跨域请求的域名
                .allowedOrigins("*")
                // 是否允许证书 不再默认开启
                .allowCredentials(true)
                // 设置允许的方法
                .allowedMethods("*")
                // 跨域允许时间
                .maxAge(3600);
    }

    @Bean
    public Docket productApi() {
        return new Docket(DocumentationType.SWAGGER_2).select()
                .apis(RequestHandlerSelectors.basePackage("com.xc.oj.controller"))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(metaData())
                .securitySchemes(securitySchemes())
                .securityContexts(securityContexts());
    }

    private List<ApiKey> securitySchemes() {
        return Collections.singletonList(new ApiKey("Authorization", "Authorization", "header"));
    }
    private List<SecurityContext> securityContexts() {
        return Arrays.asList(
                SecurityContext.builder()
                        .securityReferences(defaultAuth())
//                        .forPaths(PathSelectors.regex("^(?!auth).*$"))
                        .forPaths(PathSelectors.any())
                        .build()
        );
    }
    private List<SecurityReference> defaultAuth() {
        return Arrays.asList(new SecurityReference("Authorization", new AuthorizationScope[]{new AuthorizationScope("global", "accessEverything")}));
    }

    private ApiInfo metaData() {
        return new ApiInfoBuilder()
                .title("接口文档")
                .description("oj-backend")
                .version("1.0.0")
//                .license("Apache License Version 2.0")
//                .licenseUrl("https://www.apache.org/licenses/LICENSE-2.0")
                .build();
    }

    @Value("${file.uploadDir}")
    private String uploadDir;

    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");

        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");

        registry.addResourceHandler("/api/img/**")
                .addResourceLocations("file:"+uploadDir);
    }
}