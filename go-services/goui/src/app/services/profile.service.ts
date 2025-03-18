import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class ProfileService {
  private apiUrl = environment.orderEndpoints.baseUrl  ;

  constructor(private http: HttpClient) {}

  getUserOrders(): Observable<any> {
    return this.http.get(`${this.apiUrl}/orders`, {
      headers: { Authorization: `Bearer ${localStorage.getItem('token')}` } //Bearer
    });
  }

  updateProfile(userData: any): Observable<any> {
    return this.http.put(`${environment.customerEndpoints.baseUrl }/profile`, userData, {
      headers: { Authorization: `${localStorage.getItem('token')}` } //Bearer
    });
  }
  
}
