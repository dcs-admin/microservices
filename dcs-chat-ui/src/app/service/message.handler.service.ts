import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable, Subject, ReplaySubject, from, of, range } from 'rxjs';
import { Message } from "../model/message.model";
import {  UserContactDTO } from "../_models/usercontact";
 

@Injectable({ providedIn: "root" })
export class MessageHandlerService {

    url: string ='http://localhost:8080';
   

    constructor(private http: HttpClient) { }

    loadData(): Observable<any> {
        return this.http.get(this.url) 
    } 
     
    postMessage(message: Message){ 
        return this.http.post(this.url+'/userMessages', message).toPromise() ;
    } 

    loadConversations(userId: number){
        return this.http.get(this.url+'/userContacts/'+userId).toPromise() ;
    }

    register(user: any){
        return this.http.post(this.url+'/users', user).toPromise() ;
    }

    login(user: any){
        return this.http.post(this.url+'/users/login', user).toPromise() ;
    }

    logout(userId: number){
        return this.http.get(this.url+'/users/logout/'+userId).toPromise() ;
    }

    addContact(userContactDTO: UserContactDTO){ 
        return this.http.post(this.url+'/userContacts', userContactDTO).toPromise() ;
    } 

    getStatus(userId: number){
        return this.http.get(this.url+'/users/getstatus/'+userId).toPromise() ;
    }

    


}