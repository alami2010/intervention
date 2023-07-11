import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { FloorFormService } from './floor-form.service';
import { FloorService } from '../service/floor.service';
import { IFloor } from '../floor.model';
import { IIntervention } from 'app/entities/intervention/intervention.model';
import { InterventionService } from 'app/entities/intervention/service/intervention.service';

import { FloorUpdateComponent } from './floor-update.component';

describe('Floor Management Update Component', () => {
  let comp: FloorUpdateComponent;
  let fixture: ComponentFixture<FloorUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let floorFormService: FloorFormService;
  let floorService: FloorService;
  let interventionService: InterventionService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [FloorUpdateComponent],
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
      .overrideTemplate(FloorUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(FloorUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    floorFormService = TestBed.inject(FloorFormService);
    floorService = TestBed.inject(FloorService);
    interventionService = TestBed.inject(InterventionService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Intervention query and add missing value', () => {
      const floor: IFloor = { id: 456 };
      const intervention: IIntervention = { id: 92466 };
      floor.intervention = intervention;

      const interventionCollection: IIntervention[] = [{ id: 37743 }];
      jest.spyOn(interventionService, 'query').mockReturnValue(of(new HttpResponse({ body: interventionCollection })));
      const additionalInterventions = [intervention];
      const expectedCollection: IIntervention[] = [...additionalInterventions, ...interventionCollection];
      jest.spyOn(interventionService, 'addInterventionToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ floor });
      comp.ngOnInit();

      expect(interventionService.query).toHaveBeenCalled();
      expect(interventionService.addInterventionToCollectionIfMissing).toHaveBeenCalledWith(
        interventionCollection,
        ...additionalInterventions.map(expect.objectContaining)
      );
      expect(comp.interventionsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const floor: IFloor = { id: 456 };
      const intervention: IIntervention = { id: 93210 };
      floor.intervention = intervention;

      activatedRoute.data = of({ floor });
      comp.ngOnInit();

      expect(comp.interventionsSharedCollection).toContain(intervention);
      expect(comp.floor).toEqual(floor);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFloor>>();
      const floor = { id: 123 };
      jest.spyOn(floorFormService, 'getFloor').mockReturnValue(floor);
      jest.spyOn(floorService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ floor });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: floor }));
      saveSubject.complete();

      // THEN
      expect(floorFormService.getFloor).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(floorService.update).toHaveBeenCalledWith(expect.objectContaining(floor));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFloor>>();
      const floor = { id: 123 };
      jest.spyOn(floorFormService, 'getFloor').mockReturnValue({ id: null });
      jest.spyOn(floorService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ floor: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: floor }));
      saveSubject.complete();

      // THEN
      expect(floorFormService.getFloor).toHaveBeenCalled();
      expect(floorService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFloor>>();
      const floor = { id: 123 };
      jest.spyOn(floorService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ floor });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(floorService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareIntervention', () => {
      it('Should forward to interventionService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(interventionService, 'compareIntervention');
        comp.compareIntervention(entity, entity2);
        expect(interventionService.compareIntervention).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
