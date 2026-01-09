import {Injectable, REQUEST} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';

export type response = {message: string, success: boolean, data: string[]}
export type DataPoint = { timestamp: string, average: number }

@Injectable({
  providedIn: 'root',
})
export class DataFetcher {

  constructor(private httpClient: HttpClient) {}

  fetchData(request: {startTime: string,endTime: string, instanceIp:string, sampleInterval:number}): Observable<response> {
    return this.httpClient.post<response>("http://localhost:8081/api/v1/cpu-usage-statistics", JSON.stringify(request))
  }
}
