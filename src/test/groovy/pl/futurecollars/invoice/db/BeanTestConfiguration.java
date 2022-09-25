package pl.futurecollars.invoice.db;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.futurecollars.invoice.utils.JsonService;

@Configuration
public class BeanTestConfiguration {

  @Bean
  @ConditionalOnProperty(value = "database.type", havingValue = "in-memory")
  public JsonService jsonService() {
    return new JsonService();
  }

}
