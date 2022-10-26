package pl.futurecollars.invoice.configuration.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.channel.ChannelProcessingFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import pl.futurecollars.invoice.configuration.security.filter.CorsFilter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfiguration {

  @Autowired
  private CorsFilter corsFilter;

  @Value("${invoicing-system.csrf.disable}")
  private boolean disableCsrf;

  @Bean
  protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.httpBasic()
        .and()
        .addFilterBefore(corsFilter, ChannelProcessingFilter.class);

    if (disableCsrf) {
      http.csrf().disable();
    } else {
      http.csrf(csrf -> csrf.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()));
    }
    return http.build();
  }
}
