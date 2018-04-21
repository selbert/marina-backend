package ch.puzzle.marinabackend;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.web.context.request.RequestContextListener;

@SpringBootApplication
@EnableAsync
@EnableJpaAuditing
@EnableOAuth2Sso
public class MarinaBackendApplication extends WebSecurityConfigurerAdapter {

    public static void main(String[] args) {
        SpringApplication.run(MarinaBackendApplication.class, args);
    }

    @Bean
    @Order(0)
    public RequestContextListener requestContextListener() {
        return new RequestContextListener();
    }

    @Value("${security.enable-csrf}")
    private boolean csrfEnabled;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		if (!csrfEnabled) {
			http.csrf().disable();
		}
		http
	      .antMatcher("/**")
	      .authorizeRequests()
	      .antMatchers("/swagger-ui.html", "/swagger-resources/**", "/v2/**").permitAll()
	        .antMatchers("/", "/login**", "/webjars/**")
	        .permitAll()
	      .anyRequest()
	        .authenticated();

	}
}
