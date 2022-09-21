package pl.futurecollars.invoice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Company {

  private String name;
  private String taxIdentificationNumber;
  private String address;

}
