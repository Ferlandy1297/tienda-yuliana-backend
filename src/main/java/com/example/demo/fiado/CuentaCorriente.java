package com.example.demo.fiado;

import com.example.demo.cliente.Cliente;
import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "cuenta_corriente", schema = "tienda_yuliana")
public class CuentaCorriente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cuenta")
    private Integer idCuenta;

    @OneToOne(optional = false)
    @JoinColumn(name = "id_cliente", unique = true, nullable = false)
    private Cliente cliente;

    @Column(nullable = false)
    private BigDecimal saldo = BigDecimal.ZERO;

    @Column(nullable = false)
    private String estado = "AL_DIA"; // AL_DIA, EN_MORA, BLOQUEADO

    // Getter explícito para compatibilidad con IDEs sin AP
    public BigDecimal getSaldo() { return saldo; }

    public void setSaldo(BigDecimal saldo) { this.saldo = saldo; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }
}
