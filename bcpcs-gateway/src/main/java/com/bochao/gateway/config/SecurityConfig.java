package com.bochao.gateway.config;




import com.bochao.gateway.filter.CorsFilter;
import com.bochao.gateway.filter.ReactiveRequestContextFilter;
import com.bochao.gateway.manager.WebfluxReactiveAuthorizationManager;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authorization.ReactiveAuthorizationManager;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;



/**
 * 资源服务器配置
 * @author
 */
@EnableWebFluxSecurity
public class SecurityConfig {

	@Bean
	public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
		http.cors().and().csrf().disable()
				.authorizeExchange()
				.anyExchange().access(reactiveAuthorizationManager());
		http.addFilterAt(new CorsFilter(), SecurityWebFiltersOrder.SECURITY_CONTEXT_SERVER_WEB_EXCHANGE);
		http.addFilterAt(new ReactiveRequestContextFilter(), SecurityWebFiltersOrder.SECURITY_CONTEXT_SERVER_WEB_EXCHANGE);
		http.oauth2ResourceServer().jwt();
		return http.build();
	}

	/**
	 * 注入授权管理器
	 * @return
	 */
	@Bean
	public ReactiveAuthorizationManager reactiveAuthorizationManager(){
		WebfluxReactiveAuthorizationManager webfluxReactiveAuthorizationManager = new WebfluxReactiveAuthorizationManager();
		return webfluxReactiveAuthorizationManager;
	}
}