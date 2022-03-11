package com.jeeagile.frame.autoconfigure;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;


/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description
 */
@Configuration
@EnableOpenApi
@AutoConfigureAfter(AgileWebAutoConfigure.class)
public class AgileSwaggerAutoConfig {
    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.OAS_30)
                .groupName("JeeAgile")
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.jeeagile"))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("JeeAgile")
                .description("JeeAgile API")
                .contact(new Contact("JeeAgile", "www.JeeAgile.com", "JeeAgile@qq.com"))
                .version("2.0.0")
                .build();
    }
}
