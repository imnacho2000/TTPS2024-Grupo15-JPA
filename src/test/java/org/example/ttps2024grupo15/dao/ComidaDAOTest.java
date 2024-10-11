package org.example.ttps2024grupo15.dao;

import org.example.ttps2024grupo15.dao.menu.impl.ComidaDAOHibernateJPA;
import org.example.ttps2024grupo15.model.carta.producto.Comida;
import org.example.ttps2024grupo15.model.carta.producto.TipoComida;
import org.example.ttps2024grupo15.model.request.menu.ComidaRequest;
import org.example.ttps2024grupo15.service.menu.ComidaService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ComidaDAOTest {
    private ComidaDAOHibernateJPA comidaDAO;
    private ComidaService comidaService;

    @BeforeAll
    public void setUp(){
        this.comidaDAO = new ComidaDAOHibernateJPA();
        this.comidaService = new ComidaService(comidaDAO);
    }


    @ParameterizedTest
    @MethodSource("createComidaRequestWithData")
    @Order(1)
    public void testCreateComida(ComidaRequest comidaRequest) {
        Comida comida = this.comidaService.saveComida(comidaRequest);
        this.testQueryAndValidateComidaById(comida.getId(), comidaRequest);
    }

    @Test
    @Order(2)
    public void testMassiveQuery() {
        List<Comida> comidas = this.comidaService.getComidas();
        assertNotNull(comidas);
        assertEquals(3, comidas.size());
    }

    @Test
    @Order(3)
    public void testQueryByName() {
        List<Comida> comidas = this.comidaService.getComidasByNombre("No existe");
        assertNotNull(comidas);
        assertEquals(0, comidas.size());

        comidas = this.comidaService.getComidasByNombre("Milanesa");
        assertNotNull(comidas);
        assertEquals(1, comidas.size());
        Comida comida = comidas.get(0);
        assertEquals("Milanesa", comida.getNombre());
        assertEquals(TipoComida.PLATO_PRINCIPAL, comida.getTipoComida());
        assertEquals(2500.0, comida.getPrecio());
    }

    @Test
    @Order(4)
    public void testQueryByPrice() {
        List<Comida> comidas = this.comidaService.getComidasByPrecio(Double.valueOf("0.00"));
        assertNotNull(comidas);
        assertEquals(0, comidas.size());

        comidas = this.comidaService.getComidasByPrecio(1500.0);
        assertNotNull(comidas);
        assertEquals(1, comidas.size());
        Comida comida = comidas.get(0);
        assertEquals("Ensalada", comida.getNombre());
        assertEquals(TipoComida.ENTRADA, comida.getTipoComida());
        assertEquals(1500.0, comida.getPrecio());
    }

    @Test
    @Order(5)
    public void testQueryByPriceMultipleResults() {
        List<Comida> comidas = this.comidaService.getComidasByPrecio(Double.valueOf("2500.0"));
        assertNotNull(comidas);
        assertEquals(2, comidas.size());
    }

    @Test
    @Order(6)
    public void testDeleteComidaById() {
        List<Comida> comidas = this.comidaService.getComidas();
        assertNotNull(comidas);
        assertEquals(3, comidas.size());
        this.comidaService.deleteComida(comidas.get(0).getId());
        comidas = this.comidaService.getComidas();
        assertNotNull(comidas);
        assertEquals(2, comidas.size());
    }

    @Test
    @Order(7)
    public void testDeleteComida(){
        List<Comida> comidas = this.comidaService.getComidas();
        assertNotNull(comidas);
        assertEquals(2, comidas.size());
        this.comidaService.deleteComida(comidas.get(0));
        comidas = this.comidaService.getComidas();
        assertNotNull(comidas);
        assertEquals(1, comidas.size());
    }
    @Test
    @Order(8)
    public void deleteAllComidas(){
        List<Comida> comidas = this.comidaService.getComidas();
        for(Comida comida : comidas){
            this.comidaService.deleteComida(comida);
        }
        comidas = this.comidaService.getComidas();
        assertNotNull(comidas);
        assertEquals(0, comidas.size());
    }

    private Stream<Arguments> createComidaRequestWithData() {
        return Stream.of(
                Arguments.of(this.createComidaRequest("Milanesa", 2500.0, TipoComida.PLATO_PRINCIPAL)),
                Arguments.of(this.createComidaRequest("Ensalada", 1500.0, TipoComida.ENTRADA)),
                Arguments.of(this.createComidaRequest("Pizza", 2500.0, TipoComida.PLATO_PRINCIPAL))
        );
    }

    private void testQueryAndValidateComidaById(Long id, ComidaRequest comidaRequest) {
        Comida comida = this.comidaService.getComidaById(id);
        assertNotNull(comida);
        assertEquals(comidaRequest.getTipoComida(), comida.getTipoComida());
        assertEquals(comidaRequest.getNombre(), comida.getNombre());
        assertEquals(comidaRequest.getPrecio(), comida.getPrecio());
    }

    private ComidaRequest createComidaRequest(String nombre, Double precio, TipoComida tipoComida) {
        ComidaRequest comidaRequest = new ComidaRequest();
        comidaRequest.setNombre(nombre);
        comidaRequest.setPrecio(precio);
        comidaRequest.setTipoComida(tipoComida);
        return comidaRequest;
    }



}
