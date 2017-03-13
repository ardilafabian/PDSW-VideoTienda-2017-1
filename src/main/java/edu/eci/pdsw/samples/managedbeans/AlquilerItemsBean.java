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
import java.time.*;
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

    @ManagedProperty(value="#{dtClientes}")
    private ClientesBean clientBean;
    
    ServiciosAlquiler sp = ServiciosAlquiler.getInstance();
    long clientId;
    
    public AlquilerItemsBean() {
        Logger.logMsg(Logger.DEBUG, "Id del cliente: "+clientId);
        Logger.logMsg(Logger.DEBUG, "Se insancia " + this.getClass().getName());
    }
    
    @PostConstruct
    public void init() {
        Logger.logMsg(Logger.DEBUG, "Se ejecuta metodo init de post construccion");
        this.clientId = clientBean.getClientId();
        Logger.logMsg(Logger.DEBUG, "Se obtiene el id del cliente de clienteBean: " + clientId);
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
    
    public long getMulta(ItemRentado itr) {
        Calendar cal = Calendar.getInstance();
        Calendar now = Calendar.getInstance();
        cal.setTime(itr.getFechafinrenta());
        now.setTime(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));
        int res = daysBetween(cal.getTime(), now.getTime()); 
        return res > 0 ? res*itr.getItem().getTarifaxDia() : 0;
    }
    
    public int daysBetween(Date d1, Date d2){
             return (int)( (d2.getTime() - d1.getTime()) / (1000 * 60 * 60 * 24));
    }
}
