package control;

import entidades.Empleado;
import entidades.Pago;
import entidades.Salario;
import implementaciones.DAOSFactory;
import interfaces.IEmpleadoDAO;
import interfaces.IPagoDAO;
import interfaces.ISalarioDAO;
import java.util.List;

public class Control implements IControl {

    private IPagoDAO pagos;
    private IEmpleadoDAO empleados;
    private ISalarioDAO salarios;

    public Control() {
        this.pagos = DAOSFactory.crearPagosDAO();
        this.empleados = DAOSFactory.crearEmpleadosDAO();
        this.salarios = DAOSFactory.crearSalariosDAO();
    }

    @Override
    public boolean agregarPago(Pago pago) {
        return pagos.agregar(pago);
    }

    @Override
    public boolean actualizarPago(Pago pago) {
        return pagos.actualizar(pago);
    }

    @Override
    public boolean eliminarPago(Pago pago) {
        return pagos.eliminar(pago);
    }

    @Override
    public Pago consultarPago(Long idPago) {
        return pagos.consultar(idPago);
    }

    @Override
    public List<Pago> consultarPagos() {
        return pagos.consultarTodos();
    }

    @Override
    public boolean agregarEmpleado(Empleado empleado) {
        return empleados.agregar(empleado);
    }

    @Override
    public boolean actualizarEmpleado(Empleado empleado) {
        return empleados.actualizar(empleado);
    }

    @Override
    public boolean eliminarEmpleado(Empleado empleado) {
        return empleados.eliminar(empleado);
    }

    @Override
    public Empleado consultarEmpleado(Long idEmpleado) {
        return empleados.consultar(idEmpleado);
    }

    @Override
    public List<Empleado> consultarEmpleados() {
        return empleados.consultarTodos();
    }

    @Override
    public boolean agregarSalario(Salario salario) {
        return salarios.agregar(salario);
    }

    @Override
    public boolean actualizarSalario(Salario salario) {
        return salarios.actualizar(salario);
    }

    @Override
    public boolean eliminarSalario(Salario salario) {
        return salarios.eliminar(salario);
    }

    @Override
    public Salario consultarSalario(Long idSalario) {
        return salarios.consultar(idSalario);
    }

    @Override
    public List<Salario> consultarSalarios() {
        return salarios.consultarTodos();
    }
    
}
