package guru.springfamework.controllers.v1;

import guru.springfamework.api.v1.model.CustomerDTO;
import guru.springfamework.services.CustomerService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class CustomerControllerTest extends AbstractRestControllerTest {
    @Mock
    CustomerService customerService;

    @InjectMocks
    CustomerController controller;

    MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    public void testListCustomers() throws Exception {
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setFirstName("Calebe");
        customerDTO.setLastName("Oliveira");
        customerDTO.setCustomerUrl("smt");

        CustomerDTO customerDTO2 = new CustomerDTO();
        customerDTO2.setFirstName("Joe");
        customerDTO2.setLastName("Doe");
        customerDTO2.setCustomerUrl("url");

        when(customerService.getAllCustomers()).thenReturn(Arrays.asList(customerDTO, customerDTO2));

        mockMvc.perform(get("/api/v1/customers")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customers",  hasSize(2)));
    }

    @Test
    public void testGetCustomerById() throws Exception {
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setFirstName("Calebe");
        customerDTO.setLastName("Oliveira");
        customerDTO.setCustomerUrl("smt");

        when(customerService.getCustomerById(anyLong())).thenReturn(customerDTO);

        mockMvc.perform(get("/api/v1/customers/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.first_name", equalTo("Calebe")));
    }

    @Test
    public void testCreateNewCustomer() throws Exception {
        //given
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setFirstName("Fred");
        customerDTO.setLastName("Flinstone");

        CustomerDTO returnedDto = new CustomerDTO();
        returnedDto.setFirstName(customerDTO.getFirstName());
        returnedDto.setLastName(customerDTO.getLastName());
        returnedDto.setCustomerUrl("/api/v1/customers/1");

        when(customerService.createNewCustomer(customerDTO)).thenReturn(returnedDto);

        //when/then
        mockMvc.perform(post("/api/v1/customers/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(customerDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.first_name", equalTo("Fred")))
                .andExpect(jsonPath("$.last_name", equalTo("Flinstone")))
                .andExpect(jsonPath("$.customer_url", equalTo("/api/v1/customers/1")));

    }

    @Test
    public void testUpdateNewCustomer() throws Exception {
        //given
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setFirstName("Fred");
        customerDTO.setLastName("Flinstone");

        CustomerDTO returnedDto = new CustomerDTO();
        returnedDto.setFirstName(customerDTO.getFirstName());
        returnedDto.setLastName(customerDTO.getLastName());
        returnedDto.setCustomerUrl("/api/v1/customers/1");

        when(customerService.saveCustomerByDTO(anyLong(), eq(customerDTO))).thenReturn(returnedDto);

        //when/then
        mockMvc.perform(put("/api/v1/customers/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(customerDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.first_name", equalTo("Fred")))
                .andExpect(jsonPath("$.last_name", equalTo("Flinstone")))
                .andExpect(jsonPath("$.customer_url", equalTo("/api/v1/customers/1")));

    }

    @Test
    public void testPatchNewCustomer() throws Exception {
        //given
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setFirstName("Fred");

        CustomerDTO returnedDto = new CustomerDTO();
        returnedDto.setFirstName(customerDTO.getFirstName());
        returnedDto.setLastName("Flinstone");
        returnedDto.setCustomerUrl("/api/v1/customers/1");

        when(customerService.patchCustomer(anyLong(), eq(customerDTO))).thenReturn(returnedDto);

        //when/then
        mockMvc.perform(patch("/api/v1/customers/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(customerDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.first_name", equalTo("Fred")))
                .andExpect(jsonPath("$.last_name", equalTo("Flinstone")))
                .andExpect(jsonPath("$.customer_url", equalTo("/api/v1/customers/1")));

    }
}