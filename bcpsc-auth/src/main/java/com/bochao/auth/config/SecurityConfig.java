package com.bochao.auth.config;


import com.bochao.auth.permission.filter.CodeLoginAuthenticationFilter;
import com.bochao.auth.permission.filter.OpenIdLoginAuthenticationFilter;
import com.bochao.auth.permission.filter.PhoneLoginAuthenticationFilter;
import com.bochao.auth.permission.filter.QrLoginAuthenticationFilter;
import com.bochao.auth.permission.handler.WebLoginAuthSuccessHandler;
import com.bochao.auth.permission.handler.WebLoginFailureHandler;
import com.bochao.auth.permission.handler.WebLogoutHandler;
import com.bochao.auth.permission.handler.WebLogoutSuccessHandler;
import com.bochao.auth.permission.provider.OpenIdAuthenticationProvider;
import com.bochao.auth.permission.provider.PhoneAuthenticationProvider;
import com.bochao.auth.permission.provider.QrAuthenticationProvider;
import com.bochao.auth.permission.userDetailsService.OpenIdUserDetailService;
import com.bochao.auth.permission.userDetailsService.PhoneUserDetailService;
import com.bochao.auth.permission.userDetailsService.QrUserDetailService;
import com.bochao.auth.permission.userDetailsService.UsernameUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.web.cors.CorsUtils;


