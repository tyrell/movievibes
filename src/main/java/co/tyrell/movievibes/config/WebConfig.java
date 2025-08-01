package co.tyrell.movievibes.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.ResourceResolver;
import org.springframework.web.servlet.resource.ResourceResolverChain;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(@NonNull CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins("http://localhost:3000") // React dev server
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true);
    }

    @Override
    public void addResourceHandlers(@NonNull ResourceHandlerRegistry registry) {
        // Serve static resources from classpath:/static/
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/")
                .resourceChain(true)
                .addResolver(new SpaResourceResolver());
    }

    /**
     * Custom resource resolver to handle SPA routing.
     * If a resource is not found, serve index.html instead.
     */
    public static class SpaResourceResolver implements ResourceResolver {

        @Override
        @Nullable
        public Resource resolveResource(@Nullable HttpServletRequest request, @NonNull String requestPath,
                                      @NonNull List<? extends Resource> locations, @NonNull ResourceResolverChain chain) {
            Resource resource = chain.resolveResource(request, requestPath, locations);
            
            // If resource found, return it
            if (resource != null) {
                return resource;
            }
            
            // Skip API requests
            if (requestPath.startsWith("api/") || requestPath.startsWith("actuator/")) {
                return null;
            }
            
            // For SPA routing, serve index.html for non-API requests
            return new ClassPathResource("/static/index.html");
        }

        @Override
        @Nullable
        public String resolveUrlPath(@NonNull String resourcePath, @NonNull List<? extends Resource> locations,
                                   @NonNull ResourceResolverChain chain) {
            return chain.resolveUrlPath(resourcePath, locations);
        }
    }
}
