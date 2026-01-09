import {Component, AfterViewInit, ViewChild, ElementRef, signal} from '@angular/core';
import { Chart, registerables } from 'chart.js';

Chart.register(...registerables);

@Component({
  selector: 'app-data-chart',
  templateUrl: './data-chart.html',
  styleUrl: './data-chart.scss',
})
export class DataChartComponent implements AfterViewInit {

  @ViewChild('chart') canvas!: ElementRef<HTMLCanvasElement>;
  hasData = signal(false);
  chart!: Chart<any, { x: string; y: number }[], any>;

  ngAfterViewInit(): void {
    this.buildEmptyChart();
  }

  updateChart(newData: {x: string,y: number}[]){
    this.hasData.set(newData.length > 0)
    this.chart.data.datasets[0].data = newData;
    this.chart.update("none");
  }

  private buildEmptyChart(){
    this.chart = new Chart(this.canvas.nativeElement, {
      type: 'line',
      data: {
        datasets: [
          {
            label: 'CPU Usage (%)',
            data: [],
            borderWidth: 1,
          },
        ],
      },
      options: {
        responsive: true,
        maintainAspectRatio: false,
        scales: {
          x: {
            type: 'time',
            time: {
              unit: 'day',
              displayFormats: {
                day: 'MMM d, yyyy',
              },
              tooltipFormat: 'MMM d, yyyy HH:mm',
            },
            ticks: {
              autoSkip: true,
              maxTicksLimit: 8,
            },
          },
        },
      },
    });
  }
}
