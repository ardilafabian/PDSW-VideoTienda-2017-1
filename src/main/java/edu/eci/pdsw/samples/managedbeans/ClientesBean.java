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
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.*;

/**
 *
 * @author skinman95
 */
@ManagedBean(name = "dtClientes")
@SessionScoped
public class ClientesBean implements Serializable {
    
    ServiciosAlquiler sp = ServiciosAlquiler.getInstance();
    
    Cliente clienteAgregar;
    public long clientId;
    
    public ClientesBean() {
        Logger.logMsg(Logger.DEBUG, "Se instancia " + this.getClass().getName());
        clienteAgregar = new Cliente();
    }
    
    @PostConstruct
    public void init() {
        
    }
    
    public String changeView(long id) {
        this.clientId = id;
        Logger.logMsg(Logger.DEBUG, "*Id del usuario: "+clientId);
        return "RegistroClienteItem.xhtml";
    }
    
    public long getClientId() {
        return clientId;
    }
    
    public List<Cliente> getClientes() throws ExcepcionServiciosAlquiler {
        Logger.logMsg(Logger.DEBUG, "Se obtienen los clientes desde " + this.getClass().getName());
        return sp.consultarClientes();
    }
    
    public void registrarCliente() {
        Logger.logMsg(Logger.DEBUG, "Se intenta registrar el cliente");
        try {
            sp.registrarCliente(clienteAgregar);
            Logger.logMsg(Logger.DEBUG, "Se registra el cliente " + 
                    clienteAgregar.getNombre() != null ? clienteAgregar.getNombre() : "null");
        } catch (ExcepcionServiciosAlquiler ex) {
            Logger.logMsg(Logger.ERROR, ex.getMessage());
        }
    }
    
    public String getNombreCliente() {
        return "";
    }
    
    public long getDocumentoCliente() {
        return 0L;
    }
    
    public String getTelefonoCliente() {
        return "";
    }
    
    public String getDireccionCliente() {
        return "";
    }
    
    public String getEmailCliente() {
        return "";
    }
    
    public void setNombreCliente(String nombre) {
        Logger.logMsg(Logger.DEBUG, "Se intenta establecer el nombre del cliente");
        if (nombre != null) {
            Logger.logMsg(Logger.DEBUG, "Se establece el nombre del cliente " + nombre);
            clienteAgregar.setNombre(nombre);
        }
    }
    
    public void setDocumentoCliente(long doc) {
        Logger.logMsg(Logger.DEBUG, "Se intenta establecer el documento del cliente " + doc);
        clienteAgregar.setDocumento(doc);
    }
    
    public void setTelefonoCliente(String telefono) {
        Logger.logMsg(Logger.DEBUG, "Se intenta establecer el telefono del cliente");
        if (telefono != null) {
            Logger.logMsg(Logger.DEBUG, "Se establece el telefono del cliente " + telefono);
            clienteAgregar.setTelefono(telefono);
        }
    }
    
    public void setDireccionCliente(String dir) {
        Logger.logMsg(Logger.DEBUG, "Se intenta establecer la direccion del cliente");
        if (dir != null) {
            Logger.logMsg(Logger.DEBUG, "Se establece la direccion del cliente " + dir);
            clienteAgregar.setDireccion(dir);
        }
    }
    
    public void setEmailCliente(String email) {
        Logger.logMsg(Logger.DEBUG, "Se intenta establecer el email del cliente");
        if (email != null) {
            Logger.logMsg(Logger.DEBUG, "Se establece el email del cliente " + email);
            clienteAgregar.setEmail(email);
        }
    }
}
