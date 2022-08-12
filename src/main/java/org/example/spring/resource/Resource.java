package org.example.spring.resource;

import java.io.IOException;
import java.io.InputStream;

public interface Resource {
    InputStream getInputStream() throws IOException;
}
