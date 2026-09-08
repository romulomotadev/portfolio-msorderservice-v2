package github.romulomotadev.msclients.factory;

import github.romulomotadev.msclients.entities.Address;
import github.romulomotadev.msclients.entities.Client;
import github.romulomotadev.msclients.entities.Person;

import java.util.ArrayList;
import java.util.List;

import static github.romulomotadev.msclients.entities.Type.NATURAL_PERSON;

public class ClientFactory {

    public static Client createClient() {

        Client client = new Client();
        client.setId(1L);
        client.setName("Bial Constantine");
        client.setEmail("bial@gmail.com");

        Person person = new Person();
        person.setId(1L);
        person.setType(NATURAL_PERSON);
        person.setDocument("123.456.789-01");
        client.setPerson(person);

        List<Address> addresses = new ArrayList<>();
        Address address = new Address();
        address.setAddress("rua A, 23, Mangabeiras");
        address.setZipCode("home");
        address.setComplement("home");
        addresses.add(address);
        client.setAddresses(addresses);

        return client;
    }
}
