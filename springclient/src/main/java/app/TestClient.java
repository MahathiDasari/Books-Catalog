package app;

import org.apache.catalina.filters.CorsFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

@SpringBootApplication
public class TestClient {
    public static void main(String[] args) {
        SpringApplication.run(TestClient.class, args);
    }
}

@Configuration
class MyCorsConfiguration {
    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOrigin("http://localhost:8888");  // Allow your client application's origin
        config.addAllowedMethod("GET");
        config.addAllowedHeader("Authorization");
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter();
    }
}
