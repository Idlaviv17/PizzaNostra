package implementaciones;

import interfaces.IConexionBD;
import interfaces.IEmpleadoDAO;
import interfaces.IPagoDAO;
import interfaces.ISalarioDAO;

public class DAOSFactory {
    
    private static final IConexionBD conexionBD = new ConexionBD();
    
    public static IPagoDAO crearPagosDAO() {
        return new PagoDAO(conexionBD);
    }
    
    public static IEmpleadoDAO crearEmpleadosDAO() {
        return new EmpleadoDAO(conexionBD);
    }
    
    public static ISalarioDAO crearSalariosDAO() {
        return new SalarioDAO(conexionBD);
    }
}
