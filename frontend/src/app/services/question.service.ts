import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class QuestionService {
  constructor(private http: HttpClient) {}

  uploadQuestions(fileData: FormData): Observable<any> {
    return this.http.post(`${environment.apiUrl}/question/upload-questions`, fileData);
  }
}
