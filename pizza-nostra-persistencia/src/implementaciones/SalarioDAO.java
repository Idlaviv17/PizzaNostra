package implementaciones;

import entidades.Salario;
import interfaces.IConexionBD;
import interfaces.ISalarioDAO;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

public class SalarioDAO implements ISalarioDAO {
    
    private final IConexionBD conexionBD;

    public SalarioDAO(IConexionBD conexionBD) {
        this.conexionBD = conexionBD;
    }

    @Override
    public boolean agregar(Salario salario) {
        try {
            EntityManager em = this.conexionBD.crearConexion();
            em.getTransaction().begin();
            em.persist(salario);
            em.getTransaction().commit();
            return true;
        } catch (IllegalStateException ex) {
            System.err.println("No se pudo agregar el salario");
            return false;
        }
    }

    @Override
    public boolean actualizar(Salario salario) {
        try {
            EntityManager em = this.conexionBD.crearConexion();
            em.getTransaction().begin();
            em.merge(salario);
            em.getTransaction().commit();
            return true;
        } catch (IllegalStateException ex) {
            System.err.println("No se pudo actualizar el salario");
            return false;
        }
    }

    @Override
    public boolean eliminar(Salario salario) {
        try {
            EntityManager em = this.conexionBD.crearConexion();
            em.getTransaction().begin();
            if (!em.contains(salario)) {
                salario = em.merge(salario);
            }
            em.remove(salario);
            em.getTransaction().commit();
            return true;
        } catch (IllegalStateException ex) {
            System.err.println("No se pudo eliminar el salario");
            return false;
        }
    }

    @Override
    public Salario consultar(Long idSalario) {
        try {
            EntityManager em = this.conexionBD.crearConexion();
            return em.find(Salario.class, idSalario);
        } catch (IllegalStateException ex) {
            System.err.println("No se pudo consultar el salario " + idSalario);
            return null;
        }
    }

    @Override
    public List<Salario> consultarTodos() {
        try {
            EntityManager em = this.conexionBD.crearConexion();

            String jpqlQuery = "SELECT j FROM Salario j";
            TypedQuery<Salario> query = em.createQuery(jpqlQuery, Salario.class);

            return query.getResultList();
        } catch (IllegalStateException ex) {
            System.err.println("No se pudieron consultar los salarios");
            return null;
        }
    }
    
}
