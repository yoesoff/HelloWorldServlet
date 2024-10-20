package com.mhyusuf.api;

import jakarta.ws.rs.ApplicationPath;
import org.glassfish.jersey.server.ResourceConfig;

@ApplicationPath("/api")
public class JerseyConfig extends ResourceConfig {
    public JerseyConfig() {
        packages("com.mhyusuf.api"); // Sesuaikan dengan package tempat resource REST Anda
    }
}

