/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.pdsw.samples.managedbeans;

import com.sun.media.jfxmedia.logging.Logger;
import edu.eci.pdsw.samples.entities.Item;
import edu.eci.pdsw.samples.entities.ItemRentado;
import edu.eci.pdsw.samples.services.ExcepcionServiciosAlquiler;
import edu.eci.pdsw.samples.services.ServiciosAlquiler;
import edu.eci.pdsw.samples.services.ServiciosAlquilerFactory;
import java.io.Serializable;
import java.time.*;
import java.sql.Date;
import java.util.*;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

/**
 *
 * @author 2106913
 */
@ManagedBean(name = "AlquilerItems")
@ViewScoped
public class AlquilerItemsBean implements Serializable {

    @ManagedProperty(value="#{dtClientes}")
    private ClientesBean clientBean;
    
    ServiciosAlquiler sp = ServiciosAlquilerFactory.getInstance().getServiciosAlquiler();
    long clientId;
    
    private int itemId;
    private long rentCost;
    private int rentDays;
    
    public AlquilerItemsBean() {
        Logger.logMsg(Logger.DEBUG, "Id del cliente: "+clientId+" en "+this.getClass().getName());
        Logger.logMsg(Logger.DEBUG, "Se instancia " + this.getClass().getName());
    }
    
    @PostConstruct
    public void init() {
        Logger.logMsg(Logger.DEBUG, "Se ejecuta metodo init de post construccion");
        if (clientBean != null) {
            this.clientId = clientBean.getClientId();
            Logger.logMsg(Logger.DEBUG, "Se obtiene el id del cliente de clienteBean: " + clientId+" en "+this.getClass().getName());
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
        if (itr != null) {
            return sp.consultarMultaAlquiler(itr.getItem().getId(), itr.getFechafinrenta());
        } else {
            throw new ExcepcionServiciosAlquiler("getMulta: El item rentado es null");
        }
    }
    
    public void setRentedItem(int id) throws ExcepcionServiciosAlquiler {
        List<Item> itds = sp.consultarItemsDisponibles();
        boolean is = false;
        for (Item i : itds) {
            if (i.getId() == id) {
                is = true;
            }
        }
        Logger.logMsg(Logger.DEBUG, "El item esta disponible: "+is);
        if (is) {
            Logger.logMsg(Logger.DEBUG, "Intenta asignar id del item: "+ id);
            this.itemId = id;
            Logger.logMsg(Logger.DEBUG, "Asigna ID del item: " + itemId );
        } else {
            FacesContext.getCurrentInstance().addMessage("noId", new FacesMessage(FacesMessage.SEVERITY_ERROR, "!Error!", "Item no disponible."));
        }
    }
    
    public int getRentedItem() {
        return 0;
    }
    
    public void setDate(int days) throws ExcepcionServiciosAlquiler {
        Logger.logMsg(Logger.DEBUG, "Intenta asignar dias de prestamo" + days +" para el item "+ itemId);
        this.rentDays = days;
        this.rentCost = sp.consultarCostoAlquiler(itemId, days);
        Logger.logMsg(Logger.DEBUG, "Agrega costo de la renta "+rentCost);
    }
    
    public int getDate() {
        return 0;
    }
    
    public long getRentCost() {
        Logger.logMsg(Logger.DEBUG, "Consulta el costo de la renta");
        return rentCost;
    }
    
    public void setRentCost(long rent) {
    }
    
    public void alquilarItem() throws ExcepcionServiciosAlquiler {
        java.util.Date dt = new java.util.Date();
        Logger.logMsg(Logger.DEBUG, "Se alquila item para usuario: " + clientId + 
                " el item: " + (sp.consultarItem(itemId) != null ? sp.consultarItem(itemId).getNombre() : "null"));
        sp.registrarAlquilerCliente(new java.sql.Date(dt.getTime()), clientId, sp.consultarItem(itemId), rentDays);
    }
}
