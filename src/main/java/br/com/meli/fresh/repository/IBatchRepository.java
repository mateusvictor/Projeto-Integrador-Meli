package br.com.meli.fresh.repository;

import br.com.meli.fresh.model.Batch;
import br.com.meli.fresh.model.Section;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface IBatchRepository extends JpaRepository<Batch, String> {
    List<Batch> findAllByProduct_Id(String id);
    Batch findByProduct_Id(String id);

    List<Batch> findAllByInboundOrder_SectionOrderByDueDateAsc(Section section);
    List<Batch> findAllByDueDateBetweenOrderByDueDateAsc(LocalDate startDate, LocalDate endDate);
    List<Batch> findAllByInboundOrder_SectionAndDueDateBetweenOrderByDueDateAsc(Section section, LocalDate startDate, LocalDate endDate);

    List<Batch> findAllByProduct_CategoryOrderByDueDateAsc(String category);
    List<Batch> findAllByProduct_CategoryAndDueDateBetweenOrderByDueDateAsc(String category, LocalDate startDate, LocalDate endDate);

    // Find bacthes from one warehouse
    List<Batch> findAllByInboundOrder_Section_Warehouse_Id(String id);
}
