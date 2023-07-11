import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { TierDataDetailComponent } from './tier-data-detail.component';

describe('TierData Management Detail Component', () => {
  let comp: TierDataDetailComponent;
  let fixture: ComponentFixture<TierDataDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [TierDataDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ tierData: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(TierDataDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(TierDataDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load tierData on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.tierData).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
