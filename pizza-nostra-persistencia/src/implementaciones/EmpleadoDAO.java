package implementaciones;

import entidades.Empleado;
import interfaces.IConexionBD;
import interfaces.IEmpleadoDAO;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

public class EmpleadoDAO implements IEmpleadoDAO {

    private final IConexionBD conexionBD;

    public EmpleadoDAO(IConexionBD conexionBD) {
        this.conexionBD = conexionBD;
    }

    @Override
    public boolean agregar(Empleado empleado) {
        try {
            EntityManager em = this.conexionBD.crearConexion();
            em.getTransaction().begin();
            em.persist(empleado);
            em.getTransaction().commit();
            return true;
        } catch (IllegalStateException ex) {
            System.err.println("No se pudo agregar el empleado");
            ex.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean actualizar(Empleado empleado) {
        try {
            EntityManager em = this.conexionBD.crearConexion();
            em.getTransaction().begin();
            em.merge(empleado);
            em.getTransaction().commit();
            return true;
        } catch (IllegalStateException ex) {
            System.err.println("No se pudo actualizar el empleado");
            ex.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean eliminar(Empleado empleado) {
        try {
            EntityManager em = this.conexionBD.crearConexion();
            em.getTransaction().begin();
            if (!em.contains(empleado)) {
                empleado = em.merge(empleado);
            }
            em.remove(empleado);
            em.getTransaction().commit();
            return true;
        } catch (IllegalStateException ex) {
            System.err.println("No se pudo eliminar el empleado");
            ex.printStackTrace();
            return false;
        }
    }

    @Override
    public Empleado consultar(Long idEmpleado) {
        try {
            EntityManager em = this.conexionBD.crearConexion();
            return em.find(Empleado.class, idEmpleado);
        } catch (IllegalStateException ex) {
            System.err.println("No se pudo consultar el empleado " + idEmpleado);
            ex.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Empleado> consultarTodos() {
        try {
            EntityManager em = this.conexionBD.crearConexion();

            String jpqlQuery = "SELECT j FROM Empleado j";
            TypedQuery<Empleado> query = em.createQuery(jpqlQuery, Empleado.class);

            return query.getResultList();
        } catch (IllegalStateException ex) {
            System.err.println("No se pudieron consultar los empleados");
            ex.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Empleado> consultarPorEstado(Boolean estado) {
        try {
            EntityManager em = this.conexionBD.crearConexion();

            String jpqlQuery = "SELECT j FROM Empleado j WHERE j.estado = " + estado;
            TypedQuery<Empleado> query = em.createQuery(jpqlQuery, Empleado.class);

            return query.getResultList();
        } catch (IllegalStateException ex) {
            System.err.println("No se pudieron consultar los empleados");
            ex.printStackTrace();
            return null;
        }
    }
}