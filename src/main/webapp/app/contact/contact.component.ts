import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { NgbModalConfig, NgbModal } from '@ng-bootstrap/ng-bootstrap';
@Component({
    selector: 'jhi-contact',
    templateUrl: './contact.component.html',
    styles: ['input.ng-invalid{border-left:5px solid red;}input.ng-valid{border-left: 5px solid green;}'],
    providers: [NgbModalConfig, NgbModal]
})
export class ContactComponent implements OnInit {
    model: UserModel = {
        name: '',
        emailMsg: '',
        subject: '',
        message: ''
    };
    constructor(private http: HttpClient, private router: Router, config: NgbModalConfig, private modalService: NgbModal) {
        config.backdrop = 'static';
        config.keyboard = false;
    }
    open(content) {
        this.modalService.open(content);
    }
    ngOnInit() {}
    sendNotification(): void {
        const url = 'http://localhost:9000/api/contact';
        this.http.post(url, this.model).subscribe(res => {
            location.reload();
        });
        this.router.navigate(['']);
    }
}
export interface UserModel {
    name: string;
    emailMsg: string;
    subject: string;
    message: string;
}
