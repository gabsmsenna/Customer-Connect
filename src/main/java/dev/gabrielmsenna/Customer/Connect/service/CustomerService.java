package dev.gabrielmsenna.Customer.Connect.service;

import dev.gabrielmsenna.Customer.Connect.controller.dto.CreateCustomerDTO;
import dev.gabrielmsenna.Customer.Connect.controller.dto.UpdateCustomerDto;
import dev.gabrielmsenna.Customer.Connect.entity.Customer;
import dev.gabrielmsenna.Customer.Connect.repository.CustomerRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Optional;

import static org.springframework.util.StringUtils.hasText;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public Customer createCustomer(CreateCustomerDTO createCustomerDTO) {

        var entity = new Customer();
        entity.setFullName(createCustomerDTO.fullName());
        entity.setEmail(createCustomerDTO.email());
        entity.setCpf(createCustomerDTO.cpf());
        entity.setPhoneNumber(createCustomerDTO.phoneNumber());

        return customerRepository.save(entity);

    }

    public Page<Customer> findAll(Integer page,
                                  Integer pageSize,
                                  String orderBy,
                                  String cpf,
                                  String email) {

        var pageRequest = getPageRequest(page, pageSize, orderBy);

        return findWithFilter(cpf, email, pageRequest);
    }

    private Page<Customer> findWithFilter(String cpf, String email, PageRequest pageRequest) {
        if (hasText(cpf) && hasText(email)) {
            return customerRepository.findByCpfAndEmail(cpf, email, pageRequest);
        }

        if (hasText(cpf)) return customerRepository.findByCpf(cpf, pageRequest);

        if (hasText(email)) return customerRepository.findByEmail(email, pageRequest);

        return customerRepository.findAll(pageRequest);
    }

    private PageRequest getPageRequest(Integer page, Integer pageSize, String orderBy) {
        var direction = Sort.Direction.DESC;
        if (orderBy.equalsIgnoreCase("asc")) {
            direction = Sort.Direction.ASC;
        }

       return PageRequest.of(page, pageSize, direction, "createdAt");
    }

    public Optional<Customer> findById(Long customerId) {
        return customerRepository.findById(customerId);
    }

    public Optional<Customer> updateById(Long customerId, UpdateCustomerDto dto) {

       var customer =  customerRepository.findById(customerId);

        if (customer.isPresent()) {

            updateFields(dto, customer);

            customerRepository.save(customer.get());
        }
        return customer;
    }

    private static void updateFields(UpdateCustomerDto dto, Optional<Customer> customer) {
        if (hasText(dto.fullName())) {
            customer.get().setFullName(dto.fullName());
        }

        if (hasText(dto.phoneNumber())) {
            customer.get().setPhoneNumber(dto.phoneNumber());
        }

        if (hasText(dto.email())) {
            customer.get().setEmail(dto.email());
        }
    }

    public boolean deleteById(Long customerId) {

        var customer = customerRepository.existsById(customerId);

        if (customer) {
            customerRepository.deleteById(customerId);
        }

        return customer;
    }
}
