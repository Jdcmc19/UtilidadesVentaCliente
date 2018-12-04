package sample;

import Classes.Excel;
import Classes.Factura;
import Classes.TableFact;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.util.Callback;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

public class Controller {
    @FXML
    Pane pane;
    @FXML
    Label lblCantidad,lblDescuento,lblPrecio,lblCosto,lblVentaNeta,lblUtilidad,lblPorcentaje;
    @FXML
    TableView tableData;
    @FXML
    TableColumn colTitulo,colFactura, colTipoPago, colFecha, colCantidad,colDescuento,colPrecio,colCosto,colVentaNeta,colUtilidad,colPorcentaje;
    @FXML
    Button btoAbrirExcel, btoCargarExcel, btoIniciar, btoLimpiarReporte, btoEliExcel;
    @FXML
    TextField txtNombreFile, txtIndicadorCargar;
    @FXML
    ListView<String> listFile;

    Map<String,Map<String,Map<String, Map<String,ArrayList<Factura>>>>> data = null;
    Map<String,Map<String, Map<String,ArrayList<Factura>>>> current = null;
    Map<String,String> files = new TreeMap<>();
    int cantidadFiles = 0;
    Excel excel = new Excel();
    int temporal = 1;
    public void initialize(){

        colTitulo.setStyle("-fx-font-weight: bold");
        colTitulo.setCellValueFactory(new PropertyValueFactory<TableFact,String>("titulo"));
        colFactura.setCellValueFactory(new PropertyValueFactory<TableFact,Integer>("numero"));
        colTipoPago.setCellValueFactory(new PropertyValueFactory<TableFact,String>("tipoPago"));
        colFecha.setCellValueFactory(new PropertyValueFactory<TableFact, Date>("fecha"));
        colCantidad.setCellValueFactory(new PropertyValueFactory<TableFact,Double>("cantidad"));
        colDescuento.setCellValueFactory(new PropertyValueFactory<TableFact,Double>("descuento"));
        colPrecio.setCellValueFactory(new PropertyValueFactory<TableFact,Double>("precio"));
        colCosto.setCellValueFactory(new PropertyValueFactory<TableFact,Double>("costo"));
        colVentaNeta.setCellValueFactory(new PropertyValueFactory<TableFact,Double>("ventaNeta"));
        colUtilidad.setCellValueFactory(new PropertyValueFactory<TableFact,Double>("utilidad"));
        colPorcentaje.setCellValueFactory(new PropertyValueFactory<TableFact,Double>("porcentaje"));

        colTitulo.setSortable(false);
        colFactura.setSortable(false);
        colTipoPago.setSortable(false);
        colFecha.setSortable(false);
        colCantidad.setSortable(false);
        colDescuento.setSortable(false);
        colPrecio.setSortable(false);
        colCosto.setSortable(false);
        colVentaNeta.setSortable(false);
        colUtilidad.setSortable(false);
        colPorcentaje.setSortable(false);

        tableData.setRowFactory(new Callback<TableView<TableFact>, TableRow<TableFact>>() {
            @Override
            public TableRow<TableFact> call(TableView<TableFact> param) {
                return new TableRow<TableFact>() {
                    @Override
                    protected void updateItem(TableFact item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item==null || item.getTitulo().startsWith("TOTAL")) {
                            setStyle("-fx-font-weight: bold");
                        } else  {
                            setStyle("");
                        }
                    }
                };
            }
        });

        btoEliExcel.setOnAction(event -> {
            ArrayList<String> p = new ArrayList<>(listFile.getSelectionModel().getSelectedItems());
            for(String del : p){
                listFile.getItems().remove(del);
                files.remove(del.substring(del.indexOf('[')+1,del.indexOf(']')).trim());
            }
        });
        btoAbrirExcel.setOnAction(event -> {
            String nombre;
            if(txtNombreFile.getText().isEmpty()){
                nombre="Temporal-"+temporal++ +".xls";
            }
            else{
                nombre=txtNombreFile.getText()+".xls";
            }
            new File("TMP-UtilidadesClientes").mkdirs();
            excel.writeExcel("TMP-UtilidadesClientes\\"+nombre,current);
            File f = new File("TMP-UtilidadesClientes\\"+nombre);
            Desktop desktop = Desktop.getDesktop();
            try {
                desktop.open(f);
            } catch (IOException e) {
                e.printStackTrace();
            }
            txtNombreFile.setText("");
        });
        btoCargarExcel.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            File file = fileChooser.showOpenDialog(null);
            if(file!=null){
                String filePath = file.getAbsolutePath();
                cantidadFiles++;
                String indi = txtIndicadorCargar.getText().isEmpty() ? filePath.substring(filePath.lastIndexOf("\\")+1,filePath.lastIndexOf('.')) : txtIndicadorCargar.getText();
                indi += " - "+cantidadFiles;
                listFile.getItems().add("[ "+indi+" ]  Path: "+file.getAbsolutePath());
                listFile.scrollTo(listFile.getItems().size()-1);
                txtIndicadorCargar.setText("");
                files.put(indi,file.getAbsolutePath());
            }

