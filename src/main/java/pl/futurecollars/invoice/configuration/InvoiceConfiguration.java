package pl.futurecollars.invoice.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.futurecollars.invoice.db.Database;
import pl.futurecollars.invoice.service.InvoiceService;

@Configuration
public class InvoiceConfiguration {

  @Bean
  public InvoiceService invoiceService(Database database) {
    return new InvoiceService(database);
  }

}
