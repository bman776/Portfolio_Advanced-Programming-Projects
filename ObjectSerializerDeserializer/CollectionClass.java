import java.util.ArrayList;
import java.util.Arrays;

public class CollectionClass {
    private ArrayList<CompositeClass> attribute_a1;

    CollectionClass() {
        attribute_a1 = new ArrayList<CompositeClass>(3);
    }

    CollectionClass(CompositeClass[] inputArray) {
        attribute_a1 = new ArrayList<>(Arrays.asList(inputArray));
    }
}
