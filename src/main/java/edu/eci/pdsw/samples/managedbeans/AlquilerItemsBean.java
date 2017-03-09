/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.pdsw.samples.managedbeans;

import com.sun.media.jfxmedia.logging.Logger;
import edu.eci.pdsw.samples.services.ServiciosAlquiler;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author 2106913
 */
@ManagedBean(name = "AlquilerItems")
@SessionScoped
public class AlquilerItemsBean implements Serializable {

    @ManagedProperty(value="#{ClientesBean}")
    private ClientesBean clientBean;
    
    ServiciosAlquiler sp = ServiciosAlquiler.getInstance();
    
    
    public ClientesBean getClientbean(){
        return clientBean;
    }
    
    @PostConstruct
    public void init() {
        
    }
    
    public AlquilerItemsBean() {
        Logger.logMsg(Logger.DEBUG, "Se instancia " + this.getClass().getName());
    }

}
