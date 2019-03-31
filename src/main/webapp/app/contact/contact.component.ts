import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Component({
    selector: 'jhi-contact',
    templateUrl: './contact.component.html',
    styles: []
})
export class ContactComponent implements OnInit {
    model: UserModel = {
        name: '',
        email: '',
        subject: '',
        message: ''
    };
    constructor(private http: HttpClient) {}
    ngOnInit() {}
    sendNotification(): void {
        const url = 'http://localhost:8080/api/contact';
        this.http.post(url, this.model).subscribe(
            res => {
                location.reload();
            },
            err => {
                alert('An error has occured while sending email');
            }
        );
    }
}
export interface UserModel {
    name: string;
    email: string;
    subject: string;
    message: string;
}
