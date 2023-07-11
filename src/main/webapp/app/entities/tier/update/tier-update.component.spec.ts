import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { TierFormService } from './tier-form.service';
import { TierService } from '../service/tier.service';
import { ITier } from '../tier.model';
import { IFloor } from 'app/entities/floor/floor.model';
import { FloorService } from 'app/entities/floor/service/floor.service';

import { TierUpdateComponent } from './tier-update.component';

describe('Tier Management Update Component', () => {
  let comp: TierUpdateComponent;
  let fixture: ComponentFixture<TierUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let tierFormService: TierFormService;
  let tierService: TierService;
  let floorService: FloorService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [TierUpdateComponent],
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
      .overrideTemplate(TierUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(TierUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    tierFormService = TestBed.inject(TierFormService);
    tierService = TestBed.inject(TierService);
    floorService = TestBed.inject(FloorService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Floor query and add missing value', () => {
      const tier: ITier = { id: 456 };
      const floor: IFloor = { id: 85394 };
      tier.floor = floor;

      const floorCollection: IFloor[] = [{ id: 6608 }];
      jest.spyOn(floorService, 'query').mockReturnValue(of(new HttpResponse({ body: floorCollection })));
      const additionalFloors = [floor];
      const expectedCollection: IFloor[] = [...additionalFloors, ...floorCollection];
      jest.spyOn(floorService, 'addFloorToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ tier });
      comp.ngOnInit();

      expect(floorService.query).toHaveBeenCalled();
      expect(floorService.addFloorToCollectionIfMissing).toHaveBeenCalledWith(
        floorCollection,
        ...additionalFloors.map(expect.objectContaining)
      );
      expect(comp.floorsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const tier: ITier = { id: 456 };
      const floor: IFloor = { id: 93537 };
      tier.floor = floor;

      activatedRoute.data = of({ tier });
      comp.ngOnInit();

      expect(comp.floorsSharedCollection).toContain(floor);
      expect(comp.tier).toEqual(tier);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITier>>();
      const tier = { id: 123 };
      jest.spyOn(tierFormService, 'getTier').mockReturnValue(tier);
      jest.spyOn(tierService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ tier });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: tier }));
      saveSubject.complete();

      // THEN
      expect(tierFormService.getTier).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(tierService.update).toHaveBeenCalledWith(expect.objectContaining(tier));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITier>>();
      const tier = { id: 123 };
      jest.spyOn(tierFormService, 'getTier').mockReturnValue({ id: null });
      jest.spyOn(tierService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ tier: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: tier }));
      saveSubject.complete();

      // THEN
      expect(tierFormService.getTier).toHaveBeenCalled();
      expect(tierService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITier>>();
      const tier = { id: 123 };
      jest.spyOn(tierService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ tier });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(tierService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareFloor', () => {
      it('Should forward to floorService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(floorService, 'compareFloor');
        comp.compareFloor(entity, entity2);
        expect(floorService.compareFloor).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
