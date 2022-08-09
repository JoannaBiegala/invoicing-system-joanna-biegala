package pl.futurecollars.invoicing.model;

import lombok.Builder;

@Builder
public class Company {
  public String name;
  public int taxIdentificationNumber;
  public String address;
}
