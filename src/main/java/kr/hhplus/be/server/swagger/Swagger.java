package kr.hhplus.be.server.swagger;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class Swagger {
	@Bean
    public OpenAPI openAPI() {
	    return new OpenAPI()
    	        .info(info);
	}
    
    Info info = new Info().title("Swagger Test").version("0.0.1").description(
        "<h3>Swagger test</h3>");

}
