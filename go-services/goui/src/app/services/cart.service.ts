import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class CartService {
  private cartItems: any[] = [];
  private cartItemsSubject = new BehaviorSubject<any[]>([]);

  cartItems$ = this.cartItemsSubject.asObservable(); // Expose as observable


  private apiUrl = environment.orderEndpoints.baseUrl; 
  
  constructor(private http: HttpClient) {}

  postOrder(orderData: any) {
    return this.http.post(`${this.apiUrl + environment.orderEndpoints.getAll}`, orderData, {
      headers: new HttpHeaders(environment.defaultHeaders),
    });
  }

  addToCart(product: any) {
    const existingItem = this.cartItems.find(item => item.id === product.id);
    if (existingItem) {
      existingItem.quantity += 1; // Increase quantity if already in cart
    } else {
      this.cartItems.push({ ...product, quantity: 1 });
    }
    this.cartItemsSubject.next([...this.cartItems]); // Notify subscribers
  }

  removeFromCart(productId: number) {
    this.cartItems = this.cartItems.filter(item => item.id !== productId);
    this.cartItemsSubject.next([...this.cartItems]); // Update subscribers
  }

  clearCart() {
    this.cartItems = [];
    this.cartItemsSubject.next([]);
  }



}
