/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.pdsw.samples.tests;

import edu.eci.pdsw.logger.Logger;
import edu.eci.pdsw.samples.entities.Cliente;
import edu.eci.pdsw.samples.entities.Item;
import edu.eci.pdsw.samples.entities.TipoItem;
import edu.eci.pdsw.samples.services.ExcepcionServiciosAlquiler;
import edu.eci.pdsw.samples.services.ServiciosAlquiler;
import edu.eci.pdsw.samples.services.ServiciosAlquilerFactory;
import edu.eci.pdsw.samples.services.impl.ServiciosAlquilerItemsStub;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;



/**
 * 
 * Calculo Multa:
 * 
 * Frontera:
 * CF1: Multas a devoluciones hechas en la fecha exacta (multa 0).
 * CF2: Numero minimo de dias que puede solicitar prestado (numdias 1)
 * 
 * Clases de equivalencia:
 * CE1: Multas hechas a devolciones realizadas en fechas posteriores
 * a la limite. (multa multa_diaria*dias_retraso).
 * CE2: Numero de dias estandard del prestamo de un item (numdias 7)
 * CE3: El item solicitado para alquiler no debe estar disponible despues de alquilado (item no esta en la lista de disponibles)
 * 
 * 
 */
public class AlquilerTest {

    private ServiciosAlquiler sa;
    
    public AlquilerTest() {
        Logger.setLevel(Logger.DEBUG);
    }
    
    @Before
    public void setUp() {
        sa = ServiciosAlquilerFactory.getInstance().getServiciosAlquilerTesting();
        
        TipoItem ti = new TipoItem(0, "TIPO ITEM 0");
        
        try {
            sa.agregarTipoItem(ti);
        } catch (ExcepcionServiciosAlquiler ex) {
            fail("No fue posible agregar un tipo a ServiciosAlquiler");
        }
    }
    
    @After
    public void showAndDeleteDB() throws SQLException {
        Connection conn = DriverManager.getConnection("jdbc:h2:file:./target/db/testdb;MODE=MYSQL", "sa", "");
        Statement stmt = conn.createStatement();
        
        PreparedStatement bd = null;
        
        bd = conn.prepareStatement("select\n" +
            "    \n" +
            "            c.nombre as nombre,\n" +
            "            c.documento as documento,\n" +
            "            c.telefono as telefono,\n" +
            "            c.direccion as direccion,\n" +
            "            c.email as email,\n" +
            "            c.vetado as vetado,\n" +
            "\n" +
            "            ir.id as ir_id,\n" +
            "            ir.CLIENTES_documento as ir_documentoCliente,\n" +
            "            ir.fechainiciorenta as ir_fechaIniciorenta,\n" +
            "            ir.fechafinrenta as ir_fechaFinRenta,\n" +
            "\n" +
            "            i.id as ir_it_id,\n" +
            "            i.nombre as ir_it_nombre,\n" +
            "            i.descripcion as ir_it_descripcion,\n" +
            "            i.fechalanzamiento as ir_it_fechaLanzamiento,\n" +
            "            i.tarifaxdia as ir_it_tarifaXDia,\n" +
            "            i.formatorenta as ir_it_formatoRenta,\n" +
            "            i.genero as ir_it_genero,\n" +
            "                \n" +
            "            ti.id as ir_it_tipo_id,\n" +
            "            ti.descripcion as ir_it_tipo_descripcion \n" +
            "        FROM VI_CLIENTES as c \n" +
            "        left join VI_ITEMRENTADO as ir on c.documento=ir.CLIENTES_documento \n" +
            "        left join VI_ITEMS as i on ir.ITEMS_id=i.id \n" +
            "        left join VI_TIPOITEM as ti on i.TIPOITEM_id=ti.id ");
        
        ResultSet res = bd.executeQuery();
        
        while(res.next()) {
            System.out.println(res.getString("nombre"));
            System.out.println(res.getString("documento"));
            System.out.println(res.getString("ir_id"));
            System.out.println(res.getString("ir_it_nombre"));
            System.out.println(res.getString("ir_it_tipo_id"));
            System.out.println(res.getString("ir_it_tipo_descripcion"));
        }
        
        stmt.execute("delete from VI_CLIENTES");
        stmt.execute("delete from VI_ITEMS");
        stmt.execute("delete from VI_ITEMRENTADO");
        stmt.execute("delete from VI_TIPOITEM");
        conn.commit();
        conn.close();
    }
    
    /**
     * CF1: Multas a devoluciones hechas en la fecha exacta (multa 0).
     * @throws ExcepcionServiciosAlquiler 
     */
    @Test
    public void CF1Test() throws ExcepcionServiciosAlquiler{
        Item i1 = new Item(sa.consultarTipoItem(0), 44, "Los 4 Fantasticos", "Los 4 Fantásticos  es una película de superhéroes  basada en la serie de cómic homónima de Marvel.", java.sql.Date.valueOf("2005-06-08"), 2000, "DVD", "Ciencia Ficcion");
        sa.registrarCliente(new Cliente("Juan Perez",3842,"24234","calle 123","aa@gmail.com"));
        sa.registrarItem(i1);
        
        sa.registrarAlquilerCliente(java.sql.Date.valueOf("2005-12-20"), 3842, i1, 5);
        
        assertEquals("No se calcula correctamente la multa (0) "
                + "cuando la devolucion se realiza el dia limite."
                , 0, sa.consultarMultaAlquiler(44, java.sql.Date.valueOf("2005-12-25")));
                
    }
    
    @Test
    public void CE1Test() throws ExcepcionServiciosAlquiler{
        Item i1 = new Item(sa.consultarTipoItem(0), 55, "Los 4 Fantasticos", "Los 4 Fantásticos  es una película de superhéroes  basada en la serie de cómic homónima de Marvel.", java.sql.Date.valueOf("2005-06-08"), 2000, "DVD", "Ciencia Ficcion");        
        sa.registrarCliente(new Cliente("Juan Perez",9843,"24234","calle 123","aa@gmail.com"));
        sa.registrarItem(i1);
                
        Item item = sa.consultarItem(55);
        
        sa.registrarAlquilerCliente(java.sql.Date.valueOf("2005-12-20"), 9843, item, 5);
        //prueba: 3 dias de retraso
        assertEquals("No se calcula correctamente la multa "
                + "cuando la devolucion se realiza varios dias despues del limite."
                ,sa.valorMultaRetrasoxDia()*3,sa.consultarMultaAlquiler(55, java.sql.Date.valueOf("2005-12-28")));
                
    }
    
    
    @Test
    public void CE3test() throws ExcepcionServiciosAlquiler {
        Item i1=new Item(sa.consultarTipoItem(0), 55, "Los 4 Fantasticos", "Los 4 Fantásticos  es una película de superhéroes  basada en la serie de cómic homónima de Marvel.", java.sql.Date.valueOf("2005-06-08"), 2000, "DVD", "Ciencia Ficcion");
        // FIXME metodo registrarCliente lanza excepcion ExcepcionServiciosAlquiler "ya se registro"
        sa.registrarCliente(new Cliente("Juan Perez",9843,"24234","calle 123","aa@gmail.com")); 
        sa.registrarItem(i1);
        
        Item item=sa.consultarItem(55);
        
        sa.registrarAlquilerCliente(java.sql.Date.valueOf("2005-12-20"), 9843, item, 5);
        
        assertEquals("Item aun aparece disponible", false ,sa.consultarItemsDisponibles().contains(item));
    }
    
}
