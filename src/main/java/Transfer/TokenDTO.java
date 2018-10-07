package Transfer;

@SuppressWarnings("unused")
public class TokenDTO {
    private int id;
    private String token;

    public TokenDTO(int id, String token) {
        this.id = id;
        this.token = token;
    }

    public int getID() {
        return id;
    }

    public String getToken() {
        return token;
    }
}
