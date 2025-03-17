import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { AuthService } from './auth.service';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class ProductService {
 
  private apiUrl = environment.productEndpoints.baseUrl + environment.productEndpoints.getAll ; // Replace with your Go API URL
  //private authHeader = 'eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VybmFtZSI6InRlc3R1c2VyIiwiZXhwIjoxNzQyMDQzNTc1fQ.0qa9Jh0c2bQHTeh2tJtRUPFaR80H5ZV2K3xKq-CFw10' // 🔐 Hardcoded Bearer Auth Token

  constructor(private http: HttpClient,
    private authSerice: AuthService
    ) {}

  // 🌐 Function to fetch products with Authorization Header
  getProducts(): Observable<any[]> {
    const headers = new HttpHeaders({
      'Authorization': this.authSerice.getUserToken() 
    });

    return this.http.get<any[]>(this.apiUrl, { headers });
  }

  // 🔍 Fetch a single product by ID
  getProductById(id: number): Observable<any> {
    const headers = new HttpHeaders({
      'Authorization': this.authSerice.getUserToken() 
    });
    return this.http.get<any>(`${this.apiUrl}/${id}`, {headers});
  }
}
