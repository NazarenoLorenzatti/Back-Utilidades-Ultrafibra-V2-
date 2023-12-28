package net.ultrafibra.cotrasenas.service.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import static java.lang.String.valueOf;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import net.ultrafibra.cotrasenas.model.BaseHomebanking;
import net.ultrafibra.cotrasenas.service.iBaseLinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import net.ultrafibra.cotrasenas.dao.iHomebankingDao;

@Slf4j
@Service
public class BaseLinkServiceImpl implements iBaseLinkService {
    
    @Autowired
    private iHomebankingDao banelcoDao;
    
    private LocalDate ultFecVto;
    private String ultimaFecha;
    private String fechaPrimerVto;
    private String fechaSegundoVto;
    private double montoTotalPrimerVto, montoTotalSegundoVto;
    private String montoPrimerVto, montoSegundoVto;
    private int filasTotales;
    private double montoTotal;
    
    private DecimalFormat df = new DecimalFormat("#.00");

    // Descargar archivo de proceso
    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<byte[]> downloadTxt(String nombreArchivo) throws IOException {
        int contFilas = 2;

        // PRIMER FILA
        String fechaPrimerFila = DateTimeFormatter.ofPattern("yyMMdd").format(LocalDateTime.now());
        String baseDeDeuda = asignarEspacios("HRFACTURACIONGK0" + fechaPrimerFila + "00001", 131) + "\n";
        
        ultFecVto = LocalDate.now();

        // CUERPO
        //Proceso para tomar solo un ID de cliente en caso de que hayan Duplicados
        List<String> listaConDuplicados = new ArrayList<>();
        for (BaseHomebanking b : banelcoDao.findAll()) {
            listaConDuplicados.add(b.getIdCliente());
        }        
        List<String> listaSinDuplicados = listaConDuplicados.stream().distinct().collect(Collectors.toList());
        
        // Procesamiento de la informacion en Base al Id del cliente
        for (String i : listaSinDuplicados) {
            int contDeudas = -1; 
            for (BaseHomebanking b : banelcoDao.findAllByIdCliente(i)) {                          
                
                if (b.getFechaVencimiento().toLocalDate().isAfter(b.getFechaVencimiento().toLocalDate().plusDays(60))) {
                    continue;
                } else {
                    establecerFechas(b.getFechaVencimiento());
                    establecerMontoPrimerVto(b.getImporteAdeudado(), b.getTotalConRecargo(), b.getTotalSinRecargo());
                    establecerMontoSegundoVto(b.getTotalConRecargo(), b.getImporteAdeudado());
                    contDeudas += 1;
                    
                    String fila = "";
                    fila += contDeudas + DateTimeFormatter.ofPattern("MMyy").format(LocalDateTime.now())
                            + asignarConcepto(b.getDocumentoOrigen(), b.getTotalSinRecargo())
                            + cerosIzquierda(b.getIdCliente(), 8) + "           ";
                    
                    contFilas += 1;
                    fila += fechaPrimerVto + montoPrimerVto + fechaSegundoVto + montoSegundoVto + "000000000000000000"
                            + b.getNumeroFactura().substring(3, 18).replace("-", " ") + " " + b.getDocumentoOrigen();
                    fila = asignarEspacios(fila, 131);
                    baseDeDeuda += fila + "\n";
                }
                
            }
        }
        
        String ultimaFila = "TRFACTURACION" + cerosIzquierda(String.valueOf(contFilas), 8)
                + cerosIzquierda(String.valueOf(df.format(montoTotalPrimerVto)).replace(",", ""), 18)
                + cerosIzquierda(String.valueOf(df.format(montoTotalSegundoVto)).replace(",", ""), 18) + "000000000000000000";
        ultimaFila = asignarEspacios(ultimaFila, 131) + "\n";
        
        baseDeDeuda += ultimaFila;
        
        this.filasTotales = contFilas;

        // Convertir el contenido a bytes
        byte[] contenidoProceso = baseDeDeuda.getBytes(StandardCharsets.UTF_8);
        byte[] contenidoControl = downloadTxtControl(nombreArchivo);

        // Crea un ByteArrayOutputStream para almacenar los bytes del archivo ZIP
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try ( ZipOutputStream zipOut = new ZipOutputStream(baos)) {
            // Agrega el primer archivo al ZIP
            zipOut.putNextEntry(new ZipEntry(nombreArchivo));
            zipOut.write(contenidoProceso);
            zipOut.closeEntry();

            // Agrega el segundo archivo al ZIP
            zipOut.putNextEntry(new ZipEntry(nombreArchivo.replace("P", "C")));
            zipOut.write(contenidoControl);
            zipOut.closeEntry();
        }

        // Configura el encabezado para indicar que es un archivo ZIP
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", "archivos_combinados.zip");

        // Devuelve los bytes del archivo ZIP y el encabezado en la respuesta
        return new ResponseEntity<>(baos.toByteArray(), headers, org.springframework.http.HttpStatus.OK);
    }

