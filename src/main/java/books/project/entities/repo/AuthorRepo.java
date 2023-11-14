package books.project.entities.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import books.project.entities.Author;

public interface AuthorRepo extends  JpaRepository<Author, String> {

}