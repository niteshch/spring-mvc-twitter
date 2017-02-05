package com.twitter.spring.apidocs;

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

    @Bean
    public Docket api(){
        return new Docket(DocumentationType.SWAGGER_2)
            .select()
            .apis(RequestHandlerSelectors.any())
            .paths(PathSelectors.regex("/user/.*"))
            .build()
            .apiInfo(apiInfo());
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
            .title("Twitter User REST API")
            .description("API to access twitter user data.\n"
            		+ "Following operations are supported\n"
            		+ "1. An endpoint to read the tweets for a given user (includes tweets of the user and the "
            		+ "people being followed by the user). An extra “search=” argument can be used to further "
            		+ "filter tweets based on keyword.\n"
            		+ "2. Endpoints to get the list of people a user is following as well as the followers of the user.\n"
            		+ "3. An endpoint to start following another user.\n"
            		+ "4. An endpoint to unfollow another user.")
            .version("0.1")
            .build();
    }

}