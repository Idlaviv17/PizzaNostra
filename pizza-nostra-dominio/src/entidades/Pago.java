/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entidades;

import java.io.Serializable;
import java.util.Calendar;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author jjavi
 */
@Entity
public class Pago implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "id_pago")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @OneToOne
    @JoinColumn(name = "id_salario", nullable = false)
    private Salario salario;
    
    @Column(name = "fecha", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Calendar fecha;
    
    @Column(name = "estado", nullable = false)
    private Boolean estado;
    
    @Column(name = "rol", nullable = true, length = 400, unique = false)
    private String comentario;
    
    @Column(name = "horas_trabajadas", nullable = false)
    private Float horasTrabajadas;

    public Pago() {
    }

    public Pago(Long id) {
        this.id = id;
    }

    public Pago(Long id, Salario salario, Calendar fecha, Boolean estado, String comentario, Float horasTrabajadas) {
        this.id = id;
        this.salario = salario;
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

    public Salario getSalario() {
        return salario;
    }

    public void setSalario(Salario salario) {
        this.salario = salario;
    }

    public Calendar getFecha() {
        return fecha;
    }

    public void setFecha(Calendar fecha) {
        this.fecha = fecha;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public Float getHorasTrabajadas() {
        return horasTrabajadas;
    }

    public void setHorasTrabajadas(Float horasTrabajadas) {
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
        return "Pago{" + "id=" + id + ", salario=" + salario + ", fecha=" + fecha + ", estado=" + estado + ", comentario=" + comentario + ", horasTrabajadas=" + horasTrabajadas + '}';
    }
  
}
