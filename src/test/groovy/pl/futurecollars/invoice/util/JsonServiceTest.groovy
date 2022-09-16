package pl.futurecollars.invoice.util

import pl.futurecollars.invoice.TestHelpers
import pl.futurecollars.invoice.db.file.FileDatabaseConfiguration
import pl.futurecollars.invoice.model.Invoice
import spock.lang.Specification

class JsonServiceTest extends Specification {

    def jsonService = new FileDatabaseConfiguration().jsonService()
    def invoice = TestHelpers.invoice(1)

    def "should can convert object to json and read it back"() {
        when:
        def invoiceAsString = jsonService.toJson(invoice)

        and:
        def invoiceFromJson = jsonService.toObject(invoiceAsString, Invoice)

        then:
        invoice.getDate() == invoiceFromJson.getDate()
        invoice.getFromCompany().toString() == invoiceFromJson.getFromCompany().toString()
        invoice.getToCompany().toString() == invoiceFromJson.getToCompany().toString()
        invoice.getInvoiceEntries().toString() == invoiceFromJson.getInvoiceEntries().toString()
        invoice.getId() == invoiceFromJson.getId()
    }

    def "should return exception when parsing wrong Json string to object"() {
        given:
        def invoiceAsString = jsonService.toJson(invoice)
        invoiceAsString = invoiceAsString.replace(',', ' ')

        when:
        jsonService.toObject(invoiceAsString, Invoice)

        then:
        def exception = thrown(RuntimeException)
        exception.message == "Failed to parse JSON"
    }

}


