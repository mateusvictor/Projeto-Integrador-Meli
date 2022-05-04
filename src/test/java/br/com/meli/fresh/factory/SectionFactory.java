package br.com.meli.fresh.factory;

import br.com.meli.fresh.dto.request.SectionRequestDTO;
import br.com.meli.fresh.model.Section;

public class SectionFactory {


    public static Section createSection() {
        Section section = new Section();
        section.setProductType("type");
        section.setActualVolume(10f);
        section.setMaxVolume(10f);

        return section;
    }

    public static Section createSectionDefault(){
        return Section.builder().productType("fs").actualVolume(0f).maxVolume(30f).build();
    }

    public static SectionRequestDTO createSectionDefaultDTO(){
        return SectionRequestDTO.builder().productType("fs").actualVolume(0f).maxVolume(30f).build();
    }

    public static Section getFreshSection(){
        return new Section(null, "fresco", 0F, 30F, null, null);
    }

    public static Section getFrozenSection(){
        return new Section(null, "congelado", 10F, 30F, null, null);
    }
}
