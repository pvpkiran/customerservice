package com.example.customerservice.config;

import com.google.common.collect.Lists;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import static springfox.documentation.builders.PathSelectors.ant;

@Configuration
@ConditionalOnWebApplication
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket customerServiceApiv1() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("customer-service-v1")
                .select()
                .paths(ant("/v1/**"))
                .paths(PathSelectors.regex("(?!/error).+"))
                .paths(PathSelectors.regex("(?!/internal).+"))
                .paths(PathSelectors.regex("(?!/actuator).+"))
                .build()
                .apiInfo(apiInfo("v1"));
    }

    private ApiInfo apiInfo(String version) {
        final Contact contact = new Contact("Panikiran Periyapatna",
                "", "pvpkiran@gmail.com");
        return new ApiInfo("CustomerService-Api",
                "Microservice for Customer.", version, null,
                contact, null, null, Lists.newArrayList());
    }
}