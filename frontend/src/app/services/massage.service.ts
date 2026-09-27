import { Injectable } from '@angular/core';
import {environment} from '../../environments/environment';
import { HttpClient } from '@angular/common/http';
import {Observable} from 'rxjs';
import { Massage } from '../models/massage.model';

@Injectable({
  providedIn: 'root',
})
export class MassageService {
  private readonly apiUrl = `${environment.apiUrl}/massages`;

  constructor(private readonly http: HttpClient) {}


  getMassages(): Observable<Massage[]> {
    return this.http.get<Massage[]>(this.apiUrl);
  }

}
