package entidades;

import java.io.Serializable;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "empleado")
public class Empleado extends Usuario {

    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "id_empleado")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Pago> pagos;
    
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Horario> horario;
    
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DiaTrabajado> diasTrabajados;
    
    @OneToOne
    @JoinColumn(name="id_periodo_vacacional", nullable = true)
    private PeriodoVacacional vacacion;

    public Empleado() {
    }

    public Empleado(Long id) {
        this.id = id;
    }

    public Empleado(Long id, List<Pago> pagos, List<Horario> horario, List<DiaTrabajado> diasTrabajados, PeriodoVacacional vacacion) {
        this.id = id;
        this.pagos = pagos;
        this.horario = horario;
        this.diasTrabajados = diasTrabajados;
        this.vacacion = vacacion;
    }

    public Empleado(Long id, List<Pago> pagos, List<Horario> horario, List<DiaTrabajado> diasTrabajados, PeriodoVacacional vacacion, String nombre, String apellidos, String correo, String domicilio, String rfc, Boolean estado, String telefono, Calendar fechaNacimiento) {
        super(nombre, apellidos, correo, domicilio, rfc, estado, telefono, fechaNacimiento);
        this.id = id;
        this.pagos = pagos;
        this.horario = horario;
        this.diasTrabajados = diasTrabajados;
        this.vacacion = vacacion;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Pago> getPagos() {
        return pagos;
    }

    public void setPagos(List<Pago> pagos) {
        this.pagos = pagos;
    }

    public List<Horario> getHorario() {
        return horario;
    }

    public void setHorario(List<Horario> horario) {
        this.horario = horario;
    }

    public List<DiaTrabajado> getDiasTrabajados() {
        return diasTrabajados;
    }

    public void setDiasTrabajados(List<DiaTrabajado> diasTrabajados) {
        this.diasTrabajados = diasTrabajados;
    }

    public PeriodoVacacional getVacacion() {
        return vacacion;
    }

    public void setVacacion(PeriodoVacacional vacacion) {
        this.vacacion = vacacion;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 43 * hash + Objects.hashCode(this.id);
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
        final Empleado other = (Empleado) obj;
        return Objects.equals(this.id, other.id);
    }

    @Override
    public String toString() {
        return "Empleado{" + "id=" + id + ", pagos=" + pagos + ", horario=" + horario + ", diasTrabajados=" + diasTrabajados + ", vacacion=" + vacacion + '}';
    }

}
