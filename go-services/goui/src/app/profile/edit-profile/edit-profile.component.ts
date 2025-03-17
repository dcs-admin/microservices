import { Component, OnInit } from '@angular/core';
import { ProfileService } from '../../services/profile.service';
import { AuthService } from '../../services/auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-edit-profile',
  templateUrl: './edit-profile.component.html',
  styleUrls: ['./edit-profile.component.scss']
})
export class EditProfileComponent implements OnInit {
  user: any;

  constructor(private profileService: ProfileService, private authService: AuthService, private router: Router) {}

  ngOnInit() {
    this.user = this.authService.getUser();
  }

  updateProfile() {
    this.profileService.updateProfile(this.user).subscribe(response => {
      alert('Profile updated successfully!');
      this.router.navigate(['/profile']);
    });
  }
}
