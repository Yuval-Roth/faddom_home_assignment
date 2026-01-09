import { Component, signal } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import {DataChartComponent} from './data-chart/data-chart';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, DataChartComponent],
  templateUrl: './app.html',
  styleUrl: './app.scss'
})
export class App {
  protected readonly title = signal('web_server');
}
