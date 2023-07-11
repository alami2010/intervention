import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { FloorDataDetailComponent } from './floor-data-detail.component';

describe('FloorData Management Detail Component', () => {
  let comp: FloorDataDetailComponent;
  let fixture: ComponentFixture<FloorDataDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [FloorDataDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ floorData: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(FloorDataDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(FloorDataDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load floorData on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.floorData).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
