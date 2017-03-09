/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.pdsw.samples.managedbeans;

import com.sun.media.jfxmedia.logging.Logger;
import edu.eci.pdsw.samples.entities.Cliente;
import edu.eci.pdsw.samples.services.ExcepcionServiciosAlquiler;
import edu.eci.pdsw.samples.services.ServiciosAlquiler;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

/**
 *
 * @author skinman95
 */
@ManagedBean(name = "dtClientes")
@ViewScoped
public class ClientesBean implements Serializable {
    
    ServiciosAlquiler sp = ServiciosAlquiler.getInstance();
    
    public ClientesBean() {
        Logger.logMsg(Logger.DEBUG, "Se instancia " + this.getClass().getName());
    }
    
    @PostConstruct
    public void init() {
        
    }
    
    public List<Cliente> getClientes() throws ExcepcionServiciosAlquiler {
        Logger.logMsg(Logger.DEBUG, "Se obtienen los clientes desde " + this.getClass().getName());
        return sp.consultarClientes();
    }
    
    
    
}
