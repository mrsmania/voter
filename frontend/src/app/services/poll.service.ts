import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class PollService {
  constructor(private http: HttpClient) {}

  // API-Aufruf, um den Poll anhand des Tokens abzurufen
  getPollByToken(token: string): Observable<any> {
    return this.http.get(`/api/polls/${token}`);
  }
}
