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


import java.util.List;

@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository clientRepository;


    //================== POST ====================

    @Transactional
    public ClientDto save(ClientDto clientDto) {
        Client client = new Client();
        copyClientDtoToClient(clientDto, client);

        Person person = new Person();
        copyPersonDtoPerson(person, clientDto);
        person.setClient(client);

        List<Address> addresses = client.getAddresses();
        addresses.clear();
        copyAddressDtoAddress(clientDto, client, addresses);

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

    //================== PUT ====================

    @Transactional
    public ClientDto update(ClientDto clientDto, Long id) {
        Client client = clientRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Id not found"));
        copyClientDtoToClient(clientDto, client);

        Person person = client.getPerson();
        copyPersonDtoPerson(person, clientDto);

        List<Address> addresses = client.getAddresses();
        addresses.clear();
        copyAddressDtoAddress(clientDto, client, addresses);

        client.setPerson(person);
        client = clientRepository.save(client);
        return new ClientDto(client);
    }


    //================== AUX ====================

    // COPIA DADOS DO CLIENT DTO PARA ENTIDADE CLIENT
    public void copyClientDtoToClient(ClientDto clientDto, Client client){
        client.setName(clientDto.getName());
        client.setEmail(clientDto.getEmail());
    }

    // COPIA DADOS DO PERSON DTO PARA ENTIDADE PERSON
    private void copyPersonDtoPerson(Person person, ClientDto clientDto){
        person.setType(clientDto.getPerson().getType());
        person.setDocument(clientDto.getPerson().getDocument());
    }

    // COPIA DADOS DE ADDRESS DTO PARA ENTIDADE ADDRESS
    private void copyAddressDtoAddress(ClientDto clientDto, Client client, List<Address> addresses){

        for(AddressDto addressDto : clientDto.getAddresses()){
            Address address = new Address();
            address.setAddress(addressDto.getAddress());
            address.setZipCode(addressDto.getZipCode());
            address.setComplement(addressDto.getComplement());
            address.setClient(client);
            addresses.add(address);
        }
    }
}
