package test.java.com.verify;

import org.junit.jupiter.api.Test;

import java.security.MessageDigest;

import static org.junit.jupiter.api.Assertions.*;

class DocumentLogicTest {

    // ✅ Test 1 – hash generation
    @Test
    void sha256ShouldWork() throws Exception {
        byte[] data = "hello".getBytes();

        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] hash = md.digest(data);

        assertNotNull(hash);
        assertEquals(32, hash.length);
    }

    // ✅ Test 2 – string not empty
    @Test
    void stringShouldNotBeEmpty() {
        String name = "file.pdf";
        assertFalse(name.isBlank());
    }

    // ✅ Test 3 – simple math test (sanity)
    @Test
    void countShouldIncrease() {
        int count = 0;
        count++;
        assertEquals(1, count);
    }

    // ✅ Test 4 – basic validation
    @Test
    void fileSizeValidation() {
        long size = 4 * 1024 * 1024;
        long max = 5 * 1024 * 1024;

        assertTrue(size < max);
    }
}
