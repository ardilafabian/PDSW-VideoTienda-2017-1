package edu.eci.pdsw.sampleprj.dao.mybatis.mappers;


import edu.eci.pdsw.samples.entities.Item;
import java.util.Date;
import java.util.List;
import org.apache.ibatis.annotations.Param;

/**
 *
 * @author 2106913
 */
public interface ItemMapper {
    
    public List<Item> getItems();
    
    public Item consultarItem(@Param("idItem") int id);
    
    public void insertarItem(@Param("item") Item it);

    public List<Item> consultarItemsDisponibles();    
    
    public void actualizarTarifa(@Param("item") int id, @Param("tarifa") long tarifa);
}
