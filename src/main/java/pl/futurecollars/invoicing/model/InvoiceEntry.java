package pl.futurecollars.invoicing.model;

import java.math.BigDecimal;

public class InvoiceEntry {
  String description;
  BigDecimal price;
  BigDecimal vatValue;
  Vat vatRate;

  public InvoiceEntry(String description, BigDecimal price, BigDecimal vatValue, Vat vatRate) {
    this.description = description;
    this.price = price;
    this.vatValue = vatValue;
    this.vatRate = vatRate;
  }
}
