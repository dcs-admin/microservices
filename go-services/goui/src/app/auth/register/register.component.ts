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

  constructor(private authService: AuthService, private router: Router) {}

  register() {
    this.authService.register({ name: this.name, email: this.email, password: this.password }).subscribe(
      response => {
        const resp: any = response;
        alert(resp.message)
        this.router.navigate(['/login']); // Redirect to login page
      },
      error => {
        this.errorMessage = 'Registration failed!';
      }
    );
  }
}
