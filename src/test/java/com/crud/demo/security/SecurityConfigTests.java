package com.crud.demo.security;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@ExtendWith(MockitoExtension.class)
@Import(SecurityConfig.class)
class SecurityConfigTest {
@Autowired
    private SecurityConfig config;

    @Mock
    private AuthenticationProvider authProvider;

    @Mock
    private JwtAuthenticationFilter jwtFilter;

    @BeforeEach
    void setUp() {
        config = new SecurityConfig(authProvider, jwtFilter);
    }

    @Test
    @DisplayName("Deve configurar CORS com padrões esperados")
    void deveConfigurarCorsComPadroesEsperados() {
        CorsConfigurationSource source = config.corsConfigurationSource();

        assertTrue(source instanceof UrlBasedCorsConfigurationSource, "Should be UrlBasedCorsConfigurationSource");
        UrlBasedCorsConfigurationSource urlSource = (UrlBasedCorsConfigurationSource) source;

        Map<String, CorsConfiguration> configMap = urlSource.getCorsConfigurations();

        assertTrue(configMap.containsKey("/**"), "Configuration should contain '/**' pattern");
        CorsConfiguration cors = configMap.get("/**");
        assertNotNull(cors, "CORS configuration for '/**' should not be null");

        assertEquals(List.of("http://localhost:8080"), cors.getAllowedOrigins(), "Allowed origins mismatch");
        assertEquals(List.of("*"), cors.getAllowedMethods(), "Allowed methods mismatch");
        assertEquals(List.of("Authorization", "Content-Type"), cors.getAllowedHeaders(), "Allowed headers mismatch");
    }
}