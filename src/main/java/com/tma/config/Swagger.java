package com.tma.config;


import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.security.SecuritySchemes;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;



@Configuration
@OpenAPIDefinition(security = {@SecurityRequirement(name = "bearerToken")})
@SecuritySchemes({
	@SecurityScheme(name = "bearerToken", type = SecuritySchemeType.HTTP, scheme = "bearer", bearerFormat = "JWT")
})
public class Swagger {

	@Bean
	public OpenAPI openAPI() {
		return new OpenAPI()
				.info(new Info()
					.title("TMA Spring Boot demo REST API")
					.description("Sample OpenAPI 3.0")
					.contact(new Contact().email("ngohuunhan10@gmail.com").name("Ngo Huu Nhan").url("https://www.linkedin.com/in/ngohuunhan/"))
					.license(new License()
							.name("Apache 2.0")
							.url("http://www.apache.org/licenses/LICENSE-2.0.html"))
					.version("1.0.0"));

	}


}
