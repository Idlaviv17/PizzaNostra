/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main;

import entidades.Pago;
import entidades.Salario;
import implementaciones.DAOSFactory;
import interfaces.IPagoDAO;
import java.util.GregorianCalendar;

/**
 *
 * @author jjavi
 */
public class Main {
    
    public static void main(String[] args) {
        IPagoDAO pagos = DAOSFactory.crearPagosDAO();
        
        Pago pago = new Pago();
        pago.setSalario(new Salario("gerente", 200f));
        pago.setFecha(new GregorianCalendar());
        pago.setEstado(true);
        pago.setComentario("Muy buen trabajo :)");
        pago.setHorasTrabajadas(120f);
        
        pagos.agregar(pago);
    }
}
