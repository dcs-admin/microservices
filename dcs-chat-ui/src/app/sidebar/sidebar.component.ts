import { Component, EventEmitter, Input, OnDestroy, OnInit, Output } from '@angular/core';
import { ModalDismissReasons, NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { webSocket, WebSocketSubject } from 'rxjs/webSocket';
import { environment } from 'src/environments/environment';
import { Conversation } from '../model/conversation.model';
import { MessageHandlerService } from '../service/message.handler.service';
import { UserContactDTO } from '../_models/usercontact';


declare const SockJS: any;
declare const Stomp: any;

@Component({
  selector: 'app-sidebar',
  templateUrl: './sidebar.component.html',
  styleUrls: ['./sidebar.component.scss'],
})
export class SidebarComponent implements OnInit, OnDestroy {

  public conversations: Array<Conversation> = [];

  @Output() conversationClicked: EventEmitter<any> = new EventEmitter();

  @Output() logoutClicked: EventEmitter<any> = new EventEmitter();

  @Input() currentUser:any;

  public searchText: string = "";

  public stompClient : any= null;
  public closeResult: string = '';
  public error: string = '';
  // private unReadCounts : any = {}; 
  public userContactDTO: UserContactDTO = new UserContactDTO() ;  
  public modal: any;

  constructor(
    private messageHandlerService: MessageHandlerService,
    private modalService: NgbModal
    ) {}

  ngOnInit(): void {
    this.loadConversations();
    this.listenToMessages();
  }

  logout(){
    if(window.confirm("Are you sure to logout")){
     
      this.messageHandlerService.logout(this.currentUser.userNumber)
      .then(
        sucess =>{ 
          console.log("Logout SUCCESS: ", sucess); 
        },
        fail =>{
          this.error = fail;
        }) 
      }

      this.currentUser = null;
      this.logoutClicked.emit(null); 

  }

  listenToMessages() {
    var socket = new SockJS(environment.server_url+'/stomp-endpoint');
    this.stompClient = Stomp.over(socket);
    this.stompClient.connect({},  () => {
      console.log('Here me: messages ')
      this.stompClient.subscribe('/topic/messages/'+this.currentUser.userNumber, (message: any) => {
        let msg: any = JSON.parse(message.body);
        //console.log('Body:' +msg);
        this.loadConversation(msg);

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

  loadConversation(msg: any) {
    console.log('Received Msg: ', msg)
    let newConv : Conversation  = new Conversation();
    this.conversations.filter(x => x.userContact.contactId == msg.fromUser).forEach(conversation => {  
      let currUnRead = conversation.unread === undefined ? 1: conversation.unread+1; 
      conversation.unread =  currUnRead;
      //this.unReadCounts[conversation.cid] = currUnRead; 
      //this.unReadCounts[conversation.cid] = this.unReadCounts[conversation.cid] === undefined ? 0 : this.unReadCounts[conversation.cid];
      //this.unReadCounts[conversation.cid] += conversation.unread=== undefined ? 0: conversation.unread;  
      conversation.latestMessage = msg.body;
      //conversation.unread = this.unReadCounts[conversation.cid];

      conversation.messages.unshift({
        id: msg.id,
        body: msg.body,
        time: msg.time,
        me: false,
        fromUser: msg.fromUser,
        toUser: msg.toUser
      });

      newConv = conversation;
    })
 
    const removeIndex = this.conversations.findIndex( item => item.userContact.contactId === msg.fromUser ); 
    this.conversations.splice( removeIndex, 1 );
    this.conversations.unshift( newConv);

  }


  loadConversations(){
    this.messageHandlerService.loadConversations(this.currentUser.userNumber).then(data =>
      {
        const temp: any = data;
        this.conversations = temp.userContactMessages;
      });
  }


  open(content:any) {
    this.userContactDTO = new UserContactDTO();
    this.modal = this.modalService.open(content, {ariaLabelledBy: 'modal-basic-title'}).result
    .then((result) => {
      this.closeResult = `Closed with: ${result}`;
    }, (reason) => {
      this.closeResult = `Dismissed ${this.getDismissReason(reason)}`;
      console.log("Close Resaon: ", this.closeResult)
    });
  } 

  private getDismissReason(reason: any): string {
    if (reason === ModalDismissReasons.ESC) {
      return 'by pressing ESC';
    } else if (reason === ModalDismissReasons.BACKDROP_CLICK) {
      return 'by clicking on a backdrop';
    } else {
      return  `with: ${reason}`;
    }
  }

  

  // public conversations: Array<Conversation> = [
  //   {
  //     cid: 1, firstName: 'Anji',
  //     time: '8:21',
  //     latestMessage: 'Hi there!!',
  //     latestMessageRead: false,
  //     messages: [
  //       { id: 1, body: 'Hello world', time: '8:21', me: true, fromUser: 1, toUser: 2},
  //       { id: 2, body: 'How are you?', time: '8:21', me: false, fromUser: 1, toUser: 2},
  //       { id: 3, body: 'I am fine thanks', time: '8:21', me: true, fromUser: 1, toUser: 2},
  //       { id: 4, body: 'Glad to hear that', time: '8:21', me: false, fromUser: 1, toUser: 2},
  //     ],
  //   },
  //   {
  //     cid: 2, firstName: 'Anu',
  //     time: '8:21',
  //     latestMessage: 'wow',
  //     latestMessageRead: true,
  //     messages: [
  //       { id: 1, body: 'Hello world', time: '8:21', me: true, fromUser: 1, toUser: 2},
  //       { id: 2, body: 'How are you?', time: '8:21', me: false, fromUser: 1, toUser: 2},
  //       { id: 3, body: 'I am fine thanks', time: '8:21', me: true, fromUser: 1, toUser: 2},
  //       { id: 4, body: 'Glad to hear that', time: '8:21', me: false, fromUser: 1, toUser: 2},
  //     ],
  //   },
  //   {
  //     cid: 3, firstName: 'Devi',
  //     time: '8:21',
  //     latestMessage: 'I am fine',
  //     latestMessageRead: false,
  //     messages: [
  //       { id: 1, body: 'Hey Man', time: '8:21', me: true, fromUser: 1, toUser: 2},
  //       { id: 2, body: 'How are you?', time: '8:21', me: false, fromUser: 1, toUser: 2},
  //       { id: 3, body: 'I am fine thanks', time: '8:21', me: true, fromUser: 1, toUser: 2},
  //       { id: 4, body: 'Glad to hear that', time: '8:21', me: false, fromUser: 1, toUser: 2},
  //     ],
  //   },
  //   {
  //     cid: 14, firstName: 'Richard',
  //     time: '8:21',
  //     latestMessage: 'lol',
  //     latestMessageRead: true,
  //     messages: [
  //       { id: 1, body: 'Hello world', time: '8:21', me: true, fromUser: 1, toUser: 2},
  //       { id: 2, body: 'How are you?', time: '8:21', me: false, fromUser: 1, toUser: 2},
  //       { id: 3, body: 'I am fine thanks', time: '8:21', me: true, fromUser: 1, toUser: 2},
  //       { id: 4, body: 'Glad to hear that', time: '8:21', me: false, fromUser: 1, toUser: 2},
  //     ],
  //   },
  //   {
  //     cid: 4, firstName: 'Mahi',
  //     time: '8:21',
  //     latestMessage: 'Alright',
  //     latestMessageRead: false,
  //     messages: [
  //       { id: 1, body: 'Hello world', time: '8:21', me: true, fromUser: 1, toUser: 2},
  //       { id: 2, body: 'How are you?', time: '8:21', me: false, fromUser: 1, toUser: 2},
  //       { id: 3, body: 'I am fine thanks', time: '8:21', me: true, fromUser: 1, toUser: 2},
  //       { id: 4, body: 'Glad to hear that', time: '8:21', me: false, fromUser: 1, toUser: 2},
  //     ],
  //   },
  //   {
  //     cid: 5, firstName: 'Badri',
  //     time: '8:21',
  //     latestMessage: "Let's go",
  //     latestMessageRead: false,
  //     messages: [
  //       { id: 1, body: 'Hello world', time: '8:21', me: true, fromUser: 1, toUser: 2},
  //       { id: 2, body: 'How are you?', time: '8:21', me: false, fromUser: 1, toUser: 2},
  //       { id: 3, body: 'I am fine thanks', time: '8:21', me: true, fromUser: 1, toUser: 2},
  //       { id: 4, body: 'Glad to hear that', time: '8:21', me: false, fromUser: 1, toUser: 2},
  //     ],
  //   },
  //   {
  //     cid: 6, firstName: 'Tom',
  //     time: '8:21',
  //     latestMessage: 'I see',
  //     latestMessageRead: true,
  //     messages: [
  //       { id: 1, body: 'Hello world', time: '8:21', me: true, fromUser: 1, toUser: 2},
  //       { id: 2, body: 'How are you?', time: '8:21', me: false, fromUser: 1, toUser: 2},
  //       { id: 3, body: 'I am fine thanks', time: '8:21', me: true, fromUser: 1, toUser: 2},
  //       { id: 4, body: 'Glad to hear that', time: '8:21', me: false, fromUser: 1, toUser: 2},
  //     ],
  //   },
  //   {
  //     cid: 7, firstName: 'Karimnagar',
  //     time: '8:21',
  //     latestMessage: 'OMG',
  //     latestMessageRead: false,
  //     messages: [
  //       { id: 1, body: 'Hello world', time: '8:21', me: true, fromUser: 1, toUser: 2},
  //       { id: 2, body: 'How are you?', time: '8:21', me: false, fromUser: 1, toUser: 2},
  //       { id: 3, body: 'I am fine thanks', time: '8:21', me: true, fromUser: 1, toUser: 2},
  //       { id: 4, body: 'Glad to hear that', time: '8:21', me: false, fromUser: 1, toUser: 2},
  //     ],
  //   },
  //   {
  //     cid: 8, firstName: 'Jagadamba',
  //     time: '8:21',
  //     latestMessage: 'Oh No',
  //     latestMessageRead: false,
  //     messages: [
  //       { id: 1, body: 'Hello world', time: '8:21', me: true, fromUser: 1, toUser: 2},
  //       { id: 2, body: 'How are you?', time: '8:21', me: false, fromUser: 1, toUser: 2},
  //       { id: 3, body: 'I am fine thanks', time: '8:21', me: true, fromUser: 1, toUser: 2},
  //       { id: 4, body: 'Glad to hear that', time: '8:21', me: false, fromUser: 1, toUser: 2},
  //     ],
  //   },
  //   {
  //     cid: 13, firstName: 'Bala',
  //     time: '8:21',
  //     latestMessage: 'Thanks',
  //     latestMessageRead: true,
  //     messages: [
  //       { id: 1, body: 'Hello world', time: '8:21', me: true, fromUser: 1, toUser: 2},
  //       { id: 2, body: 'How are you?', time: '8:21', me: false, fromUser: 1, toUser: 2},
  //       { id: 3, body: 'I am fine thanks', time: '8:21', me: true, fromUser: 1, toUser: 2},
  //       { id: 4, body: 'Glad to hear that', time: '8:21', me: false, fromUser: 1, toUser: 2},
  //     ],
  //   },
  //   {
  //     cid: 9, firstName: 'Anasuya',
  //     time: '8:21',
  //     latestMessage: 'Take care',
  //     latestMessageRead: false,
  //     messages: [
  //       { id: 1, body: 'Hello world', time: '8:21', me: true, fromUser: 1, toUser: 2},
  //       { id: 2, body: 'How are you?', time: '8:21', me: false, fromUser: 1, toUser: 2},
  //       { id: 3, body: 'I am fine thanks', time: '8:21', me: true, fromUser: 1, toUser: 2},
  //       { id: 4, body: 'Glad to hear that', time: '8:21', me: false, fromUser: 1, toUser: 2},
  //     ],
  //   },
  //   {
  //     cid: 10, firstName: 'Chakri',
  //     time: '8:21',
  //     latestMessage: 'I am coming now',
  //     latestMessageRead: false,
  //     messages: [
  //       { id: 1, body: 'Hello world', time: '8:21', me: true, fromUser: 1, toUser: 2},
  //       { id: 2, body: 'How are you?', time: '8:21', me: false, fromUser: 1, toUser: 2},
  //       { id: 3, body: 'I am fine thanks', time: '8:21', me: true, fromUser: 1, toUser: 2},
  //       { id: 4, body: 'Glad to hear that', time: '8:21', me: false, fromUser: 1, toUser: 2},
  //     ],
  //   },
  //   {
  //     cid: 11, firstName: 'Chiru',
  //     time: '8:21',
  //     latestMessage: 'Good Morning!',
  //     latestMessageRead: true,
  //     messages: [
  //       { id: 1, body: 'Hello world', time: '8:21', me: true, fromUser: 1, toUser: 2},
  //       { id: 2, body: 'How are you?', time: '8:21', me: false, fromUser: 1, toUser: 2},
  //       { id: 3, body: 'I am fine thanks', time: '8:21', me: true, fromUser: 1, toUser: 2},
  //       { id: 4, body: 'Glad to hear that', time: '8:21', me: false, fromUser: 1, toUser: 2},
  //     ],
  //   },
  //   {
  //     cid: 12, firstName: 'Nagarjuna',
  //     time: '8:21',
  //     latestMessage: 'Good Morning!',
  //     latestMessageRead: true,
  //     messages: [
  //       { id: 1, body: 'Hello world', time: '8:21', me: true, fromUser: 1, toUser: 2},
  //       { id: 2, body: 'How are you?', time: '8:21', me: false, fromUser: 1, toUser: 2},
  //       { id: 3, body: 'I am fine thanks', time: '8:21', me: true, fromUser: 1, toUser: 2},
  //       { id: 4, body: 'Glad to hear that', time: '8:21', me: false, fromUser: 1, toUser: 2},
  //     ],
  //   },
  // ];

  get filteredConversations() {
    return this.conversations.filter((conversation) => {
      return (
        conversation.firstName
          .toLowerCase()
          .includes(this.searchText.toLowerCase()) ||
        conversation.latestMessage
          .toLowerCase()
          .includes(this.searchText.toLowerCase())
      );
    });
  }

 


}
