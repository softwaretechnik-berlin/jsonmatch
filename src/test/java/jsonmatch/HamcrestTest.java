package jsonmatch;

import org.junit.Ignore;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class HamcrestTest {

    public static class Person {
        public final String name;
        public final int age;

        public Person(String name, int age) {
            this.name = name;
            this.age = age;
        }

        public String getName() {
            return name;
        }

        public int getAge() {
            return age;
        }
    }


    @Test
    public void aSingleObject() {
        Person matchee = new Person("John", 18);

        assertThat(matchee, allOf(
                hasProperty("name", equalTo("John")),
                hasProperty("age", greaterThan(1))
        ));
    }


    @Test
    @Ignore
    public void failure() {
        Person matchee = new Person("Jane", 18);

        assertThat(matchee, allOf(
                hasProperty("name", equalTo("John")),
                hasProperty("age", greaterThan(1))
        ));
    }


    @Test
    @Ignore
    public void onlyOneProperty() {
        Person person = new Person("John", 18);

        assertThat(person, allOf(
                hasProperty("name", equalTo("Jahn"))
        ));
    }

    @Test
    public void andACollection() {
        List<Person> matchee = List.of(
                new Person("John", 18),
                new Person("Jane", 23)
        );

        assertThat(matchee,
            hasItem(
                hasProperty("name", equalTo("John"))
            )
        );
    }

    @Test
    public void mismatchACollection() {
        List<Person> matchee = List.of(
                new Person("Jamie", 18),
                new Person("Jane", 23)
        );

//        assertThat(matchee,
//            hasItem(
//                hasProperty("name", equalTo("John"))
//            )
//        );
    }


}
