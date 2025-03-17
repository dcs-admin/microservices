import { Component } from '@angular/core';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-forgot-password',
  templateUrl: './forgot-password.component.html',
  styleUrls: ['./forgot-password.component.scss']
})
export class ForgotPasswordComponent {
  email = '';
  message = '';

  constructor(private authService: AuthService) {}

  sendResetLink() {
    this.authService.forgotPassword(this.email).subscribe(
      () => {
        this.message = 'Password reset link sent to your email!';
      },
      error => {
        this.message = 'Failed to send reset link!';
      }
    );
  }
}
