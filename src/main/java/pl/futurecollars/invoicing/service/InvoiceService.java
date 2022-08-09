package pl.futurecollars.invoicing.service;

import java.util.Map;
import pl.futurecollars.invoicing.db.Database;
import pl.futurecollars.invoicing.model.Invoice;

public class InvoiceService {
  private final Database database;

  public InvoiceService(Database database) {
    this.database = database;
  }

  public int saveInvoice(Invoice invoice) {
    return database.save(invoice);
  }

  public Invoice findForId(int id) {
    return database.getById(id);
  }

  public void deleteInvoice(int id) {
    database.delete(id);
  }

  public Map<Integer, Invoice> getAll() {
    return database.getAll();
  }
}
