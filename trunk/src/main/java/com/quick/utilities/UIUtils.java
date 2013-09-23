/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.quick.utilities;

import com.quick.global.GlobalConstants;
import com.quick.table.QuickUploadTable;
import com.vaadin.addon.charts.Chart;
import com.vaadin.addon.charts.model.ChartType;
import com.vaadin.addon.charts.model.Configuration;
import com.vaadin.addon.charts.model.HorizontalAlign;
import com.vaadin.addon.charts.model.Labels;
import com.vaadin.addon.charts.model.Legend;
import com.vaadin.addon.charts.model.ListSeries;
import com.vaadin.addon.charts.model.PlotOptionsColumn;
import com.vaadin.addon.charts.model.Tooltip;
import com.vaadin.addon.charts.model.XAxis;
import com.vaadin.addon.charts.model.YAxis;
import com.vaadin.addon.charts.model.style.SolidColor;
import com.vaadin.addon.charts.model.style.Style;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.VerticalLayout;

/**
 *
 * @author rajkiran
 */
public class UIUtils {
    public static CssLayout createPanel(Component content) {
        CssLayout panel = new CssLayout();
        panel.addStyleName("layout-panel");
        //panel.setSizeFull();
        panel.setWidth("100%");
        panel.setHeight("97%");
        panel.addComponent(content);
        return panel;
    }
    
    
    public static HorizontalLayout getVerticalPaneView(Component c1 ,Component c2){
        HorizontalLayout horizontalLayout =new HorizontalLayout();
        horizontalLayout.addComponent(c1);
        horizontalLayout.addComponent(c2);
        return horizontalLayout;
    }
    
    public static VerticalLayout getHorizontalPaneView(Component c1 ,Component c2){
        
        VerticalLayout verticalLayout =new VerticalLayout();
        verticalLayout.addComponent(c1);
        verticalLayout.addComponent(c2);
        return verticalLayout;
        
    }
    
    
    //method requires descripttion of the passed comeponents
     public static TabSheet getTabSheetPaneView(Component... c1){
        TabSheet tabSheet =new TabSheet();
       tabSheet.setSizeFull();
       
        for(Component arg: c1) {
            tabSheet.addTab(arg,arg.getDescription());
        }
       
        return tabSheet;
        
    }
     
     /** Used to show the list of uploaded items
     * called when admin user goes to upload topics menu
     */
    public static VerticalLayout buildVerticalLayoutForComponent(Component c) {
        VerticalLayout mainVertical = new VerticalLayout();
        //HorizontalLayout tableView = new HorizontalLayout();
        mainVertical.setSpacing(true);
        mainVertical.setWidth("100%");
        mainVertical.setHeight("97%");
        mainVertical.addComponent(c);
        //mainVertical.addComponent(tableView);
        return mainVertical;
    }
    
    public static Component getBarChart(String[] xAxisCategories,Number[] scores, String graphTitle, String xAxisTitle, String yAxisTitle) {
        VerticalLayout l = new VerticalLayout();
        Chart chart = new Chart(ChartType.COLUMN);

        Configuration conf = chart.getConfiguration();
        conf.getChart().setMargin(50, 80, 100, 50);

        conf.setTitle(graphTitle);

        XAxis xAxis = new XAxis();
        xAxis.setCategories(xAxisCategories);
        Labels labels = new Labels();
        labels.setRotation(-45);
        labels.setAlign(HorizontalAlign.RIGHT);
        Style style = new Style();
        style.setFontSize("13px");
        style.setFontFamily("Verdana, sans-serif");
        labels.setStyle(style);
        xAxis.setLabels(labels);
        conf.addxAxis(xAxis);

        YAxis yAxis = new YAxis();
        yAxis.setMin(0);
        yAxis.setTitle(yAxisTitle);
        conf.addyAxis(yAxis);

        Legend legend = new Legend();
        legend.setEnabled(false);
        conf.setLegend(legend);

        Tooltip tooltip = new Tooltip();
        tooltip.setFormatter(GlobalConstants.emptyString);
        conf.setTooltip(tooltip);

        ListSeries serie = new ListSeries(xAxisTitle, scores);
        Labels dataLabels = new Labels();
        dataLabels.setEnabled(true);
        dataLabels.setRotation(-90);
        dataLabels.setColor(new SolidColor(255, 255, 255));
        dataLabels.setAlign(HorizontalAlign.RIGHT);
        dataLabels.setX(4);
        dataLabels.setY(10);
        dataLabels.setFormatter("this.y");
        style = new Style();
        style.setFontSize("13px");
        style.setFontFamily("Verdana, sans-serif");
        dataLabels.setStyle(style);
        PlotOptionsColumn plotOptionsColumn = new PlotOptionsColumn();
        plotOptionsColumn.setDataLabels(dataLabels);
        serie.setPlotOptions(plotOptionsColumn);
        conf.addSeries(serie);

        chart.setWidth("300px");
        chart.setHeight("260px");
        
        chart.drawChart(conf);
        l.addComponent(chart);
        l.setSizeFull();
        return l;
    }
    
}
