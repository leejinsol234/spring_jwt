package org.koreait.jwtStudy.configs;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableJpaAuditing
public class MvcConfig implements WebMvcConfigurer {
    @Bean
    public MessageSource messageSource(){
        ResourceBundleMessageSource ms = new ResourceBundleMessageSource();
        ms.setDefaultEncoding("UTF-8");
        ms.setBasenames("messages.commons","messages.validations", "messages.errors");

        return ms;
    }
    @Bean
    public CorsFilter corsFilter(){
        //cors 설정
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        //출처
        config.addAllowedOrigin("*"); //실제 서비스에서는 *가 아니라 연동할 도메인으로 한정한다(보안 강화)
        config.addAllowedHeader("*");
        config.addAllowedMethod("*"); // GET,POST 모두 허용

        source.registerCorsConfiguration("/api/**",config);

        return new CorsFilter(source);
    }

}
