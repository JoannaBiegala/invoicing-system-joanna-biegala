package pl.futurecollars.invoice.service;

import java.util.List;
import pl.futurecollars.invoice.db.Database;
import pl.futurecollars.invoice.model.Invoice;

public class InvoiceService {

  private final Database database;

  public InvoiceService(Database database) {
    this.database = database;
  }

  public long saveInvoice(Invoice invoice) {
    return database.save(invoice);
  }

  public Invoice findForId(long id) {
    return database.findById(id);
  }

  public void updateInvoice(long id, Invoice invoice) {
    database.update(id, invoice);
  }

  public void deleteInvoice(long id) {
    database.delete(id);
  }

  public List<Invoice> getAll() {
    return database.getAll();
  }

}
