package request;

import com.google.gson.JsonObject;
import model.UserData;
import model.AuthData;

public record RegisterRequest(String username, String password, String email) {
}