import { Component, OnInit, Inject, Input } from '@angular/core';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { MessageHandlerService } from '../service/message.handler.service';
import { UserContactDTO } from '../_models/usercontact';
 

interface DialogData {
  email: string;
}

@Component({
  selector: 'app-popup-modal',
  templateUrl: './popup.component.html',
  styleUrls: ['./popup.component.scss']
})
export class PopupComponent implements OnInit {

 
    @Input() public currentUser:any;

    public closeResult: string = '';
    public error: string = '';
    // private unReadCounts : any = {}; 
    public userContactDTO: UserContactDTO = new UserContactDTO() ; 
 
    @Input()
    public modal: any;

    constructor(
        private messageHandlerService: MessageHandlerService,
        private modalService: NgbModal
        ) {}

    ngOnInit() {
    }


  onAddContact(){
    this.userContactDTO.currentUserId = this.currentUser.userNumber;
    this.messageHandlerService.addContact(this.userContactDTO).then(
      sucess =>{
        alert('Contact added');
        //this.getDismissReason('success');
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