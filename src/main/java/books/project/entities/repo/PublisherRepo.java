package books.project.entities.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import books.project.entities.Publisher;

public interface PublisherRepo extends JpaRepository<Publisher, String> {
	

	Optional<Publisher> findByPubName(String pubName);

}