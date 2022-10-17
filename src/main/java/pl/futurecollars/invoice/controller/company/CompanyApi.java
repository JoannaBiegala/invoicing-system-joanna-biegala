package pl.futurecollars.invoice.controller.company;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import pl.futurecollars.invoice.model.Company;

@Api(tags = {"company-controller"})
public interface CompanyApi {

  String COMPANIES_ENDPOINT = "/companies";

  @ApiOperation(value = "Get company with given id")
  @GetMapping(value = COMPANIES_ENDPOINT + "/{id}", produces = {"application/json;charset=UTF-8"})
  ResponseEntity<Company> getCompany(@PathVariable long id);

  @ApiOperation(value = "Get list of all companies")
  @GetMapping(value = COMPANIES_ENDPOINT, produces = {"application/json;charset=UTF-8"})
  List<Company> getAll();

  @ApiOperation(value = "Add new company to system")
  @PostMapping(COMPANIES_ENDPOINT)
  long saveCompany(@RequestBody Company company);

  @ApiOperation(value = "Delete company with given id")
  @DeleteMapping(COMPANIES_ENDPOINT + "/{id}")
  ResponseEntity<?> deleteCompany(@PathVariable long id);

  @ApiOperation(value = "Update company with given id")
  @PutMapping(COMPANIES_ENDPOINT + "/{id}")
  ResponseEntity<?> updateCompany(@RequestBody Company company, @PathVariable long id);
}
