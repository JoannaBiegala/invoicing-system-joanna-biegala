package pl.futurecollars.invoicing.db.memory;

import java.util.HashMap;
import java.util.Map;
import pl.futurecollars.invoicing.db.Database;
import pl.futurecollars.invoicing.model.Invoice;

public class InMemoryDatabase implements Database {
  Map<Integer, Invoice> invoices = new HashMap<Integer, Invoice>();
  int index = 1;

  @Override
  public int save(Invoice invoice) {
    index++;
    invoice.id = index;
    invoices.put(index, invoice);
    return index;
  }

  @Override
  public Invoice getById(int id) {
    return invoices.get(id);
  }

  @Override
  public Map<Integer, Invoice> getAll() {
    return invoices;
  }

  @Override
  public void update(int id, Invoice updatedInvoice) {
    invoices.put(id, updatedInvoice);
  }

  @Override
  public void delete(int id) {
    invoices.remove(id);
  }
}
