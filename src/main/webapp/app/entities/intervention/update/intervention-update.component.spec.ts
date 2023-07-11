import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { InterventionFormService } from './intervention-form.service';
import { InterventionService } from '../service/intervention.service';
import { IIntervention } from '../intervention.model';

import { InterventionUpdateComponent } from './intervention-update.component';

describe('Intervention Management Update Component', () => {
  let comp: InterventionUpdateComponent;
  let fixture: ComponentFixture<InterventionUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let interventionFormService: InterventionFormService;
  let interventionService: InterventionService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [InterventionUpdateComponent],
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
      .overrideTemplate(InterventionUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(InterventionUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    interventionFormService = TestBed.inject(InterventionFormService);
    interventionService = TestBed.inject(InterventionService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const intervention: IIntervention = { id: 456 };

      activatedRoute.data = of({ intervention });
      comp.ngOnInit();

      expect(comp.intervention).toEqual(intervention);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IIntervention>>();
      const intervention = { id: 123 };
      jest.spyOn(interventionFormService, 'getIntervention').mockReturnValue(intervention);
      jest.spyOn(interventionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ intervention });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: intervention }));
      saveSubject.complete();

      // THEN
      expect(interventionFormService.getIntervention).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(interventionService.update).toHaveBeenCalledWith(expect.objectContaining(intervention));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IIntervention>>();
      const intervention = { id: 123 };
      jest.spyOn(interventionFormService, 'getIntervention').mockReturnValue({ id: null });
      jest.spyOn(interventionService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ intervention: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: intervention }));
      saveSubject.complete();

      // THEN
      expect(interventionFormService.getIntervention).toHaveBeenCalled();
      expect(interventionService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IIntervention>>();
      const intervention = { id: 123 };
      jest.spyOn(interventionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ intervention });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(interventionService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
