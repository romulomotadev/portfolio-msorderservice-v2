package github.romulomotadev.msclients.controller;

import github.romulomotadev.msclients.dto.ClientDto;
import github.romulomotadev.msclients.service.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/clients")
@RequiredArgsConstructor
public class ClientController {

    private final ClientService clientService;


    //================== POST ====================

    @PostMapping
    public ResponseEntity<ClientDto> save(@RequestBody ClientDto clientDto) {
        ClientDto client = clientService.save(clientDto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(client.getId()).toUri();
        return ResponseEntity.created(uri).body(client);
    }


    //================== GET ====================

    //BUSCA POR ID
    @GetMapping("/{id}")
    public ResponseEntity<ClientDto> findById(@PathVariable Long id) {
        ClientDto client = clientService.findById(id);
        return ResponseEntity.ok().body(client);
    }

    //BUSCA POR DOCUMENTO
    @GetMapping(value = "/document")
    public ResponseEntity<ClientDto> findByPersonDocument(@RequestParam String document) {
        ClientDto client = clientService.findByPersonDocument(document);
        return ResponseEntity.ok().body(client);
    }

    // BUSCAR TODOS CLIENTES
    @GetMapping
    public ResponseEntity<List<ClientDto>> findAll(){
        List<ClientDto> clients = clientService.findAll();
        return ResponseEntity.ok().body(clients);
    }


    //================== DELETE ====================

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        clientService.delete(id);
    }
}
