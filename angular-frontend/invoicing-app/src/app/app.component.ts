import {CompanyService} from './company.service';
import {Component, OnInit} from '@angular/core';
import {Company} from './company';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})

export class AppComponent implements OnInit {

  companies: Company[] = [];

  newCompany: Company = new Company(0,"","","",0,0);

  constructor(private companyService: CompanyService) {
  }

  ngOnInit(): void {
    this.companyService.getCompanies().subscribe(companies => {this.companies = companies;
    });
  }

  title: String = 'InvoicingApp';


  onAddButtonClick() {
    this.companyService.addCompany(this.newCompany)
      .subscribe(id => {
        this.newCompany.id = id;
        this.companies.push(this.newCompany);
        this.newCompany = new Company(0,"","","",0,0);
      });
  }

  deleteCompany(companyToDelete: Company) {
    this.companyService.deleteCompany(companyToDelete.id)
      .subscribe(() => {
        this.companies = this.companies.filter(company => company !== companyToDelete);
      })
  }

  triggerUpdate(company: Company) {
    company.editedCompany = new Company(
      company.id,
      company.taxIdentificationNumber,
      company.address,
      company.name,
      company.pensionInsurance,
      company.healthInsurance
    )
    company.editMode = true;
  }

  cancelCompanyUpdate(company: Company) {
    company.editMode = false;
  }

  updateCompany(updatedCompany: Company) {
    this.companyService.editCompany(updatedCompany.editedCompany)
      .subscribe(() => {
        updatedCompany.taxIdentificationNumber = updatedCompany.editedCompany.taxIdentificationNumber
        updatedCompany.address = updatedCompany.editedCompany.address
        updatedCompany.name = updatedCompany.editedCompany.name
        updatedCompany.pensionInsurance = updatedCompany.editedCompany.pensionInsurance
        updatedCompany.healthInsurance = updatedCompany.editedCompany.healthInsurance

        updatedCompany.editMode = false;
      })
  }
}
