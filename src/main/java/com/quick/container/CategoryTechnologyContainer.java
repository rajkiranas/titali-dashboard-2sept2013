/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.quick.container;

import com.quick.bean.CategoryDistributionBean;
import com.quick.bean.UpcomingTechnologyBean;
import com.quick.data.MasterDataProvider;
import com.vaadin.data.util.BeanItemContainer;
import java.util.Date;
import java.util.List;

/**
 *
 * @author rajkiran
 */
public class CategoryTechnologyContainer extends BeanItemContainer<CategoryDistributionBean> {


      public static final Object[] NATURAL_COL_ORDER_TECHNOLOGIES = new Object[]{
       "technologyName", "percentage"};
      //"std","fordiv",
        /**
        * "Human readable" captions for properties in same order as in
        * NATURAL_COL_ORDER_DAILY_CALLS.
        */
    public static final String[] COL_HEADERS_ENGLISH_TECHNOLOGIES = new String[]{
       "Technology","%"};
    //"Std","Div",
    
    public CategoryTechnologyContainer(){
        super(CategoryDistributionBean.class);
    }
    
    public static CategoryTechnologyContainer getRelatedTechnologiesContainer(List<CategoryDistributionBean> list){
        CategoryTechnologyContainer container = null;
        container = new CategoryTechnologyContainer();
         CategoryDistributionBean bean = null;
         if(list!=null){
             for(CategoryDistributionBean mpb:list){
           
                 
           bean = new CategoryDistributionBean();
           
           bean.setCategory(mpb.getCategory());
           bean.setTechnologyName(mpb.getTechnologyName());
           bean.setPercentage(mpb.getPercentage());
           
           container.addItem(bean);
        }
       }
        
        return container;
    }
}