            //excel.readExcel(txtIndicadorCargar.getText(),);
            //data = excel.getData();
            //chargeData();
        });
        btoLimpiarReporte.setOnAction(event -> {
            tableData.getItems().clear();
            files = new TreeMap<>();
            listFile.getItems().clear();
            excel = new Excel();
            temporal = 1;
            current = new TreeMap<>();
            data = excel.getData();
            chargeData();

        });
        btoIniciar.setOnAction(event -> {
            excel.resetData();
           for(String ind : files.keySet()){
               excel.readExcel(files.get(ind),ind);
           }

           data = excel.getData();
           chargeData();
        });
        listFile.setOnDragOver(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                Dragboard db = event.getDragboard();
                if (db.hasFiles()) {
                    event.acceptTransferModes(TransferMode.COPY);
                } else {
                    event.consume();
                }
            }
        });
        listFile.setOnDragDropped(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                Dragboard db = event.getDragboard();
                boolean success = false;
                if (db.hasFiles()) {
                    success = true;
                    String filePath = null;
                    for (File file:db.getFiles()) {
                        filePath = file.getAbsolutePath();
                        cantidadFiles++;
                        String indi = txtIndicadorCargar.getText().isEmpty() ? filePath.substring(filePath.lastIndexOf("\\")+1,filePath.lastIndexOf('.')) : txtIndicadorCargar.getText();
                        indi += " - "+cantidadFiles;
                        listFile.getItems().add("[ "+indi+" ]  Path: "+filePath);
                        listFile.scrollTo(listFile.getItems().size()-1);
                        files.put(indi,filePath);
                    }
                }
                event.setDropCompleted(success);
                event.consume();
            }
        });

    }
    public TableView<TableFact> setInfo(Map<String,Map<String, Map<String,ArrayList<Factura>>>> data, TableView<TableFact> tableData){
        ArrayList<Factura> totalGeneral = new ArrayList<>();
        for(String nombre : data.keySet()){
            tableData.getItems().add(new TableFact(nombre));
            ArrayList<Factura> totalName = new ArrayList<>();
            for(String tit1 : data.get(nombre).keySet()){
                tableData.getItems().add(new TableFact(tit1));
                ArrayList<Factura> faa = new ArrayList<>();
                for(String tit2 : data.get(nombre).get(tit1).keySet()){
                    tableData.getItems().add(new TableFact(tit2));
                    for(Factura fact : data.get(nombre).get(tit1).get(tit2)){
                        tableData.getItems().add(new TableFact(fact,""));
                    }
                    ArrayList<Factura> f = data.get(nombre).get(tit1).get(tit2);
                    Factura res = Factura.getSumaFact(f);

                    tableData.getItems().add(new TableFact(res, tit2));
                    tableData.getItems().add(new TableFact());
                    faa.add(res);
                }

                Factura totalTitulo1 = Factura.getSumaFact(faa);
                totalName.add(totalTitulo1);
                tableData.getItems().add(new TableFact(totalTitulo1,tit1));
                tableData.getItems().add(new TableFact());

            }

            Factura nameSum = Factura.getSumaFact(totalName);
            totalGeneral.add(nameSum);

            tableData.getItems().add(new TableFact(nameSum, nombre));
            tableData.getItems().add(new TableFact());
        }
        Factura total = Factura.getSumaFact(totalGeneral);

        lblCantidad.setText(Double.toString(total.getCantidad()));
        lblDescuento.setText(Double.toString(total.getDescuento()));
        lblPrecio.setText(Double.toString(total.getPrecio()));
        lblCosto.setText(Double.toString(total.getCosto()));
        lblVentaNeta.setText(Double.toString(total.getVentaNeta()));
        lblUtilidad.setText(Double.toString(total.getUtilidad()));
        lblPorcentaje.setText(Double.toString(total.getPorcentaje())+"%");

        tableData.getItems().add(new TableFact(total, "GENERAL"));
        tableData.getItems().add(new TableFact());
        return tableData;
    }

    public Map<String,Map<String, Map<String,ArrayList<Factura>>>> getCurrent(ArrayList<String> path,Map<String,Map<String,Map<String, Map<String,ArrayList<Factura>>>>> data ){
        Map<String,Map<String,Map<String,ArrayList<Factura>>>> gen = new TreeMap<>();
        Map<String,Map<String,ArrayList<Factura>>> tit1 = new TreeMap<>();
        Map<String,ArrayList<Factura>> tit2 = new TreeMap<>();
        ArrayList<Factura> fact = new ArrayList<>();
        Map<String,Map<String, Map<String,ArrayList<Factura>>>> nombre = new TreeMap<>();
        Map<String,Map<String,ArrayList<Factura>>> tmp1;
        Map<String,ArrayList<Factura>> tmp2;
        ArrayList<Factura> tmp3;
        switch (path.size()){
            case 2:
                nombre = data.get(path.get(0));
                break;
            case 3:
                gen = data.get(path.get(1));
                tit1 = gen.get(path.get(0));

                nombre.put(path.get(1),tit1);
                break;
            case 4:
                gen = data.get(path.get(2));
                tit1 = gen.get(path.get(1));
                tit2 = tit1.get(path.get(0));
                tmp1 = new TreeMap<>();
                tmp1.put(path.get(1),tit2);
                nombre.put(path.get(2),tmp1);
                break;
            case 5:
                gen = data.get(path.get(3));
                tit1 = gen.get(path.get(2));
                tit2 = tit1.get(path.get(1));
                fact = tit2.get(path.get(0));
                tmp1 = new TreeMap<>();
                tmp2 = new TreeMap<>();
                tmp2.put(path.get(1),fact);
                tmp1.put(path.get(2),tmp2);
                nombre.put(path.get(3),tmp1);
                break;
        }
    return nombre;
    }

    public void chargeData(){
        TreeItem<String> rootItem = new TreeItem<String> ("UTILIDADES POR CLIENTE");
        rootItem.setExpanded(true);

        for(String title : data.keySet()){
            TreeItem<String> file = new TreeItem<String> (title);
            file.setExpanded(false);
            for(String nombre : data.get(title).keySet()){
                TreeItem<String> nombreTI = new TreeItem<String> (nombre);
                nombreTI.setExpanded(false);
                for(String tit1 : data.get(title).get(nombre).keySet()){
                    TreeItem<String> titulo1TI = new TreeItem<String> (tit1);
                    titulo1TI.setExpanded(false);
                    for(String tit2 : data.get(title).get(nombre).get(tit1).keySet()){
                        TreeItem<String> titulo2TI = new TreeItem<String> (tit2);
                        titulo2TI.setExpanded(false);

                        titulo1TI.getChildren().add(titulo2TI);
                    }
                    nombreTI.getChildren().add(titulo1TI);
                }
                file.getChildren().add(nombreTI);
            }
            rootItem.getChildren().add(file);
        }
        TreeView<String> tree = new TreeView<String> (rootItem);
        tree.setPrefWidth(511);
        tree.setPrefHeight(1008);
        tree.setLayoutX(0);
        tree.setLayoutY(0);

        tree.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<TreeItem<String>>() {
            @Override
            public void changed(ObservableValue<? extends TreeItem<String>> observable, TreeItem<String> oldValue, TreeItem<String> newValue) {
                TreeItem tmp = newValue;
                ArrayList<String> path= new ArrayList<>();
                while(tmp!=null){
                    path.add(tmp.getValue().toString());
                    tmp = tmp.getParent();
                }
                tableData.getItems().clear();
                current = getCurrent(path,data);
                tableData = setInfo(current,tableData);
            }
        });

        pane.getChildren().add(tree);
    }

}
