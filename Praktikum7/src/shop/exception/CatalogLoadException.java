package shop.exception;

/** Thrown when the catalog file cannot be read; always wraps the original cause. */
public class CatalogLoadException extends ShopException {
    private static final long serialVersionUID = 1L;

    public CatalogLoadException(String message, Throwable cause) {
        super(message, cause);
    }
}
