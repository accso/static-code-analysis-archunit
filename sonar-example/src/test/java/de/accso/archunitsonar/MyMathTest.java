package de.accso.archunitsonar;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class MyMathTest {

    @Test
    void testAdd() {
        assertThat(new MyMath().add(5,4)).isEqualTo(9);
    }

    @Test
    void testMultiply() {
        assertThat(new MyMath().multiply(5,4)).isEqualTo(20);
    }
}