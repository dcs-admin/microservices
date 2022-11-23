import { Message } from "./message.model";

export class Conversation {
    cid =  1;
    name = 'Anji';
    time = '8:21';
    latestMessage= 'Hi there!!';
    latestMessageRead = false;
    messages : Array<Message> =[];
    isOnline?: boolean = false;
    unread?: number = 0;
}