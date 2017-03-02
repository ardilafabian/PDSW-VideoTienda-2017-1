/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.pdsw.samples.tests;

import edu.eci.pdsw.samples.entities.Cliente;
import edu.eci.pdsw.samples.services.ExcepcionServiciosAlquiler;
import edu.eci.pdsw.samples.services.ServiciosAlquiler;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * 
 */
public class ClientesTest {

    /**
     * Clases de Equivalencia: metodo registrarCliente de ServiciosAlquiler
     * CE1: Cliente, donde es la primera vez que se agrega TIPO: Normal. Resultado Esperado: se agrega el cliente
     * CE2: Cliente, donde ya fue agregado previamente TIPO: Error. Resultado Esperado: ExcepcionServiciosAlquiler
     */
    
    public ClientesTest() {
    }
    
    @Before
    public void setUp() {
    }
    
    /**
     * 
     * @throws ExcepcionServiciosAlquiler 
     */
    @Test(expected=ExcepcionServiciosAlquiler.class)
    public void CE2Test() throws ExcepcionServiciosAlquiler{
    	Cliente c = new Cliente("Alejandro Anzola Avila", 1019115165, "304 602 3862",
        "Suba Pinar Calle XXX A No YY - ZZ Casa 333", "alejandro.anzola@mail.escuelaing.edu.co");
        
        ServiciosAlquiler sa = ServiciosAlquiler.getInstance();
        
        sa.registrarCliente(c);
        
        sa.registrarCliente(c);
        
        fail("El servicio de alquiler no lanzo excepcion ExcepcionServiciosAlquiler");
    }
    
    
    
    
    
    
    
}
