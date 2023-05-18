package control;

import entidades.DiaTrabajado;
import entidades.Empleado;
import entidades.Pago;
import entidades.Salario;
import java.util.List;

public interface IControl {

    public boolean agregarPago(Pago pago);

    public boolean actualizarPago(Pago pago);

    public Pago consultarPago(Long idPago);

    public List<Pago> consultarPagos();
    
    public List<Pago> consultarPagosPorEstado(String estado);

    public boolean agregarEmpleado(Empleado empleado);

    public boolean actualizarEmpleado(Empleado empleado);

    public Empleado consultarEmpleado(Long idEmpleado);

    public List<Empleado> consultarEmpleados();
    
    public List<Empleado> consultarEmpleadosPorEstado(Boolean estado);

    public boolean agregarSalario(Salario salario);

    public boolean actualizarSalario(Salario salario);

    public boolean eliminarSalario(Salario salario);

    public Salario consultarSalario(Long idSalario);

    public List<Salario> consultarSalarios();

    public List<DiaTrabajado> consultarDiasTrabajadosPorEmpleado(Empleado empleado);
}
