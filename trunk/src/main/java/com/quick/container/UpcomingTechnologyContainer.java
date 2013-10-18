/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.quick.container;

import com.quick.bean.UpcomingTechnologyBean;
import com.quick.data.MasterDataProvider;
import com.vaadin.data.util.BeanItemContainer;
import java.util.Date;
import java.util.List;

/**
 *
 * @author rajkiran
 */
public class UpcomingTechnologyContainer extends BeanItemContainer<UpcomingTechnologyBean> {


      public static final Object[] NATURAL_COL_ORDER_TECHNOLOGIES = new Object[]{
       "technologydate", "technologyline","category"};
      //, "bywhom"
      //"std","fordiv",
        /**
        * "Human readable" captions for properties in same order as in
        * NATURAL_COL_ORDER_DAILY_CALLS.
        */
    public static final String[] COL_HEADERS_ENGLISH_TECHNOLOGIES = new String[]{
       "Date", "Name", "Category"};
    //"Std","Div",
    
    public UpcomingTechnologyContainer(){
        super(UpcomingTechnologyBean.class);
    }
    
    public static UpcomingTechnologyContainer getUTContainer(List<UpcomingTechnologyBean> list){
        UpcomingTechnologyContainer container = null;
        container = new UpcomingTechnologyContainer();
         UpcomingTechnologyBean bean = null;
         if(list!=null){
             for(UpcomingTechnologyBean mpb:list){
           bean = new UpcomingTechnologyBean();
           bean.setTechnologyid(mpb.getTechnologyid());
           bean.setBywhom(mpb.getBywhom());
           bean.setStd(mpb.getStd());
           bean.setFordiv(mpb.getFordiv());
           bean.setTechnologydate(mpb.getTechnologydate());
           bean.setTechnologyline(mpb.getTechnologyline());
           bean.setTechnologybody(mpb.getTechnologybody());
           bean.setSub(mpb.getSub());
           bean.setCategory(mpb.getCategory());
           container.addItem(bean);
        }
       }
        
        return container;
    }
}
