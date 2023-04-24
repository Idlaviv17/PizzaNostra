package implementaciones;

import entidades.DiaTrabajado;
import entidades.Empleado;
import interfaces.IConexionBD;
import interfaces.IDiaTrabajadoDAO;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

public class DiaTrabajadoDAO implements IDiaTrabajadoDAO {

    private final IConexionBD conexionBD;

    public DiaTrabajadoDAO(IConexionBD conexionBD) {
        this.conexionBD = conexionBD;
    }

    @Override
    public boolean agregar(DiaTrabajado diaTrabajado) {
        try {
            EntityManager em = this.conexionBD.crearConexion();
            em.getTransaction().begin();
            em.persist(diaTrabajado);
            em.getTransaction().commit();
            return true;
        } catch (IllegalStateException ex) {
            System.err.println("No se pudo agregar el dia trabajado");
            return false;
        }
    }

    @Override
    public boolean actualizar(DiaTrabajado diaTrabajado) {
        try {
            EntityManager em = this.conexionBD.crearConexion();
            em.getTransaction().begin();
            em.merge(diaTrabajado);
            em.getTransaction().commit();
            return true;
        } catch (IllegalStateException ex) {
            System.err.println("No se pudo actualizar el dia trabajado");
            return false;
        }
    }

    @Override
    public boolean eliminar(DiaTrabajado diaTrabajado) {
        try {
            EntityManager em = this.conexionBD.crearConexion();
            em.getTransaction().begin();
            if (!em.contains(diaTrabajado)) {
                diaTrabajado = em.merge(diaTrabajado);
            }
            em.remove(diaTrabajado);
            em.getTransaction().commit();
            return true;
        } catch (IllegalStateException ex) {
            System.err.println("No se pudo eliminar el dia trabajado");
            return false;
        }
    }

    @Override
    public DiaTrabajado consultar(Long idDiaTrabajado) {
        try {
            EntityManager em = this.conexionBD.crearConexion();
            return em.find(DiaTrabajado.class, idDiaTrabajado);
        } catch (IllegalStateException ex) {
            System.err.println("No se pudo consultar el dia rabajado " + idDiaTrabajado);
            return null;
        }
    }

    @Override
    public List<DiaTrabajado> consultarTodos() {
        try {
            EntityManager em = this.conexionBD.crearConexion();

            String jpqlQuery = "SELECT j FROM dia_trabajado j";
            TypedQuery<DiaTrabajado> query = em.createQuery(jpqlQuery, DiaTrabajado.class);

            return query.getResultList();
        } catch (IllegalStateException ex) {
            System.err.println("No se pudieron consultar los dias trabajados");
            return null;
        }
    }

    @Override
    public List<DiaTrabajado> consultarPorEmpleado(Empleado empleado) {
        try {
            EntityManager em = this.conexionBD.crearConexion();

            String jpqlQuery = "SELECT d FROM DiaTrabajado d JOIN FETCH d.empleado e WHERE e = :empleado";
            TypedQuery<DiaTrabajado> query = em.createQuery(jpqlQuery, DiaTrabajado.class);
            query.setParameter("empleado", empleado);

            return query.getResultList();
        } catch (IllegalStateException ex) {
            System.err.println("No se pudieron consultar los dias trabajados");
            return null;
        }
    }

}
