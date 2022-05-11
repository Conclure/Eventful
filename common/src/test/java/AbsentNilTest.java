import me.conclure.eventful.shared.nullability.AbsentNil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.lang.reflect.Constructor;
import java.util.function.Supplier;

public class AbsentNilTest {
    AbsentNil<Object> nil;

    @BeforeEach
    void setUp() throws ReflectiveOperationException {
        Constructor<AbsentNil.Impl> constructor = AbsentNil.Impl.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        this.nil = constructor.newInstance();
    }

    @Test
    void assertAbsent_doesNotThrow_AssertionError() {
        Assertions.assertDoesNotThrow(() -> {
            this.nil.assertAbsent();
        });
    }

    @Test()
    void assertPresent_doesThrow_AssertionError() {
        Assertions.assertThrowsExactly(AssertionError.class,() -> {
           this.nil.assertPresent();
        });
    }

    @Test
    void value_returns_null() {
        Assertions.assertNull(this.nil.value());
    }
    
    @Test
    void orValue_returnsArgument_givenNonNullArgument() {
        Object mock = Mockito.mock(Object.class);
        Assertions.assertSame(mock,this.nil.orValue(mock));
    }

    @Test
    void orGetValue_returnsValueFromSupplier_givenNonNullSupplierAndSupplierValue() {
        Object mock = Mockito.mock(Object.class);
        Supplier mockSupplier = Mockito.mock(Supplier.class);
        Mockito.when(mockSupplier.get()).thenReturn(mock);
        Assertions.assertSame(mock,this.nil.orGetValue(mockSupplier));
    }
}
