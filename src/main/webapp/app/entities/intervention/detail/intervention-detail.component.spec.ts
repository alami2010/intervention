import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { InterventionDetailComponent } from './intervention-detail.component';

describe('Intervention Management Detail Component', () => {
  let comp: InterventionDetailComponent;
  let fixture: ComponentFixture<InterventionDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [InterventionDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ intervention: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(InterventionDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(InterventionDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load intervention on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.intervention).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
