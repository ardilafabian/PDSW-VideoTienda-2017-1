package edu.eci.pdsw.sampleprj.dao.mybatis.mappers;

import edu.eci.pdsw.samples.entities.Cliente;
import edu.eci.pdsw.samples.entities.Item;
import edu.eci.pdsw.samples.entities.ItemRentado;
import java.util.Date;
import java.util.List;
import org.apache.ibatis.annotations.Param;

/**
 *
 * @author 2106913
 */
public interface ClienteMapper {
    
    public Cliente consultarCliente(@Param("idcli") int id);
    
    /**
     * Registrar un nuevo item rentado asociado al cliente identificado
     * con 'idc' y relacionado con el item identificado con 'idi'
     * @param id
     * @param idit
     * @param fechainicio
     * @param fechafin 
     */
    public void agregarItemRentadoACliente(@Param(value = "idCliente") long id, 
            @Param(value = "idItem") int idit, 
            @Param(value = "fechaInicio") Date fechainicio, 
            @Param(value = "fechaFin") Date fechafin);
    
    public List<ItemRentado> consultarItemsCliente(@Param("documento") long doc);

    /**
     * Consultar todos los clientes
     * @return 
     */
    public List<Cliente> consultarClientes();

    public boolean getCliente(int i);
    
    /**
     * Agrega un nuevo cliente si no existe
     * @param cliente nuevo
     * @return Se agrego correctamente
     */
    public boolean agregarCliente(@Param("cliente") Cliente cliente);
    
    public void vetarCliente(@Param("cliente") long doc, @Param("vetar") int vet);
    
    public void registrarDevolucion(@Param("idItem") int id);
}
