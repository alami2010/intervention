import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { FloorDataFormService } from './floor-data-form.service';
import { FloorDataService } from '../service/floor-data.service';
import { IFloorData } from '../floor-data.model';

import { FloorDataUpdateComponent } from './floor-data-update.component';

describe('FloorData Management Update Component', () => {
  let comp: FloorDataUpdateComponent;
  let fixture: ComponentFixture<FloorDataUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let floorDataFormService: FloorDataFormService;
  let floorDataService: FloorDataService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [FloorDataUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(FloorDataUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(FloorDataUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    floorDataFormService = TestBed.inject(FloorDataFormService);
    floorDataService = TestBed.inject(FloorDataService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const floorData: IFloorData = { id: 456 };

      activatedRoute.data = of({ floorData });
      comp.ngOnInit();

      expect(comp.floorData).toEqual(floorData);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFloorData>>();
      const floorData = { id: 123 };
      jest.spyOn(floorDataFormService, 'getFloorData').mockReturnValue(floorData);
      jest.spyOn(floorDataService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ floorData });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: floorData }));
      saveSubject.complete();

      // THEN
      expect(floorDataFormService.getFloorData).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(floorDataService.update).toHaveBeenCalledWith(expect.objectContaining(floorData));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFloorData>>();
      const floorData = { id: 123 };
      jest.spyOn(floorDataFormService, 'getFloorData').mockReturnValue({ id: null });
      jest.spyOn(floorDataService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ floorData: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: floorData }));
      saveSubject.complete();

      // THEN
      expect(floorDataFormService.getFloorData).toHaveBeenCalled();
      expect(floorDataService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFloorData>>();
      const floorData = { id: 123 };
      jest.spyOn(floorDataService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ floorData });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(floorDataService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
