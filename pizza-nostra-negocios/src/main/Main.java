package main;

import entidades.Empleado;
import entidades.Pago;
import implementaciones.DAOSFactory;
import interfaces.IPagoDAO;
import java.util.GregorianCalendar;

public class Main {
    
    public static void main(String[] args) {
        IPagoDAO pagos = DAOSFactory.crearPagosDAO();
        
        Pago pago = new Pago();
        pago.setEmpleado(new Empleado(1L));
        pago.setInicioPeriodo(new GregorianCalendar());
        pago.setFinPeriodo(new GregorianCalendar());
        pago.setFecha(new GregorianCalendar());
        pago.setEstado("PENDIENTE");
        pago.setComentario("Pa que comas");
        pago.setHorasTrabajadas(8);
        
        pagos.agregar(pago);
    }
}
