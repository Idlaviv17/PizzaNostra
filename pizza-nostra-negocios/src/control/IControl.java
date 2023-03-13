package control;

import entidades.Pago;
import java.util.List;

public interface IControl {
    
    public boolean agregarPago(Pago pago);

    public boolean actualizarPago(Pago pago);

    public boolean eliminarPago(Pago pago);

    public Pago consultarPago(Long idPago);

    public List<Pago> consultarPagos();
    
}
