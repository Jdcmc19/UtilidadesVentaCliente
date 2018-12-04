package Classes;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Factura {
    private int numero=0;
    private String tipoPago="";
    private Date fecha=new Date();
    private double cantidad;
    private double descuento;
    private double precio;
    private double costo;
    private double ventaNeta;
    private double utilidad;
    private double porcentaje;
    private String indicador;


    public Factura (ArrayList<String> fila){
        this.numero = Integer.parseInt(fila.get(0));
        this.tipoPago = fila.get(1);
        this.fecha = StringToDate(fila.get(2));
        this.cantidad = StringToDouble(fila.get(5));
        this.descuento = StringToDouble(fila.get(6));
        this.precio = StringToDouble(fila.get(7));
        this.costo = StringToDouble(fila.get(8));
        this.ventaNeta = StringToDouble(fila.get(9));
        this.utilidad = StringToDouble(fila.get(10));
        this.porcentaje = utilidad/costo;
    }

    public Factura(double cantidad, double descuento, double precio, double costo, double ventaNeta, double utilidad, double porcentaje) {
        this.cantidad = cantidad;
        this.descuento = descuento;
        this.precio = precio;
        this.costo = costo;
        this.ventaNeta = ventaNeta;
        this.utilidad = utilidad;
        this.porcentaje = porcentaje;
    }

    private double StringToDouble(String s){
        double a = Double.parseDouble(s.replaceAll(",","").replaceAll("%",""));
        return a;
    }
    private Date StringToDate(String d){
        DateFormat sourceFormat = new SimpleDateFormat("dd/MM/yyyy");
        try{
            Date date = sourceFormat.parse(d);
            return date;
        }catch (ParseException pe){
            pe.printStackTrace();
        }
        return null;
    }

    public void setIndicador(String indicador) {
        this.indicador = indicador;
    }

    public String getIndicador() {
        return indicador;
    }

    public static Factura getSumaFact(ArrayList<Factura> facturas){
        Factura f;
        double cantidad=0;
        double descuento=0;
        double precio=0;
        double costo=0;
        double ventaNeta=0;
        double utilidad=0;
        double promPorcentaje=0;
        for(Factura fact : facturas){
            cantidad+=fact.getCantidad();
            descuento+=fact.getDescuento();
            precio+=fact.getPrecio();
            costo+=fact.getCosto();
            ventaNeta+=fact.getVentaNeta();
            utilidad+=fact.getUtilidad();
        }
        promPorcentaje=utilidad/costo;
        f = new Factura(cantidad,descuento,precio,costo,ventaNeta,utilidad,promPorcentaje);
        return f;
    }

    public int getNumero() {
        return numero;
    }

    public String getTipoPago() {
        return tipoPago;
    }

    public Date getFecha() {
        return fecha;
    }

    public double getCantidad() {
        return cantidad;
    }

    public double getDescuento() {
        return descuento;
    }

    public double getPrecio() {
        return precio;
    }

    public double getVentaNeta() {
        return ventaNeta;
    }

    public double getUtilidad() {
        return utilidad;
    }

    public double getPorcentaje() {
        return porcentaje;
    }

    public double getCosto() {
        return costo;
    }
}
