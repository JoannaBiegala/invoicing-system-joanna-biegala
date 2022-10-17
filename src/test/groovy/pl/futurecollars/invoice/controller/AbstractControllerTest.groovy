package pl.futurecollars.invoice.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import pl.futurecollars.invoice.model.Company
import pl.futurecollars.invoice.model.Invoice
import pl.futurecollars.invoice.service.tax.TaxCalculatorResult
import pl.futurecollars.invoice.utils.JsonService
import spock.lang.Specification

import java.nio.file.Files
import java.nio.file.Path

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import static pl.futurecollars.invoice.TestHelpers.COMPANIES_ENDPOINT
import static pl.futurecollars.invoice.TestHelpers.company
import static pl.futurecollars.invoice.TestHelpers.invoice
import static pl.futurecollars.invoice.TestHelpers.INVOICES_ENDPOINT
import static pl.futurecollars.invoice.TestHelpers.TAX_ENDPOINT

class AbstractControllerTest extends Specification {

    @Autowired
    MockMvc mockMvc

    @Autowired
    JsonService jsonService

    def setup() {
        getAllInvoices().each { invoice -> delete(invoice.id, INVOICES_ENDPOINT) }
    }

    long add(Object object, String endpoint) {
        Long.valueOf(
                mockMvc.perform(
                        post(endpoint)
                                .content(getAsJson(object))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                        .andExpect(status().isOk())
                        .andReturn()
                        .response
                        .contentAsString
        )
    }

    void delete(long id, String endpoint) {
        mockMvc.perform(
                delete("$endpoint/$id"))
                .andExpect(status().isOk())
    }

    Object getById(long id, String endpoint, Class clazz) {
        def objectAsString = mockMvc.perform(get("$endpoint/$id"))
                .andExpect(status().isOk())
                .andReturn()
                .response
                .contentAsString

        return getAsObject(objectAsString, clazz)
    }

    List<Invoice> getAllInvoices() {
        def response = mockMvc.perform(get(INVOICES_ENDPOINT))
                .andExpect(status().isOk())
                .andReturn()
                .response
                .contentAsString

        return jsonService.toObject(response, Invoice[])
    }

    List<Company> getAllCompanies() {
        def response = mockMvc.perform(get(COMPANIES_ENDPOINT))
                .andExpect(status().isOk())
                .andReturn()
                .response
                .contentAsString

        return jsonService.toObject(response, Company[])
    }

    List<Invoice> addUniqueInvoices(int count) {
        (1..count).collect { id ->
            def invoice = invoice(id)
            invoice.id = add(invoice, INVOICES_ENDPOINT)
            return invoice
        }
    }

    List<Company> addUniqueCompanies(int count) {
        (1..count).collect { id ->
            def company = company(id)
            company.id = add(company, COMPANIES_ENDPOINT)
            return company
        }
    }


    TaxCalculatorResult getTaxCalculatorResult(Company company) {
        def response =
                mockMvc.perform(
                        post(TAX_ENDPOINT).content(getAsJson(company))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                        .andExpect(status().isOk())
                        .andReturn()
                        .response
                        .contentAsString

        jsonService.toObject(response, TaxCalculatorResult)
    }

    def getAsJson(Object object) {
        jsonService.toJson(object)
    }

    def getAsObject(String objectAsJson, Class clazz) {
        jsonService.toObject(objectAsJson, clazz)
    }

    def setupSpec() {
        resetFile("test_db/invoices.json")
        resetFile("test_db/nextId.txt")
    }

    def cleanupSpec() {
        Files.deleteIfExists(Path.of("test_db/invoices.json"))
        Files.deleteIfExists(Path.of("test_db/nextId.txt"))
    }

    void resetFile(String filePath) {
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
