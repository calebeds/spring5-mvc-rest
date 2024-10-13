package guru.springfamework.controllers.v1;

import guru.springfamework.services.CustomerService;
import guru.springfamework.services.ResourceNotFoundException;
import guru.springframework.model.CustomerDTO;
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
        customerDTO.setFirstname("Calebe");
        customerDTO.setLastname("Oliveira");
        customerDTO.setCustomerUrl("smt");

        CustomerDTO customerDTO2 = new CustomerDTO();
        customerDTO2.setFirstname("Joe");
        customerDTO2.setLastname("Doe");
        customerDTO2.setCustomerUrl("url");

        when(customerService.getAllCustomers()).thenReturn(Arrays.asList(customerDTO, customerDTO2));

        mockMvc.perform(get(getCustomerUrl())
                .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customers",  hasSize(2)));
    }

    @Test
    public void testGetCustomerById() throws Exception {
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setFirstname("Calebe");
        customerDTO.setLastname("Oliveira");
        customerDTO.setCustomerUrl("smt");

        when(customerService.getCustomerById(anyLong())).thenReturn(customerDTO);

        mockMvc.perform(get(getCustomerUrl() + "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstname", equalTo("Calebe")));
    }

    @Test
    public void testCreateNewCustomer() throws Exception {
        //given
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setFirstname("Fred");
        customerDTO.setLastname("Flinstone");

        CustomerDTO returnedDto = new CustomerDTO();
        returnedDto.setFirstname(customerDTO.getFirstname());
        returnedDto.setLastname(customerDTO.getLastname());
        returnedDto.setCustomerUrl(getCustomerUrl() + "1");

        when(customerService.createNewCustomer(any())).thenReturn(returnedDto);

        //when/then
        mockMvc.perform(post(getCustomerUrl())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(customerDTO))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstname", equalTo("Fred")))
                .andExpect(jsonPath("$.lastname", equalTo("Flinstone")))
                .andExpect(jsonPath("$.customerUrl", equalTo("/api/v1/customers/1")));

    }

    @Test
    public void testUpdateCustomer() throws Exception {
        //given
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setFirstname("Fred");
        customerDTO.setLastname("Flinstone");

        CustomerDTO returnedDto = new CustomerDTO();
        returnedDto.setFirstname(customerDTO.getFirstname());
        returnedDto.setLastname(customerDTO.getLastname());
        returnedDto.setCustomerUrl(getCustomerUrl() + "1");

        when(customerService.saveCustomerByDTO(anyLong(), any())).thenReturn(returnedDto);

        //when/then
        mockMvc.perform(put(getCustomerUrl() + "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(customerDTO))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstname", equalTo("Fred")))
                .andExpect(jsonPath("$.lastname", equalTo("Flinstone")))
                .andExpect(jsonPath("$.customerUrl", equalTo("/api/v1/customers/1")));

    }

    @Test
    public void testPatchCustomer() throws Exception {
        //given
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setFirstname("Fred");

        CustomerDTO returnedDto = new CustomerDTO();
        returnedDto.setFirstname(customerDTO.getFirstname());
        returnedDto.setLastname("Flinstone");
        returnedDto.setCustomerUrl(getCustomerUrl() + "1");

        when(customerService.patchCustomer(anyLong(), any())).thenReturn(returnedDto);

        //when/then
        mockMvc.perform(patch(getCustomerUrl() + "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(customerDTO))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstname", equalTo("Fred")))
                .andExpect(jsonPath("$.lastname", equalTo("Flinstone")))
                .andExpect(jsonPath("$.customerUrl", equalTo("/api/v1/customers/1")));

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