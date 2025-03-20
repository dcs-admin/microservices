import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class CustomerService {
  private apiUrl = environment.customerEndpoints.baseUrl  ;
  //private baseUrl = customerEndpoints 'http://localhost:1000/api'; // Go backend for customer-service

  constructor(private http: HttpClient) {}

  getCustomer(id: number): Observable<any> {
    return this.http.get(`${this.apiUrl}/customers/${id}`, {
      headers: { Authorization: `Bearer ${localStorage.getItem('token')}` }
    });
  }

  updateCustomer(customerData: any): Observable<any> {
    return this.http.put(`${this.apiUrl}/customers/${customerData.id}`, customerData, {
      headers: { Authorization: `Bearer ${localStorage.getItem('token')}` }
    });
  }
}
