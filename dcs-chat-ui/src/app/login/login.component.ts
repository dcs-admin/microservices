import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { first } from 'rxjs/operators';
import { AuthenticationService } from '../service/authentication.service';
import { User } from '../_models/user';
import { MessageHandlerService } from '../service/message.handler.service';



@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss'],
})
export class LoginComponent implements OnInit {

  @Output() loginClicked: EventEmitter<any> = new EventEmitter();
 
  error = ''; 
  public user:User = new User();
  //public userName = '';
  //public password = '';
  public isRegister: boolean = false;
 
   
  constructor(
    private messageHandlerService: MessageHandlerService
    // private formBuilder: FormBuilder,
    // private route: ActivatedRoute,
    // private router: Router,
    //private authenticationService: AuthenticationService
  ) {
    // redirect to home if already logged in
    // if (this.authenticationService.currentUserValue) { 
    //     this.router.navigate(['/']);
    // }
  }

  ngOnInit() {
    // this.loginForm = this.formBuilder.group({
    //     username: ['', Validators.required],
    //     password: ['', Validators.required]
    // });

    // // get return url from route parameters or default to '/'
    // this.returnUrl = this.route.snapshot.queryParams['returnUrl'] || '/';
  }

  login() {

    this.messageHandlerService.login(this.user)
    .then(
      sucess =>{

        console.log("Login SUCCESS: ", sucess);
        const currUser : any = sucess;
        this.user = currUser;
        // this.user.firstName = this.userName;
        // this.user.id = parseInt(this.userName); 
        // this.user.password = this.password;
        this.loginClicked.emit(this.user)
      },
      fail =>{
        this.error = fail;
      })
  
 
    // if((this.userName == '1' || this.userName == '2' )  && this.password == '1'){

    //   this.user.firstName = this.userName;
    //   this.user.id = parseInt(this.userName); 
    //   this.user.password = this.password;

    //   this.loginClicked.emit(this.user)

    // }else{
    //   this.error='User and password not matched! try (1/2)/1';
    // }

  }

  register(){ 
    this.isRegister = true; 
  }



  onRegister() {

    if(this.user.password != this.user.confirmPassword){
      this.error = "Password not matched!";
      return;
    }

    this.messageHandlerService.register( this.user)
    .then(
      sucess =>{
        console.log("Register SUCCESS: ", sucess); 
        this.isRegister = false; 
        this.user = new User();
      },
      fail =>{ 
        if(fail.error && fail.message){
          this.error = fail.error+ ':'+ fail.message;
        }else if(fail.error ){
          this.error = fail.error ;
        }else if(fail.message ){
          this.error = fail.message ;
        }else{
          this.error = fail;
        } 
      })
  }
}
