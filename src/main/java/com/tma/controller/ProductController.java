package com.tma.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/products")
public class ProductController {

//    @PostMapping
//    @PreAuthorize("hasAuthority('PRODUCT_CREATE')")
//    public ResponseEntity<?> create() {
//
//        return ResponseEntity.status(HttpStatus.OK).body(
//                new ResponseModel<>(false, "Create product successfully", null));
//    }
//
//    @PutMapping
//    @PreAuthorize("hasAuthority('PRODUCT_EDIT')")
//    public ResponseEntity<?> update() {
//
//        return ResponseEntity.status(HttpStatus.OK).body(
//                new ResponseModel<>(false, "Update product successfully", null));
//    }
//
//    @DeleteMapping
//    @PreAuthorize("hasAuthority('PRODUCT_DELETE')")
//    public ResponseEntity<?> delete() {
//
//        return ResponseEntity.status(HttpStatus.OK).body(
//                new ResponseModel<>(false, "Delete product successfully", null));
//    }
}
