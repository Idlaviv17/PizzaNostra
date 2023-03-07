package implementaciones;

import entidades.Pago;
import interfaces.IConexionBD;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import interfaces.IPagoDAO;

public class PagoDAO implements IPagoDAO {

    private final IConexionBD conexionBD;

    public PagoDAO(IConexionBD conexionBD) {
        this.conexionBD = conexionBD;
    }

    @Override
    public boolean agregar(Pago pago) {
        try {
            EntityManager em = this.conexionBD.crearConexion();
            em.getTransaction().begin();
            em.persist(pago);
            em.getTransaction().commit();
            return true;
        } catch (IllegalStateException ex) {
            System.err.println("No se pudo agregar el producto");
            ex.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean actualizar(Producto producto) {
        try {
            EntityManager em = this.conexionBD.crearConexion();
            em.getTransaction().begin();
            em.merge(producto);
            em.getTransaction().commit();
            return true;
        } catch (IllegalStateException ex) {
            System.err.println("No se pudo actualizar el producto");
            ex.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean eliminar(Producto producto) {
        try {
            EntityManager em = this.conexionBD.crearConexion();
            em.getTransaction().begin();
            if (!em.contains(producto)) {
                producto = em.merge(producto);
            }
            em.remove(producto);
            em.getTransaction().commit();
            return true;
        } catch (IllegalStateException ex) {
            System.err.println("No se pudo eliminar el producto");
            ex.printStackTrace();
            return false;
        }
    }

    @Override
    public Producto consultar(Long idProducto) {
        try {
            EntityManager em = this.conexionBD.crearConexion();
            return em.find(Producto.class, idProducto);
        } catch (IllegalStateException ex) {
            System.err.println("No se pudo consultar el producto " + idProducto);
            ex.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Producto> consultarTodos() {
        try {
            EntityManager em = this.conexionBD.crearConexion();

            String jpqlQuery = "SELECT j FROM Producto j";
            TypedQuery<Producto> query = em.createQuery(jpqlQuery, Producto.class);

            return query.getResultList();
        } catch (IllegalStateException ex) {
            System.err.println("No se pudieron consultar los productos");
            ex.printStackTrace();
            return null;
        }
    }

}
