/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.pdsw.samples.tests;

import edu.eci.pdsw.samples.entities.Cliente;
import edu.eci.pdsw.samples.entities.Item;
import edu.eci.pdsw.samples.entities.TipoItem;
import edu.eci.pdsw.samples.services.ExcepcionServiciosAlquiler;
import edu.eci.pdsw.samples.services.ServiciosAlquiler;
import edu.eci.pdsw.samples.services.ServiciosAlquilerFactory;
import edu.eci.pdsw.samples.services.impl.ServiciosAlquilerItemsStub;
import java.util.logging.Level;
import java.util.logging.Logger;
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
    
    @Test
    public void CF1Test() throws ExcepcionServiciosAlquiler{
        Item i1 = new Item(sa.consultarTipoItem(0), 44, "Los 4 Fantasticos", "Los 4 Fantásticos  es una película de superhéroes  basada en la serie de cómic homónima de Marvel.", java.sql.Date.valueOf("2005-06-08"), 2000, "DVD", "Ciencia Ficcion");        
        sa.registrarCliente(new Cliente("Juan Perez",3842,"24234","calle 123","aa@gmail.com"));
        sa.registrarItem(i1);
                
        Item item=sa.consultarItem(44);
        
        sa.registrarAlquilerCliente(java.sql.Date.valueOf("2005-12-20"), 3842, item, 5);
        
        assertEquals("No se calcula correctamente la multa (0) "
                + "cuando la devolucion se realiza el dia limite."
                ,0,sa.consultarMultaAlquiler(44, java.sql.Date.valueOf("2005-12-25")));
                
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
