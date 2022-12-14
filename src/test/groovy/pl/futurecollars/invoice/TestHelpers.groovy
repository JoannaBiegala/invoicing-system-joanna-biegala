package pl.futurecollars.invoice

import pl.futurecollars.invoice.model.*

import java.time.LocalDate
import java.util.function.Supplier

class TestHelpers {

    static final String INVOICES_ENDPOINT = "/invoices"
    static final String COMPANIES_ENDPOINT = "/companies"
    static final String TAX_ENDPOINT = "/tax"

    static company(long id) {
        Company.builder()
                .name(("iCode Trust $id Sp. z o.o"))
                .taxIdentificationNumber(("$id").repeat(10))
                .address("ul. Nowa 24d/$id 02-703 Warszawa, Polska")
                .healthInsurance(BigDecimal.valueOf(500).setScale(2))
                .pensionInsurance(BigDecimal.valueOf(1400).setScale(2))
                .build()
    }

    static product(long id) {
        InvoiceEntry.builder()
                .description("Programming course $id")
                .quantity(BigDecimal.valueOf(1).setScale(2))
                .netPrice(BigDecimal.valueOf(id * 1000).setScale(2))
                .vatValue(BigDecimal.valueOf(id * 1000 * 0.08).setScale(2))
                .vatRate(Vat.Vat_8)
                .build()
    }

    static invoice(long id) {
        Invoice.builder()
                .date(LocalDate.now())
                .number("2022/10/12/0000$id")
                .seller(company(id))
                .buyer(company(id + 1))
                .invoiceEntries(List.of(product(id)))
                .build()
    }

    static Supplier<Company> firstCompany = () -> Company.builder()
            .name("First")
            .taxIdentificationNumber("1111111111")
            .address("ul. Pierwsza 1, Warszawa, Polska")
            .healthInsurance(BigDecimal.valueOf(500).setScale(2))
            .pensionInsurance(BigDecimal.valueOf(1400).setScale(2))
            .build()

    static Supplier<Company> secondCompany = () -> Company.builder()
            .name("Second")
            .taxIdentificationNumber("2222222222")
            .address("ul. Druga 2, Warszawa, Polska")
            .healthInsurance(BigDecimal.valueOf(500).setScale(2))
            .pensionInsurance(BigDecimal.valueOf(1400).setScale(2))
            .build()

    static Car firstCar = Car.builder()
            .registrationNumber("AAA 12 34")
            .personalUse(true)
            .build()

    static Car secondCar = Car.builder()
            .registrationNumber("BBB 12 34")
            .personalUse(false)
            .build()

    static InvoiceEntry firstEntry = InvoiceEntry.builder()
            .description("First product")
            .quantity(BigDecimal.valueOf(1).setScale(2))
            .netPrice(BigDecimal.valueOf(10000).setScale(2))
            .vatValue(BigDecimal.valueOf(10000 * 0.23).setScale(2))
            .vatRate(Vat.Vat_23)
            .build()

    static InvoiceEntry secondEntry = InvoiceEntry.builder()
            .description("Second product")
            .quantity(BigDecimal.valueOf(10).setScale(2))
            .netPrice(BigDecimal.valueOf(5000).setScale(2))
            .vatValue(BigDecimal.valueOf(5000 * 0.05).setScale(2))
            .vatRate(Vat.Vat_5)
            .build()

    static InvoiceEntry thirdEntry = InvoiceEntry.builder()
            .description("Third product")
            .quantity(BigDecimal.valueOf(1).setScale(2))
            .netPrice(BigDecimal.valueOf(10000).setScale(2))
            .vatValue(BigDecimal.valueOf(10000 * 0.23).setScale(2))
            .vatRate(Vat.Vat_23)
            .expenseRelatedToCar(firstCar)
            .build()

    static InvoiceEntry fourthEntry = InvoiceEntry.builder()
            .description("Fourth product")
            .quantity(BigDecimal.valueOf(1).setScale(2))
            .netPrice(BigDecimal.valueOf(10000).setScale(2))
            .vatValue(BigDecimal.valueOf(10000 * 0.23).setScale(2))
            .vatRate(Vat.Vat_23)
            .expenseRelatedToCar(secondCar)
            .build()

    static Invoice firstInvoice = Invoice.builder()
            .date(LocalDate.of(2022, 10, 1))
            .number("2022/10/12/00001")
            .seller(firstCompany.get())
            .buyer(secondCompany.get())
            .invoiceEntries(List.of(firstEntry))
            .build()

    static Invoice secondInvoice = Invoice.builder()
            .date(LocalDate.of(2022, 10, 1))
            .number("2022/10/12/00002")
            .seller(secondCompany.get())
            .buyer(firstCompany.get())
            .invoiceEntries(List.of(secondEntry))
            .build()

    static Invoice thirdInvoice = Invoice.builder()
            .date(LocalDate.of(2022, 10, 1))
            .number("2022/10/12/00003")
            .seller(firstCompany.get())
            .buyer(secondCompany.get())
            .invoiceEntries(List.of(firstEntry, secondEntry))
            .build()

    static Invoice fourthInvoice = Invoice.builder()
            .date(LocalDate.of(2022, 10, 1))
            .number("2022/10/12/00004")
            .seller(secondCompany.get())
            .buyer(firstCompany.get())
            .invoiceEntries(List.of(thirdEntry))
            .build()

    static Invoice fifthInvoice = Invoice.builder()
            .date(LocalDate.of(2022, 10, 1))
            .number("2022/10/12/00005")
            .seller(secondCompany.get())
            .buyer(firstCompany.get())
            .invoiceEntries(List.of(fourthEntry))
            .build()

    static Invoice resetIds(Invoice invoice) {
        invoice.getBuyer().id = null
        invoice.getSeller().id = null
        invoice.invoiceEntries.forEach {
            it.id = null
            it.expenseRelatedToCar?.id = null
        }
        invoice
    }

    static List<Invoice> resetIds(List<Invoice> invoices) {
        invoices.forEach { invoice -> resetIds(invoice) }
    }

}
