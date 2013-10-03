/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.quick.ui.exam;

import com.quick.data.DataProvider;
import com.vaadin.addon.charts.Chart;
import com.vaadin.addon.charts.model.ChartType;
import com.vaadin.addon.charts.model.Configuration;
import com.vaadin.addon.charts.model.Cursor;
import com.vaadin.addon.charts.model.DataSeries;
import com.vaadin.addon.charts.model.DataSeriesItem;
import com.vaadin.addon.charts.model.Labels;
import com.vaadin.addon.charts.model.PlotOptionsPie;
import com.vaadin.addon.charts.model.style.SolidColor;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

/**
 *
 * @author rajkirans
 */
public class CustomPieChart extends Chart {
    
    public CustomPieChart(HashMap<String,Double> dataMap){
        // TODO this don't actually visualize top six theaters, but just makes a
        // pie chart
        super(ChartType.PIE);

//        setCaption("Students ");
//        getConfiguration().setTitle("");
//        getConfiguration().getChart().setType(ChartType.PIE);
//        setWidth("100%");
//        setHeight("90%");

        
        
        
        //Chart chart = new Chart(ChartType.PIE);

        Configuration conf = getConfiguration();

        conf.setTitle("Exam Summary");

        PlotOptionsPie plotOptions = new PlotOptionsPie();
        plotOptions.setCursor(Cursor.POINTER);
        
        Labels dataLabels = new Labels();
        dataLabels.setEnabled(true);
        dataLabels.setColor(new SolidColor(0, 0, 0));
        dataLabels.setConnectorColor(new SolidColor(0, 0, 0));
        dataLabels.setFormatter("''+ this.point.name +': '+ this.percentage +' %'");
        plotOptions.setDataLabels(dataLabels);
        conf.setPlotOptions(plotOptions);
        
        
        
        
        DataSeries series = new DataSeries();
        Set<String> keySet = dataMap.keySet();
        Iterator<String> it = keySet.iterator();
        int cnt = 0;
        while(it.hasNext()){
            String xAxis = it.next();
            double yAxis =  dataMap.get(xAxis);
          
            series.add(new DataSeriesItem(xAxis, yAxis));
        }
        
        conf.setSeries(series);
        drawChart(conf);
        
        setWidth("100%");
        setHeight("85%");
    }
    
    
    
     public static Chart createChart(HashMap<String,Double> dataMap, String selectedItem, String chartDescription) {
         
        Chart chart = new Chart(ChartType.PIE);
        chart.setDescription(chartDescription);
        Configuration conf = chart.getConfiguration();

        conf.setTitle(chartDescription);
        conf.setSubTitle("Last updated - "+new Date());

        PlotOptionsPie plotOptions = new PlotOptionsPie();
        plotOptions.setCursor(Cursor.POINTER);
        
        Labels dataLabels = new Labels();
        dataLabels.setEnabled(true);
        dataLabels.setColor(new SolidColor(0, 0, 0));
        dataLabels.setConnectorColor(new SolidColor(0, 0, 0));
        dataLabels.setFormatter("''+ this.point.name +': '+ this.percentage +' %'");
        plotOptions.setDataLabels(dataLabels);
        //plotOptions.setSize(4);
        conf.setPlotOptions(plotOptions);

        DataSeries series = new DataSeries();
        
        Set<String> keySet = dataMap.keySet();
        Iterator<String> it = keySet.iterator();
        while(it.hasNext())
        {
            String xAxis = it.next();
            double yAxis =  dataMap.get(xAxis);
             
                 if (!selectedItem.equals(xAxis)) 
                 {
                     series.add(new DataSeriesItem(xAxis, yAxis));
                 } 
                 else
                 {
                     DataSeriesItem slice = new DataSeriesItem(xAxis, yAxis);
                        slice.setSliced(true);
                        slice.setSelected(true);
                        series.add(slice);
                 }
        }
        
        conf.setSeries(series);
        chart.drawChart(conf);
        chart.setWidth("100%");
        chart.setHeight("100%");
        return chart;
    }
     
     public static Chart createChart() {
        Chart chart = new Chart(ChartType.PIE);

        Configuration conf = chart.getConfiguration();

        conf.setTitle("Browser market shares at a specific website, 2010");

        PlotOptionsPie plotOptions = new PlotOptionsPie();
        plotOptions.setCursor(Cursor.POINTER);
        Labels dataLabels = new Labels();
        dataLabels.setEnabled(true);
        dataLabels.setColor(new SolidColor(0, 0, 0));
        dataLabels.setConnectorColor(new SolidColor(0, 0, 0));
        dataLabels
                .setFormatter("''+ this.point.name +': '+ this.percentage +' %'");
        plotOptions.setDataLabels(dataLabels);
        conf.setPlotOptions(plotOptions);

        DataSeries series = new DataSeries();
        series.add(new DataSeriesItem("Firefox", 45));
        series.add(new DataSeriesItem("IE", 26));
        DataSeriesItem chrome = new DataSeriesItem("Chrome", 12);
        chrome.setSliced(true);
        chrome.setSelected(true);
        series.add(chrome);
        series.add(new DataSeriesItem("Safari", 8));
//        series.add(new DataSeriesItem("Opera", 6.2));
//        series.add(new DataSeriesItem("Others", 0.7));
        conf.setSeries(series);

        chart.drawChart(conf);

        return chart;
    }
}
