import {Component} from '@angular/core';
import {Company} from "./company"

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})

export class AppComponent {

  title: String = 'InvoicingApp';

  companies: Company[] = [];

  newCompany: Company = new Company("","","",0,0);

  onAddButtonClick() {
    this.companies.push(this.newCompany);
    this.newCompany = new Company("","","",0,0);
  }

  deleteCompany(company: Company) {
    this.companies = this.companies.filter(c => c !== company);
  }

  triggerUpdate(company: Company) {
    company.editedCompany = new Company(
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
      updatedCompany.taxIdentificationNumber = updatedCompany.editedCompany.taxIdentificationNumber
      updatedCompany.address = updatedCompany.editedCompany.address
      updatedCompany.name = updatedCompany.editedCompany.name
      updatedCompany.pensionInsurance = updatedCompany.editedCompany.pensionInsurance
      updatedCompany.healthInsurance = updatedCompany.editedCompany.healthInsurance

      updatedCompany.editMode = false;
  }

}
