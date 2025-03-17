import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { ProductService } from '../services/product.service';
import { SharedService } from '../services/shared.service';

@Component({
  selector: 'app-product-detail',
  templateUrl: './product-detail.component.html',
  styleUrls: ['./product-detail.component.scss']
})
export class ProductDetailComponent implements OnInit {
  product: any;

  quantity: any;

  cartCount = 0;

  constructor(
    private route: ActivatedRoute,
    private productService: ProductService,
    private sharedService: SharedService
  ) {}

  ngOnInit() {
    this.sharedService.cartCount$.subscribe(count => {
      this.cartCount = count;
    });

    const id = Number(this.route.snapshot.paramMap.get('id'));
    this.productService.getProductById(id).subscribe(
      data => this.product = data,
      error => console.error('Error fetching product', error)
    );

    
  }

  addToCart(){
    console.log("Count: "+this.cartCount)
    this.cartCount++;
    this.sharedService.updateCartCount(this.cartCount); 
  }

  buyNow(){
    alert('You are about to buy, payment under implementation')
  }

  check(){
    alert('It is not available at this pincode. try other?')
  }
}
