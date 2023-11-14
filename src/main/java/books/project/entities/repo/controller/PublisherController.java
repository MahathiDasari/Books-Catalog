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

import books.project.entities.Publisher;
import books.project.entities.repo.PublisherRepo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;

@RestController

public class PublisherController {

	@Autowired
	PublisherRepo publisherRepo;

	// GET ALL PUBLISHERS
	@GetMapping("/publishers")
	@Operation(summary = "Retrieves a list of all publishers", description = "returns a collection of all available publishers")
	public List<Publisher> getAllPublishers() {
		return publisherRepo.findAll();
	}

	// ADD PUBLISHER
	@PostMapping("/addpublisher")
	@Operation(summary = "Adds a new publisher", description = "Allows the addition of a new publisher to the database. If the publisher ID doesn't exist, it saves the new publisher. If the publisher ID already exists, it throws a bad request error")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "inserted successfully"),
			@ApiResponse(responseCode = "400", description = "BAD REQUEST,invalid data passed"),
			@ApiResponse(responseCode = "405", description = "Method Not Allowed"),
			@ApiResponse(responseCode = "500", description = "Internal server error") })
	public void addPublisher(@Valid @RequestBody Publisher publisher) {
		var optPublisher = publisherRepo.findById(publisher.getPubId());
		try {
			if (optPublisher.isEmpty()) {
				publisherRepo.save(publisher);
			} else
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Already Existed");
		} catch (DataAccessException ex) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
		}
	}

	// UPDATE PUBLISHER
	@PutMapping("/publishers/{pubId}")
	@Operation(summary = "Update an existing publisher", description = "Updates the publisher specified by its ID with a new publisher name. If the provided ID exists in the database, it updates the publisher's name. If the ID does not exist, it throws a not found error")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Updated successfully"),
			@ApiResponse(responseCode = "400", description = "BAD REQUEST,already existed or invalid data passed"),
			@ApiResponse(responseCode = "404", description = "Name Not Found"),
			@ApiResponse(responseCode = "405", description = "Method Not Allowed"),
			@ApiResponse(responseCode = "500", description = "Internal server error") })
	public void updatePublisher(@PathVariable("pubId") String id, @RequestParam("pubName") String newpubName) {
		var optPublisher = publisherRepo.findById(id);
		if (optPublisher.isPresent()) {
			var publisher = optPublisher.get();
			publisher.setPubName(newpubName);
			publisherRepo.save(publisher);
		} else
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Publisher Name Not Found!");
	}

	// DELETE PUBLISHER
	@DeleteMapping("/publishers/{pubId}")
	@Operation(summary = "Delete a publisher by ID", description = "Removes a publisher specified by its ID. If the ID exists, it deletes the publisher. If the ID does not exist, it throws a not found error")
	@ApiResponses(value = { @ApiResponse(responseCode = "400", description = "invalid data passed"),
			@ApiResponse(responseCode = "200", description = "okay id found deleted the row in database"),
			@ApiResponse(responseCode = "404", description = "Id Not Found"),
			@ApiResponse(responseCode = "405", description = "Method NOt Allowed"),
			@ApiResponse(responseCode = "500", description = "Internal server error") })
	public void deleteOnePublisher(@PathVariable("pubId") String id) {
		var optPublisher = publisherRepo.findById(id);
		if (optPublisher.isPresent())
			publisherRepo.deleteById(id);
		else
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Publisher Id Not Found!");
	}
}