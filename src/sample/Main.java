package sample;

import Classes.Excel;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.awt.*;
import java.io.File;
import java.io.IOException;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("UTILIDADES DE VENTAS POR CLIENTE");
        primaryStage.setScene(new Scene(root, 1908, 1002));
        primaryStage.setResizable(true);

        primaryStage.show();
    }


    public static void main(String[] args){
        /*Excel excel = new Excel();
        excel.readExcel("C:\\Users\\iworth\\iCloudDrive\\Desktop\\excel.xls");

        excel.writeExcel("C:\\Users\\iworth\\iCloudDrive\\Desktop\\resultado.xls");

        File f = new File("C:\\Users\\iworth\\iCloudDrive\\Desktop\\resultado.xls");
        Desktop desktop = Desktop.getDesktop();
        try {
            desktop.open(f);
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        launch(args);
    }
}
