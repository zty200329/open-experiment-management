package com.swpu.uchain.openexperiment.config;

import com.swpu.uchain.openexperiment.security.JwtAuthenticationEntryPoint;
import com.swpu.uchain.openexperiment.security.JwtAuthenticationTokenFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * @Author: clf
 * @Date: 19-1-17
 * @Description:
 * 配置security
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private JwtAuthenticationEntryPoint unauthorizedHandler;
    @Autowired
    private MyDisableUrlSessionFilter myDisableUrlSessionFilter;


    @Qualifier("jwtUserDetailsServiceImpl")
    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    public void configureAuthentication(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder
                .userDetailsService(this.userDetailsService)
                .passwordEncoder(passwordEncoder());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }



    @Bean
    public JwtAuthenticationTokenFilter authenticationTokenFilterBean() {
        return new JwtAuthenticationTokenFilter();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        //用于客户端第一次访问时去掉URL中的jsessionid
        httpSecurity.addFilterBefore(myDisableUrlSessionFilter,UsernamePasswordAuthenticationFilter.class);

        httpSecurity
                //token的验证方式不需要开启csrf的防护
                .csrf().disable()
                .exceptionHandling().authenticationEntryPoint(unauthorizedHandler)
                .and()
                //设置无状态的连接,即不创建session
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .authorizeRequests()
//                .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
//                当前的url允许进行匿名访问,即不需要身份认证
                .antMatchers(
                        "/",
                        "/*.html",
                        "/favicon.ico",
                        "/**/*.html",
                        "/**/*.css",
                        "/**/*.js",
                        "/project/**",
                        "/announcement/**",
                        "/file/**",
                        "/info/**",
                        "/user/**",
                        "/test/**",
                        "/druid/**",
                        "/static/**",
                        "/apply/**",
                        "/conclusion/**",
                        "/conclusionAnnex/**",
                        "/material/**",
                        "/newsImages/**",
                        "/api/homePage/**",
                        "/login/**",
                        "/logout/**"
                ).permitAll()
                //配置swagger界面的匿名访问
                .antMatchers(" /static/**").permitAll()
                .antMatchers("/swagger-ui.html").permitAll()
                .antMatchers("/swagger-resources/**").permitAll()
                .antMatchers("/images/**").permitAll()
                .antMatchers("/webjars/**").permitAll()
                .antMatchers("/v2/api-docs").permitAll()
                .antMatchers("/configuration/ui").permitAll()
                .antMatchers("/configuration/security").permitAll()
                .antMatchers("/logout/**").permitAll()
                //配置允许匿名访问的路径
                .antMatchers("/api/anon/*").permitAll()
                .antMatchers("/api/uchain/shutdown").permitAll()
                .antMatchers("/druid/**").permitAll()
                .antMatchers("/api/**").permitAll()
                .antMatchers("/document/**").permitAll()
                .antMatchers("/casToHomePage/**").permitAll()
                .anyRequest().authenticated();

        //配置自己的验证过滤器
        httpSecurity
                .addFilterBefore(authenticationTokenFilterBean(), UsernamePasswordAuthenticationFilter.class);

        // disable page caching
        httpSecurity.headers().cacheControl();

        //关闭 X-Frame-Options
        httpSecurity.headers().frameOptions().disable();
    }
}