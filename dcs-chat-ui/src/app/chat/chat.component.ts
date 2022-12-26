import { Component, EventEmitter, Input, OnInit, Output, SimpleChanges } from '@angular/core';
import { elementAt } from 'rxjs';
import { Conversation } from '../model/conversation.model';
import { Message } from '../model/message.model';
import { MessageHandlerService } from '../service/message.handler.service';
import { DateAgoPipe } from '../_utils/dateago.pipe';

declare const SockJS: any;
declare const Stomp: any;

@Component({
  selector: 'app-chat',
  templateUrl: './chat.component.html',
  styleUrls: ['./chat.component.scss'],
})
export class ChatComponent implements OnInit {
  @Input() conversation:any;
  @Input() currentUser:any;
  @Output() onSubmit: EventEmitter<any> = new EventEmitter();
  emojiPickerVisible: any;
  public message : any = '';
  public currentStatus: any = '';
  public stompClient : any= null;
  public userLoginStatus: any ;
  
  constructor(private messageHandlerService: MessageHandlerService,
    public dateAgo: DateAgoPipe) {}

  ngOnInit(): void {
   
  }

  ngOnChanges(changes: SimpleChanges) { 

    this. disconnect();
    const element = changes['conversation'].currentValue;
    const userNumber = element.userContact.contactId

    this.messageHandlerService.getStatus(userNumber).then(
      success =>{
        const userLoginStatus :any = success;
        this.currentStatus = userLoginStatus.currentStatus;
        this.userLoginStatus = userLoginStatus;
      }
    ) 
    this.listenToStatus(userNumber); 
    }


  submitMessage(event: any) : boolean{
    let value = event.target.value.trim();
    this.message = '';
    if (value.length < 1) return false;
    this.conversation.latestMessage = value;
    let message: Message = new Message();
    message.body = value;
    message.time='10:21';
    message.me = true; 
    message.fromUser=this.currentUser.userNumber;
    message.toUser=this.conversation.userContact.contactId;

    console.log('Send User: ', this.conversation)
    this.conversation.messages.unshift(message);

    console.log("sss")
    this.messageHandlerService.postMessage(message);

    return true;
  }

  emojiClicked(event: any) {
    this.message += event.emoji.native;
  }



  listenToStatus(userNumber: number) {
    var socket = new SockJS('http://localhost:8080/stomp-endpoint');
    this.stompClient = Stomp.over(socket);
    console.log('Listening on UserNumber: '+userNumber)
    this.stompClient.connect({},  () => { 
      this.stompClient.subscribe('/topic/logins/'+userNumber, (message: any) => {
        let msg: any = JSON.parse(message.body);
        console.log("New Status code received", msg)
        this.currentStatus = msg.currentStatus; 
      });
    }); 
  }

  ngOnDestroy(){
    this. disconnect();
  }

  disconnect() {
    if (this.stompClient !== null) { 
      this.stompClient.disconnect();
      console.log("Disconnected");
    } 
    
}



}
