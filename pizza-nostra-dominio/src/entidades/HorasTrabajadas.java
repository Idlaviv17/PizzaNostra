/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entidades;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @author jjavi
 */
@Entity
@Table(name = "horas_trabajadas")
public class HorasTrabajadas implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "id_horas_trabajadas")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @Column(name = "hora_entrada", nullable = false)
    private Float horaEntrada;
    
    @Column(name = "hora_salida", nullable = false)
    private Float horaSalida;

    public HorasTrabajadas() {
    }

    public HorasTrabajadas(Long id) {
        this.id = id;
    }

    public HorasTrabajadas(Long id, Float horaEntrada, Float horaSalida) {
        this.id = id;
        this.horaEntrada = horaEntrada;
        this.horaSalida = horaSalida;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Float getHoraEntrada() {
        return horaEntrada;
    }

    public void setHoraEntrada(Float horaEntrada) {
        this.horaEntrada = horaEntrada;
    }

    public Float getHoraSalida() {
        return horaSalida;
    }

    public void setHoraSalida(Float horaSalida) {
        this.horaSalida = horaSalida;
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
        if (!(object instanceof HorasTrabajadas)) {
            return false;
        }
        HorasTrabajadas other = (HorasTrabajadas) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "HorasTrabajadas{" + "id=" + id + ", horaEntrada=" + horaEntrada + ", horaSalida=" + horaSalida + '}';
    }
    
}
