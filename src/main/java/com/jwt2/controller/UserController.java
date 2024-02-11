package com.jwt2.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize; 
import org.springframework.security.authentication.AuthenticationManager; 
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken; 
import org.springframework.security.core.Authentication; 
import org.springframework.security.core.userdetails.UsernameNotFoundException; 
import org.springframework.web.bind.annotation.*;


import com.jwt2.entity.AuthRequest;
import com.jwt2.entity.BookRequest;
import com.jwt2.entity.UserInfo;
import com.jwt2.service.BookService;
import com.jwt2.service.JwtService;
import com.jwt2.service.UserInfoService;


import java.io.FileWriter;
import java.io.IOException;
import java.time.Year;

import java.util.List;

import java.util.*;

@RestController
@RequestMapping("/auth") 
public class UserController { 

	@Autowired
	private UserInfoService service; 

	@Autowired
	private JwtService jwtService; 

	@Autowired
	private AuthenticationManager authenticationManager; 

	@Autowired
    private BookService bookService;

	private static final String REGULAR_USER_CSV_FILE = "regularUser.csv";

	

	@PostMapping("/addNewUser") 
	public String addNewUser(@RequestBody UserInfo userInfo) { 
		return service.addUser(userInfo); 
	} 

	@GetMapping("/user/userProfile") 
	@PreAuthorize("hasAuthority('ROLE_USER')") 
	public String userProfile() { 
		return "Welcome to User Profile"; 
	} 

	@GetMapping("/admin/adminProfile") 
	@PreAuthorize("hasAuthority('ROLE_ADMIN')") 
	public String adminProfile() { 
		return "Welcome to Admin Profile"; 
	} 

	@PostMapping("/login") 
	public String authenticateAndGetToken(@RequestBody AuthRequest authRequest) { 
		Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())); 
		if (authentication.isAuthenticated()) { 
			return jwtService.generateToken(authRequest.getUsername()); 
		} else { 
			throw new UsernameNotFoundException("invalid user request !"); 
		} 
	} 

	@GetMapping("/admin/home")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public List<String> adminBooks() throws IOException, java.io.IOException {
        return bookService.getAdminBooks();
    }

    @GetMapping("/user/home")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public List<String> userBooks() throws IOException, java.io.IOException {
        return bookService.getUserBooks();
    }

	@PostMapping("/admin/addBook")
	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<String> addBook(@RequestBody BookRequest bookRequest) {
        if (!isValidBookRequest(bookRequest)) {
            return ResponseEntity.badRequest().body("Invalid book request.");
        }

        try {
            addBookToCSV(bookRequest);
            return ResponseEntity.ok("Book added successfully.");
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to add book.");
        }
    }

    private boolean isValidBookRequest(BookRequest bookRequest) {
        return isValidString(bookRequest.getBookName()) &&
               isValidString(bookRequest.getAuthor()) &&
               isValidPublicationYear(bookRequest.getPublicationYear());
    }

    private boolean isValidString(String value) {
        return value != null && !value.trim().isEmpty();
    }

    private boolean isValidPublicationYear(int year) {
        int currentYear = Year.now().getValue();
        return year >= 1000 && year <= currentYear;
    }

    public void addBookToCSV(BookRequest bookRequest) throws IOException {
        try (FileWriter writer = new FileWriter(new ClassPathResource(REGULAR_USER_CSV_FILE).getFile(), true)) {
            String newRecord = String.join(",", bookRequest.getBookName(), bookRequest.getAuthor(), String.valueOf(bookRequest.getPublicationYear()));
            writer.write(newRecord + System.lineSeparator());
        }
    }

	@DeleteMapping("/admin/deleteBook")
	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<String> deleteBook(@RequestParam String bookName) {
        if (!isValidBookName(bookName)) {
            return ResponseEntity.badRequest().body("Invalid book name.");
        }

        try {
            bookService.deleteBookFromCSV(bookName);
            return ResponseEntity.ok("Book deleted successfully.");
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete book.");
        }
    }

    private boolean isValidBookName(String bookName) {
        return bookName != null && !bookName.trim().isEmpty();
    }
} 
