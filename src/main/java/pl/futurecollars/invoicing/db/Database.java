package pl.futurecollars.invoicing.db;

import java.util.Map;
import pl.futurecollars.invoicing.model.Invoice;

public interface Database {
  int save(Invoice invoice);

  Invoice getById(int id);

  Map<Integer,Invoice> getAll();

  void update(int id, Invoice updatedInvoice);

  void delete(int id);
}
