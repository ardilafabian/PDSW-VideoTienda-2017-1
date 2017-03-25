/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.pdsw.sampleprj.dao.mybatis;

import com.google.inject.Inject;
import edu.eci.pdsw.sampleprj.dao.PersistenceException;
import edu.eci.pdsw.sampleprj.dao.TipoItemDAO;
import edu.eci.pdsw.sampleprj.dao.mybatis.mappers.TipoItemMapper;
import edu.eci.pdsw.samples.entities.TipoItem;
import java.util.List;

/**
 *
 * @author skinman95
 */
public class MyBATISTipoItemDAO implements TipoItemDAO{

    @Inject
    private TipoItemMapper tipoItemMapper;
    
    @Override
    public TipoItem load(int id) throws PersistenceException {
        return tipoItemMapper.getTipoItem(id);
    }

    @Override
    public List<TipoItem> loadTipos() throws PersistenceException {
        return tipoItemMapper.getTiposItems();
    }

    @Override
    public void save(TipoItem ti) throws PersistenceException {
        tipoItemMapper.addTipoItem(ti.getDescripcion());
    }
    
}
