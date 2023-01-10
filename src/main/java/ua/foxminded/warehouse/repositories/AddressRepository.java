package ua.foxminded.warehouse.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.foxminded.warehouse.models.Address;

@Repository
public interface AddressRepository extends JpaRepository<Address, Integer> {
    Address findByCountry(String country);
}
