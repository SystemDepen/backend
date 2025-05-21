package com.github.sysdepen.depen_api.security.config;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig  {

	@Bean
	@Order(1)
	public SecurityFilterChain publicLoginChain(HttpSecurity http) throws Exception {
		http
				.securityMatcher(new OrRequestMatcher(
						new AntPathRequestMatcher("/api/v1/login/logar"),
						new AntPathRequestMatcher("/api/v1/usuario/save")   // <- pega /save e /save/
				))
				.cors(Customizer.withDefaults())
				.csrf(csrf -> csrf.disable())
				.authorizeHttpRequests(a -> a.anyRequest().permitAll());
		return http.build();
	}

	@Bean
	@Order(2)
	public SecurityFilterChain apiChain(HttpSecurity http) throws Exception {
		http
				.securityMatcher("/api/**")
				.cors(Customizer.withDefaults())
				.csrf(csrf -> csrf.disable())
				.authorizeHttpRequests(a -> a.anyRequest().authenticated())
				.oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()));
		return http.build();
	}

//	@Bean
//	@Order(2)
//	public SecurityFilterChain apiChain(HttpSecurity http) throws Exception {
//		http
//				.securityMatcher("/api/**")
//				.cors(Customizer.withDefaults())
//				.csrf(csrf -> csrf.disable())
//				.authorizeHttpRequests(a -> a
//						// libera todos os pre-flights (OPTIONS)
//						.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
//						// o resto exige JWT
//						.anyRequest().authenticated()
//				)
//				.oauth2ResourceServer(o -> o.jwt(Customizer.withDefaults()));
//		return http.build();
//	}

	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration config = new CorsConfiguration();
		// liste exatamente o seu front (sem "*")
		config.setAllowedOriginPatterns(
				List.of("http://frontend.local.sysdepen.com.br")
		);
		config.setAllowedMethods(
				List.of("GET","POST","PUT","DELETE","OPTIONS")
		);
		config.setAllowedHeaders(List.of("*"));
		config.setAllowCredentials(true);
		config.setMaxAge(3600L);

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", config);
		return source;
	}

}
