import {Component, ViewChild} from '@angular/core';
import {MatFormField, MatInput, MatInputModule, MatLabel} from '@angular/material/input';
import {
  MatDatepicker,
  MatDatepickerInput,
  MatDatepickerModule,
  MatDatepickerToggle
} from '@angular/material/datepicker';
import {MatButton} from '@angular/material/button';
import {DataChartComponent} from './data-chart/data-chart';
import {MatFormFieldModule} from '@angular/material/form-field';
import {FormsModule} from '@angular/forms';
import {MatTimepicker, MatTimepickerInput, MatTimepickerToggle} from '@angular/material/timepicker';
import {DataFetcher, DataPoint} from './services/data-fetcher';

const ipv4_regex_pattern = RegExp("^(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)(\\.(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)){3}$");

@Component({
  selector: 'app-root',
  imports: [
    MatFormField,
    MatLabel,
    MatDatepickerInput,
    MatDatepicker,
    MatDatepickerToggle,
    MatFormFieldModule,
    MatInputModule,
    MatDatepickerModule,
    MatInput,
    MatButton,
    DataChartComponent,
    FormsModule,
    MatTimepickerInput,
    MatTimepicker,
    MatTimepickerToggle
  ],
  templateUrl: './app.html',
  styleUrl: './app.scss'
})
export class App {
  @ViewChild(DataChartComponent) chart!: DataChartComponent;

  startTime :Date | null = null;
  endTime:Date | null = null;
  instanceIp: string | null = null;
  intervalSeconds = 60;

  constructor(private dataFetcher: DataFetcher) {}

  loadData() {
    if(this.startTime == null || this.endTime == null || this.instanceIp == null) {
      alert("Please fill all the fields")
      return;
    }

    if(this.startTime >= this.endTime) {
      alert("Start time has to be before end time")
      return;
    }

    if(! ipv4_regex_pattern.test(this.instanceIp)){
      alert("Invalid IPv4 entered for instance IP")
      return;
    }
    const request = {
      startTime: this.startTime.toISOString().slice(0,-1),
      endTime: this.endTime.toISOString().slice(0,-1),
      instanceIp: this.instanceIp,
      sampleInterval: this.intervalSeconds,
    }
    this.dataFetcher.fetchData(request).subscribe({
      next: response => {
        if(response.success){
          const dataPoints = response.data.map(data => JSON.parse(data)) as DataPoint[]
          const chartPoints = dataPoints.map(dataPoint => ({ x:dataPoint.timestamp, y:dataPoint.average }));
          console.log(chartPoints)
          this.chart.updateChart(chartPoints);
        } else {
          alert("Error occurred while fetching data: \n\n"+response.message)
        }
      },

      error: error => {
        alert("Request failed: "+error);
      }
    })
  }
}
