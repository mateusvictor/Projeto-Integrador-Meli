package br.com.meli.fresh.dto.response.productResponse;

import br.com.meli.fresh.model.Section;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.List;

public class ProductWarehouseResponse {

    private String id;
    private String name;
}