@Configuration
@EnableWebSecurity
@Order(1)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private UsernameUserDetailService userDetailService;
    @Autowired
    private PhoneUserDetailService phoneUserDetailService;

    @Autowired
    private QrUserDetailService qrUserDetailService;

    @Autowired
    private OpenIdUserDetailService openIdUserDetailService;
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
//        return new NoEncryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
       http.cors().
				and().csrf().disable()
				.authorizeRequests().requestMatchers(CorsUtils::isPreFlightRequest).permitAll().and()
				.logout().addLogoutHandler(getLogoutHandler()).logoutSuccessHandler(getLogoutSuccessHandler()).and()
				.addFilterBefore(getPhoneLoginAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
				.addFilterBefore(getQrLoginAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
				.addFilterBefore(getUsernameLoginAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
				.addFilterBefore(getOpenIdLoginAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
				.addFilterBefore(getCodeLoginAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
				.authorizeRequests().antMatchers("/oauth/**").permitAll().and()
				.authorizeRequests().antMatchers("/logout/**").permitAll().and()
				.authorizeRequests().antMatchers("/pub-key/jwt.json").permitAll().and()
			 // 其余所有请求全部需要鉴权认证
			.authorizeRequests().anyRequest().authenticated();
    }

    /**
     * 用户验证
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(phoneAuthenticationProvider());
        auth.authenticationProvider(daoAuthenticationProvider());
        auth.authenticationProvider(openIdAuthenticationProvider());
        auth.authenticationProvider(qrAuthenticationProvider());
    }
    /**
     * 不定义没有password grant_type
     *
     * @return
     * @throws Exception
     */
    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
    @Bean
    public WebLoginAuthSuccessHandler getLoginSuccessAuth(){
        WebLoginAuthSuccessHandler myLoginAuthSuccessHandler = new WebLoginAuthSuccessHandler();
        return myLoginAuthSuccessHandler;
    }

    @Bean
    public WebLoginFailureHandler getLoginFailure(){
        WebLoginFailureHandler myLoginFailureHandler = new WebLoginFailureHandler();
        return myLoginFailureHandler;
    }
    @Bean
    public LogoutHandler getLogoutHandler(){
        WebLogoutHandler myLogoutHandler = new WebLogoutHandler();
        return myLogoutHandler;
    }

    @Bean
    public LogoutSuccessHandler getLogoutSuccessHandler(){
        WebLogoutSuccessHandler logoutSuccessHandler = new WebLogoutSuccessHandler();
        return logoutSuccessHandler;
    }


    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider(){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        // 设置userDetailsService
        provider.setUserDetailsService(userDetailService);
        // 禁止隐藏用户未找到异常
        provider.setHideUserNotFoundExceptions(false);
        // 使用BCrypt进行密码的hash
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }


    @Bean
    public PhoneAuthenticationProvider phoneAuthenticationProvider(){
        PhoneAuthenticationProvider provider = new PhoneAuthenticationProvider();
        // 设置userDetailsService
        provider.setUserDetailsService(phoneUserDetailService);
        // 禁止隐藏用户未找到异常
        provider.setHideUserNotFoundExceptions(false);
        return provider;
    }

    @Bean
    public QrAuthenticationProvider qrAuthenticationProvider(){
        QrAuthenticationProvider provider = new QrAuthenticationProvider();
        // 设置userDetailsService
        provider.setUserDetailsService(qrUserDetailService);
        // 禁止隐藏用户未找到异常
        provider.setHideUserNotFoundExceptions(false);
        return provider;
    }

    @Bean
    public OpenIdAuthenticationProvider openIdAuthenticationProvider(){
        OpenIdAuthenticationProvider provider = new OpenIdAuthenticationProvider();
        // 设置userDetailsService
        provider.setUserDetailsService(openIdUserDetailService);
        // 禁止隐藏用户未找到异常
        provider.setHideUserNotFoundExceptions(false);
        return provider;

    }
    /**
     * 账号密码登录
     * @return
     */
    @Bean
    public UsernamePasswordAuthenticationFilter getUsernameLoginAuthenticationFilter(){
        UsernamePasswordAuthenticationFilter filter = new UsernamePasswordAuthenticationFilter();
        try {
            filter.setAuthenticationManager(this.authenticationManagerBean());
        } catch (Exception e) {
            e.printStackTrace();
        }
        filter.setAuthenticationSuccessHandler(getLoginSuccessAuth());
        filter.setAuthenticationFailureHandler(getLoginFailure());
        return filter;
    }

    /**
     * 手机验证码登陆过滤器
     * @return
     */
    @Bean
    public PhoneLoginAuthenticationFilter getPhoneLoginAuthenticationFilter() {
        PhoneLoginAuthenticationFilter filter = new PhoneLoginAuthenticationFilter();
        try {
            filter.setAuthenticationManager(this.authenticationManagerBean());
        } catch (Exception e) {
            e.printStackTrace();
        }
        filter.setAuthenticationSuccessHandler(getLoginSuccessAuth());
        filter.setAuthenticationFailureHandler(getLoginFailure());
        return filter;
    }

    /**
     * 二维码登录过滤器
     * @return
     */
    @Bean
    public QrLoginAuthenticationFilter getQrLoginAuthenticationFilter() {
        QrLoginAuthenticationFilter filter = new QrLoginAuthenticationFilter();
        try {
            filter.setAuthenticationManager(this.authenticationManagerBean());
        } catch (Exception e) {
            e.printStackTrace();
        }
        filter.setAuthenticationSuccessHandler(getLoginSuccessAuth());
        filter.setAuthenticationFailureHandler(getLoginFailure());
        return filter;
    }
    /**
     * 微信OPENID登录
     * @return
     */
    @Bean
    public OpenIdLoginAuthenticationFilter getOpenIdLoginAuthenticationFilter() {
        OpenIdLoginAuthenticationFilter filter = new OpenIdLoginAuthenticationFilter();
        try {
            filter.setAuthenticationManager(this.authenticationManagerBean());
        } catch (Exception e) {
            e.printStackTrace();
        }
        filter.setAuthenticationSuccessHandler(getLoginSuccessAuth());
        filter.setAuthenticationFailureHandler(getLoginFailure());
        return filter;
    }

    /**
     * code登录
     * @return
     */
    @Bean
    public CodeLoginAuthenticationFilter getCodeLoginAuthenticationFilter() {
        CodeLoginAuthenticationFilter filter = new CodeLoginAuthenticationFilter();
        try {
            filter.setAuthenticationManager(this.authenticationManagerBean());
        } catch (Exception e) {
            e.printStackTrace();
        }
        filter.setAuthenticationSuccessHandler(getLoginSuccessAuth());
        filter.setAuthenticationFailureHandler(getLoginFailure());
        return filter;
    }

}
