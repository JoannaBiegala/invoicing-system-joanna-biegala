package pl.futurecollars.invoice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
public class InvoiceApplication {

  public static void main(String[] args) {
    SpringApplication.run(InvoiceApplication.class, args);
  }

}
