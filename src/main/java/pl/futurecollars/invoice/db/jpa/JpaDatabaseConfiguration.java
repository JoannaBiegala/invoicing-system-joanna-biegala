package pl.futurecollars.invoice.db.jpa;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.futurecollars.invoice.db.Database;
import pl.futurecollars.invoice.model.Invoice;

@Slf4j
@Configuration
@ConditionalOnProperty(value = "database.type", havingValue = "jpa")
public class JpaDatabaseConfiguration {

  @Bean
  public Database<Invoice> jpaDatabase(InvoiceRepository invoiceRepository) {
    log.info("Running on jpa database");
    return new JpaDatabase<>(invoiceRepository);
  }

}
