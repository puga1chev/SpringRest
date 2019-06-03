package ru.puga1chev.springrest.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import ru.puga1chev.springrest.handler.UrlAuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity(debug = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userDetailsService;

    public WebSecurityConfig(@Qualifier("userDetailsServiceImpl") UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth)
            throws Exception {

        auth
                .userDetailsService(userDetailsService)
                .passwordEncoder(NoOpPasswordEncoder.getInstance());
    }


    @Bean
    public AuthenticationSuccessHandler myAuthenticationSuccessHandler(){
        return new UrlAuthenticationSuccessHandler();
    }

//    еще можно фильтр сделать для этих запросов который сам будет токен добавлять в заголовок
//    сделать рест контроллер который даст токен который можно приложить в заголовок
    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
                .authorizeRequests()
                    .antMatchers("/resources/**", "/*", "/login", "/webjars/**").permitAll()
                    .antMatchers("/admin/**", "/api/**").hasRole("ADMIN")
                    .antMatchers("/user").hasAnyRole("USER", "ADMIN")
                    .anyRequest().authenticated()
                .and()
                    .formLogin()
                    .loginPage("/login").usernameParameter("username").passwordParameter("password")
                    .successHandler(myAuthenticationSuccessHandler())
                    .failureUrl("/login?error")
                    .permitAll()
                .and()
                    .logout()
                    .logoutSuccessUrl("/login")
                    .invalidateHttpSession(true)
                    .permitAll()
                .and()// todo ВКЛЮЧИТЬ csrf
                    .csrf().disable()//.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
//                .and()
                    .headers()
                    .httpStrictTransportSecurity()
                    .maxAgeInSeconds(0)
                    .includeSubDomains(true)
        ;
    }
}