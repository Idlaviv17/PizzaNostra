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
@Table(name = "pago")
public class Pago implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "id_pago")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name="id_empleado")
    private Empleado empleado;
    
    @Column(name = "inicio_periodo", nullable = false)
    @Temporal(TemporalType.DATE)
    private Calendar inicioPeriodo;
    
    @Column(name = "fin_periodo", nullable = false)
    @Temporal(TemporalType.DATE)
    private Calendar finPeriodo;
    
    @Column(name = "fecha", nullable = false)
    @Temporal(TemporalType.DATE)
    private Calendar fecha;
    
    @Column(name = "estado", nullable = false, length = 10, unique = false)
    private String estado;
    
    @Column(name = "comentario", nullable = true, length = 300, unique = false)
    private String comentario;
    
    @Column(name = "horas_trabajadas", nullable = false)
    private Integer horasTrabajadas;

    public Pago() {
    }

    public Pago(Long id) {
        this.id = id;
    }

    public Pago(Empleado empleado, Calendar inicioPeriodo, Calendar finPeriodo, Calendar fecha, String estado, String comentario, Integer horasTrabajadas) {
        this.empleado = empleado;
        this.inicioPeriodo = inicioPeriodo;
        this.finPeriodo = finPeriodo;
        this.fecha = fecha;
        this.estado = estado;
        this.comentario = comentario;
        this.horasTrabajadas = horasTrabajadas;
    }

    public Pago(Long id, Empleado empleado, Calendar inicioPeriodo, Calendar finPeriodo, Calendar fecha, String estado, String comentario, Integer horasTrabajadas) {
        this.id = id;
        this.empleado = empleado;
        this.inicioPeriodo = inicioPeriodo;
        this.finPeriodo = finPeriodo;
        this.fecha = fecha;
        this.estado = estado;
        this.comentario = comentario;
        this.horasTrabajadas = horasTrabajadas;
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

    public Calendar getInicioPeriodo() {
        return inicioPeriodo;
    }

    public void setInicioPeriodo(Calendar inicioPeriodo) {
        this.inicioPeriodo = inicioPeriodo;
    }

    public Calendar getFinPeriodo() {
        return finPeriodo;
    }

    public void setFinPeriodo(Calendar finPeriodo) {
        this.finPeriodo = finPeriodo;
    }

    public Calendar getFecha() {
        return fecha;
    }

    public void setFecha(Calendar fecha) {
        this.fecha = fecha;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public Integer getHorasTrabajadas() {
        return horasTrabajadas;
    }

    public void setHorasTrabajadas(Integer horasTrabajadas) {
        this.horasTrabajadas = horasTrabajadas;
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
        if (!(object instanceof Pago)) {
            return false;
        }
        Pago other = (Pago) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Pago{" + "id=" + id + ", empleado=" + empleado + ", inicioPeriodo=" + inicioPeriodo + ", finPeriodo=" + finPeriodo + ", fecha=" + fecha + ", estado=" + estado + ", comentario=" + comentario + ", horasTrabajadas=" + horasTrabajadas + '}';
    }

}
