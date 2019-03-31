/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { RobcoTestModule } from '../../../test.module';
import { DevicesUpdateComponent } from 'app/entities/devices/devices-update.component';
import { DevicesService } from 'app/entities/devices/devices.service';
import { Devices } from 'app/shared/model/devices.model';

describe('Component Tests', () => {
    describe('Devices Management Update Component', () => {
        let comp: DevicesUpdateComponent;
        let fixture: ComponentFixture<DevicesUpdateComponent>;
        let service: DevicesService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [RobcoTestModule],
                declarations: [DevicesUpdateComponent]
            })
                .overrideTemplate(DevicesUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(DevicesUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(DevicesService);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity', fakeAsync(() => {
                // GIVEN
                const entity = new Devices(123);
                spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.devices = entity;
                // WHEN
                comp.save();
                tick(); // simulate async

                // THEN
                expect(service.update).toHaveBeenCalledWith(entity);
                expect(comp.isSaving).toEqual(false);
            }));

            it('Should call create service on save for new entity', fakeAsync(() => {
                // GIVEN
                const entity = new Devices();
                spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.devices = entity;
                // WHEN
                comp.save();
                tick(); // simulate async

                // THEN
                expect(service.create).toHaveBeenCalledWith(entity);
                expect(comp.isSaving).toEqual(false);
            }));
        });
    });
});
