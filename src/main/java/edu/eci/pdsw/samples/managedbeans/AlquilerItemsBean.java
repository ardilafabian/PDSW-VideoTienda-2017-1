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
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;

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
    
    private int itemId;
    private long rentCost;
    private int rentDays;
    
    public AlquilerItemsBean() {
        Logger.logMsg(Logger.DEBUG, "Id del cliente: "+clientId);
        Logger.logMsg(Logger.DEBUG, "Se insancia " + this.getClass().getName());
    }
    
    @PostConstruct
    public void init() {
        Logger.logMsg(Logger.DEBUG, "Se ejecuta metodo init de post construccion");
        if (clientBean != null) {
            this.clientId = clientBean.getClientId();
            Logger.logMsg(Logger.DEBUG, "Se obtiene el id del cliente de clienteBean: " + clientId);
        } else {
            Logger.logMsg(Logger.ERROR, "Bean de cliente es null");
        }
    }
    
    public String getClientName() throws ExcepcionServiciosAlquiler {
        return sp.consultarCliente(clientId).getNombre();
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
    
    public long getMulta(ItemRentado itr) throws ExcepcionServiciosAlquiler {
        return sp.consultarMultaAlquiler(itr.getItem().getId(), itr.getFechafinrenta());
    }
    
    public void setRentedItem(int id) {
        this.itemId = id;
    }
    
    public String getRentedItem() {
        return "";
    }
    
    public void setDate(int days) throws ExcepcionServiciosAlquiler {
        this.rentDays = days;
        this.rentCost = sp.consultarCostoAlquiler(itemId, days);
        Logger.logMsg(Logger.DEBUG, "Agrega costo de la renta "+rentCost);
    }
    
    public String getDate() {
        return "";
    }
    
    public long getRentCost() {
        return rentCost;
    }
    
    public void alquilarItem() throws ExcepcionServiciosAlquiler {
        Date dt = new Date();
        sp.registrarAlquilerCliente((java.sql.Date) dt, clientId, sp.consultarItem(itemId), rentDays);
    }
}
