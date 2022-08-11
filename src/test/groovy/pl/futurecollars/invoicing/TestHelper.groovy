package pl.futurecollars.invoicing

import pl.futurecollars.invoicing.model.Company
import pl.futurecollars.invoicing.model.Invoice
import pl.futurecollars.invoicing.model.InvoiceEntry
import pl.futurecollars.invoicing.model.Vat

import java.time.LocalDate

class TestHelpers {

    static company(int id) {
        new Company(("$id").repeat(10),
                 "iCode Trust $id Sp. z o.o",
                  "ul. Bukowińska 24d/$id 02-703 Warszawa, Polska",
        );
    }

    static product(int id) {
        new InvoiceEntry("Programming course $id", BigDecimal.valueOf(id * 1000), BigDecimal.valueOf(id * 1000 * 0.08), Vat.VAT_8)
    }

    static invoice(int id) {
        Invoice invoice = Invoice.builder()
                .date(LocalDate.now())
                .fromCompany(company(id))
                .toCompany(company(id))
                .invoiceEntries(List.of(product(id)))
                .build();
    }
}
