package Classes;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleSetProperty;
import javafx.beans.property.SimpleStringProperty;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TableFact {
    private final SimpleStringProperty titulo;
    private final SimpleIntegerProperty numero;
    private final SimpleStringProperty tipoPago;
    private final SimpleStringProperty fecha;
    private final SimpleDoubleProperty cantidad;
    private final SimpleDoubleProperty descuento;
    private final SimpleDoubleProperty precio;
    private final SimpleDoubleProperty costo;
    private final SimpleDoubleProperty ventaNeta;
    private final SimpleDoubleProperty utilidad;
    private final SimpleDoubleProperty porcentaje;

    public TableFact(int numero, String tipoPago, Date fecha, double cantidad, double descuento, double precio, double costo, double ventaNeta, double utilidad, double porcentaje) {
        this.titulo = new SimpleStringProperty("");
        this.numero = new SimpleIntegerProperty(numero);
        this.tipoPago = new SimpleStringProperty(tipoPago);
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String strDate = dateFormat.format(fecha);
        this.fecha = new SimpleStringProperty(strDate);
        this.cantidad = new SimpleDoubleProperty(round(cantidad,2));
        this.descuento = new SimpleDoubleProperty(round(descuento,2));
        this.precio = new SimpleDoubleProperty(round(precio,2));
        this.costo = new SimpleDoubleProperty(round(costo,2));
        this.ventaNeta = new SimpleDoubleProperty(round(ventaNeta,2));
        this.utilidad = new SimpleDoubleProperty(round(utilidad,2));
        this.porcentaje = new SimpleDoubleProperty(round(porcentaje*100,2));
    }


    public TableFact() {
        this.titulo = new SimpleStringProperty("");
        this.numero = null;
        this.tipoPago = null;
        this.fecha = null;
        this.cantidad = null;
        this.descuento = null;
        this.precio = null;
        this.costo  = null;
        this.ventaNeta  = null;
        this.utilidad =  null;
        this.porcentaje = null;
    }

    public TableFact(String title) {
        this.titulo = new SimpleStringProperty(title);
        this.numero = null;
        this.tipoPago = null;
        this.fecha = null;
        this.cantidad = null;
        this.descuento = null;
        this.precio = null;
        this.costo  = null;
        this.ventaNeta  = null;
        this.utilidad =  null;
        this.porcentaje = null;
    }

    public TableFact(Factura fact, String where) {


        this.tipoPago = new SimpleStringProperty(fact.getTipoPago());
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String strDate = dateFormat.format(fact.getFecha());
        if(fact.getNumero()==0){
            strDate = "";
            numero=null;
            titulo=new SimpleStringProperty("TOTAL "+where);
        }
        else{
            this.numero = new SimpleIntegerProperty(fact.getNumero());
            this.titulo = new SimpleStringProperty(fact.getIndicador());
        }
        this.fecha = new SimpleStringProperty(strDate);
        this.cantidad = new SimpleDoubleProperty(round(fact.getCantidad(),2));
        this.descuento = new SimpleDoubleProperty(round(fact.getDescuento(),2));
        this.precio = new SimpleDoubleProperty(round(fact.getPrecio(),2));
        this.costo = new SimpleDoubleProperty(round(fact.getCosto(),2));
        this.ventaNeta = new SimpleDoubleProperty(round(fact.getVentaNeta(),2));
        this.utilidad = new SimpleDoubleProperty(round(fact.getUtilidad(),2));
        this.porcentaje = new SimpleDoubleProperty(round(fact.getPorcentaje()*100,2));
    }
    private static double round(double value, int places) {
        double scale = Math.pow(10, places);
        return Math.round(value * scale) / scale;
    }

    public int getNumero() {
        return numero.get();
    }

    public SimpleIntegerProperty numeroProperty() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero.set(numero);
    }

    public String getTipoPago() {
        return tipoPago.get();
    }

    public SimpleStringProperty tipoPagoProperty() {
        return tipoPago;
    }

    public void setTipoPago(String tipoPago) {
        this.tipoPago.set(tipoPago);
    }

    public double getCantidad() {
        return cantidad.get();
    }

    public SimpleDoubleProperty cantidadProperty() {
        return cantidad;
    }

    public void setCantidad(double cantidad) {
        this.cantidad.set(cantidad);
    }

    public double getDescuento() {
        return descuento.get();
    }

    public SimpleDoubleProperty descuentoProperty() {
        return descuento;
    }

    public void setDescuento(double descuento) {
        this.descuento.set(descuento);
    }

    public double getPrecio() {
        return precio.get();
    }

    public SimpleDoubleProperty precioProperty() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio.set(precio);
    }

    public double getCosto() {
        return costo.get();
    }

    public SimpleDoubleProperty costoProperty() {
        return costo;
    }

    public void setCosto(double costo) {
        this.costo.set(costo);
    }

    public double getVentaNeta() {
        return ventaNeta.get();
    }

    public SimpleDoubleProperty ventaNetaProperty() {
        return ventaNeta;
    }

    public void setVentaNeta(double ventaNeta) {
        this.ventaNeta.set(ventaNeta);
    }

    public double getUtilidad() {
        return utilidad.get();
    }

    public SimpleDoubleProperty utilidadProperty() {
        return utilidad;
    }

    public void setUtilidad(double utilidad) {
        this.utilidad.set(utilidad);
    }

    public double getPorcentaje() {
        return porcentaje.get();
    }

    public SimpleDoubleProperty porcentajeProperty() {
        return porcentaje;
    }

    public void setPorcentaje(double porcentaje) {
        this.porcentaje.set(porcentaje);
    }

    public String getFecha() {
        return fecha.get();
    }

    public SimpleStringProperty fechaProperty() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha.set(fecha);
    }

    public String getTitulo() {
        return titulo.get();
    }

    public SimpleStringProperty tituloProperty() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo.set(titulo);
    }
}
