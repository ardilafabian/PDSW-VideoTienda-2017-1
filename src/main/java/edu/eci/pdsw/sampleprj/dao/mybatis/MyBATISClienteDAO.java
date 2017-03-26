package edu.eci.pdsw.sampleprj.dao.mybatis;

import com.google.inject.Inject;
import edu.eci.pdsw.sampleprj.dao.ClienteDAO;
import edu.eci.pdsw.sampleprj.dao.PersistenceException;
import edu.eci.pdsw.sampleprj.dao.mybatis.mappers.ClienteMapper;
import edu.eci.pdsw.samples.entities.Cliente;
import edu.eci.pdsw.samples.entities.Item;
import edu.eci.pdsw.samples.entities.ItemRentado;
import java.util.List;

/**
 *
 * @author skinman95
 */
public class MyBATISClienteDAO implements ClienteDAO {
    
    @Inject
    private ClienteMapper clienteMapper;

    @Override
    public void save(Cliente c) throws PersistenceException {
        try {
            clienteMapper.agregarCliente(c);
        } catch (org.apache.ibatis.exceptions.PersistenceException e) {
            throw new PersistenceException("Error al registrar el cliente " + c.toString(),e);
        }
    }

    @Override
    public Cliente load(int id) throws PersistenceException {
        try{
            return clienteMapper.consultarCliente(id);
        }
        catch(org.apache.ibatis.exceptions.PersistenceException e){
            throw new PersistenceException("Error al consultar el cliente " + id,e);
        }
    }

    @Override
    public List<Cliente> loadClientes() throws PersistenceException {
        try{
            return clienteMapper.consultarClientes();
        }
        catch(org.apache.ibatis.exceptions.PersistenceException e){
            throw new PersistenceException("Error al consultar los clientes",e);
        }
    }

    @Override
    public void registrarItemRentado(long doc, ItemRentado i) throws PersistenceException {
        clienteMapper.agregarItemRentadoACliente(doc, 
                i.getItem().getId(), 
                i.getFechainiciorenta(), 
                i.getFechafinrenta());
    }

    @Override
    public void vetarCliente(long docu, boolean estado) throws PersistenceException {
        if (estado) {
            clienteMapper.vetarCliente(docu, 1);
        } else {
            clienteMapper.vetarCliente(docu, 0);
        }
    }

    @Override
    public void registrarDevolucion(int idItem) throws PersistenceException {
        try {
            clienteMapper.registrarDevolucion(idItem);
        } catch (org.apache.ibatis.exceptions.PersistenceException e){
            throw new PersistenceException("Error al registrar devolucion ",e);
        }
    }

    
}
