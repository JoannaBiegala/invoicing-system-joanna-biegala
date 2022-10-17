package pl.futurecollars.invoice.controller.invoice

import com.mongodb.client.MongoDatabase
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.ApplicationContext
import org.springframework.http.MediaType
import pl.futurecollars.invoice.controller.AbstractControllerTest
import pl.futurecollars.invoice.model.Invoice
import spock.lang.Requires
import spock.lang.Stepwise
import spock.lang.Unroll

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import static pl.futurecollars.invoice.TestHelpers.*

@AutoConfigureMockMvc
@SpringBootTest
@Unroll
@Stepwise
class InvoiceControllerIntegrationTest extends AbstractControllerTest {

    @Autowired
    private ApplicationContext context

    @Requires({ System.getProperty('spring.profiles.active', '').contains('mongo') })
    def "database is dropped to ensure clean state"() {
        expect:
        MongoDatabase mongoDatabase = context.getBean(MongoDatabase)
        mongoDatabase.drop()
    }

    def setup() {
        getAllInvoices().each { invoice -> delete(invoice.id, INVOICES_ENDPOINT) }
    }

    def "should return empty array when invoice database is empty"() {
        expect:
        getAllInvoices() == []
    }

    def "should add invoice and return sequential id"() {
        expect:
        def id = add(firstInvoice, INVOICES_ENDPOINT)
        add(firstInvoice, INVOICES_ENDPOINT) == id + 1
        add(firstInvoice, INVOICES_ENDPOINT) == id + 2
        add(firstInvoice, INVOICES_ENDPOINT) == id + 3
        add(firstInvoice, INVOICES_ENDPOINT) == id + 4
    }

    def "should return all invoices"() {
        given:
        def numberOfInvoices = 3
        add(firstInvoice, INVOICES_ENDPOINT)
        add(secondInvoice, INVOICES_ENDPOINT)
        add(thirdInvoice, INVOICES_ENDPOINT)

        when:
        def invoices = getAllInvoices()

        then:
        invoices.size() == numberOfInvoices
    }

    def "should return correct invoice when getting by id"() {
        given:
        def expectedInvoices = addUniqueInvoices(5)
        def verifiedInvoice = expectedInvoices.get(2)

        when:
        def invoice = getById(verifiedInvoice.getId(), INVOICES_ENDPOINT, Invoice.class)

        then:
        invoice == verifiedInvoice
    }

    def "should get status 404 when invoice id is not found when getting invoice by id"() {
        given:
        addUniqueInvoices(11)

        expect:
        mockMvc.perform(
                get("$INVOICES_ENDPOINT/$id")
        )
                .andExpect(status().isNotFound())

        where:
        id << [-100, -2, -1, 0, 168, 1256]
    }

    def "should get status 404 when invoice id is not found when deleting invoice by id"() {
        given:
        addUniqueInvoices(11)

        expect:
        mockMvc.perform(
                delete("$INVOICES_ENDPOINT/$id")
        )
                .andExpect(status().isNotFound())


        where:
        id << [-100, -2, -1, 0, 12, 13, 99, 102, 1000]
    }

    def "should get status 404 when invoice id is not found when updating invoice by id"() {
        given:
        addUniqueInvoices(11)

        expect:
        mockMvc.perform(
                put("$INVOICES_ENDPOINT/$id")
                        .content(getAsJson(invoice(1)))
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isNotFound())


        where:
        id << [-100, -2, -1, 0, 12, 13, 99, 102, 1000]
    }

    def "should update invoice by id"() {
        given:
        def id = add(firstInvoice, INVOICES_ENDPOINT)
        def updatedInvoice = secondInvoice
        when:
        mockMvc.perform(
                put("$INVOICES_ENDPOINT/$id")
                        .content(getAsJson(updatedInvoice))
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())

        then:
        secondInvoice.setId(id)
        getById(id, INVOICES_ENDPOINT,Invoice.class) == secondInvoice

    }

    def "should delete invoice by id"() {
        given:
        def invoices = addUniqueInvoices(69)

        expect:
        invoices.each { invoice -> delete(invoice.getId(), INVOICES_ENDPOINT) }
        getAllInvoices().size() == 0
    }

}