import { Injectable } from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class PollService {
  constructor(private http: HttpClient) {}

  getPollByToken(token: string): Observable<any> {
    return this.http.get(`${environment.apiUrl}/poll/${token}/get`);
  }


  submitUserEmail(hostUserEmail: string): Observable<any> {
    const params = new HttpParams().set('hostUserEmail', hostUserEmail);
    return this.http.post(`${environment.apiUrl}/poll/create`, params);
  }

  submitQuestion(pollData: any): Observable<any> {
    return this.http.post(`${environment.apiUrl}/poll/submitQuestion`, pollData);
  }
}
