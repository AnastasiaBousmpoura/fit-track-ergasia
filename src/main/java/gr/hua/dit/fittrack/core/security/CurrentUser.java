package gr.hua.dit.fittrack.core.security;

import gr.hua.dit.fittrack.core.model.entity.Role;

public record CurrentUser(
        long id,
        String emailAddress,
        Role role
) {}