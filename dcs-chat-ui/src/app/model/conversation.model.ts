import { Message } from "./message.model";

export class Conversation {
    cid =  1;
    firstName = 'Anji';
    time = '8:21';
    latestMessage= 'Hi there!!';
    latestMessageRead = false;
    messages : Array<Message> =[];
    isOnline?: boolean = false;
    unread?: number = 0;
    userContact ={
        "id":1,
        "userId":1,
        "contactId":2,
        "firstName":"",
        "lastName":"",
        "createdDate":null,
        "updatedDate":null
    };
}