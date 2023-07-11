import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IRoomData } from '../room-data.model';
import { RoomDataService } from '../service/room-data.service';

@Injectable({ providedIn: 'root' })
export class RoomDataRoutingResolveService implements Resolve<IRoomData | null> {
  constructor(protected service: RoomDataService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IRoomData | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((roomData: HttpResponse<IRoomData>) => {
          if (roomData.body) {
            return of(roomData.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(null);
  }
}
