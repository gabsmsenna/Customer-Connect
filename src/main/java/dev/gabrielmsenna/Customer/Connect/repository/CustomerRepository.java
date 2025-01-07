package dev.gabrielmsenna.Customer.Connect.repository;

import dev.gabrielmsenna.Customer.Connect.entity.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Page<Customer> findByCpf(String cpf, PageRequest pageRequest);

    Page<Customer> findByEmail(String email, PageRequest pageRequest);

    Page<Customer> findByCpfAndEmail(String cpf, String email, PageRequest pageRequest);
}
