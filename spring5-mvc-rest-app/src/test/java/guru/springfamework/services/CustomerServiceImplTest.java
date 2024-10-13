package guru.springfamework.services;

import guru.springfamework.api.v1.mapper.CustomerMapper;
import guru.springfamework.domain.Customer;
import guru.springfamework.repositories.CustomerRepository;
import guru.springframework.model.CustomerDTO;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CustomerServiceImplTest {
    @Mock
    CustomerRepository customerRepository;

    CustomerMapper customerMapper = CustomerMapper.INSTANCE;

    CustomerService customerService;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        customerService = new CustomerServiceImpl(customerMapper, customerRepository);
    }

    @Test
    public void testGetAllCustomers() {
        //given
        Customer customer = new Customer();
        customer.setFirstName("Calebe");
        customer.setLastName("Oliveira");

        Customer customer2 = new Customer();
        customer2.setFirstName("Joe");
        customer2.setLastName("Doe");

        when(customerRepository.findAll()).thenReturn(Arrays.asList(customer, customer2));

        //when
        List<CustomerDTO> customerDTOS = customerService.getAllCustomers();

        //then
        assertEquals(2, customerDTOS.size());
    }

    @Test
    public void testGetCustomerById() {
        //given
        Customer customer = new Customer();
        customer.setId(1L);
        customer.setFirstName("Michele");
        customer.setLastName("Weston");

        when(customerRepository.findById(anyLong())).thenReturn(Optional.ofNullable(customer));

        //when
        CustomerDTO customerDTO = customerService.getCustomerById(1L);

        assertEquals("Michele", customerDTO.getFirstname());
    }

    @Test
    public void testCreateNewCustomer() throws Exception {
        //given
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setFirstname("Fred");
        customerDTO.setLastname("Flinstone");

        Customer returnedCustomer = new Customer();
        returnedCustomer.setFirstName(customerDTO.getFirstname());
        returnedCustomer.setLastName(customerDTO.getLastname());

        when(customerRepository.save(any())).thenReturn(returnedCustomer);

        //when
        CustomerDTO savedDto = customerService.createNewCustomer(customerDTO);

        //then
        assertEquals(savedDto.getFirstname(), "Fred");
    }

    @Test
    public void testSaveCustomerByDTO() {
        //given
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setFirstname("Fred");
        customerDTO.setLastname("Flinstone");

        Customer returnedCustomer = new Customer();
        returnedCustomer.setFirstName(customerDTO.getFirstname());
        returnedCustomer.setLastName(customerDTO.getLastname());

        when(customerRepository.save(any())).thenReturn(returnedCustomer);

        //when
        CustomerDTO savedDto = customerService.saveCustomerByDTO(2L, customerDTO);

        //then
        assertEquals(savedDto.getFirstname(), "Fred");
    }

    @Test
    public void deleteCustomerById() throws Exception {
        Long id = 1L;

        customerRepository.deleteById(id);

        verify(customerRepository, times(1)).deleteById(anyLong());
    }
}