package control;

import entidades.Pago;
import implementaciones.DAOSFactory;
import interfaces.IPagoDAO;
import java.util.List;

public class Control implements IControl {
    
    private IPagoDAO pagos;

    public Control() {
        this.pagos = DAOSFactory.crearPagosDAO();
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
    
}
