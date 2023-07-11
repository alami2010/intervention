import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { TierDetailComponent } from './tier-detail.component';

describe('Tier Management Detail Component', () => {
  let comp: TierDetailComponent;
  let fixture: ComponentFixture<TierDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [TierDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ tier: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(TierDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(TierDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load tier on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.tier).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
