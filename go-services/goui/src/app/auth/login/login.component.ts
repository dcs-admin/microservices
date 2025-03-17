import { Component } from '@angular/core';
import { AuthService } from '../../services/auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent {
  email = '';
  password = '';
  errorMessage = '';

  constructor(private authService: AuthService, private router: Router) {}

  login() {
    this.authService.login({ email: this.email, password: this.password }).subscribe(
      response  => {
        var currResp: any = response
        this.authService.setUser(currResp.token, currResp.user);
        this.router.navigate(['/products']); // Redirect to product catalog
      },
      error => {
        this.errorMessage = 'Failed Login: '+error.error;
      }
    );
  }
}
