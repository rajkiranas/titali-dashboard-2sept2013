/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.quick.table;

import com.vaadin.data.Property;
import com.vaadin.demo.dashboard.TeacherMasterContainer;
import com.vaadin.demo.dashboard.TeacherView;
import com.vaadin.ui.Button;
import com.vaadin.ui.Table;
import com.vaadin.ui.themes.BaseTheme;

/**
 *
 * @author vmundhe
 */
public class TeacherTable extends Table{

   private TeacherView teacherView;
    
   public TeacherTable(final TeacherView teacherView) {
        this.teacherView=teacherView;
        setSizeFull();
        addStyleName("borderless");
        setSelectable(true);
        setColumnCollapsingAllowed(true);
        setColumnReorderingAllowed(true);
        setContainerDataSource(teacherView.getTeacherMasterContainer());
        setVisibleColumns(TeacherMasterContainer.NATURAL_COL_ORDER_TEACHER_INFO);
        setColumnHeaders(TeacherMasterContainer.COL_HEADERS_ENGLISH_TEACHER_INFO);
        addValueChangeListener((Property.ValueChangeListener)teacherView);
        setImmediate(true);
        setFooterVisible(true);
        setMultiSelect(true);
        
      addGeneratedColumn("Remove", new Table.ColumnGenerator() 
            {
            @Override
                public Object generateCell(Table source, Object rowItemBean, Object columnId) 
                {
                    Button btnRemove=new Button("Remove");
                    btnRemove.setImmediate(true);
                    btnRemove.setStyleName(BaseTheme.BUTTON_LINK);
                    setItemData(btnRemove,rowItemBean);
                    teacherView.addListenertoBtn(btnRemove);               
                    return btnRemove;
                }
            });
    }

      public void setItemData(Button btnRemove, Object rowItemBean)
        {
            Object arr[]=new Object[]{this,rowItemBean,""};
            btnRemove.setData(arr);
        }
    
}
