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
import javax.el.ELContext;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

/**
 *
 * @author 2106913
 */
@ManagedBean(name = "AlquilerItems")
@RequestScoped
public class AlquilerItemsBean implements Serializable {

    @ManagedProperty(value="#{ClientesBean}")
    private ClientesBean clientBean;
    
    ServiciosAlquiler sp = ServiciosAlquiler.getInstance();
    long clientId;
    
    public AlquilerItemsBean() {
        Logger.logMsg(Logger.DEBUG, "Id del cliente: "+clientId);
        Logger.logMsg(Logger.DEBUG, "Se insancia " + this.getClass().getName());
        Logger.logMsg(Logger.DEBUG, "Nombre del bean: "+clientBean.getClass().getName());
        Logger.logMsg(Logger.DEBUG, "Se intenta asignar id del usuario: "/*clientBean.getClientId()*/);
        this.clientId = clientBean.clientId;
        Logger.logMsg(Logger.DEBUG, "Se asigna el id del usuario: "+clientId);
    }
    
    /*@PostConstruct*/
    public void init() {
    }
    
    public ClientesBean getClientBean(){
        return clientBean;
    }
    
    public void setClientBean(ClientesBean mb) {
        this.clientBean = mb;
    }
    
    public List<ItemRentado> getItems() throws ExcepcionServiciosAlquiler {
        return sp.consultarItemsCliente(this.clientId);
    }

}
