package com.opentrace.server.utils.parsers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RolePermissionParserTest {

    private RolePermissionParser parser;

    @BeforeEach
    void setUp() {
        parser = new RolePermissionParser();
    }

    @Test
    @DisplayName("Should parse multiple roles, trim spaces and convert to uppercase")
    void shouldParseRolesCorrectly() {

        String rolesInput = "user, admin, worker ";

        List<String> result = parser.parseRoles(rolesInput);

        assertEquals(3, result.size());
        assertTrue(result.contains("USER"));
        assertTrue(result.contains("ADMIN"));
        assertTrue(result.contains("WORKER"));

        assertFalse(result.contains(" admin"));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"  ", "\t", "\n"})
    @DisplayName("Should return default REQUESTER role for null, empty or blank strings")
    void shouldReturnDefaultRoleForEmptyInput(String input) {

        List<String> result = parser.parseRoles(input);

        assertEquals(1, result.size());
        assertEquals("REQUESTER", result.get(0));
    }

    @Test
    @DisplayName("Should handle single role without commas")
    void shouldHandleSingleRole() {

        List<String> result = parser.parseRoles("moderator");

        assertEquals(List.of("MODERATOR"), result);
    }
}