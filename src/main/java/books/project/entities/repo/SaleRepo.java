
package books.project.entities.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import books.project.entities.Sale;
import books.project.entities.Salecpk;
import books.project.entities.repo.DTO.SaleByTitleDTO;

public interface SaleRepo extends JpaRepository<Sale, Salecpk> {

	// 15 List sales across the stores for a given title
	@Query(" select s.store.storeId as storeId, s.qtySold as qtySold from Sale s where s.title.titleId like  %:titleId% ")
	List<SaleByTitleDTO> findSalesByTitle(@Param("titleId") String titleId);

}