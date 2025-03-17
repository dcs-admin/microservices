import { Component, EventEmitter, Output } from '@angular/core';
import { User } from './models/user';
import { AuthService } from './services/auth.service';
import { CartService } from './services/cart.service';
import { SharedService } from './services/shared.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {
  loggedIn = false;
  searchQuery = '';
  cartCount = 0; // 🛒 Initialize cart count
  isCartOpen = false; 

  @Output() cartClicked = new EventEmitter<void>(); // Emit event to open cart
  user: User | null = null;

  constructor(
    private sharedService: SharedService,
    private cartService: CartService,
    private authService: AuthService
    ) {
    //this.sharedService.cartCount$.subscribe(count => this.cartCount = count);
    this.cartService.cartItems$.subscribe(cart => {
      this.cartCount = cart.length;
    });

    this.authService.user$.subscribe(user => this.user = user);
  }

  

  logout() {
    this.authService.logout();
  }

  // // 🛒 Update cart count when a product is added
  // updateCartCount(count: number) {
  //   this.cartCount = count;
  // }

  updateSearchQuery(event: any) {
    this.searchQuery = event.target.value;
    this.sharedService.updateSearchQuery(this.searchQuery);
  }

  openCart() {
    this.isCartOpen = true;
    this.cartClicked.emit(); // Emit event to parent
  }


  closeCart() {
    this.isCartOpen = false;
  }
}
