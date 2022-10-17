package pl.futurecollars.invoice.controller.company

import com.mongodb.client.MongoDatabase
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.ApplicationContext
import org.springframework.http.MediaType
import pl.futurecollars.invoice.controller.AbstractControllerTest
import pl.futurecollars.invoice.model.Company
import spock.lang.Requires
import spock.lang.Stepwise
import spock.lang.Unroll

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import static pl.futurecollars.invoice.TestHelpers.*


@AutoConfigureMockMvc
@SpringBootTest
@Unroll
@Stepwise
class CompanyControllerIntegrationTest extends AbstractControllerTest {

    @Autowired
    private ApplicationContext context

    @Requires({ System.getProperty('spring.profiles.active', '').contains('mongo') })
    def "database is dropped to ensure clean state"() {
        expect:
        MongoDatabase mongoDatabase = context.getBean(MongoDatabase)
        mongoDatabase.drop()
    }

    def setup() {
        getAllCompanies().each { company -> delete(company.id, COMPANIES_ENDPOINT) }
    }

    def "should return empty array when company database is empty"() {
        expect:
        getAllCompanies() == []
    }

    def "should add company and return sequential id"() {
        expect:
        def id = add(firstCompany, COMPANIES_ENDPOINT)
        add(firstCompany, COMPANIES_ENDPOINT) == id + 1
        add(firstCompany, COMPANIES_ENDPOINT) == id + 2
        add(firstCompany, COMPANIES_ENDPOINT) == id + 3
        add(firstCompany, COMPANIES_ENDPOINT) == id + 4
    }

    def "should return all companies"() {
        given:
        def numberOfCompanies = 3
        add(firstCompany, COMPANIES_ENDPOINT)
        add(secondCompany, COMPANIES_ENDPOINT)
        add(thirdCompany, COMPANIES_ENDPOINT)

        when:
        def companies = getAllCompanies()

        then:
        companies.size() == numberOfCompanies
    }

    def "should return correct company when getting by id"() {
        given:
        def expectedCompanies = addUniqueCompanies(5)
        def verifiedCompany = expectedCompanies.get(2)

        when:
        def company = getById(verifiedCompany.getId(), COMPANIES_ENDPOINT, Company.class)

        then:
        company == verifiedCompany
    }

    def "should get status 404 when company id is not found when getting company by id"() {
        given:
        addUniqueCompanies(11)

        expect:
        mockMvc.perform(
                get("$COMPANIES_ENDPOINT/$id")
        )
                .andExpect(status().isNotFound())

        where:
        id << [-100, -2, -1, 0, 168, 1256]
    }

    def "should get status 404 when company id is not found when deleting company by id"() {
        given:
        addUniqueCompanies(11)

        expect:
        mockMvc.perform(
                delete("$COMPANIES_ENDPOINT/$id")
        )
                .andExpect(status().isNotFound())


        where:
        id << [-100, -2, -1, 0, 12, 13, 99, 102, 1000]
    }

    def "should get status 404 when company id is not found when updating company by id"() {
        given:
        addUniqueCompanies(11)

        expect:
        mockMvc.perform(
                put("$COMPANIES_ENDPOINT/$id")
                        .content(getAsJson(company(1)))
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isNotFound())


        where:
        id << [-100, -2, -1, 0, 12, 13, 99, 102, 1000]
    }

    def "should update company by id"() {
        given:
        def id = add(firstCompany, COMPANIES_ENDPOINT)
        def updatedCompany = secondCompany
        when:
        mockMvc.perform(
                put("$COMPANIES_ENDPOINT/$id")
                        .content(getAsJson(updatedCompany))
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())

        then:
        secondCompany.setId(id)
        getById(id, COMPANIES_ENDPOINT, Company.class) == secondCompany

    }

    def "should delete company by id"() {
        given:
        def companies = addUniqueCompanies(69)

        expect:
        companies.each { company -> delete(company.getId(), COMPANIES_ENDPOINT) }
        getAllCompanies().size() == 0
    }

}
