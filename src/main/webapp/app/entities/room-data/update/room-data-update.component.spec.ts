import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { RoomDataFormService } from './room-data-form.service';
import { RoomDataService } from '../service/room-data.service';
import { IRoomData } from '../room-data.model';
import { ITierData } from 'app/entities/tier-data/tier-data.model';
import { TierDataService } from 'app/entities/tier-data/service/tier-data.service';

import { RoomDataUpdateComponent } from './room-data-update.component';

describe('RoomData Management Update Component', () => {
  let comp: RoomDataUpdateComponent;
  let fixture: ComponentFixture<RoomDataUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let roomDataFormService: RoomDataFormService;
  let roomDataService: RoomDataService;
  let tierDataService: TierDataService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [RoomDataUpdateComponent],
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
      .overrideTemplate(RoomDataUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(RoomDataUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    roomDataFormService = TestBed.inject(RoomDataFormService);
    roomDataService = TestBed.inject(RoomDataService);
    tierDataService = TestBed.inject(TierDataService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call TierData query and add missing value', () => {
      const roomData: IRoomData = { id: 456 };
      const tier: ITierData = { id: 65520 };
      roomData.tier = tier;

      const tierDataCollection: ITierData[] = [{ id: 44773 }];
      jest.spyOn(tierDataService, 'query').mockReturnValue(of(new HttpResponse({ body: tierDataCollection })));
      const additionalTierData = [tier];
      const expectedCollection: ITierData[] = [...additionalTierData, ...tierDataCollection];
      jest.spyOn(tierDataService, 'addTierDataToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ roomData });
      comp.ngOnInit();

      expect(tierDataService.query).toHaveBeenCalled();
      expect(tierDataService.addTierDataToCollectionIfMissing).toHaveBeenCalledWith(
        tierDataCollection,
        ...additionalTierData.map(expect.objectContaining)
      );
      expect(comp.tierDataSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const roomData: IRoomData = { id: 456 };
      const tier: ITierData = { id: 55011 };
      roomData.tier = tier;

      activatedRoute.data = of({ roomData });
      comp.ngOnInit();

      expect(comp.tierDataSharedCollection).toContain(tier);
      expect(comp.roomData).toEqual(roomData);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IRoomData>>();
      const roomData = { id: 123 };
      jest.spyOn(roomDataFormService, 'getRoomData').mockReturnValue(roomData);
      jest.spyOn(roomDataService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ roomData });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: roomData }));
      saveSubject.complete();

      // THEN
      expect(roomDataFormService.getRoomData).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(roomDataService.update).toHaveBeenCalledWith(expect.objectContaining(roomData));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IRoomData>>();
      const roomData = { id: 123 };
      jest.spyOn(roomDataFormService, 'getRoomData').mockReturnValue({ id: null });
      jest.spyOn(roomDataService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ roomData: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: roomData }));
      saveSubject.complete();

      // THEN
      expect(roomDataFormService.getRoomData).toHaveBeenCalled();
      expect(roomDataService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IRoomData>>();
      const roomData = { id: 123 };
      jest.spyOn(roomDataService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ roomData });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(roomDataService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareTierData', () => {
      it('Should forward to tierDataService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(tierDataService, 'compareTierData');
        comp.compareTierData(entity, entity2);
        expect(tierDataService.compareTierData).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
