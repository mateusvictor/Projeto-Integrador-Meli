package br.com.meli.fresh.factory;

import br.com.meli.fresh.model.Section;

import java.util.UUID;

public class SectionFactory {


    public static Section createSection() {
        Section section = new Section();
        section.setId(UUID.randomUUID().toString());
        section.setProductType("type");
        section.setActualVolume(10f);
        section.setMaxVolume(10f);

        return section;
    }
}
