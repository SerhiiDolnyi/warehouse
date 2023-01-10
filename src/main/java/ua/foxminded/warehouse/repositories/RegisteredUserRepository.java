package ua.foxminded.warehouse.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.foxminded.warehouse.models.auth.RegisteredUser;

import java.util.Optional;

@Repository
public interface RegisteredUserRepository extends JpaRepository<RegisteredUser, Integer> {
    Optional<RegisteredUser> findByName(String name);
}
