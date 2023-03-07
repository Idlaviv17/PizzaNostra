/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entidades;

import java.io.Serializable;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author jjavi
 */
@Entity
@Table(name = "empleado")
public class Empleado implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "id_empleado")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    //@OneToMany(mappedBy = "horario", cascade = CascadeType.PERSIST) 
    //@OneToMany
    @JoinColumn(name = "id_horario", nullable = false)
    private List<Horario> horarios;
    
    @OneToMany
    @JoinColumn(name = "id_pago", nullable = false)
    private List<Pago> pagos;

    public Empleado() {
    }

    public Empleado(Long id) {
        this.id = id;
    }
    
    public Empleado(Long id, List<Horario> horarios, List<Pago> pagos) {
        this.id = id;
        this.horarios = horarios;
        this.pagos = pagos;
    }    

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Horario> getHorarios() {
        return horarios;
    }

    public void setHorarios(List<Horario> horarios) {
        this.horarios = horarios;
    }

    public List<Pago> getPagos() {
        return pagos;
    }

    public void setPagos(List<Pago> pagos) {
        this.pagos = pagos;
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
        if (!(object instanceof Empleado)) {
            return false;
        }
        Empleado other = (Empleado) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.Empleado[ id=" + id + " ]";
    }
    
}
