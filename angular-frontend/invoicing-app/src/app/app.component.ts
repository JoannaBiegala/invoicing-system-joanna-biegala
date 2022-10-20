import {Component} from '@angular/core';
import {Company} from "./company"

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  companies = [
  {
  "name": "first",
  "taxIdentificationNumber": "tax",
  "address": "aaa",
  "pensionInsurance": 22.3,
  "healthInsurance": 33.7
  },
 {
   "name": "second",
   "taxIdentificationNumber": "tax2",
   "address": "aaa2",
   "pensionInsurance": 222.3,
   "healthInsurance": 233.7
   }
  ]
}
