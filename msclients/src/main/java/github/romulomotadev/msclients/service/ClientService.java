package github.romulomotadev.msclients.service;

import github.romulomotadev.msclients.dto.AddressDto;
import github.romulomotadev.msclients.dto.ClientDto;
import github.romulomotadev.msclients.entities.Address;
import github.romulomotadev.msclients.entities.Client;
import github.romulomotadev.msclients.entities.Person;
import github.romulomotadev.msclients.exception.exceptions.ResourceNotFoundException;
import github.romulomotadev.msclients.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository clientRepository;


    //================== POST ====================

    @Transactional
    public ClientDto save(ClientDto clientDto) {
        Client client = new Client();
        client.setName(clientDto.getName());
        client.setEmail(clientDto.getEmail());

        Person person = new Person();
        person.setType(clientDto.getPerson().getType());
        person.setDocument(clientDto.getPerson().getDocument());
        person.setClient(client);

        List<Address> addresses = new ArrayList<>();
        for(AddressDto addressDto : clientDto.getAddresses()){
            Address address = new Address();
            address.setAddress(addressDto.getAddress());
            address.setZipCode(addressDto.getZipCode());
            address.setComplement(addressDto.getComplement());
            address.setClient(client);
            addresses.add(address);
        }

        client.setPerson(person);
        client.setAddresses(addresses);
        client = clientRepository.save(client);
        return new ClientDto(client);
    }


    //================== GET ====================

    // BUSCA POR ID
    @Transactional(readOnly = true)
    public ClientDto findById(Long id) {
        Client client = clientRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Id not found"));
        return new ClientDto(client);
    }

    //BUSCA POR DOCUMENTO
    @Transactional(readOnly = true)
    public ClientDto findByPersonDocument(String document) {
        Client client = clientRepository.findByPersonDocument(document);
        return new ClientDto(client);
    }

    // BUSCAR TODOS CLIENTES
    @Transactional(readOnly = true)
    public List<ClientDto> findAll(){
        List<Client> clients = clientRepository.findAll();
        return clients.stream().map(ClientDto::new).toList();
    }


    //================== DELETE ====================

    @Transactional
    public void delete(Long id) {
        if(!clientRepository.existsById(id)){
            throw new ResourceNotFoundException("Id not found");
        } else {
            clientRepository.deleteById(id);
        }
    }



}
