public interface ISimpleChat extends AutoCloseable{
    public static final int SERVER_PORT = 45678;
    public static final int BUFFER_SIZE = 4096;

    void client() throws ChatException;
    void server() throws ChatException;
    String getMessage() throws ChatException;
    void sendMessage(String message) throws ChatException;

    @Override
    public void close() throws ChatException;
}
