package Classes;

import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import com.sun.xml.internal.ws.api.streaming.XMLStreamReaderFactory;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

public class Excel {
    Map<String,Map<String,Map<String,Map<String,ArrayList<Factura>>>>> data;
    public Excel() {
        data = new TreeMap<>();
    }
    public void resetData(){
        data = new TreeMap<>();
    }
    public void readExcel(String path, String indicador){
        InputStream excelStream = null;
        try {
            excelStream = new FileInputStream(path);
            HSSFWorkbook Workbook = new HSSFWorkbook(excelStream);
            HSSFSheet sheet = Workbook.getSheetAt(0);
            HSSFRow row;
            HSSFCell cell;

            if(sheet!=null){
                int maxRow = sheet.getLastRowNum();
                ArrayList<String> fila = new ArrayList<>();
                int maxCol = 0;

                String currentNombre = "";
                String currentTitulo1 = "";
                String currentTitulo2 = "";
                Factura currentFactura;
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                for(int r=0;r < maxRow; r++){
                    row = sheet.getRow(r);
                    if(row!=null){
                        maxCol = row.getLastCellNum();
                        for(int c=0;c < maxCol; c++){
                            cell = row.getCell(c);
                            String txt="";
                            if(cell!=null){
                                switch (cell.getCellTypeEnum()){
                                    case STRING:
                                        txt=cell.getStringCellValue();
                                        break;
                                    case NUMERIC:
                                        if (DateUtil.isCellDateFormatted(cell)){
                                            String pattern = "dd/MM/yyyy";
                                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

                                            txt = simpleDateFormat.format(cell.getDateCellValue());
                                        }else{
                                            txt="" + cell.getNumericCellValue();
                                        }
                                        break;
                                    case BOOLEAN:
                                        txt="" + cell.getBooleanCellValue();
                                        break;
                                    case BLANK:
                                        txt="BLANK";
                                        break;
                                    case FORMULA:
                                        txt="" + cell.getNumericCellValue();
                                        break;
                                    case ERROR:
                                        txt="ERROR";
                                        break;
                                    default:
                                        txt="";
                                }
                            }
                            fila.add(txt);
                        }
                        if (fila.size()>0){
                            if(isNumeric(fila.get(0))){
                                currentNombre = fila.get(3);
                                currentFactura = new Factura(fila);
                                currentFactura.setIndicador(indicador);
                                addFactura(indicador,currentNombre,currentTitulo1,currentTitulo2,currentFactura);
                                addFactura("UTILIDADES GENERALES",currentNombre,currentTitulo1,currentTitulo2,currentFactura);

                            }else if(fila.size()>1 && !fila.get(1).isEmpty()){
                                currentTitulo2 = fila.get(0)+" - "+fila.get(1);
                            }
                            else if(fila.size()==1 && !fila.get(0).isEmpty()){
                                currentTitulo1 = fila.get(0);
                            }
                        }
                        fila = new ArrayList<>();
                    }
                }
                //printData();
            }else{
                //todo aqui
            }

        }catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public boolean isNumeric(String str)
    {
        return str.matches("\\d+");  //match a number with optional '-' and decimal.
    }
    public void addFactura(String title, String nombre, String titulo1, String titulo2, Factura factura){
        Map<String,Map<String,Map<String,ArrayList<Factura>>>> each;
        Map<String,Map<String,ArrayList<Factura>>> tit1;
        Map<String,ArrayList<Factura>> tit2;
        ArrayList<Factura> fact;
        if(data.containsKey(title)){
            each = data.get(title);
            if(each.containsKey(nombre)){
                tit1 = each.get(nombre);
                if(tit1.containsKey(titulo1)){
                    tit2 = tit1.get(titulo1);
                    if(tit2.containsKey(titulo2)){
                        fact = tit2.get(titulo2);
                    }
                    else{
                        fact = new ArrayList<>();
                    }
                }else{
                    fact = new ArrayList<>();
                    tit2 = new TreeMap<>();
                }
            }else{
                fact = new ArrayList<>();
                tit2 = new TreeMap<>();
                tit1 = new TreeMap<>();
            }
        }else{
            fact = new ArrayList<>();
            tit2 = new TreeMap<>();
            tit1 = new TreeMap<>();
            each = new TreeMap<>();
        }

        fact.add(factura);
        tit2.put(titulo2,fact);
        tit1.put(titulo1,tit2);
        each.put(nombre,tit1);
        data.put(title,each);
    }
    /*public void addFactura(String nombre, String titulo1, String titulo2, Factura factura){
        Map<String,Map<String,ArrayList<Factura>>> tit1;
        Map<String,ArrayList<Factura>> tit2;
        ArrayList<Factura> fact;
        if(data.containsKey(nombre)){
            tit1 = data.get(nombre);
            if(tit1.containsKey(titulo1)){
                tit2 = tit1.get(titulo1);
                if(tit2.containsKey(titulo2)){
                    fact = tit2.get(titulo2);
                }
                else{
                    fact = new ArrayList<>();
                }
            }else{
                fact = new ArrayList<>();
                tit2 = new TreeMap<>();
            }
        }else{
            fact = new ArrayList<>();
            tit2 = new TreeMap<>();
            tit1 = new TreeMap<>();
        }
        fact.add(factura);
        tit2.put(titulo2,fact);
        tit1.put(titulo1,tit2);
        data.put(nombre,tit1);
    }*/
   /* public void printData(){
        for(String nombre : data.keySet()){
            System.out.println(nombre);
            for(String tit1 : data.get(nombre).keySet()){
                System.out.println("\t"+tit1);
                for(String tit2 : data.get(nombre).get(tit1).keySet()){
                    System.out.println("\t\t"+tit2);
                    for(Factura fact : data.get(nombre).get(tit1).get(tit2)){
                        System.out.println("\t\t\t"+fact.getNumero()+ " "+ fact.getFecha());
                    }
                }
            }
        }
    }*/

    public Map<String,Map<String, Map<String, Map<String, ArrayList<Factura>>>>> getData() {
        return data;
    }

    public void writeExcel(String path, Map<String,Map<String,Map<String,ArrayList<Factura>>>> data){
        Workbook workbook = new HSSFWorkbook();
        Sheet sheet = workbook.createSheet("UTILIDADES POR CLIENTE");

        CellStyle subtitulo = getCellStyle(workbook,true,9,"",true);
        CellStyle titulo1 = getCellStyle(workbook,true,12,"",false);
        CellStyle titulo2= getCellStyle(workbook,true,10,"",false);
        CellStyle normal = getCellStyle(workbook,false,8,"",false);
        CellStyle dateStyle = getCellStyle(workbook,false,8,"dd/MM/yyyy",false);
        CellStyle numbStyle = getCellStyle(workbook,false,8,"###,###,###0.00##",false);
        CellStyle numbStyleT = getCellStyle(workbook,true,8,"###,###,###0.00##",true);
        CellStyle numbStyleP = getCellStyle(workbook,true,8,"##0.##%",true);
        CellStyle numbStylePN = getCellStyle(workbook,false,8,"##0.##%",false);
        CellStyle numbFACTStyle = getCellStyle(workbook,false,8,"0#########",false);

        Row titulo = sheet.createRow(0);
        Cell celda = titulo.createCell(0);
        celda.setCellStyle(titulo1);
        celda.setCellValue("UTILIDADES VENTA POR CLIENTE");

        int rowCount = 2;

        Row titles = sheet.createRow(rowCount++);

        Cell comes = getCell(titles,0,subtitulo);
        comes.setCellValue("Punto de Venta");
        Cell factu = getCell(titles,1,subtitulo);
        factu.setCellValue("Factura");
        Cell type = getCell(titles,2,subtitulo);
        type.setCellValue("Tipo Pago");
        Cell date = getCell(titles,3,subtitulo);
        date.setCellValue("Fecha");
        Cell cant = getCell(titles,4,subtitulo);
        cant.setCellValue("Cantidad");
        Cell desc = getCell(titles,5,subtitulo);
        desc.setCellValue("Descuento");
        Cell price = getCell(titles,6,subtitulo);
        price.setCellValue("Precio");
        Cell cost = getCell(titles,7,subtitulo);
        cost.setCellValue("Costo");
        Cell venta = getCell(titles,8,subtitulo);
        venta.setCellValue("Venta Neta");
        Cell utlidad = getCell(titles,9,subtitulo);
        utlidad.setCellValue("Utilidad");
        Cell porc = getCell(titles,10,subtitulo);
        porc.setCellValue("%");

        rowCount++;

        ArrayList<Factura> totalGeneral = new ArrayList<>();
        for(String nombre : data.keySet()){
            Row row = sheet.createRow(rowCount++);
            Cell cell = row.createCell(0);
            cell.setCellStyle(titulo1);
            cell.setCellValue(nombre);
            rowCount++;
            ArrayList<Factura> totalName = new ArrayList<>();
            for(String tit1 : data.get(nombre).keySet()){
                Row rowt1 = sheet.createRow(rowCount++);
                Cell cellt1 = rowt1.createCell(0);
                cellt1.setCellStyle(titulo2);
                cellt1.setCellValue(tit1);
                rowCount++;
                ArrayList<Factura> faa = new ArrayList<>();
                for(String tit2 : data.get(nombre).get(tit1).keySet()){

                    Row rowt2 = sheet.createRow(rowCount++);
                    Cell cellt2 = rowt2.createCell(0);
                    cellt2.setCellStyle(subtitulo);
                    cellt2.setCellValue(tit2);
                    for(Factura fact : data.get(nombre).get(tit1).get(tit2)){
                        Row rowt3 = sheet.createRow(rowCount++);

                        Cell cellMENOS1 = getCell(rowt3,0,normal);
                        cellMENOS1.setCellValue(fact.getIndicador());
                        Cell cell0 = getCell(rowt3,1,numbFACTStyle);
                            cell0.setCellValue(fact.getNumero());
                        Cell cell1 = getCell(rowt3,2,normal);
                            cell1.setCellValue(fact.getTipoPago());
                        Cell cell2 = getCell(rowt3,3,dateStyle);
                            cell2.setCellValue(fact.getFecha());
                        Cell cell3 = getCell(rowt3,4,numbStyle);
                            cell3.setCellValue(fact.getCantidad());
                        Cell cell4 = getCell(rowt3,5,numbStyle);
                            cell4.setCellValue(fact.getDescuento());
                        Cell cell5 = getCell(rowt3,6,numbStyle);
                            cell5.setCellValue(fact.getPrecio());
                        Cell cell6 = getCell(rowt3,7,numbStyle);
                            cell6.setCellValue(fact.getCosto());
                        Cell cell7 = getCell(rowt3,8,numbStyle);
                            cell7.setCellValue(fact.getVentaNeta());
                        Cell cell8 = getCell(rowt3,9,numbStyle);
                            cell8.setCellValue(fact.getUtilidad());
                        Cell cell9 = getCell(rowt3,10,numbStylePN);
                            cell9.setCellValue(fact.getPorcentaje());
                    }
                    Row rowtotal = sheet.createRow(rowCount++);

                    ArrayList<Factura> f = data.get(nombre).get(tit1).get(tit2);
                    Factura res = Factura.getSumaFact(f);

                    faa.add(res);

                    Cell cell0 = getCell(rowtotal,1,subtitulo);
                        cell0.setCellValue("TOTAL:");
                    Cell cell3 = getCell(rowtotal,4,numbStyleT);
                        cell3.setCellValue(res.getCantidad());
                    Cell cell4 = getCell(rowtotal,5,numbStyleT);
                        cell4.setCellValue(res.getDescuento());
                    Cell cell5 = getCell(rowtotal,6,numbStyleT);
                        cell5.setCellValue(res.getPrecio());
                    Cell cell6 = getCell(rowtotal,7,numbStyleT);
                        cell6.setCellValue(res.getCosto());
                    Cell cell7 = getCell(rowtotal,8,numbStyleT);
                        cell7.setCellValue(res.getVentaNeta());
                    Cell cell8 = getCell(rowtotal,9,numbStyleT);
                        cell8.setCellValue(res.getUtilidad());
                    Cell cell9 = getCell(rowtotal,10,numbStyleP);
                        cell9.setCellValue(res.getPorcentaje());

                    rowCount++;
                }

                Row rowtotaltitulo1 = sheet.createRow(rowCount++);

                Factura totalTitulo1 = Factura.getSumaFact(faa);
                totalName.add(totalTitulo1);

                Cell cell0 = getCell(rowtotaltitulo1,1,subtitulo);
                cell0.setCellValue(tit1);
                Cell cell3 = getCell(rowtotaltitulo1,4,numbStyleT);
                cell3.setCellValue(totalTitulo1.getCantidad());
                Cell cell4 = getCell(rowtotaltitulo1,5,numbStyleT);
                cell4.setCellValue(totalTitulo1.getDescuento());
                Cell cell5 = getCell(rowtotaltitulo1,6,numbStyleT);
                cell5.setCellValue(totalTitulo1.getPrecio());
                Cell cell6 = getCell(rowtotaltitulo1,7,numbStyleT);
                cell6.setCellValue(totalTitulo1.getCosto());
                Cell cell7 = getCell(rowtotaltitulo1,8,numbStyleT);
                cell7.setCellValue(totalTitulo1.getVentaNeta());
                Cell cell8 = getCell(rowtotaltitulo1,9,numbStyleT);
                cell8.setCellValue(totalTitulo1.getUtilidad());
                Cell cell9 = getCell(rowtotaltitulo1,10,numbStyleP);
                cell9.setCellValue(totalTitulo1.getPorcentaje());

                rowCount++;
            }
            Row rowtotalName = sheet.createRow(rowCount++);

            Factura nameSum = Factura.getSumaFact(totalName);
            totalGeneral.add(nameSum);

            Cell cell0 = getCell(rowtotalName,1,subtitulo);
            cell0.setCellValue(nombre);
            Cell cell3 = getCell(rowtotalName,4,numbStyleT);
            cell3.setCellValue(nameSum.getCantidad());
            Cell cell4 = getCell(rowtotalName,5,numbStyleT);
            cell4.setCellValue(nameSum.getDescuento());
            Cell cell5 = getCell(rowtotalName,6,numbStyleT);
            cell5.setCellValue(nameSum.getPrecio());
            Cell cell6 = getCell(rowtotalName,7,numbStyleT);
            cell6.setCellValue(nameSum.getCosto());
            Cell cell7 = getCell(rowtotalName,8,numbStyleT);
            cell7.setCellValue(nameSum.getVentaNeta());
            Cell cell8 = getCell(rowtotalName,9,numbStyleT);
            cell8.setCellValue(nameSum.getUtilidad());
            Cell cell9 = getCell(rowtotalName,10,numbStyleP);
            cell9.setCellValue(nameSum.getPorcentaje());

            rowCount++;
        }
        Row titles2 = sheet.createRow(rowCount++);

        Cell cant2 = getCell(titles2,4,subtitulo);
        cant2.setCellValue("Cantidad");
        Cell desc2 = getCell(titles2,5,subtitulo);
        desc2.setCellValue("Descuento");
        Cell price2 = getCell(titles2,6,subtitulo);
        price2.setCellValue("Precio");
        Cell cost2 = getCell(titles2,7,subtitulo);
        cost2.setCellValue("Costo");
        Cell venta2 = getCell(titles2,8,subtitulo);
        venta2.setCellValue("Venta Neta");
        Cell utlidad2 = getCell(titles2,9,subtitulo);
        utlidad2.setCellValue("Utilidad");
        Cell porc2 = getCell(titles2,10,subtitulo);
        porc2.setCellValue("%");
        Row generalTotal = sheet.createRow(rowCount++);

        Factura total = Factura.getSumaFact(totalGeneral);

        Cell cell0 = getCell(generalTotal,1,subtitulo);
        cell0.setCellValue("TOTAL GENERAL:");
        Cell cell3 = getCell(generalTotal,4,numbStyleT);
        cell3.setCellValue(total.getCantidad());
        Cell cell4 = getCell(generalTotal,5,numbStyleT);
        cell4.setCellValue(total.getDescuento());
        Cell cell5 = getCell(generalTotal,6,numbStyleT);
        cell5.setCellValue(total.getPrecio());
        Cell cell6 = getCell(generalTotal,7,numbStyleT);
        cell6.setCellValue(total.getCosto());
        Cell cell7 = getCell(generalTotal,8,numbStyleT);
        cell7.setCellValue(total.getVentaNeta());
        Cell cell8 = getCell(generalTotal,9,numbStyleT);
        cell8.setCellValue(total.getUtilidad());
        Cell cell9 = getCell(generalTotal,10,numbStyleP);
        cell9.setCellValue(total.getPorcentaje());

        rowCount++;

        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(path);
            workbook.write(fileOutputStream);
            fileOutputStream.close();
            workbook.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }



    }

    public Cell getCell(Row row, int index, CellStyle style){
        Cell cell = row.createCell(index);
        cell.setCellStyle(style);
        return cell;
    }
    public CellStyle getCellStyle(Workbook workbook, Boolean bold, int pt, String format, Boolean grueso){
        Font font = workbook.createFont();
        CreationHelper creationHelper = workbook.getCreationHelper();

        font.setBold(bold);
        font.setFontHeightInPoints((short) pt);
        font.setColor(IndexedColors.BLACK.getIndex());

        CellStyle cs = workbook.createCellStyle();
        cs.setFont(font);
        if(!format.isEmpty())cs.setDataFormat(creationHelper.createDataFormat().getFormat(format));
        if(!grueso){
            cs.setBorderBottom(BorderStyle.THIN);
            cs.setBorderTop(BorderStyle.THIN);
            cs.setBorderRight(BorderStyle.THIN);
            cs.setBorderLeft(BorderStyle.THIN);
        }else{
            cs.setBorderBottom(BorderStyle.MEDIUM);
            cs.setBorderTop(BorderStyle.MEDIUM);
            cs.setBorderRight(BorderStyle.MEDIUM);
            cs.setBorderLeft(BorderStyle.MEDIUM);
        }

        return cs;
    }
}
