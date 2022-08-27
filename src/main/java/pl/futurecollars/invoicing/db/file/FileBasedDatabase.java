package pl.futurecollars.invoicing.db.file;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;
import pl.futurecollars.invoicing.db.Database;
import pl.futurecollars.invoicing.model.Invoice;
import pl.futurecollars.invoicing.utils.FilesService;
import pl.futurecollars.invoicing.utils.IdService;
import pl.futurecollars.invoicing.utils.JsonService;

public class FileBasedDatabase implements Database {

  private final FilesService filesService;
  private final JsonService jsonService;
  private final IdService idService;

  private final Path databasePath;
  private final Path idPath;

  public FileBasedDatabase(FilesService filesService, JsonService jsonService, IdService idService) throws IOException {
    this.filesService = filesService;
    this.jsonService = jsonService;
    this.idService = idService;
    this.databasePath = Path.of(Configuration.DATABASE_FILE);
    this.idPath = Path.of(Configuration.ID_FILE);
    filesService.initDatabase(idPath, databasePath);
    filesService.writeToFile(idPath, String.valueOf(0L));

  }

  @Override
  public long save(Invoice invoice) {
    long currentId = idService.getCurrentIdAndIncrement();
    invoice.setId(currentId);
    try {
      filesService.appendLineToFile(databasePath, jsonService.toJson(invoice));
    } catch (IOException e) {
      throw new RuntimeException("Problem save invoice to database repository", e);
    }
    return currentId;
  }

  @Override
  public Invoice findById(long id) {
    try {
      return filesService.readAllLines(databasePath)
          .stream()
          .filter(line -> containsId(line, id))
          .map(line -> jsonService.toObject(line, Invoice.class))
          .findFirst()
          .orElse(null);
    } catch (IOException ex) {
      throw new RuntimeException("Database failed to get invoice with id: " + id, ex);
    }
  }

  @Override
  public void update(long id, Invoice updatedInvoice) {
    try {
      List<String> allInvoices = filesService.readAllLines(databasePath);
      final String invoiceAsJson = allInvoices.stream().filter(line -> containsId(line, id)).findFirst()
          .orElseThrow(() -> new IllegalArgumentException("Id " + id + " does not exist"));

      allInvoices.remove(invoiceAsJson);
      final Invoice invoice = jsonService.toObject(invoiceAsJson, Invoice.class);

      invoice.setDate(updatedInvoice.getDate());
      invoice.setFromCompany(updatedInvoice.getFromCompany());
      invoice.setToCompany(updatedInvoice.getToCompany());
      invoice.setInvoiceEntries(updatedInvoice.getInvoiceEntries());

      allInvoices.add(jsonService.toJson(invoice));
      filesService.writeLinesToFile(databasePath, allInvoices);
    } catch (IOException ex) {
      throw new RuntimeException("Failed to update invoice with id: " + id);
    }
  }

  @Override
  public void delete(long id) {
    try {
      var invoicesExceptDeleted = filesService.readAllLines(databasePath)
          .stream()
          .filter(line -> !containsId(line, id))
          .collect(Collectors.toList());

      filesService.writeLinesToFile(databasePath, invoicesExceptDeleted);
    } catch (IOException ex) {
      throw new RuntimeException("Failed to delete invoice with id: " + id, ex);
    }
  }

  private boolean containsId(String line, long id) {
    return line.contains("\"id\":" + id + ",");
  }

  @Override
  public List<Invoice> getAll() {
    try {
      return filesService.readAllLines(databasePath).stream()
          .map(line -> jsonService.toObject(line, Invoice.class))
          .collect(Collectors.toList());
    } catch (IOException ex) {
      throw new RuntimeException("Failed to load all invoices", ex);
    }
  }

}
