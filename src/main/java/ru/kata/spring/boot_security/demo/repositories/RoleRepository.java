package ru.kata.spring.boot_security.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.kata.spring.boot_security.demo.entities.Role;

import java.util.List;
import java.util.NoSuchElementException;

public interface RoleRepository extends JpaRepository<Role, Long> {


}
