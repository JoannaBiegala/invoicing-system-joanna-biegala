package pl.futurecollars.invoicing.model;

import java.time.LocalDate;
import java.util.List;
import lombok.Builder;

@Builder
public class Invoice {
  public int id;
  public LocalDate date;
  public Company fromCompany;
  public Company toCompany;
  public List<InvoiceEntry> invoices;
}
