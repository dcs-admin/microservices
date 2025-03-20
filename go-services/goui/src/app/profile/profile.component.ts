import { Component, OnInit } from '@angular/core';
import { ProfileService } from '../services/profile.service';
import { AuthService } from '../services/auth.service';
import { CustomerService } from '../services/customer.service';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.scss']
})
export class ProfileComponent implements OnInit {
  user: any;
  orders: any[] = [];
  customer: any;

  constructor(
    private profileService: ProfileService, 
    private authService: AuthService,
    private customerService: CustomerService
    ) {}

  ngOnInit() {
    this.user = this.authService.getUser(); // Get the logged-in user details
    this.customerService.getCustomer( this.user.id).subscribe(data => {
      this.customer = data;
      this.authService.setOnlyUser(this.customer );
    });
  }

  getCustomerKeys(customer: object): string[] {
    return Object.keys(customer);
  }
}
