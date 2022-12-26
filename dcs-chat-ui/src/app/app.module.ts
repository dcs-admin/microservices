import { HttpClientModule } from '@angular/common/http';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { BrowserModule } from '@angular/platform-browser';
import { PickerModule } from '@ctrl/ngx-emoji-mart';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { ChatComponent } from './chat/chat.component';
import { LoginComponent } from './login/login.component';
import { PopupComponent } from './popup/popup.component';
import { SidebarComponent } from './sidebar/sidebar.component';
import { DateAgoPipe } from './_utils/dateago.pipe';
 
@NgModule({
  declarations: [
    AppComponent, SidebarComponent, ChatComponent, LoginComponent, PopupComponent, DateAgoPipe
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    PickerModule,
    NgbModule,
    HttpClientModule
  ],
   providers: [DateAgoPipe],
  bootstrap: [AppComponent]
})
export class AppModule { }
