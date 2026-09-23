package github.romulomotadev.msorderservice.factory;

import github.romulomotadev.msorderservice.dto.AddressResponseDto;
import github.romulomotadev.msorderservice.dto.ClientResponseDto;
import github.romulomotadev.msorderservice.dto.PersonResponseDto;

import java.util.ArrayList;
import java.util.List;

public class ClientDataResponseDtoFactory {

    public static ClientResponseDto createClientResponseDto() {

        ClientResponseDto clientResponseDto = new ClientResponseDto();
        clientResponseDto.setId(1L);
        clientResponseDto.setName("Ronaldo");
        clientResponseDto.setEmail("ronaldo@email.com");

        PersonResponseDto personResponseDto = new PersonResponseDto();
        personResponseDto.setId(1L);
        personResponseDto.setType("NATURAL_PERSON");
        personResponseDto.setDocument("111.111.111-11");
        clientResponseDto.setPerson(personResponseDto);

        List<AddressResponseDto> addresses = new ArrayList<>();
        AddressResponseDto addressResponseDto = new AddressResponseDto();
        addressResponseDto.setId(1L);
        addressResponseDto.setAddress("Rua dos bobos, 123");
        addressResponseDto.setZipCode("12345-678");
        addressResponseDto.setComplement("Apto 101");
        addresses.add(addressResponseDto);
        clientResponseDto.setAddresses(addresses);

        return clientResponseDto;
    }
}
