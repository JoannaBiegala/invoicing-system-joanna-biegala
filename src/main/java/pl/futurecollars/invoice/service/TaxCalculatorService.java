package pl.futurecollars.invoice.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.function.Function;
import java.util.function.Predicate;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.futurecollars.invoice.db.Database;
import pl.futurecollars.invoice.model.Invoice;
import pl.futurecollars.invoice.model.InvoiceEntry;

@Service
@AllArgsConstructor
public class TaxCalculatorService {

  private final Database database;

  public TaxCalculatorResult getTaxCalculatorResult(String taxIdentificationNumber) {

    BigDecimal income = calculateIncome(taxIdentificationNumber);
    BigDecimal costs = calculateCosts(taxIdentificationNumber);
    BigDecimal incomingVat = calculateIncomingVat(taxIdentificationNumber);
    BigDecimal outgoingVat = calculateOutgoingVat(taxIdentificationNumber);

    BigDecimal earnings = income.subtract(costs);
    BigDecimal vatToReturn = incomingVat.subtract(outgoingVat);

    return TaxCalculatorResult.builder()
        .income(income)
        .costs(costs)
        .earnings(earnings)
        .incomingVat(incomingVat)
        .outgoingVat(outgoingVat)
        .vatToReturn(vatToReturn)
        .build();
  }

  private BigDecimal calculateIncome(String taxIdentificationNumber) {
    return database.visit(sellerPredicate(taxIdentificationNumber), this::getTotalPrice);
  }

  private BigDecimal getTotalPrice(InvoiceEntry entry) {
    return entry.getNetPrice().multiply(entry.getQuantity());
  }

  private BigDecimal calculateCosts(String taxIdentificationNumber) {
    return database.visit(buyerPredicate(taxIdentificationNumber), calculateTotalCosts());
  }

  private Function<InvoiceEntry, BigDecimal> calculateTotalCosts() {
    return entry -> {
      if (entry.getExpenseRelatedToCar() != null && entry.getExpenseRelatedToCar().isPersonalUse()) {
        return calculateTotalNetPrice(entry);
      }
      return getTotalPrice(entry);
    };
  }

  private BigDecimal calculateTotalNetPrice(InvoiceEntry entry) {
    return (entry.getNetPrice().add(entry.getVatValue().divide(BigDecimal.valueOf(2), 2, RoundingMode.CEILING)))
        .multiply(entry.getQuantity());
  }

  private BigDecimal calculateIncomingVat(String taxIdentificationNumber) {
    return database.visit(sellerPredicate(taxIdentificationNumber), this::getTotalVat);
  }

  private BigDecimal getTotalVat(InvoiceEntry entry) {
    return entry.getVatValue().multiply(entry.getQuantity());
  }

  private BigDecimal calculateOutgoingVat(String taxIdentificationNumber) {
    return database.visit(buyerPredicate(taxIdentificationNumber), vatEntryToAmount());
  }

  private Function<InvoiceEntry, BigDecimal> vatEntryToAmount() {
    return entry -> {
      if (entry.getExpenseRelatedToCar() != null && entry.getExpenseRelatedToCar().isPersonalUse()) {
        return calculateTotalVatValue(entry);
      }
      return getTotalVat(entry);
    };
  }

  private BigDecimal calculateTotalVatValue(InvoiceEntry entry) {
    return (entry.getVatValue().divide(BigDecimal.valueOf(2), 2, RoundingMode.FLOOR))
        .multiply(entry.getQuantity());
  }

  private Predicate<Invoice> sellerPredicate(String taxIdentificationNumber) {
    return invoice -> invoice.getSeller().getTaxIdentificationNumber().equals(taxIdentificationNumber);
  }

  private Predicate<Invoice> buyerPredicate(String taxIdentificationNumber) {
    return invoice -> invoice.getBuyer().getTaxIdentificationNumber().equals(taxIdentificationNumber);
  }

}
