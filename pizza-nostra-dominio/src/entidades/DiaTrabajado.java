package entidades;

import java.io.Serializable;
import java.util.Calendar;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "dia_trabajado")
public class DiaTrabajado implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "id_dia_trabajado")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name="id_empleado")
    private Empleado empleado;
    
    @Column(name = "hora_entrada", nullable = false)
    @Temporal(TemporalType.TIME)
    private Calendar horaEntrada;
    
    @Column(name = "hora_salida", nullable = false)
    @Temporal(TemporalType.TIME)
    private Calendar horaSalida;
    
    @Column(name = "fecha", nullable = false)
    @Temporal(TemporalType.DATE)
    private Calendar fecha;

    public DiaTrabajado() {
    }

    public DiaTrabajado(Long id) {
        this.id = id;
    }

    public DiaTrabajado(Empleado empleado, Calendar horaEntrada, Calendar horaSalida, Calendar fecha) {
        this.empleado = empleado;
        this.horaEntrada = horaEntrada;
        this.horaSalida = horaSalida;
        this.fecha = fecha;
    }

    public DiaTrabajado(Long id, Empleado empleado, Calendar horaEntrada, Calendar horaSalida, Calendar fecha) {
        this.id = id;
        this.empleado = empleado;
        this.horaEntrada = horaEntrada;
        this.horaSalida = horaSalida;
        this.fecha = fecha;
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
        if (!(object instanceof DiaTrabajado)) {
            return false;
        }
        DiaTrabajado other = (DiaTrabajado) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "DiaTrabajado{" + "id=" + id + ", empleado=" + empleado + ", horaEntrada=" + horaEntrada + ", horaSalida=" + horaSalida + ", fecha=" + fecha + '}';
    }
    
}
