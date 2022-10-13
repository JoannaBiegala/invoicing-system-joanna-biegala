package pl.futurecollars.invoice.model;

import java.math.BigDecimal;
import javax.persistence.Table;

@Table(name = "vat")
public enum Vat {

  Vat_23(23),
  Vat_8(8),
  Vat_5(5),
  Vat_0(0),
  Vat_ZW(0);

  private final BigDecimal vatRate;

  Vat(int vatRate) {
    this.vatRate = BigDecimal.valueOf(vatRate);
  }

  public BigDecimal getRate() {
    return this.vatRate;
  }

}
