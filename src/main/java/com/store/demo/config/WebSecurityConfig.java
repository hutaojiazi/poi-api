package com.store.demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter
{
	@Override
	protected void configure(HttpSecurity http) throws Exception
	{
		http.authorizeRequests()
				.antMatchers(HttpMethod.POST)
				.permitAll()
				.antMatchers(HttpMethod.PUT)
				.permitAll()
				.antMatchers(HttpMethod.DELETE)
				.permitAll()
				.antMatchers(HttpMethod.GET)
				.permitAll()
				.and()
				.csrf()
				.disable();
	}
}
