package pl.futurecollars.invoice.db.sql;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import pl.futurecollars.invoice.db.Database;
import pl.futurecollars.invoice.model.Invoice;

@Slf4j
@Configuration
@ConditionalOnProperty(value = "database.type", havingValue = "sql")
public class SqlDatabaseConfiguration {

  @Bean
  public Database<Invoice> sqlDatabase(JdbcTemplate jdbcTemplate) {
    log.info("Running on sql database");
    return new SqlDatabase(jdbcTemplate);
  }

}




