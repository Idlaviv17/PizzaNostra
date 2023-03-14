package interfaces;

import entidades.Salario;
import java.util.List;

public interface ISalarioDAO {
    public boolean agregar(Salario salario);

    public boolean actualizar(Salario salario);

    public boolean eliminar(Salario salario);

    public Salario consultar(Long idSalario);

    public List<Salario> consultarTodos();
}
