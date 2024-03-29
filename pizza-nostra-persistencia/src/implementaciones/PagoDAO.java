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
            System.err.println("No se pudo agregar el pago");
            return false;
        }
    }

    @Override
    public boolean actualizar(Pago pago) {
        try {
            EntityManager em = this.conexionBD.crearConexion();
            em.getTransaction().begin();
            em.merge(pago);
            em.getTransaction().commit();
            return true;
        } catch (IllegalStateException ex) {
            System.err.println("No se pudo actualizar el pago");
            return false;
        }
    }

    @Override
    public Pago consultar(Long idPago) {
        try {
            EntityManager em = this.conexionBD.crearConexion();
            return em.find(Pago.class, idPago);
        } catch (IllegalStateException ex) {
            System.err.println("No se pudo consultar el pago " + idPago);
            return null;
        }
    }

    @Override
    public List<Pago> consultarTodos() {
        try {
            EntityManager em = this.conexionBD.crearConexion();

            String jpqlQuery = "SELECT j FROM Pago j";
            TypedQuery<Pago> query = em.createQuery(jpqlQuery, Pago.class);

            return query.getResultList();
        } catch (IllegalStateException ex) {
            System.err.println("No se pudieron consultar los pagos");
            return null;
        }
    }

    @Override
    public List<Pago> consultarPorEstado(String estado) {
        try {
            EntityManager em = this.conexionBD.crearConexion();

            String jpqlQuery = "SELECT j FROM Pago j WHERE j.estado = '" + estado + "'";
            TypedQuery<Pago> query = em.createQuery(jpqlQuery, Pago.class);

            return query.getResultList();
        } catch (IllegalStateException ex) {
            System.err.println("No se pudieron consultar los pagos");
            return null;
        }
    }
}
