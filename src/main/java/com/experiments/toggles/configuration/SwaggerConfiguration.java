package com.experiments.toggles.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;

/**
 * Swagger is a great tool for generating technical documentation about a project's endpoints.
 * By enabling it, it will scan the project based on the defined rules and generate documentation that is always in
 * sync with the code.
 *
 * REST clients can easily read and try it out before developing.
 */
@Configuration
@EnableSwagger2
public class SwaggerConfiguration {

    /**
     *
     * @return a {@link Docket} indicating what REST controllers should be considered for automatic endpoint
     * documentation
     */
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.experiments"))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiInfo());
    }

    private ApiInfo apiInfo() {
        return new ApiInfo("Toggles microservice", "Manages all systems' toggles",
                           "V1",
                           null,
                           new Contact("Gonçalo Almeida",
                                       "https://github.com/goncalomalmeida/featuretoggles",
                                       "almeida.goncalom@gmail.com"),
                           null,
                           null,
                           Collections.emptyList());
    }
}
