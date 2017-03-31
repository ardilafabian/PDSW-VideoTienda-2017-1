/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.pdsw.samples.managedbeans;

import edu.eci.pdsw.logger.Logger;
import edu.eci.pdsw.samples.entities.Item;
import edu.eci.pdsw.samples.entities.ItemRentado;
import edu.eci.pdsw.samples.services.ExcepcionServiciosAlquiler;
import edu.eci.pdsw.samples.services.ServiciosAlquiler;
import edu.eci.pdsw.samples.services.ServiciosAlquilerFactory;
import java.io.Serializable;
import java.util.*;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
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
        Logger.setLevel(Logger.DEBUG);
        
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
    
    public String getClientName() {
        String n = "null";
        try {
            n = sp.consultarCliente(clientId).getNombre();
        } catch (ExcepcionServiciosAlquiler ex) {
            Logger.logMsg(Logger.ERROR, this.getClass().getName() + ": " + ex.getMessage());
        }
        return n;
    }
    
    public ClientesBean getClientBean(){
        return clientBean;
    }
    
    public void setClientBean(ClientesBean mb) {
        this.clientBean = mb;
    }
    
    public List<ItemRentado> getItems() {
        List<ItemRentado> itrs = null;
        try {
            itrs = sp.consultarItemsCliente(this.clientId);
        } catch (ExcepcionServiciosAlquiler ex) {
            Logger.logMsg(Logger.ERROR, this.getClass().getName() + ": " + ex.getMessage());
        }
        
        return itrs;
    }
    
    public long getMulta(ItemRentado itr) {
        long r = 0;
        Logger.logMsg(Logger.DEBUG, this.getClass().getName() + "->getMulta() ItemRentado: " + itr);
        if (itr != null) {
            if (itr.getItem() != null) {
                try {
                    r =  sp.consultarMultaAlquiler(itr.getItem().getId(), itr.getFechafinrenta());
                } catch (ExcepcionServiciosAlquiler ex) {
                    Logger.logMsg(Logger.ERROR, this.getClass().getName() + ": " + ex.getMessage());
                }
            } else {
                Logger.logMsg(Logger.ERROR, this.getClass().getName() + ": El item del item rentado es null");
            }
        } else {
            Logger.logMsg(Logger.ERROR, this.getClass().getName() + "getMulta: El item rentado es null");
        }
        return r;
    }
    
    public void setRentedItem(int id) {
        boolean is = false;
        try {
            List<Item> itds = sp.consultarItemsDisponibles();
            for (Item i : itds) {
                if (i.getId() == id) {
                    is = true;
                }
            }
        } catch (ExcepcionServiciosAlquiler ex) {
            Logger.logMsg(Logger.ERROR, this.getClass().getName() + ": " + ex.getMessage());
        }
        
        Logger.logMsg(Logger.DEBUG, "El item esta disponible: " + is);
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
    
    public void setDate(int days) {
        Logger.logMsg(Logger.DEBUG, "Intenta asignar dias de prestamo" + days +" para el item "+ itemId);
        try {
            this.rentCost = sp.consultarCostoAlquiler(itemId, days);
            this.rentDays = days;
            Logger.logMsg(Logger.DEBUG, "Agrega costo de la renta "+rentCost);
        } catch (ExcepcionServiciosAlquiler ex) {
            Logger.logMsg(Logger.ERROR, this.getClass().getName() + ": " + ex.getMessage());
        }
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
    
    public void alquilarItem() {
        try {
            java.util.Date dt = new java.util.Date();
            Logger.logMsg(Logger.DEBUG, "Se alquila item para usuario: " + clientId +
                    " el item: " + sp.consultarItem(itemId).getNombre());
            sp.registrarAlquilerCliente(new java.sql.Date(dt.getTime()), clientId, sp.consultarItem(itemId), rentDays);
        } catch (ExcepcionServiciosAlquiler ex) {
            Logger.logMsg(Logger.ERROR, this.getClass().getName() + ": " + ex.getMessage());
        }
    }
}
