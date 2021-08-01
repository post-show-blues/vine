package com.post_show_blues.vine.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    private ApiInfo apiInfo() {

        return new ApiInfoBuilder()
                .title("Demo")
                .description("API EXAMPLE")
                .build();
    }

    @Bean
    public Docket commonApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("swg-group1")//빈설정을 여러개 해줄경우 구분하기 위한 구분자.
                .select()//apis, paths를 사용하주기 위한 builder
                .apis(RequestHandlerSelectors.any())//탐색할 클래스 필터링
                .paths(PathSelectors.any())//스웨거에서 보여줄 api 필터링
                .build();
    }
}
