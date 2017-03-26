package edu.eci.pdsw.samples.services.impl;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.sun.media.jfxmedia.logging.Logger;
import edu.eci.pdsw.sampleprj.dao.ClienteDAO;
import edu.eci.pdsw.sampleprj.dao.ItemDAO;
import edu.eci.pdsw.sampleprj.dao.PersistenceException;
import edu.eci.pdsw.sampleprj.dao.TipoItemDAO;

import edu.eci.pdsw.samples.entities.Cliente;
import edu.eci.pdsw.samples.entities.Item;
import edu.eci.pdsw.samples.entities.ItemRentado;
import edu.eci.pdsw.samples.entities.TipoItem;
import edu.eci.pdsw.samples.services.ExcepcionServiciosAlquiler;
import edu.eci.pdsw.samples.services.ServiciosAlquiler;
import java.sql.Date;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

/**
 * 
 * @author hcadavid
 */
@Singleton
public class ServiciosAlquilerItemsImpl implements ServiciosAlquiler {

    @Inject
    private ItemDAO daoItem;
    
    @Inject
    private ClienteDAO daoCliente;
    
    @Inject
    private TipoItemDAO daoTipoItem;
    
    private static final int MULTA_DIARIA=5000;
    
    @Override
    public int valorMultaRetrasoxDia() {
        return MULTA_DIARIA;
    }

    @Override
    public Cliente consultarCliente(long docu) throws ExcepcionServiciosAlquiler {
        try {
            return daoCliente.load((int)docu);
        } catch (PersistenceException ex) {
            throw new ExcepcionServiciosAlquiler("Error al consultar el cliente " + docu,ex);
        }
    }

    @Override
    public List<ItemRentado> consultarItemsCliente(long idcliente) throws ExcepcionServiciosAlquiler {
        Cliente c = this.consultarCliente(idcliente);
        return c.getRentados();
    }

    @Override
    public List<Cliente> consultarClientes() throws ExcepcionServiciosAlquiler {
        try {
            return daoCliente.loadClientes();
        } catch (PersistenceException ex) {
            throw new ExcepcionServiciosAlquiler("Error al consultar los clientes",ex);
        }
    }

    @Override
    public Item consultarItem(int id) throws ExcepcionServiciosAlquiler {
        try {
            return daoItem.load(id);
        } catch (PersistenceException ex) {
            throw new ExcepcionServiciosAlquiler("Error al consultar el item "+id,ex);
        }
    }

    @Override
    public List<Item> consultarItemsDisponibles() throws ExcepcionServiciosAlquiler {
        try {
            return daoItem.load();
        } catch (PersistenceException ex) {
            throw new ExcepcionServiciosAlquiler("Error al consultar items disponibles ", ex);
        } 
    }

    private ItemRentado consultarItemRentado(int iditem) throws ExcepcionServiciosAlquiler {
        List<Cliente> clientes = this.consultarClientes();
        ItemRentado item = null;
        
        for (int i = 0; i < clientes.size() && item == null; i++) {
            List<ItemRentado> items = clientes.get(i).getRentados();
            for(int j = 0; j < items.size() && item == null; j++) {
                ItemRentado actual = items.get(j);
                if (actual.getId() == iditem) {
                    item = actual;
                }
            }
        }
        
        if (item == null) {
            throw new ExcepcionServiciosAlquiler("El item " + iditem + "no esta en alquiler");
        }
        
        return item;
    }
    
    @Override
    public long consultarMultaAlquiler(int iditem, Date fechaDevolucion) throws ExcepcionServiciosAlquiler {
        ItemRentado item = consultarItemRentado(iditem);
        
        LocalDate fechaMinimaEntrega = item.getFechafinrenta().toLocalDate();
        LocalDate fechaEntrega = fechaDevolucion.toLocalDate();
        long diasRetraso = ChronoUnit.DAYS.between(fechaMinimaEntrega, fechaEntrega);
        
        if (item.getItem() == null) {
            Logger.logMsg(Logger.ERROR, "El item rentado " + iditem + " tiene su item asignado como nulo");
            throw new ExcepcionServiciosAlquiler("El item rentado" + iditem + "no tiene item asignado");
        }
        
        return diasRetraso * this.valorMultaRetrasoxDia();
    }

