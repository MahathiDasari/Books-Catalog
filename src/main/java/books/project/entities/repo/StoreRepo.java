package books.project.entities.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import books.project.entities.Store;
import books.project.entities.repo.DTO.StoreAndTitlesSold;
import books.project.entities.repo.DTO.StoreIdAndTitleDTO;

public interface StoreRepo extends JpaRepository<Store, String> {

	// 12 List top 5 stores by total titles sold
	@Query("select s.key.storeId as storeId, sum(s.qtySold) as titlesSold from Sale s group by s.key.storeId order by titlesSold desc limit 5")
	List<StoreAndTitlesSold> getTop5StoresByTitlesSold();

	// 14 Take store id and list all titles sold there
	@Query("select s.storeId as storeId,t.title as title from Store s join s.titles t where s.storeId like %:storeId%  ")
	List<StoreIdAndTitleDTO> findByStoreIdAndTitle(@Param("storeId") String storeId);


}