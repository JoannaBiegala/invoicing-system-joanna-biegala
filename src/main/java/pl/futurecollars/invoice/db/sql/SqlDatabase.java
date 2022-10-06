package pl.futurecollars.invoice.db.sql;

import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import pl.futurecollars.invoice.db.Database;
import pl.futurecollars.invoice.model.Invoice;

@AllArgsConstructor
public class SqlDatabase implements Database {

  private JdbcTemplate jdbcTemplate;

  @Override
  public long save(Invoice invoice) {
    jdbcTemplate.update("insert into companies (name,address,pension_insurance,health_insurance) values ();");
    return 0;
  }

  @Override
  public Optional<Invoice> findById(long id) {
    return Optional.empty();
  }

  @Override
  public void update(long id, Invoice updatedInvoice) {

  }

  @Override
  public void delete(long id) {

  }

  @Override
  public List<Invoice> getAll() {
    return null;
  }

}
