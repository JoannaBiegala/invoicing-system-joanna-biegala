package pl.futurecollars.invoice.db;

import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.futurecollars.invoice.utils.JsonService;

@Configuration
public class BeanTestConfiguration {

  @Bean
  @ConditionalOnExpression(
      "'${database.type}' == 'in-memory' || "
          + "'${database.type}' == 'sql' || "
          + "'${database.type}' == 'jpa'"
  )
  public JsonService jsonService() {
    return new JsonService();
  }

}
