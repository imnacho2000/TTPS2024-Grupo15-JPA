package org.example.ttps2024grupo15.model.carta;

import jakarta.persistence.*;


import java.util.List;

@Entity
public class CartaSemanal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToMany(mappedBy = "cartaSemanal")
    private List<CartaDelDia> cartas;

    public CartaSemanal(List<CartaDelDia> cartas) {
        this.cartas = this.sanitizeCartasConstructor(cartas);
    }
    public CartaSemanal() {
    }
    public List<CartaDelDia> getCartas() {
        return cartas;
    }

    public CartaDelDia getCartaPorDia(DiaSemana dia) {
        return this.cartas.stream().filter(carta -> carta.getDiaSemana().equals(dia)).findFirst().orElse(null);
    }
    public void setCartaPorDia(DiaSemana dia, CartaDelDia carta) {
        CartaDelDia  cartaDelDiaVieja = this.getCartaPorDia(dia);
        if(cartaDelDiaVieja != null) {
            this.cartas.remove(cartaDelDiaVieja);
        }
        this.cartas.add(carta);
    }
    public List<CartaDelDia> sanitizeCartasConstructor(List<CartaDelDia> cartas) {
        return cartas.size() <= 7 ? cartas : cartas.subList(0, 7);
    }

    public Long getId() {
        return id;
    }

    public void setCartas(List<CartaDelDia> cartas) {
        this.cartas = cartas;
    }

}
