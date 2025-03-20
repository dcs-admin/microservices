import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { BehaviorSubject, Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { User } from '../models/user';



@Injectable({
  providedIn: 'root'
})
export class AuthService {
   
  private apiUrl = environment.authEndpoints.baseUrl;
  private userSubject = new BehaviorSubject<User | null>(this.getUser());
  user$ = this.userSubject.asObservable();
  
  constructor(private http: HttpClient) {}

  register(userData: any) {
    return this.http.post(`${this.apiUrl + environment.authEndpoints.register}`, userData, {
      headers: new HttpHeaders(environment.defaultHeaders),
    });
  }

  login(credentials: any) {
    return this.http.post(`${this.apiUrl+ environment.authEndpoints.login}`, credentials, {
      headers: new HttpHeaders(environment.defaultHeaders),
    });
  }

  forgotPassword(email: string): Observable<any> {
    return this.http.post(`${this.apiUrl}/forgot-password`, { email });
  }


  //Utility Methods

  setUser(token: string, user: User) {
    localStorage.setItem('token', token);
    localStorage.setItem('user', JSON.stringify(user));
    this.userSubject.next(user);
  }

  setOnlyUser(user: User) { 
    localStorage.setItem('user', JSON.stringify(user)); 
  }

  getUserToken(): string{
    return localStorage.getItem('token') ?? '';
  }

  getUser(): any {
    const userJson = localStorage.getItem('user');
    return userJson ? JSON.parse(userJson) : null;
  }

  logout() {
    localStorage.removeItem('token');
    localStorage.removeItem('user');
    this.userSubject.next(null);
  }

  isLoggedIn(): boolean {
    return !!localStorage.getItem('token');
  }
}
