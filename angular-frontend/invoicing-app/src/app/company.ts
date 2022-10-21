export class Company {

  public editMode: boolean = false;
  public editedCompany: Company = null;

  constructor(
    public taxIdentificationNumber: string,
    public address: string,
    public name: string,
    public pensionInsurance: number,
    public healthInsurance: number
  ) {
  }
}
