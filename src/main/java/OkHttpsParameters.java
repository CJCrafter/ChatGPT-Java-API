
/**
 * This class allows to define the HTTP parameters used by OkHttp to talk to
 * OpenAI infrastructure.
 * 
 * @author Nicolas de Pomereu
 *
 */
public class OkHttpsParameters {

    public static final int OK_HTTP_DEFAULT_READ_TIMEOUT_SECONDS = 300;
    public static final int OK_HTTP_DEFAULT_CONNECT_TIMEOUT_SECONDS = 300;
    public static final int OK_HTTP_DEFAULT_WRITE_TIMEOUT_SECONDS = 300;

    private int OkHttpReadTimeoutSeconds = OK_HTTP_DEFAULT_READ_TIMEOUT_SECONDS;
    private int OkHttpConnecTimeoutSeconds = OK_HTTP_DEFAULT_CONNECT_TIMEOUT_SECONDS;
    private int OkHttpWriteTimeoutSeconds = OK_HTTP_DEFAULT_WRITE_TIMEOUT_SECONDS;

    /**
     * Constructor
     * 
     * @param okHttpReadTimeoutSeconds   the HTTP read timeout in seconds - defaults
     *                                   to 300 seconds
     * @param okHttpConnecTimeoutSeconds the HTTP connect timeout in seconds -
     *                                   defaults to 300 seconds
     * @param okHttpWriteTimeoutSeconds  the HTTP connect timeout in seconds
     *                                   defaults to 300 seconds
     */

    public OkHttpsParameters(int okHttpReadTimeoutSeconds, int okHttpConnecTimeoutSeconds,
	    int okHttpWriteTimeoutSeconds) {
	OkHttpReadTimeoutSeconds = okHttpReadTimeoutSeconds;
	OkHttpConnecTimeoutSeconds = okHttpConnecTimeoutSeconds;
	OkHttpWriteTimeoutSeconds = okHttpWriteTimeoutSeconds;
    }

    /**
     * Returns the HTTP read timeout in seconds
     * 
     * @return the HTTP read timeout in seconds
     */
    public int getOkHttpReadTimeoutSeconds() {
	return OkHttpReadTimeoutSeconds;
    }

    /**
     * Returns the HTTP connect timeout in seconds
     * 
     * @return the HTTP connect timeout in seconds
     */
    public int getOkHttpConnecTimeoutSeconds() {
	return OkHttpConnecTimeoutSeconds;
    }

    /**
     * Returns the HTTP write timeout in seconds
     * 
     * @return the HTTP write timeout in seconds
     */
    public int getOkHttpWriteTimeoutSeconds() {
	return OkHttpWriteTimeoutSeconds;
    }

    @Override
    public String toString() {
	return "OkHttpsParameters [OkHttpReadTimeoutSeconds=" + OkHttpReadTimeoutSeconds
		+ ", OkHttpConnecTimeoutSeconds=" + OkHttpConnecTimeoutSeconds + ", OkHttpWriteTimeoutSeconds="
		+ OkHttpWriteTimeoutSeconds + "]";
    }

}
