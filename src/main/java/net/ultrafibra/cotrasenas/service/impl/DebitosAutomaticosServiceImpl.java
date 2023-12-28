package net.ultrafibra.cotrasenas.service.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import static java.lang.String.valueOf;
import java.text.DecimalFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import lombok.extern.slf4j.Slf4j;
import net.ultrafibra.cotrasenas.dao.iDebitosAutomaticosDao;
import net.ultrafibra.cotrasenas.model.DebitosAutomaticos;
import net.ultrafibra.cotrasenas.response.DebitosAutomaticosResponseRest;
import net.ultrafibra.cotrasenas.response.ResponseRest;
import net.ultrafibra.cotrasenas.service.iDebitosAutomaticosService;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
public class DebitosAutomaticosServiceImpl implements iDebitosAutomaticosService {

    @Autowired
    private iDebitosAutomaticosDao debitosDao;

    private DecimalFormat df = new DecimalFormat("#.00");

    @Override
    @Transactional()
    public ResponseEntity<ResponseRest> eliminarTodo() {
        ResponseRest respuesta = new ResponseRest();
        try {
            debitosDao.deleteAll();
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

                        DebitosAutomaticos lineaDebito = new DebitosAutomaticos();

                        for (int i = 0; i < headerRow.getLastCellNum(); i++) {
                            Cell headerCell = headerRow.getCell(i);
                            Cell dataCell = row.getCell(i);

                            if (headerCell != null && dataCell != null) {
                                String header = headerCell.getStringCellValue();
                                String dataString = null;
                                double dataNumeric = 0.0;
                                if (dataCell.getCellType() == CellType.NUMERIC && !header.equals("Fecha vencimiento") && !header.equals("Fecha factura")) {
                                    dataNumeric = dataCell.getNumericCellValue();
                                } else if (dataCell.getCellType() == CellType.STRING) {
                                    dataString = dataCell.getStringCellValue();
                                } else {
                                    dataCell.getDateCellValue().getTime();
                                }

                                switch (header) {
                                    case "Empresa/Bancos/CBU":
                                        lineaDebito.setCbu(dataString);
                                        break;
                                    case "Empresa/ID":
                                        lineaDebito.setIdCliente(dataString);
                                        break;
                                    case "Empresa/Bancos/Nombre del titular de la cuenta":
                                        lineaDebito.setTitularCuenta(dataString);
                                        break;
                                    case "Empresa/Total a cobrar":
                                        lineaDebito.setTotalAdeudado(dataNumeric);
                                        break;
                                    case "Nombre a mostrar":
                                        lineaDebito.setNombreFactura(dataString);
                                        break;
                                }
                            }
                        }
                        debitosDao.save(lineaDebito);
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
    @Transactional(readOnly = true)
    public ResponseEntity<DebitosAutomaticosResponseRest> obtenerTabla() {
        DebitosAutomaticosResponseRest respuesta = new DebitosAutomaticosResponseRest();
        List<DebitosAutomaticos> debitosList;
        try {
            debitosList = debitosDao.findAll();
            respuesta.getDebitosAutomaticosResponse().setDebitosAutomaticos(debitosList);
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
    public ResponseEntity<byte[]> downloadXLS(String nombreArchivo, String tipoDebito, String fecha) throws IOException {
        List<List<String>> filas = new ArrayList();
        List<String> cabeceros = Arrays.asList("N°empresa sueldo", "Tipo_Banco", "Sucursal", "Tipo_cuenta",
                "Cuenta", "Id_adherente", "Id_debito", "Fecha_vto", "Moneda", "Importe");

        for (DebitosAutomaticos d : debitosDao.findAll()) {
            List<String> linea = new ArrayList();

            linea.add("00000");
            if (tipoDebito.equals("Abierto")) {
                linea.add(d.getCbu().substring(0, 3));
                linea.add("0100");
                linea.add("0");
                linea.add(d.getCbu());
                if (d.getCbu().substring(0, 3).equals("072")) { // BANCO SANTANDER
                    linea.add(cerosIzquierda(d.getIdCliente(), 5));
                } else if (d.getCbu().substring(0, 3).equals("007")) { // BANCO GALICIA
                    linea.add(cerosIzquierda(d.getIdCliente(), 6));
                } else {
                    linea.add(d.getIdCliente());
                }
            } else {
                linea.add("285");
                linea.add(d.getCbu().substring(3, 7));

                switch (d.getCbu().substring(0, 1)) {
                    case "3" ->
                        linea.add("3");
                    case "4" ->
                        linea.add("4");
                    default ->
                        linea.add("0");
                }
                linea.add(d.getCbu());
                linea.add(d.getIdCliente());
            }
            linea.add(d.getTitularCuenta());
            linea.add(fecha);
            linea.add("080");
            linea.add(this.df.format(d.getTotalAdeudado()).replace(",", ""));
            filas.add(linea);
        }

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            exportar(filas, cabeceros).write(outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=debitos.xlsx");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(outputStream.toByteArray());
    }

    private String cerosIzquierda(String fila, int pos) {
        String filaRet = null;
        char[] arrayFila = fila.toCharArray();
        char[] filaArray = new char[pos];

        for (int i = 0; i < filaArray.length; i++) {
            filaArray[i] = '0';
        }

        int y = pos - arrayFila.length;

        for (char c : arrayFila) {
            filaArray[y] = c;
            y++;
        }
        filaRet = valueOf(filaArray);
        return filaRet;
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