    // Descargar archivo de control
    public byte[] downloadTxtControl(String nombreArchivo) throws IOException {
        String fechaStr = DateTimeFormatter.ofPattern("yyyyMMdd").format(LocalDateTime.now());
        String registros = valueOf(this.filasTotales * 133);
        String fila1 = "HRPASCTRL" + fechaStr + "GK0" + nombreArchivo + cerosIzquierda(registros, 10);
        fila1 = asignarEspacios(fila1, 75) + "\n";
        String fila2 = "LOTES" + "00001" + cerosIzquierda(valueOf(this.filasTotales), 8)
                + cerosIzquierda(String.valueOf(df.format(montoTotalPrimerVto)).replace(",", ""), 18)
                + cerosIzquierda(String.valueOf(df.format(montoTotalSegundoVto)).replace(",", ""), 18) + "000000000000000000";
        fila2 = asignarEspacios(fila2, 75) + "\n";
        String fila3 = "FINAL" + cerosIzquierda(valueOf(this.filasTotales), 8) + cerosIzquierda(String.valueOf(df.format(montoTotalPrimerVto)).replace(",", ""), 18)
                + cerosIzquierda(String.valueOf(df.format(montoTotalSegundoVto)).replace(",", ""), 18) + "000000000000000000" + ultimaFecha;
        fila3 = asignarEspacios(fila3, 75) + "\n";
        String archivoControl = fila1 + fila2 + fila3;

        // Convertir el contenido a bytes
        byte[] contenidoBytes = archivoControl.getBytes(StandardCharsets.UTF_8);
        return contenidoBytes;
    }
    
    private String asignarEspacios(String fila, int pos) {
        int x = pos - fila.length();
        for (int i = 1; i <= x; i++) {
            fila += " ";
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
    
    private String asignarConcepto(String documentoOrigen, double monto) {
        String ret = "004";
        if (documentoOrigen.contains("SUB")) {
            ret = "001"; // Facturas de abono
        } else if (documentoOrigen.contains("SO") && monto == 3500 || monto == 4500 || monto == 15000) {
            ret = "003"; // Facturas Instalacion
        } else if (documentoOrigen.contains("SO")) {
            ret = "002"; // Facturas de proporcional
        }
        return ret;
    }
    
    private void establecerFechas(Date fecha) {
        LocalDate fechaHoy = LocalDate.now();
        LocalDate fechaVto = fecha.toLocalDate();
        
        if (fechaVto.isBefore(fechaHoy)) {
            fechaPrimerVto = DateTimeFormatter.ofPattern("yyMMdd").format(fechaHoy.plusDays(2));
            fechaSegundoVto = DateTimeFormatter.ofPattern("yyMMdd").format(fechaHoy.plusDays(5));
        } else {
            fechaPrimerVto = DateTimeFormatter.ofPattern("yyMMdd").format(fechaVto);
            fechaSegundoVto = DateTimeFormatter.ofPattern("yyMMdd").format(fechaVto.plusDays(10));
        }
        
        if (fechaVto.plusDays(15).isAfter(this.ultFecVto)) {
            this.ultFecVto = fechaVto.plusDays(15);
        }
        this.ultimaFecha = DateTimeFormatter.ofPattern("yyyyMMdd").format(ultFecVto);
        
    }
    
    private void establecerMontoPrimerVto(double totalAdeudado, double totalConRecargo, double totalSinRecargo) {
        if (totalAdeudado == totalSinRecargo) {
            this.montoTotalPrimerVto += totalSinRecargo;
            montoPrimerVto = cerosIzquierda(String.valueOf(totalSinRecargo).replace(".", ""), 12);
        } else {
            if (totalAdeudado != totalConRecargo) {
                this.montoTotalPrimerVto += totalAdeudado;
                montoPrimerVto = cerosIzquierda(String.valueOf(totalAdeudado).replace(".", ""), 12);
            } else {
                this.montoTotalPrimerVto += totalSinRecargo;
                montoPrimerVto = cerosIzquierda(String.valueOf(totalSinRecargo).replace(".", ""), 12);
            }
        }
    }
    
    private void establecerMontoSegundoVto(double totalConRecargo, double importeAdeudado) {
        if (totalConRecargo != 0.0) {
            this.montoTotalSegundoVto += totalConRecargo;
            montoSegundoVto = cerosIzquierda(String.valueOf(totalConRecargo).replace(".", ""), 12);
        } else {
            this.montoTotalSegundoVto += importeAdeudado;
            montoSegundoVto = montoPrimerVto; // si es igual a 0 el primer vencimiento no tiene re cargo y es igual al monto del primer vencimiento
        }
        
    }
    
}
