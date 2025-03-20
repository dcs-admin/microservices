import { HttpClient } from '@angular/common/http';
import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { CartService } from '../services/cart.service';
import { ProductService } from '../services/product.service';
import { SharedService } from '../services/shared.service';

@Component({
  selector: 'app-product-catalog',
  templateUrl: './product-catalog.component.html',
  styleUrls: ['./product-catalog.component.scss']
})
export class ProductCatalogComponent  implements OnInit {
  //@Input() searchQuery: string = '';
  @Output() cartUpdated = new EventEmitter<number>();
  searchQuery = '';
  cartCount = 0;
  
  cartItems: number = 0; // 🛒 Track cart items
  products: any[] = []; // 🛍 Store products from backend
  displayCount = 10;

  constructor(private productService: ProductService,
    private sharedService: SharedService,
    private cartService: CartService
    ) {}

  ngOnInit() {
    this.fetchProducts(); // 🚀 Fetch products when component loads

    this.sharedService.searchQuery$.subscribe(query => {
      this.searchQuery = query;
    });

    this.sharedService.cartCount$.subscribe(count => {
      this.cartCount = count;
    });
  }
  
  // products = [
  //   { name: 'Smartphone', price: 699, image: 'https://encrypted-tbn1.gstatic.com/shopping?q=tbn:ANd9GcQyeLzp_g2l5QeSKpM-q2Y2V6o9WCToIuCANMPQqMDa6m6XgY4WjfmGyLay7S7nbKT7sVJDPU9wEZTbVJWe5F-nK2vvd6abQLuF6E5DBX09' },
  //   { name: 'Laptop', price: 999, image: 'https://vsprod.vijaysales.com/media/catalog/product/2/3/238583_1_.jpg?optimize=medium&fit=bounds&height=500&width=500' },
  //   { name: 'Headphones', price: 199, image: 'https://vsprod.vijaysales.com/media/catalog/product/2/3/238583_1_.jpg?optimize=medium&fit=bounds&height=500&width=500' },
  //   { name: 'Smartwatch', price: 299, image: 'https://vsprod.vijaysales.com/media/catalog/product/b/o/book5_pro_ultra7_1__2.jpg?optimize=medium&fit=bounds&height=250&width=250' },
  //   { name: 'Camera', price: 499, image: 'https://vsprod.vijaysales.com/media/catalog/product/2/3/238583_1_.jpg?optimize=medium&fit=bounds&height=500&width=500' },
  //   { name: 'Gaming Console', price: 399, image: 'https://vsprod.vijaysales.com/media/catalog/product/2/3/238685_1_.jpg?optimize=medium&fit=bounds&height=250&width=250' },
  //   { name: 'Smartphone', price: 699, image: 'https://encrypted-tbn1.gstatic.com/shopping?q=tbn:ANd9GcQyeLzp_g2l5QeSKpM-q2Y2V6o9WCToIuCANMPQqMDa6m6XgY4WjfmGyLay7S7nbKT7sVJDPU9wEZTbVJWe5F-nK2vvd6abQLuF6E5DBX09' },
  //   { name: 'Laptop', price: 999, image: 'https://vsprod.vijaysales.com/media/catalog/product/2/3/238583_1_.jpg?optimize=medium&fit=bounds&height=500&width=500' },
  //   { name: 'Headphones', price: 199, image: 'https://vsprod.vijaysales.com/media/catalog/product/2/3/238583_1_.jpg?optimize=medium&fit=bounds&height=500&width=500' },
  //   { name: 'Smartwatch', price: 299, image: 'https://vsprod.vijaysales.com/media/catalog/product/b/o/book5_pro_ultra7_1__2.jpg?optimize=medium&fit=bounds&height=250&width=250' },
  //   { name: 'Camera', price: 499, image: 'https://vsprod.vijaysales.com/media/catalog/product/2/3/238583_1_.jpg?optimize=medium&fit=bounds&height=500&width=500' },
  //   { name: 'Gaming Console', price: 399, image: 'https://vsprod.vijaysales.com/media/catalog/product/2/3/238685_1_.jpg?optimize=medium&fit=bounds&height=250&width=250' },
  //   { name: 'Smartphone', price: 699, image: 'https://encrypted-tbn1.gstatic.com/shopping?q=tbn:ANd9GcQyeLzp_g2l5QeSKpM-q2Y2V6o9WCToIuCANMPQqMDa6m6XgY4WjfmGyLay7S7nbKT7sVJDPU9wEZTbVJWe5F-nK2vvd6abQLuF6E5DBX09' },
  //   { name: 'Laptop', price: 999, image: 'https://vsprod.vijaysales.com/media/catalog/product/2/3/238583_1_.jpg?optimize=medium&fit=bounds&height=500&width=500' },
  //   { name: 'Headphones', price: 199, image: 'https://vsprod.vijaysales.com/media/catalog/product/2/3/238583_1_.jpg?optimize=medium&fit=bounds&height=500&width=500' },
  //   { name: 'Smartwatch', price: 299, image: 'https://vsprod.vijaysales.com/media/catalog/product/b/o/book5_pro_ultra7_1__2.jpg?optimize=medium&fit=bounds&height=250&width=250' },
  //   { name: 'Camera', price: 499, image: 'https://vsprod.vijaysales.com/media/catalog/product/2/3/238583_1_.jpg?optimize=medium&fit=bounds&height=500&width=500' },
  //   { name: 'Gaming Console', price: 399, image: 'https://vsprod.vijaysales.com/media/catalog/product/2/3/238685_1_.jpg?optimize=medium&fit=bounds&height=250&width=250' }
  // ];

   

  get filteredProducts() {
    return this.products.filter(product =>
      product.name.toLowerCase().includes(this.searchQuery.toLowerCase())
    );
  }

  // addToCart(product: any) {
  //   this.cartItems++; // 🛒 Increase cart count
  //   this.cartUpdated.emit(this.cartItems); // 🔥 Emit event to update header
  // }

  // addToCart(product: any) {
  //   this.cartCount++;
  //   this.sharedService.updateCartCount(this.cartCount);
  // }

  addToCart(product: any) {
    this.cartService.addToCart(product);
    alert(product.name+" added to cart, please click on cart to buy")
  }

  fetchProducts() {
    this.productService.getProducts()
      .subscribe(
        data => this.products = data,
        error => console.error('Error fetching products', error)
      );
  }


  loadMore() {
    this.displayCount += 5; // Load 5 more orders on click
  }

}
