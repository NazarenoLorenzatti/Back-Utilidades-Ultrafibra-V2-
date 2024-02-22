package net.ultrafibra.utilidades.service.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Date;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import lombok.extern.slf4j.Slf4j;
import net.ultrafibra.utilidades.dao.iRespuestaDebitosDao;
import net.ultrafibra.utilidades.model.RespuestaDebitos;
import net.ultrafibra.utilidades.response.ResponseRest;
import net.ultrafibra.utilidades.response.RespuestaDebitosResponseRest;
import net.ultrafibra.utilidades.service.iRespuestaDebitosService;
import net.ultrafibra.utilidades.util.EscritorXLS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
public class RespuestaDebitosServiceImpl extends EscritorXLS implements iRespuestaDebitosService {

    @Autowired
    private iRespuestaDebitosDao respuestaDebitosDao;

    private final DecimalFormat df = new DecimalFormat("#,##0.00");
    private final SimpleDateFormat formatoEntrada = new SimpleDateFormat("yyyyMMdd");

    @Override
    @Transactional()
    public ResponseEntity<ResponseRest> eliminarTodo() {
        ResponseRest respuesta = new ResponseRest();
        try {
            respuestaDebitosDao.deleteAll();
            respuesta.setMetadata("Respuesta ok", "00", "Eliminadas todas las celdas");
        } catch (Exception e) {
            respuesta.setMetadata("Respuesta nok", "-1", "Error al intentar eliminar");
            e.getStackTrace();
            return new ResponseEntity<>(respuesta, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(respuesta, HttpStatus.OK);
    }

    @Override
    @Transactional()
    public ResponseEntity<byte[]> downloadXLS() throws IOException {
        List<List<String>> filas = new ArrayList();
        List<String> cabeceros = Arrays.asList("Id cliente", "Referencia", "Fecha de Pago", "Etiqueta",
                "Monto", "Motivo");

        for (RespuestaDebitos r : respuestaDebitosDao.findAll()) {
            List<String> linea = new ArrayList();
            linea.add(r.getIdCliente());
            linea.add(r.getIdCliente());
            linea.add(r.getFechaDePago().toString());
            linea.add(r.getEtiqueta());
            linea.add(df.format(r.getTotalPagado()));
            linea.add(r.getMotivo());          
            filas.add(linea);
        }

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            exportar(filas, cabeceros).write(outputStream);
        } catch (IOException e) {
        }
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=Respuesta.xlsx");
        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(outputStream.toByteArray());
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<RespuestaDebitosResponseRest> obtenerTabla() {
        RespuestaDebitosResponseRest respuesta = new RespuestaDebitosResponseRest();
        List<RespuestaDebitos> respuestaList;
        try {
            respuestaList = respuestaDebitosDao.findAll();
            respuesta.getRespuestaDebitosResponse().setRespuestaDebitos(respuestaList);
            respuesta.setMetadata("Respuesta ok", "00", "Obtenido todas las celdas");
        } catch (Exception e) {
            respuesta.setMetadata("Respuesta nok", "-1", "Error al intentar obtener las celdas cargadas");
            e.getStackTrace();
            return new ResponseEntity<>(respuesta, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(respuesta, HttpStatus.OK);
    }

    @Override
    @Transactional()
    public ResponseEntity<ResponseRest> leerTxt(MultipartFile fileExcel) {
        ResponseRest respuesta = new ResponseRest();
        if (fileExcel != null) {
            try {
                Scanner scanner = new Scanner(fileExcel.getInputStream());
                if (scanner.hasNextLine()) {
                    scanner.nextLine();
                }
                while (scanner.hasNextLine()) {
                    String filaStr = scanner.nextLine();
                    String debito = filaStr.substring(1, 6);
                    String id = filaStr.substring(44, 63);
                    String monto = filaStr.substring(97, 111);
                    String motivo = filaStr.substring(83, 86);
                    String fechaPago = null;

                    if (debito.equals("89344")) {
                         fechaPago = filaStr.substring(111, 119);
                    } else if (debito.equals("89760")) {
                        fechaPago = filaStr.substring(87, 95);
                    }

                    motivo = switch (motivo) {
                        case "R02" ->
                            "CUENTA CERRADA/SUSPENDIDA";
                        case "R03" ->
                            "CUENTA INEXISTENTE";
                        case "R04" ->
                            "NUMERO DE CUENTA INVALIDO";
                        case "R08" ->
                            "ORDEN DE NO PAGAR";
                        case "R10" ->
                            "FALTA DE FONDOS";
                        case "R14" ->
                            "IDENTIFICACION DEL CLIENTE EN LA EMPRESA ERRONEA";
                        case "R15" ->
                            "BAJA DE SERVICIO";
                        case "R17" ->
                            "ERROR DE FORMATO";
                        case "R19" ->
                            "IMPORTE ERRONEO";
                        case "R20" ->
                            "MONEDA DISTINTA A LA CUENTA DE DÉBITO";
                        case "R23" ->
                            "SUCURSAL NO HABILITADA";
                        case "R24" ->
                            "TRANSACCION DUPLICADA";
                        case "R26" ->
                            "ERROR POR CAMPO MANDATORIO";
                        default ->
                            "APLICADO";
                    };

                    Date fecha = new Date(formatoEntrada.parse(fechaPago).getTime());
                    double valorNumerico = Double.parseDouble(monto) / 100.0;
                    
                    RespuestaDebitos r = new RespuestaDebitos();
                    r.setIdCliente(id);
                    r.setTotalPagado(valorNumerico);
                    r.setEtiqueta(debito);
                    r.setFechaDePago(fecha);
                    r.setMotivo(motivo);
                    respuestaDebitosDao.save(r);                    
                }
                respuesta.setMetadata("Respuesta ok", "00", "Se guardo la respuesta correctamente");
                scanner.close();

            } catch (IOException | ParseException ex) {
                Logger.getLogger(RespuestaDebitosServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                respuesta.setMetadata("Respuesta nok", "-1", "Ocurrio un error al subir el archivo");
                return new ResponseEntity<>(respuesta, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            respuesta.setMetadata("Respuesta nok", "-1", "No se envio ningun Archivo");
            return new ResponseEntity<>(respuesta, HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(respuesta, HttpStatus.OK);
    }
    
    

}
