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
    return this.http.get(`${environment.apiUrl}/poll/${token}`);
  }

  submitUserEmail(hostUserEmail: string): Observable<any> {
    const params = new HttpParams().set('hostUserEmail', hostUserEmail);
    return this.http.post(`${environment.apiUrl}/poll/create`, params);
  }

  savePoll(pollData: any): Observable<any> {
    return this.http.post(`${environment.apiUrl}/poll/save`, pollData);
  }

  getPollByTokenPasswordEmail(token: string, password: string, email: string): Observable<any> {
    return this.http.get(`${environment.apiUrl}/poll`, { params: { token, password, email } });
  }

  vote(userEmail: string, optionId: number): Observable<any> {
    return this.http.post(`${environment.apiUrl}/vote`, null, { params: {userEmail: userEmail, optionId: optionId.toString()}});
  }

  getVotesByEmailAndPollId(userEmail: string, pollId: number): Observable<any> {
    return this.http.get(`${environment.apiUrl}/vote/votes`, { params: {userEmail, pollId: pollId.toString()}});
  }
}
