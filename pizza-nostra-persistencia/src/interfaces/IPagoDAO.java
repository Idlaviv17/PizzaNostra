package interfaces;

import entidades.Pago;
import java.util.List;

public interface IPagoDAO {
    public boolean agregar(Pago pago);

    public boolean actualizar(Pago pago);

    public Pago consultar(Long idPago);

    public List<Pago> consultarTodos();
    
    public List<Pago> consultarPorEstado(String estado);
}
