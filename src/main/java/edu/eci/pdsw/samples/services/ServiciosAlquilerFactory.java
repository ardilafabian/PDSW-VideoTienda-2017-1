/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.pdsw.samples.services;

import edu.eci.pdsw.samples.services.impl.ServiciosAlquilerItemsImpl;
import org.mybatis.guice.XMLMyBatisModule;
import org.mybatis.guice.datasource.helper.JdbcHelper;

import static com.google.inject.Guice.createInjector;
import com.google.inject.Injector;
import edu.eci.pdsw.sampleprj.dao.ClienteDAO;
import edu.eci.pdsw.sampleprj.dao.ItemDAO;
import edu.eci.pdsw.sampleprj.dao.mybatis.MyBATISItemDAO;
import edu.eci.pdsw.sampleprj.dao.mybatis.MyBATISClienteDAO;

/**
 *
 * @author hcadavid
 */
public class ServiciosAlquilerFactory {

    private static ServiciosAlquilerFactory instance = new ServiciosAlquilerFactory();
    
    private static final boolean TESTING = true;
    
    private static Injector injector;
    
    private static Injector testInjector;
    
    private ServiciosAlquilerFactory(){
        
        injector = createInjector(new XMLMyBatisModule() {

                    @Override
                    protected void initialize() {
                        install(JdbcHelper.MySQL);                        
                        setClassPathResource("mybatis-config.xml");                        
                        bind(ServiciosAlquiler.class).to(ServiciosAlquilerItemsImpl.class);
                        bind(ItemDAO.class).to(MyBATISItemDAO.class);
                        bind(ClienteDAO.class).to(MyBATISClienteDAO.class);
                    }

                }
                
        );

        testInjector = createInjector(new XMLMyBatisModule() {

                    @Override
                    protected void initialize() {
                        install(JdbcHelper.MySQL);                        
                        setClassPathResource("mybatis-config-h2.xml");                        
                        bind(ServiciosAlquiler.class).to(ServiciosAlquilerItemsImpl.class);
                        bind(ItemDAO.class).to(MyBATISItemDAO.class);
                        bind(ClienteDAO.class).to(MyBATISClienteDAO.class);
                    }

                }
                
        );

        
    }

    public ServiciosAlquiler getServiciosAlquiler(){
        return TESTING ? getServiciosAlquilerTesting() : injector.getInstance(ServiciosAlquiler.class);   
    }


    public ServiciosAlquiler getServiciosAlquilerTesting(){
        return testInjector.getInstance(ServiciosAlquiler.class);   
    }


    
    public static ServiciosAlquilerFactory getInstance(){
        return instance;
    }
    
}
