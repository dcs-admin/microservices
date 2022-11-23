import { Component, EventEmitter, OnDestroy, OnInit, Output } from '@angular/core';
import { webSocket, WebSocketSubject } from 'rxjs/webSocket';
import { Conversation } from '../model/conversation.model';


declare const SockJS: any;
declare const Stomp: any;

@Component({
  selector: 'app-sidebar',
  templateUrl: './sidebar.component.html',
  styleUrls: ['./sidebar.component.scss'],
})
export class SidebarComponent implements OnInit, OnDestroy {

  @Output() conversationClicked: EventEmitter<any> = new EventEmitter();
  public searchText: string = "";

  public stompClient : any= null;
  // private unReadCounts : any = {};   

  ngOnInit(): void {
    this.listenToMessages()
  }

  listenToMessages() {
    var socket = new SockJS('http://localhost:8080/stomp-endpoint');
    this.stompClient = Stomp.over(socket);
    this.stompClient.connect({},  () => {
      console.log('Here me: messages ')
      this.stompClient.subscribe('/topic/messages', (message: any) => {
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
    let newConv : Conversation  = new Conversation();
    this.conversations.filter(x => x.cid == msg.id).forEach(conversation => {  
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
        me: false
      });

      newConv = conversation;
    })
 
    const removeIndex = this.conversations.findIndex( item => item.cid === msg.id ); 
    this.conversations.splice( removeIndex, 1 );
    this.conversations.unshift( newConv);

  }


  public conversations: Array<Conversation> = [
    {
      cid: 1, name: 'Anji',
      time: '8:21',
      latestMessage: 'Hi there!!',
      latestMessageRead: false,
      messages: [
        { id: 1, body: 'Hello world', time: '8:21', me: true },
        { id: 2, body: 'How are you?', time: '8:21', me: false },
        { id: 3, body: 'I am fine thanks', time: '8:21', me: true },
        { id: 4, body: 'Glad to hear that', time: '8:21', me: false },
      ],
    },
    {
      cid: 2, name: 'Anu',
      time: '8:21',
      latestMessage: 'wow',
      latestMessageRead: true,
      messages: [
        { id: 1, body: 'Hello world', time: '8:21', me: true },
        { id: 2, body: 'How are you?', time: '8:21', me: false },
        { id: 3, body: 'I am fine thanks', time: '8:21', me: true },
        { id: 4, body: 'Glad to hear that', time: '8:21', me: false },
      ],
    },
    {
      cid: 3, name: 'Devi',
      time: '8:21',
      latestMessage: 'I am fine',
      latestMessageRead: false,
      messages: [
        { id: 1, body: 'Hey Man', time: '8:21', me: true },
        { id: 2, body: 'How are you?', time: '8:21', me: false },
        { id: 3, body: 'I am fine thanks', time: '8:21', me: true },
        { id: 4, body: 'Glad to hear that', time: '8:21', me: false },
      ],
    },
    {
      cid: 14, name: 'Richard',
      time: '8:21',
      latestMessage: 'lol',
      latestMessageRead: true,
      messages: [
        { id: 1, body: 'Hello world', time: '8:21', me: true },
        { id: 2, body: 'How are you?', time: '8:21', me: false },
        { id: 3, body: 'I am fine thanks', time: '8:21', me: true },
        { id: 4, body: 'Glad to hear that', time: '8:21', me: false },
      ],
    },
    {
      cid: 4, name: 'Mahi',
      time: '8:21',
      latestMessage: 'Alright',
      latestMessageRead: false,
      messages: [
        { id: 1, body: 'Hello world', time: '8:21', me: true },
        { id: 2, body: 'How are you?', time: '8:21', me: false },
        { id: 3, body: 'I am fine thanks', time: '8:21', me: true },
        { id: 4, body: 'Glad to hear that', time: '8:21', me: false },
      ],
    },
    {
      cid: 5, name: 'Badri',
      time: '8:21',
      latestMessage: "Let's go",
      latestMessageRead: false,
      messages: [
        { id: 1, body: 'Hello world', time: '8:21', me: true },
        { id: 2, body: 'How are you?', time: '8:21', me: false },
        { id: 3, body: 'I am fine thanks', time: '8:21', me: true },
        { id: 4, body: 'Glad to hear that', time: '8:21', me: false },
      ],
    },
    {
      cid: 6, name: 'Tom',
      time: '8:21',
      latestMessage: 'I see',
      latestMessageRead: true,
      messages: [
        { id: 1, body: 'Hello world', time: '8:21', me: true },
        { id: 2, body: 'How are you?', time: '8:21', me: false },
        { id: 3, body: 'I am fine thanks', time: '8:21', me: true },
        { id: 4, body: 'Glad to hear that', time: '8:21', me: false },
      ],
    },
    {
      cid: 7, name: 'Karimnagar',
      time: '8:21',
      latestMessage: 'OMG',
      latestMessageRead: false,
      messages: [
        { id: 1, body: 'Hello world', time: '8:21', me: true },
        { id: 2, body: 'How are you?', time: '8:21', me: false },
        { id: 3, body: 'I am fine thanks', time: '8:21', me: true },
        { id: 4, body: 'Glad to hear that', time: '8:21', me: false },
      ],
    },
    {
      cid: 8, name: 'Jagadamba',
      time: '8:21',
      latestMessage: 'Oh No',
      latestMessageRead: false,
      messages: [
        { id: 1, body: 'Hello world', time: '8:21', me: true },
        { id: 2, body: 'How are you?', time: '8:21', me: false },
        { id: 3, body: 'I am fine thanks', time: '8:21', me: true },
        { id: 4, body: 'Glad to hear that', time: '8:21', me: false },
      ],
    },
    {
      cid: 13, name: 'Bala',
      time: '8:21',
      latestMessage: 'Thanks',
      latestMessageRead: true,
      messages: [
        { id: 1, body: 'Hello world', time: '8:21', me: true },
        { id: 2, body: 'How are you?', time: '8:21', me: false },
        { id: 3, body: 'I am fine thanks', time: '8:21', me: true },
        { id: 4, body: 'Glad to hear that', time: '8:21', me: false },
      ],
    },
    {
      cid: 9, name: 'Anasuya',
      time: '8:21',
      latestMessage: 'Take care',
      latestMessageRead: false,
      messages: [
        { id: 1, body: 'Hello world', time: '8:21', me: true },
        { id: 2, body: 'How are you?', time: '8:21', me: false },
        { id: 3, body: 'I am fine thanks', time: '8:21', me: true },
        { id: 4, body: 'Glad to hear that', time: '8:21', me: false },
      ],
    },
    {
      cid: 10, name: 'Chakri',
      time: '8:21',
      latestMessage: 'I am coming now',
      latestMessageRead: false,
      messages: [
        { id: 1, body: 'Hello world', time: '8:21', me: true },
        { id: 2, body: 'How are you?', time: '8:21', me: false },
        { id: 3, body: 'I am fine thanks', time: '8:21', me: true },
        { id: 4, body: 'Glad to hear that', time: '8:21', me: false },
      ],
    },
    {
      cid: 11, name: 'Chiru',
      time: '8:21',
      latestMessage: 'Good Morning!',
      latestMessageRead: true,
      messages: [
        { id: 1, body: 'Hello world', time: '8:21', me: true },
        { id: 2, body: 'How are you?', time: '8:21', me: false },
        { id: 3, body: 'I am fine thanks', time: '8:21', me: true },
        { id: 4, body: 'Glad to hear that', time: '8:21', me: false },
      ],
    },
    {
      cid: 12, name: 'Nagarjuna',
      time: '8:21',
      latestMessage: 'Good Morning!',
      latestMessageRead: true,
      messages: [
        { id: 1, body: 'Hello world', time: '8:21', me: true },
        { id: 2, body: 'How are you?', time: '8:21', me: false },
        { id: 3, body: 'I am fine thanks', time: '8:21', me: true },
        { id: 4, body: 'Glad to hear that', time: '8:21', me: false },
      ],
    },
  ];

  get filteredConversations() {
    return this.conversations.filter((conversation) => {
      return (
        conversation.name
          .toLowerCase()
          .includes(this.searchText.toLowerCase()) ||
        conversation.latestMessage
          .toLowerCase()
          .includes(this.searchText.toLowerCase())
      );
    });
  }

  constructor() { }


}
