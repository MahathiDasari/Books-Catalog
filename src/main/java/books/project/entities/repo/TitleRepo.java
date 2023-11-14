package books.project.entities.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import books.project.entities.Publisher;
import books.project.entities.Title;
import books.project.entities.repo.DTO.TitleAndAuthorDTO;
import books.project.entities.repo.DTO.TitleDetailsDTO;

public interface TitleRepo extends JpaRepository<Title, String> {
	
	// 7.List all titles by publisher
	List<Title> findByPublisher(Optional<Publisher> publisher);
	
	// 8.List all titles by author
	@Query("SELECT t.title, a.auName FROM Title t JOIN t.authors a")
	List<Object[]> findByTitleAndAuthor();

	// 9.List all titles that match the given title
	List<Title> findTitlesByTitleContaining(String title);

	// 10.List all titles by the range of price(min and max price)
	List<Title> findByPriceBetween(double minPrice, double maxPrice);

	// 11.List top 5 titles by ytd_sales
	@Query("SELECT t from Title t order by ytdSales Desc limit 5")
	List<Title> getTop5TitlesByYtdSales();

	// 13.list details of a title by its id.Details are:title,price,ytd_sales,auNames,pubNames
	@Query("SELECT t.title as title ,t.price as price ,t.ytdSales as ytdSales,t.publisher.pubName as pubName ,a.auName  as auName from Title t join t.authors a where titleId like %:titleId%  ")
	List<TitleDetailsDTO> findAllDetailsByTitle(@Param("titleId") String titleId);

//  7.get titles by publishers
//	@Query(value="select t.* from titles t join publishers p on t.pub_id=p.pub_id  where p.pub_name= :pubName",nativeQuery = true)
//	public List<Title> getTitlesByPubName(@Param("pubName") String pubName);

//	11.get top 5 titles by ytd_sales
//	@Query(value="select TOP 5 * from titles order by ytd_sales desc",nativeQuery=true)
//	public List<Title>getTop5TitlesByYtdSales();

//	11.get top 5 titles by ytd_sales	
//	List<Title> findFirst5ByOrderByYtdSalesDesc();

}
