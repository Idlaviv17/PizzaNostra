package entidades;

import java.util.Date;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "administrador")
public class Administrador extends Usuario {

    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "id_administrador")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public Administrador() {
    }
    
    public Administrador(Long id) {
        this.id = id;
    }

    public Administrador(String nombre, String apellidos, String correo, String domicilio, String rfc, Boolean estado, String telefono, Date fechaNacimiento) {
        super(nombre, apellidos, correo, domicilio, rfc, estado, telefono, fechaNacimiento);
    }

    public Administrador(Long id, String nombre, String apellidos, String correo, String domicilio, String rfc, String telefono, Date fechaNacimiento) {
        super(id, nombre, apellidos, correo, domicilio, rfc, telefono, fechaNacimiento);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 83 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Administrador other = (Administrador) obj;
        return Objects.equals(this.id, other.id);
    }

    @Override
    public String toString() {
        return "Administrador{" + "id=" + id + '}';
    }
    
}
