package ch.puzzle.marinabackend.employee;

import ch.puzzle.marinabackend.security.SecurityTestUtils;
import ch.puzzle.marinabackend.security.User;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class EmployeeTest {

    @Test
    public void shouldCreateEmployeeInstanceByUser() {
        // given
        User u = SecurityTestUtils.getTestUser();

        // when
        Employee e = new Employee(u);

        // then
        assertEquals(e.getUsername(), u.getUsername());
        assertEquals(e.getId(), null);
        assertEquals(e.getFirstName(), u.getFirstName());
        assertEquals(e.getLastName(), u.getLastName());
        assertEquals(e.getEmail(), u.getEmail());
        assertEquals(e.getBruttoSalary(), null);
        assertEquals(e.getCreatedDate(), null);
        assertEquals(e.getModifiedDate(), null);
    }

}
