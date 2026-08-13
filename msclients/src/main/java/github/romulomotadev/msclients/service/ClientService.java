package github.romulomotadev.msclients.service;

import github.romulomotadev.msclients.dto.AddressDto;
import github.romulomotadev.msclients.dto.ClientDto;
import github.romulomotadev.msclients.entities.Address;
import github.romulomotadev.msclients.entities.Client;
import github.romulomotadev.msclients.entities.Person;
import github.romulomotadev.msclients.exception.exceptions.ResourceNotFoundException;
import github.romulomotadev.msclients.repository.ClientRepository;
import github.romulomotadev.msclients.repository.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository clientRepository;
    private final PersonRepository personRepository;


    //================== POST ====================

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
    public ClientDto findById(Long id) {
        Client client = clientRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Id not found"));
        return new ClientDto(client);
    }

    //BUSCA POR DOCUMENTO
    public ClientDto findByPersonDocument(String document) {
        Client client = clientRepository.findByPersonDocument(document);
        return new ClientDto(client);
    }


}
