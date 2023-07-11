import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { RoomDataDetailComponent } from './room-data-detail.component';

describe('RoomData Management Detail Component', () => {
  let comp: RoomDataDetailComponent;
  let fixture: ComponentFixture<RoomDataDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [RoomDataDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ roomData: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(RoomDataDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(RoomDataDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load roomData on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.roomData).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
