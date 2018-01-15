package li.company.java.client.model;

public interface SocketClient {

    /**
     * Sends a message to an SocketClient.
     *
     * @param message The message to be sent.
     */
    public void printMessage(String message);

    /**
     * Sends an error to an SocketClient.
     *
     * @param error The error-message to be sent.
     */
    public void printError(String error);

}
