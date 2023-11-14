package books.project.entities.repo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import books.project.entities.Author;
import books.project.entities.repo.AuthorRepo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;


@RestController

public class AuthorController {

	@Autowired
	AuthorRepo authorRepo;

	// GET ALL AUTHORS
	@GetMapping("/authors")
	@Operation(summary = "Retrieves a list of all authors", description = "returns a collection of all available authors")
	public List<Author> getAllAuthors() {
		return authorRepo.findAll();
	}

	// ADD AUTHOR
	@PostMapping("/addauthor")
	@Operation(summary = "Adds a new author", description = "Allows the addition of a new author to the database. If the author ID doesn't exist, it saves the new author. If the author ID already exists, it throws a bad request error")
	@ApiResponses(value =
    {     
			@ApiResponse(responseCode = "200", description = "inserted successfully"),
			@ApiResponse(responseCode = "400", description = "BAD REQUEST,invalid data passed"),
    		@ApiResponse(responseCode = "405", description = "Method Not Allowed"),
    		@ApiResponse(responseCode = "500", description = "Internal server error")
    })
	public void addTitle(@Valid @RequestBody Author author) {
		var optAuthor = authorRepo.findById(author.getAuId());
		try {
			if (optAuthor.isEmpty()) {
				authorRepo.save(author);
			} else
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Already Existed");
		} catch (DataAccessException ex) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
		}
	}

	// UPDATE AUTHOR
	@PutMapping("/authors/{auId}")
	@Operation(summary = "Update an existing author", description = "Updates the author specified by its ID with a new author name. If the provided ID exists in the database, it updates the author's name. If the ID does not exist, it throws a not found error")
	@ApiResponses(value = 
    { 
    	@ApiResponse(responseCode = "200", description = "Updated successfully"),
		@ApiResponse(responseCode = "400", description = "BAD REQUEST,already existed or invalid data passed"),
		@ApiResponse(responseCode = "404", description = "Name Not Found"),
		@ApiResponse(responseCode = "405", description = "Method Not Allowed"),
		@ApiResponse(responseCode = "500", description = "Internal server error")
    })
	public void updateTitle(@PathVariable("auId") String id,@RequestParam("auName") String newauName) {
		var optAuthor = authorRepo.findById(id);
		if (optAuthor.isPresent()) {
			var author = optAuthor.get();
			author.setAuName(newauName);
			authorRepo.save(author);
		} else
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Author Name Not Found!");
	}

	// DELETE AUTHOR
	@DeleteMapping("/authors/{auId}")
	@Operation(summary = " Delete an author by ID", description = "Removes an author specified by its ID. If the ID exists, it deletes the author. If the ID does not exist, it throws a not found error.")
	@ApiResponses(value = 
    { 
    		@ApiResponse(responseCode = "400", description = "invalid data passed"),
			@ApiResponse(responseCode = "200", description = "okay id found deleted the row in database"),
			@ApiResponse(responseCode = "404", description = "Id Not Found"),
			@ApiResponse(responseCode = "405", description = "Method Not Allowed"),
			@ApiResponse(responseCode = "500", description = "Internal server error")
    })
	public void deleteOneAuthor(@PathVariable("auId") String id) {
		var optAuthor = authorRepo.findById(id);
		if (optAuthor.isPresent())
			authorRepo.deleteById(id);
		else
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Author Id Not Found!");
	}
}
