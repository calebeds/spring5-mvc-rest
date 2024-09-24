package guru.springfamework.controllers.v1;

import guru.springfamework.api.v1.model.CustomerDTO;
import guru.springfamework.services.CustomerService;
import guru.springfamework.services.ResourceNotFoundException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.ResourceAccessException;

import java.util.Arrays;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
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

        mockMvc = MockMvcBuilders.standaloneSetup(controller).setControllerAdvice(new RestResponseEntityExceptionHandler()).build();
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

        mockMvc.perform(get(getCustomerUrl())
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

        mockMvc.perform(get(getCustomerUrl() + "1")
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
        returnedDto.setCustomerUrl(getCustomerUrl() + "1");

        when(customerService.createNewCustomer(customerDTO)).thenReturn(returnedDto);

        //when/then
        mockMvc.perform(post(getCustomerUrl())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(customerDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.first_name", equalTo("Fred")))
                .andExpect(jsonPath("$.last_name", equalTo("Flinstone")))
                .andExpect(jsonPath("$.customer_url", equalTo("/api/v1/customers/1")));

    }

    @Test
    public void testUpdateCustomer() throws Exception {
        //given
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setFirstName("Fred");
        customerDTO.setLastName("Flinstone");

        CustomerDTO returnedDto = new CustomerDTO();
        returnedDto.setFirstName(customerDTO.getFirstName());
        returnedDto.setLastName(customerDTO.getLastName());
        returnedDto.setCustomerUrl(getCustomerUrl() + "1");

        when(customerService.saveCustomerByDTO(anyLong(), eq(customerDTO))).thenReturn(returnedDto);

        //when/then
        mockMvc.perform(put(getCustomerUrl() + "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(customerDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.first_name", equalTo("Fred")))
                .andExpect(jsonPath("$.last_name", equalTo("Flinstone")))
                .andExpect(jsonPath("$.customer_url", equalTo("/api/v1/customers/1")));

    }

    @Test
    public void testPatchCustomer() throws Exception {
        //given
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setFirstName("Fred");

        CustomerDTO returnedDto = new CustomerDTO();
        returnedDto.setFirstName(customerDTO.getFirstName());
        returnedDto.setLastName("Flinstone");
        returnedDto.setCustomerUrl(getCustomerUrl() + "1");

        when(customerService.patchCustomer(anyLong(), eq(customerDTO))).thenReturn(returnedDto);

        //when/then
        mockMvc.perform(patch(getCustomerUrl() + "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(customerDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.first_name", equalTo("Fred")))
                .andExpect(jsonPath("$.last_name", equalTo("Flinstone")))
                .andExpect(jsonPath("$.customer_url", equalTo("/api/v1/customers/1")));

    }

    @Test
    public void testDeleteCustomer() throws Exception {
        //given

        //when
        doNothing().when(customerService).deleteCustomerById(anyLong());

        //then
        mockMvc.perform(delete(getCustomerUrl() + "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(customerService, times(1)).deleteCustomerById(anyLong());
    }

    @Test
    public void testNotFoundException() throws Exception {
        when(customerService.getCustomerById(anyLong())).thenThrow(ResourceNotFoundException.class);

        mockMvc.perform(get(getCustomerUrl() + "222")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    private String getCustomerUrl() {
        return CustomerController.API_V1_CUSTOMERS;
    }
}