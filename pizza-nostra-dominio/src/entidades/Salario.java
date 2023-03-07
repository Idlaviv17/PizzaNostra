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

/**
 *
 * @author jjavi
 */
@Entity
public class Salario implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "id_salario")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @Column(name = "rol", nullable = false, length = 100, unique = true)
    private String rol;
    
    @Column(name = "coste_por_hora", nullable = false)
    private Float costePorHora;

    public Salario() {
    }

    public Salario(Long id) {
        this.id = id;
    }

    public Salario(Long id, String rol, Float costePorHora) {
        this.id = id;
        this.rol = rol;
        this.costePorHora = costePorHora;
    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public Float getCostePorHora() {
        return costePorHora;
    }

    public void setCostePorHora(Float costePorHora) {
        this.costePorHora = costePorHora;
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
        if (!(object instanceof Salario)) {
            return false;
        }
        Salario other = (Salario) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Salario{" + "id=" + id + ", rol=" + rol + ", costePorHora=" + costePorHora + '}';
    }
    
}
