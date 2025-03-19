import { Component } from '@angular/core';
import { AuthService } from '../../services/auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss']
})
export class RegisterComponent {
  public name = '';
  public email = '';
  public password = '';
  errorMessage = '';

  registerData: any = {
    name: '',
    email: '',
    password: '',
    confirmPassword: '',
    address: '',
    preferences: '',
    location: '',
    sex: '',
    city: '',
    interests: '',
    mobile: ''
  };
   


  constructor(private authService: AuthService, private router: Router) {
    if (this.registerData.password !== this.registerData.confirmPassword) {
      this.errorMessage = "Passwords do not match!";
      return;
    }

  }

  register() {
    this.authService.register( this.registerData ).subscribe(
      response => {
        const resp: any = response;
        alert(resp.message)
        this.router.navigate(['/login']); // Redirect to login page
      },
      error => {
        this.errorMessage =  error.error.message || 'Registration failed!';
      }
    );
  }
}
