package pl.futurecollars.invoice.service;

import org.springframework.stereotype.Service;

@Service
public class TaxCalculatorService {

  public TaxCalculatorResult getTaxCalculatorResult(String taxIdentificationNumber) {
    return new TaxCalculatorResult();
  }

}
