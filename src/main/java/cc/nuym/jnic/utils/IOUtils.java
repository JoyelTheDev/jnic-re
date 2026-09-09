package cc.nuym.jnic.utils;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public final class IOUtils {
    public static byte[] readFullyWithoutClosing(final InputStream stream) throws IOException {
        final ByteArrayOutputStream result = new ByteArrayOutputStream();
        stream.transferTo(result);
        return result.toByteArray();
    }

    public static ByteArrayOutputStream readFully(final InputStream stream) throws IOException {
        try (stream) {
            final ByteArrayOutputStream result = new ByteArrayOutputStream();
            stream.transferTo(result);
            return result;
        }
    }

    public static byte[] readFullyAsByteArray(final InputStream stream) throws IOException {
        return readFully(stream).toByteArray();
    }

    public static String readFullyAsString(final InputStream stream) throws IOException {
        return readFully(stream).toString(StandardCharsets.UTF_8);
    }

    public static String readFullyAsString(final InputStream stream, final Charset charset) throws IOException {
        return readFully(stream).toString(charset);
    }

    public static void write(final String text, final OutputStream outputStream) throws IOException {
        outputStream.write(text.getBytes(StandardCharsets.UTF_8));
    }

    public static void write(final byte[] bytes, final OutputStream outputStream) throws IOException {
        new ByteArrayInputStream(bytes).transferTo(outputStream);
    }

    public static void copyTo(final InputStream src, final OutputStream dest) throws IOException {
        src.transferTo(dest);
    }

    public static void copyTo(final InputStream src, final OutputStream dest, final byte[] buf) throws IOException {
        src.transferTo(dest);
    }
}
