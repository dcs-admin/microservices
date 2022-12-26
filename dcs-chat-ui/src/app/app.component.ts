import { Component, EventEmitter, OnDestroy, OnInit, Output } from '@angular/core';
import { Conversation } from './model/conversation.model';
import { User } from './_models/user';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {
 
  public currentUser: User = new User();
  conversation:any; 
 

  onConversationSelected(conversation: Conversation){
    this.conversation = conversation;
    this.conversation.latestMessageRead = true;
    this.conversation.unread = 0;
  }

  
  loginClicked(currentUserInfo: any){
    console.log('Login Clicked')
    this.currentUser = currentUserInfo;
  }

  logoutClicked(userInfo: any){
    this.currentUser = new User();;
  }


}
