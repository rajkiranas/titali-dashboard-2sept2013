/**
 * DISCLAIMER
 * 
 * The quality of the code is such that you should not copy any of it as best
 * practice how to build Vaadin applications.
 * 
 * @author jouni@vaadin.com
 * 
 */

package com.quick.play;

import com.quick.games.*;
import com.quick.global.GlobalConstants;
import com.vaadin.event.LayoutEvents;
import com.vaadin.event.LayoutEvents.LayoutClickEvent;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FileResource;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.Flash;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;
import java.io.File;

public class PlayView extends VerticalLayout implements View, LayoutEvents.LayoutClickListener {

    @Override
    public void enter(ViewChangeEvent event) {

        addStyleName("schedule");
        setSizeFull();
        buildHeaderView();
        buildGameListing();
        //buildBodyView();
    }

    private void buildHeaderView() {
        HorizontalLayout top = new HorizontalLayout();
        top.setWidth("100%");
        top.setSpacing(true);
        top.setMargin(true);
        top.addStyleName(GlobalConstants.toolbar_style);
        top.addStyleName("lightBackgroundForDashboardActivity");
        top.addStyleName("lightGrayFourSideBorder");
        
        final Label title = new Label("Play");
        title.setSizeUndefined();
        title.addStyleName("h1");
        top.addComponent(title);
        top.setComponentAlignment(title, Alignment.MIDDLE_LEFT);
        top.setExpandRatio(title, 1);
        
        addComponent(top);
        setExpandRatio(top, 0.5f);
    }
    
    private HorizontalSplitPanel split = new HorizontalSplitPanel();
    private static final String learningLetters = "Learning Letters";
    private static final String wordRecognition = "Word Recognition";
    private static final String jungleGame = "Jungle";
    private static final String airPollution = "Air pollution";
    private static final String plantsAndAnimals = "Plants & Animals";
    private static final String bodyParts="Body Parts";
    private static final String keyboard="Keyboard";
    private static final String earthResources="Earth Resources";
    private static final String diseasePrevention="Disease Prevention";
    private static final String weather="Weather";
    
    private void buildGameListing() {
        
        split.setSizeFull();
        split.setSplitPosition(25,Unit.PERCENTAGE);
        
        Table t = new Table();
        t.setImmediate(true);
        t.addStyleName("borderless");
        t.addStyleName("plain");
        t.setCaption("Topic list");
        t.setSortEnabled(false);
        t.setWidth("100%");
        t.setHeight("100%");
        t.setSelectable(true);
        
        t.addContainerProperty(GlobalConstants.emptyString, VerticalLayout.class, null);
        
        
        
        
        addGameIntoTable(learningLetters,"http://e-learningforkids.org/Courses/EN/L0501/index.html",t);
        addGameIntoTable(wordRecognition,"http://e-learningforkids.org/Courses/EN/Word_Recognition/index.html",t);
        addGameIntoTable(jungleGame,"http://www.e-learningforkids.org/Courses/EN/L0602/index.html",t);
        addGameIntoTable(airPollution,"http://e-learningforkids.org/Courses/EN/AirPollution/index.html",t);
        addGameIntoTable(plantsAndAnimals,"http://www.e-learningforkids.org/Courses/EN/S0501/module/module1.htm",t);
        addGameIntoTable(bodyParts,"http://e-learningforkids.org/Courses/EN/S0702/index.html",t);
        addGameIntoTable(keyboard,"http://e-learningforkids.org/Courses/EN/K0001/index.html",t);
        addGameIntoTable(earthResources,"http://e-learningforkids.org/Courses/EN/S0502/module/module1.htm",t);
        addGameIntoTable(diseasePrevention,"http://www.e-learningforkids.org/Courses/Liquid_Animation/Keeping_Healthy/Disease_Prevention/index.html",t);
        addGameIntoTable(weather,"http://e-learningforkids.org/Courses/EN/Weather/index.html",t);
        
        
        
        
        
        split.setFirstComponent(t);        
        split.setSecondComponent(getImageLayout());
        
        addComponent(split);
        setExpandRatio(split, 4);
    }
    
    private void addGameIntoTable(String game,String url, Table t) 
    {
        Label l1 = new Label(game);
        l1.setSizeFull();
        

        VerticalLayout v1 = new VerticalLayout();
        v1.setSizeFull();
        v1.addComponent(l1);
        v1.setData(url);
        v1.addLayoutClickListener(this);
        
        t.addItem(new Object[]{v1},t.size()+1);
    }
    
    private VerticalLayout getImageLayout()
    {
        VerticalLayout letsPlayLayout = new VerticalLayout();
        letsPlayLayout.setSizeFull();
        Embedded xp =  new Embedded(GlobalConstants.emptyString,new ThemeResource("./img/play.png"));
        xp.setHeight("400px");
        xp.setWidth("400px");
        letsPlayLayout.addComponent(xp);
        letsPlayLayout.setComponentAlignment(xp,Alignment.MIDDLE_CENTER);
        
        return letsPlayLayout;
    }
    
    
    private HorizontalLayout game;
    private VerticalLayout sudokuLayout;
    private VerticalLayout xpLayout;
    private VerticalLayout wordLayout;
    private VerticalLayout excelLayout;
    private VerticalLayout ppLayout;
    private  HorizontalLayout h;
    
