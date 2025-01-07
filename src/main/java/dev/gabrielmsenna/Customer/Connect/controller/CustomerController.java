package dev.gabrielmsenna.Customer.Connect.controller;

import dev.gabrielmsenna.Customer.Connect.controller.dto.ApiResponse;
import dev.gabrielmsenna.Customer.Connect.controller.dto.CreateCustomerDTO;
import dev.gabrielmsenna.Customer.Connect.controller.dto.PaginationResponse;
import dev.gabrielmsenna.Customer.Connect.controller.dto.UpdateCustomerDto;
import dev.gabrielmsenna.Customer.Connect.entity.Customer;
import dev.gabrielmsenna.Customer.Connect.service.CustomerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping(path = "/customers")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping
    public ResponseEntity<Void> createCustomer(@RequestBody CreateCustomerDTO dto) {
        var customer = customerService.createCustomer(dto);

        return ResponseEntity.created(URI.create("/customers/" + customer.getCustomerId()))
                .build();
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Customer>> findAll(
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
            @RequestParam(name = "orderBy", defaultValue = "desc") String orderBy,
            @RequestParam(name = "cpf", required = false) String cpf,
            @RequestParam(name = "email", required = false) String email) {

        var pageResponse = customerService.findAll(page, pageSize, orderBy, cpf, email);

        return ResponseEntity.ok(new ApiResponse<>(
                pageResponse.getContent(),
                new PaginationResponse(pageResponse.getNumber(),
                        pageResponse.getSize(),
                        pageResponse.getTotalElements(),
                        pageResponse.getTotalPages())
        ));
    }

    @GetMapping(path = "/{customerId}")
    public ResponseEntity<Customer> findById(@PathVariable("customerId") Long customerId) {

        var user = customerService.findById(customerId);

        return user.isPresent() ?
                ResponseEntity.ok(user.get()) :
                ResponseEntity.notFound().build();
    }

    @PutMapping(path = "/{customerId}")
    public ResponseEntity<Customer> updateById(@PathVariable("customerId") Long customerId,
                                               @RequestBody UpdateCustomerDto dto) {
        var customer = customerService.updateById(customerId, dto);

        return customer.isPresent() ?
                ResponseEntity.noContent().build() :
                ResponseEntity.notFound().build();
    }

    @DeleteMapping(path = "/{customerId}")
    public ResponseEntity<Void> deleteById(@PathVariable("customerId") Long customerId) {

        var customerDeleted = customerService.deleteById(customerId);

        return customerDeleted ?
                ResponseEntity.noContent().build() :
                ResponseEntity.notFound().build();
    }
}
