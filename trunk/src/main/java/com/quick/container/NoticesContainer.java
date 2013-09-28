/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.quick.container;

import com.quick.bean.NoticeBean;
import com.quick.bean.NoticeBean;
import com.quick.data.MasterDataProvider;
import com.vaadin.data.util.BeanItemContainer;
import java.util.Date;
import java.util.List;

/**
 *
 * @author rajkiran
 */
public class NoticesContainer extends BeanItemContainer<NoticeBean> {


      public static final Object[] NATURAL_COL_ORDER_NOTICES = new Object[]{
       "noticedate", "bywhom", "noticeline"};
      //"std","fordiv",
        /**
        * "Human readable" captions for properties in same order as in
        * NATURAL_COL_ORDER_DAILY_CALLS.
        */
    public static final String[] COL_HEADERS_ENGLISH_NOTICES = new String[]{
       "Date", "By whom", "Subject"};
    //"Std","Div",
    
    public NoticesContainer(){
        super(NoticeBean.class);
    }
    
    public static NoticesContainer getNoticesContainer(List<NoticeBean> list){
        NoticesContainer container = null;
        container = new NoticesContainer();
         NoticeBean bean = null;
         if(list!=null){
             for(NoticeBean mpb:list){
           bean = new NoticeBean();
           bean.setNoticeid(mpb.getNoticeid());
           bean.setBywhom(mpb.getBywhom());
           bean.setStd(mpb.getStd());
           bean.setFordiv(mpb.getFordiv());
           bean.setNoticedate(mpb.getNoticedate());
           bean.setNoticeline(mpb.getNoticeline());
           bean.setNoticebody(mpb.getNoticebody());
           bean.setSub(mpb.getSub());
           container.addItem(bean);
        }
       }
        
        return container;
    }
}
