package pl.futurecollars.invoice

import pl.futurecollars.invoice.model.Company
import pl.futurecollars.invoice.model.Invoice
import pl.futurecollars.invoice.model.InvoiceEntry
import pl.futurecollars.invoice.model.Vat

import java.time.LocalDate

class TestHelpers {

    static company(long id) {
        new Company(("iCode Trust $id Sp. z o.o"),
                "0123456789",
                "ul. Nowa 24d/$id 02-703 Warszawa, Polska")
    }

    static product(long id) {
        new InvoiceEntry("Programming course $id", BigDecimal.ONE, BigDecimal.valueOf(id * 1000), BigDecimal.valueOf(id * 1000 * 0.08), Vat.Vat_8)
    }

    static invoice(long id) {
        new Invoice(LocalDate.now(), company(id), company(id), List.of(product(id)))
    }
}