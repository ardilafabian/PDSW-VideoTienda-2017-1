/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.pdsw.samples.managedbeans;

import com.sun.media.jfxmedia.logging.Logger;
import edu.eci.pdsw.samples.entities.ItemRentado;
import edu.eci.pdsw.samples.services.ExcepcionServiciosAlquiler;
import edu.eci.pdsw.samples.services.ServiciosAlquiler;
import java.io.Serializable;
import java.util.*;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

/**
 *
 * @author 2106913
 */
@ManagedBean(name = "AlquilerItems")
@ViewScoped
public class AlquilerItemsBean implements Serializable {

    @ManagedProperty(value="#{ClientesBean}")
    private ClientesBean clientBean;
    
    ServiciosAlquiler sp = ServiciosAlquiler.getInstance();
    long clientId = clientBean.getClientId();
    
    
    public ClientesBean getClientBean(){
        return clientBean;
    }
    
    public void setClientBean(ClientesBean mb) {
        this.clientBean = mb;
    }
    
    @PostConstruct
    public void init() {
        System.out.println("ID DOCUMENTO: "+clientBean.getClientId());
    }
    
    public AlquilerItemsBean() {
        Logger.logMsg(Logger.DEBUG, "Se instancia " + this.getClass().getName());
    }
    
    public List<ItemRentado> getItems() throws ExcepcionServiciosAlquiler {
        return sp.consultarItemsCliente(clientId);
    }

}
