/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { RobcoTestModule } from '../../../test.module';
import { DevicesDeleteDialogComponent } from 'app/entities/devices/devices-delete-dialog.component';
import { DevicesService } from 'app/entities/devices/devices.service';

describe('Component Tests', () => {
    describe('Devices Management Delete Component', () => {
        let comp: DevicesDeleteDialogComponent;
        let fixture: ComponentFixture<DevicesDeleteDialogComponent>;
        let service: DevicesService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [RobcoTestModule],
                declarations: [DevicesDeleteDialogComponent]
            })
                .overrideTemplate(DevicesDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(DevicesDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(DevicesService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('confirmDelete', () => {
            it('Should call delete service on confirmDelete', inject(
                [],
                fakeAsync(() => {
                    // GIVEN
                    spyOn(service, 'delete').and.returnValue(of({}));

                    // WHEN
                    comp.confirmDelete(123);
                    tick();

                    // THEN
                    expect(service.delete).toHaveBeenCalledWith(123);
                    expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
                })
            ));
        });
    });
});
