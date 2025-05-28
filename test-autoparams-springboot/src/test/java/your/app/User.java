package your.app;

import java.util.UUID;

public record User(UUID id, String email, String username) {
}
