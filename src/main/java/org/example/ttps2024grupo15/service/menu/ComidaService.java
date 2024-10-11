package org.example.ttps2024grupo15.service.menu;

import jakarta.transaction.Transactional;
import org.example.ttps2024grupo15.dao.menu.ComidaDAO;
import org.example.ttps2024grupo15.model.carta.producto.Comida;
import org.example.ttps2024grupo15.model.request.menu.ComidaRequest;

import java.util.List;

public class ComidaService {
    private ComidaDAO comidaDAO;
    public ComidaService(ComidaDAO comidaDAO) {
        this.comidaDAO = comidaDAO;
    }

    public Comida getComidaById(Long id) {
        return comidaDAO.getById(id);
    }

    public List<Comida> getComidas() {
        return comidaDAO.getAll();
    }
    public List<Comida> getComidasByNombre(String nombre) {
        return comidaDAO.findByNombre(nombre);
    }

    @Transactional
    public Comida saveComida(ComidaRequest comidaRequest) {
        this.sanitizeComidaRequest(comidaRequest);
        Comida comida = new Comida(comidaRequest.getNombre(), comidaRequest.getTipoComida(), comidaRequest.getPrecio());
        return comidaDAO.save(comida);
    }

    public void deleteComida(Long id) {
        comidaDAO.delete(id);
    }

    private void sanitizeComidaRequest(ComidaRequest comidaRequest){
        if(comidaRequest.getNombre() == null || comidaRequest.getNombre().isEmpty()){
            throw new IllegalArgumentException("Nombre de comida no puede ser nulo o vacio");
        }
        if(comidaRequest.getPrecio() == null || comidaRequest.getPrecio() < 0){
            throw new IllegalArgumentException("Precio de comida no puede ser nulo o negativo");
        }
        if(comidaRequest.getTipoComida() == null){
            throw new IllegalArgumentException("Tipo de comida no puede ser nulo o no existir");
        }
    }
}
