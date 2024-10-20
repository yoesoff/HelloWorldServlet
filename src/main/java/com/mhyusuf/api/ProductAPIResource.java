package com.mhyusuf.api;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.*;

// Model sederhana untuk produk
class Product {
    private int id;
    private String name;
    private double price;

    // Constructors, getters, and setters
    public Product() {}

    public Product(int id, String name, double price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}

@Path("/products")
public class ProductAPIResource {

    // Simpanan sementara produk dalam memori (seperti database sederhana)
    private static Map<Integer, Product> productDB = new HashMap<>();
    static {
        productDB.put(1, new Product(1, "Laptop", 1000.0));
        productDB.put(2, new Product(2, "Smartphone", 700.0));
    }

    // GET - Ambil semua produk
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllProducts() {
        List<Product> products = new ArrayList<>(productDB.values());
        return Response.ok(products).build();
    }

    // POST - Tambahkan produk baru
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addProduct(Product newProduct) {
        if (newProduct.getId() == 0 || newProduct.getName() == null || newProduct.getPrice() == 0.0) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Product ID, name, and price must be provided.").build();
        }
        productDB.put(newProduct.getId(), newProduct);
        return Response.status(Response.Status.CREATED).entity(newProduct).build();
    }

    // PUT - Perbarui produk berdasarkan ID
    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateProduct(@PathParam("id") int id, Product updatedProduct) {
        Product existingProduct = productDB.get(id);
        if (existingProduct == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Product not found.").build();
        }
        updatedProduct.setId(id); // Pastikan ID tetap sama
        productDB.put(id, updatedProduct);
        return Response.ok(updatedProduct).build();
    }

    // PATCH - Perbarui sebagian data produk
    @PATCH
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response patchProduct(@PathParam("id") int id, Map<String, Object> updates) {
        Product existingProduct = productDB.get(id);
        if (existingProduct == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Product not found.").build();
        }

        // Memperbarui hanya field yang disediakan di request
        if (updates.containsKey("name")) {
            existingProduct.setName((String) updates.get("name"));
        }
        if (updates.containsKey("price")) {
            existingProduct.setPrice(Double.parseDouble(updates.get("price").toString()));
        }

        // Masukkan kembali produk yang diperbarui ke dalam database
        productDB.put(id, existingProduct);
        return Response.ok(existingProduct).build();
    }
}
