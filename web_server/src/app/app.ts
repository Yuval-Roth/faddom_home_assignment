import { Component } from '@angular/core';
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
  startTime :Date | null = null;
  endTime:Date | null = null;
  instanceIp: string | null = null;
  intervalSeconds = 60;

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
    }

    console.log({
      start: this.startTime.toISOString(),
      end: this.endTime.toISOString(),
      ip: this.instanceIp,
      interval: this.intervalSeconds,
    });
  }
}
