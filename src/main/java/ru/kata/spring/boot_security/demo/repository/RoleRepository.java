package ru.kata.spring.boot_security.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.kata.spring.boot_security.demo.entity.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
    @Query("FROM Role WHERE roleName=:roleName")
    public Role getRole(@Param("roleName") String roleName);
}
