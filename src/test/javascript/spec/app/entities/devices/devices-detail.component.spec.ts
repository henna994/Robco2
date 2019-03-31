/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { RobcoTestModule } from '../../../test.module';
import { DevicesDetailComponent } from 'app/entities/devices/devices-detail.component';
import { Devices } from 'app/shared/model/devices.model';

describe('Component Tests', () => {
    describe('Devices Management Detail Component', () => {
        let comp: DevicesDetailComponent;
        let fixture: ComponentFixture<DevicesDetailComponent>;
        const route = ({ data: of({ devices: new Devices(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [RobcoTestModule],
                declarations: [DevicesDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(DevicesDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(DevicesDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.devices).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
