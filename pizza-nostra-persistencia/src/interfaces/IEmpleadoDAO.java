package interfaces;

import entidades.Empleado;
import java.util.List;

public interface IEmpleadoDAO {
    public boolean agregar(Empleado empleado);

    public boolean actualizar(Empleado empleado);

    public Empleado consultar(Long idEmpleado);

    public List<Empleado> consultarTodos();
    
    public List<Empleado> consultarPorEstado(Boolean estado);
}