    @Override
    public TipoItem consultarTipoItem(int id) throws ExcepcionServiciosAlquiler {
        try {
            return daoTipoItem.load(id);
        } catch (PersistenceException ex) {
            throw new ExcepcionServiciosAlquiler("Error al obtener el tipo de item " + id, ex);
        }
    }

    @Override
    public List<TipoItem> consultarTiposItem() throws ExcepcionServiciosAlquiler {
        try {
            return daoTipoItem.loadTipos();
        } catch (PersistenceException ex) {
            throw new ExcepcionServiciosAlquiler("Error al obtener los tipos de los items", ex);
        }
    }

    @Override
    public void registrarAlquilerCliente(Date date, long docu, Item item, int numdias) throws ExcepcionServiciosAlquiler {
        LocalDate ld = date.toLocalDate();
        LocalDate ld2 = ld.plusDays(numdias);
        
        ItemRentado ir = new ItemRentado(0,item,date,java.sql.Date.valueOf(ld2));

        //Cliente c = this.consultarCliente(docu);
        //c.getRentados().add(ir); // XXX : es necesario?
        if (! this.consultarItemsDisponibles().contains(item)) {
            throw new ExcepcionServiciosAlquiler("El item " + item + " no esta disponible para alquiler");
        }
        
        try {
            daoCliente.registrarItemRentado(docu, ir);
        } catch (PersistenceException ex) {
            throw new ExcepcionServiciosAlquiler("Error al registrar item rentado del cliente " + docu, ex);
        }
    }

    @Override
    public void registrarCliente(Cliente p) throws ExcepcionServiciosAlquiler {
        try {
            if (p != null) {
                daoCliente.save(p);
            } else {
                throw new ExcepcionServiciosAlquiler("No es posible registrar un cliente null");
            }
        } catch (PersistenceException ex) {
            throw new ExcepcionServiciosAlquiler("Error al registrar el cliente " + p, ex);
        }
    }

    @Override
    public void registrarDevolucion(int iditem) throws ExcepcionServiciosAlquiler {
        ItemRentado it = this.consultarItemRentado(iditem);
        if (this.consultarMultaAlquiler(iditem, it.getFechafinrenta()) != 0) {
            throw new ExcepcionServiciosAlquiler("El cliente tiene una multa pendiente");
        } else {
            try {
                daoCliente.registrarDevolucion(iditem);
            } catch (PersistenceException ex) {
                throw new ExcepcionServiciosAlquiler("Error al registrar devolcion ", ex);
            }
        }
    }

    @Override
    public long consultarCostoAlquiler(int iditem, int numdias) throws ExcepcionServiciosAlquiler {
        Item it = null;
        try {
            it = daoItem.load(iditem);
        } catch (PersistenceException ex) {
            throw new ExcepcionServiciosAlquiler("Error al consultar el item " + iditem, ex);
        }
        
        return it.getTarifaxDia() * numdias;
    }

    @Override
    public void actualizarTarifaItem(int id, long tarifa) throws ExcepcionServiciosAlquiler {
        Item it = this.consultarItem(id);
        
        if (it == null) {
            throw new ExcepcionServiciosAlquiler("El item no existe");
        } else if (tarifa < 0) {
            throw new ExcepcionServiciosAlquiler("Tarifa no puede ser negativa");
        } else {
            try {
                daoItem.actualizarTarifa(id, tarifa);
            } catch (PersistenceException ex) {
                throw new ExcepcionServiciosAlquiler("Error al actualizar la tarifa del item "+it.getNombre() , ex);
            }
        }
    }

    @Override
    public void registrarItem(Item i) throws ExcepcionServiciosAlquiler {
        try {
            daoItem.save(i);
        } catch (PersistenceException ex) {
            throw new ExcepcionServiciosAlquiler("Error al registrar el item " + i, ex);
        }
    }

    @Override
    public void vetarCliente(long docu, boolean estado) throws ExcepcionServiciosAlquiler {
        try {
            daoCliente.vetarCliente(docu, estado);
        } catch (PersistenceException ex) {
            throw new ExcepcionServiciosAlquiler("Error el vetar cliente ", ex);
        }
    }

    @Override
    public void agregarTipoItem(TipoItem tipo) throws ExcepcionServiciosAlquiler {
        try {
            daoTipoItem.save(tipo);
        } catch (PersistenceException ex) {
            throw new ExcepcionServiciosAlquiler("Error al registrar el tipo de item " + tipo, ex);
        }
    }
}
