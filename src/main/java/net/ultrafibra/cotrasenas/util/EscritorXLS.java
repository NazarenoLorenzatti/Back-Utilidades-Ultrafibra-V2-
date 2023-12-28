package net.ultrafibra.cotrasenas.util;

import java.util.List;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class EscritorXLS {

    public EscritorXLS() {

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
