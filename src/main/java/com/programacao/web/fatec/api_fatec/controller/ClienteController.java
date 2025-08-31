package com.programacao.web.fatec.api_fatec.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;

import com.programacao.web.fatec.api_fatec.domain.cliente.ClienteRepository;
import com.programacao.web.fatec.api_fatec.entities.Cliente;

import jakarta.annotation.PostConstruct;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

    @Autowired
    private ClienteRepository clienteRepository;

    @PostConstruct()
    public void dadosIniciais() {
        clienteRepository.save(new Cliente(null, "Danilo", "Rua das Flores, 123"));
        clienteRepository.save(new Cliente(null, "João", "Avenida Central, 456"));
        clienteRepository.save(new Cliente(null, "Maria", "Rua da Paz, 789"));
    }

    // CREATE - Criar um novo cliente
    @PostMapping
    public Cliente createCliente(@RequestBody Cliente cliente) {
        cliente.setId(null); // Garantir que o ID seja gerado automaticamente
        return clienteRepository.save(cliente);
    }

    // READ - Listar todos os clientes
    @GetMapping
    public List<Cliente> getAllClientes() {
        return clienteRepository.findAll();
    }

    // READ - Buscar cliente por ID
    @GetMapping("/{id}")
    public ResponseEntity<Cliente> getClienteById(@PathVariable Long id) {
        Optional<Cliente> cliente = clienteRepository.findById(id);
        return cliente.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    // UPDATE - Atualizar cliente existente
    @PutMapping("/{id}")
    public ResponseEntity<Cliente> updateCliente(@PathVariable Long id, @RequestBody Cliente clienteAtualizado) {
        Optional<Cliente> clienteExistente = clienteRepository.findById(id);
        
        if (clienteExistente.isPresent()) {
            Cliente cliente = clienteExistente.get();
            cliente.setNome(clienteAtualizado.getNome());
            cliente.setEndereco(clienteAtualizado.getEndereco());
            return ResponseEntity.ok(clienteRepository.save(cliente));
        }
        
        return ResponseEntity.notFound().build();
    }

    // DELETE - Deletar cliente por ID
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCliente(@PathVariable Long id) {
        if (clienteRepository.existsById(id)) {
            clienteRepository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}
