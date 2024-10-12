package guru.springfamework.api.v1.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VendorDTO {
    @ApiModelProperty(value = "Name of the Vendor")
    private String name;
    @JsonProperty("vendor_url")
    private String vendorUrl;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VendorDTO vendorDTO = (VendorDTO) o;
        return Objects.equals(name, vendorDTO.name) && Objects.equals(vendorUrl, vendorDTO.vendorUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, vendorUrl);
    }
}
