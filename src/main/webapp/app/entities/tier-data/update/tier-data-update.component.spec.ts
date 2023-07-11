import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { TierDataFormService } from './tier-data-form.service';
import { TierDataService } from '../service/tier-data.service';
import { ITierData } from '../tier-data.model';
import { IFloorData } from 'app/entities/floor-data/floor-data.model';
import { FloorDataService } from 'app/entities/floor-data/service/floor-data.service';

import { TierDataUpdateComponent } from './tier-data-update.component';

describe('TierData Management Update Component', () => {
  let comp: TierDataUpdateComponent;
  let fixture: ComponentFixture<TierDataUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let tierDataFormService: TierDataFormService;
  let tierDataService: TierDataService;
  let floorDataService: FloorDataService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [TierDataUpdateComponent],
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
      .overrideTemplate(TierDataUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(TierDataUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    tierDataFormService = TestBed.inject(TierDataFormService);
    tierDataService = TestBed.inject(TierDataService);
    floorDataService = TestBed.inject(FloorDataService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call FloorData query and add missing value', () => {
      const tierData: ITierData = { id: 456 };
      const floor: IFloorData = { id: 99972 };
      tierData.floor = floor;

      const floorDataCollection: IFloorData[] = [{ id: 46824 }];
      jest.spyOn(floorDataService, 'query').mockReturnValue(of(new HttpResponse({ body: floorDataCollection })));
      const additionalFloorData = [floor];
      const expectedCollection: IFloorData[] = [...additionalFloorData, ...floorDataCollection];
      jest.spyOn(floorDataService, 'addFloorDataToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ tierData });
      comp.ngOnInit();

      expect(floorDataService.query).toHaveBeenCalled();
      expect(floorDataService.addFloorDataToCollectionIfMissing).toHaveBeenCalledWith(
        floorDataCollection,
        ...additionalFloorData.map(expect.objectContaining)
      );
      expect(comp.floorDataSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const tierData: ITierData = { id: 456 };
      const floor: IFloorData = { id: 11560 };
      tierData.floor = floor;

      activatedRoute.data = of({ tierData });
      comp.ngOnInit();

      expect(comp.floorDataSharedCollection).toContain(floor);
      expect(comp.tierData).toEqual(tierData);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITierData>>();
      const tierData = { id: 123 };
      jest.spyOn(tierDataFormService, 'getTierData').mockReturnValue(tierData);
      jest.spyOn(tierDataService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ tierData });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: tierData }));
      saveSubject.complete();

      // THEN
      expect(tierDataFormService.getTierData).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(tierDataService.update).toHaveBeenCalledWith(expect.objectContaining(tierData));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITierData>>();
      const tierData = { id: 123 };
      jest.spyOn(tierDataFormService, 'getTierData').mockReturnValue({ id: null });
      jest.spyOn(tierDataService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ tierData: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: tierData }));
      saveSubject.complete();

      // THEN
      expect(tierDataFormService.getTierData).toHaveBeenCalled();
      expect(tierDataService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITierData>>();
      const tierData = { id: 123 };
      jest.spyOn(tierDataService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ tierData });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(tierDataService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareFloorData', () => {
      it('Should forward to floorDataService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(floorDataService, 'compareFloorData');
        comp.compareFloorData(entity, entity2);
        expect(floorDataService.compareFloorData).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
