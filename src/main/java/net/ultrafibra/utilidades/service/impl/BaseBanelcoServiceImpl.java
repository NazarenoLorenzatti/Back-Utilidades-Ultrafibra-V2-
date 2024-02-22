package net.ultrafibra.utilidades.service.impl;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.List;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.nio.charset.StandardCharsets;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.sql.Date;
import java.text.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import lombok.extern.slf4j.Slf4j;
import net.ultrafibra.utilidades.model.BaseHomebanking;
import net.ultrafibra.utilidades.response.BaseBanelcoResponseRest;
import net.ultrafibra.utilidades.response.ResponseRest;
import net.ultrafibra.utilidades.service.iBaseBanelcoService;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import net.ultrafibra.utilidades.dao.iHomebankingDao;
import net.ultrafibra.utilidades.model.Item;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

@Slf4j
@Service
public class BaseBanelcoServiceImpl implements iBaseBanelcoService {

    @Autowired
    private iHomebankingDao banelcoDao;

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
            banelcoDao.reiniciarIndices();
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
                                        lineaBanelco.setIdCliente(dataString);
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
                                    case "Documento origen":
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
    public ResponseEntity<ResponseRest> leerExcelRespuesta(MultipartFile fileExcel) {
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
                                SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm'hs'");

                                double dataNumeric = 0.0;
                                if (dataCell.getCellType() == CellType.NUMERIC) {
                                    dataNumeric = dataCell.getNumericCellValue();
                                } else if (dataCell.getCellType() == CellType.STRING) {
                                    dataString = dataCell.getStringCellValue();
                                }

                                switch (header) {
                                    case "Monto":
                                        lineaBanelco.setTotalSinRecargo(dataNumeric);
                                        break;
                                    case "Fecha Pago":
                                        java.util.Date parsed = format.parse(dataString);
                                        Date sqlDate = new Date(parsed.getTime());
                                        lineaBanelco.setFechaVencimiento(sqlDate);
                                        break;
                                    case "Informacion":
                                        Gson gson = new Gson();
                                        Type itemListType = new TypeToken<List<Item>>() {
                                        }.getType();
                                        List<Item> itemList = gson.fromJson(dataString, itemListType);
                                        for (Item item : itemList) {
                                            String clave1 = item.getClave1();
                                            lineaBanelco.setDniCliente(clave1);
                                        }
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
            } catch (ParseException ex) {
                Logger.getLogger(BaseBanelcoServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            respuesta.setMetadata("Respuesta nok", "-1", "No se envio ningun Archivo");
            return new ResponseEntity<>(respuesta, HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(respuesta, HttpStatus.OK);
    }

    // PRUEBA CON EXCEL
    @Override
    @Transactional()
    public ResponseEntity<byte[]> downloadXLS(String nombreArchivo) throws IOException {
        List<List<String>> filas = new ArrayList();
        List<String> cabeceros = Arrays.asList("Monto", "Fecha de Pago", "Etiqueta", "Empresa");
        SimpleDateFormat format = new SimpleDateFormat("dd/M/yyyy");

        for (BaseHomebanking b : banelcoDao.findAll()) {
            List<String> linea = new ArrayList();
            linea.add((this.df.format(b.getTotalSinRecargo())));
            linea.add(format.format(b.getFechaVencimiento()));
            linea.add("MacroClick");
            linea.add(b.getDniCliente());
            filas.add(linea);
        }

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            exportar(filas, cabeceros).write(outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=Respuesta Macroclick.xlsx");
        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(outputStream.toByteArray());
    }

    // PRUEBA CON CSV
    @Override
    @Transactional()
    public ResponseEntity<byte[]> downloadCsv(String nombreArchivo) throws IOException {
        SimpleDateFormat format = new SimpleDateFormat("dd/M/yyyy");

        try ( ByteArrayOutputStream outputStream = new ByteArrayOutputStream();  BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8))) {

            writer.write("Monto;Fecha de Pago;Etiqueta;Empresa\n");

            for (BaseHomebanking b : banelcoDao.findAll()) {
                writer.write(this.df.format(b.getTotalSinRecargo()) + ";" + format.format(b.getFechaVencimiento()) + ";MacroClick;" + b.getDniCliente() + "\n");
            }

            writer.flush(); // Flush para asegurar que todos los datos sean escritos
            byte[] bytes = outputStream.toByteArray();

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + nombreArchivo + ".csv");

            return ResponseEntity.ok()
                    .headers(headers)
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(bytes);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.notFound().build();
        }
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

                String motivo = "";
                if (b.getDocumentoOrigen().contains("SO")) {
                    motivo = "Monto por unica Vez";
                } else {
                    motivo = "FAC Mensual " + b.getFechaFactura().toLocalDate().getMonth().getDisplayName(TextStyle.FULL, Locale.forLanguageTag("es")) + " ULTRAFIBRA";
                }

                cont += 1;
                filas += asignarEspacios("5" + b.getDniCliente(), 20) + asignarEspacios(b.getIdLinea().toString(), 20)
                        + asignarEspacios(montosXVencimientos(b), 96)
                        + asignarEspacios(motivo, 40).toUpperCase()
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

        if (b.getTotalSinRecargo() == b.getImporteAdeudado()) {
            if (LocalDate.now().isAfter(fechaVencimiento)) {
                if (b.getTotalConRecargo() != 0) {
                    this.montoTotal += b.getTotalConRecargo(); // TOTAL CON RECARGO
                    montosXVencimientos += "0" + DateTimeFormatter.ofPattern("yyyyMMdd").format(fechaVencimiento.plusDays(5)) + strTotalConRecargo
                            + DateTimeFormatter.ofPattern("yyyyMMdd").format(fechaVencimiento.plusDays(20)) + strTotalConRecargo
                            + DateTimeFormatter.ofPattern("yyyyMMdd").format(fechaVencimiento.plusDays(45)) + strTotalConRecargo;
                } else {
                    this.montoTotal += b.getTotalSinRecargo(); // TOTAL CON RECARGO
                    montosXVencimientos += "0" + DateTimeFormatter.ofPattern("yyyyMMdd").format(fechaVencimiento.plusDays(5)) + strTotalSinRecargo
                            + DateTimeFormatter.ofPattern("yyyyMMdd").format(fechaVencimiento.plusDays(20)) + strTotalSinRecargo
                            + DateTimeFormatter.ofPattern("yyyyMMdd").format(fechaVencimiento.plusDays(45)) + strTotalSinRecargo;
                }

            } else {
                if (b.getTotalConRecargo() != 0) {
                    this.montoTotal += b.getTotalSinRecargo(); // TOTAL SIN RECARGO
                    montosXVencimientos += "0" + DateTimeFormatter.ofPattern("yyyyMMdd").format(fechaVencimiento) + strTotalSinRecargo
                            + DateTimeFormatter.ofPattern("yyyyMMdd").format(fechaVencimiento.plusDays(20)) + strTotalConRecargo
                            + DateTimeFormatter.ofPattern("yyyyMMdd").format(fechaVencimiento.plusDays(60)) + strTotalConRecargo;
                } else {
                    this.montoTotal += b.getTotalSinRecargo(); // TOTAL SIN RECARGO
                    montosXVencimientos += "0" + DateTimeFormatter.ofPattern("yyyyMMdd").format(fechaVencimiento) + strTotalSinRecargo
                            + DateTimeFormatter.ofPattern("yyyyMMdd").format(fechaVencimiento.plusDays(20)) + strTotalSinRecargo
                            + DateTimeFormatter.ofPattern("yyyyMMdd").format(fechaVencimiento.plusDays(60)) + strTotalSinRecargo;
                }

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

    public Workbook exportar(List<List<String>> filas, List<String> cabeceros) {

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Hoja1");

        int index = 0;
        //Headers
        Row headerRow = sheet.createRow(index);
        for (String c : cabeceros) {
            headerRow.createCell(index).setCellValue(cabeceros.get(index));
            index++;
        }

        for (int i = 0; i < filas.size(); i++) {
            List<String> fila = filas.get(i);
            Row dataRow = sheet.createRow(i + 1);
            int ind = 0;
            for (String celda : fila) {
                dataRow.createCell(ind).setCellValue(celda);
                ind++;
            }
        }

        return workbook;
    }

}
