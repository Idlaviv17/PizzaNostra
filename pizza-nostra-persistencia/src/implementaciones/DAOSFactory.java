package implementaciones;

import interfaces.IConexionBD;
import interfaces.IPagoDAO;

public class DAOSFactory {
    
    private static final IConexionBD conexionBD = new ConexionBD();
    
    public static IPagoDAO crearPagosDAO() {
        return new PagoDAO(conexionBD);
    }
}
