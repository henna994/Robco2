/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Observable, of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { RobcoTestModule } from '../../../test.module';
import { DevicesComponent } from 'app/entities/devices/devices.component';
import { DevicesService } from 'app/entities/devices/devices.service';
import { Devices } from 'app/shared/model/devices.model';

describe('Component Tests', () => {
    describe('Devices Management Component', () => {
        let comp: DevicesComponent;
        let fixture: ComponentFixture<DevicesComponent>;
        let service: DevicesService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [RobcoTestModule],
                declarations: [DevicesComponent],
                providers: []
            })
                .overrideTemplate(DevicesComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(DevicesComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(DevicesService);
        });

        it('Should call load all on init', () => {
            // GIVEN
            const headers = new HttpHeaders().append('link', 'link;link');
            spyOn(service, 'query').and.returnValue(
                of(
                    new HttpResponse({
                        body: [new Devices(123)],
                        headers
                    })
                )
            );

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.query).toHaveBeenCalled();
            expect(comp.devices[0]).toEqual(jasmine.objectContaining({ id: 123 }));
        });
    });
});
