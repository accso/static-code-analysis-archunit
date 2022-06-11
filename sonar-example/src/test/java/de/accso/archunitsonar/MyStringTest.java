package de.accso.archunitsonar;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class MyStringTest {

    @Test
    void testConcat() {
        assertThat(new MyString().concat("abc", "def")).isEqualTo("abcdef");
    }
}