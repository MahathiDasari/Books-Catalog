package books.project.entities.repo.controller;

import java.util.List;

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

import books.project.entities.Store;
import books.project.entities.repo.StoreRepo;
import books.project.entities.repo.DTO.StoreAndTitlesSold;
import books.project.entities.repo.DTO.StoreIdAndTitleDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;

@RestController

public class StoreController {

	@Autowired
	StoreRepo storeRepo;

	// GET ALL STORES
	@GetMapping("/stores")
	@Operation(summary = "Retrieves a list of all stores", description = "returns a collection of all available stores")
	public List<Store> getAllStores() {
		return storeRepo.findAll();
	}

	// ADD STORE
	@PostMapping("/addstore")
	@Operation(summary = "Adds a new store", description = "Allows the addition of a new store to the database. If the store ID doesn't exist, it saves the new store. If the store ID already exists, it throws a bad request error")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "inserted successfully"),
			@ApiResponse(responseCode = "400", description = "BAD REQUEST,invalid data passed"),
			@ApiResponse(responseCode = "405", description = "Method Not Allowed"),
			@ApiResponse(responseCode = "500", description = "Internal server error") })
	public void addStore(@Valid @RequestBody Store store) {
		var optStore = storeRepo.findById(store.getStoreId());
		try {
			if (optStore.isEmpty()) {
				storeRepo.save(store);
			} else
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Already Existed");
		} catch (DataAccessException ex) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
		}
	}

	// UPDATE STORE
	@PutMapping("/stores/{storeId}")
	@Operation(summary = "Update an existing store", description = "Updates the store specified by its ID with a new location. If the provided ID exists in the database, it updates the store's location. If the ID does not exist, it throws a not found error")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Updated successfully"),
			@ApiResponse(responseCode = "400", description = "BAD REQUEST,already existed or invalid data passed"),
			@ApiResponse(responseCode = "404", description = "Location Name Not Found"),
			@ApiResponse(responseCode = "405", description = "Method Not Allowed"),
			@ApiResponse(responseCode = "500", description = "Internal server error") })
	public void updateStore(@PathVariable("storeId") String id, @RequestParam("location") String newlocation) {
		var optStore = storeRepo.findById(id);
		if (optStore.isPresent()) {
			var store = optStore.get();
			store.setLocation(newlocation);
			storeRepo.save(store);
		} else
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Location Not Found!");
	}

	// DELETE STORE
	@DeleteMapping("/stores/{storeId}")
	@Operation(summary = "Delete a store by ID", description = "Removes a store specified by its ID. If the ID exists, it deletes the store. If the ID does not exist, it throws a not found error")
	@ApiResponses(value = { @ApiResponse(responseCode = "400", description = "invalid data passed"),
			@ApiResponse(responseCode = "200", description = "okay id found deleted the row in database"),
			@ApiResponse(responseCode = "404", description = "Id Not Found"),
			@ApiResponse(responseCode = "405", description = "Method Not Allowed"),
			@ApiResponse(responseCode = "500", description = "Internal server error") })
	public void deleteOneStore(@PathVariable("storeId") String id) {
		var optStore = storeRepo.findById(id);
		if (optStore.isPresent())
			storeRepo.deleteById(id);
		else
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Store Id Not Found!");
	}

	// 12 List top 5 stores by total titles sold
	@GetMapping("/listtop5stores")
	@Operation(summary = "Retrieve the top 5 stores by total titles sold", description = "Fetches and returns the top 5 stores based on the total number of titles sold")
	public List<StoreAndTitlesSold> listStoresByTitlesSold() {
		return storeRepo.getTop5StoresByTitlesSold();
	}

	// 14 Take store id and list all titles sold there
	@CrossOrigin
	@GetMapping("/store/listStoreIdAndTitle")
	@Operation(summary = "Get store ID and title details", description = "Retrieve store ID and title details based on storeId.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Store ID and title details retrieved successfully"),
			@ApiResponse(responseCode = "400", description = "Bad request - Invalid or missing storeId parameter"),
			@ApiResponse(responseCode = "500", description = "Internal Server Error") })
	public List<StoreIdAndTitleDTO> displayStoreIdAndTitle(
			@Parameter(description = "ID of the store to fetch details for", required = true) @RequestParam("storeId") String storeId) {
		if (storeId == null || storeId.isEmpty()) {
			throw new IllegalArgumentException("Invalid or missing storeId parameter");
		}
		if (!storeId.matches("^S\\d{3}$")) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					" Invalid Store id ,It should start with 'S' followed by 3 digits");
		}
		var listObj = storeRepo.findByStoreIdAndTitle(storeId);
		return listObj;
	}

}
