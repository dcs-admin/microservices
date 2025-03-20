
import { Component, OnInit } from '@angular/core';
import { ProfileService } from '../services/profile.service';
import { AuthService } from '../services/auth.service';
import { CustomerService } from '../services/customer.service';

@Component({
  selector: 'app-orders',
  templateUrl: './orders.component.html',
  styleUrls: ['./orders.component.scss']
})
export class OrdersComponent implements OnInit {
  user: any;
  orders: any[] = [];
  customer: any;
  displayCount = 5; // Show first 5 orders initially

  constructor(
    private profileService: ProfileService, 
    private authService: AuthService,
    private customerService: CustomerService
    ) {}

  ngOnInit() {
    this.user = this.authService.getUser(); // Get the logged-in user details
    this.customerService.getCustomer( this.user.id).subscribe(data => {
      this.customer = data;
      this.fetchOrders();
    });
  }

  fetchOrders() {
    this.profileService.getUserOrders().subscribe(data => {
      this.orders = data;
    });
  }

  loadMore() {
    this.displayCount += 5; // Load 5 more orders on click
  }

  
}
