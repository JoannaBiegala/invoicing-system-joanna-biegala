package pl.futurecollars.invoice.controller.tax

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import pl.futurecollars.invoice.TestHelpers
import pl.futurecollars.invoice.model.Invoice
import pl.futurecollars.invoice.service.TaxCalculatorResult
import pl.futurecollars.invoice.utils.JsonService
import spock.lang.Specification
import java.nio.file.Files
import java.nio.file.Path

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@AutoConfigureMockMvc
@SpringBootTest
class TaxCalculatorControllerRestApiTest extends Specification {

    @Autowired
    private MockMvc mockMvc

    @Autowired
    private JsonService jsonService

    def setup() {
        getAllInvoices().each { invoice -> deleteInvoice(invoice.id) }
    }

    def "should return response with zeros when there are no invoices in database"() {
        when:
        def taxCalculatorResponse = calculateTax("0")

        then:
        taxCalculatorResponse.income == 0
        taxCalculatorResponse.costs == 0
        taxCalculatorResponse.earnings == 0
        taxCalculatorResponse.incomingVat == 0
        taxCalculatorResponse.outgoingVat == 0
        taxCalculatorResponse.vatToReturn == 0
    }

    def "should return response with correct values when there is invoice in database"() {
        given:
        mockMvc.perform(
                MockMvcRequestBuilders.post("/invoices").content(jsonService.toJson(TestHelpers.invoice(1))).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())

        when:
        def taxCalculatorResponse = calculateTax("0123456789")

        then:
        taxCalculatorResponse.income == 1000
        taxCalculatorResponse.costs == 1000
        taxCalculatorResponse.earnings == 0
        taxCalculatorResponse.incomingVat == 80
        taxCalculatorResponse.outgoingVat == 80
        taxCalculatorResponse.vatToReturn == 0
    }

    TaxCalculatorResult calculateTax(String taxIdentificationNumber) {
        def response = mockMvc.perform(
                get("/tax/$taxIdentificationNumber"))
                .andExpect(status().isOk())
                .andReturn()
                .response
                .contentAsString

        jsonService.toObject(response, TaxCalculatorResult)
    }

    private List<Invoice> getAllInvoices() {
        def response = mockMvc.perform(get("/invoices"))
                .andExpect(status().isOk())
                .andReturn()
                .response
                .contentAsString

        return jsonService.toObject(response, Invoice[])
    }

    private ResultActions deleteInvoice(long id) {
        mockMvc.perform(delete("/invoices/$id"))
                .andExpect(status().isOk())
    }

    def setupSpec() {
        resetFile("test_db/invoices.json")
        resetFile("test_db/nextId.txt")
    }

    def cleanupSpec() {
        Files.deleteIfExists(Path.of("test_db/invoices.json"))
        Files.deleteIfExists(Path.of("test_db/nextId.txt"))
    }

    private void resetFile(String filePath) {
        def path = Path.of(filePath)
        if (Files.exists(path)) {
            Files.deleteIfExists(path)
            Files.createFile(path)
        } else {
            Files.createDirectories(path.getParent())
            Files.createFile(path)
        }
    }
}
