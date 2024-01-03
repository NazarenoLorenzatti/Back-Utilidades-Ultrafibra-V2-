package net.ultrafibra.utilidades.service.impl;

import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import net.ultrafibra.utilidades.dao.iDatasetsDao;
import net.ultrafibra.utilidades.model.Datasets;
import net.ultrafibra.utilidades.model.Host;
import net.ultrafibra.utilidades.response.DatasetsResponseRest;
import net.ultrafibra.utilidades.service.iDatasetsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class DatasetsServiceImpl implements iDatasetsService {

    @Autowired
    private iDatasetsDao dataDao;

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<DatasetsResponseRest> listarData() {
        DatasetsResponseRest respuesta = new DatasetsResponseRest();
        try {
            List<Datasets> listaDatasets = dataDao.findAll();
            respuesta.getDatasetsResponse().setDatasets(listaDatasets);
            respuesta.setMetadata("Respuesta ok", "00", "Lista de datos del host");
        } catch (Exception e) {
            respuesta.setMetadata("Respuesta nok", "-1", "Error al consultar");
            e.getStackTrace();
            return new ResponseEntity<>(respuesta, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(respuesta, HttpStatus.OK);
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<DatasetsResponseRest> listarDataHost(Host host) {
        DatasetsResponseRest respuesta = new DatasetsResponseRest();
        try {
            if (host != null) {
                List<Datasets> listaDatasets = dataDao.findByHost(host);
                respuesta.getDatasetsResponse().setDatasets(listaDatasets);
                respuesta.setMetadata("Respuesta ok", "00", "Lista de datos del host");
            } else {
                respuesta.setMetadata("Respuesta nok", "-1", "El host enviado es Null");
                return new ResponseEntity<>(respuesta, HttpStatus.NO_CONTENT);
            }

        } catch (Exception e) {
            respuesta.setMetadata("Respuesta nok", "-1", "Error al consultar");
            e.getStackTrace();
            return new ResponseEntity<>(respuesta, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(respuesta, HttpStatus.OK);
    }

    @Override
    @Transactional()
    public ResponseEntity<DatasetsResponseRest> registrarData(Datasets data) {
        DatasetsResponseRest respuesta = new DatasetsResponseRest();
        List<Datasets> listaDatasets = new ArrayList<>();
        try {
            if (data != null) {
                Datasets dataGuardada = dataDao.save(data);
                if (dataGuardada != null) {
                    listaDatasets.add(dataGuardada);
                    respuesta.getDatasetsResponse().setDatasets(listaDatasets);
                    respuesta.setMetadata("Respuesta ok", "00", "Data registrada");
                } else {
                    respuesta.setMetadata("Respuesta nok", "-1", "No se guardo la data");
                    return new ResponseEntity<>(respuesta, HttpStatus.BAD_REQUEST);
                }

            } else {
                respuesta.setMetadata("Respuesta nok", "-1", "La data Enviada es Null");
                return new ResponseEntity<>(respuesta, HttpStatus.NO_CONTENT);
            }

        } catch (Exception e) {
            respuesta.setMetadata("Respuesta nok", "-1", "Error al consultar");
            e.getStackTrace();
            return new ResponseEntity<>(respuesta, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(respuesta, HttpStatus.OK);
    }

    @Override
    @Transactional()
    public ResponseEntity<DatasetsResponseRest> eliminarData(Long idData) {
        DatasetsResponseRest respuesta = new DatasetsResponseRest();
        try {
            if (idData != null) {
                dataDao.deleteById(idData);
                respuesta.setMetadata("Respuesta ok", "00", "dato eliminado");
            } else {
                respuesta.setMetadata("Respuesta nok", "-1", "El id del dato enviado es Null");
                return new ResponseEntity<>(respuesta, HttpStatus.NO_CONTENT);
            }

        } catch (Exception e) {
            respuesta.setMetadata("Respuesta nok", "-1", "Error al consultar");
            e.getStackTrace();
            return new ResponseEntity<>(respuesta, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(respuesta, HttpStatus.OK);
    }

}
