package guru.springfamework.api.v1.mapper;

import guru.springfamework.domain.Customer;
import guru.springframework.model.CustomerDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CustomerMapper {
    CustomerMapper INSTANCE = Mappers.getMapper(CustomerMapper.class);

    @Mappings({
            @Mapping(source = "firstName", target = "firstname"),
            @Mapping(source = "lastName", target = "lastname"),
    })
    CustomerDTO customerToCustomerDTO(Customer customer);
    @Mappings({
            @Mapping(source = "firstname", target = "firstName"),
            @Mapping(source = "lastname", target = "lastName"),
    })
    Customer customerDtoToCustomer(CustomerDTO customerDTO);
}
