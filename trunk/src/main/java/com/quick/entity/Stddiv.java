package com.quick.entity;
// Generated 27 Apr, 2013 12:52:34 PM by Hibernate Tools 3.2.1.GA

/**
 * Stddiv generated by hbm2java
 */

public class Stddiv  implements java.io.Serializable {


     private StddivId id;
     private Std std;

    public Stddiv() {
    }

    public Stddiv(StddivId id, Std std) {
       this.id = id;
       this.std = std;
    }
   
    
   
    public StddivId getId() {
        return this.id;
    }
    
    public void setId(StddivId id) {
        this.id = id;
    }

    public Std getStd() {
        return this.std;
    }
    
    public void setStd(Std std) {
        this.std = std;
    }




}


