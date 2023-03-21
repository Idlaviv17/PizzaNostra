package entidades;

import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "periodo_vacacional")
public class PeriodoVacacional implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "id_periodo_vacacional")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @OneToOne
    @JoinColumn(name="id_empleado")
    private Empleado empleado;
    
    @Column(name = "fecha_inicio", nullable = false)
    private LocalDate fechaInicio;

    @Column(name = "fecha_final", nullable = false)
    private LocalDate fechaFinal;

    public PeriodoVacacional() {
    }

    public PeriodoVacacional(Long id) {
        this.id = id;
    }

    public PeriodoVacacional(Empleado empleado, LocalDate fechaInicio, LocalDate fechaFinal) {
        this.empleado = empleado;
        this.fechaInicio = fechaInicio;
        this.fechaFinal = fechaFinal;
    }

    public PeriodoVacacional(Long id, Empleado empleado, LocalDate fechaInicio, LocalDate fechaFinal) {
        this.id = id;
        this.empleado = empleado;
        this.fechaInicio = fechaInicio;
        this.fechaFinal = fechaFinal;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Empleado getEmpleado() {
        return empleado;
    }

    public void setEmpleado(Empleado empleado) {
        this.empleado = empleado;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDate getFechaFinal() {
        return fechaFinal;
    }

    public void setFechaFinal(LocalDate fechaFinal) {
        this.fechaFinal = fechaFinal;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PeriodoVacacional)) {
            return false;
        }
        PeriodoVacacional other = (PeriodoVacacional) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "PeriodoVacacional{" + "id=" + id + ", empleado=" + empleado + ", fechaInicio=" + fechaInicio + ", fechaFinal=" + fechaFinal + '}';
    }
    
}
