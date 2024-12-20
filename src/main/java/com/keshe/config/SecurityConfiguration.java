package com.keshe.config;

import com.keshe.entity.RestBean;
import com.keshe.filter.JwtAuthenticationFilter;
import com.keshe.util.JwtUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.io.IOException;
import java.io.PrintWriter;


@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends AbstractHttpConfigurer<SecurityConfiguration, HttpSecurity> {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf().disable()
                .authorizeHttpRequests(conf -> {
                    // 允许swagger-ui相关路径访问
                    conf.requestMatchers("/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**").permitAll();
                    conf.requestMatchers("/api/user/register").permitAll();
                    conf.requestMatchers("/api/captcha/generate").permitAll();
                    conf.requestMatchers("/api/captcha/validate").permitAll();
                    conf.anyRequest().authenticated();
                })
                .formLogin(conf -> {
                    //一般分离之后，为了统一规范接口，使用 /api/模块/功能 的形式命名接口
                    conf.loginProcessingUrl("/api/auth/login");
                    conf.failureHandler(this::handleProcess);
                    conf.successHandler(this::handleProcess);
                    conf.permitAll();
                })
                .cors(conf -> {
                    CorsConfiguration cors = new CorsConfiguration();
                    cors.addAllowedOriginPattern("*"); // Allow all origins
                    cors.addAllowedMethod("*"); // Allow all methods
                    cors.addAllowedHeader("*"); // Allow all headers
                    cors.setAllowCredentials(true); // Allow credentials
                    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
                    source.registerCorsConfiguration("/**", cors);  // Apply the configuration to all paths
                    conf.configurationSource(source);
                })

                .exceptionHandling(conf -> {
                    conf.accessDeniedHandler(this::handleProcess);
                    conf.authenticationEntryPoint(this::handleProcess);
                })
                .logout(conf -> {
                    conf.logoutUrl("/api/auth/logout");
                    conf.logoutSuccessHandler(this::handleProcess);
                })
                .sessionManagement(conf -> {
                    conf.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
                })
                .addFilterBefore(new JwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .build();
    }


    private void handleProcess(HttpServletRequest request,
                               HttpServletResponse response,
                               Object exceptionOrAuthentication) throws IOException {
        response.setContentType("application/json;charset=utf-8");
        PrintWriter writer = response.getWriter();
        if (exceptionOrAuthentication instanceof AccessDeniedException exception) {
            writer.write(RestBean.failure(403, exception.getMessage()).asJsonString());
        } else if (exceptionOrAuthentication instanceof AuthenticationException exception) {
            writer.write(RestBean.failure(401, exception.getMessage()).asJsonString());
        } else if (exceptionOrAuthentication instanceof Authentication authentication) {
            //不过这里需要注意，在登录成功的时候需要返回我们生成的JWT令牌，这样客户端下次访问就可以携带这个令牌了，令牌过期之后就需要重新登录才可以
            writer.write(RestBean.success(JwtUtils.createJwt((User) authentication.getPrincipal())).asJsonString());
        }
    }

}
