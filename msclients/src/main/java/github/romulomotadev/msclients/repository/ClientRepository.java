package github.romulomotadev.msclients.repository;

import github.romulomotadev.msclients.entities.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

    //BUSCA POR DOCUMENTO
    Client findByPersonDocument(String document);

}
