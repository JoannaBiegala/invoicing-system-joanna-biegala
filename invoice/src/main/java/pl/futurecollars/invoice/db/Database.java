package pl.futurecollars.invoice.db;

import java.util.List;
import pl.futurecollars.invoice.model.Invoice;

public interface Database {

  long save(Invoice invoice);

  Invoice findById(long id);

  void update(long id, Invoice updatedInvoice);

  void delete(long id);

  List<Invoice> getAll();

}
