import { HttpClient } from '@angular/common/http';
import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import { AuthService } from '../services/auth.service';
import { CartService } from '../services/cart.service';

@Component({
  selector: 'app-cart',
  templateUrl: './cart.component.html',
  styleUrls: ['./cart.component.scss']
})
export class CartComponent implements OnInit {
  cartItems: any[] = [];
  isCartOpen = true;


  @Output() closeCartEmiter = new EventEmitter<void>();
  
  constructor(public cartService: CartService, 
    private authService: AuthService,
    private http: HttpClient,
    ) {}

  ngOnInit() {
    this.cartService.cartItems$.subscribe(items => {
      this.cartItems = items;
    });
  }

  get totalAmount(): number {
    return this.cartItems.reduce((total, item) => total + item.price * item.quantity, 0);
  }

  removeItem(productId: number) {
    this.cartService.removeFromCart(productId);
  }

  openCart() {
    this.isCartOpen = true;
  }

  closeCart() {
    this.isCartOpen = false;
    this.closeCartEmiter.emit()
  }

  cart(){
    console.log("Cart: "+this.cartItems.length)
  }


  proceedToCheckout() {
    if (this.cartItems.length === 0) {
      alert("Your cart is empty!");
      return;
    }

    const orderData = this.cartItems.map(item => ({
      customer_id: this.authService.getUser().id, // Replace with actual customer ID from logged-in user
      product_id: item.id,
      quantity: item.quantity,
      per_cost: item.price,
      total_cost: item.quantity * item.price,
      status: "Pending"
    }));

    
    this.cartService.postOrder(orderData).subscribe(
      (response:any) => {
        alert("Order Status: "+response.message+", Total: "+response.order_count+", refernce_ids: "+response.order_ids);
        this.cartService.clearCart();
      },
      error => {
        alert("Error placing order. Try again!");
        console.error("Order error:", error);
      }
    );
  }

  isUserLoggedIn(): boolean {
    return localStorage.getItem("user") !== null;
  }
}
