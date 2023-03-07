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
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author jjavi
 */
@Entity
@Table(name = "horario")
public class Horario implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "id_horario")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @Column(name = "dia", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Calendar dia;
    
    @Column(name = "hora_inicio", nullable = false)
    private Float horaInicio;
    
    @Column(name = "hora_fin", nullable = false)
    private Float horaFin;
    
    @OneToOne
    @JoinColumn(name = "id_horas_trabajadas", nullable = false)
    private HorasTrabajadas horasTrabajadas;

    public Horario() {
    }

    public Horario(Long id) {
        this.id = id;
    }

    public Horario(Long id, Calendar dia, Float horaInicio, Float horaFin, HorasTrabajadas horasTrabajadas) {
        this.id = id;
        this.dia = dia;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
        this.horasTrabajadas = horasTrabajadas;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Calendar getDia() {
        return dia;
    }

    public void setDia(Calendar dia) {
        this.dia = dia;
    }

    public Float getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(Float horaInicio) {
        this.horaInicio = horaInicio;
    }

    public Float getHoraFin() {
        return horaFin;
    }

    public void setHoraFin(Float horaFin) {
        this.horaFin = horaFin;
    }

    public HorasTrabajadas getHorasTrabajadas() {
        return horasTrabajadas;
    }

    public void setHorasTrabajadas(HorasTrabajadas horasTrabajadas) {
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
        if (!(object instanceof Horario)) {
            return false;
        }
        Horario other = (Horario) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Horario{" + "id=" + id + ", dia=" + dia + ", horaInicio=" + horaInicio + ", horaFin=" + horaFin + ", horasTrabajadas=" + horasTrabajadas + '}';
    }
    
}
