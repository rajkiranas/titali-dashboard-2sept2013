/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.quick.utilities;

import com.vaadin.addon.charts.Chart;
import com.vaadin.addon.charts.model.Axis;
import com.vaadin.addon.charts.model.AxisType;
import com.vaadin.addon.charts.model.ChartType;
import com.vaadin.addon.charts.model.Configuration;
import com.vaadin.addon.charts.model.Cursor;
import com.vaadin.addon.charts.model.DataSeries;
import com.vaadin.addon.charts.model.DataSeriesItem;
import com.vaadin.addon.charts.model.HorizontalAlign;
import com.vaadin.addon.charts.model.Labels;
import com.vaadin.addon.charts.model.LayoutDirection;
import com.vaadin.addon.charts.model.Legend;
import com.vaadin.addon.charts.model.ListSeries;
import com.vaadin.addon.charts.model.Marker;
import com.vaadin.addon.charts.model.MarkerStates;
import com.vaadin.addon.charts.model.MarkerSymbolEnum;
import com.vaadin.addon.charts.model.PlotBand;
import com.vaadin.addon.charts.model.PlotBandLabel;
import com.vaadin.addon.charts.model.PlotOptionsColumn;
import com.vaadin.addon.charts.model.PlotOptionsPie;
import com.vaadin.addon.charts.model.PlotOptionsSpline;
import com.vaadin.addon.charts.model.State;
import com.vaadin.addon.charts.model.Tooltip;
import com.vaadin.addon.charts.model.VerticalAlign;
import com.vaadin.addon.charts.model.XAxis;
import com.vaadin.addon.charts.model.YAxis;
import com.vaadin.addon.charts.model.style.SolidColor;
import com.vaadin.addon.charts.model.style.Style;
import com.vaadin.annotations.Title;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.VerticalLayout;
import java.util.Date;

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
    
    public static Component getBarChart(String[] xAxisCategories,Number[] scores, String graphTitle, String xAxisTitle, String yAxisTitle, String height, String width) {
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
        tooltip.setFormatter("this.x +': '+ this.y +' marks'");
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

        chart.setWidth(width);
        chart.setHeight(height);
        
        chart.drawChart(conf);
        l.addComponent(chart);
        l.setSizeFull();
        return l;
    }
    
    public static Component getColumnChart(String[] xAxisCategories,Number[] classAvgScore,Number[] studAvgScore, String graphTitle, String xAxisTitle, String yAxisTitle, String height, String width) 
    {
        VerticalLayout l = new VerticalLayout();
        Chart chart = new Chart(ChartType.COLUMN);

        Configuration conf = chart.getConfiguration();

        conf.setTitle(graphTitle);
        //conf.setSubTitle("Source: WorldClimate.com");

        XAxis x = new XAxis();
        x.setCategories(xAxisCategories);
        conf.addxAxis(x);

        YAxis y = new YAxis();
        y.setMin(0);
        y.setTitle(yAxisTitle);
        conf.addyAxis(y);

        Legend legend = new Legend();
        legend.setLayout(LayoutDirection.VERTICAL);
        legend.setBackgroundColor("#FFFFFF");
        legend.setHorizontalAlign(HorizontalAlign.LEFT);
        legend.setVerticalAlign(VerticalAlign.TOP);
        //legend.setX(275);
        legend.setX(25);
        legend.setY(20);
        legend.setFloating(true);
        legend.setShadow(true);
        conf.setLegend(legend);

        Tooltip tooltip = new Tooltip();
         tooltip.setFormatter("this.x +': '+ this.y +' marks'");
        conf.setTooltip(tooltip);

        PlotOptionsColumn plot = new PlotOptionsColumn();
        plot.setPointPadding(0.2);
        plot.setBorderWidth(0);

        conf.addSeries(new ListSeries("My",studAvgScore));
        conf.addSeries(new ListSeries("Avg", classAvgScore));

        chart.drawChart(conf);
        
        chart.setHeight(height);
        chart.setWidth(width);
        
        l.addComponent(chart);
        l.setSizeFull();
        return l;
    }
    
    public static Component getTeacherPerformanceChart() {
        Chart chart = new Chart(ChartType.COLUMN);

        Configuration conf = chart.getConfiguration();

        conf.setTitle("My class(es) performance");
        //conf.setSubTitle("Source: WorldClimate.com");

        XAxis x = new XAxis();
        x.setCategories("Performance");
        conf.addxAxis(x);

        YAxis y = new YAxis();
        y.setMin(0);
        y.setTitle("Score");
        conf.addyAxis(y);

        Legend legend = new Legend();
        legend.setLayout(LayoutDirection.VERTICAL);
        legend.setBackgroundColor("#FFFFFF");
        legend.setHorizontalAlign(HorizontalAlign.LEFT);
        legend.setVerticalAlign(VerticalAlign.TOP);
        legend.setX(300);
        legend.setY(10);
        legend.setFloating(true);
        legend.setShadow(true);
        conf.setLegend(legend);

        Tooltip tooltip = new Tooltip();
        tooltip.setFormatter("this.x +': '+ this.y +' mm'");
        conf.setTooltip(tooltip);

        PlotOptionsColumn plot = new PlotOptionsColumn();
        plot.setPointPadding(0.2);
        plot.setBorderWidth(0);

        conf.addSeries(new ListSeries("My", 75.9));
        conf.addSeries(new ListSeries("Avg", 83.6));
        conf.addSeries(new ListSeries("Low", 48.9));
        conf.addSeries(new ListSeries("Top", 90.1));
        
        
        chart.setSizeFull();
        chart.drawChart(conf);
        return chart;
    }
    
    public static Chart getMyAttendancePeiChart() {
        Chart chart = new Chart(ChartType.PIE);

        Configuration conf = chart.getConfiguration();

        conf.setTitle("My attendance, 2013");

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
        series.add(new DataSeriesItem("Present", 60.0));
        series.add(new DataSeriesItem("Absent", 25.5));
        DataSeriesItem chrome = new DataSeriesItem("Late", 14.5);
        chrome.setSliced(true);
        chrome.setSelected(true);
        series.add(chrome);

        conf.setSeries(series);

        chart.setSizeFull();
        chart.drawChart(conf);

        return chart;
    }
    
    public static Chart getMyActivityLogsPeiChart() {
        Chart chart = new Chart(ChartType.PIE);

        Configuration conf = chart.getConfiguration();

        conf.setTitle("My activities, 2013");

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
        series.add(new DataSeriesItem("Notes", 20.0));
        series.add(new DataSeriesItem("Videos", 20.5));
        DataSeriesItem chrome = new DataSeriesItem("Previous questions", 9.5);
        chrome.setSliced(true);
        chrome.setSelected(true);
        series.add(chrome);
        series.add(new DataSeriesItem("Technologies", 25));
        series.add(new DataSeriesItem("Exams", 25));
        

        conf.setSeries(series);

        chart.setSizeFull();
        chart.drawChart(conf);

        return chart;
    }
    
    private static final int ONE_HOUR = 60 * 60 * 1000;
    private static final SolidColor TRANSPARENT = new SolidColor(0, 0, 0, 0);
    private static final SolidColor LIGHT_BLUE = new SolidColor(68, 170, 213,
            0.1);
    private static final SolidColor LIGHT_GRAY = new SolidColor("#606060");
    
    private static PlotBand[] plotBands;
    public static Component getSubwiseProgressChart() {
            

        final Chart chart = new Chart();
        chart.setSizeFull();

        final Configuration configuration = new Configuration();
        configuration.getChart().setType(ChartType.SPLINE);

        configuration.getTitle().setText("Subject wise growth");
//        configuration
//                .getSubTitle()
//                .setText(
//                        "October 6th and 7th 2009 at two locations in Vik i Sogn, Norway");

        configuration.getxAxis().setType(AxisType.DATETIME);

        Axis yAxis = configuration.getyAxis();
        yAxis.setTitle("Progress");
        yAxis.setMin(0);
        yAxis.setMinorGridLineWidth(0);
        yAxis.setGridLineWidth(0);

        // disable alternate grid color from Vaadin theme, disturbs
        // demonstrating plotbands
        yAxis.setAlternateGridColor(TRANSPARENT);

        plotBands = createPlotBands(yAxis);

        configuration
                .getTooltip()
                .setFormatter(
                        "Highcharts.dateFormat('%e. %b %Y, %H:00', this.x) +': '+ this.y +' m/s'");

        PlotOptionsSpline plotOptions = new PlotOptionsSpline();
        configuration.setPlotOptions(plotOptions);
        plotOptions.setMarker(new Marker(false));
        plotOptions.getMarker().setLineWidth(4);
        MarkerStates states = new MarkerStates(new State(true));
        states.getHover().setSymbol(MarkerSymbolEnum.CIRCLE);
        states.getHover().setRadius(5);
        states.getHover().setLineWidth(1);
        plotOptions.getMarker().setStates(states);

        plotOptions.setPointInterval(ONE_HOUR);
        plotOptions.setPointStart(new Date(2009 - 1900, 9 - 1, 6).getTime());

        ListSeries ls = new ListSeries();
        ls.setName("English");
        ls.setData(4.3, 5.1, 4.3, 5.2, 5.4, 4.7, 3.5, 4.1, 5.6, 7.4, 6.9, 7.1,
                7.9, 7.9, 7.5, 6.7, 7.7, 7.7, 7.4, 7.0, 7.1, 5.8, 5.9, 7.4,
                8.2, 8.5, 9.4, 8.1, 10.9, 10.4, 10.9, 12.4, 12.1, 9.5, 7.5,
                7.1, 7.5, 8.1, 6.8, 3.4, 2.1, 1.9, 2.8, 2.9, 1.3, 4.4, 4.2,
                3.0, 3.0);
        configuration.addSeries(ls);

        ls = new ListSeries();
        ls.setName("Maths");
        ls.setData(4.3, 5.1, 4.3, 5.2, 5.4, 4.7, 3.5, 4.1, 5.6, 7.4, 6.9, 7.1,
                0.0, 0.4, 0.0, 0.1, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
                0.0, 0.6, 1.2, 1.7, 0.7, 2.9, 4.1, 2.6, 3.7, 3.9, 1.7, 2.3,
                3.0, 3.3, 4.8, 5.0, 4.8, 5.0, 3.2, 2.0, 0.9, 0.4, 0.3, 0.5, 0.4);
        configuration.addSeries(ls);
        
        
        ls = new ListSeries();
        ls.setName("CS");
        ls.setData(0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.1, 0.0, 0.3, 0.0,
                 7.9, 7.9, 7.5, 6.7, 7.7, 7.7, 7.4, 7.0, 7.1, 5.8, 5.9, 7.4,
                0.0, 0.6, 1.2, 1.7, 0.7, 2.9, 4.1, 2.6, 3.7, 3.9, 1.7, 2.3,
                3.0, 3.3, 4.8, 5.0, 4.8, 5.0, 3.2, 2.0, 0.9, 0.4, 0.3, 0.5, 0.4);
        configuration.addSeries(ls);

        chart.drawChart(configuration);

//        final Button removePlotBand = new Button("Remove PlotBands");
//        removePlotBand.setId("vaadin-button");
//        removePlotBand.addClickListener(new Button.ClickListener() {
//
//            @Override
//            public void buttonClick(ClickEvent event) {
//                if (chart.getConfiguration().getyAxis().getPlotBands()
//                        .isEmpty()) {
//                    plotBands = createPlotBands(chart.getConfiguration()
//                            .getyAxis());
//                    removePlotBand.setCaption("Remove PlotBands");
//                } else {
//                    for (int i = 0; i < plotBands.length; i++) {
//                        chart.getConfiguration().getyAxis()
//                                .removePlotBand(plotBands[i]);
//                    }
//
//                    removePlotBand.setCaption("Restore PlotBands");
//                }
//                chart.drawChart(configuration);
//            }
//        });

        return chart;
    }
    
    public static Component getTeacherSubClassWiseChart() {
            

        final Chart chart = new Chart();
        chart.setSizeFull();

        final Configuration configuration = new Configuration();
        configuration.getChart().setType(ChartType.SPLINE);

        configuration.getTitle().setText("Class wise growth");
//        configuration
//                .getSubTitle()
//                .setText(
//                        "October 6th and 7th 2009 at two locations in Vik i Sogn, Norway");

        configuration.getxAxis().setType(AxisType.DATETIME);

        Axis yAxis = configuration.getyAxis();
        yAxis.setTitle("Progress");
        yAxis.setMin(0);
        yAxis.setMinorGridLineWidth(0);
        yAxis.setGridLineWidth(0);

        // disable alternate grid color from Vaadin theme, disturbs
        // demonstrating plotbands
        yAxis.setAlternateGridColor(TRANSPARENT);

        plotBands = createPlotBands(yAxis);

        configuration
                .getTooltip()
                .setFormatter(
                        "Highcharts.dateFormat('%e. %b %Y, %H:00', this.x) +': '+ this.y +' m/s'");

        PlotOptionsSpline plotOptions = new PlotOptionsSpline();
        configuration.setPlotOptions(plotOptions);
        plotOptions.setMarker(new Marker(false));
        plotOptions.getMarker().setLineWidth(4);
        MarkerStates states = new MarkerStates(new State(true));
        states.getHover().setSymbol(MarkerSymbolEnum.CIRCLE);
        states.getHover().setRadius(5);
        states.getHover().setLineWidth(1);
        plotOptions.getMarker().setStates(states);

        plotOptions.setPointInterval(ONE_HOUR);
        plotOptions.setPointStart(new Date(2009 - 1900, 9 - 1, 6).getTime());

        ListSeries ls = new ListSeries();
        ls.setName("MCA-I");
        ls.setData(4.3, 5.1, 4.3, 5.2, 5.4, 4.7, 3.5, 4.1, 5.6, 7.4, 6.9, 7.1,
                7.9, 7.9, 7.5, 6.7, 7.7, 7.7, 7.4, 7.0, 7.1, 5.8, 5.9, 7.4,
                8.2, 8.5, 9.4, 8.1, 10.9, 10.4, 10.9, 12.4, 12.1, 9.5, 7.5,
                7.1, 7.5, 8.1, 6.8, 3.4, 2.1, 1.9, 2.8, 2.9, 1.3, 4.4, 4.2,
                3.0, 3.0);
        configuration.addSeries(ls);

        ls = new ListSeries();
        ls.setName("MCA-II");
        ls.setData(4.3, 5.1, 4.3, 5.2, 5.4, 4.7, 3.5, 4.1, 5.6, 7.4, 6.9, 7.1,
                0.0, 0.4, 0.0, 0.1, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
                0.0, 0.6, 1.2, 1.7, 0.7, 2.9, 4.1, 2.6, 3.7, 3.9, 1.7, 2.3,
                3.0, 3.3, 4.8, 5.0, 4.8, 5.0, 3.2, 2.0, 0.9, 0.4, 0.3, 0.5, 0.4);
        configuration.addSeries(ls);
        
        
        ls = new ListSeries();
        ls.setName("MCA-III");
        ls.setData(0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.1, 0.0, 0.3, 0.0,
                 7.9, 7.9, 7.5, 6.7, 7.7, 7.7, 7.4, 7.0, 7.1, 5.8, 5.9, 7.4,
                0.0, 0.6, 1.2, 1.7, 0.7, 2.9, 4.1, 2.6, 3.7, 3.9, 1.7, 2.3,
                3.0, 3.3, 4.8, 5.0, 4.8, 5.0, 3.2, 2.0, 0.9, 0.4, 0.3, 0.5, 0.4);
        configuration.addSeries(ls);

        chart.drawChart(configuration);

//        final Button removePlotBand = new Button("Remove PlotBands");
//        removePlotBand.setId("vaadin-button");
//        removePlotBand.addClickListener(new Button.ClickListener() {
//
//            @Override
//            public void buttonClick(ClickEvent event) {
//                if (chart.getConfiguration().getyAxis().getPlotBands()
//                        .isEmpty()) {
//                    plotBands = createPlotBands(chart.getConfiguration()
//                            .getyAxis());
//                    removePlotBand.setCaption("Remove PlotBands");
//                } else {
//                    for (int i = 0; i < plotBands.length; i++) {
//                        chart.getConfiguration().getyAxis()
//                                .removePlotBand(plotBands[i]);
//                    }
//
//                    removePlotBand.setCaption("Restore PlotBands");
//                }
//                chart.drawChart(configuration);
//            }
//        });

        return chart;
    }
    
    private static PlotBand[] createPlotBands(Axis yAxis) {
        

        

        final PlotBand freshBreeze = new PlotBand(0, 5, LIGHT_BLUE);
        freshBreeze.setLabel(new PlotBandLabel("Average"));
        freshBreeze.getLabel().setStyle(new Style());
        freshBreeze.getLabel().getStyle().setColor(LIGHT_GRAY);

        final PlotBand strongBreeze = new PlotBand(5, 10, TRANSPARENT);
        strongBreeze.setLabel(new PlotBandLabel("Good"));
        strongBreeze.getLabel().setStyle(new Style());
        strongBreeze.getLabel().getStyle().setColor(LIGHT_GRAY);

        final PlotBand highWind = new PlotBand(10, 15, LIGHT_BLUE);
        highWind.setLabel(new PlotBandLabel("Excellent"));
        highWind.getLabel().setStyle(new Style());
        highWind.getLabel().getStyle().setColor(LIGHT_GRAY);

        yAxis.setPlotBands(
                freshBreeze, strongBreeze, highWind);

        return new PlotBand[] { freshBreeze, strongBreeze, highWind };
    }
    
}
