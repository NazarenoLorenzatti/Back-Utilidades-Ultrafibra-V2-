package net.ultrafibra.cotrasenas.service.impl;

import java.nio.charset.StandardCharsets;
import java.io.IOException;
import java.sql.Date;
import java.text.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import lombok.extern.slf4j.Slf4j;
import net.ultrafibra.cotrasenas.dao.iBaseBanelcoDao;
import net.ultrafibra.cotrasenas.model.BaseHomebanking;
import net.ultrafibra.cotrasenas.response.BaseBanelcoResponseRest;
import net.ultrafibra.cotrasenas.response.ResponseRest;
import net.ultrafibra.cotrasenas.service.iBaseBanelcoService;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
public class BaseBanelcoServiceImpl implements iBaseBanelcoService {

    @Autowired
    private iBaseBanelcoDao banelcoDao;

    private double montoTotal;

    private DecimalFormat df = new DecimalFormat("#.00");

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<BaseBanelcoResponseRest> obtenerTabla() {
        BaseBanelcoResponseRest respuesta = new BaseBanelcoResponseRest();
        List<BaseHomebanking> baseList;
        try {
            baseList = banelcoDao.findAll();
            respuesta.getBaseBanelcoResponse().setBaseBanelco(baseList);
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
    public ResponseEntity<ResponseRest> eliminarTodo() {
        ResponseRest respuesta = new ResponseRest();
        try {
            banelcoDao.deleteAll();
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
    public ResponseEntity<ResponseRest> leerExcel(MultipartFile fileExcel) {
        ResponseRest respuesta = new ResponseRest();
        if (fileExcel != null) {
            try ( Workbook workbook = WorkbookFactory.create(fileExcel.getInputStream())) {
                Iterator<Sheet> sheetIterator = workbook.sheetIterator();

                while (sheetIterator.hasNext()) {
                    Sheet sheet = sheetIterator.next();
                    Iterator<Row> rowIterator = sheet.rowIterator();

                    // La primer fila Guarda los encabezados
                    Row headerRow = rowIterator.next();

                    while (rowIterator.hasNext()) {
                        Row row = rowIterator.next();

                        BaseHomebanking lineaBanelco = new BaseHomebanking();

                        for (int i = 0; i < headerRow.getLastCellNum(); i++) {
                            Cell headerCell = headerRow.getCell(i);
                            Cell dataCell = row.getCell(i);

                            if (headerCell != null && dataCell != null) {
                                String header = headerCell.getStringCellValue();
                                String dataString = null;
                                Long date = null;
                                double dataNumeric = 0.0;
                                if (dataCell.getCellType() == CellType.NUMERIC && !header.equals("Fecha vencimiento") && !header.equals("Fecha factura")) {
                                    dataNumeric = dataCell.getNumericCellValue();
                                } else if (dataCell.getCellType() == CellType.STRING) {
                                    dataString = dataCell.getStringCellValue();
                                } else {
                                    date = dataCell.getDateCellValue().getTime();
                                }

                                switch (header) {
                                    case "Empresa/ID":
                                        lineaBanelco.setIdCliente(String.valueOf(dataNumeric).replace(".00", ""));
                                        break;
                                    case "Empresa/Número de identificación principal":
                                        lineaBanelco.setDniCliente(dataString);
                                        break;
                                    case "Total":
                                        lineaBanelco.setTotalSinRecargo(dataNumeric);
                                        break;
                                    case "Total con recargo":
                                        lineaBanelco.setTotalConRecargo(dataNumeric);
                                        break;
                                    case "Fecha vencimiento":
                                        lineaBanelco.setFechaVencimiento(new Date(date));
                                        break;
                                    case "Nombre a mostrar":
                                        lineaBanelco.setNumeroFactura(dataString);
                                        break;
                                    case "Importe adeudado":
                                        lineaBanelco.setImporteAdeudado(dataNumeric);
                                        break;
                                    case "Fecha factura":
                                        lineaBanelco.setFechaFactura(new Date(date));
                                        break;
                                    case "Documento de Origen ":
                                        lineaBanelco.setDocumentoOrigen(dataString);
                                        break;
                                }
                            }
                        }
                        banelcoDao.save(lineaBanelco);
                    }
                    respuesta.setMetadata("Respuesta ok", "00", "Se subio correctamente el Archivo");
                }
            } catch (IOException ex) {
                Logger.getLogger(BaseBanelcoServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                respuesta.setMetadata("Respuesta nok", "-1", "Ocurrio un error al subir el archivo");
                return new ResponseEntity<>(respuesta, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            respuesta.setMetadata("Respuesta nok", "-1", "No se envio ningun Archivo");
            return new ResponseEntity<>(respuesta, HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(respuesta, HttpStatus.OK);
    }

    @Override
    @Transactional()
    public ResponseEntity<byte[]> downloadTxt(String nombreArchivo) throws IOException {

        // PRIMER FILA
        String fechaPrimerFila = DateTimeFormatter.ofPattern("yyyyMMdd").format(LocalDateTime.now());
        String baseDeDeuda = "0400SBHN" + fechaPrimerFila + cerosDerecha("", 264) + "\n";

        String filas = "";
        int cont = 0;

        // CUERPO DE BASE
        for (BaseHomebanking b : banelcoDao.findAll()) {
            if (b.getFechaVencimiento().toLocalDate().isAfter(b.getFechaVencimiento().toLocalDate().plusDays(60))) {
                continue;
            } else {
                cont += 1;
                filas += asignarEspacios("5" + b.getDniCliente(), 20) + asignarEspacios(b.getIdLinea().toString(), 20)
                        + asignarEspacios(montosXVencimientos(b), 96)
                        + asignarEspacios("FAC " + b.getFechaFactura().toLocalDate().getMonth().getDisplayName(TextStyle.FULL, Locale.forLanguageTag("es")) + " ULTRAFIBRA", 40).toUpperCase()
                        + asignarEspacios("FAC " + b.getFechaFactura().toLocalDate().getMonth().getDisplayName(TextStyle.FULL, Locale.forLanguageTag("es")).substring(0, 3), 15).toUpperCase()
                        + asignarEspacios(b.getNumeroFactura(), 60) + cerosDerecha("", 29) + "\n";
            }

        }
        String ultimaFila = "9400SBHN" + fechaPrimerFila + cerosIzquierda(String.valueOf(cont), 7) + "0000000"
                + cerosIzquierda(String.valueOf(df.format(this.montoTotal)).replace(",", ""), 16)
                + cerosDerecha("", 234);

        baseDeDeuda += filas;
        baseDeDeuda += ultimaFila;

        // Convertir el contenido a bytes
        byte[] contenidoBytes = baseDeDeuda.getBytes(StandardCharsets.UTF_8);

        // Configurar los encabezados de la respuesta
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_PLAIN);
        headers.setContentLength(contenidoBytes.length);
        headers.setContentDispositionFormData("attachment", nombreArchivo);

        return new ResponseEntity<>(contenidoBytes, headers, HttpStatus.OK);
    }

    private String asignarEspacios(String fila, int pos) {
        int x = pos - fila.length();
        for (int i = 1; i <= x; i++) {
            fila += " ";
        }
        return fila;
    }

    private String cerosDerecha(String fila, int pos) {
        int x = pos - fila.length();
        for (int i = 1; i <= x; i++) {
            fila += "0";
        }
        return fila;
    }

    private String cerosIzquierda(String fila, int pos) {
        int x = pos - fila.length();
        String ceros = "";
        for (int i = 1; i <= x; i++) {
            ceros += "0";
        }
        return ceros + fila;
    }

    private String montosXVencimientos(BaseHomebanking b) {
        String montosXVencimientos = "";
        LocalDate fechaVencimiento = b.getFechaVencimiento().toLocalDate();
        String strTotalSinRecargo = cerosIzquierda(String.valueOf(df.format(b.getTotalSinRecargo())).replace(",", ""), 11);
        String strTotalConRecargo = cerosIzquierda(String.valueOf(df.format(b.getTotalConRecargo())).replace(",", ""), 11);
        String strImporteAdeudado = cerosIzquierda(String.valueOf(df.format(b.getImporteAdeudado())).replace(",", ""), 11);

        if (b.getTotalSinRecargo() != b.getImporteAdeudado()) {
            if (LocalDate.now().isAfter(fechaVencimiento)) {
                this.montoTotal += b.getTotalConRecargo(); // TOTAL CON RECARGO
                montosXVencimientos += "0" + DateTimeFormatter.ofPattern("yyyyMMdd").format(fechaVencimiento.plusDays(5)) + strTotalConRecargo
                        + DateTimeFormatter.ofPattern("yyyyMMdd").format(fechaVencimiento.plusDays(20)) + strTotalConRecargo
                        + DateTimeFormatter.ofPattern("yyyyMMdd").format(fechaVencimiento.plusDays(45)) + strTotalConRecargo;

            } else {
                this.montoTotal += b.getTotalSinRecargo(); // TOTAL SIN RECARGO
                montosXVencimientos += "0" + DateTimeFormatter.ofPattern("yyyyMMdd").format(fechaVencimiento) + strTotalSinRecargo
                        + DateTimeFormatter.ofPattern("yyyyMMdd").format(fechaVencimiento.plusDays(20)) + strTotalSinRecargo
                        + DateTimeFormatter.ofPattern("yyyyMMdd").format(fechaVencimiento.plusDays(60)) + strTotalSinRecargo;
            }
        } else {
            if (LocalDate.now().isAfter(fechaVencimiento)) {
                this.montoTotal += b.getImporteAdeudado();
                montosXVencimientos += "0" + DateTimeFormatter.ofPattern("yyyyMMdd").format(fechaVencimiento.plusDays(5)) + strImporteAdeudado
                        + DateTimeFormatter.ofPattern("yyyyMMdd").format(fechaVencimiento.plusDays(20)) + strImporteAdeudado
                        + DateTimeFormatter.ofPattern("yyyyMMdd").format(fechaVencimiento.plusDays(45)) + strImporteAdeudado;
            } else {
                this.montoTotal += b.getImporteAdeudado();
                montosXVencimientos += "0" + DateTimeFormatter.ofPattern("yyyyMMdd").format(fechaVencimiento) + strImporteAdeudado
                        + DateTimeFormatter.ofPattern("yyyyMMdd").format(fechaVencimiento.plusDays(20)) + strImporteAdeudado
                        + DateTimeFormatter.ofPattern("yyyyMMdd").format(fechaVencimiento.plusDays(60)) + strImporteAdeudado;
            }

        }

        return montosXVencimientos + "0000000000000000000" + b.getDniCliente();
    }

}
