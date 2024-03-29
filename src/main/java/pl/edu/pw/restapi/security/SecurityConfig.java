package pl.edu.pw.restapi.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.passay.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import pl.edu.pw.restapi.security.authentication.*;
import pl.edu.pw.restapi.service.UserService;

import javax.sql.DataSource;
import java.util.List;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final ObjectMapper objectMapper;
    private final RestAuthenticationSuccessHandler successHandler;
    private final RestAuthenticationFailureHandler failureHandler;
    private final AuthenticationEntryPoint authenticationEntryPoint;
    private final DataSource dataSource;
    private final UserService userService;
    private final JwtService jwtService;
    private final JwtLogoutHandler jwtLogoutHandler;

    public SecurityConfig(ObjectMapper objectMapper, RestAuthenticationSuccessHandler successHandler,
                          RestAuthenticationFailureHandler failureHandler, DataSource dataSource,
                          UserService userService, JwtService jwtService,
                          AuthenticationEntryPoint authenticationEntryPoint,
                          JwtLogoutHandler jwtLogoutHandler) {
        this.objectMapper = objectMapper;
        this.successHandler = successHandler;
        this.failureHandler = failureHandler;
        this.dataSource = dataSource;
        this.userService = userService;
        this.authenticationEntryPoint = authenticationEntryPoint;
        this.jwtService = jwtService;
        this.jwtLogoutHandler = jwtLogoutHandler;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService).passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors()
                .and()
                .csrf().disable()
                .authorizeRequests()
                    .antMatchers("/v2/api-docs").permitAll()
                    .antMatchers("/webjars/**").permitAll()
                    .antMatchers("/swagger-resources/**").permitAll()
                    .antMatchers("/swagger-ui/**").permitAll()
                    .antMatchers("/h2-console/**").permitAll()
                    .antMatchers(HttpMethod.POST, "/login").permitAll()
                    .antMatchers(HttpMethod.POST, "/register").permitAll()
                    .antMatchers(HttpMethod.POST, "/user/password/reset").permitAll()
                    .antMatchers(HttpMethod.GET, "/courses").permitAll()
                    .antMatchers(HttpMethod.GET, "/courses/*/details").permitAll()
                    .antMatchers(HttpMethod.POST, "/courses/payment-notification").permitAll()
                    .antMatchers("/categories").permitAll()
                    .anyRequest().authenticated()
                .and()
                    .logout()
                    .logoutUrl("/user/logout")
                    .addLogoutHandler(jwtLogoutHandler)
                    .logoutSuccessHandler(jwtLogoutHandler)
                .and()
                    .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                    .addFilter(authenticationFilter())
                    .addFilter(new JwtAuthenticationFilter(authenticationManager(), jwtService, userService))
                    .exceptionHandling()
                    .authenticationEntryPoint(authenticationEntryPoint)
                .and()
                    .headers().frameOptions().disable();
    }

    public JsonObjectAuthenticationFilter authenticationFilter() throws Exception {
        JsonObjectAuthenticationFilter authenticationFilter = new JsonObjectAuthenticationFilter(objectMapper);
        authenticationFilter.setAuthenticationSuccessHandler(successHandler);
        authenticationFilter.setAuthenticationFailureHandler(failureHandler);
        authenticationFilter.setAuthenticationManager(super.authenticationManager());
        return authenticationFilter;
    }

    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(13);
    }

    @Bean
    public static List<Rule> passwordRules() {
        return List.of(
                new LengthRule(6, 20),
                new UppercaseCharacterRule(1),
                new DigitCharacterRule(1),
                new LowercaseCharacterRule(1)
        );
    }
}
