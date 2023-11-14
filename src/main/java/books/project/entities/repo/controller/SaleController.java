package books.project.entities.repo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import books.project.entities.Sale;
import books.project.entities.Salecpk;
import books.project.entities.repo.SaleRepo;
import books.project.entities.repo.DTO.SaleByTitleDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;

@RestController

public class SaleController {

	@Autowired
	SaleRepo saleRepo;

	// GET ALL SALES
	@GetMapping("/sales")
	@Operation(summary = "Retrieves a list of all sales", description = "returns a collection of all available sales")
	public List<Sale> getAllSales() {
		return saleRepo.findAll();
	}

	// ADD SALE
	@PostMapping("/addsale")
	@Operation(summary = "Adds a new sale", description = "Allows the addition of a new sale. It checks if the sale ID is already present, and if not, it saves the new sale. If the sale ID already exists, it throws a bad request error")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "inserted successfully"),
			@ApiResponse(responseCode = "400", description = "BAD REQUEST,invalid data passed"),
			@ApiResponse(responseCode = "405", description = "Method Not Allowed"),
			@ApiResponse(responseCode = "500", description = "Internal server error") })
	public Sale addSale(@Valid @RequestBody Sale sale) {
		try {
			var optSale = saleRepo.findById(sale.getKey());
			if (optSale.isPresent())
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Sale Id is already present!");
			saleRepo.save(sale);
			return sale;
		} catch (DataAccessException ex) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
		}
	}

	// UPDATE SALE
	@PutMapping("/update/sale")
	@Operation(summary = "Update an existing sale", description = "Updates the sale specified by its ID. If the provided sale ID exists in the database, it updates the quantity of items sold. If the ID does not exist, it throws a not found error")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Updated successfully"),
			@ApiResponse(responseCode = "400", description = "BAD REQUEST,already existed or invalid data passed"),
			@ApiResponse(responseCode = "404", description = "sale id Not Found"),
			@ApiResponse(responseCode = "405", description = "Method Not Allowed"),
			@ApiResponse(responseCode = "500", description = "Internal server error") })
	public Sale updateStore(@RequestBody Sale sale) {
		try {
			var optSale = saleRepo.findById(sale.getKey());
			if (!optSale.isPresent())
				throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Sale Id is not found!");
			var sale1 = optSale.get();
			sale1.setQtySold(sale.getQtySold());
			saleRepo.save(sale1);
			return sale1;
		} catch (DataAccessException ex) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
		}
	}

	// DELETE SALE
	@DeleteMapping("/delete/store/title")
	@Operation(summary = "Delete a sale by store and title", description = "Removes a sale specified by its store and title. If the sale ID exists, it deletes the sale. If the sale ID does not exist, it throws a bad request error")
	@ApiResponses(value = { @ApiResponse(responseCode = "400", description = "invalid data passed"),
			@ApiResponse(responseCode = "200", description = "okay id found deleted the row in database"),
			@ApiResponse(responseCode = "404", description = "Id Not Found"),
			@ApiResponse(responseCode = "405", description = "Method Not Allowed"),
			@ApiResponse(responseCode = "500", description = "Internal server error") })
	public void deleteStore(@RequestBody Salecpk saleId) {
		try {
			if (saleRepo.findById(saleId).isPresent())
				saleRepo.deleteById(saleId);
			else
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Sale Id is not Found!");
		} catch (DataAccessException ex) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
		}
	}

	// 15 List sales across the stores for a given title
	@GetMapping("/salesByTitle")
	@Operation(summary = "Retrieve sales for a given title across stores", description = "Fetches and returns a list of sales for a specific title across various stores")
	public List<SaleByTitleDTO> displaySalesByTitle(@RequestParam("titleId") String titleId) {
		var listSales = saleRepo.findSalesByTitle(titleId);
		return listSales;
	}

}
