/**
 * DISCLAIMER
 * 
 * The quality of the code is such that you should not copy any of it as best
 * practice how to build Vaadin applications.
 * 
 * @author jouni@vaadin.com
 * 
 */

package com.quick.games;

import com.quick.forum.*;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.quick.bean.ExamBean;
import com.quick.bean.ForumEventDetailsBean;
import com.quick.bean.Userprofile;
import java.text.DecimalFormat;
import com.vaadin.data.Property;
import com.quick.entity.ForumEventDetails;
import com.quick.global.GlobalConstants;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.vaadin.event.LayoutEvents;
import com.vaadin.event.LayoutEvents.LayoutClickEvent;
import com.vaadin.event.MouseEvents;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FileResource;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.Flash;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class GamesView extends VerticalLayout implements View, LayoutEvents.LayoutClickListener {

    @Override
    public void enter(ViewChangeEvent event) {
//        setSizeFull();
        addStyleName("schedule");
        //addStyleName("dashboard");
        //addComponent(buildDraftsView());
        buildHeaderView();
        buildBodyView();
    }

    private void buildHeaderView() {
        HorizontalLayout top = new HorizontalLayout();
        top.setWidth("100%");
        top.setSpacing(true);
        //top.setMargin(new MarginInfo(true, true, false, true));
        top.addStyleName("toolbar");
        
        final Label title = new Label("Games");
        title.setSizeUndefined();
        title.addStyleName("h1");
        top.addComponent(title);
        top.setComponentAlignment(title, Alignment.MIDDLE_LEFT);
        top.setExpandRatio(title, 1);
        
        addComponent(top);
        setExpandRatio(top, 0.5f);
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
//        sudokuLayout.addLayoutClickListener(this);
        
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
        //h.addComponent(sudokuLayout);
        
        addComponent(h);
        setExpandRatio(h,1);
        
     
    }

    @Override
    public void layoutClick(LayoutClickEvent event) 
    {
        if(game!=null)
        {
            h.removeComponent(game);
        }
        String url=GlobalConstants.emptyString;
        Component clicked = event.getComponent();
        if(clicked instanceof VerticalLayout)
        {
            VerticalLayout verticalClicked = (VerticalLayout)clicked;
            if(verticalClicked==xpLayout)
            {
                url="http://e-learningforkids.org/Courses/EN/Win_XP/index.html";
            }
            else if(verticalClicked==wordLayout)
            {
                url="http://e-learningforkids.org/Courses/EN/MS_Word/index.html";
            }
            else if(verticalClicked==excelLayout)
            {
                url="http://e-learningforkids.org/Courses/EN/MS_Excel/index.html";
            }
            else if(verticalClicked==ppLayout)
            {
                url="http://e-learningforkids.org/Courses/EN/MS_Powerpoint/index.html";
            }
            else if(verticalClicked==sudokuLayout)
            {
                url="http://e-learningforkids.org/Courses/EN/MS_Word/index.html";
            }
            
        }
        game = new HorizontalLayout();
        game.setCaption("Click on the below image to launch the game");
        game.setImmediate(true);
                game.setSizeFull();
                game.setMargin(true);
                
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
                + "height='150' width='100%' "
                + "frameborder=\"0\"></iframe> "
                + "</td> "
                + "</tr> "
                + "</table> "
                + "</body> "
                + "</html>";

                Label l = new Label(s, Label.CONTENT_XHTML);
                
                
//                Flash f = new Flash("",new FileResource(new File("/home/rajkirans/Desktop/game.swf")));
//                f.setImmediate(true);
//                f.setSizeFull();
//        
                game.addComponent(l);
                h.addComponent(game);
                setExpandRatio(h,2);
        
    }
}
