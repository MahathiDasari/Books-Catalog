package books.project.entities.repo.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
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
import books.project.entities.Title;
import books.project.entities.repo.PublisherRepo;
import books.project.entities.repo.TitleRepo;
import books.project.entities.repo.DTO.TitleDetailsDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;

@RestController

public class TitleController {

	@Autowired
	TitleRepo titleRepo;

	@Autowired
	PublisherRepo publisherRepo;

	// GET ALL TITLES
	// @PreAuthorize("hasRole('ADMIN')")
	@CrossOrigin
	@GetMapping("/titles")
	@Operation(summary = "Retrieves a list of all titles", description = "returns a collection of all titles available")
	public List<Title> getAllTitles() {
		return titleRepo.findAll();
	}

	// ADD TITLE
	@PostMapping("/addtitle")
	@Operation(summary = "Adds a new title", description = "Allows the addition of a new title to the database. If the title ID doesn't exist, it saves the new title. If the title ID already exists, it throws a bad request error")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "inserted Title successfully"),
			@ApiResponse(responseCode = "400", description = "BAD REQUEST,invalid data passed"),
			@ApiResponse(responseCode = "405", description = "Method Not Allowed"),
			@ApiResponse(responseCode = "500", description = "Internal server error") })
	public void addTitle(@Valid @RequestBody Title title) {
		var optTitle = titleRepo.findById(title.getTitleId());
		try {
			if (optTitle.isEmpty())
				titleRepo.save(title);
			else
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Already Existed");
		} catch (DataAccessException ex) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
		}
	}

	// UPDATE TITLE
	// @PreAuthorize("hasRole('ADMIN')")
	@PutMapping("/titles/{titleId}")
	@Operation(summary = "Update an existing title", description = "Updates the title specified by its ID with a new title name. If the provided ID exists in the database, it updates the title name. If the ID does not exist, it throws a not found error")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Updated Title successfully"),
			@ApiResponse(responseCode = "400", description = "BAD REQUEST,invalid data passed"),
			@ApiResponse(responseCode = "404", description = "Title Name Not Found"),
			@ApiResponse(responseCode = "405", description = "Method Not Allowed"),
			@ApiResponse(responseCode = "500", description = "Internal server error") })
	public void updateTitle(@PathVariable("titleId") String id, @RequestParam("title") String newtitle) {
		var optTitle = titleRepo.findById(id);
		if (optTitle.isPresent()) {
			var title = optTitle.get();
			title.setTitle(newtitle);
			titleRepo.save(title);
		} else
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Title Name Not Found!");
	}

	// DELETE TITLE
	@DeleteMapping("/titles/{titleId}")
	@Operation(summary = "Delete a title by ID", description = "Removes a title specified by its ID. If the ID exists, it deletes the title. If the ID does not exist, it throws a not found error")
	@ApiResponses(value = { @ApiResponse(responseCode = "400", description = "BAD REQUEST,invalid data passed"),
			@ApiResponse(responseCode = "200", description = "okay id found deleted the row in database"),
			@ApiResponse(responseCode = "404", description = "Title Id Not Found"),
			@ApiResponse(responseCode = "405", description = "Method Not Allowed"),
			@ApiResponse(responseCode = "500", description = "Internal server error") })
	public void deleteOneTitle(@PathVariable("titleId") String id) {
		var optTitle = titleRepo.findById(id);
		if (optTitle.isPresent())
			titleRepo.deleteById(id);
		else
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Title Id Not Found!");
	}

	// 7.List all titles by publisher
	@GetMapping("/titles/publishers/{pub_name}")
	@Operation(summary = "Retrieve titles with publishers", description = "Fetches and returns a list of titles with their respective publishers")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Retrieved ALl Titles Successful"),
			@ApiResponse(responseCode = "404", description = "pub_name Not Found"),
			@ApiResponse(responseCode = "500", description = "An issue occurred on the server while processing a request") })
	public List<Title> getTitlesByPublisherName(@PathVariable("pub_name") String pubName) {
		Optional<Publisher> publisher = publisherRepo.findByPubName(pubName);
		if (publisher != null) {
			return titleRepo.findByPublisher(publisher);
		} else {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No titles found in the specified publisher");
		}
	}

	// 8.List all titles by author
	@GetMapping("/titleAndAuthors")
	@Operation(summary = "Get all titles by author", description = "Retrieve a list of titles along with their associated authors")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Titles and authors found successfully"),
			@ApiResponse(responseCode = "404", description = "No titles found with associated authors") })
	public List<Object[]> displayTitleAndAuthors() {
		var listObj = titleRepo.findByTitleAndAuthor();
		if (listObj.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No titles found with associated authors");
		}
		return listObj;
	}

	// 9.List all titles that match the given title
	@GetMapping("/titlesbyname/{search}")
	@Operation(summary = "Retrieve titles by name", description = "Retrieves a list of titles that match the provided title name or part of the title name")
	public List<Title> getTitlesByName(@PathVariable("search") String title) {
		return titleRepo.findTitlesByTitleContaining(title);
	}

	// 10.List all titles by the range of price(min and max price)
	@GetMapping("/titlesbyprice")
	@Operation(summary = "Retrieve titles by price range", description = "Fetches and returns a list of titles falling within a specified price range")
	public List<Title> getTitlesByPriceRange(@RequestParam("minPrice") double minPrice,
			@RequestParam("maxPrice") double maxPrice) {
		return titleRepo.findByPriceBetween(minPrice, maxPrice);
	}

	// 11.List top 5 titles by ytd_sales
	@GetMapping("/top5titles")
	@Operation(summary = "Retrieve top 5 titles by ytd_sales", description = "Fetches and returns the top 5 titles based on ytd_sales")
	public List<Title> listTop5Titles() {
		return titleRepo.getTop5TitlesByYtdSales();
	}

	// 13.list details of a title by its id.Details
	// are:title,price,ytd_sales,auNames,pubNames
	@GetMapping("/detailsoftitle")
	@Operation(summary = "Get details of a title by its ID", description = " Retrieves specific details of a title (title, price, year-to-date sales, author names, and publisher names) based on the provided title ID")
	public List<TitleDetailsDTO> displayTitleDetails(@RequestParam("titleId") String titleId) {
		var title = titleRepo.findAllDetailsByTitle(titleId);
		return title;

	}

}