    private void buildBodyView() {
        h = new HorizontalLayout();
        h.setSizeFull();
        h.setSpacing(true);
        h.setMargin(true);
        
        Embedded xp =  new Embedded("WindowsXP Game",
                new ThemeResource("./img/games/xp.jpeg"));
        xp.setHeight("200px");
        xp.setWidth("200px");
        xpLayout = new VerticalLayout();
        xpLayout.addComponent(xp);
        xpLayout.addLayoutClickListener(this);
        
        Embedded word =  new Embedded("MS-Word Game",
                new ThemeResource("./img/games/word.jpeg"));
        word.setHeight("200px");
        word.setWidth("200px");
        wordLayout = new VerticalLayout();
        wordLayout.addComponent(word);
        wordLayout.addLayoutClickListener(this);
        
        Embedded excel =  new Embedded("MS-Excel Game",
                new ThemeResource("./img/games/excel.png"));
        excel.setHeight("200px");
        excel.setWidth("200px");
        excelLayout = new VerticalLayout();
        excelLayout.addComponent(excel);
        excelLayout.addLayoutClickListener(this);
        
        Embedded pp =  new Embedded("MS-Powerpoint Game",
                new ThemeResource("./img/games/pp.jpeg"));
        pp.setHeight("200px");
        pp.setWidth("200px");
        ppLayout = new VerticalLayout();
        ppLayout.addComponent(pp);
        ppLayout.addLayoutClickListener(this);
        
//        Embedded sudoku =  new Embedded(null,
//                new ThemeResource("./img/games/sudoku.jpeg"));
//        sudoku.setHeight("200px");
//        sudoku.setWidth("200px");
//        sudokuLayout = new VerticalLayout();
//        sudokuLayout.addComponent(sudoku);
//        sudokuLayout.addLayoutClickListener(new LayoutEvents.LayoutClickListener() {
//
//            @Override
//            public void layoutClick(LayoutClickEvent event) {
//                
//        
//            }
//        });
        
        h.addComponent(xpLayout);
        h.addComponent(wordLayout);
        h.addComponent(excelLayout);
        addComponent(h);
        setExpandRatio(h,1);
        
        h = new HorizontalLayout();
        h.setSizeFull();
        h.setSpacing(true);
        h.setMargin(true);
        
        h.addComponent(ppLayout);
        
        
        Flash f1 = new Flash("",new FileResource(new File("/home/rajkirans/Desktop/crossword_1.swf")));
                f1.setImmediate(true);
                f1.setSizeFull();
        h.addComponent(f1);
        
        Flash f2 = new Flash("",new FileResource(new File("/home/rajkirans/Desktop/typinggamecollection.swf")));
                f2.setImmediate(true);
                f2.setSizeFull();
        h.addComponent(f2);
        addComponent(h);
        
        h = new HorizontalLayout();
        h.setSizeFull();
        h.setSpacing(true);
        h.setMargin(true);
        
        Flash f3 = new Flash("",new FileResource(new File("/home/rajkirans/Desktop/QUIZFIND200.swf")));
                f3.setImmediate(true);
                f3.setSizeFull();
        h.addComponent(f3);
        
        Flash f4 = new Flash("",new FileResource(new File("/home/rajkirans/Desktop/memory.swf")));
                f4.setImmediate(true);
                f4.setSizeFull();
        h.addComponent(f4);
        
        Flash f5 = new Flash("",new FileResource(new File("/home/rajkirans/Desktop/game.swf")));
                f5.setImmediate(true);
                f5.setSizeFull();
        h.addComponent(f5);
        
        addComponent(h);
        setExpandRatio(h,1);
        
     
    }

    @Override
    public void layoutClick(LayoutClickEvent event) 
    {

        VerticalLayout clicked = (VerticalLayout) event.getComponent();
        String url=(String)clicked.getData();
 
                String s = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" "
                + "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\"> "
                + "<html xmlns=\"http://www.w3.org/1999/xhtml\" > "
                + "<head> "
                + "<title>Embedding in IFrame</title> "
                + "</head> "
                + "<body style=\"background: #d0ffd0;\"> "
                + "<div style='height: 5px;'> "
                + "<script src='http://demo.vaadin.com/xsembed/getEmbedJs' "
                + "type='text/javascript'></script> "
                + " </div> "
                + "<table align=\"center\" border=\"3\" width='100%' height='100%' > "
                + "<tr valign=\"top\"> "
                + "<td> "
                + "<iframe name='iframe_a' target='iframe_a' src=\""+url+"\" "
                + "height='500' width='500' "
                + "frameborder=\"0\"></iframe> "
                + "</td> "
                + "</tr> "
                + "</table> "
                + "</body> "
                + "</html>";

                Label l = new Label(s, Label.CONTENT_XHTML);

        
                
                
                VerticalLayout v = new VerticalLayout();
                v.setCaption("Click here to start the game");
                v.setMargin(true);
//                v.setWidth("80%");
//                v.setHeight("80%");
                v.setSizeFull();
                v.addComponent(l);
                
                split.setSecondComponent(v);
        
    }

    
}
