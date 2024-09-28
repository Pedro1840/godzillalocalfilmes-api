package com.godzillalocalfilmes.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.godzillalocalfilmes.api.controller.AdminController;
import com.godzillalocalfilmes.api.exception.GlobalExceptionHandler;
import com.godzillalocalfilmes.api.model.Cliente;
import com.godzillalocalfilmes.api.security.JwtAuthenticationFilter;
import com.godzillalocalfilmes.api.security.JwtTokenProvider;
import com.godzillalocalfilmes.api.service.ClienteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AdminController.class)
@Import(GlobalExceptionHandler.class) // Certifique-se de importar o handler global de exceções
public class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ClienteService clienteService;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter; // Mock do filtro de autenticação JWT

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        objectMapper = new ObjectMapper();
    }

    // Teste para acessar endpoint protegido sem autenticação
    @Test
    public void testCriarCliente_Unauthorized() throws Exception {
        // Dados de entrada
        Cliente cliente = new Cliente();
        cliente.setEmail("novo@cliente.com");
        cliente.setNome("Novo Cliente");
        cliente.setSenha("senha123");

        // Convertendo objeto para JSON
        String clienteJson = objectMapper.writeValueAsString(cliente);

        // Executando a requisição POST sem autenticação
        mockMvc.perform(post("/admin/clientes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(clienteJson))
                .andExpect(status().isForbidden());
    }

    @Test
    public void testDeleteCliente_Unauthorized() throws Exception {
        Long clienteId = 1L;

        // Executando a requisição DELETE sem autenticação
        mockMvc.perform(delete("/admin/clientes/delete/{id}", clienteId.toString()))
                .andExpect(status().isForbidden());
    }

    // Teste para acessar endpoint protegido com usuário sem role ADMIN
    @Test
    @WithMockUser(username = "usuario@exemplo.com", roles = { "USER" })
    public void testCriarCliente_Forbidden() throws Exception {
        // Dados de entrada
        Cliente cliente = new Cliente();
        cliente.setEmail("novo@cliente.com");
        cliente.setNome("Novo Cliente");
        cliente.setSenha("senha123");

        // Convertendo objeto para JSON
        String clienteJson = objectMapper.writeValueAsString(cliente);

        // Executando a requisição POST com role USER
        mockMvc.perform(post("/admin/clientes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(clienteJson))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "usuario@exemplo.com", roles = { "USER" })
    public void testDeleteCliente_Forbidden() throws Exception {
        Long clienteId = 1L;

        // Executando a requisição DELETE com role USER
        mockMvc.perform(delete("/admin/clientes/delete/{id}", clienteId.toString()))
                .andExpect(status().isForbidden());
    }
}
