package com.wtmsbackend.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "WTMS Backend API",
                description = "API documentation for the WTMS application including secured endpoints.",
                version = "1.0",
                contact = @Contact(
                        name = "WTMS Support",
                        email = "support@wtms.com"
                )
        ),
        // This globally applies the "bearerAuth" security requirement to all endpoints in Swagger
        security = {
                @SecurityRequirement(name = "bearerAuth")
        }
)
@SecurityScheme(
        name = "bearerAuth",
        description = "JWT authentication. After logging in, copy your accessToken and paste it here. Swagger will automatically add 'Bearer ' for you.",
        scheme = "bearer",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER
)
public class OpenApiConfig {
    // Nothing needs to be inside this class; the annotations do all the work!
}
