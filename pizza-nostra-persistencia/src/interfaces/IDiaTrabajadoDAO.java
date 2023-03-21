package interfaces;

import entidades.DiaTrabajado;
import entidades.Empleado;
import java.util.List;

public interface IDiaTrabajadoDAO {
    public boolean agregar(DiaTrabajado diaTrabajado);

    public boolean actualizar(DiaTrabajado diaTrabajado);

    public boolean eliminar(DiaTrabajado diaTrabajado);

    public DiaTrabajado consultar(Long idDiaTrabajado);

    public List<DiaTrabajado> consultarTodos();
    
    public List<DiaTrabajado> consultarPorEmpleado(Empleado idEmpleado);
}
